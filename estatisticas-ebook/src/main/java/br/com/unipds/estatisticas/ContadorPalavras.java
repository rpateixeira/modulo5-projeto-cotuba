package br.com.unipds.estatisticas;

import java.util.Map;
import java.util.TreeMap;

class ContadorPalavras {

    private final TreeMap<String, Integer> mapa = new TreeMap<>();

    public void adicionarPalavras(String palavra){
        mapa.merge(palavra, 1, Integer::sum);
    }

    public Iterable<? extends Map.Entry<String, Integer>> entrySet() {
        return mapa.entrySet();
    }
}
