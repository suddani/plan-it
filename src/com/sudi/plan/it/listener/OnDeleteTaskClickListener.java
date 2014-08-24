package com.sudi.plan.it.listener;

import com.sudi.plan.it.animations.ListViewAnimator;
import com.sudi.plan.it.models.ListItem;
import com.sudi.plan.it.models.TaskAdapter;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

public class OnDeleteTaskClickListener implements OnClickListener, Runnable {
	
	private TaskAdapter taskAdapter;
	private View clicked_view;
	private ListItem item;
	private ListView listview;

	public OnDeleteTaskClickListener(TaskAdapter taskAdapter, ListItem item, ListView listview) {
		this.taskAdapter = taskAdapter;
		this.item = item;
		this.listview = listview;
	}
	
	private void animateOut() {
		clicked_view.setClickable(false);
		listview.setEnabled(false);
		item.getView().setHasTransientState(true);
		item.getView().animate().setDuration(50).alpha(0).withEndAction(this);
	}
	
	@Override
	public void onClick(View view) {
		this.clicked_view = view;
		animateOut();
	}

	@Override
	public void run() {
		item.getView().setHasTransientState(false);
		item.getView().setAlpha(1);
		clicked_view.setClickable(true);
		ListViewAnimator animator = new ListViewAnimator(listview, item.getView());
		animator.animateLayout();
		taskAdapter.remove(item.getTask());
	}
}
