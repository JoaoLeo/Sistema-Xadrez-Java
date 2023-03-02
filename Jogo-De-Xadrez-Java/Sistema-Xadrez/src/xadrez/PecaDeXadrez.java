package xadrez;

import tabuleiro.Peca;
import tabuleiro.Tabuleiro;

public abstract class PecaDeXadrez extends Peca {
	private Cores cor;
	
	public PecaDeXadrez(Tabuleiro tabuleiro,Cores cor) {
		super(tabuleiro);
		this.cor = cor;
	}

	public Cores getCor() {
		return cor;
	}

}
