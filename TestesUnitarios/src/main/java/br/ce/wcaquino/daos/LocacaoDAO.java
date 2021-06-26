package br.ce.wcaquino.daos;

import br.ce.wcaquino.entidades.Locacao;

/**
 * DAO :: Data Access Object
 * In computer software, a data access object (DAO) is a pattern that provides
 * an abstract interface to some type of database or other persistence mechanism.
 */
public interface LocacaoDAO {

    public void salvar(Locacao locacao);
}
