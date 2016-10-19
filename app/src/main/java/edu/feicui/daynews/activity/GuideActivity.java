package edu.feicui.daynews.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

import edu.feicui.daynews.R;
import edu.feicui.daynews.adapter.GuideAdapter;

/**
 * 引导界面
 */
public class GuideActivity extends Activity implements ViewPager.OnPageChangeListener {

    /**
     * 支持左滑右滑的组件（ViewPager）
     */
    ViewPager mVp;
    /**
     *装图片的数组
     */
    int[] pic={R.mipmap.bd,R.mipmap.welcome,R.mipmap.wy,R.mipmap.small};
    /**
     * 下面的四个点
     */
    ImageView mPointFirst;
    ImageView mPointSecond;
    ImageView mPointThird;
    ImageView mPointFourth;
    /**
     * 装四个点的集合
     */
    ArrayList<ImageView> points=new ArrayList<>();
    /**
     * 存储小数据的SharedPreferences
     */
    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        if(!getDate()){//不是第一次进来
            Intent intent=new Intent(this,LoadActivity.class);
            startActivity(intent);
            finish();
        }
        init();
    }

    private void init() {
        /**
         * 找控件
         */
        mVp= (ViewPager) findViewById(R.id.vp_guide);
        mPointFirst= (ImageView) findViewById(R.id.iv_first);
        mPointSecond= (ImageView) findViewById(R.id.iv_second);
        mPointThird= (ImageView) findViewById(R.id.iv_third);
        mPointFourth= (ImageView) findViewById(R.id.iv_fourth);
        /**
         * 将四个点装进集合中
         */
        points.add(mPointFirst);
        points.add(mPointSecond);
        points.add(mPointThird);
        points.add(mPointFourth);
        /**
         * 数据源
         */
        ArrayList<ImageView> list=new ArrayList();
        for (int i = 0; i <pic.length ; i++) {
            ImageView img=new ImageView(this);
            img.setImageResource(pic[i]);
            list.add(img);
        }
        /**
         * 适配器
         */
        GuideAdapter adapter=new GuideAdapter(list,this);
        mVp.setAdapter(adapter);
        mVp.setOnPageChangeListener(this);

    }

    private boolean getDate() {
        preferences=getSharedPreferences("Daily_news_SharedPreferences",MODE_PRIVATE);
        return preferences.getBoolean("isFirst",true);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }
   int count=0;
    boolean flag;
    @Override
    public void onPageSelected(int position) {
        for (ImageView point:points) {
            point.setImageResource(R.mipmap.a5);
        }
        points.get(position).setImageResource(R.mipmap.a4);
        if(position==3){
            flag=true;
            count++;
        }
        if(flag){
            mVp.setCurrentItem(3);//设置滑到下标为3的ImageView时不能滑动
            if(count==1){
                CountDownTimer countDownTimer=new CountDownTimer(3000,1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                       SharedPreferences.Editor editor= preferences.edit();
                        editor.putBoolean("isFirst",false);
                        editor.commit();
                        Intent intent=new Intent(GuideActivity.this,LoadActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }.start();
            }else{
                Toast.makeText(this,"即将跳转至加载界面，请勿滑动",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
