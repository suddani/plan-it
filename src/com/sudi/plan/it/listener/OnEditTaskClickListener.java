package com.sudi.plan.it.listener;

import com.sudi.plan.it.models.ListItem;
import com.sudi.plan.it.models.TaskEditor;

import android.view.View;
import android.view.View.OnClickListener;

public class OnEditTaskClickListener implements OnClickListener {
	
	private TaskEditor taskEditor;
	private ListItem listItem;

	public OnEditTaskClickListener(TaskEditor taskEditor, ListItem listItem) {
		this.taskEditor = taskEditor;
		this.listItem = listItem;
	}

	@Override
	public void onClick(View view) {
		taskEditor.editTask(listItem.getTask());
	}
}
