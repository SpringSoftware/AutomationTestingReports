package com.example.app.ui.test;

import android.R;
import android.R.color;
import android.graphics.drawable.ColorDrawable;
import android.test.suitebuilder.annotation.MediumTest;
import android.widget.GridView;

import com.example.activity.GameActivity;
import com.example.app.test.utils.BindGridCondition;
import com.example.app.test.utils.GameTestCase;
import com.example.app.views.Cell;
import com.squareup.spoon.Spoon;


/**
 * The TestCase for testing GUI.
 * 
 * @author Oleksii Khorunzhyi
 */
public class GenerationTest extends GameTestCase<GameActivity>{

	/**  Constants and variables. */
	private GridView mGameView;
	private static final int POSITION_OF_CELL = 82;
	
	/**
	 * Instantiates a new generation test.
	 */
	public GenerationTest() {
		super(GameActivity.class);
	}

	/**
	 * Test change state.
	 */
	@MediumTest
	public void testChangeState(){
		getInstrumentation().waitForIdleSync();
		assertTrue(mSolo.waitForView(GridView.class));
		
		mGameView = (GridView)mSolo.getView(com.example.app.R.id.grid_view_cells);
		Spoon.screenshot(getActivity(), "Start_game");
		
		clickOnCell(POSITION_OF_CELL);
		mSolo.waitForCondition(new BindGridCondition(mGameView), BindGridCondition.TIMEOUT_FOR_BIND);
		Cell cell = (Cell)mGameView.getChildAt(POSITION_OF_CELL);
		
		assertTrue("State is incorrect", cell.getState()==Cell.STATE_LIFE);
		assertTrue(((ColorDrawable)cell.getBackground()).getColor() == mContext.getResources().getColor(color.holo_orange_dark));
		Spoon.screenshot(getActivity(), "Tap_to_cell");
	}
	
	
	/**
	 * Test next generation.
	 */
	@MediumTest
	public void testNextGeneration(){
		int[] cells = {200, 201, 202};
		int[] cellsGen = {186, 201, 216};
		getInstrumentation().waitForIdleSync();
		assertTrue(mSolo.waitForView(GridView.class));
		
		mGameView = (GridView)mSolo.getView(com.example.app.R.id.grid_view_cells);
		Spoon.screenshot(getActivity(), "Start_game");
		
		for(int index = 0; index < cells.length; index++){
			clickOnCell(cells[index]);
		}
		mSolo.waitForCondition(new BindGridCondition(mGameView), BindGridCondition.TIMEOUT_FOR_BIND);
		Spoon.screenshot(getActivity(), "Tap_to_cell");
		
		mSolo.clickOnButton(mContext.getString(com.example.app.R.string.next_gener));
		mSolo.waitForCondition(new BindGridCondition(mGameView), BindGridCondition.TIMEOUT_FOR_BIND);
		
		for(int index = 0; index < cellsGen.length; index++){
			Cell cell = (Cell)mGameView.getChildAt((cellsGen[index]));
			assertTrue(((ColorDrawable)cell.getBackground()).getColor() == mContext.getResources().getColor(color.holo_orange_dark));
		}
		Spoon.screenshot(getActivity(), "State_after_next_generation");
	}
	
	
	/**
	 * Click on cell.
	 *
	 * @param position the position
	 */
	private void clickOnCell (int position){
		Cell cell = (Cell)mGameView.getChildAt(position);
		mSolo.clickOnView(cell);
	}

}
