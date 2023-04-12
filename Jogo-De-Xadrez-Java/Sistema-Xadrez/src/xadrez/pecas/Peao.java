package xadrez.pecas;

import tabuleiro.Posicao;
import tabuleiro.Tabuleiro;
import xadrez.Cor;
import xadrez.PartidaDeXadrez;
import xadrez.PecaDeXadrez;

public class Peao extends PecaDeXadrez {
    private PartidaDeXadrez partidaDeXadrez;
    public Peao(Tabuleiro tabuleiro, Cor cor, PartidaDeXadrez partidaDeXadrez) {
        super(tabuleiro, cor);
        this.partidaDeXadrez = partidaDeXadrez;

    }

    @Override
    public boolean[][] movimentosPossiveis() {
        boolean[][] mat =
                new boolean[getTabuleiro().getLinhas()]
                        [getTabuleiro().getColunas()];
        Posicao p = new Posicao(0,0);
        if (getCor() == Cor.WHITE) {
            p.setValores(posicao.getLinha() - 1, posicao.getColuna());
            if (getTabuleiro().posicaoExiste(p) && !getTabuleiro().pecaExiste(p)) {
                mat[p.getLinha()][p.getColuna()] = true;
            }
            p.setValores(posicao.getLinha() - 2, posicao.getColuna());
            Posicao p2 = new Posicao(posicao.getLinha() - 1, posicao.getColuna());
            if (getTabuleiro().posicaoExiste(p) && !getTabuleiro().pecaExiste(p) && getTabuleiro().posicaoExiste(p2) && !getTabuleiro().pecaExiste(p2) && getContadorDeMovimento() == 0) {
                mat[p.getLinha()][p.getColuna()] = true;
            }
            p.setValores(posicao.getLinha() - 1, posicao.getColuna() - 1);
            if (getTabuleiro().posicaoExiste(p) && temPecaOponente(p)) {
                mat[p.getLinha()][p.getColuna()] = true;
            }
            p.setValores(posicao.getLinha() - 1, posicao.getColuna() + 1);
            if (getTabuleiro().posicaoExiste(p) && temPecaOponente(p)) {
                mat[p.getLinha()][p.getColuna()] = true;
            }
            // Movimento especial en passant
            if (posicao.getLinha() == 3) {
                Posicao left = new Posicao(posicao.getLinha(), posicao.getColuna() - 1);
                if (getTabuleiro().posicaoExiste(left) && temPecaOponente(left) && getTabuleiro().peca(left) == partidaDeXadrez.getEnPassantVulneralvel()) {
                    mat[left.getLinha() - 1][left.getColuna()] = true;
                }
                Posicao right = new Posicao(posicao.getLinha(), posicao.getColuna() + 1);
                if (getTabuleiro().posicaoExiste(right) && temPecaOponente(right) && getTabuleiro().peca(right) == partidaDeXadrez.getEnPassantVulneralvel()) {
                    mat[right.getLinha() - 1][right.getColuna()] = true;
                }
            }
        }
        else {
            p.setValores(posicao.getLinha() + 1, posicao.getColuna());
            if (getTabuleiro().posicaoExiste(p) && !getTabuleiro().pecaExiste(p)) {
                mat[p.getLinha()][p.getColuna()] = true;
            }
            p.setValores(posicao.getLinha() + 2, posicao.getColuna());
            Posicao p3 = new Posicao(posicao.getLinha() + 1, posicao.getColuna());
            if (getTabuleiro().posicaoExiste(p) && !getTabuleiro().pecaExiste(p) && getTabuleiro().posicaoExiste(p3) && !getTabuleiro().pecaExiste(p3) && getContadorDeMovimento() == 0) {
                mat[p.getLinha()][p.getColuna()] = true;
            }
            p.setValores(posicao.getLinha() + 1, posicao.getColuna() - 1);
            if (getTabuleiro().posicaoExiste(p) && temPecaOponente(p)) {
                mat[p.getLinha()][p.getColuna()] = true;
            }
            p.setValores(posicao.getLinha() + 1, posicao.getColuna() + 1);
            if (getTabuleiro().posicaoExiste(p) && temPecaOponente(p)) {
                mat[p.getLinha()][p.getColuna()] = true;
            }

            // Movimento especial en passant
            if (posicao.getLinha() == 4) {
                Posicao left = new Posicao(posicao.getLinha(), posicao.getColuna() - 1);
                if (getTabuleiro().posicaoExiste(left) && temPecaOponente(left) && getTabuleiro().peca(left) == partidaDeXadrez.getEnPassantVulneralvel()) {
                    mat[left.getLinha() + 1][left.getColuna()] = true;
                }
                Posicao right = new Posicao(posicao.getLinha(), posicao.getColuna() + 1);
                if (getTabuleiro().posicaoExiste(right) && temPecaOponente(right) && getTabuleiro().peca(right) == partidaDeXadrez.getEnPassantVulneralvel()) {
                    mat[right.getLinha() + 1][right.getColuna()] = true;
                }
            }
        }
        return mat;
    }

    @Override
    public String toString() {
        return "P";
    }
}
