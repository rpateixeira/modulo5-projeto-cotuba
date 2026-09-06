package br.com.unipds;

import br.com.unipds.support.FormatoGeradorArquivos;
import jakarta.enterprise.context.ApplicationScoped;

import java.nio.file.Path;

@ApplicationScoped
@FormatoGeradorArquivos(FormatoEbook.HTML)
public class GeradorHTML extends GeradorArquivos {

    @Override
    public void gerar(Path arquivoDeSaida, Ebook ebook) {
        // Implementação para gerar HTML
    }
}
