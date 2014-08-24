package com.sudi.plan.it.models;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class TaskDbHelper extends SQLiteOpenHelper {

	private static final int 	DATABASE_VERSION 	= 1;
	private static final String DATABASE_NAME 		= "planit";
	private ArrayList<Task> taskList = null;

	public TaskDbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(Task.CREATE_STRING);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + Task.TABLE);
		onCreate(db);
	}
	
	public void addTask(Task task) {
		if (task.getId() != -1) {
			updateTask(task);
			return;
		}
		SQLiteDatabase db = this.getWritableDatabase();

		// Inserting Row
		task.setId(db.insert(Task.TABLE, null, task.getContentValues()));
		task.setDbHelper(this);
		loadTaskList(db);
		db.close();
	}
	
	public Task getTask(long id) {
//		String selectQuery = "SELECT  * FROM " + Task.TABLE + " WHERE ID "+Task.KEY_ID+" ASC, "+Task.KEY_DUEDATE+" ASC, "+Task.KEY_ID+" DESC";
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.query(Task.TABLE, new String[]{Task.KEY_ID,
				Task.KEY_TITLE,
				Task.KEY_DESCRIPTION,
				Task.KEY_DUEDATE,
				Task.KEY_DONE}, Task.KEY_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);
		if (cursor.moveToFirst()) {
			return Task.fromCursor(cursor, this);
		}else
			return null;
	}

	public List<Task> getTasks(boolean reload) {
		if (taskList != null && !reload)
			return taskList;
		
		return loadTaskList(null);
	}
	
	private List<Task> loadTaskList(SQLiteDatabase db_handle) {
		taskList = new ArrayList<Task>();
		String selectQuery = "SELECT  * FROM " + Task.TABLE + " ORDER BY "+Task.KEY_DONE+" ASC, "+Task.KEY_DUEDATE+" ASC, "+Task.KEY_ID+" DESC";
		SQLiteDatabase db = db_handle == null ? this.getReadableDatabase() : db_handle;
		Cursor cursor = db.rawQuery(selectQuery, null);
		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				// Adding contact to list
				taskList.add(Task.fromCursor(cursor, this));
			} while (cursor.moveToNext());
		}
		if (db_handle == null) 
			db.close();
		// return task list
		return taskList;
	}
	
	public void updateTask(Task task) {
		if (task.getId() == -1) {
			addTask(task);
			return;
		}
		task.setDbHelper(this);
		// updating row
		SQLiteDatabase db = this.getWritableDatabase();
		
		db.update(Task.TABLE, task.getContentValues(), Task.KEY_ID + " = ?", new String[]{String.valueOf(task.getId())});
		loadTaskList(db);
		db.close();
	}
	
	public void removeTask(Task task) {
		if (task.getId() == -1) {
			Log.d("PlanIt.Debug", "Can not remove task that is not in the Database");
			return;
		}
		if (taskList != null)
			taskList.remove(task);
		// updating row
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(Task.TABLE, Task.KEY_ID + " = ?", new String[]{String.valueOf(task.getId())});
		db.close();
	}

}