package com.tb.cigarette.activity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.tb.cigarette.manager.CigaretteManager;
import com.tb.cigarette.model.Cigarette;
import com.tb.cigarette.model.SearchParams;

import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.ColumnValue;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.Utils;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.PieChartView;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;

@SuppressLint("NewApi")
public class ChartsPieActivity extends FragmentActivity {
	private ColumnChartView chartBottom;
	private PieChartView chartRight;
	private PieChartView chartLeft;
	private PieChartData pieDataL;
	private PieChartData pieDataR;
	private ColumnChartData columnData;
	private ActionBar actionBar;
	private CigaretteManager mCigaretteManager = null;
	private List<Integer> chandiIntegers = new LinkedList<Integer>();
	private List<List<Cigarette>> cigarettes = new LinkedList<List<Cigarette>>();
	public String[] months;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_chartspie);
		initActionBar();
		mCigaretteManager = CigaretteManager.getInstance(this);
		chartBottom = (ColumnChartView) findViewById(R.id.chart_bottom);
		List<String> s = mCigaretteManager.loadPinpai();
		months = new String[s.size()];
		for (int i = 0; i < s.size(); i++) {
			List<Cigarette> mcigarettes = new LinkedList<Cigarette>();
			months[i] = s.get(i);
			SearchParams searchParams = new SearchParams();
			searchParams.setPinpai(s.get(i));
			mcigarettes = mCigaretteManager.loadSearchCigarette(searchParams);
			cigarettes.add(mcigarettes);
			chandiIntegers.add(mcigarettes.size());
		}
		generatePieata();
		generateColumnData();
	}

	private void generatePieata() {
		// TODO Auto-generated method stub

	}

	private void generateColumnData() {

		int numSubcolumns = 1;
		int numColumns = months.length;

		List<AxisValue> axisValues = new ArrayList<AxisValue>();
		List<Column> columns = new ArrayList<Column>();
		List<ColumnValue> values;
		for (int i = 0; i < numColumns; ++i) {

			values = new ArrayList<ColumnValue>();
			for (int j = 0; j < numSubcolumns; ++j) {
				// values.add(new ColumnValue((float) Math.random() * 50f + 5,
				// Utils.pickColor()));
				values.add(new ColumnValue((float) chandiIntegers.get(i), Utils
						.pickColor()));
				axisValues.add(new AxisValue(i, months[i].toCharArray()));
			}
			columns.add(new Column(values).setHasLabelsOnlyForSelected(true));
			columns.get(i).setHasLabels(true);
		}

		columnData = new ColumnChartData(columns);
		Axis axisX = new Axis(axisValues).setHasLines(true);
		Axis axisY = new Axis().setHasLines(true).setMaxLabelChars(3);
		axisX.setTextColor(getResources().getColor(R.color.black));
		axisY.setTextColor(getResources().getColor(R.color.black));
		columnData.setAxisXBottom(axisX);
		columnData.setAxisYLeft(axisY);

		chartBottom.setColumnChartData(columnData);

		// Set value touch listener that will trigger changes for chartTop.
		chartBottom.setOnValueTouchListener(new ValueTouchListener());

		// Set selection mode to keep selected month column highlighted.
		chartBottom.setValueSelectionEnabled(true);
		Viewport v = new Viewport(0, ChartsActivity.getMax(chandiIntegers),
				months.length, 0);
		// chartBottom.setMaximumViewport(v);
		// chartBottom.setCurrentViewport(v, false);
		chartBottom.setZoomType(ZoomType.HORIZONTAL);
		chartBottom.setZoomLevel(0, 0, months.length / 13, false);

		// chartBottom.setOnClickListener(new View.OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// SelectedValue sv = chartBottom.getSelectedValue();
		// if (!sv.isSet()) {
		// generateInitialLineData();
		// }
		//
		// }
		// });

	}

	private class ValueTouchListener implements
			ColumnChartView.ColumnChartOnValueTouchListener {

		@Override
		public void onValueTouched(int selectedLine, int selectedValue,
				ColumnValue value) {
			// generateLineData(value.getColor(), 100,
			// cigarettes.get(selectedLine));

		}

		@Override
		public void onNothingTouched() {

		}
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			overridePendingTransition(R.anim.animation_left_in,
					R.anim.animation_right_out);
			break;
		default:
			break;
		}
		return super.onMenuItemSelected(featureId, item);
	}

	private void initActionBar() {
		// TODO Auto-generated method stub
		actionBar = getActionBar();
		actionBar.setTitle("其他详情分析");
		actionBar.setIcon(R.drawable.ic_menu_back);
		actionBar.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.actionbar_background));
		actionBar.setHomeButtonEnabled(true);
	}
}
