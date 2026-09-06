package br.com.unipds.service;

import br.com.unipds.*;
import br.com.unipds.dto.ParametrosCotubaDTO;
import br.com.unipds.support.FormatoGeradorArquivosFilter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;

import java.util.List;

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
        List<Capitulo> renderizar = renderizadorMarkdown.renderizar(parametrosCotuba.getDiretorioDosMD());

        var ebook = new Ebook();
        ebook.setCapitulos(renderizar);
        ebook.setFormato(parametrosCotuba.getFormato());
        ebook.setArquivoDeSaida(parametrosCotuba.getArquivoDeSaida());
        leitorPropriedadesEbook.ler(parametrosCotuba.getDiretorioDosMD(), ebook);

        Instance<GeradorArquivos> gerador = geradorArquivos.select(FormatoGeradorArquivosFilter.of(parametrosCotuba.getFormato()));
        if (gerador.isUnsatisfied()) {
            throw new IllegalArgumentException("Formato do ebook inválido: " + parametrosCotuba.getFormato().name().toLowerCase());
        }
        gerador.get().gerar(parametrosCotuba.getArquivoDeSaida(), ebook);
        //GeradorArquivos.getInstance(parametrosCotuba.getFormato()).gerar(parametrosCotuba.getArquivoDeSaida(), ebook);

    }
}
