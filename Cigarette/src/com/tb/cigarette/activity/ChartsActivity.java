package com.tb.cigarette.activity;

import java.util.ArrayList;
import java.util.List;

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
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

@SuppressLint("NewApi")
public class ChartsActivity extends FragmentActivity {
	public final static String[] months = new String[] { "Jan", "Feb", "Mar",
			"Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec", };

	public final static String[] days = new String[] { "Mon", "Tue", "Wen",
			"Thu", "Fri", "Sat", "Sun", };

	private LineChartView chartTop;
	private ColumnChartView chartBottom;

	private LineChartData lineData;
	private ColumnChartData columnData;
	private ActionBar actionBar = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_line_column_dependency);
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
		return super.onCreateOptionsMenu(menu);
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
				values.add(new ColumnValue((float) Math.random() * 50f + 5,
						Utils.pickColor()));
				axisValues.add(new AxisValue(i, months[i].toCharArray()));
			}

			columns.add(new Column(values).setHasLabelsOnlyForSelected(true));
		}

		columnData = new ColumnChartData(columns);

		columnData.setAxisXBottom(new Axis(axisValues).setHasLines(true));
		columnData.setAxisYLeft(new Axis().setHasLines(true)
				.setMaxLabelChars(2));

		chartBottom.setColumnChartData(columnData);

		// Set value touch listener that will trigger changes for chartTop.
		chartBottom.setOnValueTouchListener(new ValueTouchListener());

		// Set selection mode to keep selected month column highlighted.
		chartBottom.setValueSelectionEnabled(true);

		chartBottom.setZoomType(ZoomType.HORIZONTAL);

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
		int numValues = 7;

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
		lineData.setAxisXBottom(new Axis(axisValues).setHasLines(true));
		lineData.setAxisYLeft(new Axis().setHasLines(true).setMaxLabelChars(3));

		chartTop.setLineChartData(lineData);

		// For build-up animation you have to disable viewport recalculation.
		chartTop.setViewportCalculationEnabled(false);

		// And set initial max viewport and current viewport- remember to set
		// viewports after data.
		Viewport v = new Viewport(0, 110, 6, 0);
		chartTop.setMaximumViewport(v);
		chartTop.setCurrentViewport(v, false);

		chartTop.setZoomType(ZoomType.HORIZONTAL);
	}

	private void generateLineData(int color, float range) {
		// Cancel last animation if not finished.
		chartTop.cancelDataAnimation();

		// Modify data targets
		Line line = lineData.getLines().get(0);// For this example there is
												// always only one line.
		line.setColor(color);
		for (PointValue value : line.getValues()) {
			// Change target only for Y value.
			value.setTarget(value.getX(), (float) Math.random() * range);
		}

		// Start new data animation with 300ms duration;
		chartTop.startDataAnimation(300);
	}

	private class ValueTouchListener implements
			ColumnChartView.ColumnChartOnValueTouchListener {

		@Override
		public void onValueTouched(int selectedLine, int selectedValue,
				ColumnValue value) {
			generateLineData(value.getColor(), 100);

		}

		@Override
		public void onNothingTouched() {

			generateLineData(Utils.COLOR_GREEN, 0);

		}
	}
}
