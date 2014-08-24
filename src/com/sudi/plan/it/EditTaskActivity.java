package com.sudi.plan.it;

import com.sudi.plan.it.fragments.DatePickerFragment;
import com.sudi.plan.it.fragments.TimePickerFragment;
import com.sudi.plan.it.models.Task;
import com.sudi.plan.it.models.TaskDbHelper;
import com.sudi.plan.it.notifications.Notifier;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class EditTaskActivity extends Activity {

	private boolean doNotSave;
	private TaskDbHelper dbHelper;
	private Task task;
	private TextView item_title;
	private TextView item_content;
	private InputMethodManager inputMethodManager;
	private ImageButton remove_reminder;
	private Button activate_reminder;
	private Button reminder_date;
	private Button reminder_time;
	private BroadcastReceiver updated_receiver;
	private Notifier notifier;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_task);
		
		inputMethodManager = (InputMethodManager)  getSystemService(Activity.INPUT_METHOD_SERVICE);
		
		doNotSave = true;
		dbHelper = new TaskDbHelper(this);

		item_title = (TextView)this.findViewById(R.id.item_title);
		item_content = (TextView)this.findViewById(R.id.item_content);
		remove_reminder = (ImageButton)this.findViewById(R.id.remove_reminder);
		activate_reminder = (Button)this.findViewById(R.id.activate_reminder);
		reminder_date = (Button)this.findViewById(R.id.reminder_date);
		reminder_time = (Button)this.findViewById(R.id.reminder_time);
		
		notifier = new Notifier(this, dbHelper);
		
		
		Intent intent = getIntent();
		initTask(intent);
		
		updated_receiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				initTask(getIntent());
			}
			
		};
		setResult(Activity.RESULT_OK, new Intent());
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		initTask(intent);
	}
	
	private void initTask(Intent intent) {
		String task_title = intent.getStringExtra("task_title");
		long task_id = intent.getLongExtra("task_id", -1);
		Log.d("PlanIt.Debug", "Called new Intent and update Task: "+task_id+" - "+task_title);
		if (task_id != -1) {
			task = dbHelper.getTask(task_id);
		}
		if (task == null) {
			task = new Task(task_title);
			task.setDbHelper(dbHelper);
		}

		item_title.setText(task.getTitle());
		item_content.setText(task.getDescription());
		
		if (!task.hasDueDate())
			this.hideReminderButtons();
		else
			this.showReminderButtons();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		if (task.getId() != -1)
			getMenuInflater().inflate(R.menu.edit_task, menu);
		else
			getMenuInflater().inflate(R.menu.edit_task_new, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_cancel) {
			inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
			finish();
			return true;
		}
		else if (id == R.id.action_save) {
			inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
			doNotSave = false;
			finish();
			return true;
		}
		else if (id == R.id.action_delete) {
			inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
			task.delete();
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		doNotSave = true;
		registerReceiver(updated_receiver, new IntentFilter("task.updated"));
	}
	
	@Override
	public void onPause() {
		unregisterReceiver(updated_receiver);
		if (!doNotSave) {
			updateOrSaveTask();
//		} else {
//			setResult(Activity.RESULT_CANCELED, new Intent());
		}
		super.onPause();
		overridePendingTransition(R.anim.animation_enter_slide_right, R.anim.animation_leave_slide_right);
	}
	
	@Override
	public void onDestroy() {
		dbHelper.close();
		super.onDestroy();
	}
	
	@Override
	public void onBackPressed(){
		doNotSave = false;
		super.onBackPressed();
	}
	
	public void removeReminder(View v) {
		task.setDueDate(null);
		hideReminderButtons();
	}

	public void activateReminder(View v) {
		task.setDueDate(Task.tomorrowMorning());
		showReminderButtons();
	}
	
	public void hideReminderButtons() {
		remove_reminder.setVisibility(View.INVISIBLE);
		activate_reminder.setVisibility(View.VISIBLE);
		reminder_time.setVisibility(View.INVISIBLE);
		reminder_date.setVisibility(View.INVISIBLE);
	}
	
	public void showReminderButtons() {
		remove_reminder.setVisibility(View.VISIBLE);
		activate_reminder.setVisibility(View.INVISIBLE);
		reminder_time.setVisibility(View.VISIBLE);
		reminder_date.setVisibility(View.VISIBLE);
		reminder_date.setText(task.getDueDatePrettyDate());
		reminder_time.setText(task.getDueDatePrettyTime());
	}

	public void changeReminderTime(View v) {
		TimePickerFragment newFragment = new TimePickerFragment(task, this);
	    newFragment.show(getFragmentManager(), "timePicker");
	}
	
	public void changeReminderDate(View v) {
		DatePickerFragment newFragment = new DatePickerFragment(task, this);
	    newFragment.show(getFragmentManager(), "timePicker");
	}
	
	public void updateOrSaveTask() {
		Log.d("PlanIt.Debug", "Save changes");
		task.setTitle(item_title.getText().toString());
		task.setDescription(item_content.getText().toString());
		if (task.getTitle().length() == 0 && task.getDescription().length() == 0)
			task.delete();
		else
			task.update();
		notifier.setNextAlarm(dbHelper);
	}
}
