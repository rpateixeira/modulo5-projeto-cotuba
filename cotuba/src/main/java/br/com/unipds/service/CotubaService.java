package br.com.unipds.service;

import br.com.unipds.*;
import br.com.unipds.dto.ParametrosCotubaDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class CotubaService {

    private final RenderizadorMarkdown renderizadorMarkdown;
    private final LeitorPropriedadesEbook leitorPropriedadesEbook;

    @Inject
    public CotubaService(RenderizadorMarkdown renderizadorMarkdown, LeitorPropriedadesEbook leitorPropriedadesEbook) {
        this.renderizadorMarkdown = renderizadorMarkdown;
        this.leitorPropriedadesEbook = leitorPropriedadesEbook;
    }

    public void executar(ParametrosCotubaDTO parametrosCotuba) {
        List<Capitulo> renderizar = renderizadorMarkdown.renderizar(parametrosCotuba.getDiretorioDosMD());

        var ebook = new Ebook();
        ebook.setCapitulos(renderizar);
        ebook.setFormato(parametrosCotuba.getFormato());
        ebook.setArquivoDeSaida(parametrosCotuba.getArquivoDeSaida());
        leitorPropriedadesEbook.ler(parametrosCotuba.getDiretorioDosMD(), ebook);
        GeradorArquivos.getInstance(parametrosCotuba.getFormato()).gerar(parametrosCotuba.getArquivoDeSaida(), ebook);

    }
}
