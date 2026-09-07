package br.com.unipds.cotuba.ports.out;

import br.com.unipds.cotuba.domain.EbookBuilder;

import java.nio.file.Path;

public interface LeitorPropriedadesEbook {
    void ler(Path diretorioMD, EbookBuilder ebook);
}
