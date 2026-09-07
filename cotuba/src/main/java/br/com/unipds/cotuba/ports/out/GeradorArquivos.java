package br.com.unipds.cotuba.ports.out;

import br.com.unipds.cotuba.domain.Ebook;

import java.nio.file.Path;

public abstract class GeradorArquivos {



    public abstract void gerar(Path arquivoDeSaida, Ebook ebook);


}
