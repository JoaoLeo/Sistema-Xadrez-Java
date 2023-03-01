package xadrez;

import tabuleiro.Peca;
import tabuleiro.Posicao;
import tabuleiro.Tabuleiro;
import xadrez.pecas.Rei;
import xadrez.pecas.Torre;


public class PartidaDeXadrez {
	private Tabuleiro tabuleiro;
	
	public PartidaDeXadrez() {
		tabuleiro = new Tabuleiro(8,8);
		setupInicial();
	}
	
	public PecaDeXadrez[][] getPecas(){
		PecaDeXadrez[][] mat = new PecaDeXadrez[tabuleiro.getLinhas()][tabuleiro.getColunas()];
		for (int i = 0; i < tabuleiro.getLinhas(); i++) {
			for(int j=0; j<tabuleiro.getColunas();j++) {
				mat[i][j] = (PecaDeXadrez) tabuleiro.peca(i,j);
			}		
		}
		return mat;
	}
	private void colocaNovaPeca(char coluna, int linha, Peca peca){
		tabuleiro.colocarPeca(peca, new XadrezPosicao(coluna, linha).converteParaPosicao());
	}
	private void setupInicial() {
		colocaNovaPeca('b',6, new Torre(tabuleiro, Cores.WHITE));
		colocaNovaPeca('e',8, new Rei(tabuleiro, Cores.BLACK));
		colocaNovaPeca('e',1, new Rei(tabuleiro, Cores.WHITE));
	}}
