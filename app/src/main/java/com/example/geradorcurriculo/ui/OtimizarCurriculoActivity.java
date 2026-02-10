package com.example.geradorcurriculo.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import com.example.geradorcurriculo.R;
import com.example.geradorcurriculo.model.Curriculo;
import com.example.geradorcurriculo.database.DatabaseHelper;
import com.example.geradorcurriculo.services.GeminiService;
import com.example.geradorcurriculo.utils.GeminiConfigManager;
import java.util.List;

public class OtimizarCurriculoActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private CardView cardScoreAts, cardOtimizacoes, cardPalavrasChave, cardErros;
    private TextView textScore, textScoreDescricao, textOtimizacoes, textPalavrasChave, textErros;
    private Button buttonIniciarOtimizacao, buttonAplicarMudancas;
    
    private long curriculoId;
    private Curriculo curriculo;
    private Curriculo curriculoOtimizado;
    private DatabaseHelper databaseHelper;
    private GeminiService geminiService;
    private GeminiConfigManager configManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otimizar_curriculo);

        databaseHelper = new DatabaseHelper(this);
        geminiService = new GeminiService(this);
        configManager = new GeminiConfigManager(this);
        
        curriculoId = getIntent().getLongExtra("curriculo_id", -1);
        
        initViews();
        setupToolbar();
        setupClickListeners();
        carregarCurriculo();
    }

    private void initViews() {
        progressBar = findViewById(R.id.progress_bar);
        cardScoreAts = findViewById(R.id.card_score_ats);
        cardOtimizacoes = findViewById(R.id.card_otimizacoes);
        cardPalavrasChave = findViewById(R.id.card_palavras_chave);
        cardErros = findViewById(R.id.card_erros);
        textScore = findViewById(R.id.text_score);
        textScoreDescricao = findViewById(R.id.text_score_descricao);
        textOtimizacoes = findViewById(R.id.text_otimizacoes);
        textPalavrasChave = findViewById(R.id.text_palavras_chave);
        textErros = findViewById(R.id.text_erros);
        buttonIniciarOtimizacao = findViewById(R.id.button_iniciar_otimizacao);
        buttonAplicarMudancas = findViewById(R.id.button_aplicar_mudancas);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Otimização com IA");
        }
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setupClickListeners() {
        buttonIniciarOtimizacao.setOnClickListener(v -> iniciarOtimizacao());
        buttonAplicarMudancas.setOnClickListener(v -> aplicarMudancas());
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
                if (curriculo == null) {
                    Toast.makeText(this, "Currículo não encontrado", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }).start();
    }

    private void iniciarOtimizacao() {
        if (!configManager.isGeminiConfigured()) {
            new AlertDialog.Builder(this)
                .setTitle("IA não configurada")
                .setMessage("Configure sua chave API do Google Gemini nas configurações para usar recursos de IA.")
                .setPositiveButton("Configurar", (dialog, which) -> {
                    android.content.Intent intent = new android.content.Intent(this, ConfiguracoesActivity.class);
                    startActivity(intent);
                })
                .setNegativeButton("Cancelar", null)
                .show();
            return;
        }

        buttonIniciarOtimizacao.setEnabled(false);
        buttonIniciarOtimizacao.setText("Otimizando...");
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setIndeterminate(true);

        new Thread(() -> {
            try {
                // Etapa 1: Calcular score ATS
                runOnUiThread(() -> progressBar.setIndeterminate(false));
                int score = geminiService.calcularScoreATS(curriculo);
                
                runOnUiThread(() -> {
                    exibirScore(score);
                    progressBar.setProgress(25);
                });

                // Etapa 2: Verificar erros
                List<String> erros = geminiService.verificarErrosGramaticais(curriculo);
                
                runOnUiThread(() -> {
                    exibirErros(erros);
                    progressBar.setProgress(50);
                });

                // Etapa 3: Gerar palavras-chave
                String area = extrairAreaAtuacao(curriculo);
                String cargo = extrairCargo(curriculo);
                List<String> palavrasChave = geminiService.gerarPalavrasChaveATS(area, cargo);
                
                runOnUiThread(() -> {
                    exibirPalavrasChave(palavrasChave);
                    progressBar.setProgress(75);
                });

                // Etapa 4: Otimizar currículo
                curriculoOtimizado = geminiService.otimizarCurriculoAutomaticamente(curriculo);
                
                runOnUiThread(() -> {
                    exibirOtimizacoes();
                    progressBar.setProgress(100);
                    progressBar.setVisibility(View.GONE);
                    buttonIniciarOtimizacao.setEnabled(true);
                    buttonIniciarOtimizacao.setText("🔄 Otimizar Novamente");
                    buttonAplicarMudancas.setVisibility(View.VISIBLE);
                    Toast.makeText(this, "Otimização concluída!", Toast.LENGTH_SHORT).show();
                });

            } catch (Exception e) {
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    buttonIniciarOtimizacao.setEnabled(true);
                    buttonIniciarOtimizacao.setText("🚀 Iniciar Otimização");
                    Toast.makeText(this, "Erro ao otimizar: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
            }
        }).start();
    }

    private void exibirScore(int score) {
        cardScoreAts.setVisibility(View.VISIBLE);
        textScore.setText(score + "/100");
        
        // Definir cor baseada no score
        int cor;
        String descricao;
        if (score >= 80) {
            cor = Color.parseColor("#4CAF50"); // Verde
            descricao = "Excelente! Seu currículo está muito bem otimizado para ATS.";
        } else if (score >= 60) {
            cor = Color.parseColor("#FF9800"); // Laranja
            descricao = "Bom! Algumas melhorias podem aumentar sua compatibilidade.";
        } else if (score >= 40) {
            cor = Color.parseColor("#FF5722"); // Vermelho claro
            descricao = "Regular. Recomendamos aplicar as otimizações sugeridas.";
        } else {
            cor = Color.parseColor("#F44336"); // Vermelho
            descricao = "Baixo. Seu currículo precisa de melhorias significativas.";
        }
        
        textScore.setTextColor(cor);
        textScoreDescricao.setText(descricao);
    }

    private void exibirErros(List<String> erros) {
        if (erros == null || erros.isEmpty() || 
            (erros.size() == 1 && erros.get(0).toLowerCase().contains("nenhum erro"))) {
            cardErros.setVisibility(View.GONE);
            return;
        }
        
        cardErros.setVisibility(View.VISIBLE);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < erros.size(); i++) {
            sb.append("• ").append(erros.get(i));
            if (i < erros.size() - 1) {
                sb.append("\n\n");
            }
        }
        textErros.setText(sb.toString());
    }

    private void exibirPalavrasChave(List<String> palavras) {
        if (palavras == null || palavras.isEmpty()) {
            cardPalavrasChave.setVisibility(View.GONE);
            return;
        }
        
        cardPalavrasChave.setVisibility(View.VISIBLE);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < palavras.size(); i++) {
            sb.append("• ").append(palavras.get(i));
            if (i < palavras.size() - 1) {
                sb.append("\n");
            }
        }
        textPalavrasChave.setText(sb.toString());
    }

    private void exibirOtimizacoes() {
        cardOtimizacoes.setVisibility(View.VISIBLE);
        
        StringBuilder sb = new StringBuilder();
        sb.append("✓ Resumo profissional otimizado\n");
        sb.append("✓ Experiências reescritas com verbos de ação\n");
        sb.append("✓ Resultados quantificados\n");
        sb.append("✓ Palavras-chave para ATS incluídas\n");
        sb.append("✓ Formatação compatível com sistemas de rastreamento\n");
        sb.append("✓ Linguagem profissional e objetiva\n");
        
        textOtimizacoes.setText(sb.toString());
    }

    private void aplicarMudancas() {
        new AlertDialog.Builder(this)
            .setTitle("Aplicar Otimizações")
            .setMessage("Deseja aplicar todas as otimizações sugeridas pela IA ao seu currículo? As alterações anteriores serão substituídas.")
            .setPositiveButton("Aplicar", (dialog, which) -> salvarCurriculoOtimizado())
            .setNegativeButton("Cancelar", null)
            .show();
    }

    private void salvarCurriculoOtimizado() {
        if (curriculoOtimizado == null) {
            Toast.makeText(this, "Nenhuma otimização disponível", Toast.LENGTH_SHORT).show();
            return;
        }

        buttonAplicarMudancas.setEnabled(false);
        buttonAplicarMudancas.setText("Salvando...");

        new Thread(() -> {
            // Manter ID e dados básicos
            curriculoOtimizado.setId(curriculo.getId());
            curriculoOtimizado.setNomeCompleto(curriculo.getNomeCompleto());
            curriculoOtimizado.setEmail(curriculo.getEmail());
            curriculoOtimizado.setTelefone(curriculo.getTelefone());
            curriculoOtimizado.setEndereco(curriculo.getEndereco());
            curriculoOtimizado.setLinkedin(curriculo.getLinkedin());
            curriculoOtimizado.setGithub(curriculo.getGithub());
            
            int resultado = databaseHelper.atualizarCurriculo(curriculoOtimizado);
            
            runOnUiThread(() -> {
                buttonAplicarMudancas.setEnabled(true);
                buttonAplicarMudancas.setText("Aplicar Mudanças");
                
                if (resultado > 0) {
                    Toast.makeText(this, "Currículo otimizado com sucesso!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Erro ao salvar otimizações", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }

    private String extrairAreaAtuacao(Curriculo curriculo) {
        // Tentar extrair da experiência ou formação
        if (curriculo.getFormacaoAcademicaTexto() != null && !curriculo.getFormacaoAcademicaTexto().isEmpty()) {
            return curriculo.getFormacaoAcademicaTexto().split("\n")[0];
        }
        return "Tecnologia";
    }

    private String extrairCargo(Curriculo curriculo) {
        // Tentar extrair da experiência
        if (curriculo.getExperienciaProfissionalTexto() != null && !curriculo.getExperienciaProfissionalTexto().isEmpty()) {
            return curriculo.getExperienciaProfissionalTexto().split("\n")[0];
        }
        return "Profissional";
    }
}
