package br.com.unipds;

import org.jmolecules.ddd.annotation.Entity;
import org.jmolecules.ddd.annotation.Identity;

import java.nio.file.Path;

@Entity
public record Markdown(String conteudo, @Identity String nome) {
}
