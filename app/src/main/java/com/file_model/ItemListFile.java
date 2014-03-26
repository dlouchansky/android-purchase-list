package com.file_model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import android.util.Log;

public class ItemListFile {
	
	public static boolean Add(String listName, File cacheDir) {
		listName = listName.trim();
		try {
			
			File newFile = new File(cacheDir, listName);
			BufferedWriter writer = new BufferedWriter(new FileWriter(newFile));
			writer.close();
			return true;
		} catch (IOException e) {
			
			Log.e("Error", e.getLocalizedMessage());
			return false;
		}
	}
	
	public static boolean Delete(String listName, File cacheDir) {
		listName = listName.trim();
		final File deleteFile = new File(cacheDir, listName);
		deleteFile.delete();
		return true;
	}
	
	public static boolean Exists(String listName, File cacheDir) {
		listName = listName.trim();
		return (new File(cacheDir, listName)).exists();
	}	
	
	public static File[] GetLists(File cacheDir) {
		return cacheDir.listFiles();
	}
	
	public static String GetFirstListName(File cacheDir) {
		File[] lists = GetLists(cacheDir);
		
		if (lists.length > 0)
			return lists[0].getName();
		else 
			return "";
	}
	
}
