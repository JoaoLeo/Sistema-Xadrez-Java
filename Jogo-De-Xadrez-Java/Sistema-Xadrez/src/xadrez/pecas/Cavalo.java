package xadrez.pecas;

import tabuleiro.Posicao;
import tabuleiro.Tabuleiro;
import xadrez.Cor;
import xadrez.PecaDeXadrez;

public class Cavalo  extends PecaDeXadrez {
    public Cavalo(Tabuleiro tabuleiro, Cor cor) {
        super(tabuleiro, cor);
    }

    private  boolean podeSeMover(Posicao posicao){
        PecaDeXadrez p = (PecaDeXadrez)getTabuleiro().peca(posicao);
        return p == null || p.getCor() != getCor();
    }
    @Override
    public boolean[][] movimentosPossiveis() {
        boolean[][] mat =
                new boolean[getTabuleiro().getLinhas()]
                        [getTabuleiro().getColunas()];
        Posicao p = new Posicao(0,0);
        p.setValores(posicao.getLinha() - 1, posicao.getColuna() -2);
        if(getTabuleiro().posicaoExiste(p) && podeSeMover(p))
            mat[p.getLinha()][p.getColuna()] = true;


        p.setValores(posicao.getLinha() -2, posicao.getColuna() -1);
        if(getTabuleiro().posicaoExiste(p) && podeSeMover(p))
            mat[p.getLinha()][p.getColuna()] = true;


        p.setValores(posicao.getLinha() - 2 , posicao.getColuna() + 1);
        if(getTabuleiro().posicaoExiste(p) && podeSeMover(p))
            mat[p.getLinha()][p.getColuna()] = true;


        p.setValores(posicao.getLinha() -1 , posicao.getColuna() + 2);
        if(getTabuleiro().posicaoExiste(p) && podeSeMover(p))
            mat[p.getLinha()][p.getColuna()] = true;


        p.setValores(posicao.getLinha() + 1 , posicao.getColuna() + 2);
        if(getTabuleiro().posicaoExiste(p) && podeSeMover(p))
            mat[p.getLinha()][p.getColuna()] = true;

        p.setValores(posicao.getLinha() + 2 , posicao.getColuna() + 1);
        if(getTabuleiro().posicaoExiste(p) && podeSeMover(p))
            mat[p.getLinha()][p.getColuna()] = true;

        p.setValores(posicao.getLinha() + 2 , posicao.getColuna() - 1);
        if(getTabuleiro().posicaoExiste(p) && podeSeMover(p))
            mat[p.getLinha()][p.getColuna()] = true;

        p.setValores(posicao.getLinha() + 1 , posicao.getColuna() - 2);
        if(getTabuleiro().posicaoExiste(p) && podeSeMover(p))
            mat[p.getLinha()][p.getColuna()] = true;
        return mat;
    }

    @Override
    public String toString(){
        return "C";
    }
}
