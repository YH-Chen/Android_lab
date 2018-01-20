package com.example.danboard.lab3;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.Map;

/**
 * Created by Danboard on 2017/10/23.
 */

public abstract class CommonAdapter extends RecyclerView.Adapter<ViewHolder>{

    private Context mContext;
    private int mlayoutId;
    private List<Map<String, Object>> mDatas;
    private LayoutInflater mInflater;
    private OnItemClickListener mOnItemClickListener;

    public CommonAdapter(Context context, int layout, List<Map<String, Object>> datas) {
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

    protected abstract void convert(ViewHolder holder, Map<String, Object> stringObjectMap);

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public interface OnItemClickListener {
        void onClick(int position);
        boolean onLongClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.mOnItemClickListener=onItemClickListener;
    }
}
