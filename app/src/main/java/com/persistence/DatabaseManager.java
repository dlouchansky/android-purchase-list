package com.persistence;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.presentation.PurchasesApp;

public class DatabaseManager {

    static private DatabaseManager instance;

    static public DatabaseManager getInstance() {
    	if (null == instance) {
            instance = new DatabaseManager(PurchasesApp.getContext());
        }
        return instance;
    }

    private DatabaseHelper helper;
    private DatabaseManager(Context ctx) {
        helper = new DatabaseHelper(ctx);
    }

    private DatabaseHelper getHelper() {
        return helper;
    }

    public ArrayList<ItemListORM> getAllItemLists() {
        List<ItemListORM> itemLists = null;
        
        try {
            itemLists = getHelper().getItemListDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return (ArrayList<ItemListORM>) itemLists;
    }
    
    public int getItemListCount() {
    	ArrayList<ItemListORM> ils = getAllItemLists();
    	return ils.size();
    }
    
    // check if name is unique
    public boolean checkItemListName(String name) {
    	List<ItemListORM> ils = null;
        try {
        	QueryBuilder<ItemListORM, Integer> queryBuilder = getHelper().getItemListDao().queryBuilder();
        	queryBuilder.where().eq("name", name);
        	PreparedQuery<ItemListORM> preparedQuery = queryBuilder.prepare();
            ils = getHelper().getItemListDao().query(preparedQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (ils.size() > 0)
        	return false;
        else
        	return true;
    }
    
    public ItemListORM getItemList(String name) {
    	List<ItemListORM> ils = null;
        try {
        	QueryBuilder<ItemListORM, Integer> queryBuilder = getHelper().getItemListDao().queryBuilder();
        	queryBuilder.where().eq("name", name);
        	PreparedQuery<ItemListORM> preparedQuery = queryBuilder.prepare();
            ils = getHelper().getItemListDao().query(preparedQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        if (ils.size() > 0)
        	return ils.get(0);
        else
        	return null;
    }
    
    public ListItemORM getListItem(String name) {
    	List<ListItemORM> listitems = null;
        try {
        	QueryBuilder<ListItemORM, Integer> queryBuilder = getHelper().getListItemDao().queryBuilder();
        	queryBuilder.where().eq("name", name);
        	PreparedQuery<ListItemORM> preparedQuery = queryBuilder.prepare();
            listitems = getHelper().getListItemDao().query(preparedQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        if (listitems.size() > 0)
        	return listitems.get(0);
        else
        	return null;
    }
    
    public ArrayList<ListItemORM> getAllListItems(int listId) {
    	List<ListItemORM> listitems = null;
        try {
        	QueryBuilder<ListItemORM, Integer> queryBuilder = getHelper().getListItemDao().queryBuilder();
        	queryBuilder.where().eq("list", listId);
        	PreparedQuery<ListItemORM> preparedQuery = queryBuilder.prepare();
            listitems = getHelper().getListItemDao().query(preparedQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return (ArrayList<ListItemORM>) listitems;
    }
    
    public ArrayList<ListItemORM> getAllListItems() {
        List<ListItemORM> listItems = null;
        
        try {
            listItems = getHelper().getListItemDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return (ArrayList<ListItemORM>) listItems;
    }
    
    public ItemListORM getItemList(int listId) {
    	ItemListORM li = null;
        try {
            li = getHelper().getItemListDao().queryForId(listId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return li;
    }
    
    public boolean addItemList(ItemListORM il) {
        try {
            getHelper().getItemListDao().create(il);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean addListItem(ListItemORM li) {
        try {
            getHelper().getListItemDao().create(li);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public void updateItemList(ItemListORM il) {
        try {
            getHelper().getItemListDao().update(il);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void updateListItem(ListItemORM li) {
        try {
            getHelper().getListItemDao().update(li);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public boolean deleteItemList(int il) {
    	try {
    		getHelper().getItemListDao().deleteById(il);
    		return true;
        } catch (SQLException e) {
            e.printStackTrace();
        } 	
    	return false;	
    }
    
    public boolean deleteListItem(int li) {
    	try {
    		getHelper().getListItemDao().deleteById(li);
    		return true;
        } catch (SQLException e) {
            e.printStackTrace();
        } 	
    	return false;	
    }
    
    
    public ItemListORM getFirstItemList() {
    	ArrayList<ItemListORM> il = getAllItemLists();
    	
    	return il.get(0);
    }
}