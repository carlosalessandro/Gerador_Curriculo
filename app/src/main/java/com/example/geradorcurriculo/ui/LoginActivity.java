package com.example.geradorcurriculo.ui;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.geradorcurriculo.MainActivity;
import com.example.geradorcurriculo.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "login_prefs";
    private static final String KEY_USUARIO = "usuario";
    private static final String KEY_LEMBRAR = "lembrar";
    
    // Credenciais padrão (em produção, usar autenticação real)
    private static final String USUARIO_ADMIN = "admin";
    private static final String SENHA_ADMIN = "123456";
    
    private ImageView imageLogo;
    private TextInputLayout inputLayoutUsuario, inputLayoutSenha;
    private TextInputEditText editUsuario, editSenha;
    private CheckBox checkboxLembrar;
    private Button buttonEntrar, buttonCriarConta;
    private TextView textEsqueciSenha;
    
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Verificar se já está logado
        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        if (verificarLoginSalvo()) {
            irParaMain();
            return;
        }
        
        setContentView(R.layout.activity_login);
        
        initViews();
        setupClickListeners();
        carregarDadosSalvos();
        animarEntrada();
    }

    private void initViews() {
        imageLogo = findViewById(R.id.image_logo);
        inputLayoutUsuario = findViewById(R.id.input_layout_usuario);
        inputLayoutSenha = findViewById(R.id.input_layout_senha);
        editUsuario = findViewById(R.id.edit_usuario);
        editSenha = findViewById(R.id.edit_senha);
        checkboxLembrar = findViewById(R.id.checkbox_lembrar);
        buttonEntrar = findViewById(R.id.button_entrar);
        buttonCriarConta = findViewById(R.id.button_criar_conta);
        textEsqueciSenha = findViewById(R.id.text_esqueci_senha);
    }

    private void setupClickListeners() {
        buttonEntrar.setOnClickListener(v -> realizarLogin());
        
        buttonCriarConta.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                .setTitle("Criar Conta")
                .setMessage("Funcionalidade em desenvolvimento.\n\nPor enquanto, use:\nUsuário: admin\nSenha: 123456")
                .setPositiveButton("OK", null)
                .show();
        });
        
        textEsqueciSenha.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                .setTitle("Recuperar Senha")
                .setMessage("Credenciais padrão:\n\nUsuário: admin\nSenha: 123456\n\nEm produção, implemente recuperação por e-mail.")
                .setPositiveButton("OK", null)
                .show();
        });
    }

    private void realizarLogin() {
        // Limpar erros anteriores
        inputLayoutUsuario.setError(null);
        inputLayoutSenha.setError(null);
        
        String usuario = editUsuario.getText().toString().trim();
        String senha = editSenha.getText().toString().trim();
        
        // Validações
        if (usuario.isEmpty()) {
            inputLayoutUsuario.setError("Digite o usuário");
            editUsuario.requestFocus();
            return;
        }
        
        if (senha.isEmpty()) {
            inputLayoutSenha.setError("Digite a senha");
            editSenha.requestFocus();
            return;
        }
        
        // Verificar credenciais
        if (usuario.equals(USUARIO_ADMIN) && senha.equals(SENHA_ADMIN)) {
            // Login bem-sucedido
            salvarLogin(usuario);
            mostrarSucessoEIrParaMain();
        } else {
            // Login falhou
            inputLayoutSenha.setError("Usuário ou senha incorretos");
            animarErro();
        }
    }

    private void salvarLogin(String usuario) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_USUARIO, usuario);
        editor.putBoolean(KEY_LEMBRAR, checkboxLembrar.isChecked());
        editor.apply();
    }

    private boolean verificarLoginSalvo() {
        boolean lembrar = prefs.getBoolean(KEY_LEMBRAR, false);
        String usuario = prefs.getString(KEY_USUARIO, "");
        return lembrar && !usuario.isEmpty();
    }

    private void carregarDadosSalvos() {
        boolean lembrar = prefs.getBoolean(KEY_LEMBRAR, false);
        if (lembrar) {
            String usuario = prefs.getString(KEY_USUARIO, "");
            editUsuario.setText(usuario);
            checkboxLembrar.setChecked(true);
        }
    }

    private void mostrarSucessoEIrParaMain() {
        new AlertDialog.Builder(this)
            .setTitle("✅ Login Realizado")
            .setMessage("Bem-vindo ao CV Pro!")
            .setPositiveButton("Continuar", (dialog, which) -> irParaMain())
            .setCancelable(false)
            .show();
    }

    private void irParaMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

    private void animarEntrada() {
        // Animar logo
        imageLogo.setAlpha(0f);
        imageLogo.setTranslationY(-50f);
        imageLogo.animate()
            .alpha(1f)
            .translationY(0f)
            .setDuration(800)
            .setInterpolator(new DecelerateInterpolator())
            .start();
        
        // Animar card de login
        View cardLogin = findViewById(R.id.card_login);
        cardLogin.setAlpha(0f);
        cardLogin.setTranslationY(50f);
        cardLogin.animate()
            .alpha(1f)
            .translationY(0f)
            .setDuration(800)
            .setStartDelay(200)
            .setInterpolator(new DecelerateInterpolator())
            .start();
    }

    private void animarErro() {
        // Shake animation no card de login
        View cardLogin = findViewById(R.id.card_login);
        ObjectAnimator animator = ObjectAnimator.ofFloat(cardLogin, "translationX", 0, 25, -25, 25, -25, 15, -15, 6, -6, 0);
        animator.setDuration(500);
        animator.start();
    }

    @Override
    public void onBackPressed() {
        // Confirmar saída
        new AlertDialog.Builder(this)
            .setTitle("Sair do App")
            .setMessage("Deseja realmente sair?")
            .setPositiveButton("Sim", (dialog, which) -> {
                super.onBackPressed();
                finishAffinity();
            })
            .setNegativeButton("Não", null)
            .show();
    }
}
