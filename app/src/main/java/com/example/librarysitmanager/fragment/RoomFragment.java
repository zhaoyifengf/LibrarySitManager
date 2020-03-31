package com.example.librarysitmanager.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.example.librarysitmanager.adapter.MyExpandableListAdapter;
import com.example.librarysitmanager.R;

import java.util.ArrayList;


public class RoomFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_room, container, false);
        String floor[] = {"1楼","2楼"};
        String room[][]  = {{"A","B"},{"A","B","C"}};
        final ArrayList<String> floors = new ArrayList<String>();
        final ArrayList<ArrayList<String>> rooms = new ArrayList<ArrayList<String>>();
        for (int i = 0; i < floor.length; i++){
            floors.add(floor[i]);
            ArrayList<String> floorRoom = new ArrayList<String>();
            for (int j=0; j<room[i].length; j++){
                floorRoom.add(room[i][j]);
            }
            rooms.add(floorRoom);
        }
        MyExpandableListAdapter myExpandableListAdapter = new MyExpandableListAdapter(getContext(),floors,rooms);
        ExpandableListView listView = view.findViewById(R.id.list);
        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long l) {
                Toast toast = Toast.makeText(getActivity(),"你点击了"
                        +floors.get(groupPosition)
                        +rooms.get(groupPosition).get(childPosition),Toast.LENGTH_LONG);
                toast.show();
                return true;
            }
        });
        listView.setAdapter(myExpandableListAdapter);
        return view;

    }
}
