package com.file_model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;



import android.util.Log;

public class ListItemFile {
	public static boolean Add(String item, File cacheDir, String chosenList) {
		try {
			File newFile = new File(cacheDir, chosenList);
			BufferedWriter writer = new BufferedWriter(new FileWriter(newFile, true));
			writer.write(item);
			writer.newLine();
			writer.close();
		} catch (IOException e) {
			Log.e("Error", e.getLocalizedMessage());
		}	
		return true;
	}
	
	public static boolean Delete(String itemName, File cacheDir, String chosenList) {
		File inputFile = new File(cacheDir, chosenList);
		File tempFile = new File(cacheDir, chosenList + ".tmp"); // maybe save in some directory
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(inputFile));
			BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
	
			String currentLine;
			
			while((currentLine = reader.readLine()) != null) {
			    String trimmedLine = currentLine.trim();
			    if(trimmedLine.equals(itemName))
			    	continue;
			    
			    writer.write(currentLine);
			}
	
			tempFile.renameTo(inputFile);
			
			writer.close();
			reader.close();
			return true;
				
		} catch (IOException e) {
			Log.e("Error", e.getLocalizedMessage());
			return false;
		}	
	}
	
	public static ArrayList<PurchaseBean> GetAllFromList(File cacheDir, String chosenList) {
		ArrayList<PurchaseBean> pbs = new ArrayList<PurchaseBean>();
		
		try {
			File newFile = new File(cacheDir, chosenList);
			BufferedReader reader = new BufferedReader(new FileReader(newFile));
			
			String line;
			while((line = reader.readLine()) != null) {
				PurchaseBean bean = new PurchaseBean();
				String[] tmp = line.split(" - ");
				bean.setName(tmp[0]);
				if (tmp.length > 1)
					bean.setQuantity(tmp[1]);
				
				pbs.add(bean);
			}
			
			reader.close();
		} catch (IOException e) {
			Log.e("Error", e.getLocalizedMessage());
		}
		return pbs;
	}
}
