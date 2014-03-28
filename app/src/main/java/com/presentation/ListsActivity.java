package com.presentation;

import java.util.ArrayList;

import com.persistence.DatabaseManager;
import com.persistence.ItemListORM;
import com.persistence.ListItemORM;
import com.integration.ExportIntentService;
import com.integration.ORMAdapter;
import com.purchaselist.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class ListsActivity extends Activity {
	private static ArrayAdapter<ItemListORM> listsAdapter;
	public static ArrayList<ItemListORM> listsList = new ArrayList<ItemListORM>();

    public static final int PREFERENCES = 1337;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		setContentView(R.layout.activity_list_select);
		
		listsList = DatabaseManager.getInstance().getAllItemLists();
		
		// create adapter from that list
		listsAdapter = new ArrayAdapter<ItemListORM>(this, android.R.layout.simple_list_item_1, listsList){
		    @Override
		    public View getView(int position, View convertView, ViewGroup parent) {
		    	TextView textView = new TextView(ListsActivity.this);
		        textView.setText(listsList.get(position).getName());
		        textView.setTag(listsList.get(position).getId());
		        textView.setPadding(6, 6, 6, 6);
		        textView.setTextSize(20);
		        return textView;
		    }
		};
		
		// attach adapter to list view
		ListView lst = (ListView) findViewById(R.id.Lists);
		lst.setAdapter(listsAdapter);
		
		// on delete
		lst.setOnItemLongClickListener(new OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> a, final View v, final int position, long id) {
				// do not delete last list
				if (listsList.size() <= 1) 
					return false;
				
		    	AlertDialog.Builder builder = new AlertDialog.Builder(ListsActivity.this);

		    	builder.setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int id) {
		            	int ilId = Integer.parseInt(((TextView) v).getTag().toString());
		            	if (DatabaseManager.getInstance().deleteItemList(ilId)) {
		            		listsList.remove(position);
		            		
		            		ArrayList<ListItemORM> lis = DatabaseManager.getInstance().getAllListItems(ilId);
		            		for (ListItemORM li : lis) {
		            			DatabaseManager.getInstance().deleteListItem(li.getId());
		            		}
		            		
		            		listsAdapter.notifyDataSetChanged();
		            		
		            		if (Prefs.sync && Prefs.restKey.length() > 0) {
		            			exportLists();
		            		}
		            	}	
		            }
		        });
		    	
		    	builder.setNegativeButton(R.string.No, new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int id) {}
		        });
		 
		    	builder.setMessage(R.string.RemoveItem).setTitle(R.string.RemoveItem);
		    	AlertDialog dialog = builder.create();
		    	
		    	dialog.show();

		        return true;		        
		    }
		});	
		
		lst.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> a, final View v, final int position, long id) {
				Intent result = new Intent(Intent.ACTION_PICK);
				result.putExtra("result", (((TextView) v).getTag().toString()));
				setResult(RESULT_OK, result);
				finish();
		    }
		});	
		
	}
	
	public void onResume() {
		super.onResume();	
		Prefs.set(this);	
	}	
	
	public void addNewList(View v) {
		String newListName = ((EditText) findViewById(R.id.NewList)).getText().toString().trim();
		
		if (newListName.length() > 0  && DatabaseManager.getInstance().checkItemListName(newListName)) {
    		
			
			// make new list ORM object
			ItemListORM newList = new ItemListORM();
			newList.setName(newListName);
			
			// add to DB
			DatabaseManager.getInstance().addItemList(newList);
			
			// update view
			listsList.add(newList);
			listsAdapter.notifyDataSetChanged();
			
    		if (Prefs.sync && Prefs.restKey.length() > 0) {
    			exportLists();
    		}

		}
		
		((EditText) findViewById(R.id.NewList)).setText("");
	}	

	private void exportLists() {
		Boolean notif = false;
		if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("notif", false)) {
			notif = true;
		}
		String data = ORMAdapter.getPurchaseMap(DatabaseManager.getInstance());
		Intent intent = new Intent(this, ExportIntentService.class);
		startService(intent.putExtra("url", Prefs.restUrl).putExtra("json", data).putExtra("notif", notif));
	}


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings:
                openPreferences();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void openPreferences() {
        Intent pref = new Intent(this, PrefActivity.class);
        startActivityForResult(pref, PREFERENCES);
    }
	
	
}
