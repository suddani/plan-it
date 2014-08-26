package com.sudi.plan.it;

import com.sudi.plan.it.animations.ListViewAnimator;
import com.sudi.plan.it.listener.MultiTaskActionMode;
import com.sudi.plan.it.listener.NewTaskTitleListener;
import com.sudi.plan.it.listener.OnTaskClicked;
import com.sudi.plan.it.models.MainTaskEditor;
import com.sudi.plan.it.models.Task;
import com.sudi.plan.it.models.TaskAdapter;
import com.sudi.plan.it.models.TaskDbHelper;
import com.sudi.plan.it.notifications.Notifier;
import com.sudi.plan.it.views.SimpleFABController;

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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {

//	private ActionBar actionBar;
	private ListView listView;
	private TaskAdapter taskAdapter;
	private TextView newItemTitle;
	private ImageButton fab;
	private ImageButton fab_expand;
	private InputMethodManager inputMethodManager;
	private MultiTaskActionMode multiSelector;
	private SimpleFABController delete_fab_controller;
	private SimpleFABController edit_fab_controller;
	private BroadcastReceiver updated_receiver;
	private Notifier notifier;
	private TaskDbHelper dbHelper;
	private MainTaskEditor mainTaskEditor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
//		actionBar = getActionBar();
//		actionBar.setDisplayHomeAsUpEnabled(true);
		
		// Get an instance of the inputManager
		inputMethodManager = (InputMethodManager)  getSystemService(Activity.INPUT_METHOD_SERVICE);
		
		// Get UI handles
		fab = (ImageButton)this.findViewById(R.id.fab);
		fab_expand = (ImageButton)this.findViewById(R.id.fab_expand);
		listView = (ListView)this.findViewById(R.id.listView1);
		newItemTitle = (TextView)this.findViewById(R.id.new_item_title);
		
		// Create the FAB controllers
		delete_fab_controller = new SimpleFABController(fab);
		delete_fab_controller.setup();
		edit_fab_controller = new SimpleFABController(fab_expand);
		edit_fab_controller.setup();
		
		// Create Utility classes
		dbHelper = new TaskDbHelper(this);
		notifier = new Notifier(this, dbHelper);
		multiSelector = new MultiTaskActionMode(listView, delete_fab_controller, notifier);
		mainTaskEditor = new MainTaskEditor(this, 
				listView, newItemTitle, 
				inputMethodManager, multiSelector, notifier);
		taskAdapter = new TaskAdapter(this, mainTaskEditor, dbHelper);
		
		// Set editor options
		mainTaskEditor.setTaskAdapter(taskAdapter);
		
		// set the listview options
		listView.setMultiChoiceModeListener(multiSelector);
		listView.setEmptyView(this.findViewById(R.id.empty_list));
		listView.setOnItemClickListener(new OnTaskClicked(mainTaskEditor));
		listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
		listView.setAdapter(taskAdapter);
		
		// Create a listener for text input
		NewTaskTitleListener newItemTitleListerner = new NewTaskTitleListener(edit_fab_controller, mainTaskEditor);
		newItemTitle.addTextChangedListener(newItemTitleListerner);
		newItemTitle.setOnFocusChangeListener(newItemTitleListerner);
		newItemTitle.setOnKeyListener(newItemTitleListerner);
		
		Log.d("PlanIt.Debug", Task.now().toString());
		
		//listen for notification updates
		updated_receiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				ListViewAnimator anim = new ListViewAnimator(listView, (View)null);
				anim.animateLayout();
				taskAdapter.reload();
			}
			
		};
	}
	
	@Override
	public void onResume() {
		super.onResume();
		registerReceiver(updated_receiver, new IntentFilter("task.updated"));
	}
	
	@Override
	public void onPause() {
		unregisterReceiver(updated_receiver);
		super.onPause();
	}
	
	@Override
	public void onDestroy() {
		dbHelper.close();
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
//		int id = item.getItemId();
//		if (id == R.id.action_settings) {
//			return true;
//		}
		return super.onOptionsItemSelected(item);
	}
	
	// Called by the UI directly
	public void addNewTask(View v) {
		mainTaskEditor.newTask();
	}
	
	// Called by the UI directly
	public void focusNewTask(View v) {
		newItemTitle.requestFocus();
		inputMethodManager.showSoftInput(newItemTitle, 0);
	}
	
	public void startEditActivity(Task task) {
		Intent intent = new Intent(this, EditTaskActivity.class);
		intent.putExtra("task_title", task.getTitle());
		intent.putExtra("task_id", task.getId());
		startActivityForResult(intent, 0);
		overridePendingTransition(R.anim.animation_enter_slide_left, R.anim.animation_leave_slide_left);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d("PlanIt.Debug", "Edit task closed with: "+requestCode+" - "+resultCode+" == "+RESULT_OK);
	    // Check which request we're responding to
	    if (requestCode == 0) {
	        // Make sure the request was successful
	        if (resultCode == RESULT_OK) {
	        	taskAdapter.reload();
	        }
	    }
	}
}
