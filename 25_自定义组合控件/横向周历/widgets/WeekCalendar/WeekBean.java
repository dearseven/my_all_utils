
import java.util.Arrays;

/**
 * 周历对象
 * Created by wx on 2018/3/13.
 */

public class WeekBean {
    private int y;
    private int m;
    //
    private Integer[] days = {0, 0, 0, 0, 0, 0, 0};

    public int getM() {
        return m;
    }

    public int getY() {
        return y;
    }

    public Integer[] getDays() {
        return days;
    }

    public void setM(int m) {
        this.m = m;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setDays(int index, int date) {
        days[index] = date;
    }

    @Override
    public String toString() {
        return "WeekBean{" +
                "y=" + y +
                ", m=" + m +
                ", days=" + Arrays.toString(days) +
                '}';
    }
}
