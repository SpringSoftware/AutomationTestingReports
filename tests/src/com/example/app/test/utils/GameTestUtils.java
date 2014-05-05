package com.example.app.test.utils;

import android.util.Log;


/**
 * The Utils
 * 
 * @author Oleksii Khorunzhyi
 */
public class GameTestUtils {

	/** The constants */
	private static final String LOG_TAG = "GameTest";
	private static final boolean DEBUG = true;
	
	/**
	 * Write message to LogCat
	 *
	 * @param text the message
	 */
	public static void writeLogCat(final String text) {
	    if (DEBUG)
	       Log.i(LOG_TAG, text);
	    }

}
