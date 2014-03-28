package com.integration;

import java.io.Serializable;

public class Purchase implements Serializable {
    private String name;
    private String quantity;
    private static final long serialVersionUID = 1;
 
    public Purchase() {
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
