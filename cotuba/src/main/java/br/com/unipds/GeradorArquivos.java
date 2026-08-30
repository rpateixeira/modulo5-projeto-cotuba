package br.com.unipds;

import java.nio.file.Path;

public abstract class GeradorArquivos {

    public static GeradorArquivos getInstance(FormatoEbook formato) {
        if(FormatoEbook.PDF.equals(formato)) {
            return new GeradorPDF();
        } else if(FormatoEbook.EPUB.equals(formato)) {
            return new GeradorEPUB();
        } else {
            throw new IllegalArgumentException("Formato do ebook inválido: " + formato.name().toLowerCase());
        }
    }

    public abstract void gerar(Path arquivoDeSaida, Ebook ebook);


}
