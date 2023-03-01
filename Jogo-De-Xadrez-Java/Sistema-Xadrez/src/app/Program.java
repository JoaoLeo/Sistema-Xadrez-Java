package app;

import tabuleiro.Tabuleiro;
import xadrez.PartidaDeXadrez;
import xadrez.PecaDeXadrez;
import xadrez.XadrezPosicao;

import java.util.Scanner;

public class Program {

	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		PartidaDeXadrez partidaDeXadrez = new PartidaDeXadrez();
		while (true){
			UI.printTabuleiro(partidaDeXadrez.getPecas());
			System.out.println();
			System.out.print("Origem: ");
			XadrezPosicao origem = UI.lerXadrezPosicao(input);

			System.out.println();
			System.out.print("Destino: ");
			XadrezPosicao destino = UI.lerXadrezPosicao(input);
			PecaDeXadrez pecaCapturada = partidaDeXadrez.performaMovimentoDeXadrez(origem,destino);
		}
	}
}
