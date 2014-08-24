package com.sudi.plan.it.models;

import java.util.Calendar;
import java.util.Date;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;


public class Task {

	public static final String TABLE 			= "tasks";
	public static final String KEY_ID 			= "id";
	public static final String KEY_TITLE 		= "title";
	public static final String KEY_DESCRIPTION 	= "description";
	public static final String KEY_DUEDATE 		= "dueDate";
	public static final String KEY_DONE 		= "done";
	
	public static final String CREATE_STRING = "CREATE TABLE IF NOT EXISTS " + TABLE + " ( "
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ KEY_TITLE+ " TEXT, "
			+ KEY_DESCRIPTION+ " TEXT, "
			+ KEY_DUEDATE+ " INTEGER, "
            + KEY_DONE + " INTEGER)";
	
	private long 	id = -1;
	private String 	title;
	private String 	description;
	private Date 	dueDate;
	private boolean done;
	
	TaskDbHelper dbHelper;

	public Task(String title) {
		this.title = title;
		description = "";

		Log.d("PlanIt.Debug", "Created Task with date: "+dueDate);
	}

	public Task() {
		title = "";
		description = "";
	}
	
	public String getShareText() {
		String shareText = title;
		if (description.length() != 0)
			shareText+="\n---\n"+description;
		if (dueDate != null)
			shareText+="\n---\n"+this.getPrettyDate();
		return shareText;
	}

	public long getId() {
		return id;
	}

	public void setId(long l) {
		this.id = l;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = ""+description;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public long getDueDateMilli() {
		if (dueDate != null) {
			return dueDate.getTime();
		}
		return 0;
	}
	
	public String getPrettyDate() {
		if (dueDate != null) {
			return (String) android.text.format.DateFormat.format("dd MMM yyyy HH:mm", dueDate);
		}
		return "";
	}
	
	public String getDueDatePrettyDate() {
		if (dueDate != null) {
			return (String) android.text.format.DateFormat.format("dd MMM", dueDate);
		}
		return "";
	}
	
	public String getDueDatePrettyTime() {
		if (dueDate != null) {
			return (String) android.text.format.DateFormat.format("HH:mm", dueDate);
		}
		return "";
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public void setDueDateMilli(long time) {
		if (time == 0)
			return;
		this.dueDate = new Date(time);
	}
	
	public boolean isDone() {
		return done;
	}

	public void setDone(boolean done) {
		this.done = done;
	}
	
	public TaskDbHelper getDbHelper() {
		return dbHelper;
	}

	public void setDbHelper(TaskDbHelper dbHelper) {
		this.dbHelper = dbHelper;
	}

	public ContentValues getContentValues() {
		ContentValues values = new ContentValues();
		values.put(Task.KEY_TITLE, getTitle());
		values.put(Task.KEY_DESCRIPTION, getDescription());
		values.put(Task.KEY_DONE, isDone()?1:0);
		values.put(Task.KEY_DUEDATE, getDueDateMilli());
		return values;
	}
	
	public void update() {
		getDbHelper().updateTask(this);
	}
	
	public void delete() {
		getDbHelper().removeTask(this);
	}

	public void rescedule() {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MINUTE, 10);
		setDueDate(c.getTime());
	}
	
	static public Task fromCursor(Cursor cursor) {
		Task task = new Task();
		task.setId(cursor.getInt(cursor.getColumnIndex(Task.KEY_ID)));
		task.setTitle(cursor.getString(cursor.getColumnIndex(Task.KEY_TITLE)));
		task.setDescription(cursor.getString(cursor.getColumnIndex(Task.KEY_DESCRIPTION)));
		task.setDone(cursor.getInt(cursor.getColumnIndex(Task.KEY_DONE))==1);
		task.setDueDateMilli(cursor.getLong(cursor.getColumnIndex(Task.KEY_DUEDATE)));
		return task;
	}

	public static Task fromCursor(Cursor cursor, TaskDbHelper taskDbHelper) {
		Task task = fromCursor(cursor);
		task.setDbHelper(taskDbHelper);
		return task;
	}

	static public Date now() {
		Calendar c = Calendar.getInstance();
		return c.getTime();
	}

	static public Date at(int time) {
		return new Date(time);
	}

	public boolean hasDueDate() {
		return dueDate != null;
	}

	public static Date tomorrowMorning() {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_MONTH, 1);
		c.set(Calendar.HOUR_OF_DAY, 9);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		return c.getTime();
	}
}
