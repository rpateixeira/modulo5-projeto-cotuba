package br.com.unipds.support;

import br.com.unipds.cotuba.domain.FormatoEbook;
import jakarta.inject.Qualifier;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Qualifier
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target(java.lang.annotation.ElementType.TYPE)
public @interface FormatoGeradorArquivos {
    FormatoEbook value();
}
