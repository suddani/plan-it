package com.sudi.plan.it.listener;

import com.sudi.plan.it.MainActivity;
import com.sudi.plan.it.R;
import com.sudi.plan.it.models.ListItem;

import android.content.Intent;
import android.view.ActionMode;
import android.view.ActionMode.Callback;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ShareActionProvider;

public class SingleTaskActionMode implements Callback {

	private MainActivity mainActivity;
	private ListItem selected_item;
	private ShareActionProvider mShareActionProvider;

	public SingleTaskActionMode(MainActivity mainActivity, ListItem listItem) {
		this.mainActivity = mainActivity;
		this.selected_item = listItem;
	}

	@Override
	public boolean onCreateActionMode(ActionMode mode, Menu menu) {
		MenuInflater inflater = mode.getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
        
        MenuItem item = menu.findItem(R.id.action_share);
	    // Fetch and store ShareActionProvider
	    mShareActionProvider = (ShareActionProvider) item.getActionProvider();
        
        setShareIntent(getTextIntent(selected_item.getTask().getShareText()));
		return true;
	}

	@Override
	public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
//		switch (item.getItemId()) {
//	        case R.id.action_share:
//	        	setShareIntent(getTextIntent(((ListItem)mode.getTag()).getTask().getTitle()));
//	            mode.finish(); // Action picked, so close the CAB
//	            return true;
//	        default:
//	            return false;
//	    }
		return false;
	}

	@Override
	public void onDestroyActionMode(ActionMode mode) {
		mainActivity.cancelItemSelected(false);
		if (mode.getTag() != null) {
			((View)mode.getTag()).setSelected(false);
		} else
			selected_item.setSelected(false);
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

}
