package br.com.unipds;

import io.soabase.recordbuilder.core.RecordBuilder;

import javax.management.StringValueExp;
import java.nio.file.Path;
@RecordBuilder
public record Capitulo (String titulo ,
      String conteudo ,
      String numero ,
      Markdown markdown ,
      String html ,
      Path arquivoMarkdown){


}
