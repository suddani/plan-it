package com.sudi.plan.it.listener;

import com.sudi.plan.it.models.Task;
import com.sudi.plan.it.models.TaskListEditor;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * Used to handle clicks on a todo item. Toggle their finished state
 * @author dsudmann
 *
 */
public class OnTaskClicked implements OnItemClickListener {
	
	private TaskListEditor taskEditor;

	public OnTaskClicked(TaskListEditor taskEditor) {
		this.taskEditor = taskEditor;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Task task = (Task)view.getTag();
		taskEditor.toggleTask(task);
	}

}
