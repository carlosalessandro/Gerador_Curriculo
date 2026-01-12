package com.example.geradorcurriculo.utils;

import android.content.Context;
import android.os.Bundle;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crashlytics.FirebaseCrashlytics;

public class AnalyticsHelper {
    
    private static FirebaseAnalytics firebaseAnalytics;
    private static FirebaseCrashlytics crashlytics;
    
    public static void init(Context context) {
        firebaseAnalytics = FirebaseAnalytics.getInstance(context);
        crashlytics = FirebaseCrashlytics.getInstance();
    }
    
    public static void logScreenView(String screenName) {
        if (firebaseAnalytics != null) {
            Bundle params = new Bundle();
            params.putString("screen_name", screenName);
            firebaseAnalytics.logEvent("screen_view", params);
        }
    }
    
    public static void logCurriculumCreated(String method) {
        if (firebaseAnalytics != null) {
            Bundle params = new Bundle();
            params.putString("method", method); // "manual" ou "import"
            firebaseAnalytics.logEvent("curriculum_created", params);
        }
    }
    
    public static void logATSAnalysis(int score, boolean isPremium) {
        if (firebaseAnalytics != null) {
            Bundle params = new Bundle();
            params.putInt("score", score);
            params.putBoolean("is_premium", isPremium);
            firebaseAnalytics.logEvent("ats_analysis", params);
        }
    }
    
    public static void logPremiumPurchase(String planType) {
        if (firebaseAnalytics != null) {
            Bundle params = new Bundle();
            params.putString("plan_type", planType); // "monthly" ou "lifetime"
            firebaseAnalytics.logEvent("premium_purchase", params);
        }
    }
    
    public static void logPDFExport() {
        if (firebaseAnalytics != null) {
            firebaseAnalytics.logEvent("pdf_export", null);
        }
    }
    
    public static void logError(String message, Throwable throwable) {
        if (crashlytics != null) {
            crashlytics.recordException(throwable);
            crashlytics.setCustomKey("error_message", message);
        }
    }
    
    public static void setUserProperty(String name, String value) {
        if (firebaseAnalytics != null) {
            firebaseAnalytics.setUserProperty(name, value);
        }
    }
    
    public static void setUserId(String userId) {
        if (firebaseAnalytics != null) {
            firebaseAnalytics.setUserId(userId);
        }
    }
}
