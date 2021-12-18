package fileUtil;


import java.io.*;

/**
 * @Author cyh
 * @Date 2021/6/12 23:42
 */

//测试用例
public class FileUtil {
    public static String getcode(String path) throws IOException {
        File file = new File(path);
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        StringBuilder stringBuilder = new StringBuilder();
        String s = "";
        while ((s=bufferedReader.readLine()) != null){
            stringBuilder.append(s).append('\n');
        }
        bufferedReader.close();
        return stringBuilder.toString();
    }
}
