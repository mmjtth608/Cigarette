package com.tb.cigarette.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.frontia.Frontia;
import com.baidu.frontia.FrontiaUser;
import com.baidu.frontia.api.FrontiaAuthorization;
import com.baidu.frontia.api.FrontiaAuthorization.MediaType;
import com.baidu.frontia.api.FrontiaAuthorizationListener.AuthorizationListener;
import com.baidu.frontia.api.FrontiaAuthorizationListener.UserInfoListener;
import com.baidu.frontia.api.FrontiaSocialShare;
import com.baidu.frontia.api.FrontiaSocialShare.FrontiaTheme;
import com.baidu.frontia.api.FrontiaSocialShareContent;
import com.baidu.frontia.api.FrontiaSocialShareListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.tb.cigarette.adapter.ImageAdapter;
import com.tb.cigarette.common.Callback;
import com.tb.cigarette.common.Invoker;
import com.tb.cigarette.common.Util;
import com.tb.cigarette.common.Utility;
import com.tb.cigarette.fragment.ListHomeFragment;
import com.tb.cigarette.manager.CigaretteManager;
import com.tb.cigarette.model.SearchParams;
import com.tb.cigarette.widget.DragLayout;
import com.tb.cigarette.widget.DragLayout.DragListener;
import com.tb.cigarette.widget.DragLayout.DragStatus;
import com.tb.cigarette.widget.SegmentedRadioGroup;

