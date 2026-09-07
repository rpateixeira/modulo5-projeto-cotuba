/**
 * Módulo JPMS do Cotuba.
 *
 * <p>É um {@code open module} porque o CDI (Weld) faz reflexão intensa sobre os
 * beans em tempo de execução, precisando de acesso a todos os pacotes.
 *
 * <p>Observações importantes:
 * <ul>
 *   <li>Este {@code module-info} passa a valer em tempo de <b>compilação</b>
 *       (encapsulamento forte do JAR perante os demais módulos). Como a aplicação
 *       é distribuída como <i>fat jar</i> (maven-shade) e executada via
 *       {@code java -jar} no <i>classpath</i>, em runtime o descriptor fica inerte.</li>
 *   <li>Os nomes {@code html2pdf}, {@code kernel}, {@code layout} e
 *       {@code epublib.core} são <b>automatic modules</b> (derivados do nome do JAR).</li>
 * </ul>
 */
open module br.com.unipds {

    // ----- Dependências (module path) -----
    // O weld-se-shaded embute (shade) as APIs jakarta.enterprise.* e jakarta.inject.*,
    // por isso dependemos do módulo automático dele em vez de jakarta.cdi/jakarta.inject.
    requires weld.se.shaded;              // CDI SE + jakarta.enterprise.* / jakarta.inject.*

    requires org.apache.commons.cli;      // CLI
    requires org.commonmark;              // parser/render de Markdown

    requires html2pdf;                    // iText html2pdf (automatic module)
    requires kernel;                      // iText kernel   (automatic module)
    requires layout;                      // iText layout   (automatic module)

    requires epublib.core;                // geração de EPUB (automatic module)

    requires org.jmolecules.ddd;
    requires org.jmolecules.architecture.hexagonal;

    // Anotações do record-builder: só em tempo de compilação
    requires static io.soabase.recordbuilder.core;
    // @javax.annotation.processing.Generated usado no código gerado pelos builders
    requires static java.compiler;

    // ----- API exposta para os módulos de plugin (tema-css, estatisticas-ebook) -----
    exports br.com.unipds.cotuba.domain;
    exports br.com.unipds.cotuba.plugin;
    exports br.com.unipds.cotuba.adapters.out;

    // ----- Pontos de extensão carregados via ServiceLoader -----
    uses br.com.unipds.cotuba.plugin.CotubaPluginAposGeracao;
    uses br.com.unipds.cotuba.plugin.CotubaPluginAposRenderizacao;
}

