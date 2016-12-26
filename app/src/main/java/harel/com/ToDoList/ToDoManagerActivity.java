package harel.com.ToDoList;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.Date;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.TextView;
//import harel.com.ToDoList.ToDoItem.Priority;
//import harel.com.ToDoList.ToDoItem.Status;


public class ToDoManagerActivity extends ListActivity {

	// Add a ToDoItem Request Code
	private static final int ADD_TODO_ITEM_REQUEST = 0;
	private static final int EDIT_TODO_ITEM_REQUEST = 1;
	private static final String FILE_NAME = "TodoManagerActivityData.txt";//?
	private static final String TAG = "Lab-UserInterface"; // for debugging

	// IDs for menu items
	private static final int MENU_DELETE = Menu.FIRST;
	private static final int MENU_DUMP = Menu.FIRST + 1;

	private static final String EXTRA_POSITION = "position";

	MyDBHandler dbHandler;
	ToDoListAdapter mAdapter;

	@Override // adding info on intent for recognizing on what purpose AddToDoActivity was called
	public void startActivityForResult(Intent intent, int requestCode) {
		intent.putExtra("requestCode", requestCode);
		super.startActivityForResult(intent, requestCode);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Create a new TodoListAdapter for this ListActivity's ListView
		mAdapter = new ToDoListAdapter(getApplicationContext(),dbHandler);
		getListView().setAdapter(mAdapter);
		// create sqlite handler
		dbHandler = new MyDBHandler(this, null, null, 1);

		// Put divider between ToDoItems and FooterView
		getListView().setFooterDividersEnabled(true);
		//Inflate footerView for footer_view.xml file
		LayoutInflater inflator = LayoutInflater.from(ToDoManagerActivity.this);
		TextView footerView = (TextView) inflator.inflate(R.layout.footer_view, null);
		//Add footerView to ListView
		getListView().addFooterView(footerView);

		footerView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				//Attach Listener to FooterView. Implement onClick().
				Intent intent = new Intent(getBaseContext(), AddToDoActivity.class); //Returns the context the view is currently running in
				startActivityForResult(intent, ADD_TODO_ITEM_REQUEST);
				//log("Entered footerView.OnClickListener.onClick()");
			}
		});

		//Attach the adapter to this ListActivity's ListView


//implement the option to edit notes by clicking them
		getListView().setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)  {
				ToDoItem item = mAdapter.getItem(position);
				Intent intent = new Intent(ToDoManagerActivity.this, AddToDoActivity.class);
				ToDoItem.packageIntent(intent,item.get_title(),item.get_textItself(),ToDoItem.FORMAT.format(item.get_date()));
				intent.putExtra(EXTRA_POSITION,position);
				startActivityForResult(intent,EDIT_TODO_ITEM_REQUEST);
			}

		});
	}

	@Override // can it be implemented out of onCreate?
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		// If user submitted a new ToDoIte Create a new
		// ToDoItem from the data Intent then add it to the adapter
		if (requestCode == ADD_TODO_ITEM_REQUEST) {
			if (resultCode == RESULT_OK) {
				ToDoItem toDo = new ToDoItem(data);
				mAdapter.add(toDo);
			}
		}
		else if
			(requestCode == EDIT_TODO_ITEM_REQUEST){
			if(resultCode == RESULT_OK) {
				dbHandler.update_note(data.getIntExtra(EXTRA_POSITION,0),new ToDoItem(data));
				mAdapter.update(data.getIntExtra(EXTRA_POSITION,0),new ToDoItem(data));
			}
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		// Load saved ToDoItems, if necessary
		if (mAdapter.getCount() == 0)
			dbHandler.loadItems(mAdapter.mItems);
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override// no idea what it's doing
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		menu.add(Menu.NONE, MENU_DELETE, Menu.NONE, "Delete all");
		menu.add(Menu.NONE, MENU_DUMP, Menu.NONE, "Dump to log");
		return true;
	}

	@Override //no idea what it's doing
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case MENU_DELETE:
				mAdapter.clear();
				return true;
			case MENU_DUMP:
				dump();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	private void dump() { // no idea what it's doing
		for (int i = 0; i < mAdapter.getCount(); i++) {
			String data = ((ToDoItem) mAdapter.getItem(i)).toLog();
			log("Item " + i + ": " + data.replace(ToDoItem.ITEM_SEP, ","));
		}
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

	// Load stored ToDoItems
//	private void loadItems() {
//		BufferedReader reader = null;
//		try {
//			FileInputStream fis = openFileInput(FILE_NAME);
//			reader = new BufferedReader(new InputStreamReader(fis));
//
//			String title = null;
//			String textItself = null;
////			String priority = null;
////			String status = null;
//			Date date = null;
//
//			while (null != (title = reader.readLine())) {
////				priority = reader.readLine();
////				status = reader.readLine();
//				textItself = reader.readLine();
//				date = ToDoItem.FORMAT.parse(reader.readLine());
//				mAdapter.add(new ToDoItem(title,textItself, date));
//			}
//
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (ParseException e) {
//			e.printStackTrace();
//		} finally {
//			if (null != reader) {
//				try {
//					reader.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//	}
//
//	// Save ToDoItems to file
//	private void saveItems() {
//		PrintWriter writer = null;
//		try {
//			FileOutputStream fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
//			writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
//					fos)));
//
//			for (int idx = 0; idx < mAdapter.getCount(); idx++) {
//				writer.println(mAdapter.getItem(idx));
//
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		} finally {
//			if (null != writer) {
//				writer.close();
//			}
//		}
//	}

