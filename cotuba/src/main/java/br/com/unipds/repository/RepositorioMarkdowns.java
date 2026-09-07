package br.com.unipds.repository;

import br.com.unipds.Markdown;

import java.nio.file.Path;
import java.util.List;

public interface RepositorioMarkdowns {
    List<Markdown> buscar(Path diretorioMD);
}
