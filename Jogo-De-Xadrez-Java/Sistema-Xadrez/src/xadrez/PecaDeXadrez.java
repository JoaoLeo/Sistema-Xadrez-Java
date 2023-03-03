package xadrez;

import tabuleiro.Peca;
import tabuleiro.Posicao;
import tabuleiro.Tabuleiro;

public abstract class PecaDeXadrez extends Peca {
	private Cor cor;
	
	public PecaDeXadrez(Tabuleiro tabuleiro, Cor cor) {
		super(tabuleiro);
		this.cor = cor;
	}

	public Cor getCor() {
		return cor;
	}

	public XadrezPosicao getPosicaoXadrez(){
		return XadrezPosicao.converteParaXadrezPosicao(posicao);
	}
	protected boolean temPecaOponente(Posicao posicao){
		PecaDeXadrez p = (PecaDeXadrez)getTabuleiro().peca(posicao);
		return p != null && p.getCor() != cor;
	}
}
