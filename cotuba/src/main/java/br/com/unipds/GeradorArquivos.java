package br.com.unipds;

import java.nio.file.Path;

public abstract class GeradorArquivos {

    public static GeradorArquivos getInstance(String formato) {
        if("pdf".equals(formato)) {
            return new GeradorPDF();
        } else if("epub".equals(formato)) {
            return new GeradorEPUB();
        } else {
            throw new IllegalArgumentException("Formato do ebook inválido: " + formato);
        }
    }

    public abstract void gerar(Path arquivoDeSaida, Path diretorioDosMD);


}
