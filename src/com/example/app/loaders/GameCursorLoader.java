package com.example.app.loaders;

import com.example.app.dbhelpers.GameOfLifeOpenHelper;
import com.example.application.GameApplication;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;

/**
 * The GameCursorLoader
 * 
 * @author Oleksii Khorunzhyi
 */
public class GameCursorLoader extends CursorLoader{

	private GameOfLifeOpenHelper mGameOfLifeOpenHelper;
	public static final int LOADER_ID = 0;
	public static final int MODE_LOAD_TABLE = 0;
	public static final int MODE_NEXT_GENERATION = 1;
	private int mMode = MODE_LOAD_TABLE;
	
	/**
	 * Instantiates a new game cursor loader.
	 *
	 * @param context the context
	 */
	public GameCursorLoader(Context context) {
		super(context);
		mGameOfLifeOpenHelper = GameApplication.getDataBase();
	}
	
	/**
	 * Sets the mode to load in background
	 *
	 * @param state the new mode to loading
	 */
	public void setMode(int state){
		mMode = state;
	}
	
	
	/**
	 * @see android.content.CursorLoader#loadInBackground()
	 */
	@Override
	public Cursor loadInBackground() {
		Cursor cursor = null;
		switch (mMode) {
		case MODE_LOAD_TABLE:
			cursor = mGameOfLifeOpenHelper.getTable();
			break;
		case MODE_NEXT_GENERATION:
			cursor = mGameOfLifeOpenHelper.nextGeneration();
			break;
		default:
			break;
		}
		return cursor;
	}

	
}
