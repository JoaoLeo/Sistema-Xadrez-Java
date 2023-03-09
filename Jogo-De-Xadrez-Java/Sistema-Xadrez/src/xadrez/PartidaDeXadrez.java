package xadrez;

import tabuleiro.Peca;
import tabuleiro.Posicao;
import tabuleiro.Tabuleiro;
import xadrez.pecas.Peao;
import xadrez.pecas.Rei;
import xadrez.pecas.Torre;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class PartidaDeXadrez {
	private int turno;
	private Cor jogadorAtual;
	private Tabuleiro tabuleiro;
	private List<Peca> pecasNoTabuleiro = new ArrayList<>();
	private List<Peca> pecasCapturadas = new ArrayList<>();
	private boolean check;
	private boolean checkMate;
	public PartidaDeXadrez() {
		tabuleiro = new Tabuleiro(8,8);
		turno = 1;
		jogadorAtual = Cor.WHITE;
		setupInicial();
	}

	public int getTurno() {
		return turno;
	}
	public Cor getJogadorAtual() {
		return jogadorAtual;
	}

	public boolean getCheck(){
		return check;
	}
	public boolean getCheckMate() { return checkMate; }
	public PecaDeXadrez[][] getPecas(){
		PecaDeXadrez[][] mat = new PecaDeXadrez[tabuleiro.getLinhas()][tabuleiro.getColunas()];
		for (int i = 0; i < tabuleiro.getLinhas(); i++) {
			for(int j=0; j<tabuleiro.getColunas();j++) {
				mat[i][j] = (PecaDeXadrez) tabuleiro.peca(i,j);
			}		
		}
		return mat;
	}
	public boolean[][] movimentosPossiveis(XadrezPosicao posicaoOrigem){
		Posicao posicao = posicaoOrigem.converteParaPosicao();
		validaPosicaodeOrigem(posicao);
		return tabuleiro.peca(posicao).movimentosPossiveis();
	}
	public PecaDeXadrez performaMovimentoDeXadrez(XadrezPosicao origem, XadrezPosicao destino){
		Posicao posicaoDeOrigem = origem.converteParaPosicao();
		Posicao posicaoDeDestino = destino.converteParaPosicao();
		validaPosicaodeOrigem(posicaoDeOrigem);
		validaPosicaoDeDestino(posicaoDeOrigem, posicaoDeDestino);
		Peca pecaCapturada = movimentaPeca(posicaoDeOrigem, posicaoDeDestino);
		if(testeCheck(jogadorAtual)) {
			desfazerMovimento(posicaoDeOrigem, posicaoDeDestino, pecaCapturada);
			throw new XadrezException("Você não pode se colocar em check");
		}
		check = testeCheckMate(oponente(jogadorAtual)) ? true : false;
		if(testeCheck(oponente(jogadorAtual)))
			checkMate = true;
		else
			proximoTurno();
		return (PecaDeXadrez) pecaCapturada;
	}
	private Peca movimentaPeca(Posicao origem, Posicao destino){
		PecaDeXadrez p = (PecaDeXadrez) tabuleiro.removerPeca(origem); // Retira a peça da posição de origem;
		p.incrementaContadorDeMovimento();
		Peca pecaCapturada = tabuleiro.removerPeca(destino); // Remove a peça que está na posição de destino, pois foi capturada
		tabuleiro.colocarPeca(p, destino);

		if(pecaCapturada != null){
			pecasNoTabuleiro.remove(pecaCapturada);
			pecasCapturadas.add(pecaCapturada);
		}
		return pecaCapturada;
	}
	private void desfazerMovimento(Posicao origem, Posicao destino, Peca pecaCapturada){
		PecaDeXadrez p = (PecaDeXadrez) tabuleiro.removerPeca(destino);
		p.decrementaContadorDeMovimento();
		tabuleiro.colocarPeca(p, origem);
		if(pecaCapturada != null) {
			tabuleiro.colocarPeca(pecaCapturada, destino);
			pecasCapturadas.remove(pecaCapturada);
			pecasNoTabuleiro.add(pecaCapturada);
		}
	}
	private void validaPosicaodeOrigem(Posicao posicao){
		if(!tabuleiro.pecaExiste(posicao))
			throw new XadrezException("Não existe peça na posição de origem");
		if(jogadorAtual != ((PecaDeXadrez)tabuleiro.peca(posicao)).getCor())
			throw new XadrezException("Essa peça não é sua");
		if(!tabuleiro.peca(posicao).temMovimentosPossiveis(posicao))
			throw new XadrezException("Não existe movimentos possiveis para a peça escolhida");
	}
	private  void validaPosicaoDeDestino(Posicao origem, Posicao destino){
		if(!tabuleiro.peca(origem).movimentoPossivel(destino))
			throw new XadrezException("A peça escolhida não pode se mover para posição de destino");
	}
	private void proximoTurno(){
		turno++;
		jogadorAtual = (jogadorAtual == Cor.WHITE) ? Cor.BLACK : Cor.WHITE;
	}
	private Cor oponente(Cor cor){
		return (cor == Cor.WHITE) ? Cor.BLACK : Cor.WHITE;
	}
	private PecaDeXadrez rei(Cor cor){
		List<Peca> pecasNoTabuleiros = pecasNoTabuleiro
				.stream()
				.filter(p -> ((PecaDeXadrez)p).getCor() == cor)
				.collect(Collectors.toList());
	for(Peca p: pecasNoTabuleiros){
		if(p instanceof Rei)
			return (PecaDeXadrez)p;
	}
		throw new IllegalStateException("Não existe rei da cor " + cor + " no tabuleiro.");
	}
	private  boolean testeCheck(Cor cor){
		Posicao posicaoRei = rei(cor)
				.getPosicaoXadrez()
				.converteParaPosicao();
		List<Peca> pecasOponentes = pecasNoTabuleiro
				.stream()
				.filter(p ->((PecaDeXadrez)p).getCor() == oponente(cor))
				.collect(Collectors.toList());
		for(Peca p: pecasOponentes){
			boolean[][] mat = p.movimentosPossiveis();
			if(mat[posicaoRei.getLinha()][posicaoRei.getColuna()])
				return  true;
		}
		return false;
	}

	private boolean testeCheckMate(Cor cor) {
		if (!testeCheck(cor))
			return false;
		List<Peca> list = pecasNoTabuleiro
				.stream()
				.filter(p -> ((PecaDeXadrez) p).getCor() == (cor))
				.collect(Collectors.toList());
		for (Peca p : list) {
			boolean[][] mat = p.movimentosPossiveis();
			for (int i = 0; i < tabuleiro.getLinhas(); i++) {
				for (int j = 0; j < tabuleiro.getColunas(); j++) {
					if (mat[i][j]) {
						Posicao origem = ((PecaDeXadrez) p).getPosicaoXadrez()
								.converteParaPosicao();
						Posicao destino = new Posicao(i, j);
						Peca capturada = movimentaPeca(origem, destino);
						boolean testCheck = testeCheck(cor);
						desfazerMovimento(origem, destino, capturada);
						if (!testCheck)
							return false;
					}
				}
			}
		}
			return true;
		}
		private void colocaNovaPeca(char coluna, int linha, PecaDeXadrez peca){
			tabuleiro.colocarPeca(peca, new XadrezPosicao(coluna, linha).converteParaPosicao());
			pecasNoTabuleiro.add(peca);
		}
		private void setupInicial() {
			colocaNovaPeca('a', 1, new Torre(tabuleiro, Cor.WHITE));
			colocaNovaPeca('e', 1, new Rei(tabuleiro, Cor.WHITE));
			colocaNovaPeca('h', 1, new Torre(tabuleiro, Cor.WHITE));
			colocaNovaPeca('a', 2, new Peao(tabuleiro, Cor.WHITE));
			colocaNovaPeca('b', 2, new Peao(tabuleiro, Cor.WHITE));
			colocaNovaPeca('c', 2, new Peao(tabuleiro, Cor.WHITE));
			colocaNovaPeca('d', 2, new Peao(tabuleiro, Cor.WHITE));
			colocaNovaPeca('e', 2, new Peao(tabuleiro, Cor.WHITE));
			colocaNovaPeca('f', 2, new Peao(tabuleiro, Cor.WHITE));
			colocaNovaPeca('g', 2, new Peao(tabuleiro, Cor.WHITE));
			colocaNovaPeca('h', 2, new Peao(tabuleiro, Cor.WHITE));

			colocaNovaPeca('a', 8, new Torre(tabuleiro, Cor.BLACK));
			colocaNovaPeca('e', 8, new Rei(tabuleiro, Cor.BLACK));
			colocaNovaPeca('h', 8, new Torre(tabuleiro, Cor.BLACK));
			colocaNovaPeca('a', 7, new Peao(tabuleiro, Cor.BLACK));
			colocaNovaPeca('b', 7, new Peao(tabuleiro, Cor.BLACK));
			colocaNovaPeca('c', 7, new Peao(tabuleiro, Cor.BLACK));
			colocaNovaPeca('d', 7, new Peao(tabuleiro, Cor.BLACK));
			colocaNovaPeca('e', 7, new Peao(tabuleiro, Cor.BLACK));
			colocaNovaPeca('f', 7, new Peao(tabuleiro, Cor.BLACK));
			colocaNovaPeca('g', 7, new Peao(tabuleiro, Cor.BLACK));
			colocaNovaPeca('h', 7, new Peao(tabuleiro, Cor.BLACK));
		}
	}