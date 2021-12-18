package exception;

/**
 * @Author cyh
 * @Date 2021/6/13 0:49
 */
public class CompileException {
    int row;
    String errorCode;
    CompileExceptionType compileExceptionType;

    public CompileException(int row, String errorCode, CompileExceptionType compileExceptionType) {
        this.row = row;
        this.errorCode = errorCode;
        this.compileExceptionType = compileExceptionType;
    }

    public CompileException(CompileExceptionType compileExceptionType) {
        this.row = 0;
        this.errorCode = "";
        this.compileExceptionType = compileExceptionType;
    }

    public CompileException(int row, CompileExceptionType compileExceptionType) {
        this.row = row;
        this.compileExceptionType = compileExceptionType;
    }

    public CompileException(int row, String errorCode) {
        this.row = row;
        this.errorCode = errorCode;
    }

    public CompileException(String errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String toString() {
        if(compileExceptionType == CompileExceptionType.CommentError){
            return "error:  " + "errorType : CommentError";
        }else if(compileExceptionType == CompileExceptionType.UnresolvedSymbol){
            return "error:  " + "row: " + row + "," + "UnresolvedSymbol: " + errorCode;
        }else if(compileExceptionType == CompileExceptionType.UnMatchedString){
            return "error:  " + "row: " + row + "," + "UnMatchedString: " + errorCode;
        }else {
            return "error:  " + "row: " + row + "," + errorCode;
        }
    }
}
