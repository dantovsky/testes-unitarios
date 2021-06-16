# Testes unitários em JAVA: Domine JUnit, Mockito e TDD
https://www.udemy.com/course/testes-unitarios-em-java/

## Testes Unitários

Os testes unitários avaliam uma das menores porções de código, os métodos.

Podemos criar n testes para esse mesmo método, onde para cada teste colocamos uma entradaa distinta que representa um cenário diferente.

`Entrada + Ação = Resultado`

No contexto de Testes Unitários devemos isolar os relacionamentos externos.

A maioria dos testes podem ser divididos em 3 etapas:
1. **Cenário**: onde as variáveis serão inicializadas, onde toda a config inicial é realizada para que o cenário que desejamos testar esteja disponível. Ou seja, consiguramos o ambiente às entradas necessárias.
2. **Ação**: onde o método que queremos testar seja efetivamente executado. Como fruto dessa etapa teremos o **resultado**, que será usado na última etapa.
3. **Validação**: coletamos o resultado da execução da ação sobre o ambiente e entrada especificado, e esse resultado pode ser um:
    - objeto
    - mensagem de sucesso
    - mensagem de erro
    - alerta 
    - exceção
    - atributo alterado
    - facto de não ser lançado exceção alguma

Na fase 3 (**validação**) é conde comparamos o resultado obtido com o resultado que era esperado para aquela ação sob as condições configuradas na fase inicial.

## Testando sem Ferramenta

Projeto `TestesUnitario`, um sistema para gerenciar os alugéis de filmes de uma locadora.

Princípio `FIRST`: detalhamento sobre o que deve existir para ser um teste unitário.

- `F`ast :: deve ser executado muito rápido ("clica e executa").
- `I`ndependent / `I`solado :: um teste não deve depender de outros, podendo rodar em qualquer ordem.
- `R`epeatable :: pode ser executando quantas vezes quiser e na hora que quiser (deve funcionar e dar sempre o mesmo resultado).
- `S`elf-Verifying :: "alto-verificado", um teste deve saber quando sua execução foi correta e quando falhou.
- `T`imely :: "oportuno", um teste deve ser criado no momento correto.
 
Exemplo de código que testa sem ferramentas de teste, e segue o princípio FIRST.

```java
package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.utils.DataUtils.adicionarDias;

import java.util.Date;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.utils.DataUtils;

public class LocacaoService {
	
	public Locacao alugarFilme(Usuario usuario, Filme filme) {
		Locacao locacao = new Locacao();
		locacao.setFilme(filme);
		locacao.setUsuario(usuario);
		locacao.setDataLocacao(new Date());
		locacao.setValor(filme.getPrecoLocacao());

		//Entrega no dia seguinte
		Date dataEntrega = new Date();
		dataEntrega = adicionarDias(dataEntrega, 1);
		locacao.setDataRetorno(dataEntrega);
		
		//Salvando a locacao...	
		//TODO adicionar método para salvar
		
		return locacao;
	}

	public static void main(String[] args) {

		// Cenario :: inicializar tudo o que precisamos
		LocacaoService service = new LocacaoService(); // instancia da classe que quero testar
		Usuario usuario = new Usuario("Usuario 1");
		Filme filme = new Filme("Filme 1", 2, 5.0);

		// Acao :: execução metodo que quero testar
		Locacao locacao = service.alugarFilme(usuario, filme);

		// Validacao :: checar se o resultado da ação está de acordo com o esperado
		System.out.println(locacao.getValor() == 5.0);
		System.out.println(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()));
		System.out.println(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)));
		// DataUtils.obterDataComDiferencaDias(1) => reorna uma data futura, o param indica quantos dias à frente além da data de hoje
	}
}
```

## JUnit e o Padrão XUnit

O JUnit segue o padrão XUni.

