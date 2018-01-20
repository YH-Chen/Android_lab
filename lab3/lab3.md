# 中山大学移动信息工程学院本科生实验报告
# （2017年秋季学期）

课程名称:移动应用开发 &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp; 任课教师： 郑贵锋
---
|  年级  | 2015级 | 专业（方向） | 移动互联网 |
| :--: | :---: | :----: | :---: |
|  学号  |       |   姓名   |       |
|  电话  |       | Email  |       |
| 开始日期 |       |  完成日期  |       |
---

## 一、实验题目

 Intent、Bundle的使用以及RecycleView、ListView的应用

## 二、实验目的

1. 复习事件处理
2. 学习Intent、bundle在Activity跳转中的应用
3. 学习Recyleview、ListView以及各类适配器的用法

## 三、实现内容

1. 商品表界面
> 每一项为一个圆圈和一个名字，圆圈与名字均竖直居中。圆圈中为字母的首字母，首字母要出于圆圈的中心，首字母为白色，名字为黑色，圆圈的颜色自定义。

2. 购物列表界面
> 在商品表界面的基础上增加一个价格，价格为黑色

3. 商品详情界面顶部
> 顶部占整个界面的1/3，图片与这块view等高。返回图标出于这块view的左上角，商品名字出于左下角，星标处于右下角，它们与边距有一定距离。返回图标与名字左对齐，名字与星标底边对齐。

4. 商品详情界面中部
> 使用的黑色 argb 编码值为#D5000000，稍微偏灰色一点的“类型”、“其他信息”的 argb 编码值为#8A000000。价格那一栏的下边有一条分割线，argb 编码值为 #1E000000，右边购物车符号的左边也有一条分割线，argb 编码值也是#1E000000，这条分割线要求高度与购物车符号的高度一致，并且竖直居中。字体的大小看着调就可以了。“更多资料”底部的分割线高度自定，argb 编码值与前面的分割线一致。

5. 商品详情界面底部
6. 两个都没有标题栏

## 四、课堂实验结果

### 1. 实验截图

<div align=center>

商品列表界面
<img src="https://i.imgur.com/Nzqxmgm.jpg" width="250dp" height="400dp"/>

长按删除
<img src="https://i.imgur.com/rB60NnC.jpg" width="250dp" height="400dp"/>

商品详情界面
<img src="https://i.imgur.com/pvnZYyl.jpg" width="250dp" height="400dp"/>

购物车列表
<img src="https://i.imgur.com/1trfWEN.jpg" width="250dp" height="400dp"/>

购物车删除
<img src="https://i.imgur.com/gpvrUR7.jpg" width="250dp" height="400dp"/>

</div>

### 2. 实验步骤以及关键代码

1.  ListView创建空列表，id为shoppinglist即表示购物车列表， 在java文件中用适配器Adapter填充数据，数据包含商品名称`name`，商品价格`price`以及名称首字母`letter`。设置监听器，实现长按和单击不同的功能。

 .xml
```xml
    <ListView
        android:id="@+id/shoppinglist"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:visibility="gone"/>			//隐藏布局，首页面为商品列表
```
.java
```Java
    shopListView = (ListView) findViewById(R.id.shoppinglist);
    simpleAdapter = new SimpleAdapter(this, shopList, R.layout.shoplist_layout,
            new String[]{"letter", "name", "price"}, new int[]{R.id.letter, R.id.name, R.id.price});
    shopListView.setAdapter(simpleAdapter);  //匹配数据

		//长按
	shopListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {……}
    });
	//单击
    shopListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {……}
    });
```

2. RecycleView布局实现商品列表，id为goodslist。因为RecycleView是support-v7包中的新组建，提供了插拔式体验，相比LayoutView更加灵活。使用时需要添加相关依赖，具体操作可以快捷键ctrl+shift+Alt+s添加。

.xml
```xml
<android.support.v7.widget.RecyclerView xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="368dp"
    android:layout_height="wrap_content"
    android:id="@+id/goodslist"
    android:visibility="visible"		//显示布局
    app:layout_constraintTop_toTopOf="parent"
    app:layout_constraintLeft_toLeftOf="parent" />
```

RecycleView自定义实现RecycleView.VieWHolder，ViewHolder为的是ListView、RecycleView滚动时快速设置值，而不必每次重新创建很多对象。
```Java
public class ViewHolder extends RecyclerView.ViewHolder {
    //获取实例
    public static ViewHolder get(Context context, ViewGroup parent, int itemId) {……}
	//将子view存入一个数组中
    public <T extends View> T getView(int viewId) {……}
}
```

RecycleView自定义实现RecycleView.Adapter，根据不同视图类型创建对应的布局，并且绑定数据。
```Java
public abstract class CommonAdapter extends RecyclerView.Adapter<ViewHolder>{

    @Override			//创建对应item视图，并返回相应的ViewHolder
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {……}

    @Override			//绑定数据到正确的item视图上
    public void onBindViewHolder(final ViewHolder holder, int position) {…}

    @Override			//获取item视图总数
    public int getItemCount() {……}

    public interface OnItemClickListener {……}	//添加监听器接口函数，再在onBindViewHolder中绑定数据

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){……}
```

