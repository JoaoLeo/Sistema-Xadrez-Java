package app;

import xadrez.Cor;
import xadrez.PartidaDeXadrez;
import xadrez.PecaDeXadrez;
import xadrez.XadrezPosicao;

import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class UI {

	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_BLACK = "\u001B[30m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	public static final String ANSI_BLUE = "\u001B[34m";
	public static final String ANSI_PURPLE = "\u001B[35m";
	public static final String ANSI_CYAN = "\u001B[36m";
	public static final String ANSI_WHITE = "\u001B[37m";

	public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
	public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
	public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
	public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
	public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
	public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
	public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
	public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";

	public static void limparTela(){
		System.out.print("\033[H\033[2J");
		System.out.flush();
	}
	public static XadrezPosicao lerXadrezPosicao(Scanner input){
		try {
			String s = input.nextLine();
			char coluna = s.charAt(0);
			int linha = Integer.parseInt(s.substring(1)); //Recorta a string a partir do index 1 e converte para int
			return new XadrezPosicao(coluna, linha);
		} catch (RuntimeException e){
			throw new InputMismatchException("Erro ao ler posição, valore válidos a1 - h8");
		}
	}
	public static void printPartida(PartidaDeXadrez partidaDeXadrez, List<PecaDeXadrez> capturadas){
		printTabuleiro(partidaDeXadrez.getPecas());
		System.out.println();
		printPecasCapturadas(capturadas);
		System.out.println("Turno: " + partidaDeXadrez.getTurno());
		if(!partidaDeXadrez.getCheckMate()) {
			System.out.println("Esperando o jogador: " + partidaDeXadrez.getJogadorAtual());
			if (partidaDeXadrez.getCheck())
				System.out.println("CHECK!");
		}
		else {
			System.out.println("CHECKMATE!");
			System.out.println("Vencedor: " + partidaDeXadrez.getJogadorAtual());
		}
	}

	public static void printTabuleiro(PecaDeXadrez[][] peca){
		for(int i = 0; i < peca.length; i++) {
			System.out.print((8 - i) + " ");
			for(int j = 0; j < peca.length; j++) {
				printPeca(peca[i][j], false);
			}
			System.out.println();
		}
		System.out.println("  a b c d e f g h");
	}

	public static void printTabuleiro(PecaDeXadrez[][] peca, boolean[][] movimentosPossiveis){
		for(int i = 0; i < peca.length; i++) {
			System.out.print((8 - i) + " ");
			for(int j = 0; j < peca.length; j++) {
				printPeca(peca[i][j], movimentosPossiveis[i][j]);
			}
			System.out.println();
		}
		System.out.println("  a b c d e f g h");
	}
	private static void printPeca(PecaDeXadrez peca, boolean background) {
		if(background)
			System.out.print(ANSI_BLUE_BACKGROUND);
		if (peca == null) {
			System.out.print("-" + ANSI_RESET);
		}
		else {
			if (peca.getCor() == Cor.WHITE) {
				System.out.print(ANSI_WHITE + peca + ANSI_RESET);
			}
			else {
				System.out.print(ANSI_YELLOW + peca + ANSI_RESET);
			}
		}
		System.out.print(" ");
	}
	private static void printPecasCapturadas(List<PecaDeXadrez> pecasDeXadrezCapturadas){
		List<PecaDeXadrez> pecasBrancasCapturadas = pecasDeXadrezCapturadas
				.stream()
				.filter(p -> p.getCor() == Cor.WHITE)
				.collect(Collectors.toList());
		List<PecaDeXadrez> pecasPretasCapturadas = pecasDeXadrezCapturadas
				.stream()
				.filter(p -> p.getCor() == Cor.BLACK)
				.collect(Collectors.toList());
		System.out.println("Peças capturadas: ");
		System.out.print("Brancas: ");
		System.out.print(ANSI_WHITE);
		System.out.println(Arrays.toString(pecasBrancasCapturadas.toArray()));
		System.out.print(ANSI_RESET);
		System.out.print("Pretas: ");
		System.out.print(ANSI_YELLOW);
		System.out.println(Arrays.toString(pecasPretasCapturadas.toArray()));
		System.out.println(ANSI_RESET);

	}
}
