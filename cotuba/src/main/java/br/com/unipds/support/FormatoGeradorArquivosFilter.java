package br.com.unipds.support;

import br.com.unipds.FormatoEbook;
import jakarta.enterprise.util.AnnotationLiteral;

public class FormatoGeradorArquivosFilter extends AnnotationLiteral<FormatoGeradorArquivos> implements FormatoGeradorArquivos {
    private final FormatoEbook value;

    private FormatoGeradorArquivosFilter(FormatoEbook value) {
        this.value = value;
    }

    public static FormatoGeradorArquivosFilter of(FormatoEbook value) {
        return new FormatoGeradorArquivosFilter(value);
    }

    @Override
    public FormatoEbook value() {
        return value;
    }
}
