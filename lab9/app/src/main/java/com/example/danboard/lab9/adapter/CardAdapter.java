package com.example.danboard.lab9.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.danboard.lab9.R;
import com.example.danboard.lab9.model.Github;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Danboard on 17-12-11.
 */

public class CardAdapter extends RecyclerView.Adapter<ViewHolder> {

    private List<Github> list = new ArrayList<>();
    private OnItemClickListener onItemClickListener = null;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewId) {
        Log.d("TAG", String.valueOf(parent));
        Log.d("TAG", "onCreateViewHolder");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyle_view_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Github item = list.get(position);
        holder.setLogin(item.getLogin());
        holder.setId("id: "+item.getId());
        holder.setBlog("blog: "+item.getBlog());
        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onClick(holder.getAdapterPosition());
                    Log.d("TAG", "Click");
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onItemClickListener.onLongClick(holder.getAdapterPosition());
                    Log.d("TAG", "LongClick");
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public Github getItem(int position) {
        return list.get(position);
    }

    public void add(Github item) {
        list.add(item);
        notifyDataSetChanged();
    }

    public void remove(int position) {
        list.remove(position);
        notifyDataSetChanged();
    }

    public void clear() {
        list.clear();
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public static abstract interface OnItemClickListener
    {
        public abstract void onClick(int paramInt);
        public abstract void onLongClick(int paramInt);
    }
}


