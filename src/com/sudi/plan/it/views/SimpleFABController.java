package com.sudi.plan.it.views;

import android.view.View;
import android.view.View.OnClickListener;

public class SimpleFABController implements FABController {

	private View fab;
	
	public SimpleFABController(View fab) {
		this.fab = fab;
	}

	@Override
	public void showFAB(OnClickListener listener) {
		fab.setVisibility(View.VISIBLE);
		fab.animate().translationY(0).alpha(1);
		fab.setOnClickListener(listener);
	}

	@Override
	public void hideFAB() {
		fab.animate().translationY(48+fab.getHeight()).alpha(0).withEndAction(new Runnable() {
			@Override
			public void run() {
				fab.setVisibility(View.INVISIBLE);
		}});
	}

	@Override
	public void setup() {
		fab.setTranslationY(48+fab.getHeight());
		fab.setVisibility(View.INVISIBLE);
	}

}
