package com.example.geradorcurriculo.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import com.example.geradorcurriculo.R;
import com.example.geradorcurriculo.model.Curriculo;
import com.example.geradorcurriculo.database.DatabaseHelper;
import com.example.geradorcurriculo.services.PDFParserService;
import com.example.geradorcurriculo.services.DocxParserService;
import com.example.geradorcurriculo.utils.PremiumManager;
import com.google.android.material.snackbar.Snackbar;

public class ImportarCurriculoActivity extends AppCompatActivity {

    private static final int PICK_PDF_REQUEST = 1;
    private static final int PICK_DOCX_REQUEST = 2;

    private CardView cardImportarPDF;
    private CardView cardImportarDOCX;
    private TextView textFileName;
    private ProgressBar progressBar;
    private Button buttonProcessar;
    
    private Uri selectedFileUri;
    private String selectedFileName;
    private DatabaseHelper databaseHelper;
    private PDFParserService pdfParserService;
    private DocxParserService docxParserService;
    private PremiumManager premiumManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_importar_curriculo);

        databaseHelper = new DatabaseHelper(this);
        pdfParserService = new PDFParserService();
        docxParserService = new DocxParserService();
        premiumManager = new PremiumManager(this);
        
        initViews();
        setupToolbar();
        setupClickListeners();
    }

    private void initViews() {
        cardImportarPDF = findViewById(R.id.card_importar_pdf);
        cardImportarDOCX = findViewById(R.id.card_importar_docx);
        textFileName = findViewById(R.id.text_file_name);
        progressBar = findViewById(R.id.progress_bar);
        buttonProcessar = findViewById(R.id.button_processar);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Importar Currículo");
        }
    }

    private void setupClickListeners() {
        cardImportarPDF.setOnClickListener(v -> selecionarArquivoPDF());
        cardImportarDOCX.setOnClickListener(v -> selecionarArquivoDOCX());
        buttonProcessar.setOnClickListener(v -> processarArquivo());
    }

    private void selecionarArquivoPDF() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, "Selecione um arquivo PDF"), PICK_PDF_REQUEST);
    }

    private void selecionarArquivoDOCX() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, "Selecione um arquivo DOCX"), PICK_DOCX_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedFileUri = data.getData();
            selectedFileName = getFileName(selectedFileUri);
            textFileName.setText("Arquivo selecionado: " + selectedFileName);
            buttonProcessar.setVisibility(View.VISIBLE);
        }
    }

    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            try (android.database.Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (index >= 0) {
                        result = cursor.getString(index);
                    }
                }
            }
        }
        if (result == null) {
            result = uri.getLastPathSegment();
        }
        return result;
    }

    private void processarArquivo() {
        if (selectedFileUri == null) {
            Toast.makeText(this, "Selecione um arquivo primeiro", Toast.LENGTH_SHORT).show();
            return;
        }

        // Verificar se usuário pode criar mais currículos
        if (!premiumManager.canCreateCurriculum()) {
            premiumManager.showPremiumDialog(this, "Importar Currículo");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        buttonProcessar.setEnabled(false);

        new Thread(() -> {
            try {
                Curriculo curriculo = null;
                
                if (selectedFileName.toLowerCase().endsWith(".pdf")) {
                    curriculo = pdfParserService.parsePDF(this, selectedFileUri);
                } else if (selectedFileName.toLowerCase().endsWith(".docx")) {
                    curriculo = docxParserService.parseDOCX(this, selectedFileUri);
                }

                final Curriculo finalCurriculo = curriculo;
                
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    buttonProcessar.setEnabled(true);

                    if (finalCurriculo != null) {
                        long resultado = databaseHelper.inserirCurriculo(finalCurriculo);
                        if (resultado != -1) {
                            // Incrementar contador de currículos criados
                            premiumManager.incrementCurriculosCreated();
                            
                            Snackbar.make(findViewById(android.R.id.content), 
                                    "Currículo importado com sucesso!", 
                                    Snackbar.LENGTH_LONG)
                                    .setAction("Visualizar", v -> {
                                        Intent intent = new Intent(this, DetalheCurriculoActivity.class);
                                        intent.putExtra("curriculo_id", resultado);
                                        startActivity(intent);
                                    })
                                    .show();
                        } else {
                            Toast.makeText(this, "Erro ao salvar currículo importado", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Não foi possível processar o arquivo", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    buttonProcessar.setEnabled(true);
                    Toast.makeText(this, "Erro ao processar arquivo: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
            }
        }).start();
    }
}
