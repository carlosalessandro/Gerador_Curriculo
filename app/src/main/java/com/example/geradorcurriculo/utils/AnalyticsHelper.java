package com.example.geradorcurriculo.utils;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
// import com.google.firebase.analytics.FirebaseAnalytics;
// import com.google.firebase.crashlytics.FirebaseCrashlytics;

public class AnalyticsHelper {
    
    private static final String TAG = "AnalyticsHelper";
    // private static FirebaseAnalytics firebaseAnalytics;
    // private static FirebaseCrashlytics crashlytics;
    
    public static void init(Context context) {
        // TODO: Implementar quando Firebase estiver configurado
        Log.d(TAG, "Analytics initialized (stub)");
        // firebaseAnalytics = FirebaseAnalytics.getInstance(context);
        // crashlytics = FirebaseCrashlytics.getInstance();
    }
    
    public static void logScreenView(String screenName) {
        Log.d(TAG, "Screen view: " + screenName);
        /*
        if (firebaseAnalytics != null) {
            Bundle params = new Bundle();
            params.putString("screen_name", screenName);
            firebaseAnalytics.logEvent("screen_view", params);
        }
        */
    }
    
    public static void logCurriculumCreated(String method) {
        Log.d(TAG, "Curriculum created: " + method);
        /*
        if (firebaseAnalytics != null) {
            Bundle params = new Bundle();
            params.putString("method", method); // "manual" ou "import"
            firebaseAnalytics.logEvent("curriculum_created", params);
        }
        */
    }
    
    public static void logATSAnalysis(int score, boolean isPremium) {
        Log.d(TAG, "ATS Analysis - Score: " + score + ", Premium: " + isPremium);
        /*
        if (firebaseAnalytics != null) {
            Bundle params = new Bundle();
            params.putInt("score", score);
            params.putBoolean("is_premium", isPremium);
            firebaseAnalytics.logEvent("ats_analysis", params);
        }
        */
    }
    
    public static void logPremiumPurchase(String planType) {
        Log.d(TAG, "Premium purchase: " + planType);
        /*
        if (firebaseAnalytics != null) {
            Bundle params = new Bundle();
            params.putString("plan_type", planType); // "monthly" ou "lifetime"
            firebaseAnalytics.logEvent("premium_purchase", params);
        }
        */
    }
    
    public static void logPDFExport() {
        Log.d(TAG, "PDF exported");
        /*
        if (firebaseAnalytics != null) {
            firebaseAnalytics.logEvent("pdf_export", null);
        }
        */
    }
    
    public static void logError(String message, Throwable throwable) {
        Log.e(TAG, "Error: " + message, throwable);
        /*
        if (crashlytics != null) {
            crashlytics.recordException(throwable);
            crashlytics.setCustomKey("error_message", message);
        }
        */
    }
    
    public static void setUserProperty(String name, String value) {
        Log.d(TAG, "User property: " + name + " = " + value);
        /*
        if (firebaseAnalytics != null) {
            firebaseAnalytics.setUserProperty(name, value);
        }
        */
    }
    
    public static void setUserId(String userId) {
        Log.d(TAG, "User ID: " + userId);
        /*
        if (firebaseAnalytics != null) {
            firebaseAnalytics.setUserId(userId);
        }
        */
    }
}
