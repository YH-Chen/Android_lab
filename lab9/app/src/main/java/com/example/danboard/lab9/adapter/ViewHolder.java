package com.example.danboard.lab9.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.danboard.lab9.R;

/**
 * Created by Danboard on 17-12-12.
 */

public class ViewHolder extends RecyclerView.ViewHolder {

    private TextView login;
    private TextView id;
    private TextView blog;

    public ViewHolder(View view) {
        super(view);
        this.login = (TextView)view.findViewById(R.id.login);
        this.id = (TextView)view.findViewById(R.id.id);
        this.blog = (TextView)view.findViewById(R.id.blog);
    }

    public void setLogin(String login) {
        this.login.setText(login);
    }
    public void setId(String id) {
        this.id.setText(id);
    }
    public void setBlog(String blog) {
        this.blog.setText(blog);
    }
}
