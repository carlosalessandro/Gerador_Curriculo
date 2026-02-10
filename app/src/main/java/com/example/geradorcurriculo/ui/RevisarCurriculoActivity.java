package com.example.geradorcurriculo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.geradorcurriculo.R;
import com.example.geradorcurriculo.model.Curriculo;
import com.example.geradorcurriculo.database.DatabaseHelper;
import com.example.geradorcurriculo.services.GeminiService;
import com.example.geradorcurriculo.utils.GeminiConfigManager;
import com.example.geradorcurriculo.utils.PremiumManager;
import com.google.android.material.textfield.TextInputLayout;
import java.util.List;

public class RevisarCurriculoActivity extends AppCompatActivity {

    private TextInputLayout inputNomeCompleto, inputEmail, inputTelefone, inputEndereco;
    private TextInputLayout inputLinkedin, inputGithub, inputObjetivo, inputResumoProfissional;
    private TextInputLayout inputExperiencia, inputFormacao, inputHabilidades;
    private Button buttonAnalisarIA, buttonCancelar, buttonSalvar;
    
    private DatabaseHelper databaseHelper;
    private PremiumManager premiumManager;
    private GeminiConfigManager geminiConfigManager;
    private GeminiService geminiService;
    private Curriculo curriculo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revisar_curriculo);

        databaseHelper = new DatabaseHelper(this);
        premiumManager = new PremiumManager(this);
        geminiConfigManager = new GeminiConfigManager(this);
        geminiService = new GeminiService(this);
        
        initViews();
        setupToolbar();
        setupClickListeners();
        carregarDadosImportados();
    }

    private void initViews() {
        inputNomeCompleto = findViewById(R.id.input_nome_completo);
        inputEmail = findViewById(R.id.input_email);
        inputTelefone = findViewById(R.id.input_telefone);
        inputEndereco = findViewById(R.id.input_endereco);
        inputLinkedin = findViewById(R.id.input_linkedin);
        inputGithub = findViewById(R.id.input_github);
        inputObjetivo = findViewById(R.id.input_objetivo);
        inputResumoProfissional = findViewById(R.id.input_resumo_profissional);
        inputExperiencia = findViewById(R.id.input_experiencia);
        inputFormacao = findViewById(R.id.input_formacao);
        inputHabilidades = findViewById(R.id.input_habilidades);
        buttonAnalisarIA = findViewById(R.id.button_analisar_ia);
        buttonCancelar = findViewById(R.id.button_cancelar);
        buttonSalvar = findViewById(R.id.button_salvar);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Revisar Currículo Importado");
        }
        toolbar.setNavigationOnClickListener(v -> confirmarCancelamento());
    }

    private void setupClickListeners() {
        buttonAnalisarIA.setOnClickListener(v -> analisarComIA());
        buttonCancelar.setOnClickListener(v -> confirmarCancelamento());
        buttonSalvar.setOnClickListener(v -> salvarCurriculo());
    }

    private void carregarDadosImportados() {
        curriculo = (Curriculo) getIntent().getSerializableExtra("curriculo");
        
        if (curriculo == null) {
            Toast.makeText(this, "Erro ao carregar dados importados", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        preencherCampos();
    }

    private void preencherCampos() {
        if (inputNomeCompleto.getEditText() != null) {
            inputNomeCompleto.getEditText().setText(curriculo.getNomeCompleto() != null ? curriculo.getNomeCompleto() : "");
        }
        if (inputEmail.getEditText() != null) {
            inputEmail.getEditText().setText(curriculo.getEmail() != null ? curriculo.getEmail() : "");
        }
        if (inputTelefone.getEditText() != null) {
            inputTelefone.getEditText().setText(curriculo.getTelefone() != null ? curriculo.getTelefone() : "");
        }
        if (inputEndereco.getEditText() != null) {
            inputEndereco.getEditText().setText(curriculo.getEndereco() != null ? curriculo.getEndereco() : "");
        }
        if (inputLinkedin.getEditText() != null) {
            inputLinkedin.getEditText().setText(curriculo.getLinkedin() != null ? curriculo.getLinkedin() : "");
        }
        if (inputGithub.getEditText() != null) {
            inputGithub.getEditText().setText(curriculo.getGithub() != null ? curriculo.getGithub() : "");
        }
        if (inputObjetivo.getEditText() != null) {
            inputObjetivo.getEditText().setText(curriculo.getObjetivo() != null ? curriculo.getObjetivo() : "");
        }
        if (inputResumoProfissional.getEditText() != null) {
            inputResumoProfissional.getEditText().setText(curriculo.getResumoProfissional() != null ? curriculo.getResumoProfissional() : "");
        }
        if (inputExperiencia.getEditText() != null) {
            inputExperiencia.getEditText().setText(curriculo.getExperienciaProfissionalTexto() != null ? curriculo.getExperienciaProfissionalTexto() : "");
        }
        if (inputFormacao.getEditText() != null) {
            inputFormacao.getEditText().setText(curriculo.getFormacaoAcademicaTexto() != null ? curriculo.getFormacaoAcademicaTexto() : "");
        }
        if (inputHabilidades.getEditText() != null) {
            inputHabilidades.getEditText().setText(curriculo.getHabilidadesTexto() != null ? curriculo.getHabilidadesTexto() : "");
        }
    }

    private boolean validarCampos() {
        boolean valido = true;
        
        if (inputNomeCompleto.getEditText() != null && inputNomeCompleto.getEditText().getText().toString().trim().isEmpty()) {
            inputNomeCompleto.setError("Nome completo é obrigatório");
            valido = false;
        } else {
            inputNomeCompleto.setError(null);
        }
        
        if (inputEmail.getEditText() != null) {
            String email = inputEmail.getEditText().getText().toString().trim();
            if (email.isEmpty()) {
                inputEmail.setError("E-mail é obrigatório");
                valido = false;
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                inputEmail.setError("E-mail inválido");
                valido = false;
            } else {
                inputEmail.setError(null);
            }
        }
        
        if (inputTelefone.getEditText() != null && inputTelefone.getEditText().getText().toString().trim().isEmpty()) {
            inputTelefone.setError("Telefone é obrigatório");
            valido = false;
        } else {
            inputTelefone.setError(null);
        }
        
        return valido;
    }

    private void salvarCurriculo() {
        if (!validarCampos()) {
            Toast.makeText(this, "Preencha todos os campos obrigatórios", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!premiumManager.canCreateCurriculum()) {
            premiumManager.showPremiumDialog(this, "Salvar Currículo");
            return;
        }

        atualizarCurriculoComDadosEditados();

        new Thread(() -> {
            long resultado = databaseHelper.inserirCurriculo(curriculo);
            
            runOnUiThread(() -> {
                if (resultado != -1) {
                    premiumManager.incrementCurriculosCreated();
                    Toast.makeText(this, "Currículo salvo com sucesso!", Toast.LENGTH_SHORT).show();
                    
                    Intent intent = new Intent(this, DetalheCurriculoActivity.class);
                    intent.putExtra("curriculo_id", resultado);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this, "Erro ao salvar currículo", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }

    private void atualizarCurriculoComDadosEditados() {
        if (inputNomeCompleto.getEditText() != null) {
            curriculo.setNomeCompleto(inputNomeCompleto.getEditText().getText().toString().trim());
        }
        if (inputEmail.getEditText() != null) {
            curriculo.setEmail(inputEmail.getEditText().getText().toString().trim());
        }
        if (inputTelefone.getEditText() != null) {
            curriculo.setTelefone(inputTelefone.getEditText().getText().toString().trim());
        }
        if (inputEndereco.getEditText() != null) {
            curriculo.setEndereco(inputEndereco.getEditText().getText().toString().trim());
        }
        if (inputLinkedin.getEditText() != null) {
            curriculo.setLinkedin(inputLinkedin.getEditText().getText().toString().trim());
        }
        if (inputGithub.getEditText() != null) {
            curriculo.setGithub(inputGithub.getEditText().getText().toString().trim());
        }
        if (inputObjetivo.getEditText() != null) {
            curriculo.setObjetivo(inputObjetivo.getEditText().getText().toString().trim());
        }
        if (inputResumoProfissional.getEditText() != null) {
            curriculo.setResumoProfissional(inputResumoProfissional.getEditText().getText().toString().trim());
        }
        if (inputExperiencia.getEditText() != null) {
            curriculo.setExperienciaProfissional(inputExperiencia.getEditText().getText().toString().trim());
        }
        if (inputFormacao.getEditText() != null) {
            curriculo.setFormacaoAcademica(inputFormacao.getEditText().getText().toString().trim());
        }
        if (inputHabilidades.getEditText() != null) {
            curriculo.setHabilidades(inputHabilidades.getEditText().getText().toString().trim());
        }
    }

    private void confirmarCancelamento() {
        new AlertDialog.Builder(this)
            .setTitle("Descartar alterações?")
            .setMessage("Os dados importados serão perdidos. Deseja continuar?")
            .setPositiveButton("Sim", (dialog, which) -> finish())
            .setNegativeButton("Não", null)
            .show();
    }

    private void analisarComIA() {
        if (!geminiConfigManager.isGeminiConfigured()) {
            new AlertDialog.Builder(this)
                .setTitle("IA não configurada")
                .setMessage("Configure sua chave API do Google Gemini nas configurações para usar recursos de IA.")
                .setPositiveButton("Configurar", (dialog, which) -> {
                    Intent intent = new Intent(this, ConfiguracoesActivity.class);
                    startActivity(intent);
                })
                .setNegativeButton("Cancelar", null)
                .show();
            return;
        }

        // Atualizar currículo com dados editados antes de analisar
        atualizarCurriculoComDadosEditados();

        buttonAnalisarIA.setEnabled(false);
        buttonAnalisarIA.setText("Analisando...");

        new Thread(() -> {
            List<String> sugestoes = geminiService.analisarCurriculo(curriculo);

            runOnUiThread(() -> {
                buttonAnalisarIA.setEnabled(true);
                buttonAnalisarIA.setText("🤖 Analisar com IA");

                if (sugestoes != null && !sugestoes.isEmpty()) {
                    mostrarSugestoesIA(sugestoes);
                } else {
                    Toast.makeText(this, "Não foi possível obter sugestões. Verifique sua conexão.", Toast.LENGTH_LONG).show();
                }
            });
        }).start();
    }

    private void mostrarSugestoesIA(List<String> sugestoes) {
        StringBuilder mensagem = new StringBuilder();
        mensagem.append("Sugestões de melhoria:\n\n");

        for (int i = 0; i < sugestoes.size(); i++) {
            mensagem.append((i + 1)).append(". ").append(sugestoes.get(i)).append("\n\n");
        }

        new AlertDialog.Builder(this)
            .setTitle("🤖 Análise com IA")
            .setMessage(mensagem.toString())
            .setPositiveButton("OK", null)
            .setNeutralButton("Gerar Resumo", (dialog, which) -> gerarResumoComIA())
            .show();
    }

    private void gerarResumoComIA() {
        buttonAnalisarIA.setEnabled(false);
        buttonAnalisarIA.setText("Gerando...");

        new Thread(() -> {
            String resumo = geminiService.gerarResumoProfissional(curriculo);

            runOnUiThread(() -> {
                buttonAnalisarIA.setEnabled(true);
                buttonAnalisarIA.setText("🤖 Analisar com IA");

                if (resumo != null && !resumo.isEmpty()) {
                    if (inputResumoProfissional.getEditText() != null) {
                        inputResumoProfissional.getEditText().setText(resumo);
                        Toast.makeText(this, "Resumo gerado com sucesso!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Não foi possível gerar resumo. Tente novamente.", Toast.LENGTH_LONG).show();
                }
            });
        }).start();
    }

    @Override
    public void onBackPressed() {
        confirmarCancelamento();
    }
}
