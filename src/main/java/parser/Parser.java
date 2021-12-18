package parser;

import Symbol.SymbolType;
import Symbol.Token;
import exception.CompileException;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author cyh
 * @Date 2021/6/23 10:27
 */
public class Parser {
    public static int index = 0;
    public static List<String>  parse(List<Token> tokens, List<CompileException> compileExceptions){
        List<String> generators = new ArrayList<String>();
        index = 0;
        parseProgram(tokens,compileExceptions,generators);
        return generators;
    }

    public static void parseProgram(List<Token> tokens, List<CompileException> compileExceptions, List<String> generators){
        if(index < tokens.size()){
            Token token = tokens.get(index);
            SymbolType type = token.getSymbolType();
            switch (type){
                case INT:
                case DOUBLE:
                case BOOL:
                case ID:
                case IF:
                case ELSE:
                case WHILE:
                    generators.add("<program>           →       <statement><program>");
                    parseStatement(tokens, compileExceptions, generators);
                    parseProgram(tokens, compileExceptions, generators);
                    break;
                case RBR:
                    generators.add("<program>           →       Ɛ");
                    break;
                default:
                    compileExceptions.add(new CompileException(token.getRow(),"invalid statement: " + token.getName()));
            }
        }else {
            generators.add("<program>           →       Ɛ");
        }
    }


    public static void parseStatement(List<Token> tokens, List<CompileException> compileExceptions, List<String> generators){
        if (index < tokens.size()) {
            Token token = tokens.get(index);
            SymbolType type = token.getSymbolType();
            switch (type){
                case INT:
                case DOUBLE:
                case BOOL:
                    generators.add("<statement>         →       <declareStatement>");
                    parseDeclareStatement(tokens,compileExceptions,generators);
                    break;
                case ID:
                    generators.add("<statement>         →       <assignStatement>");
                    parseAssignStatement(tokens,compileExceptions,generators);
                    break;
                case IF:
                    generators.add("<statement>         →       <ifStatement>");
                    parseIfStatement(tokens,compileExceptions,generators);
                    break;
                case WHILE:
                    generators.add("<statement>         →       <whileStatement>");
                    parseWhileStatement(tokens,compileExceptions,generators);
                    break;
                case ELSE:
                case RBR:
                    generators.add("<statement>         →       Ɛ");
                    break;
                default:
                    compileExceptions.add(new CompileException(token.getRow(),"invalid statement: " + token.getName()));
            }
        }
    }

    public static void parseDeclareStatement(List<Token> tokens, List<CompileException> compileExceptions, List<String> generators){
        if (index < tokens.size()) {
            Token token = tokens.get(index);
            SymbolType type = token.getSymbolType();
            switch (type){
                case INT:
                case DOUBLE:
                case BOOL:
                    generators.add("<declareStatement>  →       <type>A");
                    parseType(tokens,compileExceptions,generators);
                    parseA(tokens,compileExceptions,generators);
                    break;
                default:
                    compileExceptions.add(new CompileException(token.getRow(),"invalid dataType: " + token.getName()));
            }
        }else {
            compileExceptions.add(new CompileException("unfinished declareStatement"));
        }
    }

    public static void parseAssignStatement(List<Token> tokens, List<CompileException> compileExceptions, List<String> generators){
        if (index < tokens.size()) {
            Token token = tokens.get(index);
            SymbolType type = token.getSymbolType();
            switch (type){
                case ID:
                    generators.add("<assignStatement>   →       <id>B");
                    index++;
                    parseB(tokens,compileExceptions,generators);
                    break;
                default:
                    compileExceptions.add(new CompileException(token.getRow(),"invalid assign statement: " + token.getName()));
            }
        }else {
            compileExceptions.add(new CompileException("unfinished assignStatement"));
        }
    }

