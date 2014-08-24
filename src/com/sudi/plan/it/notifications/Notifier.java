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
	
	private void init(Context context) {
		this.context = context;
		this.alarmMgr = (AlarmManager)this.context.getSystemService(Context.ALARM_SERVICE);
	}

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

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            // Set the alarm here.
			init(context);
			setNextAlarm(null);
        }
	}
	
	public void setNextAlarm(TaskDbHelper dbHelper_) {
		dbHelper = dbHelper_ != null ? dbHelper_ : (dbHelper != null ? dbHelper : new TaskDbHelper(context));
		Task task = dbHelper.getNextDueTask(null);
		if (task == null) {
			disable();
		}
		setWakeUp(task);
		enable();
	}
	
	private void enable() {
		ComponentName receiver = new ComponentName(context, Notifier.class);
		PackageManager pm = context.getPackageManager();

		pm.setComponentEnabledSetting(receiver,
		        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
		        PackageManager.DONT_KILL_APP);
	}
	
	private void disable() {
		ComponentName receiver = new ComponentName(context, Notifier.class);
		PackageManager pm = context.getPackageManager();

		pm.setComponentEnabledSetting(receiver,
		        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
		        PackageManager.DONT_KILL_APP);
	}

}
