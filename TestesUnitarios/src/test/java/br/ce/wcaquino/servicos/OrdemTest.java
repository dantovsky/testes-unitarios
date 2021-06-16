package br.ce.wcaquino.servicos;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

// Esta notação irá executar os testes por ordem alfabética dos nomes dos métodos
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class OrdemTest {

    public static int contador = 0;

    @Test
    public void inicia() {
        contador = 1;
    }

    // Se este teste executar primeiro, irá falhar.
    @Test
    public void verifica() {
        Assert.assertEquals(1, contador);
    }

    // Nota: o melhor é ter os testes sempre independentes, atendendo ao princípio FIRST.
}
