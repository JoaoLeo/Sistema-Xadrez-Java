package tabuleiro;

public class Tabuleiro {
	private Integer linhas;
	private Integer colunas;
	private Peca[][] pecas;
	public Tabuleiro(Integer linha, Integer coluna) {
		this.linhas = linha;
		this.colunas = coluna;
		pecas = new Peca[linhas][colunas];
	}
	public Integer getLinhas() {
		return linhas;
	}
	public void setLinhas(Integer linhas) {
		this.linhas = linhas;
	}
	public Integer getColunas() {
		return colunas;
	}
	public void setColunas(Integer colunas) {
		this.colunas = colunas;
	}
	
	public Peca peca(Integer linha, Integer coluna) {
		return pecas[linha][coluna];
	}
	public Peca peca(Posicao posicao) {
		return pecas[posicao.getLinha()][posicao.getColuna()];
	}
	
	public void colocarPeca(Peca peca, Posicao posicao) {
		pecas[posicao.getLinha()][posicao.getColuna()] = peca;
		peca.posicao = posicao;
	}
	
}
