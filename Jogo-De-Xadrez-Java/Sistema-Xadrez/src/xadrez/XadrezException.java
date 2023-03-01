package xadrez;

public class XadrezException  extends  RuntimeException{
    private static final long serialVersionUID = 1L;
    
    public XadrezException(String msg){
        super(msg);
    }
}
