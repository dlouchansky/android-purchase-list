package com.persistence;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    private static final String DATABASE_NAME = "PurchaseList.sqlite";

    // any time you make changes to your database objects, you may have to increase the database version
    private static final int DATABASE_VERSION = 2;

    private Dao<ItemListORM, Integer> itemListDao = null;
    private Dao<ListItemORM, Integer> listItemDao = null;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database,ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, ItemListORM.class);
            TableUtils.createTable(connectionSource, ListItemORM.class);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
            throw new RuntimeException(e);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            List<String> allSql = new ArrayList<String>(); 
            switch(oldVersion) 
            {
              case 1: 
                  //allSql.add("alter table AdData add column `new_col` VARCHAR");
                  //allSql.add("alter table AdData add column `new_col2` VARCHAR");
            }
            for (String sql : allSql) {
                db.execSQL(sql);
            }
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "exception during onUpgrade", e);
            throw new RuntimeException(e);
        }
        
    }

    public Dao<ItemListORM, Integer> getItemListDao() {
        if (null == itemListDao) {
            try {
                itemListDao = getDao(ItemListORM.class);
            }catch (java.sql.SQLException e) {
                e.printStackTrace();
            }
        }
        return itemListDao;
    }

    public Dao<ListItemORM, Integer> getListItemDao() {
        if (null == listItemDao) {
            try {
                listItemDao = getDao(ListItemORM.class);
            }catch (java.sql.SQLException e) {
                e.printStackTrace();
            }
        }
        return listItemDao;
    }

}