    public static void parseIfStatement(List<Token> tokens, List<CompileException> compileExceptions, List<String> generators){
        if (index < tokens.size()) {
            Token token = tokens.get(index);
            SymbolType type = token.getSymbolType();
            switch (type){
                case IF:
                    generators.add("<ifStatement>       →       if(<logicExpression>){<program>}<elseStatement>");
                    index++;
                    if(index < tokens.size()){
                        token = tokens.get(index);
                        if(token.getSymbolType() == SymbolType.LB){
                            index++;
                            parseLogicExpression(tokens,compileExceptions,generators);
                            if(index < tokens.size()){
                                token = tokens.get(index);
                                if(token.getSymbolType() == SymbolType.RB){
                                    index++;
                                    if(index < tokens.size()){
                                        token = tokens.get(index);
                                        if(token.getSymbolType() == SymbolType.LBR){
                                            index++;
                                            parseProgram(tokens,compileExceptions,generators);
                                            if(index < tokens.size()){
                                                token = tokens.get(index);
                                                if(token.getSymbolType() == SymbolType.RBR){
                                                    index++;
                                                    parseElseStatement(tokens,compileExceptions,generators);
                                                }else {
                                                    compileExceptions.add(new CompileException(token.getRow(),"unfinished ifStatement: " + token.getName()));
                                                }
                                            }else {
                                                compileExceptions.add(new CompileException(token.getRow(),"unfinished ifStatement: " + token.getName()));
                                            }
                                        }else {
                                            compileExceptions.add(new CompileException(token.getRow(),"unfinished ifStatement: " + token.getName()));
                                        }
                                    }else {
                                        compileExceptions.add(new CompileException(token.getRow(),"unfinished ifStatement: " + token.getName()));
                                    }
                                }else {
                                    compileExceptions.add(new CompileException(token.getRow(),"unfinished ifStatement: " + token.getName()));
                                }
                            }else {
                                compileExceptions.add(new CompileException(token.getRow(),"unfinished ifStatement: " + token.getName()));
                            }
                        }else {
                            compileExceptions.add(new CompileException(token.getRow(),"unfinished ifStatement: " + token.getName()));
                        }
                    }else {
                        compileExceptions.add(new CompileException(token.getRow(),"unfinished ifStatement: " + token.getName()));
                    }
                    break;
                default:
                    compileExceptions.add(new CompileException(token.getRow(),"invalid branch statement: " + token.getName()));
            }
        }else {
            compileExceptions.add(new CompileException("unfinished ifStatement"));
        }
    }

    public static void parseWhileStatement(List<Token> tokens, List<CompileException> compileExceptions, List<String> generators){
        if (index < tokens.size()) {
            Token token = tokens.get(index);
            SymbolType type = token.getSymbolType();
            switch (type){
                case WHILE:
                    generators.add("<whileStatement>       →       <whileStatement>→while(<logicExpression>){<program>}");
                    index++;
                    if(index < tokens.size()){
                        token = tokens.get(index);
                        if(token.getSymbolType() == SymbolType.LB){
                            index++;
                            parseLogicExpression(tokens,compileExceptions,generators);
                            if(index < tokens.size()){
                                token = tokens.get(index);
                                if(token.getSymbolType() == SymbolType.RB){
                                    index++;
                                    if(index < tokens.size()){
                                        token = tokens.get(index);
                                        if(token.getSymbolType() == SymbolType.LBR){
                                            index++;
                                            parseProgram(tokens,compileExceptions,generators);
                                            if(index < tokens.size()){
                                                token = tokens.get(index);
                                                if(token.getSymbolType() == SymbolType.RBR){
                                                    index++;
                                                }else {
                                                    compileExceptions.add(new CompileException(token.getRow(),"unfinished whileStatement: " + token.getName()));
                                                }
                                            }else {
                                                compileExceptions.add(new CompileException(token.getRow(),"unfinished whileStatement: " + token.getName()));
                                            }
                                        }else {
                                            compileExceptions.add(new CompileException(token.getRow(),"unfinished whileStatement: " + token.getName()));
                                        }
                                    }else {
                                        compileExceptions.add(new CompileException(token.getRow(),"unfinished whileStatement: " + token.getName()));
                                    }
                                }else {
                                    compileExceptions.add(new CompileException(token.getRow(),"unfinished whileStatement: " + token.getName()));
                                }
                            }else {
                                compileExceptions.add(new CompileException(token.getRow(),"unfinished whileStatement: " + token.getName()));
                            }
                        }else {
                            compileExceptions.add(new CompileException(token.getRow(),"unfinished whileStatement: " + token.getName()));
                        }
                    }else {
                        compileExceptions.add(new CompileException(token.getRow(),"unfinished whileStatement: " + token.getName()));
                    }
                    break;
                default:
                    compileExceptions.add(new CompileException(token.getRow(),"invalid loop statement: " + token.getName()));
            }
        }else {
            compileExceptions.add(new CompileException("unfinished whileStatement"));
        }
    }

