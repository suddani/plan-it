package com.sudi.plan.it.models;

import com.sudi.plan.it.MainActivity;
import com.sudi.plan.it.animations.ListViewAnimator;
import com.sudi.plan.it.listener.MultiTaskActionMode;
import com.sudi.plan.it.notifications.Notifier;

import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Class is used to manage base editing modes on the TodoList.
 * @author dsudmann
 *
 */
public class MainTaskListEditor implements TaskListEditor {
	
	private ListView listView;
	private TaskAdapter taskAdapter;
	private TextView newItemTitle;
	private InputMethodManager inputMethodManager;
	private MultiTaskActionMode multiSelector;
	private Notifier notifier;
	private MainActivity mainActivity;

	public MainTaskListEditor(MainActivity mainActivity, ListView listView2,
			TextView newItemTitle2, InputMethodManager inputMethodManager2,
			MultiTaskActionMode multiSelector2, Notifier notifier2) {
		this.mainActivity = mainActivity;
		this.listView = listView2;
		this.newItemTitle = newItemTitle2;
		this.inputMethodManager = inputMethodManager2;
		this.multiSelector = multiSelector2;
		this.notifier = notifier2;
	}

	@Override
	public void newTask() {
		cancelTaskSelected(true);
		
		// make sure there is something in the editbox
		if (newItemTitle.getText().toString().isEmpty())
			return;
		
        inputMethodManager.hideSoftInputFromWindow(mainActivity.getCurrentFocus().getWindowToken(), 0);
		
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
		mainActivity.startEditActivity(task);
	}

	@Override
	public void toggleTask(Task task) {
		ListViewAnimator animator = new ListViewAnimator(this.listView, (View)null);
		animator.animateLayout();
		task.setDone(!task.isDone());
		task.update();
		notifier.setNextAlarm(null);
		taskAdapter.notifyDataSetChanged();
	}

	@Override
	public boolean cancelTaskSelected(boolean callfinish) {
		return multiSelector.finish();
	}
	
	/**
	 * Set the taskAdapter that should be used by this editor.
	 * @param taskAdapter2 The taskAdapter
	 */
	public void setTaskAdapter(TaskAdapter taskAdapter2) {
		this.taskAdapter = taskAdapter2;
	}

}
