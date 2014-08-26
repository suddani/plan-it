package com.sudi.plan.it.fragments;

import java.util.Calendar;

import com.sudi.plan.it.EditTaskActivity;
import com.sudi.plan.it.models.Task;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

/**
 * This fragment is used to create a time picker based on a supplied task
 * @author dsudmann
 *
 */
public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
	
	private Task task;
	private EditTaskActivity editTaskActivity;

	public TimePickerFragment(Task task, EditTaskActivity editTaskActivity) {
		this.task = task;
		this.editTaskActivity = editTaskActivity;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the task time as the default values for the picker
		final Calendar c = Calendar.getInstance();
		c.setTime(task.getDueDate());
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
		
		// Create a new instance of TimePickerDialog and return it
		return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
	}

	@Override
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		final Calendar c = Calendar.getInstance();
		c.setTime(task.getDueDate());
		c.set(Calendar.HOUR_OF_DAY, hourOfDay);
		c.set(Calendar.MINUTE, minute);
		task.setDueDate(c.getTime());
		editTaskActivity.showReminderButtons();
	}
}