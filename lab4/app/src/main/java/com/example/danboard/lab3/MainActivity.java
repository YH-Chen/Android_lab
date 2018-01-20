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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.OvershootInLeftAnimator;

public class MainActivity extends AppCompatActivity {

    private ListView shopListView;

    private List<Map<String, Object>> goodsList = new ArrayList<>();
    private List<Map<String, Object>> shopList = new ArrayList<>();

    private SimpleAdapter simpleAdapter;
    private CommonAdapter goodsListAdapter;

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
        ScaleInAnimationAdapter animationAdapter = new ScaleInAnimationAdapter(goodsListAdapter);
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
                startActivity(intent);
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
                if(goodsRecyclerView.getVisibility() == View.VISIBLE) {
                    goodsRecyclerView.setVisibility(View.GONE);
                    shopListView.setVisibility(View.VISIBLE);
                    switchButton.setImageResource(R.drawable.mainpage);
                }
                else if(shopListView.getVisibility() == View.VISIBLE) {
                    goodsRecyclerView.setVisibility(View.VISIBLE);
                    shopListView.setVisibility(View.GONE);
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
                    startActivity(intent);
                }
            }
        });


        //注册订阅者
        EventBus.getDefault().register(this);


        //发送(随机)推荐（静态）
        Random random = new Random();
        int index = random.nextInt(goodsList.size());
        Bundle bundle = new Bundle();
        bundle.putString("name", goodsList.get(index).get("name").toString());
        bundle.putString("price", goodsList.get(index).get("price").toString());
        bundle.putInt("icon", (Integer) goodsList.get(index).get("icon"));
        Intent intentBroadcast = new Intent("STATIC_ACTION");
        intentBroadcast.putExtras(bundle);
        sendBroadcast(intentBroadcast);
    }

    //准备订阅者
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        String name = event.getName();
        String price = event.getPrice();
        if (name != null && price !=null) {
            Map<String, Object> tmp = new LinkedHashMap<>();
            tmp.put("letter", name.substring(0, 1));
            tmp.put("name", name);
            tmp.put("price", price);
            shopList.add(tmp);
            simpleAdapter.notifyDataSetChanged();
        }
    }

    //转到购物车列表
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Bundle extras = intent.getExtras();
        if(extras != null) {
            if(extras.getString("shoppingList").equals("run")) {
                goodsRecyclerView.setVisibility(View.GONE);
                shopListView.setVisibility(View.VISIBLE);
                switchButton.setImageResource(R.drawable.mainpage);
            }
        }
        setIntent(intent);
    }

    //退出时注销
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    //商品列表
    private void initGoodsList() {
        String[] names = new String[]{"Enchated Forest", "Arla Milk", "Devondale Milk", "Kindle Oasis",
                "waitrose 早餐麦片", "Mcvitie's 饼干", "Ferrero Rocher", "Maltesers", "Lindt", "Borggreve"};
        String[] prices = new String[]{"¥ 5.00", "¥ 59.00", "¥ 79.00", "¥ 2399.00", "¥ 179.00",
                "¥ 14.90", "¥ 132.59","¥ 141.43","¥ 139.43","¥ 28.90"};
        int[] icons = {R.mipmap.enchatedforest, R.mipmap.arla, R.mipmap.devondale, R.mipmap.kindle,
                R.mipmap.waitrose, R.mipmap.mcvitie, R.mipmap.ferrero, R.mipmap.maltesers, R.mipmap.lindt,
                R.mipmap.borggreve};
        for (int i = 0; i < 10; i++) {
            Map<String, Object> temp = new LinkedHashMap<>();
            temp.put("letter", names[i].substring(0, 1));
            temp.put("name", names[i]);
            temp.put("price", prices[i]);
            temp.put("icon", icons[i]);
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