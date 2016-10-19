package edu.feicui.daynews.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import edu.feicui.daynews.R;
import edu.feicui.daynews.SeverUrl;
import edu.feicui.daynews.Util.MyVolley;
import edu.feicui.daynews.entity.CommentNumber;

/**
 * Created by Administrator on 2016/10/13.
 */
public class NewsDetailActivity extends BaseActivity implements View.OnClickListener {
    /**
     * 加载详情新闻的WebView控件
     */
    WebView webView;
    /**
     * 跟帖数
     */
    TextView mTvComment;
        @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_news_activity);
            getCommentHttp();
    }

    int nid=1;
    @Override
    public void initView() {
        webView = (WebView) findViewById(R.id.web_news_detail);
        mTvComment= (TextView) findViewById(R.id.tv_basge_comment);
        mTvComment.setVisibility(View.VISIBLE);
        mImgLeft.setImageResource(R.mipmap.back);
        mImgRight.setImageResource(R.mipmap.news_menu);
        mImgLeft.setOnClickListener(this);
        mTvComment.setOnClickListener(this);

        Intent intent = getIntent();
        nid = intent.getIntExtra("nid", 0);
        String url = intent.getStringExtra("link");
        webView.loadUrl(url);//加载网络链接
//        Log.e("aac", "initView: "+url);
        /**
         * 获取WebSettings的对象
         */
        WebSettings settings = webView.getSettings();
        settings.setSupportZoom(true);//支持缩放功能
        settings.setJavaScriptEnabled(true);//支持jsp脚本语言
        settings.setLoadWithOverviewMode(true);//按照任意比例缩放
        settings.setBuiltInZoomControls(true);//显示缩放视图
        settings.setUseWideViewPort(true);//适应屏幕大小
        /**
         * 将当前应用作为WebView的展示界面  返回值为true  并且重新加载链接
         */
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                webView.loadUrl(url);//加载链接
                return super.shouldOverrideUrlLoading(view,url);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();
            return true;
        } else {
            finish();
        }
        return false;
    }
    /**
     * 获取评论数量
     */
    Gson gson=new Gson();
    public void getCommentHttp(){
        //ver=版本号& nid=新闻编号
        Map<String,String> params=new HashMap<>();
        params.put("ver","0000000");
        params.put("nid",""+nid);
        MyVolley.get(this, SeverUrl.COMMENT_NUM, params, new MyVolley.OnVolleyResult() {
            @Override
            public void success(String s) {
                CommentNumber commentNumber =   gson.fromJson(s, CommentNumber.class);
                mTvComment.setText("跟帖数 "+commentNumber.data);

            }

            @Override
            public void failed(VolleyError volleyError) {
                Toast.makeText(NewsDetailActivity.this,"请求网络失败",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_basge_comment:
                break;
            case  R.id.img_base_activity_left:
                finish();
                break;
        }

    }
}


