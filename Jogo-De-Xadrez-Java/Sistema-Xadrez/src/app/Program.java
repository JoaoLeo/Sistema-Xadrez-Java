package app;

import tabuleiro.Tabuleiro;
import xadrez.PartidaDeXadrez;
import xadrez.PecaDeXadrez;
import xadrez.XadrezException;
import xadrez.XadrezPosicao;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Program {

	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		PartidaDeXadrez partidaDeXadrez = new PartidaDeXadrez();
		List<PecaDeXadrez> pecasCapturadas = new ArrayList<>();
		while (!partidaDeXadrez.getCheckMate()) {
			try {
				UI.limparTela();
				UI.printPartida(partidaDeXadrez, pecasCapturadas);
				System.out.println();
				System.out.print("Origem: ");
				XadrezPosicao origem = UI.lerXadrezPosicao(input);
				boolean[][] movimentosPossiveis = partidaDeXadrez.movimentosPossiveis(origem);
				UI.limparTela();
				UI.printTabuleiro(partidaDeXadrez.getPecas(), movimentosPossiveis);
				System.out.println();
				System.out.print("Destino: ");
				XadrezPosicao destino = UI.lerXadrezPosicao(input);
				PecaDeXadrez pecaCapturada = partidaDeXadrez.performaMovimentoDeXadrez(origem, destino);
				if (pecaCapturada != null)
					pecasCapturadas.add(pecaCapturada);
				if(partidaDeXadrez.getPromovida() != null){
					System.out.print("Digite a letra da peça que para promoção (B/C/T/Q): ");
					String tipo = input.nextLine().toUpperCase();
					while (!tipo.equals("B") && !tipo.equals("C") && !tipo.equals("T") & !tipo.equals("Q")) {
						System.out.print("Valor invalido! Digite a letra da peça que para promoção (B/C/T/Q): ");
						tipo = input.nextLine().toUpperCase();
					}
					partidaDeXadrez.trocaPecaPromovida(tipo);
				}
			} catch (XadrezException e) {
				System.out.println(e.getMessage());
				input.nextLine();
			} catch (InputMismatchException e) {
				System.out.println(e.getMessage());
				input.nextLine();
			}
		}
		UI.limparTela();
		UI.printPartida(partidaDeXadrez, pecasCapturadas);
	}
}