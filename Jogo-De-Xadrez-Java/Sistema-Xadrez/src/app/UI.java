package app;

import xadrez.Cores;
import xadrez.PecaDeXadrez;
import xadrez.XadrezPosicao;

import java.util.InputMismatchException;
import java.util.Scanner;

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

	public static void printTabuleiro(PecaDeXadrez[][] peca){
		for(int i = 0; i < peca.length; i++) {
			System.out.print((8 - i) + " ");
			for(int j = 0; j < peca.length; j++) {
				printPeca(peca[i][j]);
			}
			System.out.println();
		}
		System.out.println("  a b c d e f g h");
	}
	private static void printPeca(PecaDeXadrez peca) {
		if (peca == null) {
			System.out.print("-");
		}
		else {
			if (peca.getCor() == Cores.WHITE) {
				System.out.print(ANSI_WHITE + peca + ANSI_RESET);
			}
			else {
				System.out.print(ANSI_YELLOW + peca + ANSI_RESET);
			}
		}
		System.out.print(" ");
	}
}
