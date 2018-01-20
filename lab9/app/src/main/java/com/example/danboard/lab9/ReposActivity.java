package com.example.danboard.lab9;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.danboard.lab9.factory.ServiceFactory;
import com.example.danboard.lab9.model.Repos;
import com.example.danboard.lab9.service.GithubService;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ReposActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private List<Map<String, Object>> reposList = new ArrayList<>();
    private SimpleAdapter sa;
    private ListView reposListView;
    private WebView wv;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repos);

        progressBar = (ProgressBar)findViewById(R.id.progress2);
        reposListView = (ListView)findViewById(R.id.repos_list);
        wv = (WebView)findViewById(R.id.web);
        wv.setWebViewClient(new WebViewClient(){
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
//        wv.getSettings().setBuiltInZoomControls(true);
//        wv.getSettings().setLoadWithOverviewMode(true);
//        wv.getSettings().setSupportZoom(true);

        name = getIntent().getStringExtra("user");

        GithubService service = ServiceFactory.getRetrofit("https://api.github.com/")
                .create(GithubService.class);
        service.getRepos(name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Repos>>() {

                    @Override
                    public void onCompleted() {
                        progressBar.setVisibility(View.INVISIBLE);
                        Log.d("TAG", "Get Repositories Successfully");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(ReposActivity.this, "Fail to get repositories", Toast.LENGTH_SHORT).show();
                        Log.d("TAG", "Fail to get repositories");
                        progressBar.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onNext(List<Repos> list) {
                        for (Repos item : list) {
                            Map<String, Object> temp = new LinkedHashMap<>();
                            temp.put("name", item.getName());
                            temp.put("language", item.getLanguage());
                            temp.put("description", item.getDescription());
                            reposList.add(temp);
                        }
                        Log.d("TAG", String.valueOf(reposList));
                        sa = new SimpleAdapter(ReposActivity.this, reposList, R.layout.repos_item,
                                new String[]{"name", "language", "description"}, new int[]{R.id.name, R.id.language, R.id.descriptionn});
                        reposListView.setAdapter(sa);
                        Log.d("TAG", "Show Successfully");
                    }
                });
        reposListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String uri = "https://github.com/"+name+"/"+reposList.get(position).get("name");
                wv.setVisibility(View.VISIBLE);
                wv.loadUrl(uri);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(wv.canGoBack()) {
            wv.goBack();// 返回前一个页面
            Log.d("TAG", "WebView");
        }else if (wv.getVisibility() == View.VISIBLE) {
            wv.setVisibility(View.INVISIBLE);
            Log.d("TAG", "Repos");
        } else
        super.onBackPressed();
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK ) {
//            if (wv.canGoBack()) {
//                wv.goBack();// 返回前一个页面
//                Log.d("TAG", "WebView");
//            } else if (wv.getVisibility() == View.VISIBLE) {
//                wv.setVisibility(View.INVISIBLE);
//                Log.d("TAG", "Repos");
//            }
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }


}
