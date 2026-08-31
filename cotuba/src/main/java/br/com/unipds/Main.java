package br.com.unipds;

import br.com.unipds.cli.LeitorOpcoes;
import br.com.unipds.service.CotubaService;
import org.apache.commons.cli.CommandLine;

public class Main {

    void main(String[] args) {
        int exitCode = executar(args);
        if (exitCode != 0) {
            System.exit(exitCode);
        }
    }

    int executar(String[] args) {


        LeitorOpcoes leitorOpcoes = new LeitorOpcoes();

        boolean modoVerboso = leitorOpcoes.isModoVerboso();

        try {

            CommandLine cmd = leitorOpcoes.ler(args);
            ParametrosCotubaDTO parametrosCotuba = leitorOpcoes.validarInput(cmd);

            modoVerboso=parametrosCotuba.isModoVerboso();
            CotubaService cotubaService = new CotubaService();
            cotubaService.executar(parametrosCotuba);

            System.out.println("Arquivo gerado com sucesso: " + parametrosCotuba.getArquivoDeSaida());
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