    public static void parseElseStatement(List<Token> tokens, List<CompileException> compileExceptions, List<String> generators){
        if (index < tokens.size()) {
            Token token = tokens.get(index);
            SymbolType type = token.getSymbolType();
            switch (type){
                case ELSE:
                    generators.add("<elseStatement>     →       else{<program>}");
                    index++;
                    if(index < tokens.size()){
                        token = tokens.get(index);
                        if(token.getSymbolType() == SymbolType.LBR){
                            index++;
                            parseProgram(tokens,compileExceptions,generators);
                            if(index < tokens.size()){
                                token = tokens.get(index);
                                if(token.getSymbolType() == SymbolType.RBR){
                                    index++;
                                }else {
                                    compileExceptions.add(new CompileException(token.getRow(),"unfinished elseStatement: " + token.getName()));
                                }
                            }else {
                                compileExceptions.add(new CompileException(token.getRow(),"unfinished elseStatement: " + token.getName()));
                            }
                        }else {
                            compileExceptions.add(new CompileException(token.getRow(),"unfinished elseStatement: " + token.getName()));
                        }
                    }else {
                        compileExceptions.add(new CompileException(token.getRow(),"unfinished elseStatement: " + token.getName()));
                    }
                    break;
                case INT:
                case DOUBLE:
                case BOOL:
                case ID:
                case IF:
                case WHILE:
                case RBR:
                    generators.add("<elseStatement>     →       Ɛ}");
                    break;
                default:
                    compileExceptions.add(new CompileException(token.getRow(),"invalid branch statement: " + token.getName()));
            }
        }else {
            compileExceptions.add(new CompileException("unfinished elseStatement"));
        }
    }

    public static void parseAriExpression(List<Token> tokens, List<CompileException> compileExceptions, List<String> generators){
        if (index < tokens.size()) {
            Token token = tokens.get(index);
            SymbolType type = token.getSymbolType();
            switch (type){
                case PLUS:
                    generators.add("<ariExpression>     →       +T<ariExpression>'");
                    index++;
                    parseT(tokens,compileExceptions,generators);
                    parseAriExpressionP(tokens,compileExceptions,generators);
                    break;
                case SUB:
                    generators.add("<ariExpression>     →       -T<ariExpression>'");
                    index++;
                    parseT(tokens,compileExceptions,generators);
                    parseAriExpressionP(tokens,compileExceptions,generators);
                    break;
                case ID:
                case CONSTINT:
                case CONSTDOUBLE:
                case LB:
                    generators.add("<ariExpression>     →        T<ariExpression>'");
                    parseT(tokens,compileExceptions,generators);
                    parseAriExpressionP(tokens,compileExceptions,generators);
                    break;
                default:
                    compileExceptions.add(new CompileException(token.getRow(),"invalid arithmetic expression: " + token.getName()));
            }
        }else {
            compileExceptions.add(new CompileException("unfinished ariExpression"));
        }
    }

    public static void parseAriExpressionP(List<Token> tokens, List<CompileException> compileExceptions, List<String> generators){
        if (index < tokens.size()) {
            Token token = tokens.get(index);
            SymbolType type = token.getSymbolType();
            switch (type){
                case PLUS:
                    generators.add("<ariExpression>'    →       +T<ariExpression>'");
                    index++;
                    parseT(tokens,compileExceptions,generators);
                    parseAriExpressionP(tokens,compileExceptions,generators);
                    break;
                case SUB:
                    generators.add("<ariExpression>'    →       -T<ariExpression>'");
                    index++;
                    parseT(tokens,compileExceptions,generators);
                    parseAriExpressionP(tokens,compileExceptions,generators);
                    break;
                case SEM:
                case LT:
                case LET:
                case GT:
                case GET:
                case EQ:
                case NEQ:
                case RBR:
                case RB:
                case AND:
                case OR:
                    generators.add("<ariExpression>'    →       Ɛ");
                    break;
                default:
                    compileExceptions.add(new CompileException(token.getRow(),"invalid arithmetic expression: " + token.getName()));
            }
        }else {
            compileExceptions.add(new CompileException("unfinished ariExpression"));
        }
    }

