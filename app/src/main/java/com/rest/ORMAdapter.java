package com.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.preference.PreferenceManager;
import android.widget.Toast;

import com.orm_model.DatabaseManager;
import com.orm_model.ItemListORM;
import com.orm_model.ListItemORM;
import com.purchaselist.PurchasesApp;

public class ORMAdapter {
	
	public static String getPurchaseMap(DatabaseManager db) {
		ArrayList<ItemListORM> ils = db.getAllItemLists();
		Map<String, List<Purchase>> exportMap = new HashMap<String, List<Purchase>>();
		for (ItemListORM i : ils) {
			ArrayList<ListItemORM> lis =  db.getAllListItems(i.getId());
			List<Purchase> lp = new ArrayList<Purchase>();
			
			for (ListItemORM l : lis) {
				Purchase p = new Purchase();
				p.setName(l.getName());
				p.setQuantity(l.getQuantity());
				
				lp.add(p);
			}
			
			exportMap.put(i.getName(), lp);
		}
		
		return GSONConverter.toJson(exportMap);
	}
	
	public static Boolean JSONtoORM(String response) {
		Map<String, List<Purchase>> importMap = GSONConverter.fromJson(response);
		
		if (importMap.size() <= 0 && PreferenceManager.getDefaultSharedPreferences(PurchasesApp.getContext()).getBoolean("notif", false)) {
			Toast.makeText(PurchasesApp.getContext(), "Nothing imported", Toast.LENGTH_SHORT).show();
			return false;
		}

		for (Map.Entry<String, List<Purchase>> entry : importMap.entrySet())
		{
		    ItemListORM il = DatabaseManager.getInstance().getItemList(entry.getKey());
		    
		    if (il == null) {
		    	il = new ItemListORM();
		    	il.setName(entry.getKey());
		    	DatabaseManager.getInstance().addItemList(il);
		    }
		    
		    for (Purchase p : entry.getValue()) {
		    	ListItemORM li = DatabaseManager.getInstance().getListItem(p.getName());
		    	
		    	if (li != null) {
		    		continue;
		    	}
		    	
		    	li = new ListItemORM();
		    	li.setName(p.getName());
		    	li.setQuantity(p.getQuantity());
		    	li.setList(il.getId());
		    	DatabaseManager.getInstance().addListItem(li);
		    }
		}
		
		return true;
	}
	
	
}
