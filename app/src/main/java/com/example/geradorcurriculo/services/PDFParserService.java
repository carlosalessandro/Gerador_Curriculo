package com.example.geradorcurriculo.services;

import android.content.Context;
import android.net.Uri;
import com.example.geradorcurriculo.model.Curriculo;
// import org.apache.pdfbox.pdmodel.PDDocument;
// import org.apache.pdfbox.text.PDFTextStripper;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PDFParserService {

    public Curriculo parsePDF(Context context, Uri uri) throws IOException {
        // TODO: Implementar quando PDFBox estiver disponível
        throw new UnsupportedOperationException("Importação de PDF não implementada ainda");
    }
}
