package com.tb.cigarette.activity;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;

import com.tb.cigarette.common.LogUtil;
import com.tb.cigarette.fragment.HomeFragment;
import com.tb.cigarette.widget.ResideMenu;
import com.tb.cigarette.widget.ResideMenuItem;

public class MainActivity extends FragmentActivity implements OnClickListener {
	private ResideMenu resideMenu = null;
	private ResideMenuItem itemHome;
	private ResideMenuItem itemProfile;
	private ResideMenuItem itemCalendar;
	private ResideMenuItem itemSettings;
	private ActionBar actionBar = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
		bindEvents();
	}

	private void init() {
		// TODO Auto-generated method stub
		initActionbar();

		resideMenu = new ResideMenu(this);
		resideMenu.setBackground(R.drawable.bg_loading);
		resideMenu.attachToActivity(this);
		resideMenu.setMenuListener(menuListener);
		resideMenu.setScaleValue(0.6f);

		// create menu items;
		itemHome = new ResideMenuItem(this, R.drawable.ic_launcher, "Home");
		itemProfile = new ResideMenuItem(this, R.drawable.ic_launcher,
				"Profile");
		itemCalendar = new ResideMenuItem(this, R.drawable.ic_launcher,
				"Calendar");
		itemSettings = new ResideMenuItem(this, R.drawable.ic_launcher,
				"Settings");

		itemHome.setOnClickListener(this);
		itemProfile.setOnClickListener(this);
		itemCalendar.setOnClickListener(this);
		itemSettings.setOnClickListener(this);

		resideMenu.addMenuItem(itemHome, ResideMenu.DIRECTION_LEFT);
		resideMenu.addMenuItem(itemProfile, ResideMenu.DIRECTION_LEFT);
		resideMenu.addMenuItem(itemCalendar, ResideMenu.DIRECTION_RIGHT);
		resideMenu.addMenuItem(itemSettings, ResideMenu.DIRECTION_RIGHT);
		// resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
		// resideMenu.openMenu(ResideMenu.DIRECTION_RIGHT);
		changeFragment(new HomeFragment());
	}

	private void changeFragment(Fragment targetFragment) {
		resideMenu.clearIgnoredViewList();
		getSupportFragmentManager()
				.beginTransaction()
				.replace(R.id.main_fragment, targetFragment,
						targetFragment.getClass().getSimpleName())
				.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
				.commit();
	}

	/*
	 * 初始化actionbar
	 */
	private void initActionbar() {
		actionBar = getActionBar();
		actionBar.setTitle("浙江中烟");
		actionBar.setIcon(R.drawable.icon_menu);
		actionBar.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.actionbar_background));
		actionBar.setHomeButtonEnabled(true);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		return resideMenu.dispatchTouchEvent(ev);
	}

	private void bindEvents() {
		// TODO Auto-generated method stub

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
			if (!resideMenu.isOpened()) {
				resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
			}
			return false;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private ResideMenu.OnMenuListener menuListener = new ResideMenu.OnMenuListener() {
		@Override
		public void openMenu() {
			// Toast.makeText(MainActivity.this, "Menu is opened!",
			// Toast.LENGTH_SHORT).show();
			LogUtil.v(getPackageName(), "Menu is opened!");
		}

		@Override
		public void closeMenu() {
			// Toast.makeText(MainActivity.this, "Menu is closed!",
			// Toast.LENGTH_SHORT).show();
			LogUtil.v(getPackageName(), "Menu is closed!");
		}
	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	// What good method is to access resideMenu？
	public ResideMenu getResideMenu() {
		return resideMenu;
	}
}
