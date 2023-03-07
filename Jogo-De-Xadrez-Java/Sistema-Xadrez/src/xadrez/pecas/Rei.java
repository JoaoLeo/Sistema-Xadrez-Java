package xadrez.pecas;

import tabuleiro.Posicao;
import tabuleiro.Tabuleiro;
import xadrez.Cor;
import xadrez.PecaDeXadrez;

public class Rei extends PecaDeXadrez {

	public Rei(Tabuleiro tabuleiro, Cor cor) {
		super(tabuleiro, cor);
	}
	
	@Override
	public String toString() {
		return "K";
	}

	private  boolean podeSeMover(Posicao posicao){
		PecaDeXadrez p = (PecaDeXadrez)getTabuleiro().peca(posicao);
		return p == null || p.getCor() != getCor();
 	}
	@Override
	public boolean[][] movimentosPossiveis() {
		boolean[][] mat =
				new boolean[getTabuleiro().getLinhas()]
				[getTabuleiro().getColunas()];
		Posicao p = new Posicao(0,0);
		// Vertical - para cima
		p.setValores(posicao.getLinha() - 1, posicao.getColuna());
		if(getTabuleiro().posicaoExiste(p) && podeSeMover(p))
			mat[p.getLinha()][p.getColuna()] = true;

		//Vertical - para baixo
		p.setValores(posicao.getLinha() + 1, posicao.getColuna());
		if(getTabuleiro().posicaoExiste(p) && podeSeMover(p))
			mat[p.getLinha()][p.getColuna()] = true;

		//Horizontal - para esquerda
		p.setValores(posicao.getLinha() , posicao.getColuna() - 1);
		if(getTabuleiro().posicaoExiste(p) && podeSeMover(p))
			mat[p.getLinha()][p.getColuna()] = true;

		//Horizontal - para direita
		p.setValores(posicao.getLinha() , posicao.getColuna() + 1);
		if(getTabuleiro().posicaoExiste(p) && podeSeMover(p))
			mat[p.getLinha()][p.getColuna()] = true;

		//Diagonais
		p.setValores(posicao.getLinha() - 1 , posicao.getColuna() - 1);
		if(getTabuleiro().posicaoExiste(p) && podeSeMover(p))
			mat[p.getLinha()][p.getColuna()] = true;

		p.setValores(posicao.getLinha() - 1 , posicao.getColuna() + 1);
		if(getTabuleiro().posicaoExiste(p) && podeSeMover(p))
			mat[p.getLinha()][p.getColuna()] = true;

		p.setValores(posicao.getLinha() + 1 , posicao.getColuna() - 1);
		if(getTabuleiro().posicaoExiste(p) && podeSeMover(p))
			mat[p.getLinha()][p.getColuna()] = true;

		p.setValores(posicao.getLinha() + 1 , posicao.getColuna() + 1);
		if(getTabuleiro().posicaoExiste(p) && podeSeMover(p))
			mat[p.getLinha()][p.getColuna()] = true;

		return mat;
	}
}
