package com.sudi.plan.it.listener;

import com.sudi.plan.it.views.FABController;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View.OnClickListener;

public class NewTitleTextChangeListener implements TextWatcher {

	private FABController fab_expand;
	private boolean active = false;
	private OnClickListener onClickListener;

	public NewTitleTextChangeListener(FABController edit_fab_controller, OnClickListener onClickListener) {
		this.fab_expand = edit_fab_controller;
		this.onClickListener = onClickListener;
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
	}

	@Override
	public void afterTextChanged(Editable s) {
		if (s.length() > 0 && !active) {
			fab_expand.showFAB(onClickListener);
			active = true;
		}else if (s.length() ==  0 && active){
			fab_expand.hideFAB();
			active = false;
		}
	}

}
