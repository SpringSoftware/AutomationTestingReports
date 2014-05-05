package com.example.app.test.utils;

import android.widget.GridView;

import com.robotium.solo.Condition;

/**
 * The class for waiting of grid is binding
 * 
 * @author Oleksii Khorunzhyi
 */
public class BindGridCondition implements Condition{
	
	/** The constants and variables */
	public static final int TIMEOUT_FOR_BIND = 500;
	private GridView mGridView;
	

	/**
	 * Instantiates a new condition
	 *
	 * @param gameGrid the game grid
	 */
	public BindGridCondition(GridView gameGrid){
		mGridView = gameGrid;
	}
	
	
	/**
	 * @see com.robotium.solo.Condition#isSatisfied()
	 */
	@Override
	public boolean isSatisfied() {
		if(mGridView.getAdapter().getCount() == 500){
			return true;
		}
		return false;
	}
	
}