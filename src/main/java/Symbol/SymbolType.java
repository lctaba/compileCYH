package Symbol;

/**
 * @Author cyh
 * @Date 2021/6/12 22:16
 */
public enum SymbolType {
    //关键字
    FOR(1),
    WHILE(2),
    DO(3),
    BREAK(4),
    CONTINUE(5),
    IF(6),
    ELSE(7),
    RETURN(8),
    VOID(9),
    //标识符
    ID(10),
    //数据类型
    INT(11),
    DOUBLE(12),
    STRING(13),
    BOOL(14),
    //算数运算符
    PLUS(15),
    SUB(16),
    MUL(17),
    DIV(18),
    MOD(19),
    //逻辑运算符
    AND(20),
    OR(21),
    NOT(22),
    //大于
    GT(23),
    //小于
    LT(24),
    //大于等于
    GET(25),
    //小于等于
    LET(26),
    EQ(27),
    NEQ(28),
    //赋值符号
    ASSIGN(29),
    //界限符
    //小括号
    LB(30),
    RB(31),
    //大括号
    LBR(32),
    RBR(33),
    //分号
    SEM(34),
    //逗号
    COM(35),
    //终止符
    END(36),
    //常量
    CONSTINT(37),
    CONSTDOUBLE(38),
    CONSTSTRING(39),


    TRUE(40),
    FALSE(41);

    private int code;

    private SymbolType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
