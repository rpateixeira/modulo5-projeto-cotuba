package br.com.unipds;

import nl.siegmann.epublib.domain.Author;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.GuideReference;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.epub.EpubWriter;
import nl.siegmann.epublib.service.MediatypeService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class GeradorEPUB extends GeradorArquivos{
    @Override
    public void gerar(Path arquivoDeSaida, Ebook ebook) {
        try {
            var epub = new Book();

            //TODO: definir título e autor para o livro
            epub.getMetadata().addTitle(ebook.getTitulo());
            epub.getMetadata().addAuthor(new Author(ebook.getAutor()));

            boolean[] ehPrimeiroCapitulo = {true};

            ebook.getCapitulos().forEach(html->{
                // TODO: usar título do capítulo
                String tituloDoCapitulo = html.getTitulo();
                String epubHtml = """
                                          <html xmlns="http://www.w3.org/1999/xhtml">
                                            <head>
                                              <title>%s</title>
                                            </head>
                                            <body>
                                              %s
                                            </body>
                                          </html>
                                        """.formatted(tituloDoCapitulo,html.getHtml());
                var chapter = new Resource(epubHtml.getBytes(), MediatypeService.XHTML);
                epub.addSection(tituloDoCapitulo, chapter);

                if (ehPrimeiroCapitulo[0]) {
                    epub.getGuide().addReference(new GuideReference(chapter, "text", "Start Reading"));
                    ehPrimeiroCapitulo[0] = false;
                }

            });


            var epubWriter = new EpubWriter();

            try {
                epubWriter.write(epub, Files.newOutputStream(arquivoDeSaida));
            } catch (IOException ex) {
                throw new IllegalStateException("Erro ao criar arquivo EPUB: " + arquivoDeSaida.toAbsolutePath(), ex);
            }

        } catch (Exception ex) {
            throw new IllegalStateException("Erro ao gerar EPUB: " + arquivoDeSaida.toAbsolutePath(), ex);
        }
    }
}
