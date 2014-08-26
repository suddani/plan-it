package com.sudi.plan.it.notifications;

import com.sudi.plan.it.models.Task;
import com.sudi.plan.it.models.TaskDbHelper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class NotificationHandler extends BroadcastReceiver {
	
	private void notifiyActivity(Context context) {
		Intent myFilteredResponse= new Intent("task.updated");
		context.sendBroadcast(myFilteredResponse);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		
		TaskDbHelper dbHelper = new TaskDbHelper(context);
		Notifier notifier = new Notifier(context, dbHelper);
		
		String action = intent.getStringExtra("action");
		long task_id = intent.getLongExtra("task_id", -1);
		NotificationCreator notification = new NotificationCreator(context);
		
		Log.d("PlanIt.Debug", "[MainActivity]Called new Intent and update Task: "+task_id+" - "+action);
		
		if (task_id != -1) {
			Task task = dbHelper.getTask(task_id);
			notification.cancel(task);

			if (action.contentEquals("delete")) {
				task.delete();
				Log.d("PlanIt.Debug", "Delete task: "+task_id);
			} else if (action.contentEquals("done")) {
				task.setDone(true);
				task.update();
				Log.d("PlanIt.Debug", "Mark task as done: "+task_id);
			} else if (action.contentEquals("later")) {
				task.rescedule();
				task.update();
				Log.d("PlanIt.Debug", "Rescedule Task: "+task_id);
			}
			notifiyActivity(context);
			notifier.setNextAlarm(dbHelper);
		}
		dbHelper.close();
	}

}
