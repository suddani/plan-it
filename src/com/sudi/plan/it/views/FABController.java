package com.sudi.plan.it.views;

import android.view.View.OnClickListener;

/**
 * Used to control FABs
 * @author dsudmann
 *
 */
public interface FABController {
	/**
	 * Makes the FAB visible/clickable
	 * @param listener The clicklistener that should be used on the FAB
	 */
	public void showFAB(OnClickListener listener);

	/**
	 * Hide the FAB
	 */
	public void hideFAB();

	/**
	 * Should be called to set the initial state of the FAB
	 */
	public void setup();
}
