package com.example.librarysitmanager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.librarysitmanager.Entity.Person;
import com.example.librarysitmanager.R;
import com.example.librarysitmanager.util.ViewHolder;

import java.util.List;

public class MyAdapter extends BaseAdapter {

    private List<Person> people;
    private Context context;
    private static final int PAGER_ONE = 0;
    public MyAdapter(List<Person> people, Context context) {
        this.people = people;
        this.context = context;
    }

    @Override
    public int getCount() {
        return people.size();
    }

    @Override
    public Object getItem(int position) {
        return people.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null ){
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item,parent,false);
            holder = new ViewHolder();
            holder.setName((TextView) convertView.findViewById(R.id.name));
            holder.setSay((TextView)convertView.findViewById(R.id.say));
            convertView.setTag(convertView);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.getName().setText(people.get(position).getName());
        holder.getSay().setText(people.get(position).getSay());
        return convertView;
    }

}
