package com.sudi.plan.it.models;

/**
 * Interface for TaskList options
 * @author dsudmann
 *
 */
public interface TaskListEditor {
	/**
	 * Creates a new Task and saves it.
	 */
	public void newTask();
	
	/**
	 * Creates a new Task and opens an Edit interface for it. The Task is not saved yet.
	 */
	public void newTaskDetail();
	
	/**
	 * Opens an Edit interface for the Task
	 * @param task The Task to be edited
	 */
	public void editTask(Task task);
	
	/**
	 * Toggle the done state of the Task
	 * @param task The Task whose done state should be toggled
	 */
	public void toggleTask(Task task);
	
	/**
	 * Cancels the selection of Tasks
	 * @param callfinish Call finish for the current selection
	 * @return Returns true if Tasks where selected.
	 */
	public boolean cancelTaskSelected(boolean callfinish);
}
