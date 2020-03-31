package com.example.librarysitmanager.activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import com.example.librarysitmanager.adapter.MyFragmentPagerAdapter;
import com.example.librarysitmanager.R;
import com.example.librarysitmanager.constValue.MySharedPreferences;
import com.example.librarysitmanager.util.StatusBarUtil;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.common.Constant;

import rx.functions.Action1;

public class MainActivity extends AppCompatActivity {
    public static final int PAGE_ONE = 0;
    public static final int PAGE_TWO = 1;
    public static final int PAGE_THREE = 2;
    public static final int PAGE_FOUR = 3;
    public static final int SCANNIN_GREQUEST_CODE = 0;
    private RadioGroup tapRadioGrop;
    private RadioButton seatBookRBn;
    private RadioButton scanRBn;
    private RadioButton historyRBn;
    private RadioButton personCenRBn;
    private ImageButton scanBn;
    private ViewPager fragPage;
    private MyFragmentPagerAdapter myFragmentPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //设置导航栏颜色
        StatusBarUtil.setStatusBarMode(this,true, R.color.bg_gray);
        //创建adapter
        myFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        //初始化界面控件
        initView();
        //设置第一个tap为点击状态
        seatBookRBn.setChecked(true);
    }

    private void initView(){
        /*绑定界面组件*/
        tapRadioGrop = findViewById(R.id.rg_tab_bar);
        seatBookRBn = findViewById(R.id.rb_book);
        scanRBn = findViewById(R.id.rb_scan);
        historyRBn = findViewById(R.id.rb_history);
        personCenRBn = findViewById(R.id.rb_me);
        fragPage = findViewById(R.id.frag_page);
        scanBn = findViewById(R.id.tp_top_scan);
        //为viewPager设置adpter，确定页面填充哪些内容
        fragPage.setAdapter(myFragmentPagerAdapter);
        //设置当前界面为第一个fragment
        fragPage.setCurrentItem(PAGE_ONE);
        /*设置事件监听器*/
        //为tap添加事件监听器
        tapRadioGrop.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId){
                    case R.id.rb_book: fragPage.setCurrentItem(PAGE_ONE); break;
                    case R.id.rb_scan: fragPage.setCurrentItem(PAGE_TWO); break;
                    case R.id.rb_history: fragPage.setCurrentItem(PAGE_THREE); break;
                    case R.id.rb_me: fragPage.setCurrentItem(PAGE_FOUR); break;
                }
            }
        });
        //为viewPage设置事件监听器
        fragPage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if(state == 2){
                    switch (fragPage.getCurrentItem()){
                        case PAGE_ONE: seatBookRBn.setChecked(true); break;
                        case PAGE_TWO: scanRBn.setChecked(true); break;
                        case PAGE_THREE: historyRBn.setChecked(true); break;
                        case PAGE_FOUR: personCenRBn.setChecked(true); break;
                    }
                }
            }
        });

        scanBn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED)
                    //没有权限则申请权限
                    RxPermissions.getInstance(MainActivity.this)
                        .request(Manifest.permission.CAMERA)//这里填写所需要的权限
                        .subscribe(new Action1<Boolean>() {
                            @Override
                            public void call(Boolean aBoolean) {
                                if (aBoolean) {
                                    Log.i("permissions", Manifest.permission.CAMERA + "：" + "获取成功");
                                    Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
                                    startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
                                } else {
                                    Log.i("permissions", Manifest.permission.CAMERA + "：" + "获取失败");
                                }
                            }
                        });
                else{
                    //有权限打开扫码页面
                    Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
                    startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
                }
            }
        });
    }

    //扫描后进行的操作
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 扫描二维码/条码回传
        if (requestCode == SCANNIN_GREQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {

                String content = data.getStringExtra(Constant.CODED_CONTENT);
                Toast toast = Toast.makeText(MainActivity.this, "扫描结果为"+content,Toast.LENGTH_LONG);
                toast.show();
            }
        }
    }


    private void logout(){
        //清空preferences中的内容
        SharedPreferences preferences = getSharedPreferences(MySharedPreferences.USER_INFO,MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
        //开启log inActivity；
        Intent intent = new Intent(MainActivity.this,LoginActivity.class);
        startActivity(intent);
        //关闭当前activity
        finish();
    }
}
