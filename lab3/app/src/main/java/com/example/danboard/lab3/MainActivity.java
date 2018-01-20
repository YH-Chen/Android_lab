package com.example.danboard.lab3;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.OvershootInLeftAnimator;

public class MainActivity extends AppCompatActivity {

    private ListView shopListView;

    private List<Map<String, Object>> goodsList = new ArrayList<>();
    private List<Map<String, Object>> shopList = new ArrayList<>();

    private SimpleAdapter simpleAdapter;
    private CommonAdapter goodsListAdapter;
    private ScaleInAnimationAdapter animationAdapter;

    private AlertDialog.Builder builder;

    private RecyclerView goodsRecyclerView;

    private FloatingActionButton switchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //初始化商品列表
        initGoodsList();

        goodsRecyclerView = (RecyclerView) findViewById(R.id.goodslist);
        goodsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        goodsListAdapter = new CommonAdapter(this, R.layout.item, goodsList) {
            @Override
            protected void convert(ViewHolder holder, Map<String, Object> m) {
                TextView letter = holder.getView(R.id.letter);
                letter.setText(m.get("letter").toString());
                TextView name = holder.getView(R.id.name);
                name.setText(m.get("name").toString());
            }
        };
        //添加动画
        animationAdapter = new ScaleInAnimationAdapter(goodsListAdapter);
        animationAdapter.setDuration(1000);
        goodsRecyclerView.setAdapter(animationAdapter);
        goodsRecyclerView.setItemAnimator(new OvershootInLeftAnimator());
        //单击与长按
        goodsListAdapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                String goodsName = goodsList.get(position).get("name").toString();
                Intent intent = new Intent(MainActivity.this, GoodsInformation.class);
                intent.putExtra("name", goodsName);
                startActivityForResult(intent, 1);
            }

            @Override
            public boolean onLongClick(int position) {
                String index = Integer.toString(position);
                goodsList.remove(position);
                goodsListAdapter.notifyItemRemoved(position);
                Toast.makeText(MainActivity.this, "移除第" + index + "个商品", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        //切换成购物车
        switchButton = (FloatingActionButton)findViewById(R.id.switch_button);
        switchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(goodsRecyclerView.getVisibility() == view.VISIBLE) {
                    goodsRecyclerView.setVisibility(view.GONE);
                    shopListView.setVisibility(View.VISIBLE);
                    switchButton.setImageResource(R.drawable.mainpage);
                }
                else if(shopListView.getVisibility() == view.VISIBLE) {
                    goodsRecyclerView.setVisibility(view.VISIBLE);
                    shopListView.setVisibility(view.GONE);
                    switchButton.setImageResource(R.drawable.shoplist);
                }
            }
        });

        //初始化购物车列表
        initShopList();

        //购物车对话框
        builder = new AlertDialog.Builder(this);
        builder.setTitle("移除商品").setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {}
        });

        shopListView = (ListView) findViewById(R.id.shoppinglist);
        simpleAdapter = new SimpleAdapter(this, shopList, R.layout.shoplist_layout,
                new String[]{"letter", "name", "price"}, new int[]{R.id.letter, R.id.name, R.id.price});
        shopListView.setAdapter(simpleAdapter);
        shopListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                //长按
                builder.setMessage("从购物车移除" + shopList.get(position).get("name") + "？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        shopList.remove(position);
                        simpleAdapter.notifyDataSetChanged();
                    }
                }).create().show();
                return true;
            }
        });
        shopListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //单击
                if(position != 0) {
                    String goodsName = shopList.get(position).get("name").toString();
                    Intent intent = new Intent(MainActivity.this, GoodsInformation.class);
                    intent.putExtra("name", goodsName);
                    startActivityForResult(intent, 1);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1) {
            if(resultCode == RESULT_OK) {
                String rev_name = data.getStringExtra("name");
                String rev_price = data.getStringExtra("price");
                if(rev_name != null && rev_price != null) {
                    Map<String,Object> temp = new LinkedHashMap<>();
                    temp.put("letter", rev_name.substring(0,1));
                    temp.put("name", rev_name);
                    temp.put("price", rev_price);
                    shopList.add(temp);
                    simpleAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    //商品列表
    private void initGoodsList() {
        String[] name = new String[]{"Enchated Forest", "Arla Milk", "Devondale Milk", "Kindle Oasis",
                "waitrose 早餐麦片", "Mcvitie's 饼干", "Ferrero Rocher", "Maltesers", "Lindt", "Borggreve"};
        for (int i = 0; i < 10; i++) {
            Map<String, Object> temp = new LinkedHashMap<>();
            temp.put("letter", name[i].substring(0, 1));
            temp.put("name", name[i]);
            goodsList.add(temp);
        }
    }
    //购物车列表
    private void initShopList() {
        String[] name = new String[] {"购物车"};
        String[] price = new String[] {"价格"};
        Map<String, Object> temp = new LinkedHashMap<>();
        temp.put("letter", "*");
        temp.put("name", name[0]);
        temp.put("price", price[0]);
        shopList.add(temp);
    }
}

