package br.ce.wcaquino.suites;

// Informaro ao JUnit que a execucao será diferente

import br.ce.wcaquino.servicos.CalculadoraTest;
import br.ce.wcaquino.servicos.CalculoValorLocacaoTest;
import br.ce.wcaquino.servicos.LocacaoServiceTest;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Não é muito comum usar suites de testes, pois podemos fazer run do source test/java,
 * executando assim todos os testes contidos nela. Em uma ferramenta de integração contínua (CI/CD),
 * é procurado por todos os testes no source, não sendo necessária a suite criada, mas se existir,
 * esta será também executada, então alguns testes irão correr duas vezes.
 */

@RunWith(Suite.class)
@Suite.SuiteClasses({
    CalculadoraTest.class,
    CalculoValorLocacaoTest.class,
    LocacaoServiceTest.class
})
public class SuiteExecucao {
    // O Java obriga ter uma definicao de classe. Então, remova se puder!

    @BeforeClass
    public static void before() {
        System.out.println("before SuiteExecucao");
    }

    @AfterClass
    public static void after() {
        System.out.println("after SuiteExecucao");
    }
}
