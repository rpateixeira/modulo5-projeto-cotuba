package br.com.unipds.cotuba.cli;

import br.com.unipds.cotuba.dto.ParametrosCotubaDTO;
import br.com.unipds.service.CotubaService;
import jakarta.enterprise.inject.se.SeContainer;
import jakarta.enterprise.inject.se.SeContainerInitializer;
import org.apache.commons.cli.CommandLine;

public class Main {

    void main(String[] args) {
        int exitCode = executar(args);
        if (exitCode != 0) {
            System.exit(exitCode);
        }
    }

    public int executar(String[] args) {


        LeitorOpcoes leitorOpcoes = new LeitorOpcoes();

        boolean modoVerboso = leitorOpcoes.isModoVerboso();
        SeContainerInitializer initializer = SeContainerInitializer.newInstance();
        try (SeContainer container = initializer.initialize()) {

            CommandLine cmd = leitorOpcoes.ler(args);
            ParametrosCotubaDTO parametrosCotuba = leitorOpcoes.validarInput(cmd);

            modoVerboso=parametrosCotuba.modoVerboso();
            CotubaService cotubaService = container.select(CotubaService.class).get();
            cotubaService.executar(parametrosCotuba);

            System.out.println("Arquivo gerado com sucesso: " + parametrosCotuba.arquivoDeSaida());
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
