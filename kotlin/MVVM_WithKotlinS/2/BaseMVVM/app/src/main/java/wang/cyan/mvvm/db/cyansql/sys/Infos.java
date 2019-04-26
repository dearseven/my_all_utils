package wang.cyan.mvvm.db.cyansql.sys;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wx on 2017/3/26.
 */

public class Infos implements Parcelable {
    public static final String tableName = "info_table";

    public static final String colId = "info_col_id";
    public static final String colColletionName = "info_col_names";
    public static final String colCollectionColumns = "info_col_cols";//{};

    public static String makeTable() {
        StringBuilder sb = new StringBuilder();
        sb.append("create table ").append(tableName);
        sb.append(" ( ").append(colId)
                .append(" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, ");
        sb.append(colColletionName).append(" TEXT NOT NULL, ");
        sb.append(colCollectionColumns).append(" TEXT NOT NULL ");
        sb.append(");");
        return sb.toString();
    }


    public int info_col_id;
    public String info_col_names;
    public String info_col_cols;


    protected Infos(Parcel in) {
        info_col_id = in.readInt();
        info_col_names = in.readString();
        info_col_cols = in.readString();
    }

    public static final Creator<Infos> CREATOR = new Creator<Infos>() {
        @Override
        public Infos createFromParcel(Parcel in) {
            return new Infos(in);
        }

        @Override
        public Infos[] newArray(int size) {
            return new Infos[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(info_col_id);
        dest.writeString(info_col_names);
        dest.writeString(info_col_cols);
    }
}
