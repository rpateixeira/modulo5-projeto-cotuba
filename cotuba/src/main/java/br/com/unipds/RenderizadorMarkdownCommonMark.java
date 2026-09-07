package br.com.unipds;

import br.com.unipds.repository.RepositorioMarkdowns;
import jakarta.inject.Inject;
import org.commonmark.node.AbstractVisitor;
import org.commonmark.node.Heading;
import org.commonmark.node.Node;
import org.commonmark.node.Text;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.List;
import java.util.stream.Stream;

public class RenderizadorMarkdownCommonMark implements RenderizadorMarkdown {


    private final RepositorioMarkdowns repositorioMarkdowns;
    @Inject
    public RenderizadorMarkdownCommonMark(RepositorioMarkdowns repositorioMarkdowns) {
        this.repositorioMarkdowns = repositorioMarkdowns;
    }

    @Override
    public List<Capitulo> renderizar(Path diretorioMD) {
        List<Markdown> markdowns = repositorioMarkdowns.buscar(diretorioMD);
        return markdowns.stream().map(markdown -> {
            Parser parser = Parser.builder().build();
            Node document = parser.parse(markdown.conteudo());
            var capitulo = CapituloBuilder.builder();
            capitulo.arquivoMarkdown(markdown.arquivo());
            capitulo.markdown(markdown);
            document.accept(new AbstractVisitor() {
                @Override
                public void visit(Heading heading) {
                    if (heading.getLevel() == 1) {
                        // capítulo
                        String tituloDoCapitulo = ((Text) heading.getFirstChild()).getLiteral();
                        capitulo.titulo(tituloDoCapitulo);
                    } else if (heading.getLevel() == 2) {
                        // seção
                    } else if (heading.getLevel() == 3) {
                        // título
                    }
                }

            });
            HtmlRenderer renderer = HtmlRenderer.builder().build();
            String render = renderer.render(document);
            capitulo.html(render);
            return capitulo.build();
        }).toList();
    }
       // PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:**/*.md");
       /* try (Stream<Path> streamMDs = Files.list(diretorioMD)) {
            List<Path> arquivosMD = streamMDs
                    .filter(matcher::matches)
                    .sorted()
                    .toList();

            if (arquivosMD.isEmpty()) {
                throw new IllegalStateException("Não foram encontrados capítulos (arquivos .md) no diretório: " + diretorioMD.toAbsolutePath());
            }

            return arquivosMD.stream().map(arquivoMD -> {
                Parser parser = Parser.builder().build();
                Node document = null;
                var capitulo = new Capitulo();
                capitulo.setArquivoMarkdown(arquivoMD);
                try {
                    capitulo.setMarkdown(Files.readString(arquivoMD));
                    document = parser.parseReader(Files.newBufferedReader(arquivoMD));
                    document.accept(new AbstractVisitor() {
                        @Override
                        public void visit(Heading heading) {
                            if (heading.getLevel() == 1) {
                                // capítulo
                                String tituloDoCapitulo = ((Text) heading.getFirstChild()).getLiteral();
                                capitulo.setTitulo(tituloDoCapitulo);
                            } else if (heading.getLevel() == 2) {
                                // seção
                            } else if (heading.getLevel() == 3) {
                                // título
                            }
                        }

                    });
                } catch (Exception ex) {
                    throw new IllegalStateException("Erro ao fazer parse do arquivo " + arquivoMD, ex);
                }

                try {
                    HtmlRenderer renderer = HtmlRenderer.builder().build();
                    String render = renderer.render(document);
                    capitulo.setHtml(render);
                    return capitulo;
                } catch (Exception ex) {
                    throw new IllegalStateException("Erro ao renderizar para HTML o arquivo " + arquivoMD, ex);
                }
            }).toList();

        } catch (IOException ex) {
            throw new IllegalStateException("Erro tentando encontrar arquivos .md em " + diretorioMD.toAbsolutePath(), ex);
        }*/

}
