package com.example.geradorcurriculo.utils;

import android.content.Context;
import android.content.SharedPreferences;
import com.example.geradorcurriculo.ui.PremiumActivity;
import java.util.Date;

public class PremiumManager {
    
    private static final String PREFS_NAME = "premium_prefs";
    private static final String KEY_IS_PREMIUM = "is_premium";
    private static final String KEY_PREMIUM_TYPE = "premium_type"; // monthly, lifetime
    private static final String KEY_PREMIUM_EXPIRY = "premium_expiry";
    private static final String KEY_FREE_CURRICULOS_CREATED = "free_curriculos_created";
    private static final String KEY_FREE_ANALYSIS_USED = "free_analysis_used";
    
    private static final int MAX_FREE_CURRICULOS = 3;
    private static final int MAX_FREE_ANALYSIS = 5;
    
    private final SharedPreferences prefs;
    private final Context context;
    
    public PremiumManager(Context context) {
        this.context = context;
        this.prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }
    
    public boolean isPremium() {
        if (prefs.getBoolean(KEY_IS_PREMIUM, false)) {
            String type = prefs.getString(KEY_PREMIUM_TYPE, "");
            if ("lifetime".equals(type)) {
                return true;
            } else if ("monthly".equals(type)) {
                long expiry = prefs.getLong(KEY_PREMIUM_EXPIRY, 0);
                return expiry > System.currentTimeMillis();
            }
        }
        return false;
    }
    
    public boolean canCreateCurriculum() {
        if (isPremium()) return true;
        return getFreeCurriculosCreated() < MAX_FREE_CURRICULOS;
    }
    
    public boolean canUseAdvancedAnalysis() {
        if (isPremium()) return true;
        return getFreeAnalysisUsed() < MAX_FREE_ANALYSIS;
    }
    
    public int getFreeCurriculosCreated() {
        return prefs.getInt(KEY_FREE_CURRICULOS_CREATED, 0);
    }
    
    public int getFreeAnalysisUsed() {
        return prefs.getInt(KEY_FREE_ANALYSIS_USED, 0);
    }
    
    public void incrementCurriculosCreated() {
        if (!isPremium()) {
            int current = getFreeCurriculosCreated();
            prefs.edit().putInt(KEY_FREE_CURRICULOS_CREATED, current + 1).apply();
        }
    }
    
    public void incrementAnalysisUsed() {
        if (!isPremium()) {
            int current = getFreeAnalysisUsed();
            prefs.edit().putInt(KEY_FREE_ANALYSIS_USED, current + 1).apply();
        }
    }
    
    public void activateMonthlyPremium() {
        long expiry = System.currentTimeMillis() + (30L * 24 * 60 * 60 * 1000); // 30 dias
        prefs.edit()
            .putBoolean(KEY_IS_PREMIUM, true)
            .putString(KEY_PREMIUM_TYPE, "monthly")
            .putLong(KEY_PREMIUM_EXPIRY, expiry)
            .apply();
    }
    
    public void activateLifetimePremium() {
        prefs.edit()
            .putBoolean(KEY_IS_PREMIUM, true)
            .putString(KEY_PREMIUM_TYPE, "lifetime")
            .apply();
    }
    
    public String getPremiumStatus() {
        if (isPremium()) {
            String type = prefs.getString(KEY_PREMIUM_TYPE, "");
            if ("lifetime".equals(type)) {
                return "Premium Vitalício";
            } else {
                long expiry = prefs.getLong(KEY_PREMIUM_EXPIRY, 0);
                Date expiryDate = new Date(expiry);
                return "Premium até " + new java.text.SimpleDateFormat("dd/MM/yyyy").format(expiryDate);
            }
        } else {
            return "Grátis (" + getFreeCurriculosCreated() + "/" + MAX_FREE_CURRICULOS + " currículos)";
        }
    }
    
    public int getRemainingFreeCurriculos() {
        if (isPremium()) return -1; // Ilimitado
        return MAX_FREE_CURRICULOS - getFreeCurriculosCreated();
    }
    
    public int getRemainingFreeAnalysis() {
        if (isPremium()) return -1; // Ilimitado
        return MAX_FREE_ANALYSIS - getFreeAnalysisUsed();
    }
    
    public void showPremiumDialog(Context context, String feature) {
        new androidx.appcompat.app.AlertDialog.Builder(context)
            .setTitle("Recurso Premium")
            .setMessage("O recurso '" + feature + "' é exclusivo para usuários Premium.\n\n" +
                       "Assine CV Pro Premium e desbloqueie:\n" +
                       "• Currículos ilimitados\n" +
                       "• Análise ATS avançada\n" +
                       "• 50+ templates profissionais\n" +
                       "• Exportação em múltiplos formatos\n" +
                       "• Prioridade no suporte")
            .setPositiveButton("Assinar Agora", (dialog, which) -> {
                // Abrir tela de premium
                context.startActivity(new android.content.Intent(context, PremiumActivity.class));
            })
            .setNegativeButton("Cancelar", null)
            .show();
    }
    
    public void resetFreeUsage() {
        prefs.edit()
            .remove(KEY_FREE_CURRICULOS_CREATED)
            .remove(KEY_FREE_ANALYSIS_USED)
            .apply();
    }
}
