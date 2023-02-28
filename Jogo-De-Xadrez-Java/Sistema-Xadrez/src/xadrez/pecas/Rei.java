package xadrez.pecas;

import tabuleiro.Tabuleiro;
import xadrez.Cores;
import xadrez.PecaDeXadrez;

public class Rei extends PecaDeXadrez {

	public Rei(Tabuleiro tabuleiro, Cores cor) {
		super(tabuleiro, cor);
	}
	
	@Override
	public String toString() {
		return "K";
	}

}
