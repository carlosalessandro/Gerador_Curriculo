package com.example.geradorcurriculo.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.geradorcurriculo.R;
import com.example.geradorcurriculo.model.Curriculo;
import com.example.geradorcurriculo.database.DatabaseHelper;
import com.google.android.material.textfield.TextInputLayout;

public class CriarCurriculoActivity extends AppCompatActivity {

    private TextInputLayout inputNomeCompleto;
    private TextInputLayout inputEmail;
    private TextInputLayout inputTelefone;
    private TextInputLayout inputEndereco;
    private TextInputLayout inputLinkedin;
    private TextInputLayout inputGithub;
    private TextInputLayout inputObjetivo;
    private TextInputLayout inputResumoProfissional;
    
    private EditText editNomeCompleto;
    private EditText editEmail;
    private EditText editTelefone;
    private EditText editEndereco;
    private EditText editLinkedin;
    private EditText editGithub;
    private EditText editObjetivo;
    private EditText editResumoProfissional;
    
    private DatabaseHelper databaseHelper;
    private Curriculo curriculoAtual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_curriculo);

        databaseHelper = new DatabaseHelper(this);
        
        initViews();
        setupToolbar();
        
        curriculoAtual = new Curriculo();
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
        
        editNomeCompleto = inputNomeCompleto.getEditText();
        editEmail = inputEmail.getEditText();
        editTelefone = inputTelefone.getEditText();
        editEndereco = inputEndereco.getEditText();
        editLinkedin = inputLinkedin.getEditText();
        editGithub = inputGithub.getEditText();
        editObjetivo = inputObjetivo.getEditText();
        editResumoProfissional = inputResumoProfissional.getEditText();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Novo Currículo");
        }
    }

    private boolean validarCampos() {
        boolean valido = true;
        
        if (editNomeCompleto.getText().toString().trim().isEmpty()) {
            inputNomeCompleto.setError("Nome completo é obrigatório");
            valido = false;
        } else {
            inputNomeCompleto.setError(null);
        }
        
        if (editEmail.getText().toString().trim().isEmpty()) {
            inputEmail.setError("E-mail é obrigatório");
            valido = false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(editEmail.getText().toString()).matches()) {
            inputEmail.setError("E-mail inválido");
            valido = false;
        } else {
            inputEmail.setError(null);
        }
        
        if (editTelefone.getText().toString().trim().isEmpty()) {
            inputTelefone.setError("Telefone é obrigatório");
            valido = false;
        } else {
            inputTelefone.setError(null);
        }
        
        return valido;
    }

    private void salvarCurriculo() {
        if (!validarCampos()) {
            return;
        }
        
        curriculoAtual.setNomeCompleto(editNomeCompleto.getText().toString().trim());
        curriculoAtual.setEmail(editEmail.getText().toString().trim());
        curriculoAtual.setTelefone(editTelefone.getText().toString().trim());
        curriculoAtual.setEndereco(editEndereco.getText().toString().trim());
        curriculoAtual.setLinkedin(editLinkedin.getText().toString().trim());
        curriculoAtual.setGithub(editGithub.getText().toString().trim());
        curriculoAtual.setObjetivo(editObjetivo.getText().toString().trim());
        curriculoAtual.setResumoProfissional(editResumoProfissional.getText().toString().trim());
        
        long resultado = databaseHelper.inserirCurriculo(curriculoAtual);
        
        if (resultado != -1) {
            Toast.makeText(this, "Currículo salvo com sucesso!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Erro ao salvar currículo", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_criar_curriculo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_salvar) {
            salvarCurriculo();
            return true;
        } else if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
