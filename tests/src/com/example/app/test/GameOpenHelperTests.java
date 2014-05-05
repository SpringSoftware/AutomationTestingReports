package com.example.app.test;

import java.security.spec.MGF1ParameterSpec;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;
import android.test.suitebuilder.annotation.SmallTest;

import com.example.activity.GameActivity;
import com.example.app.dbhelpers.GameOfLifeOpenHelper;
import com.example.app.utils.GameController;
import com.example.app.utils.GameOfLifeException;
import com.example.app.views.Cell;

/**
 * The Class GameOpenHelperTests
 * 
 * @author Oleksii Khorunzhyi
 */
public class GameOpenHelperTests extends AndroidTestCase {

	private RenamingDelegatingContext mContext;
	private GameOfLifeOpenHelper mUnderTestOpenHelper;
	private Cursor mCursor;
	private List<ContentValues> cells = new ArrayList<ContentValues>();
	private static final int SIZE_BOARD = 9;
	
	/**
	 * Instantiates a new game open helper tests.
	 */
	public GameOpenHelperTests() {
		super();
	}

	/**
	 * Sets the up.
	 * 
	 * @throws Exception
	 *             the exception
	 * @see android.test.AndroidTestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		mContext = new RenamingDelegatingContext(getContext(), "tests");
		mUnderTestOpenHelper = GameOfLifeOpenHelper.getInstance(mContext);
	}

	
	public void testDatabaseNotNull() {
		assertNotNull("database is null", mUnderTestOpenHelper);
	}

	
	
	/**
	 * Create the test table.
	 */
	private void initTable() {
		int y = 0;
		int x = 0;
		mUnderTestOpenHelper.clearTable();
		for (int indexTable = 0; indexTable < SIZE_BOARD; indexTable++, x++) {
			if (x == 3) {
				x = 0;
				y++;
			}
			ContentValues cv = new ContentValues();
			cv.put(GameOfLifeOpenHelper.X, x);
			cv.put(GameOfLifeOpenHelper.Y, y);
			cv.put(GameOfLifeOpenHelper.STATE, Cell.STATE_DEAD);
			cells.add(indexTable, cv);
		}
		mUnderTestOpenHelper.insertCells(cells);
	}

	/**
	 * Tear down.
	 * 
	 * @throws Exception the exception
	 * @see android.test.AndroidTestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
		mUnderTestOpenHelper.clearTable();
	}

	/**
	 * Test get all table.
	 */
	@SmallTest
	public void testGetAllTable() {
		initTable();
		int x = 0;
		int position = 1;
		mCursor = mUnderTestOpenHelper.getTable();
		assertEquals("Table size is incorrect", SIZE_BOARD, mCursor.getCount());
		if (mCursor.moveToPosition(position)) {
			x = mCursor.getInt(mCursor.getColumnIndex(GameOfLifeOpenHelper.X));
		}
		assertEquals("Table values is incorrect", position, x);
	}

	/**
	 * Testing update coordinates with correct value.
	 * 
	 * @throws GameOfLifeException
	 *             the game of life exception
	 */
	@SmallTest
	public void testUpdateCell() throws GameOfLifeException {
		initTable();
		int x = 2;
		int y = 0;
		int state = Cell.STATE_LIFE;
		int result = 0;
		mUnderTestOpenHelper.updateCell(x, y, state);
		mCursor = mUnderTestOpenHelper.getTable();
		if (mCursor.moveToPosition(x)) {
			result = mCursor.getInt(mCursor
					.getColumnIndex(GameOfLifeOpenHelper.STATE));
		}
		assertEquals("Table size is incorrect", state, result);
	}

	/**
	 * The test is testing update coordinates with incorrect value.
	 */
	@SmallTest
	public void testIncorrectValue() {
		initTable();
		try {
			mUnderTestOpenHelper.updateCell(900, -200, 5);
			fail("Should be an exception");
		} catch (GameOfLifeException e) {
		}
	}

	/**
	 * Test generate neighbors.
	 * 
	 * @throws GameOfLifeException
	 *             the game of life exception
	 */
	@SmallTest
	public void testGenerateNeighbours() throws GameOfLifeException {
		initTable();
		int x = 0;
		int y = 0;
		mUnderTestOpenHelper.updateCell(x, y, Cell.STATE_LIFE);
		mUnderTestOpenHelper.updateNeighbours();
		for (int tests = 0; tests < cells.size(); tests++, x++) {
			if (x == 3) {
				x = 0;
				y++;
			}
			int result = getNeighbours(x, y);
			if ((x == 1 && y == 0) || (x == 0 && y == 1) || (x == 1 && y == 1)) {
				assertEquals("Count neighbours where x = " + x + " y = " + y + "  is incorrect", 1, result);
			} else
				assertEquals("Count neighbours where x = " + x + " y = " + y + "  is incorrect", 0, result);
		}
		
	}
	
	/**
	 * Test generate neighbors.
	 * 
	 * @throws GameOfLifeException
	 *             the game of life exception
	 */
	@SmallTest
	public void testGenerateNeighboursDifferentCoord() throws GameOfLifeException {
		initTable();
		int x = 1;
		int y = 1;
		mUnderTestOpenHelper.updateCell(x, y, Cell.STATE_LIFE);
		x = 0; y = 0;
		mUnderTestOpenHelper.updateNeighbours();
		for (int tests = 0; tests < cells.size(); tests++, x++) {
			if (x == 3) {
				x = 0;
				y++;
			}
			int result = getNeighbours(x, y);
			if ((x == 1 && y == 1)){
				assertEquals("Count neighbours where x = " + x + " y = " + y + "  is incorrect", 0, result);}
			else
				assertEquals("Count neighbours where x = " + x + " y = " + y + "  is incorrect", 1 , result);
		}
		
	}
	
