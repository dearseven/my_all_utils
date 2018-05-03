package uex;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by wx on 2017/6/13.
 */

public class InsertUncatchedException {
    private static final String SERVER_URL = "http://1*.2*.2*.1*:8888/CollectUncatched/";
    private static String app = "lottery";

    public static void insert(String exstr) {
        String methodName = "_catch";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(new Date());
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);

        StringBuilder sb = new StringBuilder();
        sb.append("_app=").append(app).append("&_date=").append(date).append("&_hour=").append(hour).append("&_exstr=")
                .append(exstr);
        ParameterSigner.exec(sb);
        //System.out.println(SERVER_URL + methodName + "?" + sb);
        SimpleHttp.Result rs = SimpleHttp.post(SERVER_URL + methodName, sb.toString());
        //System.out.println(rs);
    }
}
