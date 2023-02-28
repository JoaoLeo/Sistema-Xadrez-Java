package app;

import tabuleiro.Tabuleiro;
import xadrez.PartidaDeXadrez;

public class Program {

	public static void main(String[] args) {
		PartidaDeXadrez partidaDeXadrez = new PartidaDeXadrez();
		UI.printTabuleiro(partidaDeXadrez.getPecas());
	}

}
