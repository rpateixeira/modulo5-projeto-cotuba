package br.com.unipds.cotuba.ports.out;

import br.com.unipds.cotuba.domain.Capitulo;

import java.nio.file.Path;
import java.util.List;

public interface RenderizadorMarkdown {
    List<Capitulo> renderizar(Path diretorioMD);
}
