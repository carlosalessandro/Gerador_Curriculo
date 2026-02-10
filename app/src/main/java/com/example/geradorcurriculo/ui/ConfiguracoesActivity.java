package com.example.geradorcurriculo.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import com.example.geradorcurriculo.R;
import com.example.geradorcurriculo.services.GeminiService;
import com.example.geradorcurriculo.utils.GeminiConfigManager;
import com.google.android.material.textfield.TextInputLayout;

public class ConfiguracoesActivity extends AppCompatActivity {

    private TextInputLayout inputGeminiKey;
    private Button buttonTestarConexao;
    private Button buttonSalvarKey;
    private Button buttonAbrirSite;
    private TextView textStatusConexao;
    
    private GeminiConfigManager configManager;
    private GeminiService geminiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes);

        configManager = new GeminiConfigManager(this);
        geminiService = new GeminiService(this);
        
        initViews();
        setupToolbar();
        setupClickListeners();
        carregarConfiguracao();
    }

    private void initViews() {
        inputGeminiKey = findViewById(R.id.input_gemini_key);
        buttonTestarConexao = findViewById(R.id.button_testar_conexao);
        buttonSalvarKey = findViewById(R.id.button_salvar_key);
        buttonAbrirSite = findViewById(R.id.button_abrir_site);
        textStatusConexao = findViewById(R.id.text_status_conexao);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Configurações");
        }
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setupClickListeners() {
        buttonSalvarKey.setOnClickListener(v -> salvarChave());
        buttonTestarConexao.setOnClickListener(v -> testarConexao());
        buttonAbrirSite.setOnClickListener(v -> abrirSiteGemini());
    }

    private void carregarConfiguracao() {
        if (configManager.isGeminiConfigured()) {
            // Mostrar apenas parte da chave por segurança
            String key = configManager.getGeminiKey();
            if (key != null && key.length() > 10) {
                String maskedKey = key.substring(0, 10) + "..." + key.substring(key.length() - 4);
                if (inputGeminiKey.getEditText() != null) {
                    inputGeminiKey.getEditText().setText(maskedKey);
                }
            }
            atualizarStatus(true, "✅ Chave API configurada");
        } else {
            atualizarStatus(false, "⚠️ Chave API não configurada");
        }
    }

    private void salvarChave() {
        if (inputGeminiKey.getEditText() == null) {
            return;
        }
        
        String apiKey = inputGeminiKey.getEditText().getText().toString().trim();
        
        if (apiKey.isEmpty()) {
            inputGeminiKey.setError("Digite a chave API");
            return;
        }
        
        // Verificar se não é a chave mascarada
        if (apiKey.contains("...")) {
            Toast.makeText(this, "Chave já está salva. Para alterar, digite uma nova chave.", Toast.LENGTH_LONG).show();
            return;
        }
        
        if (!configManager.isValidKeyFormat(apiKey)) {
            inputGeminiKey.setError("Formato de chave inválido");
            Toast.makeText(this, "A chave deve ter pelo menos 30 caracteres", Toast.LENGTH_SHORT).show();
            return;
        }
        
        buttonSalvarKey.setEnabled(false);
        buttonSalvarKey.setText("Salvando...");
        
        new Thread(() -> {
            boolean sucesso = configManager.saveGeminiKey(apiKey);
            
            runOnUiThread(() -> {
                buttonSalvarKey.setEnabled(true);
                buttonSalvarKey.setText("Salvar");
                
                if (sucesso) {
                    inputGeminiKey.setError(null);
                    Toast.makeText(this, "Chave salva com sucesso!", Toast.LENGTH_SHORT).show();
                    atualizarStatus(true, "✅ Chave API configurada");
                    
                    // Mascarar a chave exibida
                    if (apiKey.length() > 10) {
                        String maskedKey = apiKey.substring(0, 10) + "..." + apiKey.substring(apiKey.length() - 4);
                        if (inputGeminiKey.getEditText() != null) {
                            inputGeminiKey.getEditText().setText(maskedKey);
                        }
                    }
                } else {
                    Toast.makeText(this, "Erro ao salvar chave", Toast.LENGTH_SHORT).show();
                    atualizarStatus(false, "❌ Erro ao salvar chave");
                }
            });
        }).start();
    }

    private void testarConexao() {
        if (!configManager.isGeminiConfigured()) {
            Toast.makeText(this, "Configure a chave API primeiro", Toast.LENGTH_SHORT).show();
            return;
        }
        
        buttonTestarConexao.setEnabled(false);
        buttonTestarConexao.setText("Testando...");
        atualizarStatus(null, "🔄 Testando conexão...");
        
        new Thread(() -> {
            boolean sucesso = geminiService.testConnection();
            
            runOnUiThread(() -> {
                buttonTestarConexao.setEnabled(true);
                buttonTestarConexao.setText("Testar");
                
                if (sucesso) {
                    Toast.makeText(this, "Conexão bem-sucedida!", Toast.LENGTH_SHORT).show();
                    atualizarStatus(true, "✅ Conexão funcionando perfeitamente");
                } else {
                    Toast.makeText(this, "Falha na conexão. Verifique a chave.", Toast.LENGTH_LONG).show();
                    atualizarStatus(false, "❌ Falha na conexão - Verifique a chave");
                }
            });
        }).start();
    }

    private void abrirSiteGemini() {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://ai.google.dev"));
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "Não foi possível abrir o navegador", Toast.LENGTH_SHORT).show();
        }
    }

    private void atualizarStatus(Boolean sucesso, String mensagem) {
        textStatusConexao.setText(mensagem);
        textStatusConexao.setVisibility(View.VISIBLE);
        
        if (sucesso == null) {
            // Status neutro (testando)
            textStatusConexao.setBackgroundColor(
                ContextCompat.getColor(this, android.R.color.holo_blue_light)
            );
        } else if (sucesso) {
            // Sucesso
            textStatusConexao.setBackgroundColor(
                ContextCompat.getColor(this, android.R.color.holo_green_dark)
            );
        } else {
            // Erro
            textStatusConexao.setBackgroundColor(
                ContextCompat.getColor(this, android.R.color.holo_red_dark)
            );
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarConfiguracao();
    }
}
