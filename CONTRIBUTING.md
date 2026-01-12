# 🤝 Guia de Contribuição

Obrigado pelo seu interesse em contribuir com o CV Pro! Este guia irá ajudá-lo a começar.

## 📋 Índice

- [Código de Conduta](#código-de-conduta)
- [Como Contribuir](#como-contribuir)
- [Setup do Ambiente de Desenvolvimento](#setup-do-ambiente-de-desenvolvimento)
- [Processo de Contribuição](#processo-de-contribuição)
- [Padrões de Código](#padrões-de-código)
- [Testes](#testes)
- [Documentação](#documentação)
- [Reportando Issues](#reportando-issues)
- [Pull Requests](#pull-requests)

## 🤝 Código de Conduta

Ao participar deste projeto, você concorda em seguir nosso [Código de Conduta](CODE_OF_CONDUCT.md):

- Seja respeitoso e inclusivo
- Seja construtivo em seus feedbacks
- Ajude outros contribuidores
- Mantenha um ambiente profissional e acolhedor

## 🚀 Como Contribuir

### 1. Fork o Repositório

```bash
# Fork no GitHub e clone localmente
git clone https://github.com/SEU_USERNAME/Gerador_Curriculo.git
cd Gerador_Curriculo

# Adicione o repositório original como upstream
git remote add upstream https://github.com/carlosalessandro/Gerador_Curriculo.git
```

### 2. Setup do Ambiente de Desenvolvimento

#### Pré-requisitos

- **Android Studio** Arctic Fox ou superior
- **JDK** 8 ou superior
- **Android SDK** API 24+
- **Git** configurado com seu nome e email

#### Configuração

1. **Abra o projeto no Android Studio**
   ```bash
   # Abra o Android Studio e selecione o projeto
   # Ou use a linha de comando (se tiver o Android Studio CLI)
   studio .
   ```

2. **Sincronize as dependências**
   - O Android Studio irá sincronizar automaticamente
   - Se ocorrer erros, tente: `File → Sync Project with Gradle Files`

3. **Configure o AVD (Android Virtual Device)**
   - Tools → AVD Manager
   - Crie um dispositivo com API 24+ ou superior
   - Recomendado: Pixel 4 com API 30

4. **Execute o projeto**
   - Pressione `Ctrl+Shift+R` ou clique no botão Run
   - Verifique se o app inicia corretamente

### 3. Processo de Contribuição

#### 🌟 Tipos de Contribuições

- **Bug Fixes**: Correção de erros existentes
- **New Features**: Funcionalidades novas
- **Documentation**: Melhorias na documentação
- **UI/UX**: Melhorias na interface e experiência
- **Performance**: Otimizações de performance
- **Tests**: Aumento da cobertura de testes

#### 📋 Passos para Contribuir

1. **Escolha uma Issue**
   - Verifique as [issues abertas](../../issues)
   - Comente na issue que deseja trabalhar
   - Aguarde atribuição ou comece se for "good first issue"

2. **Crie uma Branch**
   ```bash
   # Sincronize com o upstream
   git fetch upstream
   git checkout main
   git merge upstream/main

   # Crie uma branch descritiva
   git checkout -b feature/nova-funcionalidade
   # ou
   git checkout -b fix/corrigir-bug-especifico
   ```

3. **Desenvolva sua Solução**
   - Siga os [padrões de código](#padrões-de-código)
   - Adicione testes se aplicável
   - Teste em múltiplos dispositivos/emuladores
   - Verifique não introduzir regressões

4. **Teste Thoroughly**
   ```bash
   # Execute todos os testes
   ./gradlew check

   # Testes unitários
   ./gradlew test

   # Testes de instrumentação
   ./gradlew connectedAndroidTest
   ```

5. **Commit suas Mudanças**
   ```bash
   # Adicione arquivos modificados
   git add .

   # Commit com mensagem descritiva
   git commit -m "feat: adiciona nova funcionalidade de exportação PDF

   - Implementa geração de PDF com template personalizado
   - Adiciona opções de customização de fonte e cores
   - Inclui testes unitários para validação

   Fixes #123"
   ```

6. **Push e Pull Request**
   ```bash
   # Push para seu fork
   git push origin feature/nova-funcionalidade

   # Abra um Pull Request no GitHub
   ```

## 📝 Padrões de Código

### Java Style Guide

Seguimos as convenções do Google Java Style com adaptações para Android:

#### Nomenclatura

```java
// Classes: PascalCase
public class CurriculumService {
    
    // Métodos: camelCase
    public void generatePdfDocument() {
        
    }
    
    // Variáveis: camelCase
    private String curriculumTitle;
    private List<Experience> workExperiences;
    
    // Constantes: UPPER_SNAKE_CASE
    public static final String MAX_FILE_SIZE = "10MB";
    public static final int DEFAULT_TIMEOUT = 30000;
}
```

#### Estrutura de Classe

```java
package com.example.geradorcurriculo.service;

import android.content.Context;
import androidx.annotation.NonNull;
import java.util.List;

/**
 * Serviço responsável pela geração de currículos em PDF.
 * 
 * @author Carlos Alessandro
 * @since 1.0
 */
public class PdfService {
    
    // 1. Constantes estáticas
    private static final String TAG = "PdfService";
    private static final int PDF_QUALITY = 90;
    
    // 2. Variáveis de instância
    private final Context context;
    private final Curriculum curriculum;
    
    // 3. Construtor
    public PdfService(@NonNull Context context, @NonNull Curriculum curriculum) {
        this.context = context;
        this.curriculum = curriculum;
    }
    
    // 4. Métodos públicos
    public void generatePdf(@NonNull PdfCallback callback) {
        // Implementação
    }
    
    // 5. Métodos privados
    private void setupPdfDocument() {
        // Implementação
    }
    
    // 6. Classes internas/interfaces
    public interface PdfCallback {
        void onSuccess(String filePath);
        void onError(Exception exception);
    }
}
```

#### XML Layouts

```xml
<!-- Use nomes descritivos e snake_case -->
<!-- activity_main.xml -->
<androidx.constraintlayout.widget.ConstraintLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <TextView
        android:id="@+id/text_view_title"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="@string/main_title"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintStart_toStartOf="parent" />

</androidx.constraintlayout.widget.ConstraintLayout>
```

### Recursos Android

#### Strings

```xml
<!-- res/values/strings.xml -->
<resources>
    <!-- Nomes descritivos, agrupados por funcionalidade -->
    
    <!-- Main Activity -->
    <string name="main_title">CV Pro</string>
    <string name="main_subtitle">Seu currículo profissional</string>
    
    <!-- Curriculum Creation -->
    <string name="create_curriculum_title">Novo Currículo</string>
    <string name="create_curriculum_hint">Digite o título...</string>
    
    <!-- Error Messages -->
    <string name="error_network">Erro de conexão. Verifique sua internet.</string>
    <string name="error_file_too_large">Arquivo muito grande. Máximo: %s</string>
</resources>
```

#### Cores

```xml
<!-- res/values/colors.xml -->
<resources>
    <!-- Brand Colors -->
    <color name="colorPrimary">#2196F3</color>
    <color name="colorPrimaryDark">#1976D2</color>
    <color name="colorAccent">#FF4081</color>
    
    <!-- Semantic Colors -->
    <color name="success">#4CAF50</color>
    <color name="warning">#FF9800</color>
    <color name="error">#F44336</color>
    
    <!-- Neutral Colors -->
    <color name="textPrimary">#212121</color>
    <color name="textSecondary">#757575</color>
    <color name="background">#FAFAFA</color>
</resources>
```

## 🧪 Testes

### Estrutura de Testes

```
app/src/
├── test/                          # Testes unitários (JVM)
│   └── java/com/example/geradorcurriculo/
│       ├── CurriculumServiceTest.java
│       ├── AtsServiceTest.java
│       └── PdfServiceTest.java
└── androidTest/                   # Testes de instrumentação (Android)
    └── java/com/example/geradorcurriculo/
        ├── MainActivityTest.java
        ├── CurriculumCreationTest.java
        └── AtsAnalysisTest.java
```

### Escrevendo Testes

#### Testes Unitários

```java
@RunWith(MockitoJUnitRunner.class)
public class CurriculumServiceTest {
    
    @Mock
    private Context mockContext;
    
    @Mock
    private CurriculumDao mockDao;
    
    private CurriculumService curriculumService;
    
    @Before
    public void setUp() {
        curriculumService = new CurriculumService(mockContext, mockDao);
    }
    
    @Test
    public void saveCurriculum_ValidCurriculum_ReturnsSuccess() {
        // Given
        Curriculum curriculum = createValidCurriculum();
        
        // When
        boolean result = curriculumService.saveCurriculum(curriculum);
        
        // Then
        assertTrue(result);
        verify(mockDao).insert(curriculum);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void saveCurriculum_NullCurriculum_ThrowsException() {
        // When/Then
        curriculumService.saveCurriculum(null);
    }
    
    private Curriculum createValidCurriculum() {
        // Helper method para criar dados de teste
        return new Curriculum("Título", "Descrição");
    }
}
```

#### Testes de UI

```java
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    
    @Rule
    public ActivityTestRule<MainActivity> activityRule = 
        new ActivityTestRule<>(MainActivity.class);
    
    @Test
    public void clickCreateCurriculumButton_opiensCreateActivity() {
        // When
        onView(withId(R.id.btn_create_curriculum)).perform(click());
        
        // Then
        intended(hasComponent(CriarCurriculoActivity.class.getName()));
    }
    
    @Test
    public void curriculumList_displaysCorrectData() {
        // Given
        List<Curriculum> curriculums = createTestCurriculums();
        
        // When
        onView(withId(R.id.recycler_curriculums)).check(matches(isDisplayed()));
        
        // Then
        onView(withText("Currículo Teste")).check(matches(isDisplayed()));
    }
}
```

### Cobertura de Testes

- **Meta**: 80% de cobertura
- **Ferramentas**: JaCoCo
- **Relatórios**: Gerados automaticamente no CI

```bash
# Verificar cobertura
./gradlew jacocoTestReport

# Ver relatório
open app/build/reports/jacoco/jacocoTestReport/html/index.html
```

## 📚 Documentação

### Comentários de Código

```java
/**
 * Analisa um currículo usando algoritmos ATS.
 * 
 * <p>Este método utiliza machine learning para identificar palavras-chave
 * relevantes e otimizar o currículo para sistemas de rastreamento de candidatos.
 * 
 * @param curriculum O currículo a ser analisado
 * @param targetJob O cargo alvo para análise personalizada
 * @return Resultado da análise com score e sugestões
 * @throws IllegalArgumentException Se curriculum ou targetJob forem nulos
 * @throws NetworkException Se ocorrer erro na chamada à API
 * 
 * @since 1.0
 * @author Carlos Alessandro
 * 
 * @see ATSResult
 * @see CurriculumService
 */
public ATSResult analyzeCurriculum(@NonNull Curriculum curriculum, 
                                 @NonNull String targetJob) {
    // Implementação
}
```

### Atualização de README

- Mantenha o README atualizado com novas funcionalidades
- Adicione screenshots para novas telas
- Atualize pré-requisitos se necessário

## 🐛 Reportando Issues

### Bug Reports

Use o template de bug report:

```markdown
## 🐛 Bug Report
**Descrição**: Breve descrição do problema

**Passos para Reproduzir**:
1. Vá para '...'
2. Clique em '....'
3. Role para '....'
4. Veja erro

**Comportamento Esperado**: Descrição do que deveria acontecer

**Comportamento Atual**: Descrição do que acontece

**Screenshots**: Se aplicável

**Informações do Dispositivo**:
 - Dispositivo: [ex: Pixel 4]
 - Android Version: [ex: 11]
 - Versão do App: [ex: 1.1.0]

**Informações Adicionais**: Qualquer contexto relevante
```

### Feature Requests

```markdown
## ✨ Feature Request
**Título**: Título claro e conciso

**Descrição**: Descrição detalhada da funcionalidade

**Problema**: Qual problema esta feature resolve?

**Solução Proposta**: Como você imagina a solução?

**Alternativas**: Outras soluções consideradas

**Mockups/Screenshots**: Se tiver

**Contexto Adicional**: Qualquer informação relevante
```

## 🔄 Pull Requests

### Checklist antes de abrir PR

- [ ] Código segue os padrões do projeto
- [ ] Testes passando (verde no CI)
- [ ] Documentação atualizada
- [ ] Commits com mensagens claras
- [ ] Branch atualizada com main
- [ ] Sem conflitos de merge
- [ ] Arquivos desnecessários removidos
- [ ] Performance não impactada negativamente

### Template de PR

```markdown
## 📝 Descrição
Breve descrição das mudanças

## 🔧 Tipo de Mudança
- [ ] Bug fix
- [ ] New feature
- [ ] Breaking change
- [ ] Documentation update

## 🧪 Testes
- [ ] Unit tests passando
- [ ] Integration tests passando
- [ ] Manual tests realizados

## 📸 Screenshots
Se aplicável, inclua screenshots

## 🔗 Issues Relacionadas
Fixes #123
Closes #456

## ✅ Checklist
- [ ] Meu código segue os padrões do projeto
- [ ] Realizei self-review do meu código
- [ ] Adicionei comentários em áreas complexas
- [ ] Documentei minhas mudanças
- [ ] Testes passando localmente
```

### Processo de Review

1. **Auto-review**: Revise seu próprio PR
2. **Atribuição**: Request review de mantenedores
3. **Feedback**: Responda aos comentários prontamente
4. **Correções**: Faça as correções solicitadas
4. **Aprovação**: Aguarde aprovação para merge
5. **Merge**: Mantenedor fará o merge

## 🏆 Reconhecimento

Contribuidores serão reconhecidos em:

- README.md (seção de contribuidores)
- Release notes
- Posts de blog sobre features
- Badges especiais no GitHub

## 📞 Ajuda

Precisa de ajuda? Entre em contato:

- **Discord**: [Canal de desenvolvimento]
- **Email**: dev@cvpro.app
- **GitHub Issues**: Para dúvidas técnicas

---

**Obrigado por contribuir com o CV Pro! 🎉**
