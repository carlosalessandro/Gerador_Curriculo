# Funcionalidade de Revisão de Currículo Importado

## 📋 Descrição

Nova funcionalidade que permite ao usuário revisar e editar os dados extraídos automaticamente de arquivos PDF e DOCX antes de salvar o currículo no banco de dados.

## ✨ Características

### 1. Extração Automática
- **PDF**: Utiliza iText7 para extrair texto de arquivos PDF
- **DOCX**: Utiliza parsing de ZIP para extrair conteúdo de arquivos Word
- **Regex Inteligente**: Padrões de expressão regular para identificar:
  - Nome completo
  - E-mail
  - Telefone (formato brasileiro)
  - Endereço
  - LinkedIn e GitHub
  - Resumo profissional
  - Experiência profissional
  - Formação acadêmica
  - Habilidades

### 2. Tela de Revisão
Após a importação, o usuário é direcionado para uma tela onde pode:
- ✅ Visualizar todos os campos preenchidos automaticamente
- ✏️ Editar qualquer informação antes de salvar
- ❌ Cancelar a importação se necessário
- 💾 Salvar o currículo revisado

### 3. Validação de Dados
- Campos obrigatórios: Nome, E-mail e Telefone
- Validação de formato de e-mail
- Mensagens de erro claras

## 🎯 Fluxo de Uso

1. **Importar Arquivo**
   - Usuário seleciona PDF ou DOCX na tela de importação
   - Sistema processa o arquivo em background

2. **Revisão Automática**
   - Sistema extrai dados automaticamente
   - Redireciona para tela de revisão com campos preenchidos

3. **Edição e Correção**
   - Usuário revisa informações extraídas
   - Corrige ou complementa dados conforme necessário
   - Campos vazios ou incorretos podem ser editados

4. **Salvamento**
   - Validação de campos obrigatórios
   - Verificação de limite premium (se aplicável)
   - Salva no banco de dados
   - Redireciona para visualização do currículo

## 📱 Telas Envolvidas

### ImportarCurriculoActivity
- Seleção de arquivo (PDF/DOCX)
- Processamento em background
- Redirecionamento para revisão

### RevisarCurriculoActivity (NOVA)
- Exibição de todos os campos editáveis
- Validação em tempo real
- Botões de ação: Cancelar e Salvar
- Confirmação de cancelamento

## 🔧 Componentes Técnicos

### Arquivos Criados/Modificados

1. **RevisarCurriculoActivity.java** (NOVO)
   - Activity principal de revisão
   - Gerenciamento de formulário
   - Validação e salvamento

2. **activity_revisar_curriculo.xml** (NOVO)
   - Layout com ScrollView
   - TextInputLayouts para todos os campos
   - Botões de ação na parte inferior

3. **Curriculo.java** (MODIFICADO)
   - Implementa Serializable
   - Métodos auxiliares para textos de importação

4. **ImportarCurriculoActivity.java** (MODIFICADO)
   - Redireciona para RevisarCurriculoActivity
   - Remove salvamento direto

5. **PDFParserService.java** (MODIFICADO)
   - Armazena textos completos para revisão

6. **DocxParserService.java** (MODIFICADO)
   - Armazena textos completos para revisão

7. **AndroidManifest.xml** (MODIFICADO)
   - Registro da nova Activity

## 💡 Benefícios para o Usuário

1. **Controle Total**: Usuário vê exatamente o que foi extraído
2. **Correção Fácil**: Pode corrigir erros de extração antes de salvar
3. **Transparência**: Não há "caixa preta" no processo de importação
4. **Economia de Tempo**: Campos já preenchidos, apenas ajustes necessários
5. **Qualidade**: Garante que o currículo salvo está correto

## 🚀 Melhorias Futuras

- [ ] Adicionar preview do PDF/DOCX lado a lado com o formulário
- [ ] Sugestões de correção baseadas em IA
- [ ] Detecção automática de idiomas
- [ ] Extração de foto do currículo
- [ ] Análise ATS automática após importação
- [ ] Suporte para mais formatos (RTF, TXT, etc.)

## 📊 Padrões de Extração

### Nome
- Primeira linha com nome próprio capitalizado
- Padrão: 2-4 palavras capitalizadas

### E-mail
- Formato padrão de e-mail
- Regex: `[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}`

### Telefone
- Formato brasileiro: (XX) XXXXX-XXXX
- Com ou sem código do país (+55)

### Seções
- Identifica seções por palavras-chave:
  - EXPERIÊNCIA / EXPERIENCE
  - FORMAÇÃO / EDUCAÇÃO / EDUCATION
  - HABILIDADES / COMPETÊNCIAS / SKILLS
  - RESUMO / OBJETIVO / PERFIL

## 🔒 Segurança

- Arquivos processados apenas em memória
- Não há armazenamento temporário de arquivos
- Dados sensíveis não são logados
- Validação de tipos de arquivo

## 📝 Notas de Implementação

- Usa Material Design Components
- Compatível com Android 7.0+ (API 24+)
- Suporta modo escuro (se configurado no tema)
- Teclado ajusta automaticamente (adjustResize)
- Navegação com confirmação de descarte
