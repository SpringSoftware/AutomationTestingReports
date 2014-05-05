package com.example.app.views;

import android.R;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * The Cell.
 * 
 * @author Oleksii Khorunzhyi
 */
public class Cell extends LinearLayout {

	public static int STATE_LIFE = 1;
	public static int STATE_DEAD = 0;
	private static final int SIDE = 30;
	private int cellState = STATE_DEAD;
	private int x = 0;
	private int y = 0;

	/**
	 * Instantiates a new cell.
	 *
	 * @param context the context
	 */
	public Cell(Context context) {
		super(context);
	}

	/**
	 * Instantiates a new cell.
	 *
	 * @param context the context
	 * @param attrs the the attributes
	 */
	public Cell(Context context, final AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * Instantiates a new cell.
	 *
	 * @param context the context
	 * @param attrs the attributes
	 * @param defStyle the default style
	 */
	public Cell(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	/**
	 * Gets the state cell.
	 *
	 * @return the state
	 */
	public int getState() {
		return cellState;
	}

	/**
	 * Sets the state cell
	 *
	 * @param state the new state
	 */
	public void setState(int state) {
		cellState = state;
		if (cellState == STATE_LIFE) {
			setBackgroundColor(getResources().getColor(R.color.holo_orange_dark));
		} else
			setBackgroundColor(getResources().getColor(R.color.darker_gray));
	}

	/**
	 * Sets the coordinate of x.
	 *
	 * @param x the new coordinate of x
	 */
	public void setCoordX(int x) {
		this.x = x;
	}
	
	/**
	 * Gets the coordinate of x.
	 *
	 * @return the coordinate of x
	 */
	public int getCoordX() {
		return x;
	}
	
	/**
	 * Gets the coordinate of y.
	 *
	 * @return the coordinate of y
	 */
	public int getCoordY() {
		return y;
	}
	
	/**
	 * Sets the coordinate of y.
	 *
	 * @param y the new coordinate of y
	 */
	public void setCoordY(int y) {
		this.y = y;
	}
	

	/**
	 * Revert state cell.
	 */
	public void revertState() {
		if(cellState == STATE_LIFE){
			cellState = STATE_DEAD;
		}else cellState = STATE_LIFE;
		setState(cellState);
	}

	/**
	 * Gets the side.
	 *
	 * @return the side
	 */
	public static int getSIDE() {
		return SIDE;
	}

}
