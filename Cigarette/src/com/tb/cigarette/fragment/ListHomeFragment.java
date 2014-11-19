package com.tb.cigarette.fragment;

import java.util.ArrayList;

import zrc.widget.SimpleFooter;
import zrc.widget.SimpleHeader;
import zrc.widget.ZrcListView;
import zrc.widget.ZrcListView.OnStartListener;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tb.cigarette.activity.R;
import com.tb.cigarette.common.ImageLoader;
import com.tb.cigarette.model.Cigarette;
import com.tb.cigarette.task.CigaretteLoader;
import com.tb.cigarette.widget.CircleImageView;
import com.tb.cigarette.widget.LazyScrollView.OnScrollListener;

public class ListHomeFragment extends Fragment implements
		LoaderCallbacks<ArrayList<Cigarette>>, OnScrollListener {

	private int LOADER_ID = 1001;

	private View parentView;

	// private List<String> image_filenames; // 图片集合
	ArrayList<Cigarette> cigarettes = new ArrayList<Cigarette>();

	private ZrcListView listView;
	private ImageLoader imageLoader;
    private Handler handler;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		parentView = inflater.inflate(R.layout.home_list, container, false);
		setUpViews();
		return parentView;
	}

	private void setUpViews() {
		listView = (ZrcListView) parentView.findViewById(R.id.lv);
		imageLoader = new ImageLoader(R.drawable.ic_launcher, getActivity(),
				false);
        handler = new Handler();
		// 设置下拉刷新的样式
		SimpleHeader header = new SimpleHeader(getActivity());
		header.setTextColor(0xff0066aa);
		header.setCircleColor(0xff33bbee);
		listView.setHeadable(header);

		// 设置加载更多的样式
		SimpleFooter footer = new SimpleFooter(getActivity());
		footer.setCircleColor(0xff33bbee);
		listView.setFootable(footer);

		// 设置列表项出现动画
		listView.setItemAnimForTopIn(R.anim.topitem_in);
		listView.setItemAnimForBottomIn(R.anim.bottomitem_in);

		// 下拉刷新事件回调
		listView.setOnRefreshStartListener(new OnStartListener() {
			@Override
			public void onStart() {
				reloadData();
			}
		});

		// 加载更多事件回调
		listView.setOnLoadMoreStartListener(new OnStartListener() {
			@Override
			public void onStart() {
				// loadMore();
			}
		});
		listView.setRefreshSuccess("加载成功");
		listView.setRefreshFail("加载失败");
//		listView.stopLoadMore();
		listView.refresh(); // 主动下拉刷新
		// reloadData();
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
		
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
            	MyistAdapter mAdapter = new MyistAdapter();
        		listView.setAdapter(mAdapter);
                listView.setRefreshSuccess("加载成功"); // 通知加载成功
                listView.startLoadMore(); // 开启LoadingMore功能
            }
        }, 2 * 1000);
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
