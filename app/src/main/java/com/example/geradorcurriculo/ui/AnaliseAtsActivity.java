package com.example.geradorcurriculo.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.geradorcurriculo.R;
import com.example.geradorcurriculo.model.Curriculo;
import com.example.geradorcurriculo.database.DatabaseHelper;
import com.example.geradorcurriculo.services.ATSService;
import com.example.geradorcurriculo.services.AdvancedATSService;
import com.example.geradorcurriculo.ui.adapter.SuggestionAdapter;
import com.example.geradorcurriculo.utils.PremiumManager;
import com.google.android.material.snackbar.Snackbar;
import java.util.List;

public class AnaliseAtsActivity extends AppCompatActivity {

    private RecyclerView recyclerViewSuggestions;
    private TextView textScore;
    private TextView textScoreLabel;
    private TextView textCurriculoName;
    private TextView textPerfil;
    private TextView textInsights;
    private ProgressBar progressBar;
    private Button buttonAnalisar;
    private CardView cardResult;
    private CardView cardInsights;
    
    private long curriculoId;
    private Curriculo curriculo;
    private DatabaseHelper databaseHelper;
    private ATSService atsService;
    private AdvancedATSService advancedAtsService;
    private PremiumManager premiumManager;
    private SuggestionAdapter suggestionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analise_ats);

        databaseHelper = new DatabaseHelper(this);
        atsService = new ATSService();
        advancedAtsService = new AdvancedATSService(this);
        premiumManager = new PremiumManager(this);
        
        curriculoId = getIntent().getLongExtra("curriculo_id", -1);
        
        initViews();
        setupToolbar();
        setupClickListeners();
        carregarCurriculo();
    }

    private void initViews() {
        recyclerViewSuggestions = findViewById(R.id.recycler_view_suggestions);
        textScore = findViewById(R.id.text_score);
        textScoreLabel = findViewById(R.id.text_score_label);
        textCurriculoName = findViewById(R.id.text_curriculo_name);
        textPerfil = findViewById(R.id.text_perfil);
        textInsights = findViewById(R.id.text_insights);
        progressBar = findViewById(R.id.progress_bar);
        buttonAnalisar = findViewById(R.id.button_analisar);
        cardResult = findViewById(R.id.card_result);
        cardInsights = findViewById(R.id.card_insights);
        
        recyclerViewSuggestions.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Análise ATS");
        }
    }

    private void setupClickListeners() {
        buttonAnalisar.setOnClickListener(v -> realizarAnalise());
    }

    private void carregarCurriculo() {
        if (curriculoId == -1) {
            Toast.makeText(this, "Currículo não encontrado", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        new Thread(() -> {
            curriculo = databaseHelper.buscarCurriculoPorId(curriculoId);
            
            runOnUiThread(() -> {
                if (curriculo != null) {
                    textCurriculoName.setText(curriculo.getNomeCompleto());
                    if (curriculo.getAtsScore() > 0) {
                        mostrarResultadoExistente();
                    }
                } else {
                    Toast.makeText(this, "Currículo não encontrado", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }).start();
    }

    private void mostrarResultadoExistente() {
        textScore.setText(String.valueOf(curriculo.getAtsScore()));
        textScoreLabel.setText("Pontuação ATS");
        
        if (curriculo.getAtsSuggestions() != null && !curriculo.getAtsSuggestions().isEmpty()) {
            suggestionAdapter = new SuggestionAdapter(curriculo.getAtsSuggestions());
            recyclerViewSuggestions.setAdapter(suggestionAdapter);
        }
        
        cardResult.setVisibility(View.VISIBLE);
        buttonAnalisar.setText("Analisar Novamente");
    }

    private void realizarAnalise() {
        if (curriculo == null) {
            Toast.makeText(this, "Currículo não carregado", Toast.LENGTH_SHORT).show();
            return;
        }

        // Verificar se usuário pode usar análise avançada
        if (!premiumManager.canUseAdvancedAnalysis()) {
            premiumManager.showPremiumDialog(this, "Análise ATS Avançada");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        buttonAnalisar.setEnabled(false);

        new Thread(() -> {
            try {
                // Usar análise avançada se for premium, senão análise básica
                ATSService.ATSResult resultado;
                
                if (premiumManager.isPremium()) {
                    AdvancedATSService.EnhancedATSResult enhancedResult = 
                        advancedAtsService.analisarCurriculoAvancado(curriculo, "TI");
                    
                    resultado = enhancedResult;
                    
                    runOnUiThread(() -> {
                        mostrarResultadoAvancado((AdvancedATSService.EnhancedATSResult) resultado);
                    });
                } else {
                    resultado = atsService.analisarCurriculo(curriculo);
                    
                    runOnUiThread(() -> {
                        mostrarResultado(resultado);
                    });
                }
                
                // Incrementar uso da análise gratuita
                premiumManager.incrementAnalysisUsed();
                
                // Atualizar currículo com novos resultados
                curriculo.setAtsScore(resultado.getScore());
                curriculo.setAtsSuggestions(resultado.getSuggestions());
                atualizarCurriculo();
                
            } catch (Exception e) {
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    buttonAnalisar.setEnabled(true);
                    Toast.makeText(this, "Erro na análise: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
            }
        }).start();
    }

    private void mostrarResultado(ATSService.ATSResult resultado) {
        textScore.setText(String.valueOf(resultado.getScore()));
        textScoreLabel.setText("Pontuação ATS");
        
        if (resultado.getSuggestions() != null && !resultado.getSuggestions().isEmpty()) {
            suggestionAdapter = new SuggestionAdapter(resultado.getSuggestions());
            recyclerViewSuggestions.setAdapter(suggestionAdapter);
        }
        
        cardResult.setVisibility(View.VISIBLE);
        cardInsights.setVisibility(View.GONE);
        buttonAnalisar.setText("Analisar Novamente");
        
        String mensagem = resultado.getScore() >= 80 ? "Excelente! Seu currículo está bem otimizado." :
                         resultado.getScore() >= 60 ? "Bom! Seu currículo tem uma boa pontuação." :
                         resultado.getScore() >= 40 ? "Regular. Considere as sugestões para melhorar." :
                         "Baixo. Siga as sugestões para melhorar significativamente.";
        
        Snackbar.make(findViewById(android.R.id.content), mensagem, Snackbar.LENGTH_LONG).show();
    }

    private void mostrarResultadoAvancado(AdvancedATSService.EnhancedATSResult resultado) {
        mostrarResultado((ATSService.ATSResult) resultado);
        
        // Mostrar informações avançadas
        textPerfil.setText("Perfil: " + resultado.getPerfil());
        
        if (resultado.getInsights() != null && !resultado.getInsights().isEmpty()) {
            StringBuilder insightsText = new StringBuilder();
            for (String insight : resultado.getInsights()) {
                insightsText.append("• ").append(insight).append("\n");
            }
            textInsights.setText(insightsText.toString());
            cardInsights.setVisibility(View.VISIBLE);
        }
    }

    private void atualizarCurriculo() {
        new Thread(() -> {
            databaseHelper.atualizarCurriculo(curriculo);
        }).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Atualizar estado dos botões se status premium mudou
        if (buttonAnalisar != null) {
            buttonAnalisar.setEnabled(true);
        }
    }
}
