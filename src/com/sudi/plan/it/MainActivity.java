package com.sudi.plan.it;

import com.sudi.plan.it.listener.MultiTaskActionMode;
import com.sudi.plan.it.listener.OnTaskClicked;
import com.sudi.plan.it.listener.NewTitleTextChangeListener;
import com.sudi.plan.it.models.Task;
import com.sudi.plan.it.models.TaskAdapter;
import com.sudi.plan.it.models.TaskEditor;
import com.sudi.plan.it.views.SimpleFABController;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
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
		
		newItemTitle = (TextView)this.findViewById(R.id.new_item_title);
		newItemTitle.addTextChangedListener(new NewTitleTextChangeListener(edit_fab_controller, new OnClickListener(){
			@Override
			public void onClick(View v) {
				createDetailItem(v);
			}}));
		newItemTitle.setOnFocusChangeListener(new OnFocusChangeListener(){

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					cancelItemSelected(true);
				}
			}});
		newItemTitle.setOnKeyListener(new OnKeyListener(){
			@Override
		    public boolean onKey(View v, int keyCode, KeyEvent event)
		    {
		        if (event.getAction() == KeyEvent.ACTION_DOWN)
		        {
		            switch (keyCode)
		            {
		                case KeyEvent.KEYCODE_DPAD_CENTER:
		                case KeyEvent.KEYCODE_ENTER:
		                    addNewTask(null);
		                    return true;
		                default:
		                    break;
		            }
		        }
		        return false;
		    }
		});
		
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
	public void editTask(Task task) {
		Log.d("PlanIt.Debug", "Edit Task["+task.getId()+"]: "+task.getTitle());
		cancelItemSelected(true);
		startEditActivity(task);
	}
	
	public void createDetailItem(View v) {
		Task task = new Task(newItemTitle.getText().toString());
		newItemTitle.setText("");
		Log.d("PlanIt.Debug", "Create Task["+task.getId()+"]: "+task.getTitle());
		editTask(task);
	}
	
	public void addNewTask(View v) {
		cancelItemSelected(true);
		
		// make sure there is something in the editbox
		if (newItemTitle.getText().toString().isEmpty())
			return;
		
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
		
        taskAdapter.add(new Task(newItemTitle.getText().toString()));
        
        // commented out because...
        newItemTitle.setText("");
	}
	
	public boolean cancelItemSelected(boolean callfinish) {
		return multiSelector.finish();
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
