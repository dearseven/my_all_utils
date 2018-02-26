
import java.util.regex.Pattern;

/**
 * Created by wx on 2017/7/28.
 */
public class Regex {
    /**
     * 11位数字，并且1开头
     *
     * @param longStr
     * @return
     */
    public static boolean checkNumStartWith1Length11(String longStr) {
        String numStr = longStr;

        String pattern = "^1[\\d]{10}";

        boolean isMatch = Pattern.matches(pattern, numStr);
        return isMatch;
    }
}
