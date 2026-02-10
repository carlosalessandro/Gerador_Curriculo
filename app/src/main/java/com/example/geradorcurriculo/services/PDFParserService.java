package com.example.geradorcurriculo.services;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import com.example.geradorcurriculo.model.Curriculo;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PDFParserService {
    private static final String TAG = "PDFParserService";

    public Curriculo parsePDF(Context context, Uri uri) {
        try {
            String texto = extrairTextoPDF(context, uri);
            Log.d(TAG, "Texto extraído: " + (texto.length() > 500 ? texto.substring(0, 500) : texto));
            if (texto.trim().isEmpty()) {
                return criarCurriculoVazio();
            }
            return extrairInformacoes(texto);
        } catch (Exception e) {
            Log.e(TAG, "Erro ao processar PDF", e);
            return criarCurriculoVazio();
        }
    }

    private String extrairTextoPDF(Context context, Uri uri) throws Exception {
        StringBuilder texto = new StringBuilder();
        InputStream inputStream = context.getContentResolver().openInputStream(uri);
        if (inputStream == null) {
            throw new Exception("Não foi possível abrir o arquivo");
        }
        
        PdfReader reader = new PdfReader(inputStream);
        PdfDocument pdfDoc = new PdfDocument(reader);
        
        int numberOfPages = pdfDoc.getNumberOfPages();
        for (int i = 1; i <= numberOfPages; i++) {
            String pageText = PdfTextExtractor.getTextFromPage(pdfDoc.getPage(i));
            texto.append(pageText).append("\n");
        }
        
        pdfDoc.close();
        reader.close();
        inputStream.close();
        
        return texto.toString();
    }

    private Curriculo criarCurriculoVazio() {
        Curriculo curriculo = new Curriculo();
        curriculo.setNomeCompleto("Currículo Importado de PDF");
        curriculo.setResumoProfissional("Não foi possível extrair dados automaticamente. Preencha manualmente.");
        return curriculo;
    }

    private Curriculo extrairInformacoes(String texto) {
        Curriculo curriculo = new Curriculo();
        curriculo.setNomeCompleto(extrairCampo(texto, PATTERNS_NOME, "Nome não identificado"));
        curriculo.setEmail(extrairCampo(texto, PATTERNS_EMAIL, ""));
        curriculo.setTelefone(extrairCampo(texto, PATTERNS_TELEFONE, ""));
        curriculo.setEndereco(extrairCampo(texto, PATTERNS_ENDERECO, ""));
        curriculo.setLinkedin(extrairCampo(texto, PATTERNS_LINKEDIN, ""));
        curriculo.setGithub(extrairCampo(texto, PATTERNS_GITHUB, ""));
        curriculo.setResumoProfissional(extrairCampo(texto, PATTERNS_RESUMO, "Currículo importado. Revise as informações."));
        curriculo.setExperienciaProfissional(extrairCampo(texto, PATTERNS_EXPERIENCIA, ""));
        curriculo.setFormacaoAcademica(extrairCampo(texto, PATTERNS_FORMACAO, ""));
        curriculo.setHabilidades(extrairCampo(texto, PATTERNS_HABILIDADES, ""));
        return curriculo;
    }

    private String extrairCampo(String texto, String[] patterns, String padrao) {
        for (String patternStr : patterns) {
            Pattern pattern = Pattern.compile(patternStr, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
            Matcher matcher = pattern.matcher(texto);
            if (matcher.find()) {
                String resultado = matcher.groupCount() > 0 ? matcher.group(1) : matcher.group();
                resultado = resultado.trim().replaceAll("\\s+", " ");
                if (!resultado.isEmpty()) {
                    return resultado;
                }
            }
        }
        return padrao;
    }

    private static final String[] PATTERNS_NOME = {"^([A-ZÀ-Ÿ][a-zà-ÿ]+(?:\\s+[A-ZÀ-Ÿ][a-zà-ÿ]+){1,3})","(?:NOME|NAME)\\s*:?\\s*([A-ZÀ-Ÿ][a-zà-ÿ]+(?:\\s+[A-ZÀ-Ÿ][a-zà-ÿ]+)+)"};
    private static final String[] PATTERNS_EMAIL = {"\\b([A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,})\\b"};
    private static final String[] PATTERNS_TELEFONE = {"\\+?55\\s*\\(?\\d{2}\\)?\\s*9?\\d{4}[-\\s]?\\d{4}","\\(?\\d{2}\\)?\\s*9?\\d{4}[-\\s]?\\d{4}"};
    private static final String[] PATTERNS_ENDERECO = {"(?:ENDEREÇO|ENDERECO|ADDRESS)\\s*:?\\s*(.+?)(?=\\n(?:[A-Z]{3,}|$))"};
    private static final String[] PATTERNS_LINKEDIN = {"(?:https?://)?(?:www\\.)?linkedin\\.com/in/[a-zA-Z0-9-]+"};
    private static final String[] PATTERNS_GITHUB = {"(?:https?://)?(?:www\\.)?github\\.com/[a-zA-Z0-9-]+"};
    private static final String[] PATTERNS_RESUMO = {"(?:RESUMO|OBJETIVO|PERFIL)\\s*(?:PROFISSIONAL)?\\s*:?\\s*(.{50,800}?)(?=\\n\\s*(?:EXPERIÊNCIA|FORMAÇÃO|EDUCAÇÃO|HABILIDADES|$))"};
    private static final String[] PATTERNS_EXPERIENCIA = {"(?:EXPERIÊNCIA|EXPERIENCIA)\\s*(?:PROFISSIONAL)?\\s*:?\\s*(.{50,2000}?)(?=\\n\\s*(?:FORMAÇÃO|EDUCAÇÃO|HABILIDADES|$))"};
    private static final String[] PATTERNS_FORMACAO = {"(?:FORMAÇÃO|FORMACAO|EDUCAÇÃO|EDUCACAO)\\s*(?:ACADÊMICA|ACADEMICA)?\\s*:?\\s*(.{50,1500}?)(?=\\n\\s*(?:EXPERIÊNCIA|HABILIDADES|IDIOMAS|$))"};
    private static final String[] PATTERNS_HABILIDADES = {"(?:HABILIDADES|COMPETÊNCIAS|COMPETENCIAS)\\s*(?:TÉCNICAS)?\\s*:?\\s*(.{30,1000}?)(?=\\n\\s*(?:IDIOMAS|CERTIFICAÇÕES|PROJETOS|$))"};
}
