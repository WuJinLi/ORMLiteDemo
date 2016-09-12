package come.example.my.ormlitedemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by My on 2016/9/12.
 */
public class MyAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<Student> list;

    public MyAdapter(List<Student> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_listview, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Student student = list.get(position);
        long id = student.getId();
        int age = student.getAge();
        String name = student.getName();
        String sex = student.getSex();
        holder.textview_item_age.setText(age+"");
        holder.textview_item_name.setText(name);
        holder.textview_item_sex.setText(sex);
        holder.textview_item_id.setText(id + "");
        return convertView;
    }

    class ViewHolder {
        public TextView textview_item_name;
        public TextView textview_item_age;
        public TextView textview_item_sex;
        public TextView textview_item_id;

        public ViewHolder(View convertView) {
            textview_item_id = (TextView) convertView.findViewById(R.id.textview_item_id);
            textview_item_name = (TextView) convertView.findViewById(R.id.textview_item_name);
            textview_item_age = (TextView) convertView.findViewById(R.id.textview_item_age);
            textview_item_sex = (TextView) convertView.findViewById(R.id.textview_item_sex);
        }
    }


    //自定义重新加载数据的方法
    public void reLoadList(List<Student> _list, boolean isClear) {
        if (isClear) {
            list.clear();
        }
        list.addAll(_list);
        notifyDataSetChanged();
    }
}
