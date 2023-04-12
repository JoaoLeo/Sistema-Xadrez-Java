package xadrez;

import tabuleiro.Peca;
import tabuleiro.Posicao;
import tabuleiro.Tabuleiro;
import xadrez.pecas.*;

import java.security.InvalidParameterException;
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
	private PecaDeXadrez enPassantVulneralvel;
	private PecaDeXadrez promovida;

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

	public PecaDeXadrez getPromovida() {
		return promovida;
	}

	public void setPromovida(PecaDeXadrez promovida) {
		this.promovida = promovida;
	}

	public PecaDeXadrez getEnPassantVulneralvel() { return enPassantVulneralvel; }
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
		PecaDeXadrez pecaMovida = (PecaDeXadrez)tabuleiro.peca(posicaoDeDestino);
		// Movimento especial - Promoção
		promovida = null;
		if(pecaMovida instanceof Peao){
			if((pecaMovida.getCor() == Cor.WHITE && posicaoDeDestino.getLinha() == 0 ) || (pecaMovida.getCor() == Cor.BLACK && posicaoDeDestino.getLinha() == 7 )){
				promovida = (PecaDeXadrez)tabuleiro.peca(posicaoDeDestino);
				promovida = trocaPecaPromovida("Q");
			}
		}

		check = testeCheck(oponente(jogadorAtual)) ? true : false;
		if(testeCheckMate(oponente(jogadorAtual)))
			checkMate = true;
		else
			proximoTurno();
		if(pecaMovida instanceof Peao && (posicaoDeDestino.getLinha() == posicaoDeOrigem.getLinha() - 2 || posicaoDeDestino.getLinha() == posicaoDeDestino.getLinha() + 2))
			enPassantVulneralvel = pecaMovida;
		else
			enPassantVulneralvel = null;
		return (PecaDeXadrez) pecaCapturada;
	}
	public PecaDeXadrez trocaPecaPromovida(String tipo){
		if(promovida == null){
			throw new IllegalStateException("Não há peça pra ser promovida");
		}
		if(!tipo.equals("B" ) && !tipo.equals("C") && !tipo.equals("T") && !tipo.equals("Q")){
			throw new InvalidParameterException("Tipo invalido para promoção");
		}
		Posicao pos = promovida.getPosicaoXadrez().converteParaPosicao();
		Peca p = tabuleiro.removerPeca(pos);
		pecasNoTabuleiro.remove(p);
		PecaDeXadrez novaPeca = novaPeca(tipo, promovida.getCor());
		tabuleiro.colocarPeca(novaPeca, pos);
		pecasNoTabuleiro.add(novaPeca);
		return novaPeca;
	}

	private PecaDeXadrez novaPeca(String tipo, Cor cor){
		if(tipo.equals("B"))return new Bispo(tabuleiro, cor);
		if(tipo.equals("C"))return new Cavalo(tabuleiro, cor);
		if(tipo.equals("T"))return new Torre(tabuleiro, cor);
		return new Rainha(tabuleiro, cor);
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
		// Roque pequeno
		if(p instanceof Rei && destino.getColuna() == origem.getColuna() + 2){
			Posicao origemTorre = new Posicao(origem.getLinha(), origem.getColuna() + 3);
			Posicao destinoTorre = new Posicao(origem.getLinha(), origem.getColuna() + 1);
			PecaDeXadrez torre = (PecaDeXadrez)tabuleiro.removerPeca(origemTorre);
			tabuleiro.colocarPeca(torre, destinoTorre);
			torre.incrementaContadorDeMovimento();
		}

		// Roque grande
		if(p instanceof Rei && destino.getColuna() == origem.getColuna() - 2){
			Posicao origemTorre = new Posicao(origem.getLinha(), origem.getColuna() - 4);
			Posicao destinoTorre = new Posicao(origem.getLinha(), origem.getColuna() - 1);
			PecaDeXadrez torre = (PecaDeXadrez)tabuleiro.removerPeca(origemTorre);
			tabuleiro.colocarPeca(torre, destinoTorre);
			torre.incrementaContadorDeMovimento();
		}

		// En passant
		if(p instanceof Peao){
			if(origem.getColuna() != destino.getColuna() && pecaCapturada == null){
				Posicao peaoPosicao;
				if(p.getCor() == Cor.WHITE)
					peaoPosicao = new Posicao(destino.getLinha() + 1, destino.getColuna());
				else
					peaoPosicao = new Posicao(destino.getLinha() - 1, destino.getColuna());
				pecaCapturada = tabuleiro.removerPeca(peaoPosicao);
				pecasCapturadas.add(pecaCapturada);
				pecasNoTabuleiro.remove(pecaCapturada);
			}
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
		// Roque pequeno
		if(p instanceof Rei && destino.getColuna() == origem.getColuna() + 2){
			Posicao origemTorre = new Posicao(origem.getLinha(), origem.getColuna() + 3);
			Posicao destinoTorre = new Posicao(origem.getLinha(), origem.getColuna() + 1);
			PecaDeXadrez torre = (PecaDeXadrez)tabuleiro.removerPeca(destinoTorre);
			tabuleiro.colocarPeca(torre, origemTorre);
			torre.decrementaContadorDeMovimento();
		}

		// Roque grande
		if(p instanceof Rei && destino.getColuna() == origem.getColuna() - 2){
			Posicao origemTorre = new Posicao(origem.getLinha(), origem.getColuna() - 4);
			Posicao destinoTorre = new Posicao(origem.getLinha(), origem.getColuna() - 1);
			PecaDeXadrez torre = (PecaDeXadrez)tabuleiro.removerPeca(destinoTorre);
			tabuleiro.colocarPeca(torre, origemTorre);
			torre.decrementaContadorDeMovimento();
		}

		//En passant
		if(p instanceof Peao){
			if(origem.getColuna() != destino.getColuna() && pecaCapturada == enPassantVulneralvel){
				PecaDeXadrez peao = (PecaDeXadrez)tabuleiro.removerPeca(destino);
				Posicao peaoPosicao;
				if(p.getCor() == Cor.WHITE)
					peaoPosicao = new Posicao(3 , destino.getColuna());
				else
					peaoPosicao = new Posicao(4, destino.getColuna());
				tabuleiro.colocarPeca(peao, peaoPosicao);
			}
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
				return true;
		}
		return false;
	}

	private boolean testeCheckMate(Cor cor) {
		if (!testeCheck(cor))
			return false;
		List<Peca> list = pecasNoTabuleiro
				.stream()
				.filter(p -> ((PecaDeXadrez) p).getCor() == cor)
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
						if (!testCheck) {
							return false;
						}
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
			colocaNovaPeca('b', 1, new Cavalo(tabuleiro, Cor.WHITE));
			colocaNovaPeca('c', 1, new Bispo(tabuleiro, Cor.WHITE));
			colocaNovaPeca('d', 1, new Rainha(tabuleiro, Cor.WHITE));
			colocaNovaPeca('e', 1, new Rei(tabuleiro, Cor.WHITE, this));
			colocaNovaPeca('f', 1, new Bispo(tabuleiro, Cor.WHITE));
			colocaNovaPeca('h', 1, new Torre(tabuleiro, Cor.WHITE));
			colocaNovaPeca('g', 1, new Cavalo(tabuleiro, Cor.WHITE));
			colocaNovaPeca('a', 2, new Peao(tabuleiro, Cor.WHITE,this));
			colocaNovaPeca('b', 2, new Peao(tabuleiro, Cor.WHITE,this));
			colocaNovaPeca('c', 2, new Peao(tabuleiro, Cor.WHITE,this));
			colocaNovaPeca('d', 2, new Peao(tabuleiro, Cor.WHITE,this));
			colocaNovaPeca('e', 2, new Peao(tabuleiro, Cor.WHITE,this));
			colocaNovaPeca('f', 2, new Peao(tabuleiro, Cor.WHITE,this));
			colocaNovaPeca('g', 2, new Peao(tabuleiro, Cor.WHITE,this));
			colocaNovaPeca('h', 2, new Peao(tabuleiro, Cor.WHITE,this));

			colocaNovaPeca('a', 8, new Torre(tabuleiro, Cor.BLACK));
			colocaNovaPeca('b', 8, new Cavalo(tabuleiro, Cor.BLACK));
			colocaNovaPeca('c', 8, new Bispo(tabuleiro, Cor.BLACK));
			colocaNovaPeca('d', 8, new Rainha(tabuleiro, Cor.BLACK));
			colocaNovaPeca('e', 8, new Rei(tabuleiro, Cor.BLACK, this));
			colocaNovaPeca('f', 8, new Bispo(tabuleiro, Cor.BLACK));
			colocaNovaPeca('h', 8, new Torre(tabuleiro, Cor.BLACK));
			colocaNovaPeca('g', 8, new Cavalo(tabuleiro, Cor.BLACK));
			colocaNovaPeca('a', 7, new Peao(tabuleiro, Cor.BLACK,this));
			colocaNovaPeca('b', 7, new Peao(tabuleiro, Cor.BLACK,this));
			colocaNovaPeca('c', 7, new Peao(tabuleiro, Cor.BLACK,this));
			colocaNovaPeca('d', 7, new Peao(tabuleiro, Cor.BLACK,this));
			colocaNovaPeca('e', 7, new Peao(tabuleiro, Cor.BLACK,this));
			colocaNovaPeca('f', 7, new Peao(tabuleiro, Cor.BLACK,this));
			colocaNovaPeca('g', 7, new Peao(tabuleiro, Cor.BLACK,this));
			colocaNovaPeca('h', 7, new Peao(tabuleiro, Cor.BLACK,this));
		}
	}