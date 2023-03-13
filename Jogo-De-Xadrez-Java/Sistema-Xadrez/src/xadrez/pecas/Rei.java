package xadrez.pecas;

import tabuleiro.Posicao;
import tabuleiro.Tabuleiro;
import xadrez.Cor;
import xadrez.PartidaDeXadrez;
import xadrez.PecaDeXadrez;

public class Rei extends PecaDeXadrez {

	private PartidaDeXadrez partidaDeXadrez;
	public Rei(Tabuleiro tabuleiro, Cor cor, PartidaDeXadrez partidaDeXadrez) {
		super(tabuleiro, cor);
		this.partidaDeXadrez = partidaDeXadrez;
	}
	
	@Override
	public String toString() {
		return "K";
	}

	private  boolean podeSeMover(Posicao posicao){
		PecaDeXadrez p = (PecaDeXadrez)getTabuleiro().peca(posicao);
		return p == null || p.getCor() != getCor();
 	}

	 private boolean testTorreRoque(Posicao posicao){
		PecaDeXadrez p = (PecaDeXadrez)getTabuleiro().peca(posicao);
		return  p != null && p instanceof Torre && p.getCor() == getCor() && p.getContadorDeMovimento() ==0;
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

		// Jogada Roque
		if(getContadorDeMovimento() == 0 && !partidaDeXadrez.getCheck()){
			// Roque pequeno
			Posicao torre1Posicao = new Posicao(posicao.getLinha(), posicao.getColuna() + 3);
			if(testTorreRoque(torre1Posicao)){
				Posicao p1 = new Posicao(posicao.getLinha(), posicao.getColuna() + 1);
				Posicao p2 = new Posicao(posicao.getLinha(), posicao.getColuna() + 2);
				if(getTabuleiro().peca(p1) == null && getTabuleiro().peca(p2) == null){
					mat[posicao.getLinha()][posicao.getColuna() + 2] = true;
				}
			}
			// Roque grande
			Posicao torre2Posicao = new Posicao(posicao.getLinha(), posicao.getColuna() - 4);
			if(testTorreRoque(torre2Posicao)){
				Posicao p3 = new Posicao(posicao.getLinha(), posicao.getColuna() - 1);
				Posicao p4 = new Posicao(posicao.getLinha(), posicao.getColuna() - 2);
				Posicao p5 = new Posicao(posicao.getLinha(), posicao.getColuna() - 3);
				if(getTabuleiro().peca(p3) == null && getTabuleiro().peca(p4) == null && getTabuleiro().peca(p5) == null){
					mat[posicao.getLinha()][posicao.getColuna() - 2] = true;
				}
			}
		}
		return mat;
	}
}
