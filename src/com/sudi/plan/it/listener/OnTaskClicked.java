package com.sudi.plan.it.listener;

import com.sudi.plan.it.models.Task;
import com.sudi.plan.it.models.TaskEditor;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class OnTaskClicked implements OnItemClickListener {
	
	private TaskEditor taskEditor;

	public OnTaskClicked(TaskEditor taskEditor) {
		this.taskEditor = taskEditor;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Task task = (Task)view.getTag();
		taskEditor.toggleTask(task);
	}

}
