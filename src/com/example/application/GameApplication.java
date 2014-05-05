package com.example.application;

import com.example.app.dbhelpers.GameOfLifeOpenHelper;

import android.app.Application;


/**
 * The Class GameApplication
 * 
 * @author Oleksii Khorunzhyi
 */
public class GameApplication extends Application{

	private static GameOfLifeOpenHelper mGameOfLifeOpenHelper;
	
	/**
	 * @see android.app.Application#onCreate()
	 */
	@Override
	public void onCreate() {
		super.onCreate();
		mGameOfLifeOpenHelper = GameOfLifeOpenHelper.getInstance(getApplicationContext());
	}
	
	
	/**
	 * The method getting game database
	 * @return the game data base
	 */
	public static GameOfLifeOpenHelper getDataBase(){
		return mGameOfLifeOpenHelper;
	}
}
