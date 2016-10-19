package edu.feicui.daynews.activity.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import edu.feicui.daynews.entity.RegisterResponse;

/**
 * Created by Administrator on 2016/10/10.
 */
public class RegistFragment extends Fragment implements View.OnClickListener {
    /**
     * 该碎片
     */
    View mView;
    /**
     * 碎片管理
     */
    FragmentManager mFragmentManager;
    /**
     * 加载该FragMent的Activity
     */
    HomeActivity mHomeActivity;
    /**
     * 邮箱地址
     */
    EditText mEmail;
    /**
     * 用户昵称
     */
    EditText mName;
    /**
     * 用户密码
     */
  EditText mPsw;
    /**
     * 注册按钮
     */
    Button mRegistBtn;
    /**
     * 同意服务条款的CheckBox
     */
    CheckBox mChAgree;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            mView=inflater.inflate(R.layout.fragment_regist,container,false);
        return  mView;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        mHomeActivity=getActivity();
        mFragmentManager=getFragmentManager();
        /**
         * 找控件
         */
        mEmail= (EditText) view.findViewById(R.id.edt_regist_fragment_mail);
        mName=(EditText) view.findViewById(R.id.edt_regist_fragment_name);
        mPsw=(EditText) view.findViewById(R.id.edt_regist_fragment_pwd);
        mRegistBtn= (Button) view.findViewById(R.id.btn_regist_fragment_regist);
        mChAgree= (CheckBox) view.findViewById(R.id.ch_regist_fragment_agree);

        mRegistBtn.setOnClickListener(this);

    }

    String email;
    String name;
    String psw;

    /**
     * 判断是否满足注册条件
     */
    private boolean isRegist(){
        email=mEmail.getText().toString();
        name=mName.getText().toString();
        psw=mPsw.getText().toString();
        boolean emailFlag = false;
        boolean nameFlag = false;
        boolean pswFlag = false;
        if(email!=null){
            emailFlag= email.matches("[[a-z][A-Z][0-9]]{6,10}@[a-z][0-9]{2,3}[\\.][Cc][Oo][Mm]");//判断邮箱格式正确与否
            if(!emailFlag){
                Toast.makeText(getContext(),"邮箱格式错误，请重新输入",Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getContext(),"请先输入邮箱",Toast.LENGTH_SHORT).show();
        }
        if(name!=null){
            nameFlag=name.matches("^[a-zA-Z]\\w{5,23}$");//正确格式为：以字母开头，长度在6~24之间，只能包含字母、数字和下划线。
            if(!nameFlag){
                Toast.makeText(getContext(),"昵称格式错误，请重新输入",Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getContext(),"请先输入昵称",Toast.LENGTH_SHORT).show();
        }
        if(psw!=null){
            pswFlag=name.matches("^[a-zA-Z]\\w{5,23}$");//正确格式为：以字母开头，长度在6~18之间，只能包含字母、数字和下划线。
            if(!pswFlag){
                Toast.makeText(getContext(),"密码格式错误，请重新输入",Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getContext(),"请先输入密码",Toast.LENGTH_SHORT).show();
        }
        if(emailFlag==true&&nameFlag==true&&pswFlag==true){
            return true;
        }
        return false;
    }
    @Override
    public void onClick(View v) {
        if(isRegist()){//判断是否满足注册条件
            //ver=版本号&uid=用户名&email=邮箱&pwd=登陆密码
            Map<String,String> params=new HashMap<>();
            params.put("ver","0000000");
            params.put("uid",name);
            params.put("email",email);
            params.put("pwd",psw);

            final Gson gson=new Gson();
            MyVolley.get(getContext(), SeverUrl.USER_REGISTER, params, new MyVolley.OnVolleyResult() {
                @Override
                public void success(String s) {
                    RegisterResponse registerResponse=  gson.fromJson(s, RegisterResponse.class);
                    if(registerResponse.data.result==0){
                        Toast.makeText(getContext(),"注册成功",Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void failed(VolleyError volleyError) {
                    Toast.makeText(getContext(),"网络连接失败",Toast.LENGTH_SHORT).show();
                }
            });

        }
    }
}
