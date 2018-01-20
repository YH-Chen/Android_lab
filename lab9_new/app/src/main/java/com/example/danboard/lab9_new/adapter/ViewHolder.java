package com.example.danboard.lab9_new.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Danboard on 17-12-12.
 */

public class ViewHolder extends RecyclerView.ViewHolder {
    private SparseArray<View> mViews; //存储listItem的子view
    private View mConvertView;        //存储listItem
    private Context mContext;

    public ViewHolder(Context context, View itemView, ViewGroup parent) {
        super(itemView);                  //调用父类构造函数
        mConvertView = itemView;
        mViews = new SparseArray<>();
        mContext = context;
    }

    //获取viewHolder实例
    public static ViewHolder get(Context context, ViewGroup parent, int itemId) {
        View view = LayoutInflater.from(context).inflate(itemId, parent, false);    //加载布局
        ViewHolder holder = new  ViewHolder(context, view, parent);
        return holder;
    }

    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);   //创建view
            mViews.put(viewId, view);                    //将view存入mViews
        }
        return (T) view;
    }
}
