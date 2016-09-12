package come.example.my.ormlitedemo;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 实体类的创建，ormlite利用注解的方法实现对数据库表名和列名的创建
 */

//创建数据库的表名的创建
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

    public Student() {
    }

    public Student(long id, int age, String sex, String name) {
        this.id = id;
        this.age = age;
        this.sex = sex;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
