package br.com.unipds.repository;

import br.com.unipds.cotuba.domain.Markdown;
import org.jmolecules.ddd.annotation.Repository;
import java.nio.file.Path;
import java.util.List;

@Repository
public interface RepositorioMarkdowns {
    List<Markdown> buscar(Path diretorioMD);
}
