package br.com.unipds.service;

import br.com.unipds.*;
import br.com.unipds.dto.ParametrosCotubaDTO;
import br.com.unipds.support.FormatoGeradorArquivosFilter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import org.jmolecules.ddd.annotation.Service;

import java.util.List;

@Service
@ApplicationScoped
public class CotubaService {

    private final RenderizadorMarkdown renderizadorMarkdown;
    private final LeitorPropriedadesEbook leitorPropriedadesEbook;
    private final Instance<GeradorArquivos> geradorArquivos;
    @Inject
    public CotubaService(RenderizadorMarkdown renderizadorMarkdown, LeitorPropriedadesEbook leitorPropriedadesEbook, @Any Instance<GeradorArquivos> geradorArquivos) {
        this.renderizadorMarkdown = renderizadorMarkdown;
        this.leitorPropriedadesEbook = leitorPropriedadesEbook;
        this.geradorArquivos = geradorArquivos;
    }

    public void executar(ParametrosCotubaDTO parametrosCotuba) {
        List<Capitulo> renderizar = renderizadorMarkdown.renderizar(parametrosCotuba.diretorioDosMD());

        var ebook =  EbookBuilder.builder();
        ebook.capitulos(renderizar);
        ebook.formato(parametrosCotuba.formato());
        ebook.arquivoDeSaida(parametrosCotuba.arquivoDeSaida());
        leitorPropriedadesEbook.ler(parametrosCotuba.diretorioDosMD(), ebook);

        Instance<GeradorArquivos> gerador = geradorArquivos.select(FormatoGeradorArquivosFilter.of(parametrosCotuba.formato()));
        if (gerador.isUnsatisfied()) {
            throw new IllegalArgumentException("Formato do ebook inválido: " + parametrosCotuba.formato().name().toLowerCase());
        }
        gerador.get().gerar(parametrosCotuba.arquivoDeSaida(), ebook.build());
        //GeradorArquivos.getInstance(parametrosCotuba.getFormato()).gerar(parametrosCotuba.getArquivoDeSaida(), ebook);

    }
}
