package br.com.unipds;

import br.com.unipds.cli.LeitorOpcoes;
import org.apache.commons.cli.CommandLine;

import java.nio.file.Path;

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
        String formato;
        Path arquivoDeSaida;
        boolean modoVerboso = false;

        try {

            CommandLine cmd = leitorOpcoes.ler(args);
            leitorOpcoes.validarInput(cmd);
            diretorioDosMD=leitorOpcoes.getDiretorioDosMD();
            formato=leitorOpcoes.getFormato();
            arquivoDeSaida=leitorOpcoes.getArquivoDeSaida();
            modoVerboso=leitorOpcoes.isModoVerboso();
            GeradorArquivos.getInstance(formato).gerar(arquivoDeSaida, diretorioDosMD);


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
