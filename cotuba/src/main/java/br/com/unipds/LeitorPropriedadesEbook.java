package br.com.unipds;

import java.nio.file.Path;

public interface LeitorPropriedadesEbook {
    void ler(Path diretorioMD, EbookBuilder ebook);
}
