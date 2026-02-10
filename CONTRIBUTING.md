# 🤝 Contribuindo para o CV Pro

Obrigado por considerar contribuir para o CV Pro! Este documento fornece diretrizes para contribuições.

## 📋 Código de Conduta

### Nossos Compromissos

- Ser respeitoso e inclusivo
- Aceitar críticas construtivas
- Focar no que é melhor para a comunidade
- Mostrar empatia com outros membros

## 🚀 Como Contribuir

### 1. Reportar Bugs

Encontrou um bug? Ajude-nos a melhorar!

**Antes de reportar**:
- Verifique se o bug já foi reportado
- Teste na versão mais recente
- Colete informações relevantes

**Ao reportar, inclua**:
- Descrição clara do problema
- Passos para reproduzir
- Comportamento esperado vs atual
- Screenshots (se aplicável)
- Versão do Android
- Modelo do dispositivo
- Logs de erro

### 2. Sugerir Melhorias

Tem uma ideia? Compartilhe!

**Ao sugerir**:
- Descreva claramente a funcionalidade
- Explique o problema que resolve
- Forneça exemplos de uso
- Considere alternativas

### 3. Contribuir com Código

#### Preparação

1. **Fork** o repositório
2. **Clone** seu fork
```bash
git clone https://github.com/seu-usuario/cv-pro.git
```

3. **Crie** uma branch
```bash
git checkout -b feature/minha-funcionalidade
```

#### Desenvolvimento

**Padrões de Código**:
- Siga o estilo Java existente
- Use nomes descritivos
- Adicione comentários quando necessário
- Mantenha métodos pequenos e focados
- Evite código duplicado

**Estrutura de Commits**:
```
tipo(escopo): descrição curta

Descrição detalhada (opcional)

Closes #123
```

**Tipos**:
- `feat`: Nova funcionalidade
- `fix`: Correção de bug
- `docs`: Documentação
- `style`: Formatação
- `refactor`: Refatoração
- `test`: Testes
- `chore`: Manutenção

**Exemplos**:
```bash
feat(ia): adiciona análise de soft skills
fix(import): corrige parsing de PDF com imagens
docs(readme): atualiza instruções de instalação
```

#### Testes

- Teste suas mudanças manualmente
- Verifique em diferentes dispositivos
- Teste casos extremos
- Garanta que não quebrou funcionalidades existentes

#### Pull Request

1. **Atualize** sua branch
```bash
git fetch upstream
git rebase upstream/main
```

2. **Push** suas mudanças
```bash
git push origin feature/minha-funcionalidade
```

3. **Abra** um Pull Request

**No PR, inclua**:
- Descrição clara das mudanças
- Referência a issues relacionadas
- Screenshots (se UI)
- Checklist de testes
- Breaking changes (se houver)

**Template de PR**:
```markdown
## Descrição
Breve descrição das mudanças

## Tipo de Mudança
- [ ] Bug fix
- [ ] Nova funcionalidade
- [ ] Breaking change
- [ ] Documentação

## Como Testar
1. Passo 1
2. Passo 2
3. Passo 3

## Checklist
- [ ] Código segue o padrão do projeto
- [ ] Comentários adicionados quando necessário
- [ ] Documentação atualizada
- [ ] Testado manualmente
- [ ] Sem warnings de compilação

## Screenshots
(se aplicável)

## Issues Relacionadas
Closes #123
```

## 📝 Diretrizes Específicas

### Java

```java
// ✅ BOM
public class CurriculoService {
    private static final String TAG = "CurriculoService";
    
    /**
     * Calcula o score ATS do currículo
     * @param curriculo Currículo a ser analisado
     * @return Score de 0 a 100
     */
    public int calcularScore(Curriculo curriculo) {
        if (curriculo == null) {
            Log.e(TAG, "Currículo nulo");
            return 0;
        }
        // Lógica...
    }
}

// ❌ EVITAR
public class cs {
    public int calc(Curriculo c) {
        return c.getScore(); // Sem validação
    }
}
```

### XML

