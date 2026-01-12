package com.example.geradorcurriculo.model;

import java.util.List;
import java.util.Date;

public class Curriculo {
    private Long id;
    private String nomeCompleto;
    private String email;
    private String telefone;
    private String endereco;
    private String linkedin;
    private String github;
    private String objetivo;
    private String resumoProfissional;
    private List<ExperienciaProfissional> experiencias;
    private List<Formacao> formacoes;
    private List<Habilidade> habilidades;
    private List<Idioma> idiomas;
    private List<Certificacao> certificacoes;
    private Date dataCriacao;
    private Date dataAtualizacao;
    private int atsScore;
    private List<String> atsSuggestions;

    public Curriculo() {
        this.dataCriacao = new Date();
        this.dataAtualizacao = new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getLinkedin() {
        return linkedin;
    }

    public void setLinkedin(String linkedin) {
        this.linkedin = linkedin;
    }

    public String getGithub() {
        return github;
    }

    public void setGithub(String github) {
        this.github = github;
    }

    public String getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(String objetivo) {
        this.objetivo = objetivo;
    }

    public String getResumoProfissional() {
        return resumoProfissional;
    }

    public void setResumoProfissional(String resumoProfissional) {
        this.resumoProfissional = resumoProfissional;
    }

    public List<ExperienciaProfissional> getExperiencias() {
        return experiencias;
    }

    public void setExperiencias(List<ExperienciaProfissional> experiencias) {
        this.experiencias = experiencias;
    }

    public List<Formacao> getFormacoes() {
        return formacoes;
    }

    public void setFormacoes(List<Formacao> formacoes) {
        this.formacoes = formacoes;
    }

    public List<Habilidade> getHabilidades() {
        return habilidades;
    }

    public void setHabilidades(List<Habilidade> habilidades) {
        this.habilidades = habilidades;
    }

    public List<Idioma> getIdiomas() {
        return idiomas;
    }

    public void setIdiomas(List<Idioma> idiomas) {
        this.idiomas = idiomas;
    }

    public List<Certificacao> getCertificacoes() {
        return certificacoes;
    }

    public void setCertificacoes(List<Certificacao> certificacoes) {
        this.certificacoes = certificacoes;
    }

    public Date getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public Date getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(Date dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    public int getAtsScore() {
        return atsScore;
    }

    public void setAtsScore(int atsScore) {
        this.atsScore = atsScore;
    }

    public List<String> getAtsSuggestions() {
        return atsSuggestions;
    }

    public void setAtsSuggestions(List<String> atsSuggestions) {
        this.atsSuggestions = atsSuggestions;
    }
    
    // Métodos auxiliares para importação de texto
    private String experienciaProfissionalTexto;
    private String formacaoAcademicaTexto;
    private String habilidadesTexto;
    
    public String getExperienciaProfissionalTexto() {
        return experienciaProfissionalTexto;
    }
    
    public void setExperienciaProfissional(String texto) {
        this.experienciaProfissionalTexto = texto;
    }
    
    public String getFormacaoAcademicaTexto() {
        return formacaoAcademicaTexto;
    }
    
    public void setFormacaoAcademica(String texto) {
        this.formacaoAcademicaTexto = texto;
    }
    
    public String getHabilidadesTexto() {
        return habilidadesTexto;
    }
    
    public void setHabilidades(String texto) {
        this.habilidadesTexto = texto;
    }
}
