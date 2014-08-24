package com.sudi.plan.it.models;

import java.util.List;

import com.sudi.plan.it.R;
import com.sudi.plan.it.views.TaskViewFactory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class TaskAdapter extends ArrayAdapter<Task> {

	private TaskDbHelper dbHelper;
	private TaskViewFactory factory;

	public TaskAdapter(Context context, TaskEditor taskEditor) {
		super(context, R.layout.todo_item_date);
		dbHelper = new TaskDbHelper(context);
		factory = new TaskViewFactory(taskEditor, this, (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE));
	}

	private List<Task> tasks() {
		return dbHelper.getTasks(false);
	}
	
	public void reload() {
		this.dbHelper.getTasks(true);
		notifyDataSetChanged();
	}
	
	@Override
	public int getItemViewType(int position) {
		Task task = getItem(position);
		return factory.getViewType(task);
	}
	
	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public void add(Task task) {
		dbHelper.addTask(task);
		notifyDataSetChanged();
	}

	@Override
	public void remove(Task task) {
		dbHelper.removeTask(task);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return tasks().size();
	}

	@Override
	public Task getItem(int position) {
		if (tasks().size()<=position)
			return null;
		return tasks().get(position);
	}

	@Override
	public int getPosition(Task item) {
		return tasks().indexOf(item);
	}

	@Override
	public long getItemId(int position) {
		Task task = getItem(position);
		if (task == null)
			return -1;
		return task.getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Task current = tasks().get(position);
		return factory.create(current, convertView, parent);
	}

}
