/*
 * This file is part of TrackWorkTime (TWT).
 *
 * TWT is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * TWT is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with TWT. If not, see <http://www.gnu.org/licenses/>.
 */
package org.zephyrsoft.trackworktime;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;

import org.pmw.tinylog.Logger;
import org.threeten.bp.ZonedDateTime;
import org.zephyrsoft.trackworktime.database.DAO;
import org.zephyrsoft.trackworktime.databinding.ReportsBinding;
import org.zephyrsoft.trackworktime.model.Event;
import org.zephyrsoft.trackworktime.model.Range;
import org.zephyrsoft.trackworktime.model.Task;
import org.zephyrsoft.trackworktime.model.TimeSum;
import org.zephyrsoft.trackworktime.model.Unit;
import org.zephyrsoft.trackworktime.model.Week;
import org.zephyrsoft.trackworktime.report.CsvGenerator;
import org.zephyrsoft.trackworktime.timer.TimeCalculator;
import org.zephyrsoft.trackworktime.util.DateTimeUtil;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Reports dialog.
 *
 * @author Mathis Dirksen-Thedens
 */
public class ReportsActivity extends AppCompatActivity {

	private ReportsBinding binding;

	private DAO dao;
	private TimeCalculator timeCalculator;
	private CsvGenerator csvGenerator;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		dao = Basics.getInstance().getDao();
		timeCalculator = Basics.getInstance().getTimeCalculator();
		csvGenerator = new CsvGenerator(dao);

