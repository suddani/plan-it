package com.sudi.plan.it.listener;

import com.sudi.plan.it.models.TaskListEditor;
import com.sudi.plan.it.views.FABController;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;

/**
 * This class is used to manage the different states the NewTodo item inputbox can have
 * @author dsudmann
 *
 */
public class NewTaskTitleListener implements TextWatcher, OnFocusChangeListener, OnKeyListener {

	private FABController fab_expand;
	private boolean active = false;
	private TaskListEditor task_editor;
	private OnClickListener createDetailTaskClicked;
	
	public NewTaskTitleListener(FABController edit_fab_controller, TaskListEditor task_editor) {
		this.fab_expand = edit_fab_controller;
		this.task_editor = task_editor;
		this.createDetailTaskClicked = new OnClickListener(){
			@Override
			public void onClick(View v) {
				NewTaskTitleListener.this.task_editor.newTaskDetail();
		}};
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// nothing todo here
		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// nothing todo here
	}

	/**
	 * Show the Fab_expand controller button in case there is text in the inputBox
	 */
	@Override
	public void afterTextChanged(Editable s) {
		if (s.length() > 0 && !active) {
			fab_expand.showFAB(createDetailTaskClicked);
			active = true;
		}else if (s.length() ==  0 && active){
			fab_expand.hideFAB();
			active = false;
		}
	}

	/**
	 * Cancel the actionMode if the user selects the InputBox
	 */
	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (hasFocus) {
			task_editor.cancelTaskSelected(true);
		}
	}

	/**
	 * add the newly typped todo item to the list
	 */
	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN)
        {
            switch (keyCode)
            {
                case KeyEvent.KEYCODE_DPAD_CENTER:
                case KeyEvent.KEYCODE_ENTER:
                    task_editor.newTask();
                    return true;
                default:
                    break;
            }
        }
        return false;
	}

}
