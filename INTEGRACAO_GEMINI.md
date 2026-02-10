# Integração com Google Gemini AI

## 🤖 Visão Geral

Integração completa com o Google Gemini AI para fornecer recursos avançados de análise e otimização de currículos usando inteligência artificial.

## ✨ Recursos Implementados

### 1. Configuração Segura de API Key
- **Tela de Configurações**: Interface intuitiva para inserir e gerenciar a chave API
- **Criptografia**: Chave armazenada de forma criptografada usando Android Keystore
- **Validação**: Verificação de formato e teste de conexão
- **Privacidade**: Chave armazenada apenas no dispositivo do usuário

### 2. Serviços de IA Disponíveis

#### Análise de Currículo
- Análise completa do currículo
- 5 sugestões específicas de melhoria
- Feedback personalizado baseado no conteúdo

#### Geração de Resumo Profissional
- Cria resumos profissionais impactantes
- Baseado em experiência, formação e habilidades
- Máximo de 3 parágrafos otimizados

#### Otimização para ATS
- Análise específica para sistemas ATS
- Sugestões de palavras-chave
- Compatibilidade com descrição de vaga

## 🔒 Segurança

### Armazenamento Criptografado
```java
// Usa Android Keystore para criptografia AES/GCM
- Chave criptografada com AES-256
- IV (Initialization Vector) único para cada criptografia
- Armazenamento em SharedPreferences criptografado
```

### Proteção de Dados
- ✅ Chave nunca é enviada para servidores próprios
- ✅ Comunicação direta com API do Google
- ✅ HTTPS obrigatório
- ✅ Sem logs de dados sensíveis
- ✅ Usuário pode remover a chave a qualquer momento

## 📱 Interface do Usuário

### Tela de Configurações

#### Seção 1: Integração com IA
- Campo de entrada para API Key (com toggle de visibilidade)
- Botão "Testar" - Verifica conexão com a API
- Botão "Salvar" - Armazena a chave de forma segura
- Indicador de status visual (cores e emojis)

#### Seção 2: Como Obter a Chave
- Instruções passo a passo
- Link direto para ai.google.dev
- Botão para abrir o site

#### Seção 3: Recursos com IA
- Lista de funcionalidades disponíveis
- Descrição de cada recurso

#### Seção 4: Segurança e Privacidade
- Informações sobre proteção de dados
- Garantias de privacidade

## 🔧 Componentes Técnicos

### Arquivos Criados

1. **GeminiConfigManager.java**
   - Gerenciamento seguro de configurações
   - Criptografia/descriptografia de chave
   - Validação de formato

2. **GeminiService.java**
   - Comunicação com API do Gemini
   - Métodos de análise e otimização
   - Parse de respostas

3. **ConfiguracoesActivity.java**
   - Interface de configuração
   - Teste de conexão
   - Feedback visual

4. **activity_configuracoes.xml**
   - Layout responsivo
   - Material Design
   - Cards informativos

### Dependências Adicionadas

```gradle
// HTTP client para comunicação com Gemini API
implementation 'com.squareup.okhttp3:okhttp:4.9.3'
```

**Nota**: Usamos OkHttp diretamente ao invés da biblioteca oficial do Gemini para manter compatibilidade total com projetos Java puros, sem necessidade de Kotlin ou Coroutines.

## 🚀 Como Usar

### Para o Usuário

1. **Obter Chave API**
   - Acessar ai.google.dev
   - Fazer login com conta Google
   - Clicar em "Get API Key"
   - Copiar a chave gerada

2. **Configurar no App**
   - Abrir "Meus Currículos"
   - Menu → Configurações
   - Colar a chave API
   - Clicar em "Testar" (opcional)
   - Clicar em "Salvar"

3. **Usar Recursos de IA**
   - Análise de currículo
   - Sugestões de melhoria
   - Otimização ATS
   - Geração de resumo

### Para o Desenvolvedor

```java
// Inicializar serviço
GeminiService geminiService = new GeminiService(context);

// Analisar currículo
List<String> sugestoes = geminiService.analisarCurriculo(curriculo);

// Gerar resumo
String resumo = geminiService.gerarResumoProfissional(curriculo);

// Otimizar para ATS
List<String> otimizacoes = geminiService.otimizarParaATS(curriculo, vagaDescricao);

// Testar conexão
boolean conectado = geminiService.testConnection();
```