		binding = ReportsBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());

		binding.rangeAllData.setOnCheckedChangeListener((buttonView, isChecked) -> {
			binding.unitWeek.setEnabled(!isChecked);
			binding.unitMonth.setEnabled(!isChecked);
			binding.unitYear.setEnabled(!isChecked);
		});

		binding.reportExport.setOnClickListener(v -> export());
	}

	private void export() {
		switch (binding.grouping.getCheckedRadioButtonId()) {
			case R.id.groupingNone:
				exportAllEvents();
				break;
			case R.id.groupingByTask:
				exportTimesByTask();
				break;
			case R.id.groupingByTaskPerDay:
				exportTimesByTaskPerDay();
				break;
			case R.id.groupingByTaskPerWeek:
				exportTimesByTaskPerWeek();
				break;
			case R.id.groupingByTaskPerMonth:
				exportTimesByTaskPerMonth();
				break;
			default:
				throw new RuntimeException("Grouping not implemented");
		}
	}

	private void exportAllEvents() {
		Range selectedRange = getSelectedRange();
		Unit selectedUnit = getSelectedUnit();

		ZonedDateTime[] beginAndEnd = timeCalculator.calculateBeginAndEnd(selectedRange, selectedUnit);
		List<Event> events = dao.getEvents(beginAndEnd[0].toInstant(), beginAndEnd[1].toInstant());

		String report = csvGenerator.createEventCsv(events);
		String reportName = getNameForSelection(selectedRange, selectedUnit);
		if (report == null) {
			logAndShowError("could not generate report " + reportName);
			return;
		}

		boolean success = saveAndSendReport(reportName,
			"events-" + reportName.replaceAll(" ", "-"),
			report);

		if (success) {
			// close this dialog
			finish();
		}
	}

	private void exportTimesByTask() {
		Range selectedRange = getSelectedRange();
		Unit selectedUnit = getSelectedUnit();

		ZonedDateTime[] beginAndEnd = timeCalculator.calculateBeginAndEnd(selectedRange, selectedUnit);
		List<Event> events = dao.getEvents(beginAndEnd[0].toInstant(), beginAndEnd[1].toInstant());
		Map<Task, TimeSum> sums = timeCalculator.calculateSums(beginAndEnd[0].toOffsetDateTime(), beginAndEnd[1].toOffsetDateTime(), events);

		String report = csvGenerator.createSumsCsv(sums);
		String reportName = getNameForSelection(selectedRange, selectedUnit);
		if (report == null) {
			logAndShowError("could not generate report " + reportName);
			return;
		}

		boolean success = saveAndSendReport(reportName,
			"sums-" + reportName.replaceAll(" ", "-"),
			report);

		if (success) {
			// close this dialog
			finish();
		}
	}

	private void exportTimesByTaskPerDay() {
		Range selectedRange = getSelectedRange();
		Unit selectedUnit = getSelectedUnit();

		ZonedDateTime[] beginAndEnd = timeCalculator.calculateBeginAndEnd(selectedRange, selectedUnit);
		List<ZonedDateTime> rangeBeginnings = timeCalculator.calculateRangeBeginnings(Unit.DAY, beginAndEnd[0],
				beginAndEnd[1]);
		Map<ZonedDateTime, Map<Task, TimeSum>> sumsPerRange = calculateSumsPerRange(rangeBeginnings, beginAndEnd[1]);

		String report = csvGenerator.createSumsPerDayCsv(sumsPerRange);
		String reportName = getNameForSelection(selectedRange, selectedUnit);
		if (report == null) {
			logAndShowError("could not generate report " + reportName);
			return;
		}

		boolean success = saveAndSendReport(reportName,
				"sums-per-day-" + reportName.replaceAll(" ", "-"),
				report);

		if (success) {
			// close this dialog
			finish();
		}
	}

	private void exportTimesByTaskPerWeek() {
		Range selectedRange = getSelectedRange();
		Unit selectedUnit = getSelectedUnit();

		ZonedDateTime[] beginAndEnd = timeCalculator.calculateBeginAndEnd(selectedRange, selectedUnit);
		List<ZonedDateTime> rangeBeginnings = timeCalculator.calculateRangeBeginnings(Unit.WEEK, beginAndEnd[0],
			beginAndEnd[1]);
		Map<ZonedDateTime, Map<Task, TimeSum>> sumsPerRange = calculateSumsPerRange(rangeBeginnings, beginAndEnd[1]);

		String report = csvGenerator.createSumsPerWeekCsv(sumsPerRange);
		String reportName = getNameForSelection(selectedRange, selectedUnit);
		if (report == null) {
			logAndShowError("could not generate report " + reportName);
			return;
		}

		boolean success = saveAndSendReport(reportName,
			"sums-per-week-" + reportName.replaceAll(" ", "-"),
			report);

		if (success) {
			// close this dialog
			finish();
		}
	}

	private void exportTimesByTaskPerMonth() {
		Range selectedRange = getSelectedRange();
		Unit selectedUnit = getSelectedUnit();

		ZonedDateTime[] beginAndEnd = timeCalculator.calculateBeginAndEnd(selectedRange, selectedUnit);
		List<ZonedDateTime> rangeBeginnings = timeCalculator.calculateRangeBeginnings(Unit.MONTH, beginAndEnd[0],
			beginAndEnd[1]);
		Map<ZonedDateTime, Map<Task, TimeSum>> sumsPerRange = calculateSumsPerRange(rangeBeginnings, beginAndEnd[1]);

		String report = csvGenerator.createSumsPerMonthCsv(sumsPerRange);
		String reportName = getNameForSelection(selectedRange, selectedUnit);
		if (report == null) {
			logAndShowError("could not generate report " + reportName);
			return;
		}

		boolean success = saveAndSendReport(reportName,
			"sums-per-month-" + reportName.replaceAll(" ", "-"),
			report);

		if (success) {
			// close this dialog
			finish();
		}
	}

	private Map<ZonedDateTime, Map<Task, TimeSum>> calculateSumsPerRange(List<ZonedDateTime> rangeBeginnings, ZonedDateTime end) {
		Map<ZonedDateTime, Map<Task, TimeSum>> sumsPerRange = new HashMap<>();

		for (int i = 0; i < rangeBeginnings.size(); i++) {
			ZonedDateTime rangeStart = rangeBeginnings.get(i);
			ZonedDateTime rangeEnd = (i >= rangeBeginnings.size() - 1 ? end : rangeBeginnings.get(i + 1));
			List<Event> events = dao.getEvents(rangeStart.toInstant(), rangeEnd.toInstant());
			Map<Task, TimeSum> sums = timeCalculator.calculateSums(rangeStart.toOffsetDateTime(), rangeEnd.toOffsetDateTime(), events);
			sumsPerRange.put(rangeStart, sums);
		}
		return sumsPerRange;
	}

	private void logAndShowError(String errorMessage) {
		Logger.error(errorMessage);
		Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
	}

	private Range getSelectedRange() {
		if (binding.rangeLast.isChecked()) {
			return Range.LAST;
		} else if (binding.rangeCurrent.isChecked()) {
			return Range.CURRENT;
		} else if (binding.rangeLastAndCurrent.isChecked()) {
			return Range.LAST_AND_CURRENT;
		} else if (binding.rangeAllData.isChecked()) {
			return Range.ALL_DATA;
		} else {
			throw new IllegalStateException("unknown range");
		}
	}

	private Unit getSelectedUnit() {
		if (binding.unitWeek.isChecked()) {
			return Unit.WEEK;
		} else if (binding.unitMonth.isChecked()) {
			return Unit.MONTH;
		} else if (binding.unitYear.isChecked()) {
			return Unit.YEAR;
		} else {
			throw new IllegalStateException("unknown unit");
		}
	}

	private String getNameForSelection(Range range, Unit unit) {
		return range == Range.ALL_DATA
            ? range.getName()
            : range.getName() + " " + unit.getName();
	}

	private boolean saveAndSendReport(String reportName, String filePrefix, String report) {
		File reportFile = ExternalStorage.writeFile("reports", filePrefix + "-" +
			reportName.replaceAll(" ", "-"), ".csv", report.getBytes(), this);
		if (reportFile == null) {
			String errorMessage = "could not write report to external storage";
			Logger.error(errorMessage);
			Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
			return false;
		}

		// send the report
		Intent sendingIntent = new Intent(Intent.ACTION_SEND);
		sendingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Track Work Time Report");
		sendingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "report time frame: " + reportName);
		Uri fileUri = FileProvider.getUriForFile(this,
			BuildConfig.APPLICATION_ID + ".util.GenericFileProvider", reportFile);
		sendingIntent.putExtra(Intent.EXTRA_STREAM, fileUri);
		sendingIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
		sendingIntent.setType("text/plain");
		startActivity(Intent.createChooser(sendingIntent, "Send report..."));

		return true;
	}

	private static String getWeekName(Week week) {
		return "week-beginning-on-" + DateTimeUtil.dateToULString(week.getStart());
	}

}
