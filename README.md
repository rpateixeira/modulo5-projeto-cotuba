# Cotuba

O **Cotuba** é um gerador de ebooks via linha de comando (CLI) que converte arquivos em formato Markdown (`.md`) para os formatos **PDF** e **EPUB**. 

Ele é projetado para ser simples, rápido e executável em qualquer ambiente com o Java instalado, empacotando todas as suas dependências em um único arquivo (*fat jar*).

## 🛠 Tecnologias Utilizadas

* **Linguagem:** Java 25
* **Gerenciador de Dependências:** Maven
* **Empacotamento:** Maven Shade Plugin (Geração do *Fat JAR*)
* **Bibliotecas Principais:**
  * **Apache Commons CLI:** Para o parsing dos argumentos de linha de comando.
  * **CommonMark:** Para o parsing e renderização dos arquivos Markdown.
  * **iTextPDF:** Para a geração do arquivo final em PDF.
  * **Epublib:** Para a geração do arquivo final em EPUB.

---

## ⚙️ Como compilar o projeto

Para construir o projeto e gerar o executável com todas as dependências embutidas, certifique-se de ter o Java 25 e o Maven instalados. No diretório raiz do projeto, execute:

```bash
cd cotuba/
mvn clean package
```

Isso irá compilar o código e gerar um arquivo `.jar` dentro da pasta `target/` (ex: `cotuba-1.0-SNAPSHOT.jar`).

---

## 🚀 Como usar

O Cotuba é executado via terminal passando o `.jar` gerado. 

### Opções de Linha de Comando

| Opção | Argumento Longo | Descrição | Valor Padrão |
| :--- | :--- | :--- | :--- |
| `-d` | `--dir <arg>` | Diretório que contém os arquivos `.md`. | Diretório atual (`.`) |
| `-f` | `--format <arg>`| Formato de saída do ebook (`pdf` ou `epub`). | `pdf` |
| `-o` | `--output <arg>`| Nome do arquivo de saída do ebook gerado. | `book.{formato}` |
| `-v` | `--verbose` | Habilita o modo verboso para depuração/logs de erro. | Desativado |

### Livro de Exemplo

A pasta `apostila-design/` contém um livro de exemplo com 12 capítulos sobre **Software Design & System Design**, que cobre desde fundamentação teórica até implementação prática de padrões arquiteturais modernos.

Os arquivos Markdown são processados em ordem alfabética e convertidos em capítulos do ebook gerado.

### Exemplos de Uso

**1. Gerar um PDF (Comportamento Padrão)**
Se você apontar apenas o diretório, o Cotuba vai ler os arquivos Markdown e gerar um arquivo chamado `book.pdf` no diretório atual.

```bash
java -jar target/cotuba-1.0-SNAPSHOT.jar -d ../apostila-design
```
*Equivalente a rodar explicitamente com a flag de formato:*
```bash
java -jar target/cotuba-1.0-SNAPSHOT.jar -d ../apostila-design -f pdf
```

**2. Gerar um EPUB**
Para alterar o formato de saída para EPUB, utilize a flag `-f epub`. Isso irá gerar um arquivo `book.epub`.

```bash
java -jar target/cotuba-1.0-SNAPSHOT.jar -d ../apostila-design -f epub
```

**3. Customizar o nome do arquivo de saída**
Você pode usar a flag `-o` para definir o nome exato e o caminho do arquivo gerado.

```bash
java -jar target/cotuba-1.0-SNAPSHOT.jar -d ../apostila-design -o apostila-design.pdf
```

---

## 🔌 Plugins

O Cotuba possui um mecanismo de **plugins** baseado no **Java SPI (`ServiceLoader`)**. Qualquer módulo que implemente a interface `br.com.unipds.cotuba.plugin.CotubaPlugin` e a registre em `META-INF/services/br.com.unipds.cotuba.plugin.CotubaPlugin` é carregado automaticamente pelo Cotuba, desde que esteja presente no **classpath**.

A interface expõe dois pontos de extensão:

| Método | Quando é chamado | Uso |
| :--- | :--- | :--- |
| `String aposRenderizacao(String html)` | Após cada capítulo Markdown ser renderizado em HTML | Transformar/enriquecer o HTML (ex.: injetar CSS). Retorne o HTML modificado. |
| `void aposGeracao(Ebook ebook)` | Após o ebook final ser gerado | Executar ações finais sobre o ebook (ex.: gerar relatórios/estatísticas). |

### Plugins disponíveis

| Plugin | Módulo | O que faz |
| :--- | :--- | :--- |
| **Tema CSS** | `tema-css` | Injeta um tema CSS no HTML dos capítulos (estiliza títulos `h1`/`h2`), personalizando a aparência do ebook gerado. |
| **Estatísticas do Ebook** | `estatisticas-ebook` | Após a geração, analisa o conteúdo de todos os capítulos e imprime no console a contagem de ocorrências de cada palavra. |

### Como compilar os plugins

Cada plugin depende do artefato `cotuba`. Primeiro instale o `cotuba` no repositório Maven local e depois empacote os plugins:

```bash
# 1. Instalar o cotuba no repositório local (necessário para os plugins)
mvn -f cotuba/pom.xml clean install

# 2. Empacotar os plugins
mvn -f tema-css/pom.xml clean package
mvn -f estatisticas-ebook/pom.xml clean package
```

### Como executar o Cotuba com plugins

> ⚠️ **Importante:** os plugins **não são executáveis** — são bibliotecas carregadas via classpath.
> Além disso, `java -jar cotuba.jar` **ignora** o `-cp`/`-classpath`. Portanto, para usar plugins **não** utilize `-jar`: coloque todos os `.jar` no classpath e invoque a classe `Main` explicitamente.

**Executando com os dois plugins** (a partir da raiz do projeto):

```bash
java -cp "cotuba/target/cotuba-1.0-SNAPSHOT.jar:tema-css/target/tema-css-1.0-SNAPSHOT.jar:estatisticas-ebook/target/estatisticas-ebook-1.0-SNAPSHOT.jar" \
  br.com.unipds.cotuba.adapters.in.cli.Main -d apostila-design -o apostila-design.pdf
```

Para usar **apenas um** plugin, basta incluir somente o `.jar` desejado no classpath. Exemplo apenas com o tema CSS:

```bash
java -cp "cotuba/target/cotuba-1.0-SNAPSHOT.jar:tema-css/target/tema-css-1.0-SNAPSHOT.jar" \
  br.com.unipds.cotuba.adapters.in.cli.Main -d apostila-design -f epub
```

> 💡 No **Windows**, troque o separador de classpath `:` por `;`.

### Como criar seu próprio plugin

1. Crie um módulo com dependência no artefato `cotuba`.
2. Implemente a interface `br.com.unipds.cotuba.plugin.CotubaPlugin`.
3. Registre a implementação criando o arquivo:
   `src/main/resources/META-INF/services/br.com.unipds.cotuba.plugin.CotubaPlugin`
   contendo o nome totalmente qualificado da sua classe, por exemplo:
   ```
   br.com.seupacote.MeuPlugin
   ```
4. Empacote (`mvn package`) e inclua o `.jar` gerado no classpath ao executar o Cotuba.

