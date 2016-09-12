package come.example.my.ormlitedemo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ListView;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by My on 2016/9/12.
 */
public class MySQLiteOpenHelper extends OrmLiteSqliteOpenHelper {

    private static final String DB_NAME = "db_student.db";
    private static final int VERSION = 1;
    private static MySQLiteOpenHelper dbHelper;
    private Dao<Student, Long> studentDao;

    //创建数据库
    public MySQLiteOpenHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }


    //单里获取数据库工具类
    public static MySQLiteOpenHelper getInstance(Context context) {
        if (dbHelper == null) {
            dbHelper = new MySQLiteOpenHelper(context);
        }
        return dbHelper;
    }

    //获取表的访问控制对象
    public Dao<Student, Long> getStudentDao() throws SQLException {
        if (studentDao == null) {
            studentDao = getDao(Student.class);
        }
        return studentDao;
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            //TableUtils为ormlite提供的工具类
            //connectionSource为连接资源，Student.class为创建数据库所映射的实体类及本实例中的Student.class
            TableUtils.createTableIfNotExists(connectionSource, Student.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //数据库的更新
    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int
            oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            try {
                TableUtils.dropTable(connectionSource, Student.class, true);
                onCreate(database, connectionSource);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
