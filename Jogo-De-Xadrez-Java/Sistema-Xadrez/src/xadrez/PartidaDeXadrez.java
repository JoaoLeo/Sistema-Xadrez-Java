package xadrez;

import tabuleiro.Peca;
import tabuleiro.Posicao;
import tabuleiro.Tabuleiro;
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
		check = testeCheck(oponente(jogadorAtual)) ? true : false;
		proximoTurno();
		return (PecaDeXadrez) pecaCapturada;
	}
	private Peca movimentaPeca(Posicao origem, Posicao destino){
		Peca p = tabuleiro.removerPeca(origem); // Retira a peça da posição de origem;
		Peca pecaCapturada = tabuleiro.removerPeca(destino); // Remove a peça que está na posição de destino, pois foi capturada
		tabuleiro.colocarPeca(p, destino);

		if(pecaCapturada != null){
			pecasNoTabuleiro.remove(pecaCapturada);
			pecasCapturadas.add(pecaCapturada);
		}
		return pecaCapturada;
	}
	private void desfazerMovimento(Posicao origem, Posicao destino, Peca pecaCapturada){
		Peca p = tabuleiro.removerPeca(destino);
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
	private void colocaNovaPeca(char coluna, int linha, PecaDeXadrez peca){
		tabuleiro.colocarPeca(peca, new XadrezPosicao(coluna, linha).converteParaPosicao());
		pecasNoTabuleiro.add(peca);
	}
	private void setupInicial() {
		colocaNovaPeca('c', 1, new Torre(tabuleiro, Cor.WHITE));
		colocaNovaPeca('c', 2, new Torre(tabuleiro, Cor.WHITE));
		colocaNovaPeca('d', 2, new Torre(tabuleiro, Cor.WHITE));
		colocaNovaPeca('e', 2, new Torre(tabuleiro, Cor.WHITE));
		colocaNovaPeca('e', 1, new Torre(tabuleiro, Cor.WHITE));
		colocaNovaPeca('d', 1, new Rei(tabuleiro, Cor.WHITE));

		colocaNovaPeca('c', 7, new Torre(tabuleiro, Cor.BLACK));
		colocaNovaPeca('c', 8, new Torre(tabuleiro, Cor.BLACK));
		colocaNovaPeca('d', 7 ,new Torre(tabuleiro, Cor.BLACK));
		colocaNovaPeca('e', 7, new Torre(tabuleiro, Cor.BLACK));
		colocaNovaPeca('e', 8, new Torre(tabuleiro, Cor.BLACK));
		colocaNovaPeca('d', 8, new Rei(tabuleiro, Cor.BLACK));
	}}
