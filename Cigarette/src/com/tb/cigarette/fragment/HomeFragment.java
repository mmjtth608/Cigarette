package com.tb.cigarette.fragment;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.res.AssetManager;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.tb.cigarette.activity.R;
import com.tb.cigarette.common.ImageDownLoadAsyncTask;
import com.tb.cigarette.common.LogUtil;
import com.tb.cigarette.model.Cigarette;
import com.tb.cigarette.task.CigaretteLoader;
import com.tb.cigarette.widget.LazyScrollView.OnScrollListener;

public class HomeFragment extends Fragment implements
		LoaderCallbacks<ArrayList<Cigarette>>, OnScrollListener {

	private View parentView;
	// private ResideMenu resideMenu;
	private int LOADER_ID = 1001;
	private ScrollView lazyScrollView;
	private LinearLayout waterfall_container;
	private ArrayList<LinearLayout> linearLayouts;// 列布局

	private LinearLayout progressbar;// 进度条

	private TextView loadtext;// 底部加载view

	private AssetManager assetManager;

	// private List<String> image_filenames; // 图片集合
	ArrayList<Cigarette> cigarettes = new ArrayList<Cigarette>();
	private ImageDownLoadAsyncTask asyncTask;

	private int current_page = 0;// 页码
	private int count = 20;// 每页显示的个数
	private int column = 4;// 显示列数

	private int item_width;// 每一个item的宽度

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		parentView = inflater.inflate(R.layout.home, container, false);
		setUpViews();
		return parentView;
	}

	@SuppressWarnings("deprecation")
	private void setUpViews() {
		lazyScrollView = (ScrollView) parentView
				.findViewById(R.id.waterfall_scroll);
		// lazyScrollView.getView();
		// lazyScrollView.setOnScrollListener(this);
		lazyScrollView.setOnTouchListener(new OnTouchListener() {
			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					break;
				case MotionEvent.ACTION_MOVE:
					if (v.getScrollY() <= 0) {
						LogUtil.d("scroll view", "top");
					} else if (lazyScrollView.getChildAt(0).getMeasuredHeight() <= v
							.getHeight() + v.getScrollY()) {
						LogUtil.d("scroll view", "bottom");
						LogUtil.d("scroll view", "view.getMeasuredHeight() = "
								+ lazyScrollView.getMeasuredHeight()
								+ ", v.getHeight() = "
								+ v.getHeight()
								+ ", v.getScrollY() = "
								+ v.getScrollY()
								+ ", view.getChildAt(0).getMeasuredHeight() = "
								+ lazyScrollView.getChildAt(0)
										.getMeasuredHeight());
						addImage(++current_page, count);
					}
					break;
				default:
					break;
				}
				return false;
			}
		});
		waterfall_container = (LinearLayout) parentView
				.findViewById(R.id.waterfall_container);
		progressbar = (LinearLayout) parentView.findViewById(R.id.progressbar);
		loadtext = (TextView) parentView.findViewById(R.id.loadtext);

		item_width = getActivity().getWindowManager().getDefaultDisplay()
				.getWidth()
				/ column;
		linearLayouts = new ArrayList<LinearLayout>();

		// 添加三列到waterfall_container
		for (int i = 0; i < column; i++) {
			LinearLayout layout = new LinearLayout(getActivity());
			LinearLayout.LayoutParams itemParam = new LinearLayout.LayoutParams(
					item_width, LayoutParams.WRAP_CONTENT);
			layout.setOrientation(LinearLayout.VERTICAL);
			layout.setLayoutParams(itemParam);
			linearLayouts.add(layout);
			waterfall_container.addView(layout);
		}
		assetManager = getActivity().getAssets();

		// MainActivity parentActivity = (MainActivity) getActivity();
		// resideMenu = parentActivity.getResideMenu();
		reloadData();
		// 获取图片集合
		// try {
		// image_filenames = Arrays.asList(assetManager.list(file));
		// } catch (IOException e) {
		// e.printStackTrace();
		// }

		// add gesture operation's ignored views
		// FrameLayout ignored_view = (FrameLayout) parentView
		// .findViewById(R.id.ignored_view);
		// resideMenu.addIgnoredView(ignored_view);
	}

	/***
	 * 加载更多
	 * 
	 * @param current_page
	 * @param count
	 */
	private void addImage(int current_page, int count) {
		int j = 0;
		int imagecount = cigarettes.size();
		for (int i = current_page * count; i < count * (current_page + 1)
				&& i < imagecount; i++) {
			addBitMapToImage(cigarettes.get(i), j, i);
			j++;
			if (j >= column)
				j = 0;
		}

	}

	/***
	 * 添加图片到相应image
	 * 
	 * @param string
	 *            图片名称
	 * @param j
	 *            列
	 * @param i
	 *            图片下标
	 */
	@SuppressLint("InflateParams")
	private void addBitMapToImage(Cigarette mCigarette, int j, int i) {
		LinearLayout tagView = (LinearLayout) LayoutInflater
				.from(getActivity()).inflate(R.layout.item_home, null);
		TextView tv_name = (TextView) tagView.findViewById(R.id.tv_name);
		TextView tv_price = (TextView) tagView.findViewById(R.id.tv_price);
		tv_name.setText(mCigarette.getName());
		tv_price.setText(mCigarette.getShoujia() + "元/条");
		ImageView imageView = getImageview(mCigarette.getImg());
		asyncTask = new ImageDownLoadAsyncTask(getActivity(),
				mCigarette.getImg(), imageView, item_width);

		asyncTask.setProgressbar(progressbar);
		asyncTask.setLoadtext(loadtext);
		asyncTask.execute();

		imageView.setTag(i);
		// 添加相应view
		tagView.addView(imageView);
		// linearLayouts.get(j).addView(imageView);
		linearLayouts.get(j).addView(tagView);

		imageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(getActivity(), "您点击了" + v.getTag() + "个Item",
						Toast.LENGTH_SHORT).show();

			}
		});
	}

	/***
	 * 获取imageview
	 * 
	 * @param imageName
	 * @return
	 */
	public ImageView getImageview(String imageName) {
		BitmapFactory.Options options = getBitmapBounds(imageName);
		// 创建显示图片的对象
		ImageView imageView = new ImageView(getActivity());
		@SuppressWarnings("deprecation")
		LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.FILL_PARENT);
		imageView.setLayoutParams(layoutParams);
		//
		imageView.setMinimumHeight(options.outHeight);
		imageView.setMinimumWidth(options.outWidth);
		imageView.setPadding(2, 0, 2, 2);
		imageView.setBackgroundResource(R.drawable.image_border);
		if (options != null)
			options = null;
		return imageView;
	}

	/***
	 * 
	 * 获取相应图片的 BitmapFactory.Options
	 */
	public BitmapFactory.Options getBitmapBounds(String imageName) {
		@SuppressWarnings("unused")
		int h, w;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;// 只返回bitmap的大小，可以减少内存使用，防止OOM.
		InputStream is = null;
		try {
			is = assetManager.open(imageName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		BitmapFactory.decodeStream(is, null, options);
		return options;

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
		return new CigaretteLoader(getActivity(), null);
	}

	@Override
	public void onLoadFinished(Loader<ArrayList<Cigarette>> arg0,
			ArrayList<Cigarette> arg1) {
		// TODO Auto-generated method stub
		cigarettes = arg1;
		// 第一次加载
		addImage(current_page, count);
	}

	@Override
	public void onLoaderReset(Loader<ArrayList<Cigarette>> arg0) {
		// TODO Auto-generated method stub

	}

}
