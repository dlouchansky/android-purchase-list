package com.persistence;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "listItems")
public class ListItemORM {
    @DatabaseField(generatedId = true)
    private int id;
    
    @DatabaseField
    private String name;

    @DatabaseField
    private String quantity;

    @DatabaseField
    private int list;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setList(int list) {
        this.list = list;
    }

    public int getList() {
        return list;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getQuantity() {
        return quantity;
    }
    
    public ListItemORM() {
    	
    }
}