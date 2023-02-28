package tabuleiro;

public class TabuleiroException extends  RuntimeException{
    private static final long serialVersionUID = 1L;

    public TabuleiroException(String msg){
        super(msg); //Passando a mensagem para a super classe run time exception
    }}
