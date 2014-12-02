/**
 * @Title: AlertDialog.java
 * @Description: TODO
 * @author: Calvinyang
 * @date: Jun 4, 2014 11:26:47 AM
 * Copyright: Copyright (c) 2013
 * @version: 1.0
 */
package com.tb.cigarette.widget;

import com.tb.cigarette.activity.R;
import com.tb.cigarette.common.ImageLoader;
import com.tb.cigarette.model.Cigarette;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author: tangbin
 * @Description: 提醒框
 */
public class DetailDialog extends Dialog {
	private Context context;
	private Cigarette cigarette;
	private ImageLoader imageLoader;

	/**
	 * @param context
	 */
	private DetailDialog(Context context) {
		super(context, R.style.dialog_style);
		this.context = context;
	}

	/**
	 * @param context
	 * @param hint
	 */
	public DetailDialog(Context context, Cigarette cigarette) {
		this(context);
		this.cigarette = cigarette;
		this.imageLoader = new ImageLoader(R.drawable.ic_launcher, context,
				false);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_detail);
		Window window = getWindow();
		LayoutParams params = window.getAttributes();
		params.gravity = Gravity.CENTER;
		params.width = (int) (CommonUtils.getWidth(context) - 60 * CommonUtils
				.getDensity(context));
		// params.height = (int) (CommonUtils.getDensity(context) *
		// (hint.length() / 12 * 30 + 120));
		// params.height = 260;
		window.setAttributes(params);

		ImageView img = (ImageView) findViewById(R.id.img);
		TextView tv_leixing = (TextView) findViewById(R.id.tv_leixing);
		TextView tv_guige = (TextView) findViewById(R.id.tv_guige);
		TextView tv_chandi = (TextView) findViewById(R.id.tv_chandi);
		TextView tv_changjia = (TextView) findViewById(R.id.tv_changjia);
		TextView tv_pinpai = (TextView) findViewById(R.id.tv_pinpai);
		TextView tv_dangci = (TextView) findViewById(R.id.tv_dangci);
		TextView tv_shoujia = (TextView) findViewById(R.id.tv_shoujia);
		TextView tv_name = (TextView) findViewById(R.id.tv_name);

		tv_leixing.setText("类型：" + cigarette.getLeixing());
		tv_guige.setText("规格：" + cigarette.getGuige());
		tv_chandi.setText("产地：" + cigarette.getChandi());
		tv_changjia.setText("厂家：" + cigarette.getChangjia());
		tv_pinpai.setText("品牌：" + cigarette.getPinpai());
		tv_dangci.setText("档次：" + cigarette.getDangci());
		tv_shoujia.setText("售价：" + cigarette.getShoujia() + "元/条");
		tv_name.setText(cigarette.getName());
		img.setImageBitmap(imageLoader.getBitmap(cigarette.getImg()));
		// imageLoader.DisplayImage(cigarette.getImg(), img);

		setCancelable(false);
		findViewById(R.id.alert_ok).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View paramView) {
						dismiss();
					}
				});
	}
}
