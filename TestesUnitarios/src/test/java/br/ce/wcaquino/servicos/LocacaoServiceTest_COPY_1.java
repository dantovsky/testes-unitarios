package br.ce.wcaquino.servicos;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.utils.DataUtils;
import org.junit.*;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static br.ce.wcaquino.utils.DataUtils.isMesmaData;
import static br.ce.wcaquino.utils.DataUtils.obterDataComDiferencaDias;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@Ignore // Ignora a execução desta classe de testes
public class LocacaoServiceTest_COPY_1 {

    private LocacaoService service;

    // Utilizacao das rules para que colete todos os erros e apresente todos de uma vez só

    @Rule
    public ErrorCollector error = new ErrorCollector();

    // Rule para a forma nova
    @Rule
    public ExpectedException exception = ExpectedException.none();

    // Aula 11 - Before e After
    @Before
    public void setup() {
        System.out.println("Before");
        service = new LocacaoService(); // instancia da classe que quero testar
        // Essa instancia será aplicada antes de cada @Test
    }

    @After
    public void tearDown() {
        System.out.println("After");
    }

    // BeforeClass executa apenas uma vez antes da classe ser instanciada
    @BeforeClass
    public static void setupClass() {
        System.out.println("Before class");
    }

    // AfterClass executa apenas uma vez depois da classe ser instanciada
    @AfterClass
    public static void tearDownClass() {
        System.out.println("After class");
    }

