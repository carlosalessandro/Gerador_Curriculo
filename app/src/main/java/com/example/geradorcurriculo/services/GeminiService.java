package com.example.geradorcurriculo.services;

import android.content.Context;
import android.util.Log;
import com.example.geradorcurriculo.model.Curriculo;
import com.example.geradorcurriculo.utils.GeminiConfigManager;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class GeminiService {
    
    private static final String TAG = "GeminiService";
    private static final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent";
    
    private final Context context;
    private final GeminiConfigManager configManager;

    public GeminiService(Context context) {
        this.context = context;
        this.configManager = new GeminiConfigManager(context);
    }

    /**
     * Testa a conexão com a API do Gemini
     */
    public boolean testConnection() {
        try {
            String apiKey = configManager.getGeminiKey();
            if (apiKey == null) {
                return false;
            }

            String prompt = "Responda apenas 'OK' se você está funcionando.";
            String response = sendRequest(apiKey, prompt);
            
            return response != null && !response.isEmpty();
        } catch (Exception e) {
            Log.e(TAG, "Erro ao testar conexão", e);
            return false;
        }
    }

    /**
     * Analisa um currículo e retorna sugestões de melhoria
     */
    public List<String> analisarCurriculo(Curriculo curriculo) {
        List<String> sugestoes = new ArrayList<>();
        
        try {
            String apiKey = configManager.getGeminiKey();
            if (apiKey == null) {
                sugestoes.add("Configure a chave API do Gemini nas configurações");
                return sugestoes;
            }

            String prompt = construirPromptAnalise(curriculo);
            String response = sendRequest(apiKey, prompt);
            
            if (response != null) {
                sugestoes = parseAnaliseResponse(response);
            }
        } catch (Exception e) {
            Log.e(TAG, "Erro ao analisar currículo", e);
            sugestoes.add("Erro ao processar análise: " + e.getMessage());
        }
        
        return sugestoes;
    }

    /**
     * Gera um resumo profissional baseado nas informações do currículo
     */
    public String gerarResumoProfissional(Curriculo curriculo) {
        try {
            String apiKey = configManager.getGeminiKey();
            if (apiKey == null) {
                return null;
            }

            String prompt = construirPromptResumo(curriculo);
            return sendRequest(apiKey, prompt);
        } catch (Exception e) {
            Log.e(TAG, "Erro ao gerar resumo", e);
            return null;
        }
    }

    /**
     * Otimiza o currículo para sistemas ATS
     */
    public List<String> otimizarParaATS(Curriculo curriculo, String vagaDescricao) {
        List<String> sugestoes = new ArrayList<>();
        
        try {
            String apiKey = configManager.getGeminiKey();
            if (apiKey == null) {
                sugestoes.add("Configure a chave API do Gemini nas configurações");
                return sugestoes;
            }

            String prompt = construirPromptATS(curriculo, vagaDescricao);
            String response = sendRequest(apiKey, prompt);
            
            if (response != null) {
                sugestoes = parseAnaliseResponse(response);
            }
        } catch (Exception e) {
            Log.e(TAG, "Erro ao otimizar para ATS", e);
            sugestoes.add("Erro ao processar otimização: " + e.getMessage());
        }
        
        return sugestoes;
    }

    /**
     * Envia requisição para a API do Gemini
     */
    private String sendRequest(String apiKey, String prompt) throws Exception {
        URL url = new URL(GEMINI_API_URL + "?key=" + apiKey);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        
        try {
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);

            // Construir JSON da requisição
            JSONObject requestBody = new JSONObject();
            JSONArray contents = new JSONArray();
            JSONObject content = new JSONObject();
            JSONArray parts = new JSONArray();
            JSONObject part = new JSONObject();
            
            part.put("text", prompt);
            parts.put(part);
            content.put("parts", parts);
            contents.put(content);
            requestBody.put("contents", contents);

            // Enviar requisição
            OutputStream os = conn.getOutputStream();
            os.write(requestBody.toString().getBytes(StandardCharsets.UTF_8));
            os.flush();
            os.close();

            // Ler resposta
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8)
                );
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    response.append(line);
                }
                br.close();

                // Parse da resposta
                JSONObject jsonResponse = new JSONObject(response.toString());
                JSONArray candidates = jsonResponse.getJSONArray("candidates");
                if (candidates.length() > 0) {
                    JSONObject candidate = candidates.getJSONObject(0);
                    JSONObject contentObj = candidate.getJSONObject("content");
                    JSONArray partsArray = contentObj.getJSONArray("parts");
                    if (partsArray.length() > 0) {
                        return partsArray.getJSONObject(0).getString("text");
                    }
                }
            } else {
                Log.e(TAG, "Erro na API: " + responseCode);
                BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8)
                );
                StringBuilder error = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    error.append(line);
                }
                br.close();
                Log.e(TAG, "Erro detalhado: " + error.toString());
            }
        } finally {
            conn.disconnect();
        }
        
        return null;
    }

    private String construirPromptAnalise(Curriculo curriculo) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Analise o seguinte currículo e forneça 5 sugestões específicas de melhoria. ");
        prompt.append("Seja direto e objetivo. Liste as sugestões numeradas de 1 a 5.\n\n");
        prompt.append("CURRÍCULO:\n");
        prompt.append("Nome: ").append(curriculo.getNomeCompleto()).append("\n");
        prompt.append("Email: ").append(curriculo.getEmail()).append("\n");
        prompt.append("Telefone: ").append(curriculo.getTelefone()).append("\n");
        
        if (curriculo.getResumoProfissional() != null) {
            prompt.append("\nResumo: ").append(curriculo.getResumoProfissional()).append("\n");
        }
        
        if (curriculo.getExperienciaProfissionalTexto() != null) {
            prompt.append("\nExperiência: ").append(curriculo.getExperienciaProfissionalTexto()).append("\n");
        }
        
        if (curriculo.getFormacaoAcademicaTexto() != null) {
            prompt.append("\nFormação: ").append(curriculo.getFormacaoAcademicaTexto()).append("\n");
        }
        
        if (curriculo.getHabilidadesTexto() != null) {
            prompt.append("\nHabilidades: ").append(curriculo.getHabilidadesTexto()).append("\n");
        }
        
        return prompt.toString();
    }

    private String construirPromptResumo(Curriculo curriculo) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Crie um resumo profissional impactante (máximo 3 parágrafos) baseado nas seguintes informações:\n\n");
        
        if (curriculo.getExperienciaProfissionalTexto() != null) {
            prompt.append("Experiência: ").append(curriculo.getExperienciaProfissionalTexto()).append("\n");
        }
        
        if (curriculo.getFormacaoAcademicaTexto() != null) {
            prompt.append("Formação: ").append(curriculo.getFormacaoAcademicaTexto()).append("\n");
        }
        
        if (curriculo.getHabilidadesTexto() != null) {
            prompt.append("Habilidades: ").append(curriculo.getHabilidadesTexto()).append("\n");
        }
        
        return prompt.toString();
    }

    private String construirPromptATS(Curriculo curriculo, String vagaDescricao) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Analise este currículo para otimização ATS (Applicant Tracking System) ");
        prompt.append("considerando a seguinte descrição de vaga. ");
        prompt.append("Forneça 5 sugestões específicas para melhorar a compatibilidade com ATS.\n\n");
        prompt.append("DESCRIÇÃO DA VAGA:\n").append(vagaDescricao).append("\n\n");
        prompt.append("CURRÍCULO:\n");
        prompt.append(construirPromptAnalise(curriculo));
        
        return prompt.toString();
    }

    private List<String> parseAnaliseResponse(String response) {
        List<String> sugestoes = new ArrayList<>();
        
        if (response == null || response.isEmpty()) {
            return sugestoes;
        }
        
        // Parse simples: dividir por linhas e filtrar numeradas
        String[] lines = response.split("\n");
        for (String line : lines) {
            line = line.trim();
            if (!line.isEmpty() && (
                line.matches("^\\d+\\..*") || 
                line.matches("^\\d+\\).*") ||
                line.matches("^-.*") ||
                line.matches("^\\*.*")
            )) {
                // Remover numeração/marcadores
                line = line.replaceFirst("^\\d+[\\.\\)]\\s*", "");
                line = line.replaceFirst("^[-\\*]\\s*", "");
                sugestoes.add(line);
            }
        }
        
        // Se não encontrou sugestões formatadas, retornar resposta completa
        if (sugestoes.isEmpty()) {
            sugestoes.add(response);
        }
        
        return sugestoes;
    }
}
