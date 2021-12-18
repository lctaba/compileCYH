package lexer;

import Symbol.SymbolType;
import Symbol.Token;
import exception.CompileException;
import exception.CompileExceptionType;

import java.util.*;

/**
 * @Author cyh
 * @Date 2021/6/12 22:17
 */
public class Lexer {
    //public static final String[] keyword = {"for","while","do","break","continue","if","else","return","void","int","double","string","bool"};
    public static final Map<String,SymbolType> keyword = new HashMap<String, SymbolType>();
    static {
        keyword.put("for",SymbolType.FOR);
        keyword.put("while",SymbolType.WHILE);
        keyword.put("do",SymbolType.DO);
        keyword.put("break",SymbolType.BREAK);
        keyword.put("continue",SymbolType.CONTINUE);
        keyword.put("if",SymbolType.IF);
        keyword.put("else",SymbolType.ELSE);
        keyword.put("return",SymbolType.RETURN);
        keyword.put("void",SymbolType.VOID);
        keyword.put("int",SymbolType.INT);
        keyword.put("double",SymbolType.DOUBLE);
        keyword.put("string",SymbolType.STRING);
        keyword.put("bool",SymbolType.BOOL);
        keyword.put("true",SymbolType.TRUE);
        keyword.put("false",SymbolType.FALSE);
    }
    public static List<Token> lexer(String code, List<CompileException> compileExceptions){
        List<Token> tokens = new ArrayList<Token>();
        int index = 0;
        char tempChar = '#';
        char nextChar = '#';
        int row = 1;
        while(index<code.length()){
            tempChar = code.charAt(index);
            if(isLetter(tempChar)){
                StringBuilder stringBuilder = new StringBuilder();
                while (isLetter(tempChar) || isNumber(tempChar)){
                    stringBuilder.append(tempChar);
                    index++;
                    tempChar = code.charAt(index);
                }
                index--;
                String id = stringBuilder.toString();
                if(isKeyword(id)){
                    tokens.add(new Token(row,keyword.get(id),id));
                }else {
                    tokens.add(new Token(row,SymbolType.ID,id));
                }
            }else if(isNumber(tempChar)){
                StringBuilder stringBuilder = new StringBuilder();
                while (isNumber(tempChar)){
                    stringBuilder.append(tempChar);
                    index++;
                    tempChar = code.charAt(index);
                }
                if(tempChar != '.'){
                    tokens.add(new Token(row,SymbolType.CONSTINT,stringBuilder.toString()));
                    index--;
                }else{
                    stringBuilder.append(tempChar);
                    index++;
                    tempChar = code.charAt(index);
                    while (isNumber(tempChar)){
                        stringBuilder.append(tempChar);
                        index++;
                        tempChar = code.charAt(index);
                    }
                    tokens.add(new Token(row,SymbolType.CONSTDOUBLE,stringBuilder.toString()));
                    index--;
                }
            }else {
                switch (tempChar){
                    case '\n':
                        row++;
                        break;
                    case ' ':
                    case '\t':
                    case '\r':
                        break;
                    case '(':
                        tokens.add(new Token(row, SymbolType.LB,"("));
                        break;
                    case ')':
                        tokens.add(new Token(row, SymbolType.RB,")"));
                        break;
                    case '{':
                        tokens.add(new Token(row, SymbolType.LBR,"{"));
                        break;
                    case '}':
                        tokens.add(new Token(row, SymbolType.RBR,"}"));
                        break;
                    case ';':
                        tokens.add(new Token(row, SymbolType.SEM,";"));
                        break;
                    case ',':
                        tokens.add(new Token(row, SymbolType.COM,","));
                        break;
                    case '+':
                        tokens.add(new Token(row, SymbolType.PLUS,"+"));
                        break;
                    case '-':
                        tokens.add(new Token(row, SymbolType.SUB,"-"));
                        break;
                    case '*':
                        tokens.add(new Token(row, SymbolType.MUL,"*"));
                        break;
                    case '/':
                        tokens.add(new Token(row, SymbolType.DIV,"/"));
                        break;
                    case '%':
                        tokens.add(new Token(row, SymbolType.MOD,"%"));
                        break;
                    case '&':
                        tokens.add(new Token(row, SymbolType.AND,"&"));
                        break;
                    case '|':
                        tokens.add(new Token(row, SymbolType.OR,"|"));
                        break;
                    case '=':
                        nextChar = code.charAt(index+1);
                        if(nextChar == '='){
                            tokens.add(new Token(row,SymbolType.EQ,"=="));
                            index++;
                        }else{
                            tokens.add(new Token(row,SymbolType.ASSIGN,"="));
                        }
                        break;
                    case '>':
                        nextChar = code.charAt(index + 1);
                        if(nextChar == '='){
                            tokens.add(new Token(row,SymbolType.GET,">="));
                            index++;
                        }else {
                            tokens.add(new Token(row,SymbolType.GT,">"));
                        }
                        break;
                    case '<':
                        nextChar = code.charAt(index+1);
                        if(nextChar == '='){
                            tokens.add(new Token(row,SymbolType.LET,"<="));
                            index++;
                        }else {
                            tokens.add(new Token(row,SymbolType.LT,"<"));
                        }
                        break;
                    case '!':
                        nextChar = code.charAt(index+1);
                        if(nextChar == '='){
                            tokens.add(new Token(row,SymbolType.NEQ,"!="));
                            index++;
                        }else {
                            tokens.add(new Token(row,SymbolType.NOT,"!"));
                        }
                        break;
                    case '"':
                        StringBuilder stringBuilder = new StringBuilder("");
                        index++;
                        tempChar = code.charAt(index);
                        int tempRow = 0;
                        while(tempChar != '"'){
                            if(tempChar == '\n'){
                                tempRow++;
                            }else {
                                stringBuilder.append(tempChar);
                            }
                            index++;
                            if(index<code.length()){
                                tempChar = code.charAt(index);
                            }else {
                                compileExceptions.add(new CompileException(row, stringBuilder.toString(), CompileExceptionType.UnMatchedString));
                                return tokens;
                            }
                        }
                        tokens.add(new Token(row,SymbolType.CONSTSTRING,stringBuilder.toString()));
                        row += tempRow;
                        break;
                    default:
                        compileExceptions.add(new CompileException(row,String.valueOf(tempChar),CompileExceptionType.UnresolvedSymbol));
                        return tokens;
                }
            }
            index++;
        }
        return tokens;
    }


