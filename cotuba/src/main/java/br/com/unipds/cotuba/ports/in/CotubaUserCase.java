package br.com.unipds.cotuba.ports.in;

import br.com.unipds.cotuba.dto.ParametrosCotubaDTO;

public interface CotubaUserCase {
    public void executar(ParametrosCotubaDTO parametrosCotuba);
}