    public static void parseT(List<Token> tokens, List<CompileException> compileExceptions, List<String> generators){
        if (index < tokens.size()) {
            Token token = tokens.get(index);
            SymbolType type = token.getSymbolType();
            switch (type){
                case ID:
                case CONSTDOUBLE:
                case CONSTINT:
                case LB:
                    generators.add("T                   →       FT'");
                    parseF(tokens,compileExceptions,generators);
                    parseTP(tokens,compileExceptions,generators);
                    break;
                default:
                    compileExceptions.add(new CompileException(token.getRow(),"invalid arithmetic expression: " + token.getName()));
            }
        }else {
            compileExceptions.add(new CompileException("unfinished ariExpression"));
        }
    }

    public static void parseA(List<Token> tokens, List<CompileException> compileExceptions, List<String> generators){
        if (index < tokens.size()) {
            Token token = tokens.get(index);
            SymbolType type = token.getSymbolType();
            switch (type){
                case ID:
                    generators.add("A                   →       <id>C");
                    index++;
                    parseC(tokens,compileExceptions,generators);
                    break;
                default:
                    compileExceptions.add(new CompileException(token.getRow(),"invalid declare statement: " + token.getName()));
            }
        }else {
            compileExceptions.add(new CompileException("unfinished ariExpression"));
        }
    }

    public static void parseB(List<Token> tokens, List<CompileException> compileExceptions, List<String> generators){
        if (index < tokens.size()) {
            Token token = tokens.get(index);
            SymbolType type = token.getSymbolType();
            switch (type){
                case ASSIGN:
                    generators.add("B                   →       =<ariExpression>;");
                    index++;
                    parseAriExpression(tokens,compileExceptions,generators);
                    hasSem(tokens, compileExceptions, token);
                    break;
                case LET:
                    generators.add("B                   →       <=<logicExpression>;");
                    index++;
                    parseLogicExpression(tokens,compileExceptions,generators);
                    hasSem(tokens, compileExceptions, token);
                    break;
                default:
                    compileExceptions.add(new CompileException(token.getRow(),"invalid assign statement: " + token.getName()));
            }
        }else {
            compileExceptions.add(new CompileException("unfinished ariExpression"));
        }
    }

    public static void parseC(List<Token> tokens, List<CompileException> compileExceptions, List<String> generators){
        if (index < tokens.size()) {
            Token token = tokens.get(index);
            SymbolType type = token.getSymbolType();
            switch (type){
                case SEM:
                    generators.add("C                   →       ;");
                    index++;
                    break;
                case ASSIGN:
                    generators.add("C                   →       =<ariExpression>;");
                    index++;
                    parseAriExpression(tokens,compileExceptions,generators);
                    hasSem(tokens, compileExceptions, token);
                    break;
                case LET:
                    generators.add("C                   →       <=<logicExpression>;");
                    index++;
                    parseLogicExpression(tokens,compileExceptions,generators);
                    hasSem(tokens, compileExceptions, token);
                    break;
                default:
                    compileExceptions.add(new CompileException(token.getRow(),"invalid declare statement: " + token.getName()));
            }
        }else {
            compileExceptions.add(new CompileException("unfinished ariExpression"));
        }
    }

    private static void hasSem(List<Token> tokens, List<CompileException> compileExceptions, Token token) {
        if(index < tokens.size()){
            token = tokens.get(index);
            if(token.getSymbolType() == SymbolType.SEM){
                index++;
            }else {
                compileExceptions.add(new CompileException(token.getRow(),"missing ;"));
            }
        }else {
            compileExceptions.add(new CompileException(token.getRow(),"unfinished assignStatement: " + token.getName()));
        }
    }

