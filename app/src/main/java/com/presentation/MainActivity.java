package com.presentation;

import java.util.ArrayList;

import com.persistence.DatabaseManager;
import com.persistence.ItemListORM;
import com.persistence.ListItemORM;
import com.integration.ExportIntentService;
import com.integration.HTTPTask;
import com.integration.ORMAdapter;
import com.purchaselist.R;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("UseSparseArrays")
public class MainActivity extends Activity {
	private static ArrayList<ListItemORM> list = new ArrayList<ListItemORM>();
	private static ItemListORM chosenList;

    public static final int PREFERENCES = 1337;
    public static final int SELECT_LIST = 8008135;

	private ArrayAdapter<ListItemORM> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		setContentView(R.layout.activity_main);

		adapter = new ArrayAdapter<ListItemORM>(this, android.R.layout.simple_list_item_1, list){
		    @Override
		    public View getView(int position, View convertView, ViewGroup parent) {
		    	TextView textView = new TextView(MainActivity.this);
		        textView.setText(list.get(position).getName() + " - " + list.get(position).getQuantity());
		        textView.setTag(list.get(position).getId());
		        textView.setPadding(6, 6, 6, 6);
		        textView.setTextSize(20);
		        return textView;
		    }
		};

		ListView listView = (ListView) findViewById(R.id.List);
		listView.setAdapter(adapter);
		
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {
		    
			public boolean onItemLongClick(AdapterView<?> a, final View v, final int position, long id) {
		    	
		    	AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

		    	builder.setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int id) {
		        		if (DatabaseManager.getInstance().deleteListItem(Integer.parseInt(((TextView) v).getTag().toString()))) {
			            	list.remove(position);
			        		adapter.notifyDataSetChanged();
			        		
			        		if (Prefs.sync && Prefs.restKey.length() > 0) {
			        			exportLists();
			        		}
		        		}
		            }
		        });

		    	builder.setNegativeButton(R.string.No, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });

		    	builder.setMessage(R.string.RemoveItem)
		    	       .setTitle(R.string.RemoveItem);

		    	AlertDialog dialog = builder.create();
		    	
		    	dialog.show();
		        return true;
		    }
			
		});
		
		updateList();
		
		Prefs.set(this);
		if (Prefs.sync && Prefs.restKey.length() > 0) {
			importLists();
		}
	}
	
	public void addToList(View v) {
		EditText nameField = (EditText) this.findViewById(R.id.Name);
		EditText quantityField = (EditText) this.findViewById(R.id.Quantity);
		String name = nameField.getText().toString().trim();
		String quantity = quantityField.getText().toString().trim();

		if (name.length() > 0 && quantity.length() > 0) {
			
			ListItemORM li = new ListItemORM();
			li.setName(name);
			li.setQuantity(quantity);
			li.setList(chosenList.getId());
				
			nameField.setText("");
			quantityField.setText("");
			
			if (DatabaseManager.getInstance().addListItem(li)) {	
				list.add(li);
				adapter.notifyDataSetChanged();
				
				if (Prefs.sync && Prefs.restKey.length() > 0) {
					exportLists();
				}
			}
		} else {
			Toast.makeText(this, "Input name and quantity", Toast.LENGTH_SHORT).show();
		}
	}
	
	public void openPreferences() {
		Intent pref = new Intent(this, PrefActivity.class);
		startActivityForResult(pref, PREFERENCES);
	}
	
	public void openLists(View v) {
		Intent listsIntent = new Intent("com.purchaselist.action.LIST_SELECT");
		startActivityForResult(listsIntent, SELECT_LIST);
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == SELECT_LIST) {
			if (resultCode == RESULT_OK) {
				String response = data.getExtras().getString("result");
				chosenList = DatabaseManager.getInstance().getItemList(Integer.parseInt(response));	
			}
			
			updateList();
		}
		
		if (requestCode == PREFERENCES) {
			Prefs.set(this);	
			if (Prefs.sync && Prefs.restKey.length() > 0) {
				importLists();
			}
		}
	}	
	
	public Boolean updateList() {
		if (chosenList != null && DatabaseManager.getInstance().getItemList(chosenList.getId()) == null) {
			chosenList = null;
		}
		
		if (DatabaseManager.getInstance().getItemListCount() == 0) {
			chosenList = null;
			ItemListORM ilnew = new ItemListORM();
			ilnew.setName("Default");
			DatabaseManager.getInstance().addItemList(ilnew);
		}
		
		if (chosenList == null) {
			chosenList = DatabaseManager.getInstance().getFirstItemList();
		}
		
		list.clear();
		ArrayList<ListItemORM> newList = DatabaseManager.getInstance().getAllListItems(chosenList.getId());

		for (ListItemORM l : newList) {
			list.add(l);
		}
		adapter.notifyDataSetChanged();
		
		TextView txtv = (TextView) findViewById(R.id.chosenList);
		txtv.setText(chosenList.getName());
		
		return true;
	}
	
	private void importLists() {
		try {
			new HTTPTask(Prefs.restUrl, MainActivity.this).execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
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

}
