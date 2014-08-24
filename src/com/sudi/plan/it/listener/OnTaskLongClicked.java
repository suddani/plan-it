package com.sudi.plan.it.listener;

import com.sudi.plan.it.MainActivity;
import com.sudi.plan.it.models.ListItem;
import com.sudi.plan.it.models.Task;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;

public class OnTaskLongClicked implements OnItemLongClickListener {

	private MainActivity mainActivity;

	public OnTaskLongClicked(MainActivity mainActivity) {
		this.mainActivity = mainActivity;
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		mainActivity.cancelItemSelected(true);
		mainActivity.todoItemSelected(new ListItem(view, (Task)parent.getAdapter().getItem(position)));
		view.setSelected(true);
		return true;
	}

}
