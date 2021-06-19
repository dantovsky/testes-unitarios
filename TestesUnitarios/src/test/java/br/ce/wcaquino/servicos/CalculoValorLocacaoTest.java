package br.ce.wcaquino.servicos;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class CalculoValorLocacaoTest {

    private LocacaoService service;
    private List<Filme> filmes;
    private Double valorLocacao;

    @Before
    public void setup() {
        service = new LocacaoService(); // instancia da classe que quero testar
        // Essa instancia será aplicada antes de cada @Test
    }

    @Test
    public void deveCalcularValorLocacaoConsiderandoDescontos() throws FilmeSemEstoqueException, LocadoraException {
        // cenario
        Usuario usuario = new Usuario();

        // acao
        Locacao resultado = service.alugarFilme(usuario, filmes);

        // verificacao => 4+4+3+2+1 = 14
        assertThat(resultado.getValor(), is(valorLocacao));
    }
}
