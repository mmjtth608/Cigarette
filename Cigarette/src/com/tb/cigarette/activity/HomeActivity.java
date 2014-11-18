package com.tb.cigarette.activity;

import java.util.Random;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tb.cigarette.adapter.ImageAdapter;
import com.tb.cigarette.common.Callback;
import com.tb.cigarette.common.Invoker;
import com.tb.cigarette.common.Util;
import com.tb.cigarette.fragment.HomeFragment;
import com.tb.cigarette.fragment.ListHomeFragment;
import com.tb.cigarette.widget.DragLayout;
import com.tb.cigarette.widget.DragLayout.DragListener;

public class HomeActivity extends FragmentActivity {

	private DragLayout dl;
	private GridView gv_img;
	private ImageAdapter adapter;
	private ListView lv;
	private TextView tv_noimg;
	private ImageView iv_bottom;
	private ActionBar actionBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_homegrid);
		// Util.initImageLoader(this);
		initDragLayout();
		initActionBar();
		initView();
		changeFragment(new ListHomeFragment());
	}

	private void changeFragment(Fragment targetFragment) {
		getSupportFragmentManager()
				.beginTransaction()
				.replace(R.id.main_fragment, targetFragment,
						targetFragment.getClass().getSimpleName())
				.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
				.commit();
	}

	private void initActionBar() {
		// TODO Auto-generated method stub
		actionBar = getActionBar();
		actionBar.setTitle("浙江中烟产品展示平台");
		actionBar.setIcon(R.drawable.icon_menu);
		actionBar.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.actionbar_background));
		// actionBar.setHomeButtonEnabled(true);
	}

	private void initDragLayout() {
		dl = (DragLayout) findViewById(R.id.dl);
		dl.setDragListener(new DragListener() {
			@Override
			public void onOpen() {
				lv.smoothScrollToPosition(new Random().nextInt(30));
			}

			@Override
			public void onClose() {
				shake();
			}

			@Override
			public void onDrag(float percent) {
				// ViewHelper.setAlpha(iv_icon, 1 - percent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
			dl.open();
			return false;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void initView() {
		iv_bottom = (ImageView) findViewById(R.id.iv_bottom);
		gv_img = (GridView) findViewById(R.id.gv_img);
		tv_noimg = (TextView) findViewById(R.id.iv_noimg);
		gv_img.setFastScrollEnabled(true);
		adapter = new ImageAdapter(this);
		// gv_img.setAdapter(adapter);
		// gv_img.setOnItemClickListener(new OnItemClickListener() {
		// @Override
		// public void onItemClick(AdapterView<?> parent, View view,
		// int position, long id) {
		// Intent intent = new Intent(HomeActivity.this,
		// ImageActivity.class);
		// intent.putExtra("path", adapter.getItem(position));
		// startActivity(intent);
		// }
		// });
		lv = (ListView) findViewById(R.id.lv);
		lv.setAdapter(new ArrayAdapter<String>(HomeActivity.this,
				R.layout.item_text, new String[] { "NewBee", "ViCi Gaming",
						"Evil Geniuses", "Team DK", "Invictus Gaming", "LGD",
						"Natus Vincere", "Team Empire", "Alliance", "Cloud9",
						"Titan", "Mousesports", "Fnatic", "Team Liquid",
						"MVP Phoenix", "NewBee", "ViCi Gaming",
						"Evil Geniuses", "Team DK", "Invictus Gaming", "LGD",
						"Natus Vincere", "Team Empire", "Alliance", "Cloud9",
						"Titan", "Mousesports", "Fnatic", "Team Liquid",
						"MVP Phoenix" }));
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				Util.t(getApplicationContext(), "click " + position);
			}
		});
		// iv_icon.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View arg0) {
		// dl.open();
		// }
		// });
	}

	@Override
	protected void onResume() {
		super.onResume();
		// loadImage();
	}

	@SuppressWarnings("unused")
	private void loadImage() {
		new Invoker(new Callback() {
			@Override
			public boolean onRun() {
				adapter.addAll(Util.getGalleryPhotos(HomeActivity.this));
				return adapter.isEmpty();
			}

			@Override
			public void onBefore() {
				// 转菊花
			}

			@Override
			public void onAfter(boolean b) {
				adapter.notifyDataSetChanged();
				if (b) {
					tv_noimg.setVisibility(View.VISIBLE);
				} else {
					tv_noimg.setVisibility(View.GONE);
					String s = "file://" + adapter.getItem(0);
					// ImageLoader.getInstance().displayImage(s, iv_icon);
					ImageLoader.getInstance().displayImage(s, iv_bottom);
				}
				shake();
			}
		}).start();

	}

	private void shake() {
		// iv_icon.startAnimation(AnimationUtils.loadAnimation(this,
		// R.anim.shake));
	}

}
