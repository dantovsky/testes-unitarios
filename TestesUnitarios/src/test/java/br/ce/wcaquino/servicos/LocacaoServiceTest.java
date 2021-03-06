package br.ce.wcaquino.servicos;

import br.ce.wcaquino.builders.FilmeBuilder;
import br.ce.wcaquino.builders.UsuarioBuilder;
import br.ce.wcaquino.daos.LocacaoDAO;
import br.ce.wcaquino.daos.LocacaoDAOFake;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.utils.DataUtils;
import buildermaster.BuilderMaster;
import org.junit.*;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static br.ce.wcaquino.builders.FilmeBuilder.*;
import static br.ce.wcaquino.builders.UsuarioBuilder.*;
import static br.ce.wcaquino.matchers.MatchersProprios.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class LocacaoServiceTest {

    private LocacaoService service;
    private LocacaoDAO dao;
    private SPCService spc;

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

        service = new LocacaoService(); // instancia da classe que quero testar (será aplicada antes de cada @Test

        // LocacaoDAO dao = new LocacaoDAOFake();
        dao = Mockito.mock(LocacaoDAO.class);
        service.setLocacaoDAO(dao); // Injecao do DAO

        // Injecao do SPC
        spc = Mockito.mock(SPCService.class); // instancia mockada
        service.setSpcService(spc); // injecao do spc
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
        Usuario usuario = umUsuario().agora(); // new Usuario("Usuario 1");
        List<Filme> filmes = Arrays.asList(umFilme().comValor(5.0).agora()); // Arrays.asList(new Filme("Filme 1", 1, 5.0));

        // Acao :: execução metodo que quero testar
        Locacao locacao = service.alugarFilme(usuario, filmes);


        // Utilização  do Rule

        error.checkThat(locacao.getValor(), is(equalTo(5.0)));
        error.checkThat(locacao.getDataLocacao(), ehHoje()); // usando matcher próprio
        error.checkThat(locacao.getDataRetorno(), ehHojeComDiferencaDias(1)); // usando matcher próprio

        // error.checkThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
        // error.checkThat(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)), is(true));
    }

    // Existem 3 formas para se tratar uma exceção enviada

    // Forma 1: "elegante" => funciona bem quando apenas a exceção importa.
    // Precisa tentar garantir que a exceção vem apenas por um motivo.
    // Não se consegue obter a mensagem da exceção.
    @Test(expected = Exception.class)
    public void naoDeveAlugarFilmeSemEstoque() throws Exception {

        // Cenario :: inicializar tudo o que precisamos
        Usuario usuario = umUsuario().agora(); // new Usuario("Usuario 1");
        List<Filme> filmes = Arrays.asList(umFilmeSemEstoque().agora()); // Arrays.asList(new Filme("Filme 1", 0, 5.0));

        // Acao :: execução metodo que quero testar
        service.alugarFilme(usuario, filmes);
    }

    // Forma 2: "robusta" (forma completa) => permite um controle maior sobre a exec do teste, que a forma elegante não dá
    // A vantagem aqui é que o try ... catch trata o erro e o fluxo do code continua.
    // Há casos em que essa forma é necessária, como quando se usa com Mocks.
    @Test
    public void testLocacao_filmeSemEstoque2() { // ??? Não entendi!!! (aula 9)

        // Cenario :: inicializar tudo o que precisamos
        Usuario usuario = umUsuario().agora(); // new Usuario("Usuario 1");
        List<Filme> filmes = Arrays.asList(umFilme().agora()); // Arrays.asList(new Filme("Filme 1", 2, 5.0));

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
        Usuario usuario = umUsuario().agora(); // new Usuario("Usuario 1");
        List<Filme> filmes = Arrays.asList(umFilme().semEstoque().agora()); // Arrays.asList(new Filme("Filme 1", 0, 5.0));

        exception.expect(Exception.class);
        exception.expectMessage("Filme sem estoque");

        // Acao :: execução metodo que quero testar
        service.alugarFilme(usuario, filmes);

    }

    // Utilizando a forma robusta
    @Test
    public void naoDeveAlugarFilmeSemUsuario() throws FilmeSemEstoqueException {

        // cenario
        List<Filme> filmes = Arrays.asList(umFilme().agora()); // Arrays.asList(new Filme("Filme 2", 1, 4.0));

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
        Usuario usuario = umUsuario().agora(); // new Usuario("Usuario 1");

        // Checagem através da Rule
        exception.expect(LocadoraException.class);
        exception.expectMessage("Filme vazio");

        // Acao
        service.alugarFilme(usuario, null);

        System.out.println("Forma nova"); // esse log não será exibido
    }

    @Test
    public void deveDevolverNaSegundaAoAlugarNoSabado() throws FilmeSemEstoqueException, LocadoraException {

        // Checagem dinâmica: esse teste só irá correr se for sábado
        Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

        // cenario
        Usuario usuario = umUsuario().agora(); // new Usuario("Usuario 1");
        List<Filme> filmes = Arrays.asList(umFilme().agora()); // Arrays.asList(new Filme("Filme 1", 2, 4.0));

        // acao
        Locacao retorno = service.alugarFilme(usuario, filmes);

        // verificacao
        //        boolean ehSegunda = DataUtils.verificarDiaSemana(retorno.getDataRetorno(), Calendar.MONDAY);
        //        Assert.assertTrue(ehSegunda);
        // assertThat(retorno.getDataRetorno(), new DiaSemanaMatcher(Calendar.MONDAY));
        // assertThat(retorno.getDataRetorno(), caiEm(Calendar.SUNDAY));
        assertThat(retorno.getDataRetorno(), caiNumaSegunda());
    }

    // Aula 23 - Lib BuilderMaster, para automatizar a criação de builders
    public static void main(String[] args) {
        new BuilderMaster().gerarCodigoClasse(Locacao.class);
    }

    @Test
    public void naoDeveAllugarFilmeParaNegativadoSPC() throws FilmeSemEstoqueException, LocadoraException {
        // cenario
        Usuario usuario = umUsuario().agora();
        Usuario usuario2 = umUsuario().conNome("Usuario 2").agora();
        List<Filme> filmes = Arrays.asList(umFilme().agora());

        // Definir o comportamento no Mockito
        Mockito.when(spc.possuiNegativacao(usuario)).thenReturn(true);
        // quando spc possuiNegativacao passando um usuario for chamado, entao retorne true (por padrão ele retornava false)

        exception.expect(LocadoraException.class);
        exception.expectMessage("Usuário Negativado");

        // acao
        service.alugarFilme(usuario, filmes);
    }
}

