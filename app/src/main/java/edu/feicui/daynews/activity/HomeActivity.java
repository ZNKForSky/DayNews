package edu.feicui.daynews.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import edu.feicui.daynews.R;
import edu.feicui.daynews.activity.fragment.HomeNewsFragment;
import edu.feicui.daynews.activity.fragment.LoginFragment;
import edu.feicui.daynews.adapter.HomeLeftAdapter;
import edu.feicui.daynews.entity.LeftItem;
import edu.zx.slidingmenu.SlidingMenu;


/**
 * 主界面
 */

public class HomeActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    /**
     * 传入用户中心的名字
     */
    String loginName;
    /**
     * 跳转登陆界面按钮
     */
    ImageView mImgLogin;
    /**
     * 初始化loginFragment对象
     */
    LoginFragment loginFragment=new LoginFragment();
    /**
     * 屏幕左滑listview适配器
     */
    HomeLeftAdapter adapterLeft;
    /**
     * 初始化一个HomeFragment对象
     */
   HomeNewsFragment homeFragment=new HomeNewsFragment();
    /**
     * 侧滑
     */
//    DrawerLayout mDl;

    /**
     * 左滑的listview
     */
    ListView mListViewLeft;
    /**
     * 右滑的界面
     */
    LinearLayout mTvRight;
    /**
     * 选择更多新闻按钮
     */
    TextView mTvMore;
    /**
     * fragment管理器
     */
    FragmentManager fragmentManager;
    /**
     * 登陆成功，显示用户名
     */
    TextView mTvCenterName;
    /**
     * 登陆成功的响应
     */
    boolean flag=true;
    /**
     * fragment事务管理的操作对象
     */
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    /**
     * 实现左右侧滑的第三方的类
     */
    SlidingMenu menu;
     @Override
    public void initView() {
         menu=new SlidingMenu(this);
         menu.attachToActivity(this,SlidingMenu.SLIDING_CONTENT);
         /**
          * 设置左侧菜单以及右侧菜单
          */
         View viewLeft=getLayoutInflater().inflate(R.layout.home_left,null);
         menu.setMenu(viewLeft);
         View viewRight=getLayoutInflater().inflate(R.layout.home_right,null);
         menu.setSecondaryMenu(viewRight);
         /**
          * 设置展示模式
          */
         menu.setMode(SlidingMenu.LEFT_RIGHT);//左右都可展示
         /**
          * 设置边界距离
          *  防止菜单栏完全遮盖主内容  需要设置一定的边界距离
          */
         menu.setBehindOffset(200);
         /**
          * 设置触摸展开方式
          */
         menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

         mTvMore= (TextView) findViewById(R.id.tv_choose_more_news_home);
         mListViewLeft= (ListView)viewLeft.findViewById(R.id.lv_home_left);
         mListViewLeft.setOnItemClickListener(this);
         mTvRight= (LinearLayout)viewRight.findViewById(R.id.ll_home_right);
         mImgLogin= (ImageView)viewRight.findViewById(R.id.img_home_right_login);
         mTvCenterName= (TextView)viewRight.findViewById(R.id.tv_home_center_name);



        fragmentManager=getSupportFragmentManager();
        fragmentTransaction= fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.ll_home_fg,homeFragment);
        fragmentTransaction.commit();




         setOnClick(this,mImgLeft,mImgRight,mImgLogin);
        ArrayList<LeftItem> listLeft=new ArrayList<>();
        listLeft.add(new LeftItem(R.mipmap.biz_navigation_tab_news,"新闻","NEWS"));
        listLeft.add(new LeftItem(R.mipmap.biz_navigation_tab_read,"收藏","FAVORITE"));
        listLeft.add(new LeftItem(R.mipmap.biz_navigation_tab_local_news,"本地","LOCAL"));
        listLeft.add(new LeftItem(R.mipmap.biz_navigation_tab_ties,"跟帖","COMMENT"));
        listLeft.add(new LeftItem(R.mipmap.biz_navigation_tab_pics,"图片","PHOTO"));
        adapterLeft=new HomeLeftAdapter(this,listLeft,R.layout.item_list_home_left);
        mListViewLeft.setAdapter(adapterLeft);
//         mTvCenterName.setText("立即登陆");
        Intent intentLoad= getIntent();
        int loadInt= intentLoad.getIntExtra("int",1);

         //从个人中心回退到该界面
        if(loadInt==1){
            i++;
            Intent intent=getIntent();
            flag=intent.getBooleanExtra("flag",true);
            loginName=intent.getStringExtra("loginName");
            mTvCenterName.setText(loginName);
        }
    }

    int i=0;


    @Override
    public void onClick(View v) {
        fragmentTransaction= fragmentManager.beginTransaction();
        switch (v.getId()){
            case R.id.img_base_activity_left://左滑界面
//                mDl.openDrawer(Gravity.LEFT);
                menu.showMenu();
                break;
            case R.id.img_base_activity_right://右滑界面
//                mDl.openDrawer(Gravity.RIGHT);
                menu.showSecondaryMenu();
                break;
            case R.id.tv_choose_more_news_home://更多新闻按钮
                break;
            case R.id.img_home_right_login:
            {
                if(flag&&i==0){//跳到登陆界面
                    fragmentTransaction.replace(R.id.ll_home_fg,loginFragment);
                    fragmentTransaction.commit();
//                    mDl.closeDrawer(Gravity.RIGHT);
                }else{//跳到用户中心界面
                    Intent intent=new Intent();
                    intent.putExtra("loginName",loginName);
                    intent.setClass(this,UserInfoActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if(menu.isMenuShowing()||menu.isSecondaryMenuShowing()){
            menu.showContent();
            return;
        }
        super.onBackPressed();
    }

    /**
     * 左滑界面的子条目点击事件
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.e("aac", "onItemClick: " );
        fragmentTransaction=fragmentManager.beginTransaction();
        switch (position){
            case 0:
            {
                fragmentTransaction.replace(R.id.ll_home_fg,homeFragment);//跳到新闻列表
                fragmentTransaction.commit();
//                mDl.closeDrawer(Gravity.LEFT);
                menu.showContent();
            }
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;

        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if(keyCode == KeyEvent.KEYCODE_BACK);
        {
            exitBy2Click();
        }
        return false;
    }
    private static Boolean isExit = false;
    private void exitBy2Click() {//双击退出
        // TODO Auto-generated method stub
        Timer tExit = null;
        if (isExit == false) {
            isExit = true; // 准备退出
            Toast.makeText(this, "请再次点击返回键退出", Toast.LENGTH_SHORT).show();
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务
        }
        else {
            finish();
            System.exit(0);
        }
    }
}
