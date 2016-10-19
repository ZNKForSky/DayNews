package edu.feicui.daynews.activity.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import edu.feicui.daynews.R;
import edu.feicui.daynews.SeverUrl;
import edu.feicui.daynews.Util.MyVolley;
import edu.feicui.daynews.activity.HomeActivity;
import edu.feicui.daynews.activity.NewsDetailActivity;
import edu.feicui.daynews.adapter.HomeNewsAdapter;
import edu.feicui.daynews.entity.News;
import edu.feicui.daynews.entity.NewsArray;
import edu.feicui.daynews.view.XListView;


/**
 * 新闻列表
 * Created by zhaoCe on 2016/9/28.
 */

public class  HomeNewsFragment extends Fragment implements XListView.IXListViewListener, AdapterView.OnItemClickListener {
    /**
     * 加载此Fragment的Activity
     */
    HomeActivity mHomeActivity;
    Context mContext;
    /**
     * 主界面的数据源显示的listview
     */
    XListView mListView;
    /**
     * 该界面listview的适配器
     */
    HomeNewsAdapter adapterNews;
    int dir = 1;//1是下拉刷新   2是上拉加载
    int nid = 1;//新闻id

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_news, container, false);
        mListView = (XListView) view.findViewById(R.id.lv_home);
        init();
        getHttpData();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mListView.setPullLoadEnable(true);
        mListView.setPullRefreshEnable(true);
        mListView.setXListViewListener(this);
        mListView.setOnItemClickListener(this);
    }

    /**
     * 初始化工作
     */
    void init() {
      mContext= getActivity();
      mHomeActivity = (HomeActivity) mContext;
//        mHomeActivity = (HomeActivity) getActivity();
        adapterNews = new HomeNewsAdapter(mContext, null, R.layout.item_list_home_news_fg);
        mListView.setAdapter(adapterNews);
        mHomeActivity.mTvTitle.setText("资讯");
    }
    @Override
    public void onRefresh() {
        dir = 1;
        adapterNews.mList.clear();//刷新之前清空集合------清空之前的新闻列表
        /**
         * 重新获取数据
         */
        getHttpData();


    }

    /**
     * if代码块不太懂
     */
    @Override
    public void onLoadMore() {
        dir = 2;
        if (adapterNews.mList.size() > 0) {
            News news = adapterNews.mList.get(adapterNews.mList.size() - 1);
            nid = news.nid;
        }
        getHttpData();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        News news = adapterNews.mList.get(position);
        Intent intent = new Intent();
//        Log.e("aac", "onItemClick: "+news.link );
        intent.putExtra("link", news.link);
        intent.putExtra("nid", news.nid);
        intent.setClass(mContext, NewsDetailActivity.class);
        startActivity(intent);
    }
    /**
     * 获取服务器的数据
     */
    private void getHttpData() {
        final SimpleDateFormat format = new SimpleDateFormat(" yyyy-MM-dd HH:mm");
        final ProgressDialog dialog = ProgressDialog.show(mContext, "", "加载中...");
        Map<String, String> params = new HashMap<>();
        params.put("ver", "0000000");
        params.put("subid", "1");
        params.put("dir", "" + dir);
        params.put("nid", "" + nid);
        params.put("stamp", "20140321000000");
        params.put("cnt", "20");
        MyVolley.get(mContext, SeverUrl.NEWS_LIST, params, new MyVolley.OnVolleyResult() {//get请求
            @Override
            public void success(String s) {
                Gson gson = new Gson();
                NewsArray array = gson.fromJson(s, NewsArray.class);
                if (array.data != null && array.data.size() > 0) {//
                    adapterNews.mList.addAll(array.data);
                    adapterNews.notifyDataSetChanged();//刷新适配器
                }
                if (dir == 1) {//如果是下拉刷新
                    Date date = new Date();//获取当前时间
                    mListView.setRefreshTime(format.format(date));//将当前时间赋给更新时间
                }
                mListView.stopLoadMore();//停止加载
                mListView.stopRefresh();//停止刷新
                dialog.dismiss();//在数据刷新之后，关闭加载框
            }

            @Override
            public void failed(VolleyError volleyError) {
                dialog.dismiss();//关闭加载框
                Toast.makeText(mContext, "失败！", Toast.LENGTH_SHORT).show();
                mListView.stopLoadMore();
                mListView.stopRefresh();
            }
        });

    }


}
