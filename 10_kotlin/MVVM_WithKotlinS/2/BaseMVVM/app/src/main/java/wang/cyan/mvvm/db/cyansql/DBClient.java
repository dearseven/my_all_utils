package wang.cyan.mvvm.db.cyansql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wang.cyan.mvvm.db.cyansql.sys.Infos;


/**
 * 查询时，默认表和字段存在，update和add的时候，会自动创建不存在的字段和表
 * Created by wx on 2017/3/26.
 */

public class DBClient extends SQLiteOpenHelper implements IDBClient {
    private static String NAME = "intelliCyan_DB";
    /**
     * 每张表都有一个字段叫做___lastModifyDateTime
     */
    public static String ___lastModifyDateTime = "___lastModifyDateTime";


    public static DBClient getInstance(Context ctx) {
        if (_t == null) {
            synchronized (DBClient.class) {
                if (_t == null)
                    _t = new DBClient(ctx, NAME, null, DB_VERSION);
            }
        }
        return _t;
    }

    private SQLiteDatabase getWritableDb() {
        return getWritableDatabase();
    }

    private SQLiteDatabase getReadableDb() {
        return getReadableDatabase();
    }

    private static final int DB_VERSION = 1;
    private static DBClient _t = null;


    public List<Map<String, String>> retrieve(String tableName, String sql, String[] selectionArgs, String[] colNames) {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        Cursor cs = null;
        SQLiteDatabase db = getReadableDb();
        try {
            if (!ifTableExist(db, tableName)) {
                throw new NotSuchATableException();
            }
            cs = db.rawQuery(sql, selectionArgs);
            while (cs.moveToNext()) {
                Map<String, String> m = new HashMap<String, String>();
                if (colNames != null) {
                    for (int i = 0; i < colNames.length; i++) {
                        m.put(colNames[i], cs.getString(cs.getColumnIndex(colNames[i])));
                    }
                } else {
                    int colNumbers = cs.getColumnCount();
                    for (int i = 0; i < colNumbers; i++) {
                        m.put(cs.getColumnName(i), cs.getString(cs.getColumnIndex(cs.getColumnName(i))));
                    }
                }
                list.add(m);
            }
        } catch (NotSuchATableException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cs != null)
                cs.close();
            db.close();
        }
        return list;
    }

    public List<Map<String, String>> retrieve(String tableName, String[] cols, String where, String[] cause, @Nullable String group, @Nullable String having, @Nullable String order, @Nullable String limit) {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        Cursor cs = null;
        SQLiteDatabase db = getReadableDb();
        try {
            if (!ifTableExist(db, tableName)) {
                throw new NotSuchATableException();
            }
            cs = db.query(tableName, cols, where, cause, group, having, order, limit);
            while (cs.moveToNext()) {
                Map<String, String> m = new HashMap<String, String>();
                if (cols != null) {
                    for (int i = 0; i < cols.length; i++) {
                        m.put(cols[i], cs.getString(cs.getColumnIndex(cols[i])));
                    }
                } else {
                    int colNumbers = cs.getColumnCount();
                    for (int i = 0; i < colNumbers; i++) {
                        m.put(cs.getColumnName(i), cs.getString(cs.getColumnIndex(cs.getColumnName(i))));
                    }
                }
                list.add(m);
            }
        } catch (NotSuchATableException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cs != null)
                cs.close();
            db.close();
        }
        return list;
    }

    public boolean insert(String tableName, String[] cols, String[] vals) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            StringBuilder sb = new StringBuilder();
            compareCols(db, tableName, cols);
            ContentValues cv = new ContentValues();
            sb.append("{");
            for (int i = 0; i < cols.length; i++) {
                cv.put(cols[i], vals[i]);
                sb.append("\"").append(cols[i]).append("\":\"").append(vals[i]).append("\"");
                sb.append(",");
            }
            cv.put(___lastModifyDateTime, System.currentTimeMillis());
            sb.append("\"___lastModifyDateTime\":").append(System.currentTimeMillis());
            sb.append("}");
            db.insert(tableName, null, cv);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        } finally {
            db.close();
        }
    }

    public boolean update(String tableName, String[] cols, String[] vals, String where, String[] cause) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            compareCols(db, tableName, cols);
            StringBuilder sb = new StringBuilder();
            ContentValues cv = new ContentValues();
            for (int i = 0; i < cols.length; i++) {
                cv.put(cols[i], vals[i]);
                sb.append("\"").append(cols[i]).append("\":\"").append(vals[i]).append("\"");
                sb.append(",");
            }
            cv.put(___lastModifyDateTime, System.currentTimeMillis());
            sb.append("\"___lastModifyDateTime\":").append(System.currentTimeMillis());
            sb.append("}|where=");
            sb.append(where);
            if (cause != null)
                sb.append("|cause=").append(Arrays.toString(cause));
            else
                sb.append("|cause=").append("[]");
            db.update(tableName, cv, where, cause);
            return true;
        } catch (Exception ex) {
            return false;
        } finally {
            db.close();
        }
    }

    public boolean delete(String tableName, String where, String[] cause) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            if (!ifTableExist(db, tableName)) {
                throw new NotSuchATableException();
            }
            int rows = db.delete(tableName, where, cause);
            return true;
        } catch (NotSuchATableException ex) {
            return false;
        } catch (Exception ex) {
            return false;
        } finally {
            db.close();
        }
    }


    private DBClient(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Infos.makeTable());

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //-----------下面的方法是用来多数据库结构判定的--------------
    private class NotSuchATableException extends Exception {

    }

    /**
     * 在查询和删除的时候检查表是否存在
     *
     * @param db
     * @param tableName
     * @return
     */
    public boolean ifTableExist(SQLiteDatabase db, String tableName) {
        Cursor csr = null;
        try {
            //先找看看表存在还是不存在
            csr = db.query(Infos.tableName, null, Infos.colColletionName + " = ?", new String[]{tableName}, null, null, null);
            if (csr.getCount() == 0) //表不存在
                return false;
            else
                return true;
        } catch (Exception ex) {
            return false;
        } finally {
            if (csr != null) csr.close();
        }
    }


    /**
     * 对比字段
     *
     * @param db
     * @param tableName
     * @param cols      要插入数据的字段
     */
    public void compareCols(SQLiteDatabase db, String tableName, String[] cols) {
        Cursor csr = null;
        try {
            //先找看看表存在还是不存在
            csr = db.query(Infos.tableName, null, Infos.colColletionName + " = ?", new String[]{tableName}, null, null, null);
            if (csr.getCount() == 0) {//表不存在
                //先创建表
                StringBuilder sb = new StringBuilder();
                sb.append("create table ").append(tableName);
                sb.append(" ( ").append("objKeyId")
                        .append(" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT ");
                sb.append(",___lastModifyDateTime bigint not null");
                sb.append(");");
                db.execSQL(sb.toString());
                sb.setLength(0);

                ContentValues cv = new ContentValues();
                cv.put(Infos.colColletionName, tableName);
                cv.put(Infos.colCollectionColumns, "");
                db.insert(Infos.tableName, null, cv);
            }
            //判断字段
            //找到已有的字段
            String columns = null;
            Map<String, Byte> existCols = new HashMap<String, Byte>(csr.getCount());
            while (csr.moveToNext()) {
                columns = csr.getString(csr.getColumnIndex(Infos.colCollectionColumns));
                String[] ec = columns.trim().split(",");
                for (int i = 0; i < ec.length; i++) {
                    existCols.put(ec[i], new Byte((byte) 1));
                }
            }
            //如果cols不在existCols里面则需要创建
            boolean addedNewCol = false;
            for (int i = 0; i < cols.length; i++) {
                if (!existCols.containsKey(cols[i])) {
                    addedNewCol = true;
                    db.execSQL("ALTER TABLE " + tableName
                            + " ADD COLUMN " + cols[i] + " text not null default ''; ");
                    if (columns != null)
                        columns += "," + cols[i];
                    else
                        columns = cols[i];
                }
            }
            if (addedNewCol) {
                ContentValues cv = new ContentValues();
                cv.put(Infos.colCollectionColumns, columns);
                db.update(Infos.tableName, cv, Infos.colColletionName + " = ?", new String[]{tableName});
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (csr != null && csr.isClosed() == false) {
                csr.close();
                csr = null;
            }
        }
    }

}
