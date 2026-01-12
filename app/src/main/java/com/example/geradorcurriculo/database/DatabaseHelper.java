package com.example.geradorcurriculo.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.geradorcurriculo.model.Curriculo;
import com.example.geradorcurriculo.model.ExperienciaProfissional;
import com.example.geradorcurriculo.model.Formacao;
import com.example.geradorcurriculo.model.Habilidade;
import com.example.geradorcurriculo.model.Idioma;
import com.example.geradorcurriculo.model.Certificacao;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "curriculo_db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_CURRICULOS = "curriculos";
    private static final String TABLE_EXPERIENCIAS = "experiencias";
    private static final String TABLE_FORMACOES = "formacoes";
    private static final String TABLE_HABILIDADES = "habilidades";
    private static final String TABLE_IDIOMAS = "idiomas";
    private static final String TABLE_CERTIFICACOES = "certificacoes";

    private Gson gson = new Gson();

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTables(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        dropTables(db);
        onCreate(db);
    }

    private void createTables(SQLiteDatabase db) {
        String CREATE_CURRICULOS_TABLE = "CREATE TABLE " + TABLE_CURRICULOS + "("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "nome_completo TEXT,"
                + "email TEXT,"
                + "telefone TEXT,"
                + "endereco TEXT,"
                + "linkedin TEXT,"
                + "github TEXT,"
                + "objetivo TEXT,"
                + "resumo_profissional TEXT,"
                + "data_criacao INTEGER,"
                + "data_atualizacao INTEGER,"
                + "ats_score INTEGER DEFAULT 0,"
                + "ats_suggestions TEXT"
                + ")";

        String CREATE_EXPERIENCIAS_TABLE = "CREATE TABLE " + TABLE_EXPERIENCIAS + "("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "curriculo_id INTEGER,"
                + "empresa TEXT,"
                + "cargo TEXT,"
                + "descricao TEXT,"
                + "data_inicio INTEGER,"
                + "data_fim INTEGER,"
                + "atual INTEGER DEFAULT 0,"
                + "cidade TEXT,"
                + "pais TEXT,"
                + "FOREIGN KEY(curriculo_id) REFERENCES " + TABLE_CURRICULOS + "(id)"
                + ")";

        String CREATE_FORMACOES_TABLE = "CREATE TABLE " + TABLE_FORMACOES + "("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "curriculo_id INTEGER,"
                + "instituicao TEXT,"
                + "curso TEXT,"
                + "nivel TEXT,"
                + "data_inicio INTEGER,"
                + "data_fim INTEGER,"
                + "atual INTEGER DEFAULT 0,"
                + "descricao TEXT,"
                + "cidade TEXT,"
                + "pais TEXT,"
                + "FOREIGN KEY(curriculo_id) REFERENCES " + TABLE_CURRICULOS + "(id)"
                + ")";

        String CREATE_HABILIDADES_TABLE = "CREATE TABLE " + TABLE_HABILIDADES + "("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "curriculo_id INTEGER,"
                + "nome TEXT,"
                + "categoria TEXT,"
                + "nivel TEXT,"
                + "FOREIGN KEY(curriculo_id) REFERENCES " + TABLE_CURRICULOS + "(id)"
                + ")";

        String CREATE_IDIOMAS_TABLE = "CREATE TABLE " + TABLE_IDIOMAS + "("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "curriculo_id INTEGER,"
                + "nome TEXT,"
                + "nivel TEXT,"
                + "certificado TEXT,"
                + "FOREIGN KEY(curriculo_id) REFERENCES " + TABLE_CURRICULOS + "(id)"
                + ")";

        String CREATE_CERTIFICACOES_TABLE = "CREATE TABLE " + TABLE_CERTIFICACOES + "("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "curriculo_id INTEGER,"
                + "nome TEXT,"
                + "instituicao TEXT,"
                + "data_emissao INTEGER,"
                + "data_validade INTEGER,"
                + "credencial TEXT,"
                + "url TEXT,"
                + "FOREIGN KEY(curriculo_id) REFERENCES " + TABLE_CURRICULOS + "(id)"
                + ")";

        db.execSQL(CREATE_CURRICULOS_TABLE);
        db.execSQL(CREATE_EXPERIENCIAS_TABLE);
        db.execSQL(CREATE_FORMACOES_TABLE);
        db.execSQL(CREATE_HABILIDADES_TABLE);
        db.execSQL(CREATE_IDIOMAS_TABLE);
        db.execSQL(CREATE_CERTIFICACOES_TABLE);
    }

    private void dropTables(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CERTIFICACOES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_IDIOMAS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HABILIDADES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FORMACOES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPERIENCIAS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CURRICULOS);
    }

    public long inserirCurriculo(Curriculo curriculo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("nome_completo", curriculo.getNomeCompleto());
        values.put("email", curriculo.getEmail());
        values.put("telefone", curriculo.getTelefone());
        values.put("endereco", curriculo.getEndereco());
        values.put("linkedin", curriculo.getLinkedin());
        values.put("github", curriculo.getGithub());
        values.put("objetivo", curriculo.getObjetivo());
        values.put("resumo_profissional", curriculo.getResumoProfissional());
        values.put("data_criacao", curriculo.getDataCriacao().getTime());
        values.put("data_atualizacao", curriculo.getDataAtualizacao().getTime());
        values.put("ats_score", curriculo.getAtsScore());
        
        if (curriculo.getAtsSuggestions() != null) {
            values.put("ats_suggestions", gson.toJson(curriculo.getAtsSuggestions()));
        }

        long id = db.insert(TABLE_CURRICULOS, null, db);
        curriculo.setId(id);

        if (curriculo.getExperiencias() != null) {
            for (ExperienciaProfissional exp : curriculo.getExperiencias()) {
                inserirExperiencia(id, exp);
            }
        }

        if (curriculo.getFormacoes() != null) {
            for (Formacao form : curriculo.getFormacoes()) {
                inserirFormacao(id, form);
            }
        }

        if (curriculo.getHabilidades() != null) {
            for (Habilidade hab : curriculo.getHabilidades()) {
                inserirHabilidade(id, hab);
            }
        }

        if (curriculo.getIdiomas() != null) {
            for (Idioma idioma : curriculo.getIdiomas()) {
                inserirIdioma(id, idioma);
            }
        }

        if (curriculo.getCertificacoes() != null) {
            for (Certificacao cert : curriculo.getCertificacoes()) {
                inserirCertificacao(id, cert);
            }
        }

        db.close();
        return id;
    }

    private void inserirExperiencia(long curriculoId, ExperienciaProfissional experiencia) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("curriculo_id", curriculoId);
        values.put("empresa", experiencia.getEmpresa());
        values.put("cargo", experiencia.getCargo());
        values.put("descricao", experiencia.getDescricao());
        values.put("data_inicio", experiencia.getDataInicio() != null ? experiencia.getDataInicio().getTime() : null);
        values.put("data_fim", experiencia.getDataFim() != null ? experiencia.getDataFim().getTime() : null);
        values.put("atual", experiencia.isAtual() ? 1 : 0);
        values.put("cidade", experiencia.getCidade());
        values.put("pais", experiencia.getPais());

        long id = db.insert(TABLE_EXPERIENCIAS, null, values);
        experiencia.setId(id);
        db.close();
    }

    private void inserirFormacao(long curriculoId, Formacao formacao) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("curriculo_id", curriculoId);
        values.put("instituicao", formacao.getInstituicao());
        values.put("curso", formacao.getCurso());
        values.put("nivel", formacao.getNivel());
        values.put("data_inicio", formacao.getDataInicio() != null ? formacao.getDataInicio().getTime() : null);
        values.put("data_fim", formacao.getDataFim() != null ? formacao.getDataFim().getTime() : null);
        values.put("atual", formacao.isAtual() ? 1 : 0);
        values.put("descricao", formacao.getDescricao());
        values.put("cidade", formacao.getCidade());
        values.put("pais", formacao.getPais());

        long id = db.insert(TABLE_FORMACOES, null, values);
        formacao.setId(id);
        db.close();
    }

    private void inserirHabilidade(long curriculoId, Habilidade habilidade) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("curriculo_id", curriculoId);
        values.put("nome", habilidade.getNome());
        values.put("categoria", habilidade.getCategoria());
        values.put("nivel", habilidade.getNivel());

        long id = db.insert(TABLE_HABILIDADES, null, values);
        habilidade.setId(id);
        db.close();
    }

    private void inserirIdioma(long curriculoId, Idioma idioma) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("curriculo_id", curriculoId);
        values.put("nome", idioma.getNome());
        values.put("nivel", idioma.getNivel());
        values.put("certificado", idioma.getCertificado());

        long id = db.insert(TABLE_IDIOMAS, null, values);
        idioma.setId(id);
        db.close();
    }

    private void inserirCertificacao(long curriculoId, Certificacao certificacao) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("curriculo_id", curriculoId);
        values.put("nome", certificacao.getNome());
        values.put("instituicao", certificacao.getInstituicao());
        values.put("data_emissao", certificacao.getDataEmissao() != null ? certificacao.getDataEmissao().getTime() : null);
        values.put("data_validade", certificacao.getDataValidade() != null ? certificacao.getDataValidade().getTime() : null);
        values.put("credencial", certificacao.getCredencial());
        values.put("url", certificacao.getUrl());

        long id = db.insert(TABLE_CERTIFICACOES, null, values);
        certificacao.setId(id);
        db.close();
    }

    public List<Curriculo> listarTodosCurriculos() {
        List<Curriculo> curriculos = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_CURRICULOS + " ORDER BY data_atualizacao DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Curriculo curriculo = new Curriculo();
                curriculo.setId(cursor.getLong(0));
                curriculo.setNomeCompleto(cursor.getString(1));
                curriculo.setEmail(cursor.getString(2));
                curriculo.setTelefone(cursor.getString(3));
                curriculo.setEndereco(cursor.getString(4));
                curriculo.setLinkedin(cursor.getString(5));
                curriculo.setGithub(cursor.getString(6));
                curriculo.setObjetivo(cursor.getString(7));
                curriculo.setResumoProfissional(cursor.getString(8));
                curriculo.setDataCriacao(new Date(cursor.getLong(9)));
                curriculo.setDataAtualizacao(new Date(cursor.getLong(10)));
                curriculo.setAtsScore(cursor.getInt(11));
                
                String suggestionsJson = cursor.getString(12);
                if (suggestionsJson != null) {
                    Type listType = new TypeToken<List<String>>(){}.getType();
                    curriculo.setAtsSuggestions(gson.fromJson(suggestionsJson, listType));
                }

                carregarDadosRelacionados(curriculo);
                curriculos.add(curriculo);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return curriculos;
    }

    public Curriculo buscarCurriculoPorId(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CURRICULOS, null, "id = ?", 
                new String[]{String.valueOf(id)}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            Curriculo curriculo = new Curriculo();
            curriculo.setId(cursor.getLong(0));
            curriculo.setNomeCompleto(cursor.getString(1));
            curriculo.setEmail(cursor.getString(2));
            curriculo.setTelefone(cursor.getString(3));
            curriculo.setEndereco(cursor.getString(4));
            curriculo.setLinkedin(cursor.getString(5));
            curriculo.setGithub(cursor.getString(6));
            curriculo.setObjetivo(cursor.getString(7));
            curriculo.setResumoProfissional(cursor.getString(8));
            curriculo.setDataCriacao(new Date(cursor.getLong(9)));
            curriculo.setDataAtualizacao(new Date(cursor.getLong(10)));
            curriculo.setAtsScore(cursor.getInt(11));
            
            String suggestionsJson = cursor.getString(12);
            if (suggestionsJson != null) {
                Type listType = new TypeToken<List<String>>(){}.getType();
                curriculo.setAtsSuggestions(gson.fromJson(suggestionsJson, listType));
            }

            carregarDadosRelacionados(curriculo);
            
            cursor.close();
            db.close();
            return curriculo;
        }

        if (cursor != null) cursor.close();
        db.close();
        return null;
    }

    private void carregarDadosRelacionados(Curriculo curriculo) {
        curriculo.setExperiencias(buscarExperienciasPorCurriculoId(curriculo.getId()));
        curriculo.setFormacoes(buscarFormacoesPorCurriculoId(curriculo.getId()));
        curriculo.setHabilidades(buscarHabilidadesPorCurriculoId(curriculo.getId()));
        curriculo.setIdiomas(buscarIdiomasPorCurriculoId(curriculo.getId()));
        curriculo.setCertificacoes(buscarCertificacoesPorCurriculoId(curriculo.getId()));
    }

    private List<ExperienciaProfissional> buscarExperienciasPorCurriculoId(long curriculoId) {
        List<ExperienciaProfissional> experiencias = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_EXPERIENCIAS, null, "curriculo_id = ?", 
                new String[]{String.valueOf(curriculoId)}, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                ExperienciaProfissional exp = new ExperienciaProfissional();
                exp.setId(cursor.getLong(0));
                exp.setEmpresa(cursor.getString(2));
                exp.setCargo(cursor.getString(3));
                exp.setDescricao(cursor.getString(4));
                
                long dataInicio = cursor.getLong(5);
                if (dataInicio > 0) exp.setDataInicio(new Date(dataInicio));
                
                long dataFim = cursor.getLong(6);
                if (dataFim > 0) exp.setDataFim(new Date(dataFim));
                
                exp.setAtual(cursor.getInt(7) == 1);
                exp.setCidade(cursor.getString(8));
                exp.setPais(cursor.getString(9));
                
                experiencias.add(exp);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return experiencias;
    }

    private List<Formacao> buscarFormacoesPorCurriculoId(long curriculoId) {
        List<Formacao> formacoes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_FORMACOES, null, "curriculo_id = ?", 
                new String[]{String.valueOf(curriculoId)}, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Formacao form = new Formacao();
                form.setId(cursor.getLong(0));
                form.setInstituicao(cursor.getString(2));
                form.setCurso(cursor.getString(3));
                form.setNivel(cursor.getString(4));
                
                long dataInicio = cursor.getLong(5);
                if (dataInicio > 0) form.setDataInicio(new Date(dataInicio));
                
                long dataFim = cursor.getLong(6);
                if (dataFim > 0) form.setDataFim(new Date(dataFim));
                
                form.setAtual(cursor.getInt(7) == 1);
                form.setDescricao(cursor.getString(8));
                form.setCidade(cursor.getString(9));
                form.setPais(cursor.getString(10));
                
                formacoes.add(form);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return formacoes;
    }

    private List<Habilidade> buscarHabilidadesPorCurriculoId(long curriculoId) {
        List<Habilidade> habilidades = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_HABILIDADES, null, "curriculo_id = ?", 
                new String[]{String.valueOf(curriculoId)}, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Habilidade hab = new Habilidade();
                hab.setId(cursor.getLong(0));
                hab.setNome(cursor.getString(2));
                hab.setCategoria(cursor.getString(3));
                hab.setNivel(cursor.getString(4));
                
                habilidades.add(hab);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return habilidades;
    }

    private List<Idioma> buscarIdiomasPorCurriculoId(long curriculoId) {
        List<Idioma> idiomas = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_IDIOMAS, null, "curriculo_id = ?", 
                new String[]{String.valueOf(curriculoId)}, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Idioma idioma = new Idioma();
                idioma.setId(cursor.getLong(0));
                idioma.setNome(cursor.getString(2));
                idioma.setNivel(cursor.getString(3));
                idioma.setCertificado(cursor.getString(4));
                
                idiomas.add(idioma);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return idiomas;
    }

    private List<Certificacao> buscarCertificacoesPorCurriculoId(long curriculoId) {
        List<Certificacao> certificacoes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CERTIFICACOES, null, "curriculo_id = ?", 
                new String[]{String.valueOf(curriculoId)}, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Certificacao cert = new Certificacao();
                cert.setId(cursor.getLong(0));
                cert.setNome(cursor.getString(2));
                cert.setInstituicao(cursor.getString(3));
                
                long dataEmissao = cursor.getLong(4);
                if (dataEmissao > 0) cert.setDataEmissao(new Date(dataEmissao));
                
                long dataValidade = cursor.getLong(5);
                if (dataValidade > 0) cert.setDataValidade(new Date(dataValidade));
                
                cert.setCredencial(cursor.getString(6));
                cert.setUrl(cursor.getString(7));
                
                certificacoes.add(cert);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return certificacoes;
    }

    public int atualizarCurriculo(Curriculo curriculo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("nome_completo", curriculo.getNomeCompleto());
        values.put("email", curriculo.getEmail());
        values.put("telefone", curriculo.getTelefone());
        values.put("endereco", curriculo.getEndereco());
        values.put("linkedin", curriculo.getLinkedin());
        values.put("github", curriculo.getGithub());
        values.put("objetivo", curriculo.getObjetivo());
        values.put("resumo_profissional", curriculo.getResumoProfissional());
        values.put("data_atualizacao", new Date().getTime());
        values.put("ats_score", curriculo.getAtsScore());
        
        if (curriculo.getAtsSuggestions() != null) {
            values.put("ats_suggestions", gson.toJson(curriculo.getAtsSuggestions()));
        }

        int resultado = db.update(TABLE_CURRICULOS, values, "id = ?", 
                new String[]{String.valueOf(curriculo.getId())});
        
        db.close();
        return resultado;
    }

    public int excluirCurriculo(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        
        db.delete(TABLE_EXPERIENCIAS, "curriculo_id = ?", new String[]{String.valueOf(id)});
        db.delete(TABLE_FORMACOES, "curriculo_id = ?", new String[]{String.valueOf(id)});
        db.delete(TABLE_HABILIDADES, "curriculo_id = ?", new String[]{String.valueOf(id)});
        db.delete(TABLE_IDIOMAS, "curriculo_id = ?", new String[]{String.valueOf(id)});
        db.delete(TABLE_CERTIFICACOES, "curriculo_id = ?", new String[]{String.valueOf(id)});
        
        int resultado = db.delete(TABLE_CURRICULOS, "id = ?", new String[]{String.valueOf(id)});
        db.close();
        return resultado;
    }
}
