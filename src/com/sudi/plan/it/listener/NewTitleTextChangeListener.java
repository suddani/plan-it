package com.sudi.plan.it.listener;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageButton;

public class NewTitleTextChangeListener implements TextWatcher {

	private ImageButton fab_expand;
	private boolean active = false;

	public NewTitleTextChangeListener(ImageButton fab_expand) {
		this.fab_expand = fab_expand;
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterTextChanged(Editable s) {
		if (s.length() > 0 && !active) {
			fab_expand.setVisibility(View.VISIBLE);
			fab_expand.animate().translationY(0).alpha(1);
			active = true;
		}else if (s.length() ==  0 && active){
			fab_expand.animate().translationY(48+fab_expand.getHeight()).alpha(0).withEndAction(new Runnable() {

				@Override
				public void run() {
					fab_expand.setVisibility(View.INVISIBLE);
				}});

			active = false;
		}
	}

}
