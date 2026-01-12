package com.example.geradorcurriculo.services;

import android.content.Context;
import com.example.geradorcurriculo.model.Curriculo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class AdvancedATSService extends ATSService {

    private static final Map<String, List<String>> INDUSTRY_KEYWORDS = new HashMap<>();
    private static final Map<String, Double> SKILL_WEIGHTS = new HashMap<>();
    
    static {
        // Tecnologia da Informação
        INDUSTRY_KEYWORDS.put("TI", Arrays.asList(
            "java", "python", "javascript", "react", "angular", "node.js", "spring boot",
            "aws", "azure", "docker", "kubernetes", "git", "sql", "nosql", "mongodb",
            "postgresql", "mysql", "oracle", "microservices", "api", "rest", "graphql",
            "machine learning", "ai", "data science", "big data", "cloud", "devops",
            "agile", "scrum", "kanban", "ci/cd", "tdd", "unit testing", "integration testing"
        ));
        
        // Gestão e Negócios
        INDUSTRY_KEYWORDS.put("GESTAO", Arrays.asList(
            "liderança", "gestão", "planejamento", "estratégia", "projetos", "equipe",
            "coordenação", "supervisão", "negociação", "comunicação", "apresentação",
            "análise", "resolução de problemas", "tomada de decisão", "inovação",
            "melhoria contínua", "processos", "qualidade", "metas", "resultados",
            "kpi", "dashboard", "budget", "forecast", "stakeholder", "roadmap"
        ));
        
        // Marketing
        INDUSTRY_KEYWORDS.put("MARKETING", Arrays.asList(
            "marketing digital", "seo", "sem", "social media", "content marketing",
            "email marketing", "analytics", "google analytics", "facebook ads", "google ads",
            "inbound marketing", "outbound marketing", "branding", "copywriting",
            "funil de vendas", "conversão", "tráfego", "engajamento", "crm"
        ));
        
        // Vendas
        INDUSTRY_KEYWORDS.put("VENDAS", Arrays.asList(
            "vendas", "negociação", "fechamento", "prospecção", "lead", "cliente",
            "crm", "funil de vendas", "metas", "comissão", "contrato", "proposta",
            "follow-up", "cold call", "networking", "relacionamento", "pós-venda"
        ));
        
        // Pesos para diferentes tipos de habilidades
        SKILL_WEIGHTS.put("technical", 1.5);
        SKILL_WEIGHTS.put("soft", 1.2);
        SKILL_WEIGHTS.put("leadership", 1.8);
        SKILL_WEIGHTS.put("certification", 1.3);
    }
    
    private final Context context;
    
    public AdvancedATSService(Context context) {
        this.context = context;
    }

    public EnhancedATSResult analisarCurriculoAvancado(Curriculo curriculo, String targetIndustry) {
        ATSResult basicResult = analisarCurriculo(curriculo);
        
        int score = basicResult.getScore();
        List<String> suggestions = new ArrayList<>(basicResult.getSuggestions());
        
        // Análise avançada por indústria
        score += analisarPorIndustria(curriculo, targetIndustry, suggestions);
        
        // Análise de senioridade
        score += analisarSenioridade(curriculo, suggestions);
        
        // Análise de impacto e métricas
        score += analisarImpacto(curriculo, suggestions);
        
        // Análise de palavras-chave em alta
        score += analisarTendenciasMercado(curriculo, suggestions);
        
        // Análise de estrutura otimizada
        score += analisarEstruturaOtimizada(curriculo, suggestions);
        
        score = Math.min(100, Math.max(0, score));
        
        return new EnhancedATSResult(score, suggestions, detectarPerfil(curriculo), gerarInsights(curriculo));
    }
    
    private int analisarPorIndustria(Curriculo curriculo, String industry, List<String> suggestions) {
        if (industry == null || !INDUSTRY_KEYWORDS.containsKey(industry)) {
            return 0;
        }
        
        String texto = montarTextoCompleto(curriculo).toLowerCase();
        List<String> keywords = INDUSTRY_KEYWORDS.get(industry);
        
        int matchCount = 0;
        for (String keyword : keywords) {
            if (texto.contains(keyword.toLowerCase())) {
                matchCount++;
            }
        }
        
        int score = (matchCount * 100) / keywords.size();
        
        if (score < 30) {
            suggestions.add("Adicione mais palavras-chave específicas da área de " + industry);
        }
        
        return Math.min(20, score / 5);
    }
    
    private int analisarSenioridade(Curriculo curriculo, List<String> suggestions) {
        String texto = montarTextoCompleto(curriculo).toLowerCase();
        
        int seniorityScore = 0;
        
        // Indicadores de senioridade
        if (texto.contains("sênior") || texto.contains("senior")) seniorityScore += 10;
        if (texto.contains("pleno") || texto.contains("mid")) seniorityScore += 7;
        if (texto.contains("júnior") || texto.contains("junior")) seniorityScore += 3;
        if (texto.contains("lead")) seniorityScore += 8;
        if (texto.contains("manager") || texto.contains("gerente")) seniorityScore += 10;
        if (texto.contains("director") || texto.contains("diretor")) seniorityScore += 12;
        if (texto.contains("coordenador")) seniorityScore += 8;
        if (texto.contains("especialista")) seniorityScore += 6;
        
        // Anos de experiência
        if (texto.matches(".*\\b(10|[1-9]\\d+)\\s*anos?.*")) {
            seniorityScore += 10;
        } else if (texto.matches(".*\\b([5-9])\\s*anos?.*")) {
            seniorityScore += 7;
        } else if (texto.matches(".*\\b([2-4])\\s*anos?.*")) {
            seniorityScore += 4;
        }
        
        return Math.min(15, seniorityScore);
    }
    
    private int analisarImpacto(Curriculo curriculo, List<String> suggestions) {
        String texto = montarTextoCompleto(curriculo);
        
        int impactScore = 0;
        
        // Métricas e resultados quantificáveis
        if (texto.matches(".*\\d+%.*")) impactScore += 5;
        if (texto.matches(".*\\d+\\s*(mil|milhar|k).*")) impactScore += 5;
        if (texto.matches(".*\\d+\\s*(milhão|mi|m).*")) impactScore += 8;
        if (texto.matches(".*\\d+\\s*(reais|r\\$|us\\$|\\$).*")) impactScore += 5;
        
        // Verbos de impacto
        String[] impactVerbs = {"aumentei", "reduzi", "otimizei", "melhorei", "economizei", 
                               "gerenciei", "liderei", "implementei", "desenvolvi", "criei"};
        
        for (String verb : impactVerbs) {
            if (texto.toLowerCase().contains(verb)) {
                impactScore += 2;
            }
        }
        
        if (impactScore < 10) {
            suggestions.add("Adicione métricas e resultados quantificáveis (ex: 'aumentei vendas em 30%')");
        }
        
        return Math.min(20, impactScore);
    }
    
    private int analisarTendenciasMercado(Curriculo curriculo, List<String> suggestions) {
        String texto = montarTextoCompleto(curriculo).toLowerCase();
        
        // Palavras-chave em alta no mercado atual
        String[] trendingKeywords = {
            "remote work", "trabalho remoto", "hybrid", "agile", "scrum",
            "cloud", "aws", "azure", "devops", "microservices",
            "data science", "machine learning", "ai", "ia",
            "digital transformation", "transformação digital",
            "sustainability", "esg", "diversity", "inclusão"
        };
        
        int trendingScore = 0;
        for (String keyword : trendingKeywords) {
            if (texto.contains(keyword)) {
                trendingScore += 3;
            }
        }
        
        if (trendingScore < 6) {
            suggestions.add("Considere incluir habilidades em alta como trabalho remoto, cloud, ou transformação digital");
        }
        
        return Math.min(15, trendingScore);
    }
    
    private int analisarEstruturaOtimizada(Curriculo curriculo, List<String> suggestions) {
        int structureScore = 0;
        
        // Tamanho ideal do currículo
        String texto = montarTextoCompleto(curriculo);
        int wordCount = texto.split("\\s+").length;
        
        if (wordCount >= 300 && wordCount <= 600) {
            structureScore += 10;
        } else if (wordCount < 300) {
            suggestions.add("Seu currículo parece muito curto. Adicione mais detalhes sobre suas experiências");
        } else {
            suggestions.add("Considere resumir seu currículo para 300-600 palavras para melhor leitura");
        }
        
        // Seções essenciais
        if (curriculo.getResumoProfissional() != null && !curriculo.getResumoProfissional().trim().isEmpty()) {
            structureScore += 5;
        }
        
        if (curriculo.getExperiencias() != null && !curriculo.getExperiencias().isEmpty()) {
            structureScore += 5;
        }
        
        if (curriculo.getHabilidades() != null && !curriculo.getHabilidades().isEmpty()) {
            structureScore += 5;
        }
        
        return Math.min(25, structureScore);
    }
    
    private String detectarPerfil(Curriculo curriculo) {
        String texto = montarTextoCompleto(curriculo).toLowerCase();
        
        if (texto.contains("sênior") || texto.contains("senior") || 
            texto.contains("director") || texto.contains("gerente")) {
            return "Sênior/Leadership";
        } else if (texto.contains("pleno") || texto.contains("mid") || 
                   texto.contains("lead")) {
            return "Pleno/Intermediário";
        } else {
            return "Júnior/Iniciante";
        }
    }
    
    private List<String> gerarInsights(Curriculo curriculo) {
        List<String> insights = new ArrayList<>();
        
        String texto = montarTextoCompleto(curriculo);
        
        // Insight sobre formato
        if (texto.length() < 500) {
            insights.add("💡 Seu currículo está conciso. Considere adicionar mais detalhes sobre suas conquistas.");
        } else if (texto.length() > 2000) {
            insights.add("💡 Seu currículo está muito extenso. Recrutadores gastam em média 7 segundos por currículo.");
        }
        
        // Insight sobre palavras-chave
        long keywordCount = INDUSTRY_KEYWORDS.values().stream()
            .flatMap(List::stream)
            .filter(keyword -> texto.toLowerCase().contains(keyword))
            .count();
            
        if (keywordCount > 15) {
            insights.add("🎯 Ótimo uso de palavras-chave! Seu currículo tem boa visibilidade para sistemas ATS.");
        } else if (keywordCount < 5) {
            insights.add("⚠️ Poucas palavras-chave encontradas. Isso pode afetar sua visibilidade em sistemas ATS.");
        }
        
        return insights;
    }
    
    public static class EnhancedATSResult extends ATSResult {
        private final String perfil;
        private final List<String> insights;
        
        public EnhancedATSResult(int score, List<String> suggestions, String perfil, List<String> insights) {
            super(score, suggestions);
            this.perfil = perfil;
            this.insights = insights;
        }
        
        public String getPerfil() {
            return perfil;
        }
        
        public List<String> getInsights() {
            return insights;
        }
    }
}
