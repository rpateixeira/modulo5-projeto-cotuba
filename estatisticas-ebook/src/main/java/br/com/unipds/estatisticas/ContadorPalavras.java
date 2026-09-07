package br.com.unipds.estatisticas;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

class ContadorPalavras implements Iterable<ContadorPalavras.ContagemPalavra>{

    @Override
    public Iterator<ContagemPalavra> iterator() {
        return mapa.entrySet().stream()
                .map(entry -> new ContagemPalavra(entry.getKey(), entry.getValue()))
                .iterator();
    }

    record ContagemPalavra(String palavra, int contagem){}

    private final TreeMap<String, Integer> mapa = new TreeMap<>();

    public void adicionarPalavras(String palavra){
        mapa.merge(palavra, 1, Integer::sum);
    }

    public Iterable<? extends Map.Entry<String, Integer>> entrySet() {
        return mapa.entrySet();
    }
}
