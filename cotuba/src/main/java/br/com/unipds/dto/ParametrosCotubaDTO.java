package br.com.unipds.dto;

import br.com.unipds.FormatoEbook;

import java.nio.file.Path;

public record ParametrosCotubaDTO(
    Path diretorioDosMD,
    FormatoEbook formato,
    Path arquivoDeSaida,
    boolean modoVerboso
) {
    ParametrosCotubaDTO (Path diretorioDosMD, FormatoEbook formato, Path arquivoDeSaida) {
        this(diretorioDosMD, formato, arquivoDeSaida, false);
    }
}