    @Test
    public void deveAlugarFilme() throws Exception {

        // Checagem dinâmica: esse teste só irá correr se não for sábado
        Assume.assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

        // public static void main(String[] args) {

        // Cenario :: inicializar tudo o que precisamos
        Usuario usuario = new Usuario("Usuario 1");
        List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 1, 5.0));

        // Acao :: execução metodo que quero testar
        Locacao locacao = service.alugarFilme(usuario, filmes);

        // Sem utilização do Rule

        /*
        // Validacao :: checar se o resultado da ação está de acordo com o esperado
        assertEquals(5.0, locacao.getValor(), 0.01); // 3º param => delta :: margem de erro de 0.01 para que não passe nem um centavo errado
        assertTrue(isMesmaData(locacao.getDataLocacao(), new Date()));
        assertTrue(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)));
        // DataUtils.obterDataComDiferencaDias(1) => retorna uma data futura, o param indica quantos dias à frente além da data de hoje

        // --- AttertThat :: Verificação genérica => "verifique que"
        // Deixa a leitura do método mais fluida

        // "Verifique que o valor da locação é 5"
        assertThat(locacao.getValor(), is(5.0)); // o valor esperado é o 2º param
        assertThat(locacao.getValor(), is(equalTo(5.0)));
        assertThat(locacao.getValor(), is(not(6.0)));
        // O JUnit já vem com alguns matchers próprios, graças ao hamcrest (listados na classe CoreMatchers)

        // " Verifique que data 1, data 2, é verdadeiro
        assertThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
        assertThat(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)), is(true));
        */

        // Com utilização  do Rule

        error.checkThat(locacao.getValor(), is(equalTo(5.0)));
        error.checkThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
        error.checkThat(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)), is(true));
    }

    // Existem 3 formas para se tratar uma exceção enviada

    // Forma 1: "elegante" => funciona bem quando apenas a exceção importa.
    // Precisa tentar garantir que a exceção vem apenas por um motivo.
    // Não se consegue obter a mensagem da exceção.
    @Test(expected = Exception.class)
    public void naoDeveAlugarFilmeSemEstoque() throws Exception {

        // Cenario :: inicializar tudo o que precisamos
        Usuario usuario = new Usuario("Usuario 1");
        List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 0, 5.0));

        // Acao :: execução metodo que quero testar
        service.alugarFilme(usuario, filmes);
    }

    // Forma 2: "robusta" (forma completa) => permite um controle maior sobre a exec do teste, que a forma elegante não dá
    // A vantagem aqui é que o try ... catch trata o erro e o fluxo do code continua.
    // Há casos em que essa forma é necessária, como quando se usa com Mocks.
    @Test
    public void testLocacao_filmeSemEstoque2() { // ??? Não entendi!!! (aula 9)

        // Cenario :: inicializar tudo o que precisamos
        Usuario usuario = new Usuario("Usuario 1");
        List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 2, 5.0));

        // Acao :: execução metodo que quero testar
        try {
            service.alugarFilme(usuario, filmes);
            // Assert.fail("Deveria ter lançado uma exceção");
        } catch (Exception e) {
            // Alem de capturar a exceção, podemos também verificar a mensagem que vem da exceção
            Assert.assertThat(e.getMessage(), is("Filme sem estoque"));
        }
    }

    // Forma 3: "forma nova" => atende na maioria dos casos (mas há casos em que somente a forma robusta vai ajudar)
    @Test
    public void testLocacao_filmeSemEstoque3() throws Exception {

        // Cenario :: inicializar tudo o que precisamos
        Usuario usuario = new Usuario("Usuario 1");
        List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 0, 5.0));

        exception.expect(Exception.class);
        exception.expectMessage("Filme sem estoque");

        // Acao :: execução metodo que quero testar
        service.alugarFilme(usuario, filmes);

    }

    // Utilizando a forma robusta
    @Test
    public void naoDeveAlugarFilmeSemUsuario() throws FilmeSemEstoqueException {

        // cenario
        List<Filme> filmes = Arrays.asList(new Filme("Filme 2", 1, 4.0));

        // acao
        try {
            service.alugarFilme(null, filmes);
            Assert.fail(); // porque está a suar a forma robusta
        } catch (LocadoraException e) {
            assertThat(e.getMessage(), is("Usuario vazio"));
        }

        System.out.println("Forma robusta"); // esse log será exibido
    }

    // Utilizando a forma nova
    @Test
    public void naoDeveAlugarFilmeSemFilme() throws FilmeSemEstoqueException, LocadoraException {

        // Cenario
        Usuario usuario = new Usuario("Usuario 1");

        // Checagem através da Rule
        exception.expect(LocadoraException.class);
        exception.expectMessage("Filme vazio");

        // Acao
        service.alugarFilme(usuario, null);

        System.out.println("Forma nova"); // esse log não será exibido
    }

    // Aula 15 - Aplicando TDD
    @Test
    public void devePagar75PctNoFilme3() throws FilmeSemEstoqueException, LocadoraException {
        // cenario
        Usuario usuario = new Usuario();
        List<Filme> filmes = Arrays.asList(
                new Filme("Filme 1", 2, 4.0),
                new Filme("Filme 2", 2, 4.0),
                new Filme("Filme 3", 2, 4.0)
        );

        // acao
        Locacao resultado = service.alugarFilme(usuario, filmes);

        // verificacao => 4+4+3 = 11
        assertThat(resultado.getValor(), is(11.0));
    }

    @Test
    public void devePagar50PctNoFilme4() throws FilmeSemEstoqueException, LocadoraException {
        // cenario
        Usuario usuario = new Usuario();
        List<Filme> filmes = Arrays.asList(
                new Filme("Filme 1", 2, 4.0),
                new Filme("Filme 2", 2, 4.0),
                new Filme("Filme 3", 2, 4.0),
                new Filme("Filme 4", 2, 4.0)
        );

        // acao
        Locacao resultado = service.alugarFilme(usuario, filmes);

        // verificacao => 4+4+3+2 = 13
        assertThat(resultado.getValor(), is(13.0));
    }

    @Test
    public void devePagar25PctNoFilme5() throws FilmeSemEstoqueException, LocadoraException {
        // cenario
        Usuario usuario = new Usuario();
        List<Filme> filmes = Arrays.asList(
                new Filme("Filme 1", 2, 4.0),
                new Filme("Filme 2", 2, 4.0),
                new Filme("Filme 3", 2, 4.0),
                new Filme("Filme 4", 2, 4.0),
                new Filme("Filme 5", 2, 4.0)
        );

        // acao
        Locacao resultado = service.alugarFilme(usuario, filmes);

        // verificacao => 4+4+3+2+1 = 14
        assertThat(resultado.getValor(), is(14.0));
    }

    @Test
    public void devePagar0PctNoFilme6() throws FilmeSemEstoqueException, LocadoraException {
        // cenario
        Usuario usuario = new Usuario();
        List<Filme> filmes = Arrays.asList(
                new Filme("Filme 1", 2, 4.0),
                new Filme("Filme 2", 2, 4.0),
                new Filme("Filme 3", 2, 4.0),
                new Filme("Filme 4", 2, 4.0),
                new Filme("Filme 5", 2, 4.0),
                new Filme("Filme 6", 2, 4.0)
        );

        // acao
        Locacao resultado = service.alugarFilme(usuario, filmes);

        // verificacao => 4+4+3+2+1+0 = 14
        assertThat(resultado.getValor(), is(14.0));
    }

    @Test
    public void deveDevolverNaSegundaAoAlugarNoSabado() throws FilmeSemEstoqueException, LocadoraException {

        // Checagem dinâmica: esse teste só irá correr se for sábado
        Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

        // cenario
        Usuario usuario = new Usuario();
        List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 2, 4.0));

        // acao
        Locacao retorno = service.alugarFilme(usuario, filmes);

        // verificacao
        boolean ehSegunda = DataUtils.verificarDiaSemana(retorno.getDataRetorno(), Calendar.MONDAY);
        Assert.assertTrue(ehSegunda);
    }
}
