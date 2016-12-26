package harel.com.ToDoList;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import static harel.com.ToDoList.R.id.delete_icon;

public class ToDoListAdapter extends BaseAdapter {

	// List of ToDoItems
	 public List<ToDoItem> mItems = new ArrayList<ToDoItem>();

	private final Context mContext;
	private final MyDBHandler dbHandler;

	private static final String TAG = "Lab-UserInterface";

	public ToDoListAdapter(Context context, MyDBHandler handler) {

		mContext = context;
		dbHandler = handler;


	}

// Add a ToDoItem to the adapter
// Notify observers that the data set has changed

	public void add(ToDoItem item) {
		mItems.add(item);
		int pos = mItems.size();

		dbHandler.add_toDoItem(item, pos);
		notifyDataSetChanged();

	}
	public void update(int pos, ToDoItem item) {
		mItems.remove(pos);

		mItems.add(pos, item);
		notifyDataSetChanged();

	}
	public void remove (int pos){
		mItems.remove(pos);
		notifyDataSetChanged();

	}

// Clears the list adapter of all items.

	public void clear(){

		mItems.clear();
		notifyDataSetChanged();

	}

// Returns the number of ToDoItems

	@Override
	public int getCount() {

		return mItems.size();

	}

// Retrieve the number of ToDoItems

	@Override
	public ToDoItem getItem(int pos) {

		return mItems.get(pos);

	}

// Get the ID for the ToDoItem
// In this case it's just the position

	@Override
	public long getItemId(int pos) {

		return pos;

	}

//Create a View to display the ToDoItem
// at specified position in mItems

	@Override
	public View getView( int position, View convertView, ViewGroup parent) {

		final int pos = position;
		//TODO - Get the current ToDoItem
		final ToDoItem toDoItem = (ToDoItem) getItem (position);

		//TODO - Inflate the View for this ToDoItem
		// from todo_item.xml.

		if(convertView == null){

			LayoutInflater inflater =(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.todo_item, null);

		}


		final TextView titleView = (TextView)convertView.findViewById(R.id.titleView);
		titleView.setText(toDoItem.get_title());

		// set up the text itself
		final TextView textItself = (TextView)convertView.findViewById(R.id.textItself);
		textItself.setText(toDoItem.get_textItself());

		// set up delete button
		final ImageView delete_icon = (ImageView) convertView.findViewById(R.id.delete_icon);
		delete_icon.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mItems.remove(pos);

				log("enter adapter remove");
				dbHandler.remove_note(pos);
				mItems.clear();
				dbHandler.loadItems(mItems);
				notifyDataSetChanged();

			}
		});
		return convertView;
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