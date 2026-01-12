package com.example.geradorcurriculo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.geradorcurriculo.R;
import com.example.geradorcurriculo.model.Curriculo;
import com.example.geradorcurriculo.database.DatabaseHelper;
import com.example.geradorcurriculo.services.PDFGeneratorService;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class DetalheCurriculoActivity extends AppCompatActivity {

    private TextView textNome;
    private TextView textEmail;
    private TextView textTelefone;
    private TextView textEndereco;
    private TextView textLinkedin;
    private TextView textGithub;
    private TextView textObjetivo;
    private TextView textResumo;
    private TextView textDataCriacao;
    private TextView textDataAtualizacao;
    private TextView textAtsScore;
    
    private long curriculoId;
    private Curriculo curriculo;
    private DatabaseHelper databaseHelper;
    private PDFGeneratorService pdfGeneratorService;
    private SimpleDateFormat dateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe_curriculo);

        databaseHelper = new DatabaseHelper(this);
        pdfGeneratorService = new PDFGeneratorService();
        dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        
        curriculoId = getIntent().getLongExtra("curriculo_id", -1);
        
        initViews();
        setupToolbar();
        carregarCurriculo();
    }

    private void initViews() {
        textNome = findViewById(R.id.text_nome);
        textEmail = findViewById(R.id.text_email);
        textTelefone = findViewById(R.id.text_telefone);
        textEndereco = findViewById(R.id.text_endereco);
        textLinkedin = findViewById(R.id.text_linkedin);
        textGithub = findViewById(R.id.text_github);
        textObjetivo = findViewById(R.id.text_objetivo);
        textResumo = findViewById(R.id.text_resumo);
        textDataCriacao = findViewById(R.id.text_data_criacao);
        textDataAtualizacao = findViewById(R.id.text_data_atualizacao);
        textAtsScore = findViewById(R.id.text_ats_score);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Detalhes do Currículo");
        }
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
                    preencherDados();
                } else {
                    Toast.makeText(this, "Currículo não encontrado", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }).start();
    }

    private void preencherDados() {
        textNome.setText(curriculo.getNomeCompleto() != null ? curriculo.getNomeCompleto() : "Não informado");
        textEmail.setText(curriculo.getEmail() != null ? curriculo.getEmail() : "Não informado");
        textTelefone.setText(curriculo.getTelefone() != null ? curriculo.getTelefone() : "Não informado");
        textEndereco.setText(curriculo.getEndereco() != null ? curriculo.getEndereco() : "Não informado");
        textLinkedin.setText(curriculo.getLinkedin() != null ? curriculo.getLinkedin() : "Não informado");
        textGithub.setText(curriculo.getGithub() != null ? curriculo.getGithub() : "Não informado");
        textObjetivo.setText(curriculo.getObjetivo() != null ? curriculo.getObjetivo() : "Não informado");
        textResumo.setText(curriculo.getResumoProfissional() != null ? curriculo.getResumoProfissional() : "Não informado");
        
        textDataCriacao.setText("Criado: " + dateFormat.format(curriculo.getDataCriacao()));
        textDataAtualizacao.setText("Atualizado: " + dateFormat.format(curriculo.getDataAtualizacao()));
        
        if (curriculo.getAtsScore() > 0) {
            textAtsScore.setText("Pontuação ATS: " + curriculo.getAtsScore() + "/100");
            textAtsScore.setVisibility(android.view.View.VISIBLE);
        } else {
            textAtsScore.setVisibility(android.view.View.GONE);
        }
        
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(curriculo.getNomeCompleto());
        }
    }

    private void editarCurriculo() {
        Intent intent = new Intent(this, EditarCurriculoActivity.class);
        intent.putExtra("curriculo_id", curriculoId);
        startActivity(intent);
    }

    private void gerarPDF() {
        new Thread(() -> {
            try {
                String filePath = pdfGeneratorService.gerarPDFCurriculo(this, curriculo);
                
                runOnUiThread(() -> {
                    Toast.makeText(this, "PDF gerado com sucesso: " + filePath, Toast.LENGTH_LONG).show();
                });
            } catch (Exception e) {
                runOnUiThread(() -> {
                    Toast.makeText(this, "Erro ao gerar PDF: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
            }
        }).start();
    }

    private void realizarAnaliseATS() {
        Intent intent = new Intent(this, AnaliseAtsActivity.class);
        intent.putExtra("curriculo_id", curriculoId);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detalhe_curriculo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_editar) {
            editarCurriculo();
            return true;
        } else if (id == R.id.action_gerar_pdf) {
            gerarPDF();
            return true;
        } else if (id == R.id.action_analise_ats) {
            realizarAnaliseATS();
            return true;
        } else if (id == android.R.id.home) {
            onBackPressed();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
