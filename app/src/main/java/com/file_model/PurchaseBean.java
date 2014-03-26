package com.file_model;

import java.io.Serializable;

public class PurchaseBean implements Serializable {
    private String name;
    private String quantity;
    private static final long serialVersionUID = 1;
 
    public PurchaseBean() {
    }
 
    public String getName() {
        return (this.name);
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getQuantity() {
        return (this.quantity);
    }
    
    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }    
    
    public String toString() {
    	return name + " - " + this.quantity;
    }
 
}
