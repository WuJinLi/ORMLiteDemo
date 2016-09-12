# ORMLiteDemo
ORMLite对数据库的操作

#OrmLite 是一个轻量级的ORM（Object Relational Mapping）数据库工具，方便持久化java对象到数据库
##1. 使用准备
   导包compile 'com.j256.ormlite:ormlite-android:5.0'
##2. 创建测试表
  OrmLite在每个类的顶部使用@DatabaseTable标识一个表，使用@DatabaseField标识每个字段。需要注意的是OrmLite需要一个空的构造函数
  代码入下：
  /创建数据库的表名的创建
@DatabaseTable(tableName = "tb_student")
public class Student {
    //对数据库的列名的创建columnName为列名，dataType为存入数据库的数据的类型
    //generatedId数据为自增长
    @DatabaseField(columnName = "_id", generatedId = true)
    private long id;

    @DatabaseField(columnName = "age", dataType = DataType.INTEGER)
    private int age;

    @DatabaseField(columnName = "sex", dataType = DataType.STRING)
    private String sex;

    @DatabaseField(columnName = "name", dataType = DataType.STRING)
    private String name;
##3. 实现DatabaseHelper
  自定义的DatabaseHelper需要继承OrmLiteSqliteOpenHelper，和继承SQLiteOpenHelper的方法一样，需要实现onCreate和onUpgrade。同时可以提供DAO的get方法方便其他类使用。
  示例如下：
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
##4进行数据库的增删改查操作：
   queryForAll()查询  list<T>
   create(T data)增加  int
   delete(T data)删除  int
   update(T data)修改  int
