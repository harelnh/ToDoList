package harel.com.ToDoList.tests;

import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

import harel.com.ToDoList.ToDoManagerActivity;

public class Test3_CancelTest extends
		ActivityInstrumentationTestCase2<ToDoManagerActivity> {
	private Solo solo;

	public Test3_CancelTest() {
		super(ToDoManagerActivity.class);
	}

	public void setUp() throws Exception {
		solo = new Solo(getInstrumentation(),getActivity());
	}

	@Override
	public void tearDown() throws Exception {
		solo.finishOpenedActivities();
	}

	// Executes the CancelTest
	public void testRun() {

		int delay = 2000;

		// Wait for activity: 'course.labs.todomanager.ToDoManagerActivity'
		assertTrue("CancelTest failed:" +
				"Section One:" +
				"ToDoManagerActivity did not load correctly.",
				solo.waitForActivity(
						harel.com.ToDoList.ToDoManagerActivity.class, 2000));

		// Click on action bar item
		solo.clickOnActionBarItem(0x1);

		solo.sleep(delay);
		
		// Click on Add New ToDo Item
		solo.clickOnView(solo.getView(harel.com.ToDoList.R.id.footerView));

		// Wait for activity: 'course.labs.todomanager.AddToDoActivity'
		assertTrue("CancelTest failed:" +
				"Section One:" +
				"AddToDoActivity did not load correctly.",
				solo.waitForActivity(harel.com.ToDoList.AddToDoActivity.class));

		// Hide the soft keyboard
		solo.hideSoftKeyboard();
		
		// Enter the text: 't3'
		solo.clearEditText((android.widget.EditText) solo
				.getView(harel.com.ToDoList.R.id.title));
		
		solo.enterText((android.widget.EditText) solo
				.getView(harel.com.ToDoList.R.id.title), "t3");
		
		// Hide the soft keyboard
		solo.hideSoftKeyboard();
		
		// Click on Done:
		solo.clickOnView(solo.getView(harel.com.ToDoList.R.id.statusDone));
		
		// Click on High
		solo.clickOnView(solo
				.getView(harel.com.ToDoList.R.id.highPriority));

		// Click on Cancel
		solo.clickOnView(solo
				.getView(harel.com.ToDoList.R.id.cancelButton));
	
		// Wait for activity: 'course.labs.todomanager.AddToDoActivity'
		assertTrue("Cancel test failed:" + "Section One:"
				+ "AddToDoActivity did not correctly load.",
				solo.waitForActivity(
						harel.com.ToDoList.AddToDoActivity.class, delay));
		
		// Click on Add New ToDo Item
		solo.clickOnView(solo.getView(harel.com.ToDoList.R.id.footerView));

		// Wait for activity: 'course.labs.todomanager.AddToDoActivity'
		assertTrue("CancelTest failed:" +
				"Section One:" +
				"AddToDoActivity did not load correctly.",
				solo.waitForActivity(harel.com.ToDoList.AddToDoActivity.class));

		// Hide the soft keyboard
		solo.hideSoftKeyboard();
		
		// Enter the text: 't4'
		solo.clearEditText((android.widget.EditText) solo
				.getView(harel.com.ToDoList.R.id.title));
		
		solo.enterText((android.widget.EditText) solo
				.getView(harel.com.ToDoList.R.id.title), "t4");

		// Hide the soft keyboard
		solo.hideSoftKeyboard();
		
		// Click on Done:
		solo.clickOnView(solo.getView(harel.com.ToDoList.R.id.statusDone));
		
		// Click on Low
		solo.clickOnView(solo.getView(harel.com.ToDoList.R.id.lowPriority));

		// Click on Submit
		solo.clickOnView(solo
				.getView(harel.com.ToDoList.R.id.submitButton));
		
		// ================ Section Two ===================
		
		// Wait for activity: 'course.labs.todomanager.ToDoManagerActivity'
		assertTrue("CancelTest failed:" +
				"Section Two:" +
				"ToDoManagerActivity did not load correctly.",
				solo.waitForActivity(harel.com.ToDoList.ToDoManagerActivity.class));

		assertFalse("CancelTest failed:" +
				"Section Two:" +
				"Did not correctly cancel the creation of a ToDo Task.",
				solo.searchText("t3"));
		
		assertTrue("CancelTest failed:" +
				"Section Two:" +
				"Did not correctly set title of ToDo Task following cancel.",
				solo.searchText("t4"));
		
		assertTrue("CancelTest failed:" +
				"Section Two:" +
				"Did not correctly set priority of ToDo Task following cancel.",
				solo.searchText("[lL][oO][wW]"));
		
	}
}
