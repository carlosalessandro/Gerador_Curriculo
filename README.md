# 📄 CV Pro - Gerador de Currículo Profissional

<div align="center">

![Logo](https://img.shields.io/badge/CV%20Pro-v1.1-blue)
![Platform](https://img.shields.io/badge/platform-Android-green)
![API](https://img.shields.io/badge/API-24%2B-brightgreen)
![License](https://img.shields.io/badge/license-MIT-blue)

**Um aplicativo Android moderno para criar, editar e analisar currículos profissionais com tecnologia ATS**

[📱 Download APK](#) • [📖 Documentação](#documentação) • [🤝 Contribuir](#contribuir) • [🐛 Issues](#issues)

</div>

## 🌟 Sobre

O CV Pro é um aplicativo Android completo que ajuda profissionais a criar currículos impressionantes com análise ATS (Applicant Tracking System) integrada. Com uma interface intuitiva e recursos avançados, o app facilita a criação de currículos que passam pelos filtros automáticos das empresas.

### ✨ Funcionalidades Principais

- 📝 **Criação de Currículos**: Interface intuitiva para criar currículos do zero
- 📋 **Templates Profissionais**: Diversos modelos modernos e elegantes
- 🤖 **Análise ATS**: Verificação automática para otimizar seu currículo para sistemas de rastreamento
- 📁 **Gerenciamento**: Organize múltiplos currículos em um só lugar
- 📤 **Exportação**: Exporte em PDF, DOCX e outros formatos
- 🌐 **Importação**: Importe currículos existentes de arquivos PDF/DOCX
- 💎 **Modo Premium**: Recursos avançados com assinatura premium
- 📊 **Analytics**: Insights sobre visualizações e performance do currículo

## 📱 Capturas de Tela

<div align="center">
  <img src="https://via.placeholder.com/200x400/4CAF50/FFFFFF?text=Tela+Principal" alt="Tela Principal" width="200"/>
  <img src="https://via.placeholder.com/200x400/2196F3/FFFFFF?text=Editor" alt="Editor" width="200"/>
  <img src="https://via.placeholder.com/200x400/FF9800/FFFFFF?text=ATS" alt="Análise ATS" width="200"/>
</div>

## 🚀 Começando

### Pré-requisitos

- **Android Studio** Arctic Fox ou superior
- **JDK** 8 ou superior
- **Android SDK** API 24+ (Android 7.0)
- **Gradle** 7.0+

### Instalação

1. **Clone o repositório**
   ```bash
   git clone https://github.com/carlosalessandro/Gerador_Curriculo.git
   cd Gerador_Curriculo
   ```

2. **Abra no Android Studio**
   - Abra o Android Studio
   - Selecione "Open an existing project"
   - Navegue até a pasta do projeto

3. **Sincronize o projeto**
   - Deixe o Gradle sincronizar as dependências
   - Aguarde o build completion

4. **Execute o aplicativo**
   - Conecte um dispositivo Android ou inicie um emulador
   - Pressione `Ctrl+Shift+R` ou clique no botão Run

### Build Variants

O projeto suporta duas variantes de build:

- **debug**: Para desenvolvimento e testes
- **release**: Versão otimizada para produção

```bash
# Build debug
./gradlew assembleDebug

# Build release
./gradlew assembleRelease
```

## 🏗️ Arquitetura

O projeto segue as melhores práticas de desenvolvimento Android:

### 📁 Estrutura do Projeto

```
app/
├── src/main/
│   ├── java/com/example/geradorcurriculo/
│   │   ├── MainActivity.java              # Tela principal
│   │   ├── database/                      # Camada de dados
│   │   │   ├── AppDatabase.java           # Room Database
│   │   │   ├── CurriculumDao.java         # DAO para currículos
│   │   │   └── entities/                  # Entidades do banco
│   │   ├── model/                         # Models de dados
│   │   │   ├── Curriculum.java            # Modelo do currículo
│   │   │   ├── PersonalInfo.java          # Informações pessoais
│   │   │   ├── Experience.java            # Experiência profissional
│   │   │   ├── Education.java             # Formação acadêmica
│   │   │   ├── Skills.java                # Habilidades
│   │   │   └── ATSResult.java             # Resultado da análise ATS
│   │   ├── services/                      # Camada de negócio
│   │   │   ├── CurriculumService.java     # Serviço de currículos
│   │   │   ├── ATSService.java            # Serviço de análise ATS
│   │   │   ├── PDFService.java            # Geração de PDF
│   │   │   ├── ImportService.java         # Importação de arquivos
│   │   │   └── PremiumService.java        # Serviços premium
│   │   ├── ui/                           # Interface do usuário
│   │   │   ├── activities/               # Activities principais
│   │   │   ├── adapters/                 # Adaptadores para listas
│   │   │   ├── fragments/                # Fragments
│   │   │   └── dialogs/                  # Dialogs customizados
│   │   └── utils/                        # Utilitários
│   │       ├── Constants.java            # Constantes do app
│   │       ├── PreferencesManager.java   # Gerenciamento de preferências
│   │       ├── FileHelper.java           # Helper para arquivos
│   │       └── DateUtils.java            # Utilitários de data
│   ├── res/                             # Recursos do app
│   │   ├── layout/                      # Layouts XML
│   │   ├── values/                      # Strings, colors, styles
│   │   ├── drawable/                    # Imagens e ícones
│   │   └── mipmap/                      # Ícones do app
│   └── AndroidManifest.xml              # Manifest do aplicativo
├── build.gradle                         # Configuração do módulo
└── proguard-rules.pro                   # Regras de ofuscação
```

### 🧩 Tecnologias Utilizadas

- **Linguagem**: Java 8
- **UI Framework**: Android Jetpack
- **Arquitetura**: MVVM (Model-View-ViewModel)
- **Banco de Dados**: Room Database
- **HTTP Client**: OkHttp
- **JSON Parsing**: Gson
- **PDF Generation**: iText 7
- **Material Design**: Material Components
- **Navigation**: Navigation Component

## 🔧 Configuração

### Variáveis de Ambiente

O aplicativo utiliza algumas configurações que podem ser personalizadas:

```java
// app/src/main/java/com/example/geradorcurriculo/utils/Constants.java
public static final String ATS_API_URL = "https://api.ats-service.com/v1/";
public static final String PREMIUM_PRICE_MONTHLY = "R$19,90";
public static final String PREMIUM_PRICE_LIFETIME = "R$99,90";
```

### Firebase (Opcional)

Para habilitar recursos do Firebase:

1. Crie um projeto no [Firebase Console](https://console.firebase.google.com/)
2. Adicione o arquivo `google-services.json` em `app/src/main/`
3. Descomente as dependências Firebase no `build.gradle`

```gradle
// Firebase
implementation platform('com.google.firebase:firebase-bom:32.7.0')
implementation 'com.google.firebase:firebase-analytics'
implementation 'com.google.firebase:firebase-crashlytics'
```

## 📊 Análise ATS

O sistema de análise ATS verifica seu currículo em múltiplos critérios:

### 🔍 Critérios Avaliados

- **Palavras-chave**: Verificação de termos relevantes para a vaga
- **Formatação**: Estrutura e organização do conteúdo
- **Comprimento**: Densidade e quantidade de informação
- **Contato**: Informações de contato completas
- **Experiência**: Clareza na descrição de experiências
- **Educação**: Formatação acadêmica adequada

### 📈 Score ATS

O score varia de 0-100 e indica a probabilidade de passar por filtros automáticos:

- **90-100**: Excelente - Alta chance de aprovação
- **70-89**: Bom - Boas chances de aprovação
- **50-69**: Regular - Precisa de melhorias
- **0-49**: Ruim - Requer revisão completa

## 💎 Recursos Premium

### 🌟 Funcionalidades Exclusivas

- **Currículos Ilimitados**: Crie quantos currículos quiser
- **ATS Avançado**: Análise mais detalhada com sugestões personalizadas
- **Templates Premium**: Acesso a todos os templates profissionais
- **Exportação Avançada**: Mais formatos e opções de customização
- **Prioridade no Suporte**: Atendimento dedicado
- **Sem Anúncios**: Experiência limpa e focada

### 💰 Planos

- **Mensal**: R$19,90/mês
- **Vitalício**: R$99,90 (pagamento único)

## 🧪 Testes

### Executando Testes

```bash
# Testes unitários
./gradlew test

# Testes de instrumentação
./gradlew connectedAndroidTest

# Todos os testes
./gradlew check
```

### Cobertura de Testes

O projeto visa manter 80% de cobertura de código:

- **Testes Unitários**: Lógica de negócio e utilitários
- **Testes de UI**: Interação do usuário
- **Testes de Integração**: Fluxos completos do aplicativo

## 📦 Deploy

### Google Play Store

1. **Gere a chave de assinatura**
   ```bash
   keytool -genkey -v -keystore release.keystore -alias cvpro -keyalg RSA -keysize 2048 -validity 10000
   ```

2. **Configure o build release**
   ```gradle
   signingConfigs {
       release {
           storeFile file('release.keystore')
           storePassword 'your_store_password'
           keyAlias 'cvpro'
           keyPassword 'your_key_password'
       }
   }
   ```

3. **Build e upload**
   ```bash
   ./gradlew assembleRelease
   # Faça o upload do APK gerado para o Google Play Console
   ```

## 🤝 Contribuir

Contribuições são bem-vindas! Por favor, leia nosso [Guia de Contribuição](CONTRIBUTING.md) antes de começar.

### 🐛 Reportando Issues

- Use o [GitHub Issues](../../issues) para reportar bugs
- Forneça detalhes completos: dispositivo, Android version, passos para reproduzir
- Inclua screenshots se aplicável

### 💡 Sugestões de Features

- Abra uma issue com a tag `enhancement`
- Descreva a funcionalidade proposta
- Explique o caso de uso e benefícios

## 📄 Licença

Este projeto está licenciado sob a Licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

## 📞 Contato

- **Desenvolvedor**: Carlos Alessandro
- **Email**: contato@cvpro.app
- **Website**: [cvpro.app](https://cvpro.app)
- **LinkedIn**: [linkedin.com/in/carlosalessandro](https://linkedin.com/in/carlosalessandro)

## 🙏 Agradecimentos

- Comunidade Android Studio
- Contribuidores de código aberto
- Usuários beta testers
- Designers e UX researchers

---

<div align="center">

**⭐ Se este projeto te ajudou, considere dar uma estrela!**

Made with ❤️ in Brazil

</div>
