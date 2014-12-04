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
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.Utils;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.LineChartView;
import android.R.integer;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

@SuppressLint("NewApi")
public class ChartsActivity extends FragmentActivity {
	public String[] months;

	public final static String[] days = new String[] { "顶级烟", "高档烟", "中档烟",
			"普通烟" };

	private LineChartView chartTop;
	private ColumnChartView chartBottom;

	private LineChartData lineData;
	private ColumnChartData columnData;
	private ActionBar actionBar = null;
	private CigaretteManager mCigaretteManager = null;
	private List<Integer> chandiIntegers = new LinkedList<Integer>();
	private List<List<Cigarette>> cigarettes = new LinkedList<List<Cigarette>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_line_column_dependency);
		mCigaretteManager = CigaretteManager.getInstance(this);
		List<String> s = mCigaretteManager.loadChandi();
		months = new String[s.size()];
		for (int i = 0; i < s.size(); i++) {
			List<Cigarette> mcigarettes = new LinkedList<Cigarette>();
			months[i] = s.get(i);
			SearchParams searchParams = new SearchParams();
			searchParams.setChandi(s.get(i));
			mcigarettes = mCigaretteManager.loadSearchCigarette(searchParams);
			cigarettes.add(mcigarettes);
			chandiIntegers.add(mcigarettes.size());
		}
		initActionBar();
		// *** TOP LINE CHART ***
		chartTop = (LineChartView) findViewById(R.id.chart_top);
		// Generate and set data for line chart
		generateInitialLineData();
		// *** BOTTOM COLUMN CHART ***
		chartBottom = (ColumnChartView) findViewById(R.id.chart_bottom);
		generateColumnData();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.menu_charts, menu);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			overridePendingTransition(R.anim.animation_left_in,
					R.anim.animation_right_out);
			break;
		case R.id.action_pie:
			Intent intent = new Intent();
			intent.setClass(ChartsActivity.this, ChartsPieActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
		return super.onMenuItemSelected(featureId, item);
	}

	private void initActionBar() {
		// TODO Auto-generated method stub
		actionBar = getActionBar();
		actionBar.setTitle("统计概况");
		actionBar.setIcon(R.drawable.ic_menu_back);
		actionBar.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.actionbar_background));
		actionBar.setHomeButtonEnabled(true);
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
		Viewport v = new Viewport(0, getMax(chandiIntegers), months.length, 0);
		// chartBottom.setMaximumViewport(v);
		// chartBottom.setCurrentViewport(v, false);
		chartBottom.setZoomType(ZoomType.HORIZONTAL);
		chartBottom.setZoomLevel(0, 0, months.length / 8, false);

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

	/**
	 * Generates initial data for line chart. At the begining all Y values are
	 * equals 0. That will change when user will select value on column chart.
	 */
	private void generateInitialLineData() {
		int numValues = days.length;

		List<AxisValue> axisValues = new ArrayList<AxisValue>();
		List<PointValue> values = new ArrayList<PointValue>();
		for (int i = 0; i < numValues; ++i) {
			values.add(new PointValue(i, 0));
			axisValues.add(new AxisValue(i, days[i].toCharArray()));
		}

		Line line = new Line(values);
		line.setColor(Utils.COLOR_GREEN).setCubic(true);
		line.setHasLabels(true);
		line.setFilled(true);

		List<Line> lines = new ArrayList<Line>();
		lines.add(line);

		lineData = new LineChartData(lines);
		Axis axisX = new Axis(axisValues).setHasLines(true);
		Axis axisY = new Axis().setHasLines(true).setMaxLabelChars(3);
		axisX.setTextColor(getResources().getColor(R.color.black));
		axisY.setTextColor(getResources().getColor(R.color.black));
		lineData.setAxisXBottom(axisX);
		lineData.setAxisYLeft(axisY);

		chartTop.setLineChartData(lineData);

		// For build-up animation you have to disable viewport recalculation.
		chartTop.setViewportCalculationEnabled(false);

		// And set initial max viewport and current viewport- remember to set
		// viewports after data.
		Viewport v = new Viewport(0, 80, days.length, 0);
		chartTop.setMaximumViewport(v);
		chartTop.setCurrentViewport(v, false);

		chartTop.setZoomType(ZoomType.HORIZONTAL);
	}

	private void generateLineData(int color, float range,
			List<Cigarette> cigarettes) {
		// Cancel last animation if not finished.

		List<Cigarette> dj = new LinkedList<Cigarette>();
		List<Cigarette> gd = new LinkedList<Cigarette>();
		List<Cigarette> zd = new LinkedList<Cigarette>();
		List<Cigarette> pt = new LinkedList<Cigarette>();
		for (Cigarette cigarette : cigarettes) {
			if (cigarette.getDangci().contains("顶级")) {
				dj.add(cigarette);
			} else if (cigarette.getDangci().contains("高档")) {
				gd.add(cigarette);
			} else if (cigarette.getDangci().contains("中档")) {
				zd.add(cigarette);
			} else if (cigarette.getDangci().contains("普通")) {
				pt.add(cigarette);
			}
		}

		chartTop.cancelDataAnimation();

		// Modify data targets
		Line line = lineData.getLines().get(0);// For this example there is
												// always only one line.
		line.setColor(color);
		// for (PointValue value : line.getValues()) {
		// Change target only for Y value.
		line.getValues().get(0)
				.setTarget(line.getValues().get(0).getX(), dj.size());
		line.getValues().get(1)
				.setTarget(line.getValues().get(1).getX(), gd.size());
		line.getValues().get(2)
				.setTarget(line.getValues().get(2).getX(), zd.size());
		line.getValues().get(3)
				.setTarget(line.getValues().get(3).getX(), pt.size());
		// }

		// Start new data animation with 300ms duration;
		chartTop.startDataAnimation(300);
	}

	private class ValueTouchListener implements
			ColumnChartView.ColumnChartOnValueTouchListener {

		@Override
		public void onValueTouched(int selectedLine, int selectedValue,
				ColumnValue value) {
			generateLineData(value.getColor(), 100,
					cigarettes.get(selectedLine));

		}

		@Override
		public void onNothingTouched() {

		}
	}

	/**
	 * 得到最大值
	 * 
	 * @param list
	 * @return
	 */
	public static Integer getMax(List<Integer> list) {
		int max = 0;
		if (list == null || list.size() == 0) {
			return max;
		} else {
			for (int m : list) {
				if (m > max) {
					max = m;
				}
			}
			return max;
		}
	}
}
