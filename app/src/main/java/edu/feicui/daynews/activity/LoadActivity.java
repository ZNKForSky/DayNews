package edu.feicui.daynews.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PersistableBundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import edu.feicui.daynews.R;

/**
 * Created by Administrator on 2016/10/9.
 */
public class LoadActivity extends Activity {
    /**
     * logo图标
     */
    ImageView mImageView;
    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_load);
        init();
    }


    public void init() {
        mImageView= (ImageView) findViewById(R.id.iv_logo_load);
        mImageView.setImageResource(R.mipmap.logo);
        /**
         * 创建动画
         */
        Animation animation= AnimationUtils.loadAnimation(this,R.anim.load_anim);
        /**
         * 给图标设置动画
         */
        mImageView.startAnimation(animation);
        /**
         * 设置延时两秒
         */
        CountDownTimer countDownTimer=new CountDownTimer(3200,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                Intent intent=new Intent();
                intent.setClass(LoadActivity.this,HomeActivity.class);
                intent.putExtra("int",2);
                startActivity(intent);
                finish();
            }
        }.start();
    }
}
