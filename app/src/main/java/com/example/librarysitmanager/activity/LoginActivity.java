package com.example.librarysitmanager.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.dx.dxloadingbutton.lib.LoadingButton;
import com.example.librarysitmanager.R;
import com.example.librarysitmanager.util.HttpUtil;
import com.example.librarysitmanager.constValue.AcountInfo;
import com.example.librarysitmanager.constValue.AcountStatus;
import com.example.librarysitmanager.constValue.MySharedPreferences;
import com.example.librarysitmanager.constValue.MyURL;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends Activity implements TextWatcher {

    private EditText userNameET;
    private EditText passWordET;
    private LoadingButton loginBn;
    private CheckBox autoLoginBox;
    private ImageView rightArm;
    private ImageView leftArm;
    private ImageView leftHand;
    private ImageView rightHand;
    private Toast toast;
    private final long maxTime=60;
    private SimpleDateFormat format;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
        initOthers();
        new AutoLoginThread(handler).start();

    }
    /*初始化类的成员*/
    //初始化非界面组件
    private void initOthers(){
        //初始化preferences
        format = new SimpleDateFormat(this.getString(R.string.simple_date_format));
        preferences = getSharedPreferences(MySharedPreferences.USER_INFO,MODE_PRIVATE);
        editor = preferences.edit();
        //处理用户界面的相关事件
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch(msg.what){
                    //正在登录
                    case AcountStatus.CHECKING:
                        loginBn.startLoading();
                        break;
                        //登录成功，开启新的界面,结束当前界面
                    case AcountStatus.ON_LINE:
                        loginBn.loadingSuccessful();
                        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                        //登录失败，按钮变化
                    case AcountStatus.NOT_EXIST:
                        loginBn.loadingFailed();
                        toast = Toast.makeText(LoginActivity.this,"账号不存在或者密码错误！",Toast.LENGTH_SHORT);
                        toast.show();
                        break;
                        //超时，按钮变化
                    case AcountStatus.REQUEST_TIME_OUT:
                        loginBn.loadingFailed();
                        toast = Toast.makeText(LoginActivity.this,"连接服务器超时！",Toast.LENGTH_SHORT);
                        toast.show();
                        break;
                }
            }
        };
    }

    //初始化界面控件
    private void initViews(){
        userNameET = findViewById(R.id.et_user);
        passWordET = findViewById(R.id.et_password);
        loginBn = findViewById(R.id.login_btn);
        autoLoginBox = findViewById(R.id.autoLogin);
        leftArm = findViewById(R.id.arm_left);
        rightArm = findViewById(R.id.arm_right);
        leftHand = findViewById(R.id.hand_left);
        rightHand = findViewById(R.id.hand_right);
        loginBn.setEnabled(false);

        //监听内容改变
        userNameET.addTextChangedListener(this);
        passWordET.addTextChangedListener(this);

        //监听EditText的焦点变化
        passWordET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b == true){
                    // 捂住眼睛
                    close();
                }else {
                    //放开
                    open();
                }
            }
        });

        //登录按钮被点击
        loginBn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message message = new Message();
                // 执行输入校验
                String userName = userNameET.getText().toString();
                String passWord = passWordET.getText().toString();
                boolean isAutoLogin = autoLoginBox.isChecked();
                new LoingThread(userName,passWord,isAutoLogin,handler).start();
            }
        });
    }

    /**
     * 键盘
     * 当有控件获得焦点focus 自动弹出键盘
     * 1.点击软键盘的 return键 自动收回键盘
     * 2.代码控制  InputMethodManager
     *    showSoftInput  显示键盘 必须先让这个view成为焦点
     *
     * */

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN){
            //隐藏键盘
            // 1.获取系统输入输出的管理器
            InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            // 2.隐藏键盘
            System.out.println(inputMethodManager.toString());
            inputMethodManager.hideSoftInputFromWindow(userNameET.getWindowToken(), 0);
            // 3.取消焦点
            View focusView = getCurrentFocus();
            if (focusView != null){
                focusView.clearFocus();
            }
        }
        return true;
    }



    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        // 判断两个输入框是否有内容
        if ((userNameET.getText().toString().length()>0) && (passWordET.getText().toString().length()>0)){
            // 按钮可以点击了
            loginBn.setEnabled(true);
            loginBn.setRippleEnable(true);
        }else {
            loginBn.setEnabled(false);
            loginBn.setRippleEnable(false);
        }
    }

    //控制翅膀上升
    private void close(){
        Animation handUpAnimation = AnimationUtils.loadAnimation(this,R.anim.hand_up);
        Animation leftArmUpAnimation = AnimationUtils.loadAnimation(this,R.anim.lef_arm_up);
        Animation rightArmUpAnimation = AnimationUtils.loadAnimation(this,R.anim.right_arm_up);
        handUpAnimation.setFillAfter(true);
        leftArmUpAnimation.setFillAfter(true);
        rightArmUpAnimation.setFillAfter(true);
        leftHand.startAnimation(handUpAnimation);
        rightHand.startAnimation(handUpAnimation);
        leftArm.startAnimation(leftArmUpAnimation);
        rightArm.startAnimation(rightArmUpAnimation);

    }

    //控制翅膀下降
    private void open(){
        Animation handUpAnimation = AnimationUtils.loadAnimation(this,R.anim.hand_down);
        Animation leftArmUpAnimation = AnimationUtils.loadAnimation(this,R.anim.left_arm_down);
        Animation rightArmUpAnimation = AnimationUtils.loadAnimation(this,R.anim.right_arm_down);
        leftHand.startAnimation(handUpAnimation);
        rightHand.startAnimation(handUpAnimation);
        leftArm.startAnimation(leftArmUpAnimation);
        rightArm.startAnimation(rightArmUpAnimation);
    }

    /*用户登录处理*/
    private boolean loginPro(String userName,String passWord) throws Exception
    {
        // 获取用户输入的用户名、密码
        JSONObject jsonObj;
            jsonObj = loginQuery(userName, passWord);
            // 如果userId 大于0
            if (jsonObj.getBoolean("isValid"))
                return true;
            else
                return false;
    }

    // 对用户输入的用户名、密码进行校验
    private boolean validate(String userName, String passWord)
    {
        if (userName.equals(""))
        {
            Toast.makeText(LoginActivity.this,"用户账户是必填项！",Toast.LENGTH_LONG);
            return false;
        }
        if (passWord.equals(""))
        {
            Toast.makeText(LoginActivity.this,"用户口令是必填项！",Toast.LENGTH_LONG);
            return false;
        }
        return true;
    }

    // 定义发送请求的方法
    private JSONObject loginQuery(String username, String password)
            throws Exception
    {
        // 使用Map封装请求参数
        Map<String, String> map = new HashMap<>();
        map.put(AcountInfo.USER_NAME, username);
        map.put(AcountInfo.PASS_WORD, password);
        // 定义发送请求的URL
        String url = MyURL.BASE_URL+MyURL.LOGIN_URL;
        // 发送请求
        return new JSONObject(HttpUtil.postRequest(url, map));
    }

    private JSONObject registQuery(String username, String password,String age)
            throws Exception
    {
        // 使用Map封装请求参数
        Map<String, String> map = new HashMap<>();
        map.put(AcountInfo.USER_NAME, username);
        map.put(AcountInfo.PASS_WORD, password);
        map.put(AcountInfo.AGE,age);
        // 定义发送请求的URL
        String url = MyURL.BASE_URL+MyURL.LOGIN_URL;
        // 发送请求
        return new JSONObject(HttpUtil.postRequest(url, map));
    }

    //自动登录线程
    class AutoLoginThread extends Thread{
        Handler handler;
        public AutoLoginThread(Handler handler){
            this.handler = handler;
        }
        @Override
        public void run() {
            String userName = preferences.getString(AcountInfo.USER_NAME,null);
            String passWord = preferences.getString(AcountInfo.PASS_WORD,null);
            String time = preferences.getString(AcountInfo.TIME,null);
            try{
                Date start = format.parse(time);
                Date now = format.parse(format.format(new Date()));
                if(userName != null && passWord != null && (now.getTime()-start.getTime())/1000<=maxTime){
                    //用户登录过，显示正在登录
                    userNameET.setText(userName);
                    passWordET.setText(passWord);
                    Message lodingMsg = new Message();
                    lodingMsg.what = AcountStatus.CHECKING;
                    handler.sendMessage(lodingMsg);
                    try {
                        if(loginPro(userName,passWord)){
                            //登录成功
                            Message successMsg = new Message();
                            successMsg.what = AcountStatus.ON_LINE;
                            handler.sendMessage(successMsg);
                        }else{
                            //清除preferencs中的信息
                            Message notExistMsg = new Message();
                            editor.clear();
                            notExistMsg.what = AcountStatus.NOT_EXIST;
                            handler.sendMessage(notExistMsg);
                        }
                    }catch (Exception e){
                        //登录超时
                        Message timeOutMsg = new Message();
                        timeOutMsg.what = AcountStatus.REQUEST_TIME_OUT;
                        handler.sendMessage(timeOutMsg);
                       e.printStackTrace();
                    }
                }else {
                    //用户没有登录过
                    Message neverLoginMsg = new Message();
                    neverLoginMsg.what = AcountStatus.NEVER_LOGIN;
                    handler.sendMessage(neverLoginMsg);
                }
            }catch (Exception e){
                //解析用户登录信息失败
                Message failResolutionMsg = new Message();
                failResolutionMsg.what = AcountStatus.NEVER_LOGIN;
                handler.sendMessage(failResolutionMsg);
                e.printStackTrace();
            }
        }
    }


    //用户登录线程
    class LoingThread extends  Thread{
        String userName;
        String passWord;
        boolean isAutoLogin;
        Handler handler;

        public LoingThread(String userName,String passWord,boolean isAutoLogin,Handler handler){
            this.userName = userName;
            this.passWord = passWord;
            this.isAutoLogin = isAutoLogin;
            this.handler = handler;
        }
        @Override
        public void run() {
            if (validate(userName,passWord))
            {
                try{
                    Message lodingMsg = new Message();
                    lodingMsg.what = AcountStatus.CHECKING;
                    handler.sendMessage(lodingMsg);
                    // 如果登录成功
                    if (loginPro(userName,passWord))
                    {
                        if(isAutoLogin){
                            //保存账号信息
                            editor.putString(AcountInfo.USER_NAME,userName);
                            editor.putString(AcountInfo.PASS_WORD,passWord);
                            editor.putString(AcountInfo.TIME,format.format(new Date()));
                            editor.commit();
                        }
                        Message onLineMsg = new Message();
                        onLineMsg.what = AcountStatus.ON_LINE;
                        handler.sendMessage(onLineMsg);
                    }
                    //账号不存在
                    else
                    {
                        Message notExistMsg = new Message();
                        notExistMsg.what = AcountStatus.NOT_EXIST;
                        handler.sendMessage(notExistMsg);
                    }
                }catch (Exception e){
                    //网络问题
                    Message timOutMsg = new Message();
                    timOutMsg.what = AcountStatus.REQUEST_TIME_OUT;
                    handler.sendMessage(timOutMsg);
                    e.printStackTrace();
                }
            }
        }
    }
}