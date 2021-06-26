package br.ce.wcaquino.servicos;

import br.ce.wcaquino.entidades.Usuario;

/**
 * Regra de negócio: "não deve alugar filme para caloteiro" (quem tem o nome no SPC).
 */
public interface SPCService {

    public boolean possuiNegativacao(Usuario usuario);
}
