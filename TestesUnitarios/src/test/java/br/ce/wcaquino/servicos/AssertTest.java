package br.ce.wcaquino.servicos;

import br.ce.wcaquino.entidades.Usuario;
import org.junit.Assert;
import org.junit.Test;

public class AssertTest {

    @Test
    public void test() {
        Assert.assertTrue(true);
        Assert.assertFalse(false);

        // asssertEquals verifica se um valor é igual ao outro
        // porém vai tratar cada tipo de uma forma diferente.
        Assert.assertEquals(1, 1); // 1º param: esperado, 2º param: atual
        Assert.assertEquals("Erro de comparação :: ", 1, 1); // as assertivas também recebem uma string (message) como 1º param,
        Assert.assertEquals(0.51234, 0.512, 0.001); // 3º param é o delta: uma margem de erro, que são as cassas decimais a analisar
        Assert.assertEquals(Math.PI, 3.14, 0.01);

        // Autobox e unbox (conversão entre tipos primitivos e a sua representação em objeto

        int i = 5;
        Integer i2 = 5;

        // No assertEquals, esse autoboxing e unboxing não existe, então se quiser
        // comparar esses dois valores temos duas formas:

        // - passar o tipo primitivo para objeto
        Assert.assertEquals(Integer.valueOf(i), i2);

        // - passar o objeto para o tipo primitivo
        Assert.assertEquals(i, i2.intValue());

        // Comparação de strings
        Assert.assertEquals("bola", "bola");
        Assert.assertNotEquals("bola", "casa");
        Assert.assertTrue("bola".equalsIgnoreCase("Bola"));
        Assert.assertTrue("bola".startsWith("bo"));

        // Comparação de Objetos
        Usuario u1 = new Usuario("Usuario 1");
        Usuario u2 = new Usuario("Usuario 1");
        Usuario u3 = u2;
        Usuario u4 = null;

        Assert.assertEquals(u1, u2); // tem o mesmo nome
        Assert.assertSame(u3, u2); // são a mesma instancia
        Assert.assertNotSame(u1, u2); // apesar de serem iguais, eles são de instancias diferentes
        Assert.assertTrue(u4 == null); // é um objeto nulo
        Assert.assertNull(u4); // é um objeto nulo
        Assert.assertNotNull(u2); // é um objeto que não está vazio (não nulo)
    }

}
