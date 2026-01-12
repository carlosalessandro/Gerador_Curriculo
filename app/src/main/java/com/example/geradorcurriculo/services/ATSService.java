package com.example.geradorcurriculo.services;

import com.example.geradorcurriculo.model.Curriculo;
import com.example.geradorcurriculo.model.ExperienciaProfissional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class ATSService {

    private static final List<String> KEYWORDS_TECNOLOGIA = Arrays.asList(
        "java", "python", "javascript", "react", "angular", "node.js", "spring boot",
        "aws", "azure", "docker", "kubernetes", "git", "sql", "nosql", "mongodb",
        "postgresql", "mysql", "oracle", "microservices", "api", "rest", "graphql",
        "machine learning", "ai", "data science", "big data", "cloud", "devops",
        "agile", "scrum", "kanban", "ci/cd", "tdd", "unit testing", "integration testing"
    );

    private static final List<String> KEYWORDS_GESTAO = Arrays.asList(
        "liderança", "gestão", "planejamento", "estratégia", "projetos", "equipe",
        "coordenação", "supervisão", "negociação", "comunicação", "apresentação",
        "análise", "resolução de problemas", "tomada de decisão", "inovação",
        "melhoria contínua", "processos", "qualidade", "metas", "resultados"
    );

    private static final List<String> ACTION_VERBS = Arrays.asList(
        "desenvolvi", "implementei", "criei", "gerenciei", "liderei", "coordenei",
        "otimizei", "melhorei", "reduzi", "aumentei", "implementei", "desenvolvi",
        "projetei", "arquitetei", "construí", "desenvolvi", "mantive", "suportei",
        "analisei", "avaliei", "testei", "documentei", "treinei", "mentorei"
    );

    private static final List<String> SKILLS_WORDS = Arrays.asList(
        "habilidades", "competências", "skills", "qualificações", "conhecimentos",
        "experiência", "profissional", "técnico", "senior", "junior", "pleno",
        "especialista", "analista", "desenvolvedor", "engenheiro", "gerente"
    );

    public ATSResult analisarCurriculo(Curriculo curriculo) {
        int score = 0;
        List<String> suggestions = new ArrayList<>();

        String textoCompleto = montarTextoCompleto(curriculo);
        
        score += avaliarEstrutura(curriculo, suggestions);
        score += avaliarPalavrasChave(textoCompleto, suggestions);
        score += avaliarVerbosAcao(textoCompleto, suggestions);
        score += avaliarFormato(curriculo, suggestions);
        score += avaliarConteudo(textoCompleto, suggestions);

        score = Math.min(100, Math.max(0, score));

        return new ATSResult(score, suggestions);
    }

    protected String montarTextoCompleto(Curriculo curriculo) {
        StringBuilder texto = new StringBuilder();
        
        if (curriculo.getNomeCompleto() != null) texto.append(curriculo.getNomeCompleto()).append(" ");
        if (curriculo.getObjetivo() != null) texto.append(curriculo.getObjetivo()).append(" ");
        if (curriculo.getResumoProfissional() != null) texto.append(curriculo.getResumoProfissional()).append(" ");
        
        if (curriculo.getExperiencias() != null) {
            for (int i = 0; i < curriculo.getExperiencias().size(); i++) {
                ExperienciaProfissional exp = curriculo.getExperiencias().get(i);
                texto.append(exp.getCargo()).append(" ");
                texto.append(exp.getDescricao()).append(" ");
            }
        }
        
        if (curriculo.getHabilidades() != null) {
            for (int i = 0; i < curriculo.getHabilidades().size(); i++) {
                texto.append(curriculo.getHabilidades().get(i).getNome()).append(" ");
            }
        }
        
        return texto.toString().toLowerCase();
    }

    private int avaliarEstrutura(Curriculo curriculo, List<String> suggestions) {
        int score = 0;
        
        if (curriculo.getNomeCompleto() != null && !curriculo.getNomeCompleto().trim().isEmpty()) {
            score += 5;
        } else {
            suggestions.add("Adicione seu nome completo no início do currículo");
        }
        
        if (curriculo.getEmail() != null && !curriculo.getEmail().trim().isEmpty()) {
            score += 5;
        } else {
            suggestions.add("Inclua seu e-mail para contato");
        }
        
        if (curriculo.getTelefone() != null && !curriculo.getTelefone().trim().isEmpty()) {
            score += 5;
        } else {
            suggestions.add("Adicione seu telefone para contato");
        }
        
        if (curriculo.getResumoProfissional() != null && !curriculo.getResumoProfissional().trim().isEmpty()) {
            score += 10;
        } else {
            suggestions.add("Adicione um resumo profissional destacando suas principais qualificações");
        }
        
        if (curriculo.getExperiencias() != null && !curriculo.getExperiencias().isEmpty()) {
            score += 15;
        } else {
            suggestions.add("Inclua suas experiências profissionais anteriores");
        }
        
        if (curriculo.getFormacoes() != null && !curriculo.getFormacoes().isEmpty()) {
            score += 10;
        } else {
            suggestions.add("Adicione sua formação acadêmica");
        }
        
        return score;
    }

    private int avaliarPalavrasChave(String texto, List<String> suggestions) {
        int score = 0;
        int keywordsCount = 0;
        
        for (String keyword : KEYWORDS_TECNOLOGIA) {
            if (texto.contains(keyword.toLowerCase())) {
                keywordsCount++;
            }
        }
        
        for (String keyword : KEYWORDS_GESTAO) {
            if (texto.contains(keyword.toLowerCase())) {
                keywordsCount++;
            }
        }
        
        if (keywordsCount >= 10) {
            score += 20;
        } else if (keywordsCount >= 5) {
            score += 15;
        } else if (keywordsCount >= 3) {
            score += 10;
        } else {
            score += 5;
            suggestions.add("Adicione mais palavras-chave relevantes para sua área de atuação");
        }
        
        return score;
    }

    private int avaliarVerbosAcao(String texto, List<String> suggestions) {
        int score = 0;
        int verbsCount = 0;
        
        for (String verb : ACTION_VERBS) {
            if (texto.contains(verb.toLowerCase())) {
                verbsCount++;
            }
        }
        
        if (verbsCount >= 8) {
            score += 15;
        } else if (verbsCount >= 5) {
            score += 10;
        } else if (verbsCount >= 3) {
            score += 5;
        } else {
            suggestions.add("Use mais verbos de ação para descrever suas experiências (ex: desenvolvi, gerenciei, otimizei)");
        }
        
        return score;
    }

    private int avaliarFormato(Curriculo curriculo, List<String> suggestions) {
        int score = 0;
        
        if (curriculo.getLinkedin() != null && !curriculo.getLinkedin().trim().isEmpty()) {
            score += 5;
        } else {
            suggestions.add("Considere adicionar seu perfil do LinkedIn");
        }
        
        if (curriculo.getHabilidades() != null && !curriculo.getHabilidades().isEmpty()) {
            score += 10;
        } else {
            suggestions.add("Adicione uma seção de habilidades técnicas");
        }
        
        if (curriculo.getIdiomas() != null && !curriculo.getIdiomas().isEmpty()) {
            score += 5;
        } else {
            suggestions.add("Inclua idiomas que você fala");
        }
        
        return score;
    }

    private int avaliarConteudo(String texto, List<String> suggestions) {
        int score = 0;
        
        if (texto.length() >= 500) {
            score += 10;
        } else {
            suggestions.add("Seu currículo parece muito curto. Adicione mais detalhes sobre suas experiências");
        }
        
        boolean hasNumbers = Pattern.compile("\\d+").matcher(texto).find();
        if (hasNumbers) {
            score += 5;
        } else {
            suggestions.add("Inclua números e métricas para quantificar suas conquistas (ex: 'aumentei vendas em 30%')");
        }
        
        return score;
    }

    public static class ATSResult {
        private final int score;
        private final List<String> suggestions;

        public ATSResult(int score, List<String> suggestions) {
            this.score = score;
            this.suggestions = suggestions;
        }

        public int getScore() {
            return score;
        }

        public List<String> getSuggestions() {
            return suggestions;
        }
    }
}
