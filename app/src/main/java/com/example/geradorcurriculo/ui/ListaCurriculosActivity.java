package com.example.geradorcurriculo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.geradorcurriculo.R;
import com.example.geradorcurriculo.model.Curriculo;
import com.example.geradorcurriculo.database.DatabaseHelper;
import com.example.geradorcurriculo.ui.adapter.CurriculoAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;

public class ListaCurriculosActivity extends AppCompatActivity {

    private ListView listViewCurriculos;
    private TextView textEmpty;
    private ProgressBar progressBar;
    private FloatingActionButton fabNovoCurriculo;
    
    private DatabaseHelper databaseHelper;
    private CurriculoAdapter adapter;
    private List<Curriculo> listaCurriculos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_curriculos);

        databaseHelper = new DatabaseHelper(this);
        
        initViews();
        setupToolbar();
        setupClickListeners();
        carregarCurriculos();
    }

    private void initViews() {
        listViewCurriculos = findViewById(R.id.list_view_curriculos);
        textEmpty = findViewById(R.id.text_empty);
        progressBar = findViewById(R.id.progress_bar);
        fabNovoCurriculo = findViewById(R.id.fab_novo_curriculo);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Meus Currículos");
        }
    }

    private void setupClickListeners() {
        fabNovoCurriculo.setOnClickListener(v -> {
            Intent intent = new Intent(ListaCurriculosActivity.this, CriarCurriculoActivity.class);
            startActivity(intent);
        });

        listViewCurriculos.setOnItemClickListener((parent, view, position, id) -> {
            Curriculo curriculo = listaCurriculos.get(position);
            Intent intent = new Intent(ListaCurriculosActivity.this, DetalheCurriculoActivity.class);
            intent.putExtra("curriculo_id", curriculo.getId());
            startActivity(intent);
        });

        listViewCurriculos.setOnItemLongClickListener((parent, view, position, id) -> {
            Curriculo curriculo = listaCurriculos.get(position);
            mostrarOpcoesCurriculo(curriculo);
            return true;
        });
    }

    private void carregarCurriculos() {
        progressBar.setVisibility(View.VISIBLE);
        
        new Thread(() -> {
            listaCurriculos = databaseHelper.listarTodosCurriculos();
            
            runOnUiThread(() -> {
                progressBar.setVisibility(View.GONE);
                
                if (listaCurriculos.isEmpty()) {
                    textEmpty.setVisibility(View.VISIBLE);
                    listViewCurriculos.setVisibility(View.GONE);
                } else {
                    textEmpty.setVisibility(View.GONE);
                    listViewCurriculos.setVisibility(View.VISIBLE);
                    adapter = new CurriculoAdapter(this, listaCurriculos);
                    listViewCurriculos.setAdapter(adapter);
                }
            });
        }).start();
    }

    private void mostrarOpcoesCurriculo(Curriculo curriculo) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Opções")
                .setItems(new CharSequence[]{"Editar", "Visualizar", "Exportar PDF", "Análise ATS", "Excluir"}, 
                    (dialog, which) -> {
                        switch (which) {
                            case 0:
                                Intent intentEdit = new Intent(this, EditarCurriculoActivity.class);
                                intentEdit.putExtra("curriculo_id", curriculo.getId());
                                startActivity(intentEdit);
                                break;
                            case 1:
                                Intent intentView = new Intent(this, DetalheCurriculoActivity.class);
                                intentView.putExtra("curriculo_id", curriculo.getId());
                                startActivity(intentView);
                                break;
                            case 2:
                                exportarPDF(curriculo);
                                break;
                            case 3:
                                Intent intentAts = new Intent(this, AnaliseAtsActivity.class);
                                intentAts.putExtra("curriculo_id", curriculo.getId());
                                startActivity(intentAts);
                                break;
                            case 4:
                                confirmarExclusao(curriculo);
                                break;
                        }
                    });
        builder.show();
    }

    private void exportarPDF(Curriculo curriculo) {
        
    }

    private void confirmarExclusao(Curriculo curriculo) {
        new android.app.AlertDialog.Builder(this)
                .setTitle("Confirmar Exclusão")
                .setMessage("Tem certeza que deseja excluir este currículo?")
                .setPositiveButton("Excluir", (dialog, which) -> excluirCurriculo(curriculo))
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void excluirCurriculo(Curriculo curriculo) {
        new Thread(() -> {
            int resultado = databaseHelper.excluirCurriculo(curriculo.getId());
            
            runOnUiThread(() -> {
                if (resultado > 0) {
                    carregarCurriculos();
                }
            });
        }).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarCurriculos();
    }
}
