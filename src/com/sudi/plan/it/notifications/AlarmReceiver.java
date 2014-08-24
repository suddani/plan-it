package com.sudi.plan.it.notifications;

import com.sudi.plan.it.models.TaskDbHelper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {
	
	TaskDbHelper dbHelper;

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d("PlanIt.Debug", "Received Alarm");
		dbHelper = new TaskDbHelper(context);
		long task_id = intent.getLongExtra("task_id", -1);

		NotificationCreator notification = new NotificationCreator(context);
		if (task_id != -1)
			notification.notify(dbHelper.getTask(task_id));
	}

}
