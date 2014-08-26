package com.sudi.plan.it.listener;

import com.sudi.plan.it.models.ListItem;
import com.sudi.plan.it.models.TaskListEditor;

import android.view.View;
import android.view.View.OnClickListener;

/**
 * Used to handle clicks on the edit button of todo items
 * @author dsudmann
 *
 */
public class OnEditTaskClickListener implements OnClickListener {
	
	private TaskListEditor taskEditor;
	private ListItem listItem;

	public OnEditTaskClickListener(TaskListEditor taskEditor, ListItem listItem) {
		this.taskEditor = taskEditor;
		this.listItem = listItem;
	}

	@Override
	public void onClick(View view) {
		taskEditor.editTask(listItem.getTask());
	}
}
