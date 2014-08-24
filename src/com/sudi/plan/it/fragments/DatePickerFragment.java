package com.sudi.plan.it.fragments;

import java.util.Calendar;

import com.sudi.plan.it.EditTaskActivity;
import com.sudi.plan.it.models.Task;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
	
	private Task task;
	private EditTaskActivity editTaskActivity;

	public DatePickerFragment(Task task, EditTaskActivity editTaskActivity) {
		this.task = task;
		this.editTaskActivity = editTaskActivity;
	}

	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the task date as the default date in the picker
        final Calendar c = Calendar.getInstance();
		c.setTime(task.getDueDate());
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }
	
	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		final Calendar c = Calendar.getInstance();
		c.setTime(task.getDueDate());
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, monthOfYear);
		c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
		task.setDueDate(c.getTime());
		editTaskActivity.showReminderButtons();
	}

}
