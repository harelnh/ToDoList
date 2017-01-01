package harel.com.ToDoList;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Intent;
import android.util.Log;

public class ToDoItem {
	//lets see you in action, github!
	public static final String ITEM_SEP = System.getProperty("line.separator");
	private static final String TAG = "Lab-UserInterface";
	public final static String TITLE = "title";
	public final static String TEXT = "textItself";
	public final static String DATE = "date";
	public final static String FILENAME = "filename";
	public final static SimpleDateFormat FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss", Locale.US);

	private String mTitle = new String();
	private String mText = new String();
	private Date mDate = new Date();
	// is it neccessary?
	private int _id;

	ToDoItem (){};
	ToDoItem(String title, String textItself, Date date) {
		this.mTitle = title;
		this.mText = textItself;
//		this.mPriority = priority;
//		this.mStatus = status;
		this.mDate = date;
	}
	ToDoItem(Intent intent) {
		mTitle = intent.getStringExtra(ToDoItem.TITLE);
		mText = intent.getStringExtra(ToDoItem.TEXT);
		try {
			mDate = ToDoItem.FORMAT.parse(intent.getStringExtra(ToDoItem.DATE));

		}

		catch (ParseException e) {
			mDate = new Date();
		}
	}
	// getters
	public String get_title() {
		return mTitle;
	}
	public String get_textItself() {
		return mText;
	}
	public Date get_date() {
		return mDate;
	}
	public int get_id() {return _id;}

	// setters

	public void set_title(String title) {
		mTitle = title;
	}
	public void set_textItself(String textItself) {
		mText = textItself ;
	}
	public void set_date(Date date) {mDate = date;}
	public void set_id(int _id) {this._id = _id;}


	// Take a set of String data values and 
	// package them as an Intent

	public static void packageIntent(Intent intent, String title,
			String textItself, String date) {

		intent.putExtra(ToDoItem.TITLE, title);
		intent.putExtra(ToDoItem.TEXT, textItself);
		intent.putExtra(ToDoItem.DATE, date);
	}

	public String toString() {
		return mTitle + ITEM_SEP +mText+ITEM_SEP
				+ FORMAT.format(mDate);
	}

	public String toLog() {
		return "Title:" + mTitle + ITEM_SEP
				+ ITEM_SEP + "Date:"
				+ FORMAT.format(mDate) + "\n";
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
