package com.example.danboard.lab3;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Danboard on 2017/10/23.
 */

public class GoodsInformation extends AppCompatActivity{

    String choiceName = null;
    String choicePrice = null;
    String data = null;
    int pos = 0;

    private List<Map<String, Object>> inforList = new ArrayList<>();
    private List<Map<String, Object>> otherList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.information);

        /*
         *初始化信息
         */
        String[] names = new String[]{"Enchated Forest", "Arla Milk", "Devondale Milk", "Kindle Oasis", "waitrose 早餐麦片",
                "Mcvitie's 饼干", "Ferrero Rocher","Maltesers","Lindt","Borggreve"};
        String[] prices = new String[]{"¥ 5.00", "¥ 59.00", "¥ 79.00", "¥ 2399.00", "¥ 179.00",
                "¥ 14.90", "¥ 132.59","¥ 141.43","¥ 139.43","¥ 28.90"};
        final String[] infors = new String[]{"作者 Johanna Basford","产地 德国","产地 澳大利亚","版本 8GB",
                "重量 2Kg","产地 英国","重量 300g","重量 118g","重量 249g","重量 640g"};
        for(int i = 0; i < 10; i++) {
            Map<String, Object> temp = new LinkedHashMap<>();
            temp.put("name", names[i]);
            temp.put("price", prices[i]);
            temp.put("infor", infors[i]);
            inforList.add(temp);
        }

        /*
         *响应事件
         */
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            data = extras.getString("name");
            for(int i = 0; i < inforList.size(); i++) {
                if(inforList.get(i).get("name").toString().equals(data)) {
                    pos = i;
                }
            }
        }
        TextView goodsName = (TextView) findViewById(R.id.goodsName);
        goodsName.setText(data);

        int[] imageId = {R.drawable.enchatedforest, R.drawable.arla, R.drawable.devondale, R.drawable.kindle,
                R.drawable.waitrose, R.drawable.mcvitie, R.drawable.ferrero, R.drawable.maltesers,
                R.drawable.borggreve};
        ImageView goodsImage = (ImageView) findViewById(R.id.imageInfor);
        int resId = imageId[pos];
        goodsImage.setImageResource(resId);

        TextView goodsPrice = (TextView) findViewById(R.id.goodsPrice);
        goodsPrice.setText(inforList.get(pos).get("price").toString());

        TextView goodsInfor = (TextView) findViewById(R.id.goodsInfor);
        goodsInfor.setText(inforList.get(pos).get("infor").toString());

        /*
         *返回商品列表
         */
        ImageButton back = (ImageButton) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("name", choiceName);
                intent.putExtra("price", choicePrice);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        /*
         *星星图标切换
         */

        final ImageButton star = (ImageButton)findViewById(R.id.star);
        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(star.getTag().toString().equals("0")) {
                    star.setTag("1");
                    star.setImageResource(R.drawable.full_star);
                }
                else if(star.getTag().toString().equals("1")) {
                    star.setTag("0");
                    star.setImageResource(R.drawable.empty_star);
                }
            }
        });

        /*
         *添加购物车
         */
        ImageButton car = (ImageButton) findViewById(R.id.car);
        car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choiceName = inforList.get(pos).get("name").toString();
                choicePrice = inforList.get(pos).get("price").toString();
                Toast.makeText(GoodsInformation.this, "商品已添加到购物车", Toast.LENGTH_SHORT).show();
            }
        });
        /*
         *其他功能列表
         */
        String[] function = {"一键下单", "分享商品", "不感兴趣", "查看更多商品促销信息"};
        for(int i = 0; i < 4; i++) {
            Map<String,Object> temp = new LinkedHashMap<>();
            temp.put("function", function[i]);
            otherList.add(temp);
        }
        ListView otherListView = (ListView)findViewById(R.id.otherListView);
        SimpleAdapter moreListAdapter = new SimpleAdapter(this, otherList, R.layout.otherlist_layout,
                new String[]{"function"}, new int[]{R.id.otherList});
        otherListView.setAdapter(moreListAdapter);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("name", choiceName);
        intent.putExtra("price", choicePrice);
        setResult(RESULT_OK, intent);
        finish();
    }
}