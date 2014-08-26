package com.sudi.plan.it.notifications;

import java.util.Calendar;

import com.sudi.plan.it.models.Task;
import com.sudi.plan.it.models.TaskDbHelper;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

/**
 * Used to shedule notifications for Tasks.
 * It is also used to as a receiver for the BOOT event to activate the alarm again after a reboot of the phone
 * @author dsudmann
 *
 */
public class Notifier extends BroadcastReceiver {
	private AlarmManager alarmMgr;
	private Context context;
	private TaskDbHelper dbHelper;
	
	public Notifier(Context context, TaskDbHelper dbHelper) {
		this.dbHelper = dbHelper;
		init(context);
	}
	
	public Notifier() {
	}

	/**
	 * Init the notifier with a context.
	 * @param context The context of the notifier
	 */
	private void init(Context context) {
		this.context = context;
		this.alarmMgr = (AlarmManager)this.context.getSystemService(Context.ALARM_SERVICE);
	}

	/**
	 * Set the alarm for a Task
	 * @param task The Task the alarm should be set for
	 */
	private void setWakeUp(Task task) {
		Log.d("PlanIt.Debug", "find new task for notification");
		Intent intent = new Intent(context, AlarmReceiver.class);
		if (task != null) {
			intent.putExtra("task_id", task.getId());
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(task.getDueDate());
//			calendar.add(Calendar.SECOND, 10);
			alarmMgr.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT));
			Log.d("PlanIt.Debug", "find new task for notification...found it: "+task.getId());
		} else {
			Log.d("PlanIt.Debug", "cancel all pending notifications");
			alarmMgr.cancel(PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT));
		}
	}

	/**
	 * Set the Alarm to the Task with the closest dueDate in the Future whose state is not done.
	 * @param dbHelper_ The TaskDbHelper that should be used by this function. If null is passed and the Notifier was not initialized with one a new one will be created.
	 */
	public void setNextAlarm(TaskDbHelper dbHelper_) {
		dbHelper = dbHelper_ != null ? dbHelper_ : (dbHelper != null ? dbHelper : new TaskDbHelper(context));
		Task task = dbHelper.getNextDueTask(null);
		if (task == null) {
			disable();
		}
		setWakeUp(task);
		enable();
	}

	/**
	 * Enable the on reboot listener
	 */
	private void enable() {
		ComponentName receiver = new ComponentName(context, Notifier.class);
		PackageManager pm = context.getPackageManager();

		pm.setComponentEnabledSetting(receiver,
		        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
		        PackageManager.DONT_KILL_APP);
	}

	/**
	 * Disable the on reboot listener
	 */
	private void disable() {
		ComponentName receiver = new ComponentName(context, Notifier.class);
		PackageManager pm = context.getPackageManager();

		pm.setComponentEnabledSetting(receiver,
		        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
		        PackageManager.DONT_KILL_APP);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            // Set the alarm here.
			init(context);
			setNextAlarm(null);
        }
	}

}
