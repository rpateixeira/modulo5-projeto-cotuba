package br.com.unipds;

import io.soabase.recordbuilder.core.RecordBuilder;
import org.jmolecules.ddd.annotation.Entity;
import org.jmolecules.ddd.annotation.Identity;

import javax.management.StringValueExp;
import java.nio.file.Path;

@Entity
@RecordBuilder
public record Capitulo (
        @Identity
        String titulo ,
      String conteudo ,
      String numero ,
      Markdown markdown ,
      String html ){


}
