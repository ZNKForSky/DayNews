package edu.feicui.daynews.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import edu.feicui.daynews.R;
import edu.feicui.daynews.SeverUrl;
import edu.feicui.daynews.Util.MyVolley;
import edu.feicui.daynews.entity.RegisterResponse;
import edu.feicui.daynews.entity.UserInfoResponse;

/**
 * Created by Administrator on 2016/10/10.
 */
public class UserInfoActivity extends Activity implements View.OnClickListener {
    View mView;

    /**
     * 头像
     */
    ImageView mIvTitle;
TextInputEditText edt;
    /**
     * 声明PopWindow
     */
    PopupWindow popupWindow;
    /**
     * 存放图片的文件夹路径
     */
    public static final String DIRECTORY_PATH= Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator+"EveryDayNews";
    /**
     * 图片文件路径
     */
    public static final String PHOTO_PATH=DIRECTORY_PATH+File.separator+"photo.jpg";
    /**
     * 相机权限请求码
     */
    public static final int CAMERA_PERMISSION=200;
    /**
     * 跳转相机请求码
     */
    public static final int GOTO_CAMERA=201;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        mView=getLayoutInflater().inflate(R.layout.pop_window_user_info,null);
        LinearLayout mLlCamera= (LinearLayout) mView.findViewById(R.id.ll_camera);
        LinearLayout mLlPhotos= (LinearLayout) mView.findViewById(R.id.ll_photo_sel);
//        LinearLayout mLlPay= (LinearLayout) mView.findViewById(R.id.ll_photo_pay);
        mIvTitle= (ImageView) findViewById(R.id.iv_title_photo);

        /**
         * 给相机、图库和银联支付设置监听事件
         */
        mLlCamera.setOnClickListener(this);
        mLlPhotos.setOnClickListener(this);
//        mLlPay.setOnClickListener(this);

        /**
         * 拿到PopWindow的对象
         */
        popupWindow=new PopupWindow(mView,LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mIvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 设置外部可以点击
                 */
                popupWindow.setOutsideTouchable(true);
                popupWindow.setBackgroundDrawable(new BitmapDrawable());

                /**
                 * 2.进行显示
                 *  （1）基于某一个控件显示PopWindow
                 *  （2）基于整个屏幕显示PopWindow
                 */
//                popupWindow.showAsDropDown(mIvTitle,0,0);//(1)
                popupWindow.showAtLocation(mIvTitle, Gravity.BOTTOM,0,0);
            }
        });
    }

    /**
     * 跳转到相机
     */
    public void gotoCamera(){
        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //传递此次拍照图片存放的路径
        //指定一个路径存放图片  手机SD卡
        File fileDestroy=new File(DIRECTORY_PATH);
        if(!fileDestroy.exists()){
            fileDestroy.mkdirs();
        }
        Log.e("aac", "gotoCamera: "+fileDestroy);
        //向相机传递图片文件路径
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(PHOTO_PATH)));
        startActivityForResult(intent,GOTO_CAMERA);

//        Intent intent1=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        //传递此次拍照图片存储的路径
//        //指定一个路径去存放图片  手机SD卡路径
//        File fileDir=new File(DIR_PATH);
//        if(!fileDir.exists()){//不存在 创建文件夹
//            fileDir.mkdirs();
//        }
//        //向相机传递文件路径
//        intent1.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(PHOTO_FILE_PATH)));
//        startActivityForResult(intent1,GOTO_CAMERA);
    }
    /**
     * 拿到回传数据的结果
     * @param requestCode   请求码     区分返回结果的请求
     * @param resultCode    结果码     区分结果是否成功
     * @param data          回传的数据
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case GOTO_CAMERA:
                if(resultCode==RESULT_OK){
                   Bitmap bitmap= BitmapFactory.decodeFile(PHOTO_PATH);
                    mIvTitle.setImageBitmap(bitmap);
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_camera:
//                Toast.makeText(this,"相机",Toast.LENGTH_SHORT).show();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//如果手机版本大于23需要判断是否从用户获取到权限
                    if(checkSelfPermission(Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED&&
                            checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
                        //如果已经获取了相机权限和SD卡读写权限
                        gotoCamera();

                    }else{//请求权限
                            requestPermissions(new String[]{Manifest.permission.CAMERA,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE},CAMERA_PERMISSION);

                    }
                }else{
                    gotoCamera();
                }

                popupWindow.dismiss();
                break;
            case R.id.ll_photo_sel:
                Toast.makeText(this,"图库",Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();
                break;
//            case R.id.ll_photo_pay:
                // “00” – 银联正式环境
                // “01” – 银联测试环境，该环境中不发生真实交易
//                String serverMode = "01";
//                UPPayAssistEx.startPay (this, null, null, "201610121611101439048", serverMode);
//                break;

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
       switch (requestCode){
           case CAMERA_PERMISSION:
               if(grantResults[0]== PackageManager.PERMISSION_GRANTED
                       &&grantResults[1]== PackageManager.PERMISSION_GRANTED){
                   gotoCamera();
               }else{
                   Toast.makeText(this,"您未获取相机权限",Toast.LENGTH_SHORT).show();
               }
               break;
       }
    }

    /**
     * 调用户中心的接口
     */
    public void getUserHttp(){
        //ver=版本号&imei=手机标识符&token =用户令牌
        RegisterResponse registerResponse= (RegisterResponse) getIntent().getSerializableExtra("regist_response");
        String token=registerResponse.data.token;
        Map<String,String> params=new HashMap<>();
        params.put("ver","0000000");
        params.put("imei","000000000000000");
        params.put("token",token);
        MyVolley.get(this, SeverUrl.USER_LOGIN, params, new MyVolley.OnVolleyResult() {
            @Override
            public void success(String s) {
                Gson gson=new Gson();
                UserInfoResponse userInfoResponse= gson.fromJson(s, UserInfoResponse.class);

            }

            @Override
            public void failed(VolleyError volleyError) {

            }
        });
    }
}
