package com.example.geradorcurriculo.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.geradorcurriculo.R;
import com.example.geradorcurriculo.model.Curriculo;
import com.example.geradorcurriculo.model.ExperienciaProfissional;
import com.example.geradorcurriculo.model.Formacao;
import com.example.geradorcurriculo.model.Habilidade;
import com.example.geradorcurriculo.model.Idioma;
import com.example.geradorcurriculo.database.DatabaseHelper;
import com.example.geradorcurriculo.utils.PremiumManager;
import com.google.android.material.textfield.TextInputLayout;
import java.util.ArrayList;
import java.util.List;

public class CriarCurriculoActivity extends AppCompatActivity {

    private TextInputLayout inputNomeCompleto, inputEmail, inputTelefone, inputEndereco;
    private TextInputLayout inputLinkedin, inputGithub, inputObjetivo, inputResumoProfissional;
    private EditText editNomeCompleto, editEmail, editTelefone, editEndereco;
    private EditText editLinkedin, editGithub, editObjetivo, editResumoProfissional;
    private Button btnAdicionarExperiencia, btnAdicionarFormacao, btnAdicionarHabilidade, btnAdicionarIdioma;
    
    private DatabaseHelper databaseHelper;
    private PremiumManager premiumManager;
    private Curriculo curriculoAtual;
    private List<ExperienciaProfissional> experiencias;
    private List<Formacao> formacoes;
    private List<Habilidade> habilidades;
    private List<Idioma> idiomas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_curriculo);

        databaseHelper = new DatabaseHelper(this);
        premiumManager = new PremiumManager(this);
        experiencias = new ArrayList<>();
        formacoes = new ArrayList<>();
        habilidades = new ArrayList<>();
        idiomas = new ArrayList<>();
        
        initViews();
        setupToolbar();
        setupClickListeners();
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
        
        btnAdicionarExperiencia = findViewById(R.id.btn_adicionar_experiencia);
        btnAdicionarFormacao = findViewById(R.id.btn_adicionar_formacao);
        btnAdicionarHabilidade = findViewById(R.id.btn_adicionar_habilidade);
        btnAdicionarIdioma = findViewById(R.id.btn_adicionar_idioma);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Novo Currículo");
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }
    
    private void setupClickListeners() {
        btnAdicionarExperiencia.setOnClickListener(v -> mostrarDialogExperiencia());
        btnAdicionarFormacao.setOnClickListener(v -> mostrarDialogFormacao());
        btnAdicionarHabilidade.setOnClickListener(v -> mostrarDialogHabilidade());
        btnAdicionarIdioma.setOnClickListener(v -> mostrarDialogIdioma());
    }

    private void mostrarDialogExperiencia() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Adicionar Experiência");
        
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10);
        
        EditText inputCargo = new EditText(this);
        inputCargo.setHint("Cargo");
        layout.addView(inputCargo);
        
        EditText inputEmpresa = new EditText(this);
        inputEmpresa.setHint("Empresa");
        layout.addView(inputEmpresa);
        
        EditText inputPeriodo = new EditText(this);
        inputPeriodo.setHint("Período");
        layout.addView(inputPeriodo);
        
        EditText inputDescricao = new EditText(this);
        inputDescricao.setHint("Descrição");
        layout.addView(inputDescricao);
        
        builder.setView(layout);
        builder.setPositiveButton("Adicionar", (dialog, which) -> {
            ExperienciaProfissional exp = new ExperienciaProfissional();
            exp.setCargo(inputCargo.getText().toString());
            exp.setEmpresa(inputEmpresa.getText().toString());
            exp.setDescricao(inputDescricao.getText().toString());
            experiencias.add(exp);
            Toast.makeText(this, "Experiência adicionada! Total: " + experiencias.size(), Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }
    
    private void mostrarDialogFormacao() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Adicionar Formação");
        
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10);
        
        EditText inputCurso = new EditText(this);
        inputCurso.setHint("Curso");
        layout.addView(inputCurso);
        
        EditText inputInstituicao = new EditText(this);
        inputInstituicao.setHint("Instituição");
        layout.addView(inputInstituicao);
        
        EditText inputPeriodo = new EditText(this);
        inputPeriodo.setHint("Período");
        layout.addView(inputPeriodo);
        
        builder.setView(layout);
        builder.setPositiveButton("Adicionar", (dialog, which) -> {
            Formacao formacao = new Formacao();
            formacao.setCurso(inputCurso.getText().toString());
            formacao.setInstituicao(inputInstituicao.getText().toString());
            formacoes.add(formacao);
            Toast.makeText(this, "Formação adicionada! Total: " + formacoes.size(), Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }
    
    private void mostrarDialogHabilidade() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Adicionar Habilidade");
        
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10);
        
        EditText inputNome = new EditText(this);
        inputNome.setHint("Nome da habilidade");
        layout.addView(inputNome);
        
        TextView tvNivel = new TextView(this);
        tvNivel.setText("Nível:");
        tvNivel.setPadding(0, 20, 0, 10);
        layout.addView(tvNivel);
        
        String[] niveis = {"Básico", "Intermediário", "Avançado", "Especialista"};
        int[] nivelSelecionado = {1};
        
        RadioGroup radioGroup = new RadioGroup(this);
        for (int i = 0; i < niveis.length; i++) {
            RadioButton rb = new RadioButton(this);
            rb.setText(niveis[i]);
            rb.setId(i);
            if (i == 1) rb.setChecked(true);
            radioGroup.addView(rb);
        }
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> nivelSelecionado[0] = checkedId);
        layout.addView(radioGroup);
        
        builder.setView(layout);
        builder.setPositiveButton("Adicionar", (dialog, which) -> {
            Habilidade habilidade = new Habilidade();
            habilidade.setNome(inputNome.getText().toString());
            habilidade.setNivel(niveis[nivelSelecionado[0]]);
            habilidades.add(habilidade);
            Toast.makeText(this, "Habilidade adicionada! Total: " + habilidades.size(), Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }
    
    private void mostrarDialogIdioma() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Adicionar Idioma");
        
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10);
        
        EditText inputIdioma = new EditText(this);
        inputIdioma.setHint("Idioma");
        layout.addView(inputIdioma);
        
        TextView tvNivel = new TextView(this);
        tvNivel.setText("Nível:");
        tvNivel.setPadding(0, 20, 0, 10);
        layout.addView(tvNivel);
        
        String[] niveis = {"Básico", "Intermediário", "Avançado", "Fluente", "Nativo"};
        int[] nivelSelecionado = {2};
        
        RadioGroup radioGroup = new RadioGroup(this);
        for (int i = 0; i < niveis.length; i++) {
            RadioButton rb = new RadioButton(this);
            rb.setText(niveis[i]);
            rb.setId(i);
            if (i == 2) rb.setChecked(true);
            radioGroup.addView(rb);
        }
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> nivelSelecionado[0] = checkedId);
        layout.addView(radioGroup);
        
        builder.setView(layout);
        builder.setPositiveButton("Adicionar", (dialog, which) -> {
            Idioma idioma = new Idioma();
            idioma.setNome(inputIdioma.getText().toString());
            idioma.setNivel(niveis[nivelSelecionado[0]]);
            idiomas.add(idioma);
            Toast.makeText(this, "Idioma adicionado! Total: " + idiomas.size(), Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton("Cancelar", null);
        builder.show();
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
            Toast.makeText(this, "Preencha todos os campos obrigatórios", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!premiumManager.canCreateCurriculum()) {
            premiumManager.showPremiumDialog(this, "Criar Currículo");
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
        curriculoAtual.setExperiencias(experiencias);
        curriculoAtual.setFormacoes(formacoes);
        curriculoAtual.setHabilidades(habilidades);
        curriculoAtual.setIdiomas(idiomas);
        long resultado = databaseHelper.inserirCurriculo(curriculoAtual);
        if (resultado != -1) {
            premiumManager.incrementCurriculosCreated();
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
    
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
            .setTitle("Descartar alterações?")
            .setMessage("Você tem certeza que deseja sair sem salvar?")
            .setPositiveButton("Sim", (dialog, which) -> super.onBackPressed())
            .setNegativeButton("Não", null)
            .show();
    }
}
