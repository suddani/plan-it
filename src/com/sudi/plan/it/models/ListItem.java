package com.sudi.plan.it.models;

import android.view.View;

/**
 * Used to manage tasks in the listview. Provides access to the view that represents a task
 * @author dsudmann
 *
 */
public class ListItem {
	private View view;
	private Task task;
	
	public ListItem(View view, Task task) {
		this.view = view;
		this.task = task;
	}
	
	public Task getTask() {
		return task;
	}
	
	public void setSelected(boolean selected) {
		view.setSelected(selected);
	}
	
	public View getView() {
		return view;
	}
}
