package GUItil;

public class MyUtil {
    /**
     * 生成指定长度字符串，不足位右补空格
     *
     * @param str
     * @param length
     * @return
     */
    public static String formatStr(String str, int length) {
        int strLen;
        if (str == null) {
            strLen = 0;
        } else {
            strLen = str.getBytes().length;
//            strLen= str.length();
        }
        if (strLen == length) {
            return str;
        } else if (strLen < length) {
            int temp = length - strLen;
            for (int i = 0; i < temp; i += 2) {
                str = " " + str + " ";
            }
            if (str.getBytes().length != length) {
                str = str.substring(1);
            }
            return str;
        } else {
            return str;
        }
    }
}
