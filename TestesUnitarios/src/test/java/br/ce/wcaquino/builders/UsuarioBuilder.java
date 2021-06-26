package br.ce.wcaquino.builders;

import br.ce.wcaquino.entidades.Usuario;

public class UsuarioBuilder {

    private Usuario usuario;

    // Construtor privado :: para que nao se possa criar instancias do builder externamente ao proprio builder
    private UsuarioBuilder() {}

    // Metodo public static para que ele possa ser acessado externamente sem a necessidade de uma instancia,
    // ou seja, esse metodo sera a porta de entrada para a criacao de um usuario
    public static UsuarioBuilder umUsuario() {
        UsuarioBuilder builder = new UsuarioBuilder();
        builder.usuario = new Usuario();
        builder.usuario.setNome("Usuario 1");
        return builder;
    }

    public Usuario agora() {
        return usuario;
    }
}
