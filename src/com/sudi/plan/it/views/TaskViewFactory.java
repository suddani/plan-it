package com.sudi.plan.it.views;

import com.sudi.plan.it.R;
import com.sudi.plan.it.listener.OnEditTaskClickListener;
import com.sudi.plan.it.models.ListItem;
import com.sudi.plan.it.models.Task;
import com.sudi.plan.it.models.TaskAdapter;
import com.sudi.plan.it.models.TaskListEditor;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Used to create Views for the Tasks in the TaskAdapter
 * @author dsudmann
 *
 */
public class TaskViewFactory {
	private LayoutInflater inflater;
	private TaskListEditor taskEditor;

	final static int TODO_ITEM = 0;
	final static int TODO_ITEM_DATE = 1;
	final static int TODO_ITEM_COUNT = 2;

	public TaskViewFactory(TaskListEditor taskEditor, TaskAdapter taskAdapter, LayoutInflater inflater) {
		this.inflater = inflater;
		this.taskEditor = taskEditor;
	}
	
	/**
	 * Get the ViewTypeCount that this factory can create
	 * @return The ViewTypeCount
	 */
	public int getViewTypeCount() {
		return TODO_ITEM_COUNT;
	}
	
	/**
	 * Get the ViewType that belongs to a Task
	 * @param task The Task to be analysed
	 * @return The ViewType of the Task
	 */
	public int getViewType(Task task) {
		if (task.getDueDate() == null && task.getDescription().length() == 0)
			return TODO_ITEM;
		else
			return TODO_ITEM_DATE;
	}
	
	/**
	 * Get The LayoutID that belongs to a viewType
	 * @param viewType The ViewType to be analysed
	 * @return A LayoutID
	 */
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

	/**
	 * Inflates a view based on a Task
	 * @param task The Task that will be displayed by the View
	 * @param parent The parent of the view
	 * @return A new view that will hold the Task
	 */
	private View inflateView(Task task, ViewGroup parent) {
		return inflater.inflate(getLayoutID(getViewType(task)), parent, false);
	}

	/**
	 * Creates or reuses a view and fills it with the Task as content
	 * @param task The Task that will be displayed by the View
	 * @param convertView The View that might be able to be recycled
	 * @param parent The parent of the View
	 * @return The new view filled with the Task as content
	 */
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
