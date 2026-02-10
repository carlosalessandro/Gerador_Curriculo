package com.example.geradorcurriculo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.geradorcurriculo.R;
import com.example.geradorcurriculo.model.Curriculo;
import com.example.geradorcurriculo.database.DatabaseHelper;
import com.example.geradorcurriculo.ui.adapter.CurriculoAdapter;
import com.example.geradorcurriculo.utils.PremiumManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;

public class ListaCurriculosActivity extends AppCompatActivity {

    private ListView listViewCurriculos;
    private TextView textEmpty;
    private ProgressBar progressBar;
    private FloatingActionButton fabNovoCurriculo;
    
    private DatabaseHelper databaseHelper;
    private PremiumManager premiumManager;
    private CurriculoAdapter adapter;
    private List<Curriculo> listaCurriculos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_curriculos);

        databaseHelper = new DatabaseHelper(this);
        premiumManager = new PremiumManager(this);
        
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
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setupClickListeners() {
        fabNovoCurriculo.setOnClickListener(v -> {
            if (!premiumManager.canCreateCurriculum()) {
                premiumManager.showPremiumDialog(this, "Criar Currículo");
                return;
            }
            Intent intent = new Intent(this, CriarCurriculoActivity.class);
            startActivity(intent);
        });

        listViewCurriculos.setOnItemClickListener((parent, view, position, id) -> {
            Curriculo curriculo = listaCurriculos.get(position);
            Intent intent = new Intent(this, DetalheCurriculoActivity.class);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Opções do Currículo")
                .setItems(new CharSequence[]{"Visualizar", "Editar", "Exportar PDF", "Análise ATS", "Duplicar", "Excluir"}, 
                    (dialog, which) -> {
                        switch (which) {
                            case 0: visualizarCurriculo(curriculo); break;
                            case 1: editarCurriculo(curriculo); break;
                            case 2: exportarPDF(curriculo); break;
                            case 3: analisarATS(curriculo); break;
                            case 4: duplicarCurriculo(curriculo); break;
                            case 5: confirmarExclusao(curriculo); break;
                        }
                    });
        builder.show();
    }
    
    private void visualizarCurriculo(Curriculo curriculo) {
        Intent intent = new Intent(this, DetalheCurriculoActivity.class);
        intent.putExtra("curriculo_id", curriculo.getId());
        startActivity(intent);
    }
    
    private void editarCurriculo(Curriculo curriculo) {
        Intent intent = new Intent(this, EditarCurriculoActivity.class);
        intent.putExtra("curriculo_id", curriculo.getId());
        startActivity(intent);
    }
    
    private void analisarATS(Curriculo curriculo) {
        if (!premiumManager.canUseAdvancedAnalysis()) {
            premiumManager.showPremiumDialog(this, "Análise ATS");
            return;
        }
        Intent intent = new Intent(this, AnaliseAtsActivity.class);
        intent.putExtra("curriculo_id", curriculo.getId());
        startActivity(intent);
    }
    
    private void duplicarCurriculo(Curriculo curriculo) {
        if (!premiumManager.canCreateCurriculum()) {
            premiumManager.showPremiumDialog(this, "Duplicar Currículo");
            return;
        }
        
        new AlertDialog.Builder(this)
                .setTitle("Duplicar Currículo")
                .setMessage("Deseja criar uma cópia deste currículo?")
                .setPositiveButton("Duplicar", (dialog, which) -> {
                    Toast.makeText(this, "Duplicação - Em desenvolvimento", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void exportarPDF(Curriculo curriculo) {
        Toast.makeText(this, "Exportação PDF - Em desenvolvimento", Toast.LENGTH_SHORT).show();
    }

    private void confirmarExclusao(Curriculo curriculo) {
        new AlertDialog.Builder(this)
                .setTitle("Confirmar Exclusão")
                .setMessage("Tem certeza que deseja excluir o currículo de " + curriculo.getNomeCompleto() + "?")
                .setPositiveButton("Excluir", (dialog, which) -> excluirCurriculo(curriculo))
                .setNegativeButton("Cancelar", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void excluirCurriculo(Curriculo curriculo) {
        progressBar.setVisibility(View.VISIBLE);
        
        new Thread(() -> {
            int resultado = databaseHelper.excluirCurriculo(curriculo.getId());
            
            runOnUiThread(() -> {
                progressBar.setVisibility(View.GONE);
                
                if (resultado > 0) {
                    Toast.makeText(this, "Currículo excluído com sucesso", Toast.LENGTH_SHORT).show();
                    carregarCurriculos();
                } else {
                    Toast.makeText(this, "Erro ao excluir currículo", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_lista_curriculos, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        
        if (id == R.id.action_importar) {
            Intent intent = new Intent(this, ImportarCurriculoActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_premium) {
            Intent intent = new Intent(this, PremiumActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_configuracoes) {
            Intent intent = new Intent(this, ConfiguracoesActivity.class);
            startActivity(intent);
            return true;
        } else if (id == android.R.id.home) {
            finish();
            return true;
        }
        
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarCurriculos();
    }
}
