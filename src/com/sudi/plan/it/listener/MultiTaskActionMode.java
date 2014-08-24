package com.sudi.plan.it.listener;

import java.util.ArrayList;

import com.sudi.plan.it.R;
import com.sudi.plan.it.animations.ListViewAnimator;
import com.sudi.plan.it.models.ListItem;
import com.sudi.plan.it.models.Task;
import com.sudi.plan.it.views.FABController;

import android.content.Intent;
import android.util.Log;
import android.util.LongSparseArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.ShareActionProvider;
import android.widget.AbsListView.MultiChoiceModeListener;

public class MultiTaskActionMode implements MultiChoiceModeListener {

	private ShareActionProvider mShareActionProvider;
	private ListView listView;
	private LongSparseArray<ListItem> selected_items;
	private ActionMode active_mode;
	private FABController fabController;

	public MultiTaskActionMode(ListView listView, FABController fabController) {
		this.listView = listView;
		this.fabController = fabController;
		selected_items = new LongSparseArray<ListItem>();
	}

	@Override
	public boolean onCreateActionMode(ActionMode mode, Menu menu) {
		active_mode = mode;
		fabController.showFAB(new OnClickListener(){

			@Override
			public void onClick(View v) {
				deleteSelectedItems();
			}});
		return true;
	}

	protected void deleteSelectedItems() {
		if (active_mode != null) {
			ArrayList<View> viewsToremove = new ArrayList<View>();
			for (int i=0;i<selected_items.size();i++) {
				viewsToremove.add(selected_items.get(selected_items.keyAt(i)).getView());
			}
			ListViewAnimator animator = new ListViewAnimator(listView, viewsToremove);
			animator.animateLayout();
			for (int i=0;i<selected_items.size();i++) {
				selected_items.get(selected_items.keyAt(i)).getTask().delete();
			}
			active_mode.finish();
		}
	}

	@Override
	public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
		MenuInflater inflater = mode.getMenuInflater();
		menu.clear();
		if (selected_items.size() == 1) {
	        inflater.inflate(R.menu.context_menu, menu);
	        MenuItem item = menu.findItem(R.id.action_share);
		    // Fetch and store ShareActionProvider
		    mShareActionProvider = (ShareActionProvider) item.getActionProvider();
	        setShareIntent(getTextIntent(selected_items.get(selected_items.keyAt(0)).getTask().getShareText()));
		} else {
			inflater.inflate(R.menu.context_menu_multi, menu);
		}
		return true;
	}

	@Override
	public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_delete) {
			deleteSelectedItems();
			return true;
		}
		return false;
	}

	@Override
	public void onDestroyActionMode(ActionMode mode) {
		selected_items.clear();
		fabController.hideFAB();
		active_mode = null;
	}

	@Override
	public void onItemCheckedStateChanged(ActionMode mode, int position,
			long id, boolean checked) {
		Log.d("PlanIt.Debug", "Item: "+position+":"+id+">"+checked);
		View v = listView.getChildAt(position);
		if (checked) {
			selected_items.put(id, new ListItem(v, (Task)listView.getItemAtPosition(position)));
		} else {
			selected_items.remove(id);
		}
		mode.invalidate();
	}
	
	// Call to update the share intent
	private void setShareIntent(Intent shareIntent) {
	    if (mShareActionProvider != null) {
	    	mShareActionProvider.setShareIntent(shareIntent);
	    }
	}
	
	private Intent getTextIntent(String text) {
	    Intent intent = new Intent(Intent.ACTION_SEND);
	    intent.setType("text/plain");
	    intent.putExtra(Intent.EXTRA_TEXT, text);
	    return intent;
	}

	public boolean finish() {
		if (active_mode != null) {
			active_mode.finish();
			return true;
		}
		return false;
	}
}