```xml
<!-- ✅ BOM -->
<TextView
    android:id="@+id/text_titulo"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:text="@string/titulo"
    android:textSize="18sp"
    android:textStyle="bold" />

<!-- ❌ EVITAR -->
<TextView android:id="@+id/tv1" android:layout_width="match_parent" 
android:layout_height="wrap_content" android:text="Título" />
```

### Recursos

- Use `strings.xml` para textos
- Use `dimens.xml` para dimensões
- Use `colors.xml` para cores
- Nomeie recursos de forma descritiva

## 🎨 UI/UX

### Princípios

- Siga Material Design
- Mantenha consistência visual
- Priorize acessibilidade
- Teste em diferentes tamanhos de tela

### Cores

Use as cores definidas em `colors.xml`:
```xml
<color name="primary_color">#1976D2</color>
<color name="accent_color">#FF4081</color>
```

### Animações

- Mantenha suaves (200-300ms)
- Use interpoladores adequados
- Não abuse de animações

## 🔒 Segurança

### Nunca Commite

- ❌ Chaves API
- ❌ Senhas
- ❌ Tokens
- ❌ Dados pessoais
- ❌ Certificados

### Use

- ✅ Variáveis de ambiente
- ✅ Arquivos .gitignore
- ✅ Configurações locais

## 📚 Documentação

### Código

```java
/**
 * Descrição breve do método
 * 
 * Descrição detalhada (se necessário)
 * 
 * @param parametro Descrição do parâmetro
 * @return Descrição do retorno
 * @throws Exception Quando ocorre erro
 */
```

### README

- Atualize se adicionar funcionalidades
- Mantenha exemplos atualizados
- Adicione screenshots se relevante

## 🧪 Testes

### Manuais

Teste pelo menos:
- Fluxo principal
- Casos de erro
- Casos extremos
- Diferentes dispositivos

### Automatizados (Futuro)

```java
@Test
public void testCalcularScore() {
    Curriculo curriculo = new Curriculo();
    // Setup...
    
    int score = service.calcularScore(curriculo);
    
    assertTrue(score >= 0 && score <= 100);
}
```

## 🏷️ Versionamento

Seguimos [Semantic Versioning](https://semver.org/):

- **MAJOR**: Mudanças incompatíveis
- **MINOR**: Novas funcionalidades compatíveis
- **PATCH**: Correções de bugs

Exemplo: `1.2.3`

## 📞 Comunicação

### Canais

- **Issues**: Bugs e sugestões
- **Pull Requests**: Código
- **Discussions**: Perguntas gerais
- **Email**: suporte@cvpro.app

### Tempo de Resposta

- Issues: 1-3 dias úteis
- Pull Requests: 3-7 dias úteis
- Emails: 1-2 dias úteis

## 🎯 Prioridades

### Alta Prioridade

- Bugs críticos
- Problemas de segurança
- Perda de dados
- Crashes

### Média Prioridade

- Bugs não críticos
- Melhorias de performance
- Novas funcionalidades

### Baixa Prioridade

- Melhorias de UI
- Refatorações
- Documentação

## 🏆 Reconhecimento

Contribuidores serão:
- Listados no README
- Mencionados nas release notes
- Creditados no app (se contribuição significativa)

## 📄 Licença

Ao contribuir, você concorda que suas contribuições serão licenciadas sob a mesma licença MIT do projeto.

---

## 💡 Dicas

### Para Iniciantes

1. Comece com issues marcadas como `good first issue`
2. Leia o código existente
3. Faça perguntas
4. Comece pequeno

### Para Experientes

1. Revise Pull Requests
2. Ajude com issues complexas
3. Melhore a arquitetura
4. Adicione testes

## 📚 Recursos

- [Android Developers](https://developer.android.com/)
- [Material Design](https://material.io/)
- [Java Style Guide](https://google.github.io/styleguide/javaguide.html)
- [Git Best Practices](https://git-scm.com/book/en/v2)

---

## ❓ Dúvidas?

Não hesite em perguntar! Estamos aqui para ajudar.

- Abra uma issue
- Envie um email
- Participe das discussions

---

**Obrigado por contribuir! 🎉**

Juntos, tornamos o CV Pro melhor para todos.