    public static boolean isLetter(char c){
        return (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z');
    }

    public static boolean isNumber(char c){
        return c >= '0' && c <= '9';
    }

    public static boolean isKeyword(String s){
        return keyword.containsKey(s);
    }

    public static boolean isBlank(char c){
        return c == '\t' || c == '\n' || c == '\r' || c == ' ';
    }
//    public static boolean isIdNext(char c){
//        return isBlank(c) || c == '(' || c == ')' || c == '}' || c ==
//    }
}


/*
if(tempChar == '\n' || tempChar == ' ' || tempChar == '\t'){
        row++;
        } else if(tempChar == '('){
        tokens.add(new Token(row, SymbolType.LB,"("));
        }else if(tempChar == ')'){
        tokens.add(new Token(row, SymbolType.RB,")"));
        }else if(tempChar == '{'){
        tokens.add(new Token(row, SymbolType.LBR,"{"));
        }else if(tempChar == '}'){
        tokens.add(new Token(row, SymbolType.RBR,"}"));
        }else if(tempChar == ';'){
        tokens.add(new Token(row, SymbolType.SEM,";"));
        }else if(tempChar == ','){
        tokens.add(new Token(row, SymbolType.COM,","));
        }else if(tempChar == '+'){
        tokens.add(new Token(row, SymbolType.PLUS,"+"));
        }else if(tempChar == '-'){
        tokens.add(new Token(row, SymbolType.SUB,"-"));
        }else if(tempChar == '*'){
        tokens.add(new Token(row, SymbolType.MUL,"*"));
        }else if(tempChar == '/'){
        tokens.add(new Token(row, SymbolType.DIV,"/"));
        }else if(tempChar == '%'){
        tokens.add(new Token(row, SymbolType.MOD,"%"));
        }else if(tempChar == '&'){
        tokens.add(new Token(row, SymbolType.AND,"&"));
        }else if(tempChar == '|'){
        tokens.add(new Token(row, SymbolType.OR,"|"));
        }else if(tempChar == '!'){
        char c = code.charAt(index+1);
        if(c == '='){
        tokens.add(new Token(row,SymbolType.NEQ,"!="));
        index++;
        }else {
        tokens.add(new Token(row,SymbolType.NOT,"!"));
        }
        }else if(tempChar == '='){
        char c = code.charAt(index+1);
        if(c == '='){
        tokens.add(new Token(row,SymbolType.EQ,"=="));
        index++;
        }else{
        tokens.add(new Token(row,SymbolType.ASSIGN,"="));
        }
        }else if(tempChar == '>'){
        char c = code.charAt(index+1);
        if(c == '='){
        tokens.add(new Token(row,SymbolType.GET,">="));
        index++;
        }else {
        tokens.add(new Token(row,SymbolType.GT,">"));
        }
        }else if(tempChar == '<'){
        char c = code.charAt(index+1);
        if(c == '='){
        tokens.add(new Token(row,SymbolType.LET,"<="));
        index++;
        }else {
        tokens.add(new Token(row,SymbolType.LT,"<"));
        }
        }
*/