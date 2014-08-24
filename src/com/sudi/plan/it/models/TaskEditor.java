package com.sudi.plan.it.models;

public interface TaskEditor {
	public void newTask();
	public void newTaskDetail();
	public void editTask(Task task);
	public boolean cancelTaskSelected(boolean callfinish);
//	public void deleteTask(Task task);
}
