package com.sudi.plan.it.views;

import com.sudi.plan.it.R;
import com.sudi.plan.it.listener.OnEditTaskClickListener;
import com.sudi.plan.it.models.ListItem;
import com.sudi.plan.it.models.Task;
import com.sudi.plan.it.models.TaskAdapter;
import com.sudi.plan.it.models.TaskEditor;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

public class TaskViewFactory {
	private LayoutInflater inflater;
	private TaskEditor taskEditor;

	final static int TODO_ITEM = 0;
	final static int TODO_ITEM_DATE = 1;
	final static int TODO_ITEM_COUNT = 2;

	public TaskViewFactory(TaskEditor taskEditor, TaskAdapter taskAdapter, LayoutInflater inflater) {
		this.inflater = inflater;
		this.taskEditor = taskEditor;
	}
	
	public int getViewTypeCount() {
		return TODO_ITEM_COUNT;
	}
	
	public int getViewType(Task task) {
		if (task.getDueDate() == null && task.getDescription().length() == 0)
			return TODO_ITEM;
		else
			return TODO_ITEM_DATE;
	}
	
	public int getLayoutID(int viewType) {
		switch(viewType) {
		case TODO_ITEM:
			return R.layout.todo_item;
		case TODO_ITEM_DATE:
			return R.layout.todo_item_date;
		default:
			return TODO_ITEM;
		}
	}

	private View inflateView(Task task, ViewGroup parent) {
		return inflater.inflate(getLayoutID(getViewType(task)), parent, false);
	}
	
	public View create(Task task, View convertView, ViewGroup parent) {
		if (convertView == null) {
            convertView = inflateView(task, parent);
		}
		convertView.setTag(task);
		
		TextView title = (TextView)convertView.findViewById(R.id.item_title);
		title.setText(task.getTitle());
		
		if ((title.getPaintFlags() & Paint.STRIKE_THRU_TEXT_FLAG) == Paint.STRIKE_THRU_TEXT_FLAG && !task.isDone()) {
			title.setPaintFlags(title.getPaintFlags() ^ Paint.STRIKE_THRU_TEXT_FLAG);
		}else if (task.isDone())
			title.setPaintFlags(title.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
		
		ImageButton removeItem = (ImageButton)convertView.findViewById(R.id.remove_list_item);
		removeItem.setTag(task);
		removeItem.setOnClickListener(new OnEditTaskClickListener(taskEditor, new ListItem(convertView, task)));
		
		boolean hasDate = task.getDueDate() != null;
		boolean hasText = task.getDescription().length() > 0;
		if (hasDate || hasText) {
			TextView date = (TextView)convertView.findViewById(R.id.item_date);
			String extra = "";
			if (hasDate)
				extra+=task.getPrettyDate()+(hasText?" - ":"");
			if (hasText)
				extra+=task.getDescription();
			date.setText(extra);
		}
		
			
		return convertView;
	}
}
