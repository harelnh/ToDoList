package harel.com.ToDoList;
//TODO - questions - what exactly do we have to implement inside onCreate?

import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
//import harel.com.ToDoList.ToDoItem.Priority;
//import harel.com.ToDoList.ToDoItem.Status;

public class AddToDoActivity extends Activity {

	// 7 days in milliseconds - 7 * 24 * 60 * 60 * 1000
	private static final int SEVEN_DAYS = 604800000;
	private static final int EDIT_TODO_ITEM_REQUEST = 1;

	private static final String TAG = "Lab-UserInterface";

	private static String timeString;
	private static String dateString;
	private static TextView dateView;
	private static TextView titleLabel;
	private static TextView time_and_date;
	private static TextView timeView;
	private static TextView textItselftitle;
	private static EditText textItself;
	private Date mDate;
	private EditText mTitleText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_todo);

		titleLabel = (TextView) findViewById(R.id.TitleLabel);
		mTitleText = (EditText) findViewById(R.id.title);
		textItself = (EditText) findViewById(R.id.textItself);
		dateView = (TextView) findViewById(R.id.date);
		time_and_date = (TextView) findViewById(R.id.time_and_date);
		timeView = (TextView) findViewById(R.id.time);

		//in case of editing note, updating title and text.
		if(getIntent().getIntExtra("requestCode",0)==EDIT_TODO_ITEM_REQUEST)
		{
			ToDoItem item = new ToDoItem(getIntent());
			mTitleText.setText(item.get_title());
			textItself.setText(item.get_textItself());
		}

		setDefaultDateTime();
		final Button datePickerButton = (Button) findViewById(R.id.date_picker_button);
		datePickerButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showDatePickerDialog();
			}
		});

		final Button timePickerButton = (Button) findViewById(R.id.time_picker_button);
		timePickerButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showTimePickerDialog();
			}
		});

		// OnClickListener for the Cancel Button,
		final Button cancelButton = (Button) findViewById(R.id.cancelButton);
		cancelButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = null;
				setResult(Activity.RESULT_CANCELED, intent);
				finish();
			}
		});

		//OnClickListener for the Reset Button
		final Button resetButton = (Button) findViewById(R.id.resetButton);
		resetButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//Reset data fields to default values
				mTitleText.setText("");
				textItself.setText("");
				setDefaultDateTime();
			}
		});

		// OnClickListener for the Submit Button
		final Button submitButton = (Button) findViewById(R.id.submitButton);
		submitButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				// gathering the information that accepted
				// TODO- why not private variables?
				String titleString = mTitleText.getText().toString();
				String textString = textItself.getText().toString();
				String fullDate = dateString + " " + timeString;
				Intent dataIntent = new Intent();
				ToDoItem.packageIntent(dataIntent, titleString, textString, fullDate);
				// in case of editing returning the original position of the item
				if(getIntent().getIntExtra("requestCode",0)==EDIT_TODO_ITEM_REQUEST)
				{
					dataIntent.putExtra("position",getIntent().getIntExtra("position",0));
				}
				// return data Intent and finish
				setResult(Activity.RESULT_OK, dataIntent);
				finish();
			}
		});
	}

//dont realy know what going on below here

// Use this method to set the default date and time
	private void setDefaultDateTime() {

		// Default is current time + 7 days
		mDate = new Date();
		mDate = new Date(mDate.getTime() + SEVEN_DAYS);

		Calendar c = Calendar.getInstance();
		c.setTime(mDate);

		setDateString(c.get(Calendar.YEAR), c.get(Calendar.MONTH),
				c.get(Calendar.DAY_OF_MONTH));

		dateView.setText(dateString);

		setTimeString(c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE),
				c.get(Calendar.MILLISECOND));

		timeView.setText(timeString);
	}

	private static void setDateString(int year, int monthOfYear, int dayOfMonth) {

		// Increment monthOfYear for Calendar/Date -> Time Format setting
		monthOfYear++;
		String mon = "" + monthOfYear;
		String day = "" + dayOfMonth;

		if (monthOfYear < 10)
			mon = "0" + monthOfYear;
		if (dayOfMonth < 10)
			day = "0" + dayOfMonth;

		dateString = year + "-" + mon + "-" + day;
	}

	private static void setTimeString(int hourOfDay, int minute, int mili) {
		String hour = "" + hourOfDay;
		String min = "" + minute;

		if (hourOfDay < 10)
			hour = "0" + hourOfDay;
		if (minute < 10)
			min = "0" + minute;

		timeString = hour + ":" + min + ":00";
	}

// DialogFragment used to pick a ToDoItem deadline date

	public static class DatePickerFragment extends DialogFragment implements
			DatePickerDialog.OnDateSetListener {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {

			// Use the current date as the default date in the picker

			final Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int day = c.get(Calendar.DAY_OF_MONTH);

			// Create a new instance of DatePickerDialog and return it
			return new DatePickerDialog(getActivity(), this, year, month, day);
		}

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
							  int dayOfMonth) {
			setDateString(year, monthOfYear, dayOfMonth);

			dateView.setText(dateString);
		}

	}

// DialogFragment used to pick a ToDoItem deadline time

	public static class TimePickerFragment extends DialogFragment implements
			TimePickerDialog.OnTimeSetListener {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {

			// Use the current time as the default values for the picker
			final Calendar c = Calendar.getInstance();
			int hour = c.get(Calendar.HOUR_OF_DAY);
			int minute = c.get(Calendar.MINUTE);

			// Create a new instance of TimePickerDialog and return
			return new TimePickerDialog(getActivity(), this, hour, minute,
					true);
		}

		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			setTimeString(hourOfDay, minute, 0);

			timeView.setText(timeString);
		}
	}

	private void showDatePickerDialog() {
		DialogFragment newFragment = new DatePickerFragment();
		newFragment.show(getFragmentManager(), "datePicker");
	}

	private void showTimePickerDialog() {
		DialogFragment newFragment = new TimePickerFragment();
		newFragment.show(getFragmentManager(), "timePicker");
	}

	private void log(String msg) {
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Log.i(TAG, msg);
	}
}