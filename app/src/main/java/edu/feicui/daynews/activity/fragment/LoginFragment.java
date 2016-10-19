package edu.feicui.daynews.activity.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import edu.feicui.daynews.R;
import edu.feicui.daynews.SeverUrl;
import edu.feicui.daynews.Util.MyVolley;
import edu.feicui.daynews.activity.HomeActivity;
import edu.feicui.daynews.activity.UserInfoActivity;
import edu.feicui.daynews.entity.RegisterResponse;

/**
 * Created by Administrator on 2016/10/11.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {
    /**
     * 加载该Fragment的Activity
     */
    HomeActivity homeActivity;
    /**
     * 碎片管理
     */
    FragmentManager fragManager;
    /**
     * 用户昵称
     */
    EditText edtName;
    /**
     * 用户密码
     */
    EditText edtPsw;
    /**
     * 注册按钮
     */
    Button mBtnRegister;
    /**
     * 忘记密码按钮
     */
    Button mBtnForgetPsw;
    /**
     * 登录按钮
     */
    Button mBtnLoad;

    /**
     * 此碎片
     */
    View view;

    /**
     * 存储小数据的SharedPreferences
     */
    SharedPreferences preferences;

    /**
     * 登陆界面记住密码的CheckBox
     */
    CheckBox mChBox;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login, container, false);
        inti();

        return view;
    }

    private void inti() {
        /**
         * 初始化SharedPreferences的对象
         */
        preferences=homeActivity.getSharedPreferences("preferences_remember", Context.MODE_PRIVATE);
        /**
         * 获取加载此碎片的Activity
         */
        homeActivity = (HomeActivity) getActivity();
        /**
         * 获取碎片管理对象
         */
        fragManager = homeActivity.getSupportFragmentManager();
        /**
         * 找到此碎片上的各个控件
         */
        edtName = (EditText) view.findViewById(R.id.edt_login_fragment_name);
        edtPsw = (EditText) view.findViewById(R.id.edt_login_fragment_pwd);
        mBtnRegister = (Button) view.findViewById(R.id.btn_login_fragment_regist);
        mBtnForgetPsw = (Button) view.findViewById(R.id.btn_login_fragment_forget);
        mBtnLoad = (Button) view.findViewById(R.id.btn_login_fragment_login);
        mChBox= (CheckBox) view.findViewById(R.id.ch_remember);


        edtName.setText( preferences.getString("account","none"));
        edtPsw.setText(preferences.getString("psw","none"));
        mChBox.setChecked(preferences.getBoolean("isChecked",true));

        homeActivity.setOnClick(this, mBtnRegister, mBtnForgetPsw, mBtnLoad);
    }

//    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//    }

    /**
     * 点击登陆按钮之后的响应
     */
    private void getLoadHttp(){
        Map<String, String> params = new HashMap<>();
        //ver=版本号&uid=用户名&pwd=密码&device=0
        params.put("ver", "0000000");
        params.put("uid", edtName.getText().toString());
        params.put("uid", edtPsw.getText().toString());
        params.put("device", "0");
        MyVolley.get(getContext(), SeverUrl.USER_LOGIN, params, new MyVolley.OnVolleyResult() {
            Gson gson = new Gson();

            @Override
            public void success(String s) {
                RegisterResponse registerResponse = gson.fromJson(s, RegisterResponse.class);
                if(registerResponse.data.result==0){
                    Intent intent=new Intent(homeActivity, UserInfoActivity.class);

                    if(mChBox.isChecked()){
                        SharedPreferences.Editor editor=preferences.edit();
                        editor.putBoolean("isChecked",mChBox.isChecked());//有问题
                        editor.putString("account",edtName.getText().toString());
                        editor.putString("psw",edtPsw.getText().toString());
                        editor.commit();
                    }




                    intent.putExtra("regist_response",registerResponse);
                    startActivity(intent);

                    Toast.makeText(getContext(),"登陆成功",Toast.LENGTH_SHORT).show();
                }
                if(registerResponse.data.result==1){
                    Toast.makeText(getContext(),"用户名或密码错误",Toast.LENGTH_SHORT).show();
                }
                if(registerResponse.data.result==2){
                    Toast.makeText(getContext(),"限制登陆(禁言,封IP)",Toast.LENGTH_SHORT).show();
                }
                if(registerResponse.data.result==3){
                    Toast.makeText(getContext(),"限制登陆(异地登陆等异常)",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failed(VolleyError volleyError) {
                Toast.makeText(getContext(),"网络连接失败",Toast.LENGTH_SHORT).show();

            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login_fragment_regist://跳转到注册的Fragment
                FragmentTransaction transactionRegist = fragManager.beginTransaction();
                transactionRegist.replace(R.id.ll_home_fg, new RegistFragment());
                transactionRegist.commit();
                break;
            case R.id.btn_login_fragment_forget://跳转到忘记密码的Fragment
                FragmentTransaction transactionForget = fragManager.beginTransaction();
                transactionForget.replace(R.id.ll_home_fg, new ForgetPassFragment());
                transactionForget.commit();
                break;
            case R.id.btn_login_fragment_login://跳转到用户中心
                getLoadHttp();
                break;

        }
    }
}
