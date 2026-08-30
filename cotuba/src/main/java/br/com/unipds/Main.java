package br.com.unipds;

import br.com.unipds.cli.LeitorOpcoes;
import org.apache.commons.cli.CommandLine;

import java.nio.file.Path;
import java.util.List;

public class Main {

    void main(String[] args) {
        int exitCode = executar(args);
        if (exitCode != 0) {
            System.exit(exitCode);
        }
    }

    int executar(String[] args) {


        LeitorOpcoes leitorOpcoes = new LeitorOpcoes();

        Path diretorioDosMD;
        FormatoEbook formato;
        Path arquivoDeSaida;
        boolean modoVerboso = false;

        try {

            CommandLine cmd = leitorOpcoes.ler(args);
            leitorOpcoes.validarInput(cmd);
            diretorioDosMD=leitorOpcoes.getDiretorioDosMD();
            formato=leitorOpcoes.getFormato();
            arquivoDeSaida=leitorOpcoes.getArquivoDeSaida();
            modoVerboso=leitorOpcoes.isModoVerboso();
            List<Capitulo> renderizar = RenderizadorMarkdown.renderizar(diretorioDosMD);
            var ebook = new Ebook();
            ebook.setCapitulos(renderizar);
            ebook.setFormato(formato);
            ebook.setArquivoDeSaida(arquivoDeSaida);
            ebook.setTitulo("titulo");
            ebook.setAutor("autor");
            GeradorArquivos.getInstance(formato).gerar(arquivoDeSaida, ebook);


            System.out.println("Arquivo gerado com sucesso: " + arquivoDeSaida);
            return 0;

        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            if (modoVerboso) {
                System.err.println();
                ex.printStackTrace();
            }
            return 1;
        }
    }

}