    public static void parseTP(List<Token> tokens, List<CompileException> compileExceptions, List<String> generators){
        if (index < tokens.size()) {
            Token token = tokens.get(index);
            SymbolType type = token.getSymbolType();
            switch (type){
                case MUL:
                    generators.add("T'                  →       *FT'");
                    index++;
                    parseF(tokens,compileExceptions,generators);
                    parseTP(tokens,compileExceptions,generators);
                    break;
                case DIV:
                    generators.add("T'                  →       /FT'");
                    index++;
                    parseF(tokens,compileExceptions,generators);
                    parseTP(tokens,compileExceptions,generators);
                    break;
                case PLUS:
                case SUB:
                case SEM:
                case LT:
                case LET:
                case GET:
                case GT:
                case NEQ:
                case EQ:
                case RB:
                case RBR:
                case AND:
                case OR:
                    generators.add("T'                  →       Ɛ");
                    break;
                default:
                    compileExceptions.add(new CompileException(token.getRow(),"invalid arithmetic expression: " + token.getName()));
            }
        }else {
            compileExceptions.add(new CompileException("unfinished ariExpression"));
        }
    }

    public static void parseF(List<Token> tokens, List<CompileException> compileExceptions, List<String> generators){
        if (index < tokens.size()) {
            Token token = tokens.get(index);
            SymbolType type = token.getSymbolType();
            switch (type){
                case ID:
                    generators.add("F                   →       <id>");
                    index++;
                    break;
                case CONSTINT:
                    generators.add("F                   →       <constInt>");
                    index++;
                    break;
                case CONSTDOUBLE:
                    generators.add("F                   →       <constDouble>");
                    index++;
                    break;
                case LB:
                    generators.add("F                   →       (<ariExpression>)");
                    index++;
                    parseAriExpression(tokens,compileExceptions,generators);
                    if(index < tokens.size()){
                        token = tokens.get(index);
                        if(token.getSymbolType() == SymbolType.RB){
                            index++;
                        }else {
                            compileExceptions.add(new CompileException(token.getRow(),"invalid arithmetic expression: " + token.getName()));
                        }
                    }else {
                        compileExceptions.add(new CompileException(token.getRow(),"invalid arithmetic expression: " + token.getName()));
                    }
                    break;
                default:
                    compileExceptions.add(new CompileException(token.getRow(),"invalid arithmetic expression: " + token.getName()));
            }
        }else {
            compileExceptions.add(new CompileException("unfinished ariExpression"));
        }
    }

    public static void parseLogicExpression(List<Token> tokens, List<CompileException> compileExceptions, List<String> generators){
        if (index < tokens.size()) {
            Token token = tokens.get(index);
            SymbolType type = token.getSymbolType();
            switch (type){
                case LBR:
                    generators.add("<logicExpression>   →       {<ariExpression><comparator><ariExpression>}<logicExpression>'");
                    index++;
                    parseAriExpression(tokens,compileExceptions,generators);
                    parseComparator(tokens,compileExceptions,generators);
                    parseAriExpression(tokens,compileExceptions,generators);
                    if(index < tokens.size()){
                        token = tokens.get(index);
                        if(token.getSymbolType() == SymbolType.RBR){
                            index++;
                            parseLogicExpressionP(tokens,compileExceptions,generators);
                        }else {
                            compileExceptions.add(new CompileException(token.getRow(),"invalid logical expression: " + token.getName()));
                        }
                    }else {
                        compileExceptions.add(new CompileException(token.getRow(),"invalid logical expression: " + token.getName()));
                    }
                    break;
                case NOT:
                    generators.add("<logicExpression>   →       !<logicExpression><logicExpression>'");
                    index++;
                    parseLogicExpression(tokens,compileExceptions,generators);
                    parseLogicExpressionP(tokens,compileExceptions,generators);
                    break;
                case LB:
                    generators.add("<logicExpression>   →       (<logicExpression>)<logicExpression>'");
                    index++;
                    parseLogicExpression(tokens,compileExceptions,generators);
                    if(index<tokens.size()){
                        token = tokens.get(index);
                        if(token.getSymbolType() == SymbolType.RB){
                            index++;
                            parseLogicExpressionP(tokens,compileExceptions,generators);
                        }else {
                            compileExceptions.add(new CompileException(token.getRow(),"invalid logical expression: " + token.getName()));
                        }
                    }else {
                        compileExceptions.add(new CompileException(token.getRow(),"invalid logical expression: " + token.getName()));
                    }
                    break;
                case TRUE:
                    generators.add("<logicExpression>   →       true<logicExpression>'");
                    index++;
                    parseLogicExpressionP(tokens,compileExceptions,generators);
                    break;
                case FALSE:
                    generators.add("<logicExpression>   →       false<logicExpression>'");
                    index++;
                    parseLogicExpressionP(tokens,compileExceptions,generators);
                    break;
                case ID:
                    generators.add("<logicExpression>   →       <id><logicExpression>'");
                    index++;
                    parseLogicExpressionP(tokens,compileExceptions,generators);
                    break;
                default:
                    compileExceptions.add(new CompileException(token.getRow(),"invalid logical expression: " + token.getName()));
            }
        }else {
            compileExceptions.add(new CompileException("unfinished logicExpression"));
        }
    }

