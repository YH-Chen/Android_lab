package com.example.danboard.lab9;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.danboard.lab9.adapter.CardAdapter;
import com.example.danboard.lab9.factory.ServiceFactory;
import com.example.danboard.lab9.model.Github;
import com.example.danboard.lab9.service.GithubService;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity {

    private ProgressBar progress;
    private CardAdapter cards;
    private EditText search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("TAG", "onCreate");

        search = (EditText) findViewById(R.id.search);
        progress = (ProgressBar)findViewById(R.id.progress1);
        cards = new CardAdapter();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(cards);

        Button clear = (Button) findViewById(R.id.clear);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cards.clear();
            }
        });

        Button fetch = (Button) findViewById(R.id.fetch);
        fetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress.setVisibility(View.VISIBLE);
                GithubService service = ServiceFactory.getRetrofit("https://api.github.com")
                        .create(GithubService.class);
                service.getUser(search.getText().toString())
                    .subscribeOn (Schedulers.newThread())       // 新建线程进行网络访问
                    .observeOn(AndroidSchedulers.mainThread()) // 在主线程处理请求结果
                    .subscribe(new Subscriber<Github>() {
                        @Override
                        public void onCompleted() {
                            progress.setVisibility(View.INVISIBLE);
                            Log.d("TAG", "Search Successfully");
                        }

                        @Override
                        public void onError(Throwable e) {
                            progress.setVisibility(View.INVISIBLE);
                            Toast.makeText(MainActivity.this, e.hashCode() + "请确认你搜索的用户存在", Toast.LENGTH_SHORT).show();
                            Log.d("TAG", "Fail to search the User");
                        }

                        @Override
                        public void onNext(Github item) {
                            cards.add(item);
                            cards.notifyDataSetChanged();
                            Log.d("TAG", "Add Successfully");
                        }
                    });
            }
        });

        cards.setOnItemClickListener(new CardAdapter.OnItemClickListener() {
            @Override
            public void onClick(int paramInt) {
                Intent intent = new Intent(MainActivity.this, ReposActivity.class);
                intent.putExtra("user", cards.getItem(paramInt).getLogin());
                startActivity(intent);
            }

            @Override
            public void onLongClick(int paramInt) {
                cards.remove(paramInt);
            }
        });
    }
}
