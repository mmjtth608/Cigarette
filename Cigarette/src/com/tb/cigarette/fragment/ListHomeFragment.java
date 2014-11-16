package com.tb.cigarette.fragment;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.tb.cigarette.activity.MainActivity;
import com.tb.cigarette.activity.R;
import com.tb.cigarette.common.BitmapCache;
import com.tb.cigarette.common.ImageDownLoadAsyncTask;
import com.tb.cigarette.common.ImageLoader;
import com.tb.cigarette.common.LogUtil;
import com.tb.cigarette.common.Utility;
import com.tb.cigarette.model.Cigarette;
import com.tb.cigarette.task.CigaretteLoader;
import com.tb.cigarette.widget.CircleImageView;
import com.tb.cigarette.widget.LazyScrollView;
import com.tb.cigarette.widget.LazyScrollView.OnScrollListener;
import com.tb.cigarette.widget.ResideMenu;

public class ListHomeFragment extends Fragment implements
		LoaderCallbacks<ArrayList<Cigarette>>, OnScrollListener {

	private ResideMenu resideMenu;
	private int LOADER_ID = 1001;

	private View parentView;
	private AssetManager assetManager;

	// private List<String> image_filenames; // 图片集合
	ArrayList<Cigarette> cigarettes = new ArrayList<Cigarette>();

	private int current_page = 0;// 页码
	private int count = 20;// 每页显示的个数
	private int column = 4;// 显示列数

	private int item_width;// 每一个item的宽度
	private ListView lView;
	private ImageLoader imageLoader;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		parentView = inflater.inflate(R.layout.home_list, container, false);
		setUpViews();
		return parentView;
	}

	private void setUpViews() {
		lView = (ListView) parentView.findViewById(R.id.lv);

		item_width = getActivity().getWindowManager().getDefaultDisplay()
				.getWidth()
				/ column;
		imageLoader = new ImageLoader(R.drawable.ic_launcher, getActivity(),
				false);
		assetManager = getActivity().getAssets();

		MainActivity parentActivity = (MainActivity) getActivity();
		resideMenu = parentActivity.getResideMenu();
		reloadData();
	}

	@Override
	public void onBottom() {

	}

	@Override
	public void onTop() {

	}

	@Override
	public void onScroll() {

	}

	private void reloadData() {
		getLoaderManager().restartLoader(LOADER_ID, null, this);
	}

	@Override
	public Loader<ArrayList<Cigarette>> onCreateLoader(int arg0, Bundle arg1) {
		// TODO Auto-generated method stub
		return new CigaretteLoader(getActivity());
	}

	@Override
	public void onLoadFinished(Loader<ArrayList<Cigarette>> arg0,
			ArrayList<Cigarette> arg1) {
		// TODO Auto-generated method stub
		cigarettes = arg1;
		// ArrayList<Map<String, String>> maps = new ArrayList<Map<String,
		// String>>();
		// for (int i = 0; i < arg1.size(); i++) {
		// Map<String, String> map = new HashMap<String, String>();
		// map.put("name", arg1.get(i).getName());
		// map.put("price", arg1.get(i).getShoujia() + "元/条");
		// map.put("address", arg1.get(i).getChangjia());
		// maps.add(map);
		// }
		// 第一次加载
		// addImage(current_page, count);
		// SimpleAdapter mAdapter = new SimpleAdapter(getActivity(), maps,
		// R.layout.item_list,
		// new String[] { "name", "price", "address" }, new int[] {
		// R.id.tv_name, R.id.tv_price, R.id.tv_chandi });
		MyistAdapter mAdapter = new MyistAdapter();
		lView.setAdapter(mAdapter);

	}

	@Override
	public void onLoaderReset(Loader<ArrayList<Cigarette>> arg0) {
		// TODO Auto-generated method stub

	}

	class MyistAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return cigarettes.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return cigarettes.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder viewholder = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(getActivity()).inflate(
						R.layout.item_list, null);

				viewholder = new ViewHolder();
				viewholder.icon_img = (CircleImageView) convertView
						.findViewById(R.id.item_img);
				viewholder.tv_name = (TextView) convertView
						.findViewById(R.id.tv_name);
				viewholder.tv_price = (TextView) convertView
						.findViewById(R.id.tv_price);
				viewholder.tv_address = (TextView) convertView
						.findViewById(R.id.tv_chandi);
				convertView.setTag(viewholder);
			} else {
				viewholder = (ViewHolder) convertView.getTag();
			}
			viewholder.tv_name.setText(cigarettes.get(position).getName());
			viewholder.tv_price.setText(cigarettes.get(position).getShoujia()
					+ "元/条");
			viewholder.tv_address.setText(cigarettes.get(position)
					.getChangjia());
			imageLoader.DisplayImage(cigarettes.get(position).getImg(),
					viewholder.icon_img);
			return convertView;
		}

	}

	class ViewHolder {
		CircleImageView icon_img;
		TextView tv_name;
		TextView tv_price;
		TextView tv_address;
	}

}
