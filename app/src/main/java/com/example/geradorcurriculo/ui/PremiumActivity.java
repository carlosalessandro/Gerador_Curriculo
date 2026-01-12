package com.example.geradorcurriculo.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.geradorcurriculo.R;
import com.example.geradorcurriculo.utils.PremiumManager;

public class PremiumActivity extends AppCompatActivity {

    private PremiumManager premiumManager;
    private TextView textStatus;
    private Button buttonMonthly;
    private Button buttonLifetime;
    private Button buttonRestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_premium);

        premiumManager = new PremiumManager(this);
        
        initViews();
        setupToolbar();
        updateUI();
        setupClickListeners();
    }

    private void initViews() {
        textStatus = findViewById(R.id.text_premium_status);
        buttonMonthly = findViewById(R.id.btn_monthly);
        buttonLifetime = findViewById(R.id.btn_lifetime);
        buttonRestore = findViewById(R.id.btn_restore);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("CV Pro Premium");
        }
    }

    private void updateUI() {
        String status = premiumManager.getPremiumStatus();
        textStatus.setText("Status: " + status);
        
        if (premiumManager.isPremium()) {
            buttonMonthly.setVisibility(View.GONE);
            buttonLifetime.setVisibility(View.GONE);
            buttonRestore.setVisibility(View.VISIBLE);
            textStatus.setTextColor(getResources().getColor(R.color.success_color));
        } else {
            buttonMonthly.setVisibility(View.VISIBLE);
            buttonLifetime.setVisibility(View.VISIBLE);
            buttonRestore.setVisibility(View.GONE);
            textStatus.setTextColor(getResources().getColor(R.color.secondary_text_color));
        }
    }

    private void setupClickListeners() {
        buttonMonthly.setOnClickListener(v -> activateMonthly());
        buttonLifetime.setOnClickListener(v -> activateLifetime());
        buttonRestore.setOnClickListener(v -> restorePurchase());
    }

    private void activateMonthly() {
        // Simulação de compra - na implementação real, integrar com Google Play Billing
        premiumManager.activateMonthlyPremium();
        updateUI();
        
        // Mostrar mensagem de sucesso
        new androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Premium Ativado!")
            .setMessage("CV Pro Premium mensal ativado com sucesso!\n\n" +
                       "Aproveite todos os recursos por 30 dias.")
            .setPositiveButton("OK", (dialog, which) -> finish())
            .show();
    }

    private void activateLifetime() {
        // Simulação de compra - na implementação real, integrar com Google Play Billing
        premiumManager.activateLifetimePremium();
        updateUI();
        
        // Mostrar mensagem de sucesso
        new androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Premium Vitalício Ativado!")
            .setMessage("CV Pro Premium vitalício ativado com sucesso!\n\n" +
                       "Aproveite todos os recursos para sempre!")
            .setPositiveButton("OK", (dialog, which) -> finish())
            .show();
    }

    private void restorePurchase() {
        // Na implementação real, verificar com Google Play Billing
        new androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Restaurar Compra")
            .setMessage("Sua compra Premium foi restaurada com sucesso!")
            .setPositiveButton("OK", null)
            .show();
    }

    private void openPrivacyPolicy() {
        Intent intent = new Intent(Intent.ACTION_VIEW, 
            Uri.parse("https://cvpro.app/privacy"));
        startActivity(intent);
    }

    private void openTermsOfService() {
        Intent intent = new Intent(Intent.ACTION_VIEW, 
            Uri.parse("https://cvpro.app/terms"));
        startActivity(intent);
    }
}
