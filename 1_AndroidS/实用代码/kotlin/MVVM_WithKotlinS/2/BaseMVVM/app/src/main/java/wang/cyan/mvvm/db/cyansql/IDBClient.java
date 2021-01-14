package wang.cyan.mvvm.db.cyansql;

import android.support.annotation.Nullable;

import java.util.List;
import java.util.Map;

/**
 * 其实就是对sqlite做了封装，可以不用总是修改表结构，<br/>
 * 因为nosql其实对结构要求不严格，对于总是喜欢改来改去的数据库合适和使用<br/>
 * 会根据用户的sql对表和字段进行创建，避免了麻烦<br/>
 * Created by wx on 2017/3/26.
 */

public interface IDBClient {
    /**
     * 原生查询
     *
     * @param tableName
     * @param sql           select * from table where a=? and b=?
     * @param selectionArgs new String[]{"fora","forb"};
     * @param colNames      new String[]{"a","b"} 要查询的字段
     * @return
     */
    public List<Map<String, String>> retrieve(String tableName, String sql, String[] selectionArgs, String[] colNames);

    /**
     * 查询，所有字段必须都存在,否则肯定是没有值的啊 对不对
     *
     * @param tableName
     * @param cols
     * @param where
     * @param cause
     * @return
     */
    public List<Map<String, String>> retrieve(String tableName, String[] cols, String where, String[] cause, @Nullable String group, @Nullable String having, @Nullable String order, @Nullable String limit);

    /**
     * 插入数据库
     *
     * @param tableName
     * @param cols
     * @param vals
     * @return
     */
    public boolean insert(String tableName, String[] cols, String[] vals);

    /**
     * 修改数据库，where里的字段必须保证已经存在
     *
     * @param tableName
     * @param cols
     * @param vals
     * @param where
     * @param cause
     * @return
     */
    public boolean update(String tableName, String[] cols, String[] vals, String where, String[] cause);

    /**
     * 删除,where里的字段必须保证已经存在
     *
     * @param tableName
     * @param where
     * @param cause
     * @return
     */
    public boolean delete(String tableName, String where, String[] cause);
}
