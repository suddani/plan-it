package com.sudi.plan.it.views;

import android.view.View.OnClickListener;

public interface FABController {
	public void showFAB(OnClickListener listener);
	public void hideFAB();
	public void setup();
}
