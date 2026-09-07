package br.com.unipds;

import br.com.unipds.support.FormatoGeradorArquivos;
import jakarta.enterprise.context.ApplicationScoped;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@ApplicationScoped
@FormatoGeradorArquivos(FormatoEbook.HTML)
public class GeradorHTML extends GeradorArquivos {

    @Override
    public void gerar(Path arquivoDeSaida, EbookBuilder ebook) {
        try {
            // O arquivoDeSaida é tratado como o diretório onde os HTMLs serão escritos.
            Path diretorioDeSaida = resolverDiretorioDeSaida(arquivoDeSaida);
            Files.createDirectories(diretorioDeSaida);

            List<Capitulo> capitulos = ebook.build().capitulos();
            StringBuilder itensDoSumario = new StringBuilder();

            for (int i = 0; i < capitulos.size(); i++) {
                Capitulo capitulo = capitulos.get(i);

                String tituloCapitulo = tituloDoCapitulo(capitulo, i);
                String nomeArquivo = "capitulo-" + (i + 1) + ".html";

                // Adiciona o link do capítulo no sumário
                itensDoSumario
                        .append("      <li><a href=\"")
                        .append(nomeArquivo)
                        .append("\">")
                        .append(escapar(tituloCapitulo))
                        .append("</a></li>\n");

                // Escreve o arquivo HTML do capítulo
                String htmlDoCapitulo = montarPaginaDoCapitulo(tituloCapitulo, capitulo.html());
                Files.writeString(diretorioDeSaida.resolve(nomeArquivo), htmlDoCapitulo, StandardCharsets.UTF_8);
            }

            // Escreve o sumário (index.html) com os links para cada capítulo
            String sumario = montarSumario(ebook.titulo(), itensDoSumario.toString());
            Files.writeString(diretorioDeSaida.resolve("index.html"), sumario, StandardCharsets.UTF_8);

        } catch (IOException ex) {
            throw new IllegalStateException("Erro ao gerar HTML em: " + arquivoDeSaida.toAbsolutePath(), ex);
        }
    }

    private Path resolverDiretorioDeSaida(Path arquivoDeSaida) {
        String nome = arquivoDeSaida.getFileName().toString();
        int posicaoDaExtensao = nome.lastIndexOf('.');
        if (posicaoDaExtensao > 0) {
            // Remove a extensão (ex.: book.html -> book) para usar como nome do diretório
            String nomeSemExtensao = nome.substring(0, posicaoDaExtensao);
            Path pai = arquivoDeSaida.getParent();
            return pai != null ? pai.resolve(nomeSemExtensao) : Path.of(nomeSemExtensao);
        }
        return arquivoDeSaida;
    }

    private String tituloDoCapitulo(Capitulo capitulo, int indice) {
        String titulo = capitulo.titulo();
        if (titulo != null && !titulo.isBlank()) {
            return titulo;
        }
        return "Capítulo " + (indice + 1);
    }

    private String montarPaginaDoCapitulo(String titulo, String conteudoHtml) {
        return """
                <!DOCTYPE html>
                <html lang="pt-BR">
                  <head>
                    <meta charset="UTF-8"/>
                    <title>%s</title>
                  </head>
                  <body>
                    <p><a href="index.html">&larr; Sumário</a></p>
                    %s
                  </body>
                </html>
                """.formatted(escapar(titulo), conteudoHtml == null ? "" : conteudoHtml);
    }

    private String montarSumario(String tituloDoEbook, String itens) {
        String titulo = (tituloDoEbook != null && !tituloDoEbook.isBlank()) ? tituloDoEbook : "Sumário";
        return """
                <!DOCTYPE html>
                <html lang="pt-BR">
                  <head>
                    <meta charset="UTF-8"/>
                    <title>%s</title>
                  </head>
                  <body>
                    <h1>%s</h1>
                    <nav>
                      <ul>
                %s      </ul>
                    </nav>
                  </body>
                </html>
                """.formatted(escapar(titulo), escapar(titulo), itens);
    }

    private String escapar(String texto) {
        if (texto == null) {
            return "";
        }
        return texto
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;");
    }
}