@SuppressLint("NewApi")
public class HomeActivity extends FragmentActivity implements
		OnCheckedChangeListener {

	private DragLayout dl;
	private GridView gv_img;
	private ImageAdapter adapter;
	private ListView lv;
	private TextView tv_noimg;
	private ImageView iv_bottom;
	private ActionBar actionBar;
	private FrontiaSocialShare mSocialShare;
	private FrontiaSocialShareContent mImageContent = new FrontiaSocialShareContent();
	private FrontiaAuthorization mAuthorization;
	private TextView tv_name;

	protected ImageLoader imageLoader;
	private DisplayImageOptions options;
	private ImageLoadingListener animateFirstListener;
	private final static String Scope_Basic = "basic";
	private final static String Scope_Netdisk = "netdisk";
	private boolean isFirst = true;
	private SegmentedRadioGroup segmentText;
	private ArrayList<String> filterList = new ArrayList<String>();
	private CigaretteManager mCigaretteManager = null;
	private ArrayAdapter<String> mAdapter;
	private SearchParams searchParams = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_homegrid);
		// Util.initImageLoader(this);
		initDragLayout();
		initActionBar();
		initView();
		initData();
		changeFragment(new ListHomeFragment());
		startBaiduStatus();
	}

	private void initData() {
		// TODO Auto-generated method stub
		imageLoader = ImageLoader.getInstance();
		options = Utility.getUserIconDisplayOption();
		mCigaretteManager = CigaretteManager.getInstance(this);
		animateFirstListener = new AnimateFirstDisplayListener();
		mAuthorization = Frontia.getAuthorization();
		mSocialShare = Frontia.getSocialShare();
		mSocialShare.setContext(this);
		mSocialShare.setClientId(MediaType.WEIXIN.toString(),
				"wx329c742cb69b41b8");
		mImageContent.setTitle("浙江中烟");
		mImageContent.setContent("欢迎使用浙江中烟展示平台，相关问题请邮件15152626115@126.com");
		mImageContent
				.setLinkUrl("http://zjgy.tobacco.com.cn/nportal/portal/s/zjzyout/");
		mImageContent
				.setImageUri(Uri
						.parse("http://zjgy.tobacco.com.cn/np_application/zjzy/waiwang/images/index_02.jpg"));
		filterList = mCigaretteManager.loadPinpai();
		segmentText.setTag(0);
		mAdapter = new ArrayAdapter<String>(HomeActivity.this,
				R.layout.item_text, filterList);
		lv.setAdapter(mAdapter);
		searchParams = new SearchParams();
	}

	/**
	 * 加载图片用
	 * 
	 * @author Administrator
	 * 
	 */
	private static class AnimateFirstDisplayListener extends
			SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections
				.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view,
				Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}

	/**
	 * 
	 * @param targetFragment
	 */
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
		actionBar.setHomeButtonEnabled(true);
	}

	private void initDragLayout() {
		dl = (DragLayout) findViewById(R.id.dl);
		dl.setDragListener(new DragListener() {
			@Override
			public void onOpen() {
				// lv.smoothScrollToPosition(new Random().nextInt(30));
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

	// @Override
	// public boolean onOptionsItemSelected(MenuItem item) {
	// // TODO Auto-generated method stub
	// switch (item.getItemId()) {
	// case android.R.id.home:
	// dl.open();
	// return false;
	// default:
	// break;
	// }
	// return super.onOptionsItemSelected(item);
	// }

	private void initView() {
		tv_name = (TextView) findViewById(R.id.tv_name);
		segmentText = (SegmentedRadioGroup) findViewById(R.id.segment_text);
		segmentText.setOnCheckedChangeListener(this);
		tv_name.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startBaiduStatus();
			}
		});
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
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// Util.t(getApplicationContext(), "click " + position);
				searchParams.clearParams();
				switch (Integer.parseInt(segmentText.getTag().toString())) {
				case 0:
					searchParams.setPinpai(mAdapter.getItem(position));
					break;
				case 1:
					searchParams.setDangci(mAdapter.getItem(position));
					break;
				case 2:
					searchParams.setChandi(mAdapter.getItem(position));
					break;

				default:
					break;
				}
				dl.close();
				((ListHomeFragment) (getSupportFragmentManager()
						.findFragmentByTag(ListHomeFragment.class
								.getSimpleName())))
						.getDataBySearch(searchParams);
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_main_sort, menu);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			if (dl.getStatus() == DragStatus.Open) {
				dl.close();
			} else if (dl.getStatus() == DragStatus.Close) {
				dl.open();
			}
			break;
		case R.id.action_share:
			// mSocialShare.share(mImageContent,MediaType.BATCHSHARE.toString(),new
			// ShareListener(),true);
			mSocialShare.show(HomeActivity.this.getWindow().getDecorView(),
					mImageContent, FrontiaTheme.DARK, new ShareListener());

			break;
		default:
			break;
		}
		return super.onMenuItemSelected(featureId, item);
	}

	/**
	 * 
	 * @author Administrator
	 * 
	 */
	private class ShareListener implements FrontiaSocialShareListener {

		@Override
		public void onSuccess() {
			Log.d("Test", "share success");
		}

		@Override
		public void onFailure(int errCode, String errMsg) {
			Log.d("Test", "share errCode " + errCode);
		}

		@Override
		public void onCancel() {
			Log.d("Test", "cancel ");
		}
	}

	/**
 * 
 */
	protected void startBaidu() {
		ArrayList<String> scope = new ArrayList<String>();
		scope.add(Scope_Basic);
		scope.add(Scope_Netdisk);
		mAuthorization.authorize(this,
				FrontiaAuthorization.MediaType.BAIDU.toString(), scope,
				new AuthorizationListener() {

					@Override
					public void onSuccess(FrontiaUser result) {
						String log = "social id: " + result.getId() + "\n"
								+ "token: " + result.getAccessToken() + "\n"
								+ "expired: " + result.getExpiresIn();
						Log.d("SocialDialog", log);
						// Toast.makeText(HomeActivity.this, log,
						// Toast.LENGTH_SHORT).show();
						startBaiduUserInfo();
					}

					@Override
					public void onFailure(int errCode, String errMsg) {
						Toast.makeText(HomeActivity.this,
								"errCode:" + errCode + ", errMsg:" + errMsg,
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onCancel() {
						Toast.makeText(HomeActivity.this, "cancel",
								Toast.LENGTH_SHORT).show();
					}

				});
	}

	/**
 * 
 */
	protected void startBaiduUserInfo() {
		userinfo(MediaType.BAIDU.toString());

	}

	/**
	 * 
	 * @param accessToken
	 */
	private void userinfo(String accessToken) {
		mAuthorization.getUserInfo(accessToken, new UserInfoListener() {

			@Override
			public void onSuccess(FrontiaUser.FrontiaUserDetail result) {
				String resultStr = "username:" + result.getName() + "\n"
						+ "birthday:" + result.getBirthday() + "\n" + "city:"
						+ result.getCity() + "\n" + "province:"
						+ result.getProvince() + "\n" + "sex:"
						+ result.getSex() + "\n" + "pic url:"
						+ result.getHeadUrl() + "\n";
				// Toast.makeText(HomeActivity.this, resultStr,
				// Toast.LENGTH_SHORT)
				// .show();
				tv_name.setText(result.getName());
				imageLoader.displayImage(result.getHeadUrl(), iv_bottom,
						options, animateFirstListener);
			}

			@Override
			public void onFailure(int errCode, String errMsg) {
				Toast.makeText(HomeActivity.this,
						"errCode:" + errCode + ", errMsg:" + errMsg,
						Toast.LENGTH_SHORT).show();
			}

		});
	}

	/**
 * 
 */
	protected void startBaiduStatus() {
		boolean result = mAuthorization
				.isAuthorizationReady(FrontiaAuthorization.MediaType.BAIDU
						.toString());
		if (result) {
			// mResultTextView.setText("已经登录QQ空间帐号");
			if (isFirst) {
				startBaiduUserInfo();
			} else {
				Toast.makeText(HomeActivity.this, "已经登录帐号", Toast.LENGTH_SHORT)
						.show();
			}
		} else {
			// mResultTextView.setText("未登录QQ空间帐号");
			if (!isFirst) {
				startBaidu();
			}
		}
		isFirst = false;
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		if (group == segmentText) {
			mAdapter.clear();
			if (checkedId == R.id.button_one) {
				segmentText.setTag(0);
				filterList = mCigaretteManager.loadPinpai();
			} else if (checkedId == R.id.button_two) {
				filterList = mCigaretteManager.loadDangci();
				segmentText.setTag(1);
			} else if (checkedId == R.id.button_three) {
				filterList = mCigaretteManager.loadChandi();
				segmentText.setTag(2);
			}
			mAdapter.addAll(filterList);
			mAdapter.notifyDataSetChanged();
		}
	}
}
