package com.example.geradorcurriculo.ui;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import com.example.geradorcurriculo.R;
import com.example.geradorcurriculo.model.Curriculo;
import com.example.geradorcurriculo.database.DatabaseHelper;
import com.example.geradorcurriculo.utils.PremiumManager;
import com.google.android.material.snackbar.Snackbar;

public class ImportarCurriculoActivity extends AppCompatActivity {

    private CardView cardImportarPDF;
    private CardView cardImportarDOCX;
    private CardView cardFileInfo;
    private TextView textFileName;
    private ProgressBar progressBar;
    private Button buttonProcessar;
    
    private Uri selectedFileUri;
    private String selectedFileName;
    private DatabaseHelper databaseHelper;
    private PremiumManager premiumManager;
    
    private ActivityResultLauncher<Intent> pdfPickerLauncher;
    private ActivityResultLauncher<Intent> docxPickerLauncher;
    private ActivityResultLauncher<String> requestPermissionLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_importar_curriculo);

        databaseHelper = new DatabaseHelper(this);
        premiumManager = new PremiumManager(this);
        
        setupActivityResultLaunchers();
        initViews();
        setupToolbar();
        setupClickListeners();
        checkPermissions();
    }
    
    private void setupActivityResultLaunchers() {
        // Launcher para seleção de PDF
        pdfPickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    handleFileSelection(result.getData().getData());
                }
            }
        );
        
        // Launcher para seleção de DOCX
        docxPickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    handleFileSelection(result.getData().getData());
                }
            }
        );
        
        // Launcher para permissões
        requestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            isGranted -> {
                if (isGranted) {
                    Toast.makeText(this, "Permissão concedida", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permissão negada. Você pode conceder nas configurações.", Toast.LENGTH_LONG).show();
                }
            }
        );
    }
    
    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android 13+ não precisa de permissão para ACTION_GET_CONTENT
            return;
        }
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String permission = "android.permission.READ_EXTERNAL_STORAGE";
            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(permission);
            }
        }
    }
    
    private void handleFileSelection(Uri uri) {
        if (uri != null) {
            selectedFileUri = uri;
            selectedFileName = getFileName(uri);
            
            if (selectedFileName != null) {
                textFileName.setText("Arquivo selecionado: " + selectedFileName);
                cardFileInfo.setVisibility(View.VISIBLE);
                buttonProcessar.setVisibility(View.VISIBLE);
                
                Toast.makeText(this, "Arquivo pronto para processar", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Erro ao obter nome do arquivo", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Nenhum arquivo selecionado", Toast.LENGTH_SHORT).show();
        }
    }

    private void initViews() {
        cardImportarPDF = findViewById(R.id.card_importar_pdf);
        cardImportarDOCX = findViewById(R.id.card_importar_docx);
        cardFileInfo = findViewById(R.id.card_file_info);
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
        toolbar.setNavigationOnClickListener(v -> finish());
    }
    
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
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
        try {
            pdfPickerLauncher.launch(Intent.createChooser(intent, "Selecione um arquivo PDF"));
        } catch (Exception e) {
            Toast.makeText(this, "Erro ao abrir seletor de arquivos: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void selecionarArquivoDOCX() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            docxPickerLauncher.launch(Intent.createChooser(intent, "Selecione um arquivo DOCX"));
        } catch (Exception e) {
            // Se não conseguir abrir com tipo específico, tenta com todos os arquivos
            intent.setType("*/*");
            try {
                docxPickerLauncher.launch(Intent.createChooser(intent, "Selecione um arquivo"));
            } catch (Exception ex) {
                Toast.makeText(this, "Erro ao abrir seletor de arquivos: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
    
    @Deprecated
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (resultCode == RESULT_OK && data != null && data.getData() != null) {
            handleFileSelection(data.getData());
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
        
        Toast.makeText(this, "Processando arquivo...", Toast.LENGTH_SHORT).show();

        new Thread(() -> {
            try {
                Curriculo curriculo = null;
                
                // Usa reflexão para evitar erro de compilação durante cache do Gradle
                try {
                    if (selectedFileName != null && selectedFileName.toLowerCase().endsWith(".pdf")) {
                        Class<?> pdfParserClass = Class.forName("com.example.geradorcurriculo.services.PDFParserService");
                        Object pdfParser = pdfParserClass.newInstance();
                        java.lang.reflect.Method parseMethod = pdfParserClass.getMethod("parsePDF", android.content.Context.class, Uri.class);
                        curriculo = (Curriculo) parseMethod.invoke(pdfParser, this, selectedFileUri);
                    } else if (selectedFileName != null && selectedFileName.toLowerCase().endsWith(".docx")) {
                        Class<?> docxParserClass = Class.forName("com.example.geradorcurriculo.services.DocxParserService");
                        Object docxParser = docxParserClass.newInstance();
                        java.lang.reflect.Method parseMethod = docxParserClass.getMethod("parseDOCX", android.content.Context.class, Uri.class);
                        curriculo = (Curriculo) parseMethod.invoke(docxParser, this, selectedFileUri);
                    } else {
                        curriculo = new Curriculo();
                        curriculo.setNomeCompleto("Currículo Importado");
                        curriculo.setResumoProfissional("Arquivo importado. Por favor, preencha os dados manualmente.");
                    }
                } catch (ClassNotFoundException e) {
                    // Classes ainda não compiladas, cria currículo vazio
                    curriculo = new Curriculo();
                    curriculo.setNomeCompleto("Currículo Importado");
                    curriculo.setResumoProfissional("Recurso de importação em desenvolvimento. Preencha os dados manualmente.");
                }

                final Curriculo finalCurriculo = curriculo;
                
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    buttonProcessar.setEnabled(true);

                    if (finalCurriculo != null) {
                        // Redirecionar para tela de revisão ao invés de salvar diretamente
                        Intent intent = new Intent(ImportarCurriculoActivity.this, RevisarCurriculoActivity.class);
                        intent.putExtra("curriculo", finalCurriculo);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(this, "Não foi possível processar o arquivo", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                final String errorMsg = e.getMessage();
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    buttonProcessar.setEnabled(true);
                    Toast.makeText(this, "Erro ao processar arquivo: " + errorMsg, Toast.LENGTH_LONG).show();
                });
            }
        }).start();
    }
}
