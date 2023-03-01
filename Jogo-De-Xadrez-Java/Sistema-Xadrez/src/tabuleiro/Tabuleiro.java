package tabuleiro;

public class Tabuleiro {
	private Integer linhas;
	private Integer colunas;
	private Peca[][] pecas;
	public Tabuleiro(Integer linha, Integer coluna) {
		if(linha < 1 || coluna < 1)
			throw new TabuleiroException("Erro ao criar tabuleiro é necessario que haja pelo menos 1 linha e 1 coluna.");
		this.linhas = linha;
		this.colunas = coluna;
		pecas = new Peca[linhas][colunas];
	}
	public Integer getLinhas() {
		return linhas;
	}
	public Integer getColunas() {
		return colunas;
	}
	public Peca peca(Integer linha, Integer coluna) {
		if(!posicaoExiste(linha, coluna))
			throw new TabuleiroException("Não existe essa posição no tabuleiro");
		return pecas[linha][coluna];
	}
	public Peca peca(Posicao posicao) {
		if(!posicaoExiste(posicao))
			throw new TabuleiroException("Não existe essa posição no tabuleiro");
		return pecas[posicao.getLinha()][posicao.getColuna()];
	}
	
	public void colocarPeca(Peca peca, Posicao posicao) {
		if(pecaExiste(posicao))
			throw new TabuleiroException("Já existe uma peça na posição " + posicao);
		pecas[posicao.getLinha()][posicao.getColuna()] = peca;
		peca.posicao = posicao;
	}
	public Peca removerPeca(Posicao posicao){
		if(!posicaoExiste(posicao))
			throw new TabuleiroException("Não existe essa posição no tabuleiro");
		if(peca(posicao) == null)
			return null;
		Peca p = peca(posicao);
		p.posicao = null;
		pecas[posicao.getLinha()][posicao.getColuna()] = null;
		return p;
	}
	private Boolean posicaoExiste(int linha, int coluna){
		return linha >= 0 && linha < linhas && coluna >=0  && coluna < colunas;
	}
	public Boolean posicaoExiste(Posicao posicao){
		return posicaoExiste(posicao.getLinha(), posicao.getColuna());
	}

	public Boolean pecaExiste(Posicao posicao){
		if(!posicaoExiste(posicao))
			throw new TabuleiroException("Não existe essa posição no tabuleiro");
		return peca(posicao) != null;
	}
}
