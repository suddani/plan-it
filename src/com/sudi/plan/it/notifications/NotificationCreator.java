package com.sudi.plan.it.notifications;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
//import android.support.v4.app.NotificationCompat.WearableExtender;
import android.util.Log;

import com.sudi.plan.it.EditTaskActivity;
import com.sudi.plan.it.R;
import com.sudi.plan.it.models.Task;

/**
 * This class is used to create Notofications based on Tasks
 * @author dsudmann
 *
 */
public class NotificationCreator {
	private Context context;
	private NotificationManagerCompat notificationManager;

	public NotificationCreator(Context context) {
		this.context = context;
		
		// Get an instance of the NotificationManager service
		notificationManager = NotificationManagerCompat.from(context);
	}
	
	/**
	 * Cancel the notification that belongs to the Task
	 * @param task The Task whose notification should be canceled.
	 */
	public void cancel(Task task) {
		notificationManager.cancel((int) task.getId());
	}
	
	/**
	 * Display a notification for a Task
	 * @param task The Task that should be displayed int the Notification Bar
	 */
	public void notify(Task task) {
		if (task == null) {
			Log.e("PlanIt.Debug", "Fatal error...called notify on deleted task");
			return;
		}
	
		int notificationId = (int)task.getId();
		
		Log.d("PlanIt.Debug", "Create notification for: "+task.getTitle()+" - "+task.getId());

		NotificationCompat.Builder notificationBuilder =
		        new NotificationCompat.Builder(context)
		        .setSmallIcon(R.drawable.ic_launcher)
		        .setContentTitle(task.getTitle())
		        .setContentText(task.getDescription())
		        .addAction(R.drawable.ic_action_alerts_and_states_add_alarm, "Later", laterIntent(context, task))
		        .addAction(R.drawable.ic_action_content_delete_bar, "Delete", deleteIntent(context, task))
		        .setDeleteIntent(dismissIntent(context, task))
		        .setContentIntent(showIntent(context, task))
		        .setDefaults(Notification.DEFAULT_ALL)
		        .setAutoCancel(true);

		// Build the notification and issues it with notification manager.
		notificationManager.notify(notificationId, notificationBuilder.build());
	}
	
	/**
	 * Creates an intent to open the EditTaskActivity for a Task
	 * @param context The application Context
	 * @param task The Task that should be used
	 * @return The Intent that triggers the action
	 */
	private PendingIntent showIntent(Context context, Task task) {
		Intent intent = new Intent(context, EditTaskActivity.class);
		intent.putExtra("task_id", (long)task.getId());
		return PendingIntent.getActivity(
        		context,
        		(int)task.getId(),
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT);
	}
	
	/**
	 * Creates an intent to delete the Task
	 * @param context The application Context
	 * @param task The Task that should be deleted
	 * @return The Intent that triggers the action
	 */
	private PendingIntent deleteIntent(Context context, Task task) {
		Intent intent = new Intent(context, NotificationHandler.class);
		intent.putExtra("action", "delete");
		intent.putExtra("task_id", (long)task.getId());
		return PendingIntent.getBroadcast(
        		context,
        		(int)task.getId()*3,
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT);
	}
	
	/**
	 * Creates an intent to postpone the Task notification
	 * @param context The application Context
	 * @param task The Task that should be post poned
	 * @return The Intent that triggers the action
	 */
	private PendingIntent laterIntent(Context context, Task task) {
		Intent intent = new Intent(context, NotificationHandler.class);
		intent.putExtra("action", "later");
		intent.putExtra("task_id", (long)task.getId());
		return PendingIntent.getBroadcast(
        		context,
        		(int)task.getId()*3+1,
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT);
	}
	
	/**
	 * Creates an intent to dismiss/mark as done the Task notification
	 * @param context The application Context
	 * @param task The Task that should be dismissed/marked as done
	 * @return The Intent that triggers the action
	 */
	private PendingIntent dismissIntent(Context context, Task task) {
		Intent intent = new Intent(context, NotificationHandler.class);
		intent.putExtra("action", "done");
		intent.putExtra("task_id", (long)task.getId());
		return PendingIntent.getBroadcast(
        		context,
                (int)task.getId()*3+2,
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT);
	}
}