LayoutManager布局管理器，绑定数据；AnimationAdapter设置动画；OnItemClickListener设置监听器，实现单击与长按功能。
```Java
    goodsRecyclerView = (RecyclerView) findViewById(R.id.goodslist);
	//匹配数据
    goodsRecyclerView.setLayoutManager(new LinearLayoutManager(this));			
    //添加动画
    animationAdapter = new ScaleInAnimationAdapter(goodsListAdapter);
    animationAdapter.setDuration(1000);
    goodsRecyclerView.setAdapter(animationAdapter);
    goodsRecyclerView.setItemAnimator(new OvershootInLeftAnimator());
    //单击与长按
    goodsListAdapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener(){……});
```

3. 商品列表与购物车列表实现在同一个Activity中，通过FloatingActionButton（需要添加依赖）图片按钮实现。
```Java
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
```

4. 创建新的活动，响应单击事件，并设置该活动布局。

RelativeLayout: 相对布局，设置占顶部1/3的图片信息，包括商品图片、商品名字、返回按钮、收藏按钮；

LinerLayout：线性布局，设置下面其他的列表信息，指定id，可以通过适配器匹配数据；

View：通过设定背景颜色以及宽高来定义分隔线；

5. 设置事件响应，包括单击，返回等等

```Java
	//单击跳转（活动1）
    public void onClick(int position) {
        String goodsName = goodsList.get(position).get("name").toString();
        Intent intent = new Intent(MainActivity.this, GoodsInformation.class);
        intent.putExtra("name", goodsName);
        startActivityForResult(intent, 1);
    }
	//接收点击位置的参数（活动2）
    Bundle extras = getIntent().getExtras();
    if(extras != null) {
        data = extras.getString("name");			//“name”与活动1匹配
        for(int i = 0; i < inforList.size(); i++) {
            if(inforList.get(i).get("name").toString().equals(data)) {
                pos = i;
            }
        }
    }
	//得到pos，就可以匹配相应的数据，再显示到布局
```

### 3. 实验遇到困难以及解决思路 

>1. 为了添加一些新的控件而添加新的依赖，根据实验文档添加依赖综合后总是会报错，有时会提示版本过低等原因，但又不知道具体应该怎样添加哪个版本的，根据网上资料，通过快捷键Ctrl+Shift+Alter+S可以打开编译的依赖库，查询实验文档提示的关键词，就可以找到对应的依赖。综合后发现之前的问题没有出现，反而是弹出图片错误，原因是图片命名问题，大写字母都改为小写就解决了，不知道是故意设的bug。
>2. 使用RecyView比较麻烦，还要自定义ViewHolder与Adapter，虽然给出了代码，但还是不太理解，最后还是通过百度，找到关于RecyView的相关介绍，了解其相关功能以及其调用的一些函数的具体操作。所以觉得要用到一些特殊的控件，可以线了解其相关函数的作用，才能更好理解。
>3. 布局文件较多，对应的id混淆，调用的时候不会报错，调试的时候就是各种闪退，最后，不同文件有相同的标签的，id都要设置为不同，在事件处理中，以及适配器匹配数据的时候，需要认请变量名以及id。一个活动一般对应打开一个布局，所以活动中的id就要与布局中指定的id一一对应。实验中还因为把商品列表goodsList与购物车列表shopList搞错，导致添加进购物车的商品点击后是按商品列表中的顺序。
>4. 单击无法跳转到另一个活动，直接闪退，打开Android Monitor，显示的是事件处理intent的启动startActivity出问题，但是看了半天都没找到问题，最后将另一个活动的代码全部注释，在一点点调试，最后发现是更多商品信息下面的列表有问题，在商品详情中只定义了一个ListLayout，以为适配器自动把全部信息填充进去，而代码中是匹配列表中的每一项，即要指定TextView的id。改正错误就不会闪退了。

## 五、课后实验结果

无

## 六、实验思考及感想

>1. 感觉Android越来越难，第一次只是写布局文件，还好一些，只是调调位置，到后面要写事件处理，涉及到Java代码层，就觉得开始难了，之前没学过Java语言，对一些变量类型String（大写的S）以及重载等等都不熟，最近把Java基础语法看完才觉得与C/C++很相近，代码也比较好理解了，但对于一些控件调用的函数，现在虽然知道了它的作用，下次不一定能记得调用哪些参数。
>2. 实验文档总会有一些扩展的模块，虽然给出代码但还不是很能理解，每次用的时候总得百度一遍。实验文档内容很多，在做实验的时候跟文档的顺序都跟不上，有时候写完狗，才发现后面有的没有用到，很难定位到自己的需要实现的地方。
>3. 基础差，做得慢，但动手做还是有收获的，知道怎么去添加依赖，怎样快速添加布局以及函数等，而不是一个字母一个字母的敲。实验的安排比较紧凑，都是跳跃式学习，想要真正掌握还是多练多查。