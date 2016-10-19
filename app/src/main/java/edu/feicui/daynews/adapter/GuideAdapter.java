package edu.feicui.daynews.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/10/9.
 */
public class GuideAdapter extends PagerAdapter {
    ArrayList<ImageView> mList;
    Context mContext;
    LayoutInflater mInflater;
    public GuideAdapter(ArrayList<ImageView> mList,Context mContext){
        this.mList=mList;
        this.mContext=mContext;

    }
    @Override
    public int getCount() {
        return mList==null?0:mList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView view=mList.get(position);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeViewAt(position);
    }
}
