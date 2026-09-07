package br.com.unipds.cotuba.adapters.out;

import br.com.unipds.cotuba.domain.Ebook;
import br.com.unipds.cotuba.domain.FormatoEbook;
import br.com.unipds.cotuba.ports.out.GeradorArquivos;
import br.com.unipds.support.FormatoGeradorArquivos;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfOutline;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.navigation.PdfExplicitDestination;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.IBlockElement;
import com.itextpdf.layout.element.IElement;
import com.itextpdf.layout.properties.AreaBreakType;
import jakarta.enterprise.context.ApplicationScoped;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
@ApplicationScoped
@FormatoGeradorArquivos(FormatoEbook.PDF)
public class GeradorPDF extends GeradorArquivos {
    @Override
    public void gerar(Path arquivoDeSaida, Ebook ebook) {
        try (var writer = new PdfWriter(Files.newOutputStream(arquivoDeSaida));
             var pdf = new PdfDocument(writer);
             var pdfDocument = new Document(pdf)) {

            //TODO: definir título e autor para o livro
            pdf.getDocumentInfo().setTitle(ebook.titulo());
            pdf.getDocumentInfo().setAuthor(ebook.autor());

            ebook.capitulos().forEach(capitulo -> {
                    try {


                        List<IElement> convertToElements = HtmlConverter.convertToElements(capitulo.html());

                        if (pdf.getNumberOfPages() == 0) {
                            pdf.addNewPage();
                        }
                        PdfOutline rootOutline = pdf.getOutlines(false);
                        if (rootOutline == null) {
                            pdf.initializeOutlines();
                            rootOutline = pdf.getOutlines(false);
                        }

                        // TODO: usar título do capítulo
                        String tituloCapitulo = capitulo.titulo();
                        PdfOutline chapterOutline = rootOutline.addOutline(tituloCapitulo);
                        chapterOutline.addDestination(PdfExplicitDestination.createFit(pdf.getLastPage()));

                        for (IElement element : convertToElements) {
                            pdfDocument.add((IBlockElement) element);
                        }
                        // TODO: não adicionar página depois do último capítulo
                        pdfDocument.add(new AreaBreak(AreaBreakType.NEXT_PAGE));

                    } catch (Exception ex) {
                        throw new IllegalStateException("Erro ao renderizar para HTML o arquivo " + capitulo.markdown().nome(), ex);
                    }

                });
            } catch (IOException ex) {
                throw new IllegalStateException("Erro tentando encontrar arquivos .md em " + ebook, ex);
            }
    }
}
