package br.com.unipds;

import java.nio.file.Path;
import java.util.List;

public interface RenderizadorMarkdown {
    List<Capitulo> renderizar(Path diretorioMD);
}
