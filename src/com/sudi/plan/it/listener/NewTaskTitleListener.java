package com.sudi.plan.it.listener;

import com.sudi.plan.it.models.TaskEditor;
import com.sudi.plan.it.views.FABController;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;

public class NewTaskTitleListener implements TextWatcher, OnFocusChangeListener, OnKeyListener {

	private FABController fab_expand;
	private boolean active = false;
	private TaskEditor task_editor;
	private OnClickListener createDetailTaskClicked;
	
	public NewTaskTitleListener(FABController edit_fab_controller, TaskEditor task_editor) {
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

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (hasFocus) {
			task_editor.cancelTaskSelected(true);
		}
	}

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
