package com.example.librarysitmanager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.librarysitmanager.R;

import java.util.ArrayList;

public class MyExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList <String> groupData;
    private ArrayList<ArrayList<String>>itemData;

    public MyExpandableListAdapter(Context context, ArrayList<String> groupData, ArrayList<ArrayList<String>> itemData) {
        this.context = context;
        this.groupData = groupData;
        this.itemData = itemData;
    }

    @Override
    public int getGroupCount() {
        return groupData.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return itemData.get(groupPosition).size();
    }

    @Override
    public ArrayList<String> getGroup(int groupPosition) {
        return itemData.get(groupPosition);
    }

    @Override
    public String getChild(int groupPosition, int childPosition) {
        return itemData.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    //取得用于显示给定分组的视图. 这个方法仅返回分组的视图对象
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        ViewHolderGroup groupHolder;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.floor_item, parent, false);
            groupHolder = new ViewHolderGroup();
            groupHolder.group_name = (TextView) convertView.findViewById(R.id.floor);
            convertView.setTag(groupHolder);
        }else{
            groupHolder = (ViewHolderGroup) convertView.getTag();
        }
        groupHolder.group_name.setText(groupData.get(groupPosition));
        return convertView;
    }

    //取得显示给定分组给定子位置的数据用的视图
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolderItem itemHolder;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.room_item, parent, false);
            itemHolder = new ViewHolderItem();
            itemHolder.item_name = convertView.findViewById(R.id.room);
            convertView.setTag(itemHolder);
        }else{
            itemHolder = (ViewHolderItem) convertView.getTag();
        }
            itemHolder.item_name.setText(itemData.get(groupPosition).get(childPosition));
        return convertView;
    }

    //设置子列表是否可选中
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    private static class ViewHolderGroup{
        private TextView group_name;
    }

    private static class ViewHolderItem{
        private TextView item_name;
    }

}
