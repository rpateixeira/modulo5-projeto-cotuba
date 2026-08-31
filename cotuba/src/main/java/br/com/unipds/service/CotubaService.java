package br.com.unipds.service;

import br.com.unipds.*;

import java.nio.file.Path;
import java.util.List;

public class CotubaService {
    public void executar(ParametrosCotubaDTO parametrosCotuba) {
        List<Capitulo> renderizar = RenderizadorMarkdown.renderizar(parametrosCotuba.getDiretorioDosMD());
        var ebook = new Ebook();
        LeitorPropriedadesEbook leitorPropriedadesEbook = new LeitorPropriedadesEbook();
        ebook.setCapitulos(renderizar);
        ebook.setFormato(parametrosCotuba.getFormato());
        ebook.setArquivoDeSaida(parametrosCotuba.getArquivoDeSaida());
        leitorPropriedadesEbook.ler(parametrosCotuba.getDiretorioDosMD(), ebook);
        GeradorArquivos.getInstance(parametrosCotuba.getFormato()).gerar(parametrosCotuba.getArquivoDeSaida(), ebook);

    }
}
