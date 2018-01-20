package com.example.danboard.lab9_new.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.danboard.lab9_new.model.Github;

import java.util.List;

/**
 * Created by Danboard on 17-12-11.
 */

public abstract class CardAdapter extends RecyclerView.Adapter<ViewHolder> {

    private Context mContext;
    private int mlayoutId;
    private List<Github> mDatas;
    private LayoutInflater mInflater;
    private OnItemClickListener mOnItemClickListener;

    public CardAdapter(Context context, int layout, List<Github> datas) {
        mContext = context;
        mlayoutId = layout;
        mDatas = datas;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        ViewHolder viewHolder = ViewHolder.get(mContext, parent, mlayoutId);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        convert(holder, mDatas.get(position));
        if(mOnItemClickListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.onClick(holder.getAdapterPosition());
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    mOnItemClickListener.onLongClick(holder.getAdapterPosition());
                    return true;
                }
            });
        }
    }

    protected abstract void convert(ViewHolder holder, Github item);


    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public Github getItem(int position) {
        return mDatas.get(position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public static abstract interface OnItemClickListener
    {
        public abstract void onClick(int paramInt);
        public abstract void onLongClick(int paramInt);
    }
}


