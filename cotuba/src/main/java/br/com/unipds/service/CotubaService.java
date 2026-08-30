package br.com.unipds.service;

import br.com.unipds.*;

import java.nio.file.Path;
import java.util.List;

public class CotubaService {
    public void executar(ParametrosCotubaDTO parametrosCotuba) {
        List<Capitulo> renderizar = RenderizadorMarkdown.renderizar(parametrosCotuba.getDiretorioDosMD());
        var ebook = new Ebook();
        ebook.setCapitulos(renderizar);
        ebook.setFormato(parametrosCotuba.getFormato());
        ebook.setArquivoDeSaida(parametrosCotuba.getArquivoDeSaida());
        ebook.setTitulo("titulo");
        ebook.setAutor("autor");
        GeradorArquivos.getInstance(parametrosCotuba.getFormato()).gerar(parametrosCotuba.getArquivoDeSaida(), ebook);

    }
}
