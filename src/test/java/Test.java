import Symbol.Token;
import exception.CompileException;
import fileUtil.FileUtil;
import lexer.Lexer;
import parser.Parser;
import preprocessor.Preprocessor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author cyh
 * @Date 2021/6/12 23:35
 */

public class Test {
    @org.junit.Test
    public void removeComment() throws IOException{
        String code = FileUtil.getcode("D:/javaproject/compilerCYH/src/main/java/fileUtil/test.txt");
        System.out.println(code);
        List<CompileException> compileExceptions = new ArrayList<CompileException>();
        String after = Preprocessor.preprocessor(code,compileExceptions);
        System.out.println(after);
    }

    @org.junit.Test
    public void lexer() throws IOException {
        String code = FileUtil.getcode("D:/javaproject/compilerCYH/src/main/java/fileUtil/test.txt");
        System.out.println(code);
        List<CompileException> compileExceptions = new ArrayList<CompileException>();
        String after = Preprocessor.preprocessor(code,compileExceptions);
        if(!compileExceptions.isEmpty()){
            for (CompileException compileException : compileExceptions){
                System.out.println(compileException.toString());
            }
            return;
        }
        List<Token> tokens = Lexer.lexer(after,compileExceptions);
        for (Token token:tokens){
            System.out.println(token.toString());
        }
        for (CompileException compileException : compileExceptions){
            System.out.println(compileException.toString());
        }
    }

    @org.junit.Test
    public void parse() throws IOException {
        String code = FileUtil.getcode("D:/javaproject/compilerCYH/src/main/java/fileUtil/test.txt");
        List<CompileException> compileExceptions = new ArrayList<CompileException>();
        String after = Preprocessor.preprocessor(code,compileExceptions);
        if(!compileExceptions.isEmpty()){
            for (CompileException compileException : compileExceptions){
                System.out.println(compileException.toString());
            }
            return;
        }
        List<Token> tokens = Lexer.lexer(after,compileExceptions);
        if(compileExceptions.size() == 0){
            List<String> generators = Parser.parse(tokens,compileExceptions);
            if(compileExceptions.size() == 0){
                for(String s:generators){
                    System.out.println(s);
                }
            }else {
                for (CompileException compileException : compileExceptions){
                    System.out.println(compileException.toString());
                }
            }
        }else {
            for (CompileException compileException : compileExceptions){
                System.out.println(compileException.toString());
            }
        }

//        for (Token token:tokens){
//            System.out.println(token.toString());
//        }
//        for (CompileException compileException : compileExceptions){
//            System.out.println(compileException.toString());
//        }
    }
}