1. `TesteRunner` :: quem vai executar os testes e coletar os resultados.
2. `TestFixture` :: conhecido como TestContext, são as preconições necessárias aos testes.
3. `TestSuites` :: onde podemos elencar os testes que devem ser executados.
4. `TestResultFotmatter` :: quem vai padronizar os resultados dos tesets.
5. `Assertions` ::  verificam o comportamento ou estado do que está sendo testando, geralmente através de uma expressão lógica. Se uma sessão não for satisfeira, o teste será parado neste momento.


## Assertivas

Todo tip primitivo possui uma representação em forma de objeto.

Ex:
int i = 5
Integer i2 = 5;

Podemos trabalhar com estas duas formas porque o java tem o conceito de outobox e unboxing,
que fica variando entre o tipo primitivo e o objeto automaticamente.
No assertEquals, esse autoboxing e unboxing não existe, então se quiser
comparar esses dois valores temos duas formas:
- passar o tipo primitivo para objeto
- passar o objeto para o tipo primitivo


AttertThat :: Verificação genérica => "verifique que"
Deixa a leitura do método mais fluida
```
// "Verifique que o valor da locação é 5"
assertThat(locacao.getValor(), is(5.0));
assertThat(locacao.getValor(), is(equalTo(5.0)));
assertThat(locacao.getValor(), is(not(6.0)));
// O JUnit já vem com alguns matchers próprios, graças ao hamcrest (listados na classe CoreMatchers)

// " Verifique que data 1, data 2, é verdadeiro
assertThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
assertThat(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)), is(true));
```
## Aula 9 - Tratamento de exceções, parte 1

### Regras:
1. não deve alugar filmes ssem estoque.
2. verificar filme que não está no estoque.

-> Questão - Diferença entre erros e falhas?

Falhas: ocorrem quando o teste é executado sem problemas, porém, alguma condição que era esperada não foi atendida (representado na forma de assertivas).

Erros: um erro acontece quando algum problema durante a execução do teste impede que o mesmo seja concluído, ou seja, quando ocorre alguma exceção não esperada e não tratada.

### Aulas 9 e 10 - Formas de tratamento de erros

Resumo das 3 formas (elegante, robusta e nova).

- Elegante: funciona bem quando apenas a exceção importa. Precisa tentar garantir que a exceção vem apenas por um motivo. Não se consegue obter a mensagem da exceção.
- Robusta (forma completa): permite um controle maior sobre a exec do teste, que a forma elegante não dá. A vantagem aqui é que o try ... catch trata o erro e o fluxo do code continua. Há casos em que essa forma é necessária, como quando se usa com Mocks.
- Nova: atende na maioria dos casos (mas há casos em que somente a forma robusta vai ajudar).

## Aula 11 - Before e After

Exemplo de um contador de @Test.

```
...
   private LocacaoService service;

    // Def do contador
    public static int countTests = 0; // Se não for static, o JUnit irá reinicializar o valor a cada @Test

    // Utilizacao das rules para que colete todos os erros e apresente todos de uma vez só

    @Rule public ErrorCollector error = new ErrorCollector();

    // Rule para a forma nova
    @Rule public ExpectedException exception = ExpectedException.none();

    // Aula 11 - Before e After
    @Before public void setup() {
        System.out.println("Before");
        service = new LocacaoService(); // instancia da classe que quero testar
        // Essa instancia será aplicada antes de cada @Test

        countTests++;
        System.out.println("countTests: " + countTests);
    }

    @After public void tearDown() {
        System.out.println("After");
    }

    // BeforeClass executa apenas uma vez antes da classe ser instanciada
    @BeforeClass public static void setupClass() {
        System.out.println("Before class");
    }

    // AfterClass executa apenas uma vez depois da classe ser instanciada
    @AfterClass public static void tearDownClass() {
        System.out.println("After class");
    }
...
```

## Andamento

Parei na aula 9 - Tratamento de exceções, parte 1
https://www.udemy.com/course/testes-unitarios-em-java/learn/lecture/6994632#overview

Minuto: 00:00

