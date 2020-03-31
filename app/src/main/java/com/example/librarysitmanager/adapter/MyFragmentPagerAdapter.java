package com.example.librarysitmanager.adapter;

import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.librarysitmanager.activity.MainActivity;
import com.example.librarysitmanager.fragment.HistoryBookFragment;
import com.example.librarysitmanager.fragment.PersonalCenterFragment;
import com.example.librarysitmanager.fragment.RoomFragment;
import com.example.librarysitmanager.fragment.ScanFragment;

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
    private final int PAGER_COUNT = 4;
    //定义界面组件
    private HistoryBookFragment historyBookFragment = null;
    private RoomFragment roomFragment = null;
    private ScanFragment scanFragment = null;
    private PersonalCenterFragment personalCenterFragment = null;
    //初始化界面组件
    public MyFragmentPagerAdapter(FragmentManager fm){
        super(fm);
        historyBookFragment = new HistoryBookFragment();
        roomFragment = new RoomFragment();
        scanFragment = new ScanFragment();
        personalCenterFragment = new PersonalCenterFragment();
    }

    @Override
    public int getCount() {
        return PAGER_COUNT;
    }

    @Override
    public Object instantiateItem(ViewGroup vg, int position) {
        return super.instantiateItem(vg, position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        System.out.println("position Destory" + position);
        super.destroyItem(container, position, object);
    }

    //决定ViewPager中的fragment
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case MainActivity.PAGE_ONE:
                fragment = roomFragment;
                break;
            case MainActivity.PAGE_TWO:
                fragment = scanFragment;
                break;
            case MainActivity.PAGE_THREE:
                fragment = historyBookFragment;
                break;
            case MainActivity.PAGE_FOUR:
                fragment = personalCenterFragment;
                break;
        }
        return fragment;
    }

}