    public static void parseLogicExpressionP(List<Token> tokens, List<CompileException> compileExceptions, List<String> generators){
        if (index < tokens.size()) {
            Token token = tokens.get(index);
            SymbolType type = token.getSymbolType();
            switch (type){
                case AND:
                    generators.add("<logicExpression>'  →       &<logicExpression><logicExpression>'");
                    index++;
                    parseLogicExpression(tokens,compileExceptions,generators);
                    parseLogicExpressionP(tokens,compileExceptions,generators);
                    break;
                case OR:
                    generators.add("<logicExpression>'  →       |<logicExpression><logicExpression>'");
                    index++;
                    parseLogicExpression(tokens,compileExceptions,generators);
                    parseLogicExpressionP(tokens,compileExceptions,generators);
                    break;
                case RB:
                case RBR:
                case SEM:
                    generators.add("<logicExpression>'  →       Ɛ");
                    break;
                default:
                    compileExceptions.add(new CompileException(token.getRow(),"invalid logical expression: " + token.getName()));
            }
        }else {
            compileExceptions.add(new CompileException("unfinished logicExpression"));
        }
    }

    public static void parseType(List<Token> tokens, List<CompileException> compileExceptions, List<String> generators){
        if (index < tokens.size()) {
            Token token = tokens.get(index);
            SymbolType type = token.getSymbolType();
            switch (type){
                case INT:
                    generators.add("<type>              →       int");
                    index++;
                    break;
                case DOUBLE:
                    generators.add("<type>              →       double");
                    index++;
                    break;
                case BOOL:
                    generators.add("<type>              →       bool");
                    index++;
                    break;
                default:
                    compileExceptions.add(new CompileException(token.getRow(),"invalid datatype: " + token.getName()));
            }
        }else {
            compileExceptions.add(new CompileException("unfinished assignStatement"));
        }
    }

    public static void parseComparator(List<Token> tokens, List<CompileException> compileExceptions, List<String> generators){
        if (index < tokens.size()) {
            Token token = tokens.get(index);
            SymbolType type = token.getSymbolType();
            switch (type){
                case LT:
                    generators.add("<comparator>        →       <");
                    index++;
                    break;
                case GT:
                    generators.add("<comparator>        →       >");
                    index++;
                    break;
                case GET:
                    generators.add("<comparator>        →       >=");
                    index++;
                    break;
                case LET:
                    generators.add("<comparator>        →       <=");
                    index++;
                    break;
                case NEQ:
                    generators.add("<comparator>        →       !=");
                    index++;
                    break;
                case EQ:
                    generators.add("<comparator>        →       ==");
                    index++;
                    break;
                default:
                    compileExceptions.add(new CompileException(token.getRow(),"invalid comparator: " + token.getName()));
            }
        }else {
            compileExceptions.add(new CompileException("unfinished logicExpression"));
        }
    }
}
