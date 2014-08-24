package com.sudi.plan.it;

import com.sudi.plan.it.listener.MultiTaskActionMode;
import com.sudi.plan.it.listener.NewTaskTitleListener;
import com.sudi.plan.it.listener.OnTaskClicked;
import com.sudi.plan.it.models.Task;
import com.sudi.plan.it.models.TaskAdapter;
import com.sudi.plan.it.models.TaskEditor;
import com.sudi.plan.it.views.SimpleFABController;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity implements TaskEditor {

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
//		actionBar = getActionBar();
//		actionBar.setDisplayHomeAsUpEnabled(true);
		
		inputMethodManager = (InputMethodManager)  getSystemService(Activity.INPUT_METHOD_SERVICE);
		

		taskAdapter = new TaskAdapter(this, this);
		
		fab = (ImageButton)this.findViewById(R.id.fab);
		fab.setTranslationY(48+fab.getHeight());
		fab.setVisibility(View.INVISIBLE);
		
		fab_expand = (ImageButton)this.findViewById(R.id.fab_expand);
		fab_expand.setTranslationY(48+fab.getHeight());
		fab_expand.setVisibility(View.INVISIBLE);

		delete_fab_controller = new SimpleFABController(fab);
		edit_fab_controller = new SimpleFABController(fab_expand);
		
		listView = (ListView)this.findViewById(R.id.listView1);
		listView.setEmptyView(this.findViewById(R.id.empty_list));
		listView.setOnItemClickListener(new OnTaskClicked(taskAdapter));
		listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
		
		multiSelector = new MultiTaskActionMode(listView, delete_fab_controller);
		listView.setMultiChoiceModeListener(multiSelector);
		
		listView.setAdapter(taskAdapter);
		
		NewTaskTitleListener newItemTitleListerner = new NewTaskTitleListener(edit_fab_controller, this);
		
		newItemTitle = (TextView)this.findViewById(R.id.new_item_title);
		newItemTitle.addTextChangedListener(newItemTitleListerner);
		newItemTitle.setOnFocusChangeListener(newItemTitleListerner);
		newItemTitle.setOnKeyListener(newItemTitleListerner);
		
		Log.d("PlanIt.Debug", Task.now().toString());
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
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void newTask() {
		cancelTaskSelected(true);
		
		// make sure there is something in the editbox
		if (newItemTitle.getText().toString().isEmpty())
			return;
		
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
		
        taskAdapter.add(new Task(newItemTitle.getText().toString()));
        
        // commented out because...
        newItemTitle.setText("");
	}

	@Override
	public void newTaskDetail() {
		Task task = new Task(newItemTitle.getText().toString());
		newItemTitle.setText("");
		Log.d("PlanIt.Debug", "Create Task["+task.getId()+"]: "+task.getTitle());
		editTask(task);
	}
	
	@Override
	public void editTask(Task task) {
		Log.d("PlanIt.Debug", "Edit Task["+task.getId()+"]: "+task.getTitle());
		cancelTaskSelected(true);
		startEditActivity(task);
	}
	
	@Override
	public boolean cancelTaskSelected(boolean callfinish) {
		return multiSelector.finish();
	}
	
	public void createDetailItem(View v) {
		newTaskDetail();
	}
	
	public void addNewTask(View v) {
		newTask();
	}
	
	private void startEditActivity(Task task) {
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
