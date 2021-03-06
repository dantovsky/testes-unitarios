package br.ce.wcaquino.servicos;

import br.ce.wcaquino.daos.LocacaoDAO;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.utils.DataUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static br.ce.wcaquino.utils.DataUtils.adicionarDias;

public class LocacaoService {

	private LocacaoDAO dao;
	private SPCService spcService;

	public Locacao alugarFilme(Usuario usuario, List<Filme> filmes) throws FilmeSemEstoqueException, LocadoraException {

		if (usuario == null) {
			throw new LocadoraException("Usuario vazio");
		}

		if (filmes == null || filmes.isEmpty()) {
			throw new LocadoraException("Filme vazio");
		}

		for (Filme filme : filmes) {
			if (filme.getEstoque() == 0) {
				throw new FilmeSemEstoqueException("Filme sem estoque");
			}
		}

		// Consultar se user está no SPC
		if (spcService.possuiNegativacao(usuario)) {
			throw new LocadoraException("Usuário Negativado");
		}

		Locacao locacao = new Locacao();
		locacao.setFilmes(filmes);
		locacao.setUsuario(usuario);
		locacao.setDataLocacao(new Date());

		// Dar desconto do 3º ao 6º filme alugado
		Double valorTotal = 0d;
		for (int i = 0; i < filmes.size(); i++) {
			Filme filme = filmes.get(i);
			Double valorFilme = filme.getPrecoLocacao();

			switch (i) {
				case 2: valorFilme = valorFilme * 0.75; break;
				case 3: valorFilme = valorFilme * 0.5; break;
				case 4: valorFilme = valorFilme * 0.25; break;
				case 5: valorFilme = valorFilme * 0d; break;
			}

			valorTotal += valorFilme;
		}
		locacao.setValor(valorTotal);

		//Entrega no dia seguinte
		Date dataEntrega = new Date();
		dataEntrega = adicionarDias(dataEntrega, 1);

		// Verificar se a data de retorno cai em um domingo
		if (DataUtils.verificarDiaSemana(dataEntrega, Calendar.SUNDAY)) {
			// Incrementa mais um dia de entrega (joga para segunda)
			dataEntrega = adicionarDias(dataEntrega, 1);
		}
		locacao.setDataRetorno(dataEntrega);

		// Salvando a locacao...
		// O método agora depende da camada de persistência para funcionar.
		// Soluções:
		// 1. Implementar a camada de persistência, porém deixaria de ser um teste unitário e passaria a ser um teste de integração.
		dao.salvar(locacao);

		return locacao;
	}

	// Fazer a injeção de dependência do DAO
	// Assim o LocacaoService estará povoado com uma estancia de DAO fake
	public void setLocacaoDAO(LocacaoDAO dao) {
		this.dao = dao;
	}

	public void setSpcService(SPCService spc) {
		spcService = spc;
	}
}
