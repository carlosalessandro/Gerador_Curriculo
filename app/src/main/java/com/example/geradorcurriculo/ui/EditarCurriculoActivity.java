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

public class EditarCurriculoActivity extends AppCompatActivity {

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
    
    private long curriculoId;
    private Curriculo curriculo;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_curriculo);

        databaseHelper = new DatabaseHelper(this);
        
        curriculoId = getIntent().getLongExtra("curriculo_id", -1);
        
        initViews();
        setupToolbar();
        carregarCurriculo();
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
            getSupportActionBar().setTitle("Editar Currículo");
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
                    preencherCampos();
                } else {
                    Toast.makeText(this, "Currículo não encontrado", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }).start();
    }

    private void preencherCampos() {
        if (curriculo.getNomeCompleto() != null) editNomeCompleto.setText(curriculo.getNomeCompleto());
        if (curriculo.getEmail() != null) editEmail.setText(curriculo.getEmail());
        if (curriculo.getTelefone() != null) editTelefone.setText(curriculo.getTelefone());
        if (curriculo.getEndereco() != null) editEndereco.setText(curriculo.getEndereco());
        if (curriculo.getLinkedin() != null) editLinkedin.setText(curriculo.getLinkedin());
        if (curriculo.getGithub() != null) editGithub.setText(curriculo.getGithub());
        if (curriculo.getObjetivo() != null) editObjetivo.setText(curriculo.getObjetivo());
        if (curriculo.getResumoProfissional() != null) editResumoProfissional.setText(curriculo.getResumoProfissional());
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

    private void atualizarCurriculo() {
        if (!validarCampos()) {
            return;
        }
        
        curriculo.setNomeCompleto(editNomeCompleto.getText().toString().trim());
        curriculo.setEmail(editEmail.getText().toString().trim());
        curriculo.setTelefone(editTelefone.getText().toString().trim());
        curriculo.setEndereco(editEndereco.getText().toString().trim());
        curriculo.setLinkedin(editLinkedin.getText().toString().trim());
        curriculo.setGithub(editGithub.getText().toString().trim());
        curriculo.setObjetivo(editObjetivo.getText().toString().trim());
        curriculo.setResumoProfissional(editResumoProfissional.getText().toString().trim());
        
        new Thread(() -> {
            int resultado = databaseHelper.atualizarCurriculo(curriculo);
            
            runOnUiThread(() -> {
                if (resultado > 0) {
                    Toast.makeText(this, "Currículo atualizado com sucesso!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Erro ao atualizar currículo", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editar_curriculo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_salvar) {
            atualizarCurriculo();
            return true;
        } else if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
