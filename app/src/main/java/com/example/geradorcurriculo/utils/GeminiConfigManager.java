package com.example.geradorcurriculo.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Base64;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

public class GeminiConfigManager {
    
    private static final String PREFS_NAME = "gemini_config";
    private static final String KEY_GEMINI_API = "gemini_api_key_encrypted";
    private static final String KEY_IV = "gemini_iv";
    private static final String KEY_ENABLED = "gemini_enabled";
    private static final String KEYSTORE_ALIAS = "GeminiKeyAlias";
    private static final String ANDROID_KEYSTORE = "AndroidKeyStore";
    
    private final Context context;
    private final SharedPreferences prefs;

    public GeminiConfigManager(Context context) {
        this.context = context.getApplicationContext();
        this.prefs = this.context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    /**
     * Salva a chave API do Gemini de forma criptografada
     */
    public boolean saveGeminiKey(String apiKey) {
        try {
            if (apiKey == null || apiKey.trim().isEmpty()) {
                return false;
            }

            // Gerar ou obter chave de criptografia
            SecretKey secretKey = getOrCreateSecretKey();
            
            // Criptografar a API key
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] iv = cipher.getIV();
            byte[] encryptedKey = cipher.doFinal(apiKey.getBytes(StandardCharsets.UTF_8));
            
            // Salvar dados criptografados
            String encryptedKeyBase64 = Base64.encodeToString(encryptedKey, Base64.DEFAULT);
            String ivBase64 = Base64.encodeToString(iv, Base64.DEFAULT);
            
            prefs.edit()
                .putString(KEY_GEMINI_API, encryptedKeyBase64)
                .putString(KEY_IV, ivBase64)
                .putBoolean(KEY_ENABLED, true)
                .apply();
            
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            // Fallback: salvar sem criptografia (não recomendado para produção)
            return saveFallback(apiKey);
        }
    }

    /**
     * Recupera a chave API do Gemini descriptografada
     */
    public String getGeminiKey() {
        try {
            String encryptedKeyBase64 = prefs.getString(KEY_GEMINI_API, null);
            String ivBase64 = prefs.getString(KEY_IV, null);
            
            if (encryptedKeyBase64 == null || ivBase64 == null) {
                return null;
            }
            
            // Obter chave de descriptografia
            SecretKey secretKey = getOrCreateSecretKey();
            
            // Descriptografar
            byte[] encryptedKey = Base64.decode(encryptedKeyBase64, Base64.DEFAULT);
            byte[] iv = Base64.decode(ivBase64, Base64.DEFAULT);
            
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            GCMParameterSpec spec = new GCMParameterSpec(128, iv);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, spec);
            
            byte[] decryptedKey = cipher.doFinal(encryptedKey);
            return new String(decryptedKey, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Verifica se a chave API está configurada
     */
    public boolean isGeminiConfigured() {
        return prefs.getBoolean(KEY_ENABLED, false) && 
               prefs.getString(KEY_GEMINI_API, null) != null;
    }

    /**
     * Remove a chave API
     */
    public void removeGeminiKey() {
        prefs.edit()
            .remove(KEY_GEMINI_API)
            .remove(KEY_IV)
            .putBoolean(KEY_ENABLED, false)
            .apply();
    }

    /**
     * Obtém ou cria uma chave secreta no Android Keystore
     */
    private SecretKey getOrCreateSecretKey() throws Exception {
        KeyStore keyStore = KeyStore.getInstance(ANDROID_KEYSTORE);
        keyStore.load(null);
        
        if (!keyStore.containsAlias(KEYSTORE_ALIAS)) {
            // Criar nova chave
            KeyGenerator keyGenerator = KeyGenerator.getInstance(
                KeyProperties.KEY_ALGORITHM_AES, 
                ANDROID_KEYSTORE
            );
            
            KeyGenParameterSpec keyGenParameterSpec = new KeyGenParameterSpec.Builder(
                KEYSTORE_ALIAS,
                KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT
            )
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .setRandomizedEncryptionRequired(true)
                .build();
            
            keyGenerator.init(keyGenParameterSpec);
            return keyGenerator.generateKey();
        } else {
            // Recuperar chave existente
            return (SecretKey) keyStore.getKey(KEYSTORE_ALIAS, null);
        }
    }

    /**
     * Fallback para dispositivos que não suportam Android Keystore
     */
    private boolean saveFallback(String apiKey) {
        try {
            // Simples ofuscação (não é seguro, mas melhor que texto plano)
            String obfuscated = Base64.encodeToString(
                apiKey.getBytes(StandardCharsets.UTF_8), 
                Base64.DEFAULT
            );
            prefs.edit()
                .putString(KEY_GEMINI_API, obfuscated)
                .putBoolean(KEY_ENABLED, true)
                .apply();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Verifica se a chave tem formato válido
     */
    public boolean isValidKeyFormat(String apiKey) {
        if (apiKey == null || apiKey.trim().isEmpty()) {
            return false;
        }
        // Chaves do Gemini geralmente começam com "AI" e têm pelo menos 30 caracteres
        return apiKey.trim().length() >= 30;
    }
}
