package com.example.geradorcurriculo.services;

import android.content.Context;
import android.os.Environment;
import com.example.geradorcurriculo.model.Curriculo;
import com.example.geradorcurriculo.model.ExperienciaProfissional;
import com.example.geradorcurriculo.model.Formacao;
import com.example.geradorcurriculo.model.Habilidade;
import com.example.geradorcurriculo.model.Idioma;
import com.example.geradorcurriculo.model.Certificacao;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.List;
import com.itextpdf.layout.element.ListItem;
import com.itextpdf.layout.element.LineSeparator;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PDFGeneratorService {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/yyyy", Locale.getDefault());

    public String gerarPDFCurriculo(Context context, Curriculo curriculo) throws IOException {
        File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        String fileName = "curriculo_" + curriculo.getNomeCompleto().replaceAll("\\s+", "_") + "_" + 
                         new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date()) + ".pdf";
        File outputFile = new File(downloadsDir, fileName);

        PdfWriter writer = new PdfWriter(outputFile);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        try {
            PdfFont fontBold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
            PdfFont fontNormal = PdfFontFactory.createFont(StandardFonts.HELVETICA);

            adicionarCabecalho(document, curriculo, fontBold, fontNormal);
            document.add(new LineSeparator(new SolidLine()));
            
            if (curriculo.getResumoProfissional() != null && !curriculo.getResumoProfissional().trim().isEmpty()) {
                adicionarResumoProfissional(document, curriculo, fontBold, fontNormal);
                document.add(new LineSeparator(new SolidLine()));
            }
            
            if (curriculo.getExperiencias() != null && !curriculo.getExperiencias().isEmpty()) {
                adicionarExperiencias(document, curriculo, fontBold, fontNormal);
                document.add(new LineSeparator(new SolidLine()));
            }
            
            if (curriculo.getFormacoes() != null && !curriculo.getFormacoes().isEmpty()) {
                adicionarFormacoes(document, curriculo, fontBold, fontNormal);
                document.add(new LineSeparator(new SolidLine()));
            }
            
            if (curriculo.getHabilidades() != null && !curriculo.getHabilidades().isEmpty()) {
                adicionarHabilidades(document, curriculo, fontBold, fontNormal);
                document.add(new LineSeparator(new SolidLine()));
            }
            
            if (curriculo.getIdiomas() != null && !curriculo.getIdiomas().isEmpty()) {
                adicionarIdiomas(document, curriculo, fontBold, fontNormal);
                document.add(new LineSeparator(new SolidLine()));
            }
            
            if (curriculo.getCertificacoes() != null && !curriculo.getCertificacoes().isEmpty()) {
                adicionarCertificacoes(document, curriculo, fontBold, fontNormal);
            }

            document.close();
            return outputFile.getAbsolutePath();
        } catch (Exception e) {
            document.close();
            throw new IOException("Erro ao gerar PDF: " + e.getMessage(), e);
        }
    }

    private void adicionarCabecalho(Document document, Curriculo curriculo, PdfFont fontBold, PdfFont fontNormal) {
        Paragraph nome = new Paragraph(curriculo.getNomeCompleto())
                .setFont(fontBold)
                .setFontSize(18)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(5);
        document.add(nome);

        StringBuilder contato = new StringBuilder();
        if (curriculo.getEmail() != null && !curriculo.getEmail().trim().isEmpty()) {
            contato.append(curriculo.getEmail());
        }
        if (curriculo.getTelefone() != null && !curriculo.getTelefone().trim().isEmpty()) {
            if (contato.length() > 0) contato.append(" | ");
            contato.append(curriculo.getTelefone());
        }
        if (curriculo.getLinkedin() != null && !curriculo.getLinkedin().trim().isEmpty()) {
            if (contato.length() > 0) contato.append(" | ");
            contato.append(curriculo.getLinkedin());
        }
        if (curriculo.getGithub() != null && !curriculo.getGithub().trim().isEmpty()) {
            if (contato.length() > 0) contato.append(" | ");
            contato.append(curriculo.getGithub());
        }

        Paragraph contatoParagraph = new Paragraph(contato.toString())
                .setFont(fontNormal)
                .setFontSize(10)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(10);
        document.add(contatoParagraph);

        if (curriculo.getObjetivo() != null && !curriculo.getObjetivo().trim().isEmpty()) {
            Paragraph objetivo = new Paragraph("OBJETIVO")
                    .setFont(fontBold)
                    .setFontSize(12)
                    .setMarginTop(10);
            document.add(objetivo);

            Paragraph objetivoTexto = new Paragraph(curriculo.getObjetivo())
                    .setFont(fontNormal)
                    .setFontSize(11)
                    .setMarginBottom(10);
            document.add(objetivoTexto);
        }
    }

    private void adicionarResumoProfissional(Document document, Curriculo curriculo, PdfFont fontBold, PdfFont fontNormal) {
        Paragraph titulo = new Paragraph("RESUMO PROFISSIONAL")
                .setFont(fontBold)
                .setFontSize(12)
                .setMarginTop(10);
        document.add(titulo);

        Paragraph resumo = new Paragraph(curriculo.getResumoProfissional())
                .setFont(fontNormal)
                .setFontSize(11)
                .setMarginBottom(10);
        document.add(resumo);
    }

    private void adicionarExperiencias(Document document, Curriculo curriculo, PdfFont fontBold, PdfFont fontNormal) {
        Paragraph titulo = new Paragraph("EXPERIÊNCIA PROFISSIONAL")
                .setFont(fontBold)
                .setFontSize(12)
                .setMarginTop(10);
        document.add(titulo);

        for (ExperienciaProfissional exp : curriculo.getExperiencias()) {
            Paragraph cargo = new Paragraph(exp.getCargo())
                    .setFont(fontBold)
                    .setFontSize(11)
                    .setMarginTop(8);
            document.add(cargo);

            StringBuilder periodo = new StringBuilder(exp.getEmpresa());
            if (exp.getCidade() != null && !exp.getCidade().trim().isEmpty()) {
                periodo.append(" - ").append(exp.getCidade());
            }
            periodo.append(" | ");
            
            if (exp.getDataInicio() != null) {
                periodo.append(DATE_FORMAT.format(exp.getDataInicio()));
            }
            periodo.append(" a ");
            
            if (exp.isAtual()) {
                periodo.append("Atual");
            } else if (exp.getDataFim() != null) {
                periodo.append(DATE_FORMAT.format(exp.getDataFim()));
            }

            Paragraph empresaPeriodo = new Paragraph(periodo.toString())
                    .setFont(fontNormal)
                    .setFontSize(10)
                    .setItalic();
            document.add(empresaPeriodo);

            if (exp.getDescricao() != null && !exp.getDescricao().trim().isEmpty()) {
                Paragraph descricao = new Paragraph(exp.getDescricao())
                        .setFont(fontNormal)
                        .setFontSize(10)
                        .setMarginBottom(5);
                document.add(descricao);
            }
        }
    }

    private void adicionarFormacoes(Document document, Curriculo curriculo, PdfFont fontBold, PdfFont fontNormal) {
        Paragraph titulo = new Paragraph("FORMAÇÃO ACADÊMICA")
                .setFont(fontBold)
                .setFontSize(12)
                .setMarginTop(10);
        document.add(titulo);

        for (Formacao form : curriculo.getFormacoes()) {
            Paragraph curso = new Paragraph(form.getCurso())
                    .setFont(fontBold)
                    .setFontSize(11)
                    .setMarginTop(8);
            document.add(curso);

            StringBuilder detalhes = new StringBuilder(form.getInstituicao());
            if (form.getNivel() != null && !form.getNivel().trim().isEmpty()) {
                detalhes.append(" - ").append(form.getNivel());
            }
            detalhes.append(" | ");
            
            if (form.getDataInicio() != null) {
                detalhes.append(DATE_FORMAT.format(form.getDataInicio()));
            }
            detalhes.append(" a ");
            
            if (form.isAtual()) {
                detalhes.append("Atual");
            } else if (form.getDataFim() != null) {
                detalhes.append(DATE_FORMAT.format(form.getDataFim()));
            }

            Paragraph instituicaoPeriodo = new Paragraph(detalhes.toString())
                    .setFont(fontNormal)
                    .setFontSize(10);
            document.add(instituicaoPeriodo);
        }
    }

    private void adicionarHabilidades(Document document, Curriculo curriculo, PdfFont fontBold, PdfFont fontNormal) {
        Paragraph titulo = new Paragraph("HABILIDADES")
                .setFont(fontBold)
                .setFontSize(12)
                .setMarginTop(10);
        document.add(titulo);

        List habilidadesList = new List()
                .setSymbolIndent(12)
                .setListSymbol("•");

        for (Habilidade habilidade : curriculo.getHabilidades()) {
            StringBuilder item = new StringBuilder(habilidade.getNome());
            if (habilidade.getNivel() != null && !habilidade.getNivel().trim().isEmpty()) {
                item.append(" (").append(habilidade.getNivel()).append(")");
            }
            if (habilidade.getCategoria() != null && !habilidade.getCategoria().trim().isEmpty()) {
                item.append(" - ").append(habilidade.getCategoria());
            }
            habilidadesList.add(new ListItem(item.toString()));
        }

        document.add(habilidadesList);
    }

    private void adicionarIdiomas(Document document, Curriculo curriculo, PdfFont fontBold, PdfFont fontNormal) {
        Paragraph titulo = new Paragraph("IDIOMAS")
                .setFont(fontBold)
                .setFontSize(12)
                .setMarginTop(10);
        document.add(titulo);

        List idiomasList = new List()
                .setSymbolIndent(12)
                .setListSymbol("•");

        for (Idioma idioma : curriculo.getIdiomas()) {
            StringBuilder item = new StringBuilder(idioma.getNome());
            if (idioma.getNivel() != null && !idioma.getNivel().trim().isEmpty()) {
                item.append(" - ").append(idioma.getNivel());
            }
            if (idioma.getCertificado() != null && !idioma.getCertificado().trim().isEmpty()) {
                item.append(" (").append(idioma.getCertificado()).append(")");
            }
            idiomasList.add(new ListItem(item.toString()));
        }

        document.add(idiomasList);
    }

    private void adicionarCertificacoes(Document document, Curriculo curriculo, PdfFont fontBold, PdfFont fontNormal) {
        Paragraph titulo = new Paragraph("CERTIFICAÇÕES")
                .setFont(fontBold)
                .setFontSize(12)
                .setMarginTop(10);
        document.add(titulo);

        List certificacoesList = new List()
                .setSymbolIndent(12)
                .setListSymbol("•");

        for (Certificacao cert : curriculo.getCertificacoes()) {
            StringBuilder item = new StringBuilder(cert.getNome());
            if (cert.getInstituicao() != null && !cert.getInstituicao().trim().isEmpty()) {
                item.append(" - ").append(cert.getInstituicao());
            }
            if (cert.getDataEmissao() != null) {
                item.append(" (").append(DATE_FORMAT.format(cert.getDataEmissao())).append(")");
            }
            certificacoesList.add(new ListItem(item.toString()));
        }

        document.add(certificacoesList);
    }
}
