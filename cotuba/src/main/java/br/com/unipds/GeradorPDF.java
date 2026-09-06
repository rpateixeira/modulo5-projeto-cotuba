package br.com.unipds;

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
public class GeradorPDF extends GeradorArquivos{
    @Override
    public void gerar(Path arquivoDeSaida, Ebook ebook) {
        try (var writer = new PdfWriter(Files.newOutputStream(arquivoDeSaida));
             var pdf = new PdfDocument(writer);
             var pdfDocument = new Document(pdf)) {

            //TODO: definir título e autor para o livro
            pdf.getDocumentInfo().setTitle(ebook.getTitulo());
            pdf.getDocumentInfo().setAuthor(ebook.getAutor());

            ebook.getCapitulos().forEach(arquivoMD -> {
                    try {


                        List<IElement> convertToElements = HtmlConverter.convertToElements(arquivoMD.getHtml());

                        if (pdf.getNumberOfPages() == 0) {
                            pdf.addNewPage();
                        }
                        PdfOutline rootOutline = pdf.getOutlines(false);
                        if (rootOutline == null) {
                            pdf.initializeOutlines();
                            rootOutline = pdf.getOutlines(false);
                        }

                        // TODO: usar título do capítulo
                        String tituloCapitulo = arquivoMD.getTitulo();
                        PdfOutline chapterOutline = rootOutline.addOutline(tituloCapitulo);
                        chapterOutline.addDestination(PdfExplicitDestination.createFit(pdf.getLastPage()));

                        for (IElement element : convertToElements) {
                            pdfDocument.add((IBlockElement) element);
                        }
                        // TODO: não adicionar página depois do último capítulo
                        pdfDocument.add(new AreaBreak(AreaBreakType.NEXT_PAGE));

                    } catch (Exception ex) {
                        throw new IllegalStateException("Erro ao renderizar para HTML o arquivo " + arquivoMD, ex);
                    }

                });
            } catch (IOException ex) {
                throw new IllegalStateException("Erro tentando encontrar arquivos .md em " + ebook, ex);
            }
    }
}
