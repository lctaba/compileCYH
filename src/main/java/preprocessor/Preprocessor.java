package preprocessor;


import com.sun.xml.internal.ws.api.model.ExceptionType;
import exception.CompileException;
import exception.CompileExceptionType;

import java.util.List;

/**
 * @Author cyh
 * @Date 2021/6/12 23:17
 */
public class Preprocessor {
    public static String preprocessor(String code, List<CompileException> compileExceptions){
        //去除注释
        int index = 0;
        int lastIndex = 0;
        char tempChar;
        StringBuilder res = new StringBuilder();
        while (index < code.length()){
            tempChar = code.charAt(index);
            if(tempChar == '/'){
                index = addAndSet(index,code,compileExceptions);
                tempChar = code.charAt(index);
                if(tempChar == '/'){
                    res.append(code, lastIndex, index-1);
                    index = addAndSet(index,code,compileExceptions);
                    tempChar = code.charAt(index);
                    while (tempChar != '\n'){
                        index = addAndSet(index,code,compileExceptions);
                        tempChar = code.charAt(index);
                    }
                    lastIndex = index+1;
                }else if(tempChar == '*'){
                    res.append(code, lastIndex, index-1);
                    index = addAndSet(index,code,compileExceptions);
                    if(index == 0){
                        return "";
                    }
                    tempChar = code.charAt(index);
                    boolean flag = true;
                    while (flag){
                        while (tempChar != '*'){
                            index = addAndSet(index,code,compileExceptions);
                            if(index == 0){
                                return "";
                            }
                            tempChar = code.charAt(index);
                        }
                        index = addAndSet(index,code,compileExceptions);
                        if(index == 0){
                            return "";
                        }
                        tempChar = code.charAt(index);
                        if(tempChar == '/'){
                            flag = false;
                            lastIndex = index+1;
                        }
                    }
                }
            }
            index++;
        }
        res.append(code,lastIndex,index);
        return res.toString();
    }

    //不判断多行注释可能会导致下标溢出异常
    public static int addAndSet(int index, String code, List<CompileException> compileExceptions){
        if(index < code.length()-1){
            return index+1;
        }else {
            //return index+1;
            //throw new CommentException();
            compileExceptions.add(new CompileException(CompileExceptionType.CommentError));
            return 0;
        }
    }
}
