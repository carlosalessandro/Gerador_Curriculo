package com.example.geradorcurriculo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.example.geradorcurriculo.ui.ListaCurriculosActivity;
import com.example.geradorcurriculo.ui.CriarCurriculoActivity;
import com.example.geradorcurriculo.ui.ImportarCurriculoActivity;
import com.example.geradorcurriculo.ui.AnaliseAtsActivity;
import com.example.geradorcurriculo.ui.OnboardingActivity;
import com.example.geradorcurriculo.ui.PremiumActivity;
import com.example.geradorcurriculo.utils.AppRater;
import com.example.geradorcurriculo.utils.PremiumManager;
import com.example.geradorcurriculo.utils.AnalyticsHelper;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    private CardView cardNovoCurriculo;
    private CardView cardMeusCurriculos;
    private CardView cardImportar;
    private CardView cardAnaliseAts;
    private CardView cardPremium;
    private PremiumManager premiumManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        premiumManager = new PremiumManager(this);
        
        // Inicializar analytics
        AnalyticsHelper.init(this);
        AnalyticsHelper.logScreenView("MainActivity");
        
        initViews();
        setupClickListeners();
        
        // Sistema de avaliação do app
        AppRater.appLaunched(this);
        
        // Verificar primeiro acesso para onboarding
        checkFirstAccess();
    }

    private void initViews() {
        cardNovoCurriculo = findViewById(R.id.card_novo_curriculo);
        cardMeusCurriculos = findViewById(R.id.card_meus_curriculos);
        cardImportar = findViewById(R.id.card_importar);
        cardAnaliseAts = findViewById(R.id.card_analise_ats);
        cardPremium = findViewById(R.id.card_premium);
    }

    private void setupClickListeners() {
        cardNovoCurriculo.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CriarCurriculoActivity.class);
            startActivity(intent);
        });

        cardMeusCurriculos.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ListaCurriculosActivity.class);
            startActivity(intent);
        });

        cardImportar.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ImportarCurriculoActivity.class);
            startActivity(intent);
        });

        cardAnaliseAts.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AnaliseAtsActivity.class);
            startActivity(intent);
        });

        cardPremium.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PremiumActivity.class);
            startActivity(intent);
        });
    }

    private void checkFirstAccess() {
        android.content.SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
        boolean isFirstAccess = prefs.getBoolean("first_access", true);
        
        if (isFirstAccess) {
            android.content.SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("first_access", false);
            editor.apply();
            
            // Abrir onboarding após um pequeno delay
            cardNovoCurriculo.postDelayed(() -> {
                Intent intent = new Intent(MainActivity.this, OnboardingActivity.class);
                startActivity(intent);
            }, 500);
        }
    }

    private void showMessage(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show();
    }
}
