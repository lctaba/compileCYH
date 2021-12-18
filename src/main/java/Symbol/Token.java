package Symbol;


/**
 * @Author cyh
 * @Date 2021/6/12 17:40
 */

public class Token {
    enum ConstType{
        NULL,
        INT,
        DOUBLE,
        STRING,
        BOOL,
    }
    //行数
    int row;
    //符号类型
    SymbolType symbolType;
    //具体分词
    String name;
    //常数类型;
    ConstType constType;

    public Token(int row, SymbolType symbolType, String name) {
        this.row = row;
        this.symbolType = symbolType;
        this.name = name;
        if(symbolType.getCode() == 37){
            constType = ConstType.INT;
        }else if(symbolType.getCode() == 38){
            constType = ConstType.DOUBLE;
        }else if(symbolType.getCode() == 39){
            constType = ConstType.STRING;
        }else if(symbolType.getCode() == 40 || symbolType.getCode() == 41){
            constType = ConstType.BOOL;
        }else {
            constType = ConstType.NULL;
        }
    }

    @Override
    public String toString() {
        return "Token{" +
                "row=" + row +
                ", symbolType=" + symbolType +
                ", name=" + name +
                ", constType=" + constType +
                '}';
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public SymbolType getSymbolType() {
        return symbolType;
    }

    public void setSymbolType(SymbolType symbolType) {
        this.symbolType = symbolType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ConstType getConstType() {
        return constType;
    }

    public void setConstType(ConstType constType) {
        this.constType = constType;
    }
}
