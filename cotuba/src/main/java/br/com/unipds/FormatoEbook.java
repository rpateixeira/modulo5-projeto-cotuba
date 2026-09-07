package br.com.unipds;

import org.jmolecules.ddd.annotation.ValueObject;

@ValueObject
public enum FormatoEbook {
    PDF, EPUB, MOBI, HTML;
    private FormatoEbook(){}

}
