package br.com.unipds;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class LeitorPropriedadesEbookArquivo implements LeitorPropriedadesEbook {

    @Override
    public void ler(Path diretorioMD, EbookBuilder ebook) {

        Path arquivoProperties = diretorioMD.resolve("ebook.properties");

        if (!Files.exists(arquivoProperties)) {
            throw new IllegalStateException("Arquivo ebook.properties não encontrado em: " + diretorioMD);
        }

        Properties properties = new Properties();
        try (Reader reader = Files.newBufferedReader(arquivoProperties, StandardCharsets.ISO_8859_1)) {
            properties.load(reader);
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new IllegalStateException("Erro ao ler arquivo: " + arquivoProperties, ex);
        }

        String propriedadeTitulo = "cotuba.ebook.titulo";
        String titulo = properties.getProperty(propriedadeTitulo);
        validarPropriedade(titulo, propriedadeTitulo);

        String propriedadeAutor = "cotuba.ebook.autor";
        String autor = properties.getProperty(propriedadeAutor);
        validarPropriedade(autor, propriedadeAutor);

        ebook.titulo(titulo);
        ebook.autor(autor);
    }

    private static void validarPropriedade(String valor, String propriedade) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("Propriedade inválida: " + propriedade);
        }
    }
}