	/**
	 * Test create generation. This test set state in coordinate 
	 * x = 1, y = 1. After create generation all cells must be with
	 * state 0.
	 *
	 * @throws GameOfLifeException the game of life exception
	 */
	@SmallTest
	public void testCreateGeneration() throws GameOfLifeException {
		initTable();
		int x = 1;
		int y = 1;
		mUnderTestOpenHelper.updateCell(x, y, Cell.STATE_LIFE);
		x = 0; y = 0;
		mUnderTestOpenHelper.nextGeneration();
		for (int tests = 0; tests < cells.size(); tests++, x++) {
			if (x == 3) {
				x = 0;
				y++;
			}
			int state = getState(x, y);
			assertEquals("State in cell where x = " + x + " y = " + y + "  is incorrect", Cell.STATE_DEAD, state);
		}
		
	}
	
	/**
	 * Test create generation. Testing rule: dead cell with exactly three live neighbors
	 * becomes a live cell, as if by reproduction. This test set state in coordinate 
	 * all row x where y = 1. After create generation cells in
	 * row x = 1 must "alive" 
	 *
	 * @throws GameOfLifeException the game of life exception
	 */
	@SmallTest
	public void testGenerationFirstRule() throws GameOfLifeException {
		initTable();
		int x = 0;
		int y = 1;
		mUnderTestOpenHelper.updateCell(x, y, Cell.STATE_LIFE);
		mUnderTestOpenHelper.updateCell(x + 1 , y, Cell.STATE_LIFE);
		mUnderTestOpenHelper.updateCell(x + 2, y, Cell.STATE_LIFE);
		x = 0; y = 0;
		mUnderTestOpenHelper.nextGeneration();
		for (int tests = 0; tests < cells.size(); tests++, x++) {
			if (x == 3) {
				x = 0;
				y++;
			}
			int result = getState(x, y);
			if ((x == 1 && y == 0) || (x == 1 && y == 1) || (x == 1 && y == 2) || (x == 1 && y == 1)) {
				assertEquals("State where x = " + x + " y = " + y + "  is incorrect", Cell.STATE_LIFE, result);
			} else
				assertEquals("State where x = " + x + " y = " + y + "  is incorrect", Cell.STATE_DEAD, result);
		}
		
	}
	
	/**
	 * Test create generation. Testing rule: Any live cell with two or three live 
	 * neighbours lives on to the next generation. 
	 * After create generation cells should not change state
	 *
	 * @throws GameOfLifeException the game of life exception
	 */
	@SmallTest
	public void testGenerationSecondRule() throws GameOfLifeException {
		initTable();
		int x = 0;
		int y = 0;
		mUnderTestOpenHelper.updateCell(x, y, Cell.STATE_LIFE);
		mUnderTestOpenHelper.updateCell(x + 1 , y, Cell.STATE_LIFE);
		mUnderTestOpenHelper.updateCell(x, y + 1, Cell.STATE_LIFE);
		mUnderTestOpenHelper.updateCell(x +1  , y + 1, Cell.STATE_LIFE);
		x = 0; y = 0;
		mUnderTestOpenHelper.nextGeneration();
		for (int tests = 0; tests < cells.size(); tests++, x++) {
			if (x == 3) {
				x = 0;
				y++;
			}
			int result = getState(x, y);
			if ((x == 0 && y == 0) || (x == 1 && y == 0) || (x == 1 && y == 1) || (x == 0 && y == 1)) {
				assertEquals("State where x = " + x + " y = " + y + "  is incorrect", Cell.STATE_LIFE, result);
			} else
				assertEquals("State where x = " + x + " y = " + y + "  is incorrect", Cell.STATE_DEAD, result);
		}
		
	}
	
	/**
	 * Gets the neighbors.
	 * 
	 * @param x the x coordinate 
	 * @param y the y coordinate 
	 * @return the neighbors in current coordinate
	 */
	private int getNeighbours(int x, int y) {
		String whereClause = String.format("%s = ? and %s = ?",
				GameOfLifeOpenHelper.X, GameOfLifeOpenHelper.Y);
		SQLiteDatabase db = mUnderTestOpenHelper.getWritableDatabase();
		Cursor mCursor = db.query(GameOfLifeOpenHelper.TABLE_LIFE_NAME,
				new String[] { GameOfLifeOpenHelper.NEIGHBOURS }, 
				whereClause,
				new String[] { String.valueOf(x), String.valueOf(y) }, 
				null, null, null);
		int neighbours = 0;
		if (mCursor != null && mCursor.moveToFirst()) {
			neighbours = mCursor.getInt(mCursor
					.getColumnIndex(GameOfLifeOpenHelper.NEIGHBOURS));
		}
		return neighbours;
	}

	/**
	 * Gets the state coordinate
	 * 
	 * @param x the x coordinate 
	 * @param y the y coordinate 
	 * @return the state in current coordinate
	 */
	private int getState(int x, int y) {
		String whereClause = String.format("%s = ? and %s = ?",
				GameOfLifeOpenHelper.X, GameOfLifeOpenHelper.Y);
		SQLiteDatabase db = mUnderTestOpenHelper.getWritableDatabase();
		Cursor mCursor = db.query(GameOfLifeOpenHelper.TABLE_LIFE_NAME,
				new String[] { GameOfLifeOpenHelper.STATE }, whereClause,
				new String[] { String.valueOf(x), String.valueOf(y) }, null,
				null, null);
		int neighbours = 0;
		if (mCursor != null && mCursor.moveToFirst()) {
			neighbours = mCursor.getInt(mCursor
					.getColumnIndex(GameOfLifeOpenHelper.STATE));
		}
		return neighbours;
	}

}
