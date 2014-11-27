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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tb.cigarette.activity.R;
import com.tb.cigarette.common.ImageLoader;
import com.tb.cigarette.manager.CigaretteManager;
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
	private MyistAdapter mAdapter;
	private EditText et_key;
	private Button btn_cancle;
	private CigaretteManager mCigaretteManager = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		parentView = inflater.inflate(R.layout.home_list, container, false);
		setUpViews();
		return parentView;
	}

	private void setUpViews() {
		mCigaretteManager = CigaretteManager.getInstance(getActivity());
		listView = (ZrcListView) parentView.findViewById(R.id.lv);
		imageLoader = new ImageLoader(R.drawable.ic_launcher, getActivity(),
				false);
		handler = new Handler();
		// 设置下拉刷新的样式
		SimpleHeader header = new SimpleHeader(getActivity());
		header.setTextColor(0xffCD2626);
		header.setCircleColor(0xffCD2626);
		listView.setHeadable(header);

		// 设置加载更多的样式
		SimpleFooter footer = new SimpleFooter(getActivity());
		footer.setCircleColor(0xffCD2626);
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
		View headerView = LayoutInflater.from(getActivity()).inflate(
				R.layout.view_searchheader, null);
		et_key = (EditText) headerView.findViewById(R.id.et_search);
		btn_cancle = (Button) headerView.findViewById(R.id.ib_search_cancel);
		listView.addHeaderView(headerView);

		mAdapter = new MyistAdapter();
		listView.setAdapter(mAdapter);
		listView.refresh(); // 主动下拉刷新
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
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				getLoaderManager().restartLoader(LOADER_ID, null,
						ListHomeFragment.this);
			}
		}, 2000);
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
		if (arg1 != null && arg1.size() > 0) {
			cigarettes = arg1;
			mAdapter.notifyDataSetChanged();
			listView.setRefreshSuccess("加载成功"); // 通知加载成功
		} else {
			listView.setRefreshSuccess("加载成功"); // 通知加载成功
		}
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
		public View getView(final int position, View convertView,
				ViewGroup parent) {
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
				viewholder.target_attent = (ImageView) convertView
						.findViewById(R.id.target_attent);
				convertView.setTag(viewholder);
			} else {
				viewholder = (ViewHolder) convertView.getTag();
			}
			viewholder.tv_name.setText(cigarettes.get(position).getName());
			viewholder.tv_price.setText("价格："
					+ cigarettes.get(position).getShoujia() + "元/条");
			viewholder.tv_address.setText("厂家："
					+ cigarettes.get(position).getChangjia());
			viewholder.target_attent.setImageDrawable(cigarettes.get(position)
					.getShare() == 0 ? getActivity().getResources()
					.getDrawable(R.drawable.ic_menu_target_list_attent)
					: getActivity().getResources().getDrawable(
							R.drawable.ic_menu_target_list_attent_click));
			imageLoader.DisplayImage(cigarettes.get(position).getImg(),
					viewholder.icon_img);
			viewholder.target_attent.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (cigarettes.get(position).getShare() == 0) {
						cigarettes.get(position).setShare(1);
					} else {
						cigarettes.get(position).setShare(0);
					}
					mCigaretteManager.updateCigarette(cigarettes.get(position));
					notifyDataSetChanged();
				}
			});
			return convertView;
		}

	}

	class ViewHolder {
		CircleImageView icon_img;
		TextView tv_name;
		TextView tv_price;
		TextView tv_address;
		ImageView target_attent;
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		return super.onOptionsItemSelected(item);
	}

}
