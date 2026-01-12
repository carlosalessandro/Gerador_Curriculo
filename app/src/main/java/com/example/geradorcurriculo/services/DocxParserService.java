package com.example.geradorcurriculo.services;

import android.content.Context;
import android.net.Uri;
import com.example.geradorcurriculo.model.Curriculo;
// import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
// import org.apache.poi.xwpf.usermodel.XWPFDocument;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DocxParserService {

    public Curriculo parseDOCX(Context context, Uri uri) throws IOException {
        // TODO: Implementar quando Apache POI estiver disponível
        throw new UnsupportedOperationException("Importação de DOCX não implementada ainda");
        /*
        String texto = extrairTextoDOCX(context, uri);
        if (texto == null || texto.trim().isEmpty()) {
            return null;
        }
        
        return extrairInformacoesCurriculo(texto);
        */
    }

    /*
    private String extrairTextoDOCX(Context context, Uri uri) throws IOException {
        try (InputStream inputStream = context.getContentResolver().openInputStream(uri);
             XWPFDocument document = new XWPFDocument(inputStream);
             XWPFWordExtractor extractor = new XWPFWordExtractor(document)) {
            
            return extractor.getText();
        }
    }

    private Curriculo extrairInformacoesCurriculo(String texto) {
        Curriculo curriculo = new Curriculo();
        texto = texto.replaceAll("\\s+", " ").trim();
        
        curriculo.setNomeCompleto(extrairNome(texto));
        curriculo.setEmail(extrairEmail(texto));
        curriculo.setTelefone(extrairTelefone(texto));
        curriculo.setLinkedin(extrairLinkedIn(texto));
        curriculo.setGithub(extrairGithub(texto));
        curriculo.setResumoProfissional(extrairResumo(texto));
        
        return curriculo;
    }

    private String extrairNome(String texto) {
        Pattern[] patterns = {
            Pattern.compile("^([A-Z][a-z]+ [A-Z][a-z]+(?: [A-Z][a-z]+)*)", Pattern.MULTILINE),
            Pattern.compile("NOME:?\\s*([A-Z][a-z]+ [A-Z][a-z]+(?: [A-Z][a-z]+)*)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("CURRICULUM VITAE\\s*([A-Z][a-z]+ [A-Z][a-z]+(?: [A-Z][a-z]+)*)", Pattern.CASE_INSENSITIVE)
        };
        
        for (Pattern pattern : patterns) {
            Matcher matcher = pattern.matcher(texto);
            if (matcher.find()) {
                return matcher.group(1).trim();
            }
        }
        return "";
    }

    private String extrairEmail(String texto) {
        Pattern pattern = Pattern.compile("\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\\b");
        Matcher matcher = pattern.matcher(texto);
        return matcher.find() ? matcher.group() : "";
    }

    private String extrairTelefone(String texto) {
        Pattern[] patterns = {
            Pattern.compile("\\(\\d{2}\\)\\s*\\d{4,5}-\\d{4}"),
            Pattern.compile("\\d{2}\\s*\\d{4,5}-\\d{4}"),
            Pattern.compile("\\+\\d{2}\\s*\\d{2}\\s*\\d{4,5}-\\d{4}"),
            Pattern.compile("TELEFONE:?\\s*([\\d\\s\\-\\(\\)]+)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("CELULAR:?\\s*([\\d\\s\\-\\(\\)]+)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("WHATSAPP:?\\s*([\\d\\s\\-\\(\\)]+)", Pattern.CASE_INSENSITIVE)
        };
        
        for (Pattern pattern : patterns) {
            Matcher matcher = pattern.matcher(texto);
            if (matcher.find()) {
                String telefone = matcher.groupCount() > 0 ? matcher.group(1) : matcher.group();
                return telefone.trim();
            }
        }
        return "";
    }

    private String extrairLinkedIn(String texto) {
        Pattern[] patterns = {
            Pattern.compile("linkedin\\.com/in/[a-zA-Z0-9-]+"),
            Pattern.compile("LINKEDIN:?\\s*(linkedin\\.com/in/[a-zA-Z0-9-]+)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("perfil profissional:?\\s*(linkedin\\.com/in/[a-zA-Z0-9-]+)", Pattern.CASE_INSENSITIVE)
        };
        
        for (Pattern pattern : patterns) {
            Matcher matcher = pattern.matcher(texto);
            if (matcher.find()) {
                String linkedin = matcher.groupCount() > 0 ? matcher.group(1) : matcher.group();
                return linkedin.trim();
            }
        }
        return "";
    }

    private String extrairGithub(String texto) {
        Pattern[] patterns = {
            Pattern.compile("github\\.com/[a-zA-Z0-9-]+"),
            Pattern.compile("GITHUB:?\\s*(github\\.com/[a-zA-Z0-9-]+)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("GIT:?\\s*(github\\.com/[a-zA-Z0-9-]+)", Pattern.CASE_INSENSITIVE)
        };
        
        for (Pattern pattern : patterns) {
            Matcher matcher = pattern.matcher(texto);
            if (matcher.find()) {
                String github = matcher.groupCount() > 0 ? matcher.group(1) : matcher.group();
                return github.trim();
            }
        }
        return "";
    }

    private String extrairResumo(String texto) {
        Pattern[] patterns = {
            Pattern.compile("RESUMO PROFISSIONAL:?\\s*(.+?)(?=\\n\\n|EXPERIÊNCIA|FORMAÇÃO|HABILIDADES|$)", Pattern.CASE_INSENSITIVE | Pattern.DOTALL),
            Pattern.compile("OBJETIVO:?\\s*(.+?)(?=\\n\\n|EXPERIÊNCIA|FORMAÇÃO|HABILIDADES|$)", Pattern.CASE_INSENSITIVE | Pattern.DOTALL),
            Pattern.compile("SOBRE MIM:?\\s*(.+?)(?=\\n\\n|EXPERIÊNCIA|FORMAÇÃO|HABILIDADES|$)", Pattern.CASE_INSENSITIVE | Pattern.DOTALL),
            Pattern.compile("PROFILE:?\\s*(.+?)(?=\\n\\n|EXPERIÊNCIA|FORMAÇÃO|HABILIDADES|$)", Pattern.CASE_INSENSITIVE | Pattern.DOTALL)
        };
        
        for (Pattern pattern : patterns) {
            Matcher matcher = pattern.matcher(texto);
            if (matcher.find()) {
                String resumo = matcher.group(1).trim();
                return resumo.length() > 500 ? resumo.substring(0, 500) + "..." : resumo;
            }
        }
        return "";
    }
    */
}
