package come.example.my.ormlitedemo;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.stmt.query.In;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView listview_main;
    private TextView textview_main_show;
    private MySQLiteOpenHelper openHelper;
    private Dao<Student, Long> studentDao;
    private Context mContext = this;
    private List<Student> list = new ArrayList<>();
    private MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        //调用初始化数据库工具类的方法
        initDBHelper();
        //初始化数据源
        list = selectData();
        //初始化适配器
        myAdapter = new MyAdapter(list, mContext);
        //设置适配器
        listview_main.setAdapter(myAdapter);
        //设置空数据是显示的信息
        listview_main.setEmptyView(textview_main_show);

        //为listView注册上下文菜单
        registerForContextMenu(listview_main);
    }

    private void initDBHelper() {
        //初始化openHelper
        openHelper = MySQLiteOpenHelper.getInstance(mContext);
        try {
            //通过openHelper来初始化操作数据库的对象
            studentDao = openHelper.getDao(Student.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        listview_main = (ListView) findViewById(R.id.listview_main);
        textview_main_show = (TextView) findViewById(R.id.textview_main_show);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //点击菜单进行数据的添加
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_insert:
                //点击添加数据，出现对话框
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setIcon(R.mipmap.ic_launcher);
                builder.setTitle("添加数据");
                //填充自定义对话框的布局
                View view = getLayoutInflater().inflate(R.layout.alert, null);
                //获取文本编辑框中的内容
                final EditText edittext_alert_name = (EditText) view.findViewById(R.id
                        .edittext_alert_name);
                final EditText edittext_alert_sex = (EditText) view.findViewById(R.id
                        .edittext_alert_sex);
                final EditText edittext_alert_age = (EditText) view.findViewById(R.id
                        .edittext_alert_age);
                builder.setView(view);
                //设置确定和取消按钮
                builder.setNegativeButton("取消", null);
                //点击确定按钮将数据存储到数据库中
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //点击确定按钮执行添加数据的操作
                        if (!TextUtils.isEmpty(edittext_alert_sex.getText())
                                && !TextUtils.isEmpty(edittext_alert_age.getText())
                                && !TextUtils.isEmpty(edittext_alert_name.getText())
                                ) {
                            //获取文本编辑框中的信息
                            String age = edittext_alert_age.getText().toString();
                            String sex = edittext_alert_sex.getText().toString();
                            String name = edittext_alert_name.getText().toString();

                            //初始化student对象
                            Student student = new Student();
                            student.setSex(sex);
                            student.setAge(Integer.parseInt(age));
                            student.setName(name);
                            try {
                                //执行向数据库添加数据的操作
                                int count = studentDao.create(student);
                                if (count > 0) {
                                    myAdapter.reLoadList(selectData(), true);
                                } else {
                                    Toast.makeText(mContext, "添加失败", Toast.LENGTH_SHORT).show();
                                }
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(mContext, "添加数据不能为空", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    //初始化上下文菜单
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo
            menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        //获取AdapterContextMenuInfo对象
        AdapterView.AdapterContextMenuInfo contextMenuInfo = (AdapterView.AdapterContextMenuInfo)
                menuInfo;
        //获取点击item的position
        int position = contextMenuInfo.position;
        //设置上下文菜单的头视图
        menu.setHeaderIcon(R.mipmap.ic_launcher);
        //设置上下文菜单的标题
        menu.setHeaderTitle(list.get(position).getName());
        //加载上下文菜单布局
        getMenuInflater().inflate(R.menu.menu_context, menu);

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        //获取contextMenuInfo对象
        AdapterView.AdapterContextMenuInfo contextMenuInfo = (AdapterView.AdapterContextMenuInfo)
                item.getMenuInfo();
        //获取当前点击条目在list中的位置
        final int position = contextMenuInfo.position;
        switch (item.getItemId()) {
            case R.id.action_delete:
                try {
                    //删除点击的item
                    int delete = studentDao.delete(list.get(position));
                    if (delete > 0) {
                        //刷新数据
                        myAdapter.reLoadList(selectData(), true);
                    } else {
                        Toast.makeText(mContext, "删除数据失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            //对点击item的数据进行修改
            case R.id.action_update:
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setIcon(R.mipmap.ic_launcher);
                View view = getLayoutInflater().inflate(R.layout.alert, null);
                final EditText edittext_alert_name = (EditText) view.findViewById(R.id
                        .edittext_alert_name);
                final EditText edittext_alert_sex = (EditText) view.findViewById(R.id
                        .edittext_alert_sex);
                final EditText edittext_alert_age = (EditText) view.findViewById(R.id
                        .edittext_alert_age);

                //显示对话框，并将原有的数据展示在文本编辑框上
                Student student = list.get(position);
                edittext_alert_name.setText(student.getName());
                edittext_alert_age.setText(student.getAge() + "");
                edittext_alert_sex.setText(student.getSex());

                builder.setView(view);
                builder.setNegativeButton("取消", null);
                //对已有数据进行修改，并且保存到数据库中
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //点击确定按钮执行添加数据的操作
                        if (!TextUtils.isEmpty(edittext_alert_sex.getText())
                                && !TextUtils.isEmpty(edittext_alert_age.getText())
                                && !TextUtils.isEmpty(edittext_alert_name.getText())
                                ) {
                            String age = edittext_alert_age.getText().toString();
                            String sex = edittext_alert_sex.getText().toString();
                            String name = edittext_alert_name.getText().toString();

                            Student student1 = new Student();
                            long id = list.get(position).getId();
                            student1.setId(id);
                            student1.setName(name);
                            student1.setAge(Integer.parseInt(age));
                            student1.setSex(sex);
                            try {
                                int count = studentDao.update(student1);
                                if (count > 0) {
                                    myAdapter.reLoadList(selectData(), true);
                                } else {
                                    Toast.makeText(mContext, "更新失败", Toast.LENGTH_SHORT).show();
                                }
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(mContext, "填写项不可以为空！", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.show();
                break;
        }
        return super.onContextItemSelected(item);
    }

    //获取数据库当前的数据
    private List<Student> selectData() {
        try {
            return studentDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
