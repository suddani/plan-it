package com.sudi.plan.it.animations;

import java.util.ArrayList;
import java.util.List;

import android.util.LongSparseArray;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.ListView;

public class ListViewAnimator implements OnPreDrawListener {
	
	private ListView listview;
	private View viewToRemove;
	private List<View> viewsToRemove;
	private ViewTreeObserver observer;
	private LongSparseArray<Integer> itemTopMap;
	
	public ListViewAnimator(ListView listview, View view) {
		this.listview = listview;
		this.viewToRemove = view;
		this.viewsToRemove = new ArrayList<View>();
	}
	
	public ListViewAnimator(ListView listview, List<View> viewsToRemove) {
		this.listview = listview;
		this.viewToRemove = null;
		this.viewsToRemove = viewsToRemove;
	}

	/**
	 * After this method is called the animation is hot.
	 * This instance should not be used anymore after this!
	 */
	public void animateLayout() {
		
		// Lets do some magic
		itemTopMap = new LongSparseArray<Integer>();
		int firstVisiblePosition = listview.getFirstVisiblePosition();
		for (int i=0;i<listview.getChildCount();i++) {
			View child = listview.getChildAt(i);
			if (child != viewToRemove || viewsToRemove.contains(child)) {
				int position = firstVisiblePosition+i;
				long itemId = listview.getAdapter().getItemId(position);
				itemTopMap.put(itemId, child.getTop());
			}
		}
		
		observer = listview.getViewTreeObserver();
		observer.addOnPreDrawListener(this);
	}
	
	/**
	 * Do not call this method!!! It is used internally
	 */
	@Override
	public boolean onPreDraw() {
		observer.removeOnPreDrawListener(this);
		boolean firstAnimation = true;
		int firstVisiblePosition = listview.getFirstVisiblePosition();
		for (int i=0;i<listview.getChildCount();i++) {
			View child = listview.getChildAt(i);
			int position = firstVisiblePosition+i;
			long itemId = listview.getAdapter().getItemId(position);
			Integer startTop = itemTopMap.get(itemId);
			int top = child.getTop();
			if (startTop != null) {
				if (startTop != top) {
					animateView(child, startTop, top);
					if (firstAnimation) {
						listview.setEnabled(true);
						firstAnimation = false;
					}
				}
			} else {
				int childHeight = child.getHeight() + listview.getDividerHeight();
				startTop = top + (i > 0 ? childHeight : -childHeight);

				animateView(child, startTop, top);
				if (firstAnimation) {
					listview.setEnabled(true);
					firstAnimation = false;
				}
			}
		}
		if (firstAnimation) {
			listview.setEnabled(true);
			firstAnimation = false;
		}
		return true;
	}

	private void animateView(View child, int start, int end) {
		int delta = start-end;
		child.setTranslationY(delta);
		child.animate().setDuration(200).translationY(0);
	}

}
