package br.ce.wcaquino.servicos;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import javax.crypto.spec.PSource;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * A estrutura desse teste é mais complexa, porém, a dinamicidade que ele traz compensa o esforço.
 * Data Driven Test (DDT) são muito comuns hoje em dia, e essa é a forma de se trabalhar com eles,
 * que apresenta os melhores resultados.
 */

// Como essa execucao será diferente de uma execucao padrão do JUnit, vamos alterar o Test Runner

@RunWith(Parameterized.class)
public class CalculoValorLocacaoTest {

    private LocacaoService service;

    @Parameterized.Parameter // 1º parametro do array
    public List<Filme> filmes;

    @Parameterized.Parameter(value = 1) // 2º parametro do array
    public Double valorLocacao;

    @Parameterized.Parameter(value = 2) // 3º parametro do array
    public String cenario;

    @Before
    public void setup() {
        service = new LocacaoService(); // instancia da classe que quero testar
        // Essa instancia será aplicada antes de cada @Test
    }

    private static Filme filme1 = new Filme("Filme 1", 2, 4.0);
    private static Filme filme2 = new Filme("Filme 2", 2, 4.0);
    private static Filme filme3 = new Filme("Filme 3", 2, 4.0);
    private static Filme filme4 = new Filme("Filme 4", 2, 4.0);
    private static Filme filme5 = new Filme("Filme 5", 2, 4.0);
    private static Filme filme6 = new Filme("Filme 6", 2, 4.0);
    private static Filme filme7 = new Filme("Filme 7", 2, 4.0);

    // Definir o conjunto de dados que será testado
    // Informar ao JUnit que esta é a fonte de dados
    @Parameterized.Parameters(name = "{2}")
    public static Collection<Object[]> getParametros() {
        return Arrays.asList(new Object[][] {
                {Arrays.asList(filme1, filme2), 8.0, "2 Filmes: Sem Desconto"},
                {Arrays.asList(filme1, filme2, filme3), 11.0, "3 Filmes: 25%"},
                {Arrays.asList(filme1, filme2, filme3, filme4), 13.0, "4 Filmes: 50%"},
                {Arrays.asList(filme1, filme2, filme3, filme4, filme5), 14.0, "5 Filmes:75%"},
                {Arrays.asList(filme1, filme2, filme3, filme4, filme5, filme6), 14.0, "6 Filmes: 100%"},
                {Arrays.asList(filme1, filme2, filme3, filme4, filme5, filme6, filme7), 18.0, "7 Filmes: Sem Desconto"}
        });
    }

    @Test
    public void deveCalcularValorLocacaoConsiderandoDescontos() throws FilmeSemEstoqueException, LocadoraException {
        // cenario
        Usuario usuario = new Usuario();

        // acao
        Locacao resultado = service.alugarFilme(usuario, filmes);

        // verificacao => Edx calculo para 5 filmes: 4+4+3+2+1 = 14
        assertThat(resultado.getValor(), is(valorLocacao));

        System.out.println("!");
    }

    @Test
    public void print() {
        System.out.println(valorLocacao);
    }
}
