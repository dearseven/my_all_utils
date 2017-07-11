package cc.m2u.xiandao.checkin1.util;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.Calendar;

/**
 * Created by wx on 2017/3/10.
 */

public class Log2File {
    final static boolean  exe=true;
    public static final void log(Context ctx, String userId, String info) {
        try {
            if(!exe)return;
            Calendar c = Calendar.getInstance();
            StringBuilder sb = new StringBuilder();
            sb.append(c.get(Calendar.YEAR)).append("_").append(c.get(Calendar.MONTH) + 1).append("_").append(c.get(Calendar.DAY_OF_MONTH));
            String ymd = sb.toString();
            sb.setLength(0);

            String fileName = ymd + "_" + userId + ".txt";
            String folder = Environment.getExternalStorageDirectory() + "/xdchecklog/";
            File f = new File(folder);
            if (f.exists() == false) {
                f.mkdir();
            }
            File logFile = new File(folder, fileName);
            if (logFile.exists() == false) {
                logFile.createNewFile();
            }
            // 打开一个随机访问文件流，按读写方式
            RandomAccessFile randomFile = new RandomAccessFile(logFile, "rw");
            // 文件长度，字节数
            long fileLength = randomFile.length();
            // 将写文件指针移到文件尾。
            randomFile.seek(fileLength);
            sb.append("\n\n-------------------------------------------------------------------");
            sb.append("\nlog in date and Time:").append(ymd).append(" ").append(c.get(Calendar.HOUR_OF_DAY)).append(":").append(c.get(Calendar.MINUTE)).append(":").append(c.get(Calendar.SECOND));
            sb.append("\n");
            sb.append(info);
            randomFile.writeUTF(new String(sb.toString().getBytes(),"utf-8"));
            randomFile.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