## 📊 Fluxo de Dados

```
Usuário → ConfiguracoesActivity
    ↓
GeminiConfigManager (criptografa)
    ↓
SharedPreferences (armazenamento seguro)
    ↓
GeminiService (usa chave)
    ↓
API Gemini (HTTPS)
    ↓
Resposta processada
    ↓
Exibição para usuário
```

## 🎯 Casos de Uso

### 1. Análise de Currículo Importado
```java
// Após importar PDF/DOCX
Curriculo curriculo = parsePDF(uri);
List<String> sugestoes = geminiService.analisarCurriculo(curriculo);
// Exibir sugestões na tela de revisão
```

### 2. Otimização para Vaga Específica
```java
// Usuário cola descrição da vaga
String vagaDescricao = inputVaga.getText().toString();
List<String> otimizacoes = geminiService.otimizarParaATS(curriculo, vagaDescricao);
// Exibir sugestões de otimização
```

### 3. Geração de Resumo Profissional
```java
// Botão "Gerar com IA"
String resumo = geminiService.gerarResumoProfissional(curriculo);
inputResumo.setText(resumo);
```

## ⚠️ Tratamento de Erros

### Chave Não Configurada
```java
if (!configManager.isGeminiConfigured()) {
    // Redirecionar para configurações
    // Ou exibir mensagem informativa
}
```

### Erro de Conexão
```java
try {
    String response = geminiService.analisarCurriculo(curriculo);
} catch (Exception e) {
    // Exibir mensagem amigável
    // Sugerir verificar conexão/chave
}
```

### Limite de API
```java
// Implementar retry com backoff
// Informar usuário sobre limites
// Sugerir aguardar alguns minutos
```

## 💡 Melhorias Futuras

- [ ] Cache de respostas para economizar chamadas
- [ ] Modo offline com sugestões pré-definidas
- [ ] Histórico de análises
- [ ] Comparação antes/depois
- [ ] Suporte a múltiplos idiomas
- [ ] Análise de compatibilidade com vagas
- [ ] Score de qualidade do currículo
- [ ] Sugestões de formatação visual
- [ ] Integração com LinkedIn para importação
- [ ] Análise de soft skills

## 📈 Métricas e Analytics

### Eventos para Rastrear
- Configuração de chave API
- Teste de conexão (sucesso/falha)
- Uso de análise de currículo
- Uso de geração de resumo
- Uso de otimização ATS
- Erros de API

## 🔐 Conformidade e Privacidade

### LGPD/GDPR
- ✅ Dados processados apenas com consentimento
- ✅ Usuário controla seus dados
- ✅ Possibilidade de exclusão de dados
- ✅ Transparência no uso de IA
- ✅ Sem compartilhamento com terceiros

### Termos de Uso do Gemini
- Respeitar limites de taxa da API
- Não usar para conteúdo proibido
- Atribuir uso de IA quando apropriado
- Seguir políticas do Google AI

## 📝 Notas de Implementação

### Android Keystore
- Disponível a partir do Android 6.0 (API 23)
- Fallback para dispositivos antigos (Base64)
- Chaves não exportáveis
- Proteção contra root/jailbreak

### Requisitos de Rede
- Permissão INTERNET (já incluída)
- Timeout de 30 segundos
- Retry automático em caso de falha temporária

### Performance
- Chamadas assíncronas (Thread separada)
- Feedback visual durante processamento
- Cancelamento de requisições longas

## 🎨 Design e UX

### Princípios
- **Simplicidade**: Processo de configuração em 3 passos
- **Transparência**: Status claro e feedback visual
- **Segurança**: Indicadores de proteção de dados
- **Acessibilidade**: Instruções claras e ajuda contextual

### Cores de Status
- 🟢 Verde: Configurado e funcionando
- 🔵 Azul: Testando conexão
- 🔴 Vermelho: Erro ou não configurado
- 🟡 Amarelo: Aviso ou atenção necessária

## 🧪 Testes

### Testes Manuais
1. Configurar chave válida
2. Configurar chave inválida
3. Testar sem conexão
4. Testar com conexão lenta
5. Remover e reconfigurar chave
6. Usar recursos de IA
7. Verificar criptografia

### Testes Automatizados (Futuro)
- Unit tests para GeminiConfigManager
- Integration tests para GeminiService
- UI tests para ConfiguracoesActivity
