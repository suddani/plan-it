package com.sudi.plan.it.models;

import java.util.Calendar;
import java.util.Date;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * A Task
 * @author dsudmann
 *
 */
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
	}

	public Task() {
		title = "";
		description = "";
	}

	/**
	 * This text is used when sharing the task.
	 * @return
	 */
	public String getShareText() {
		String shareText = title;
		if (description.length() != 0)
			shareText+="\n---\n"+description;
		if (dueDate != null)
			shareText+="\n---\n"+this.getPrettyDate();
		return shareText;
	}

	/**
	 * Get the task id
	 * @return The unique id of the Task
	 */
	public long getId() {
		return id;
	}

	/**
	 * Set the task id
	 * SHOULD NEVER BE USED
	 * @param l The id to be used
	 */
	public void setId(long l) {
		this.id = l;
	}

	/**
	 * Get the Task title
	 * @return The title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Set the Task title
	 * @param title The new title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Get the Task description/content
	 * @return The description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Set the Task description
	 * @param description The new description
	 */
	public void setDescription(String description) {
		this.description = ""+description;
	}

	/**
	 * Get the dueDate as a Date object
	 * @return The dueDate
	 */
	public Date getDueDate() {
		return dueDate;
	}

	/**
	 * Get the dueDate in milliseconds. Used to save the date in the Database
	 * @return The dueDate in milliseconds
	 */
	public long getDueDateMilli() {
		if (dueDate != null) {
			return dueDate.getTime();
		}
		return 0;
	}

	/**
	 * Get the dueDate as a pretty String. This is used to display the date and time in the Task list
	 * @return The dueDate/time as a String
	 */
	public String getPrettyDate() {
		if (dueDate != null) {
			return (String) android.text.format.DateFormat.format("dd MMM yyyy HH:mm", dueDate);
		}
		return "";
	}

	/**
	 * Get the date portion of the dueDate as a String. Used in the EditTaskActivity to display the date button.
	 * @return The date portion of the dueDate as a String.
	 */
	public String getDueDatePrettyDate() {
		if (dueDate != null) {
			return (String) android.text.format.DateFormat.format("dd MMM", dueDate);
		}
		return "";
	}

	/**
	 * Get the time portion of the dueDate as a String. Used in the EditTaskActivity to display the time button.
	 * @return The time portion of the dueDate as a String.
	 */
	public String getDueDatePrettyTime() {
		if (dueDate != null) {
			return (String) android.text.format.DateFormat.format("HH:mm", dueDate);
		}
		return "";
	}

	/**
	 * Set the dueDate from a Date object
	 * @param dueDate The new dueDate
	 */
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	/**
	 * Set the dueDate from milliseconds. This function is used when the Task is loaded from the Database.
	 * @param time The new dueDate in milliseconds
	 */
	public void setDueDateMilli(long time) {
		if (time == 0)
			return;
		this.dueDate = new Date(time);
	}

	/**
	 * The state of the Task
	 * @return Returns true if the Task is done.
	 */
	public boolean isDone() {
		return done;
	}

	/**
	 * Sets the state of the Task
	 * @param done Pass true to set the state to done
	 */
	public void setDone(boolean done) {
		this.done = done;
	}

	/**
	 * Get the TaskDbHelper this task belongs to and is managed by
	 * @return Returns the TaskDbHelper
	 */
	public TaskDbHelper getDbHelper() {
		return dbHelper;
	}

	/**
	 * Set the TaskDbHelper this Task belongs to.
	 * @param dbHelper The TaskDbHelper
	 */
	public void setDbHelper(TaskDbHelper dbHelper) {
		this.dbHelper = dbHelper;
	}

	/**
	 * Serialize the Task into ContentValues. This function is used to save/update the Task in the Database.
	 * @return A ContentValues instance that holds a Task
	 */
	public ContentValues getContentValues() {
		ContentValues values = new ContentValues();
		values.put(Task.KEY_TITLE, getTitle());
		values.put(Task.KEY_DESCRIPTION, getDescription());
		values.put(Task.KEY_DONE, isDone()?1:0);
		values.put(Task.KEY_DUEDATE, getDueDateMilli());
		return values;
	}

	/**
	 * Update/Save this Task in the Database.
	 */
	public void update() {
		getDbHelper().updateTask(this);
	}

	/**
	 * Delete this Task from the Database.
	 */
	public void delete() {
		getDbHelper().removeTask(this);
	}

	/**
	 * Set the dueDate to 10 minitues in the future. This is used by the notification when pressing "Later".
	 */
	public void rescedule() {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MINUTE, 10);
		setDueDate(c.getTime());
	}

	/**
	 * Check whether the Task has a dueDate.
	 * @return Returns true if the Task has a dueDate set.
	 */
	public boolean hasDueDate() {
		return dueDate != null;
	}

	/**
	 * Create a Task from a Database cursor
	 * @param cursor The cursor that holds Task values
	 * @return A new Task object initialized with the cursor.
	 */
	static public Task fromCursor(Cursor cursor) {
		Task task = new Task();
		task.setId(cursor.getInt(cursor.getColumnIndex(Task.KEY_ID)));
		task.setTitle(cursor.getString(cursor.getColumnIndex(Task.KEY_TITLE)));
		task.setDescription(cursor.getString(cursor.getColumnIndex(Task.KEY_DESCRIPTION)));
		task.setDone(cursor.getInt(cursor.getColumnIndex(Task.KEY_DONE))==1);
		task.setDueDateMilli(cursor.getLong(cursor.getColumnIndex(Task.KEY_DUEDATE)));
		return task;
	}

	/**
	 * Create a Task from a Database cursor
	 * @param cursor The cursor that holds Task values
	 * @param taskDbHelper The TaskDbHelper that will manage this Task
	 * @return A new Task object initialized with the cursor.
	 */
	public static Task fromCursor(Cursor cursor, TaskDbHelper taskDbHelper) {
		Task task = fromCursor(cursor);
		task.setDbHelper(taskDbHelper);
		return task;
	}

	/**
	 * Get the current Time/Date
	 * @return The current time/date as a Date object.
	 */
	static public Date now() {
		Calendar c = Calendar.getInstance();
		return c.getTime();
	}

	/**
	 * Create a Date object from milliseconds
	 * @param time The time in milliseconds the Date object should reflect
	 * @return A new Date object.
	 */
	static public Date at(int time) {
		return new Date(time);
	}

	/**
	 * Create a Date object pointing to 9am the next day. Used by the EditTaskActivity when pressing "Remind me".
	 * @return Returns a new Date object with the time set to 9am of the next day.
	 */
	public static Date tomorrowMorning() {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_MONTH, 1);
		c.set(Calendar.HOUR_OF_DAY, 9);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		return c.getTime();
	}
}
