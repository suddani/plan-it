package com.sudi.plan.it.listener;

import com.sudi.plan.it.R;
import com.sudi.plan.it.animations.ListViewAnimator;
import com.sudi.plan.it.models.Task;
import com.sudi.plan.it.models.TaskAdapter;
import com.sudi.plan.it.notifications.Notifier;

import android.graphics.Paint;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class OnTaskClicked implements OnItemClickListener {
	
	private TaskAdapter adapter;
	private Notifier notifier;

	public OnTaskClicked(TaskAdapter adapter, Notifier notifier) {
		this.adapter = adapter;
		this.notifier = notifier;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		
		TextView title = (TextView)view.findViewById(R.id.item_title);
		if ((title.getPaintFlags() & Paint.STRIKE_THRU_TEXT_FLAG) == Paint.STRIKE_THRU_TEXT_FLAG) {
			title.setPaintFlags(title.getPaintFlags() ^ Paint.STRIKE_THRU_TEXT_FLAG);
		}else
			title.setPaintFlags(title.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

		ListViewAnimator animator = new ListViewAnimator((ListView)parent, (View)null);
		animator.animateLayout();
		Task task = (Task)view.getTag();
		task.setDone(!task.isDone());
		task.update();
		notifier.setNextAlarm(null);
		adapter.notifyDataSetChanged();
	}

}
