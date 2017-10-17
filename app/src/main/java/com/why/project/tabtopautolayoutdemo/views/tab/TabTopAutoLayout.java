package com.why.project.tabtopautolayoutdemo.views.tab;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.why.project.tabtopautolayoutdemo.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by HaiyuKing
 * Used 顶部动态选项卡数据且可滑动
 */

public class TabTopAutoLayout extends LinearLayout {

	private Context mContext;
	//选项卡标题
	//CharSequence与String都能用于定义字符串，但CharSequence的值是可读可写序列，而String的值是只读序列。
	private CharSequence[] toptab_Titles = {""};

	//选项卡的各个选项的view的集合：用于更改背景颜色
	private List<View> toptab_Items = new ArrayList<View>();
	//选项卡的各个选项的标题的集合：用于切换时改变文字颜色
	private List<TextView> topTab_titles = new ArrayList<TextView>();
	//选项卡的各个选项的下划线的集合：用于切换时改变下划线颜色
	private List<View> topTab_underlines = new ArrayList<View>();

	public TabTopAutoLayout(Context context, AttributeSet attrs) {
		super(context, attrs);

		mContext = context;

		List<CharSequence> toptab_titleList = new ArrayList<CharSequence>();
		toptab_titleList = Arrays.asList(toptab_Titles);
		//初始化view：创建多个view对象（引用tab_bottom_item文件），设置图片和文字，然后添加到这个自定义类的布局中
		initAddBottomTabItemView(toptab_titleList);
	}

	//初始化控件
	private void initAddBottomTabItemView(List<CharSequence> tabTitleList){

		int countChild = this.getChildCount();
		if(countChild > 0){
			this.removeAllViewsInLayout();//清空控件
			//将各个选项的view添加到集合中
			toptab_Items.clear();
			//将各个选项卡的各个选项的标题添加到集合中
			topTab_titles.clear();
			//将各个选项卡的各个选项的下划线添加到集合中
			topTab_underlines.clear();
		}

		//设置要添加的子布局view的参数
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//		params.weight = 1;//在tab_item文件的根节点RelativeLayout中是无法添加的，而这个是必须要写上的，否则只会展现一个view
		params.gravity = Gravity.CENTER;

		for(int index=0;index<tabTitleList.size();index++){

			final int finalIndex = index;

			//============引用选项卡的各个选项的布局文件=================
			View toptabitemView = LayoutInflater.from(mContext).inflate(R.layout.tab_top_auto_item, this, false);

			//===========选项卡的根布局==========
			RelativeLayout toptabLayout = (RelativeLayout) toptabitemView.findViewById(R.id.toptabLayout);

			//===========设置选项卡的文字==========
			final TextView top_title = (TextView) toptabitemView.findViewById(R.id.top_title);
			//设置选项卡的文字
			top_title.setText(tabTitleList.get(index));
			//===========设置选项卡控件的Tag(索引)==========用于后续的切换更改图片和文字
			top_title.setTag("tag"+index);

			//===========设置选项卡控件的下划线【不能使用View，否则setWidth不能用】==========
			final TextView top_underline = (TextView) toptabitemView.findViewById(R.id.top_underline);
			//设置下划线的宽度值==根布局的宽度
			top_title.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
			Log.w("why", "top_title.getMeasuredWidth()="+top_title.getMeasuredWidth());
			toptabLayout.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
			Log.w("why", "toptabLayout.getMeasuredWidth()="+toptabLayout.getMeasuredWidth());
			top_underline.setWidth(toptabLayout.getMeasuredWidth());//手动设置下划线的宽度值

			//添加选项卡各个选项的触发事件监听
			toptabitemView.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					//设置当前选项卡状态为选中状态
					//修改View的背景颜色
					setTabsDisplay(finalIndex);
					//添加点击事件
					if(topTabSelectedListener != null){
						//执行activity主类中的onBottomTabSelected方法
						topTabSelectedListener.onTopTabSelected(finalIndex);
					}
				}
			});

			//把这个view添加到自定义的布局里面
			this.addView(toptabitemView,params);

			//将各个选项的view添加到集合中
			toptab_Items.add(toptabitemView);
			//将各个选项卡的各个选项的标题添加到集合中
			topTab_titles.add(top_title);
			//将各个选项卡的各个选项的下划线添加到集合中
			topTab_underlines.add(top_underline);

		}

	}

	/**
	 * 设置底部导航中图片显示状态和字体颜色
	 */
	public void setTabsDisplay(int checkedIndex) {

		int size = topTab_titles.size();

		for(int i=0;i<size;i++){
			TextView topTabTitle = topTab_titles.get(i);
			View top_underline = topTab_underlines.get(i);
			//设置选项卡状态为选中状态
			if(topTabTitle.getTag().equals("tag"+checkedIndex)){
				//修改文字颜色
				topTabTitle.setTextColor(getResources().getColor(R.color.tab_text_selected_top));
				//修改下划线的颜色
				top_underline.setBackgroundColor(getResources().getColor(R.color.tab_auto_selected_top));
			}else{
				//修改文字颜色
				topTabTitle.setTextColor(getResources().getColor(R.color.tab_text_normal_top));
				//修改下划线的颜色
				top_underline.setBackgroundColor(getResources().getColor(R.color.tab_auto_normal_top));
			}
		}
	}

	/**设置显示的选项卡集合*/
	public void setTabList(ArrayList<CharSequence> toptab_titleList){
		initAddBottomTabItemView(toptab_titleList);
	}

	private OnTopTabUnderLineSelectListener topTabSelectedListener;

	//自定义一个内部接口，用于监听选项卡选中的事件,用于获取选中的选项卡的下标值
	public interface OnTopTabUnderLineSelectListener{
		void onTopTabSelected(int index);
	}

	public void setOnTopTabSelectedListener(OnTopTabUnderLineSelectListener topTabSelectedListener){
		this.topTabSelectedListener = topTabSelectedListener;
	}
}
