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

    /**
     * Otimiza o currículo automaticamente para ATS e melhores práticas de recrutamento
     */
    public Curriculo otimizarCurriculoAutomaticamente(Curriculo curriculo) {
        try {
            String apiKey = configManager.getGeminiKey();
            if (apiKey == null) {
                return curriculo;
            }

            String prompt = construirPromptOtimizacaoCompleta(curriculo);
            String response = sendRequest(apiKey, prompt);
            
            if (response != null) {
                return aplicarOtimizacoes(curriculo, response);
            }
        } catch (Exception e) {
            Log.e(TAG, "Erro ao otimizar currículo", e);
        }
        
        return curriculo;
    }

    /**
     * Analisa compatibilidade com ATS e retorna score
     */
    public int calcularScoreATS(Curriculo curriculo) {
        try {
            String apiKey = configManager.getGeminiKey();
            if (apiKey == null) {
                return 0;
            }

            String prompt = construirPromptScoreATS(curriculo);
            String response = sendRequest(apiKey, prompt);
            
            if (response != null) {
                return extrairScore(response);
            }
        } catch (Exception e) {
            Log.e(TAG, "Erro ao calcular score ATS", e);
        }
        
        return 0;
    }

    /**
     * Gera palavras-chave otimizadas para ATS baseado na área de atuação
     */
    public List<String> gerarPalavrasChaveATS(String areaAtuacao, String cargo) {
        List<String> palavrasChave = new ArrayList<>();
        
        try {
            String apiKey = configManager.getGeminiKey();
            if (apiKey == null) {
                return palavrasChave;
            }

            String prompt = construirPromptPalavrasChave(areaAtuacao, cargo);
            String response = sendRequest(apiKey, prompt);
            
            if (response != null) {
                palavrasChave = parsePalavrasChave(response);
            }
        } catch (Exception e) {
            Log.e(TAG, "Erro ao gerar palavras-chave", e);
        }
        
        return palavrasChave;
    }

    /**
     * Melhora descrição de experiência profissional
     */
    public String melhorarDescricaoExperiencia(String descricaoOriginal, String cargo) {
        try {
            String apiKey = configManager.getGeminiKey();
            if (apiKey == null) {
                return descricaoOriginal;
            }

            String prompt = construirPromptMelhorarExperiencia(descricaoOriginal, cargo);
            return sendRequest(apiKey, prompt);
        } catch (Exception e) {
            Log.e(TAG, "Erro ao melhorar experiência", e);
            return descricaoOriginal;
        }
    }

    /**
     * Verifica erros gramaticais e ortográficos
     */
    public List<String> verificarErrosGramaticais(Curriculo curriculo) {
        List<String> erros = new ArrayList<>();
        
        try {
            String apiKey = configManager.getGeminiKey();
            if (apiKey == null) {
                return erros;
            }

            String prompt = construirPromptVerificarErros(curriculo);
            String response = sendRequest(apiKey, prompt);
            
            if (response != null) {
                erros = parseAnaliseResponse(response);
            }
        } catch (Exception e) {
            Log.e(TAG, "Erro ao verificar gramática", e);
        }
        
        return erros;
    }

    private String construirPromptOtimizacaoCompleta(Curriculo curriculo) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Você é um especialista em recrutamento e sistemas ATS. ");
        prompt.append("Otimize este currículo seguindo as melhores práticas:\n\n");
        prompt.append("REGRAS DE OTIMIZAÇÃO:\n");
        prompt.append("1. Use verbos de ação no início das descrições\n");
        prompt.append("2. Quantifique resultados sempre que possível\n");
        prompt.append("3. Inclua palavras-chave relevantes para ATS\n");
        prompt.append("4. Mantenha linguagem profissional e objetiva\n");
        prompt.append("5. Destaque conquistas e impacto\n");
        prompt.append("6. Evite jargões e termos vagos\n");
        prompt.append("7. Use formatação compatível com ATS\n\n");
        
        prompt.append("CURRÍCULO ATUAL:\n");
        prompt.append("Nome: ").append(curriculo.getNomeCompleto()).append("\n");
        
        if (curriculo.getResumoProfissional() != null) {
            prompt.append("\nResumo Profissional:\n").append(curriculo.getResumoProfissional()).append("\n");
        }
        
        if (curriculo.getExperienciaProfissionalTexto() != null) {
            prompt.append("\nExperiência Profissional:\n").append(curriculo.getExperienciaProfissionalTexto()).append("\n");
        }
        
        if (curriculo.getFormacaoAcademicaTexto() != null) {
            prompt.append("\nFormação Acadêmica:\n").append(curriculo.getFormacaoAcademicaTexto()).append("\n");
        }
        
        if (curriculo.getHabilidadesTexto() != null) {
            prompt.append("\nHabilidades:\n").append(curriculo.getHabilidadesTexto()).append("\n");
        }
        
        prompt.append("\nRETORNE O CURRÍCULO OTIMIZADO NO SEGUINTE FORMATO:\n");
        prompt.append("RESUMO: [resumo otimizado]\n");
        prompt.append("EXPERIENCIA: [experiência otimizada]\n");
        prompt.append("FORMACAO: [formação otimizada]\n");
        prompt.append("HABILIDADES: [habilidades otimizadas]\n");
        
        return prompt.toString();
    }

    private String construirPromptScoreATS(Curriculo curriculo) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Analise este currículo e atribua um score de 0 a 100 baseado em compatibilidade com sistemas ATS.\n\n");
        prompt.append("CRITÉRIOS DE AVALIAÇÃO:\n");
        prompt.append("- Uso de palavras-chave relevantes (25 pontos)\n");
        prompt.append("- Formatação compatível com ATS (20 pontos)\n");
        prompt.append("- Clareza e objetividade (20 pontos)\n");
        prompt.append("- Quantificação de resultados (15 pontos)\n");
        prompt.append("- Verbos de ação (10 pontos)\n");
        prompt.append("- Ausência de erros (10 pontos)\n\n");
        
        prompt.append("CURRÍCULO:\n");
        prompt.append(construirPromptAnalise(curriculo));
        prompt.append("\nRETORNE APENAS O NÚMERO DO SCORE (0-100).\n");
        
        return prompt.toString();
    }

    private String construirPromptPalavrasChave(String areaAtuacao, String cargo) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Liste 15 palavras-chave essenciais para sistemas ATS na área de ");
        prompt.append(areaAtuacao).append(" para o cargo de ").append(cargo).append(".\n\n");
        prompt.append("Inclua:\n");
        prompt.append("- Habilidades técnicas\n");
        prompt.append("- Ferramentas e tecnologias\n");
        prompt.append("- Competências comportamentais\n");
        prompt.append("- Certificações relevantes\n\n");
        prompt.append("Retorne apenas as palavras-chave, uma por linha, sem numeração.\n");
        
        return prompt.toString();
    }

    private String construirPromptMelhorarExperiencia(String descricaoOriginal, String cargo) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Reescreva esta descrição de experiência profissional para o cargo de ");
        prompt.append(cargo).append(" seguindo as melhores práticas:\n\n");
        prompt.append("DESCRIÇÃO ORIGINAL:\n").append(descricaoOriginal).append("\n\n");
        prompt.append("MELHORIAS NECESSÁRIAS:\n");
        prompt.append("1. Comece com verbos de ação fortes\n");
        prompt.append("2. Quantifique resultados (use números, percentuais)\n");
        prompt.append("3. Destaque impacto e conquistas\n");
        prompt.append("4. Use palavras-chave relevantes para ATS\n");
        prompt.append("5. Seja específico e objetivo\n");
        prompt.append("6. Máximo de 3-4 linhas\n\n");
        prompt.append("Retorne apenas a descrição melhorada.\n");
        
        return prompt.toString();
    }

    private String construirPromptVerificarErros(Curriculo curriculo) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Analise este currículo e identifique APENAS erros gramaticais, ortográficos e de formatação.\n\n");
        prompt.append("CURRÍCULO:\n");
        prompt.append(construirPromptAnalise(curriculo));
        prompt.append("\nListe os erros encontrados, um por linha. Se não houver erros, retorne 'Nenhum erro encontrado'.\n");
        
        return prompt.toString();
    }

    private Curriculo aplicarOtimizacoes(Curriculo curriculo, String response) {
        try {
            // Parse da resposta estruturada
            String[] sections = response.split("\n");
            
            for (String section : sections) {
                if (section.startsWith("RESUMO:")) {
                    String resumo = section.substring(7).trim();
                    if (!resumo.isEmpty()) {
                        curriculo.setResumoProfissional(resumo);
                    }
                } else if (section.startsWith("EXPERIENCIA:")) {
                    String experiencia = section.substring(12).trim();
                    if (!experiencia.isEmpty()) {
                        curriculo.setExperienciaProfissional(experiencia);
                    }
                } else if (section.startsWith("FORMACAO:")) {
                    String formacao = section.substring(9).trim();
                    if (!formacao.isEmpty()) {
                        curriculo.setFormacaoAcademica(formacao);
                    }
                } else if (section.startsWith("HABILIDADES:")) {
                    String habilidades = section.substring(12).trim();
                    if (!habilidades.isEmpty()) {
                        curriculo.setHabilidades(habilidades);
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Erro ao aplicar otimizações", e);
        }
        
        return curriculo;
    }

    private int extrairScore(String response) {
        try {
            // Extrair número da resposta
            String cleaned = response.replaceAll("[^0-9]", "");
            if (!cleaned.isEmpty()) {
                int score = Integer.parseInt(cleaned);
                return Math.min(100, Math.max(0, score)); // Garantir entre 0-100
            }
        } catch (Exception e) {
            Log.e(TAG, "Erro ao extrair score", e);
        }
        return 0;
    }

    private List<String> parsePalavrasChave(String response) {
        List<String> palavras = new ArrayList<>();
        
        if (response == null || response.isEmpty()) {
            return palavras;
        }
        
        String[] lines = response.split("\n");
        for (String line : lines) {
            line = line.trim();
            // Remover numeração e marcadores
            line = line.replaceFirst("^\\d+[\\.\\)]\\s*", "");
            line = line.replaceFirst("^[-\\*]\\s*", "");
            
            if (!line.isEmpty() && line.length() > 2) {
                palavras.add(line);
            }
        }
        
        return palavras;
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
