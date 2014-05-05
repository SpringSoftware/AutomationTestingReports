package com.example.app.utils;

import java.util.ArrayList;
import java.util.List;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentValues;
import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.example.app.adapters.CellsAdapter;
import com.example.app.dbhelpers.GameOfLifeOpenHelper;
import com.example.app.loaders.GameCursorLoader;
import com.example.app.services.GameThread;
import com.example.app.views.Cell;
import com.example.application.GameApplication;


/**
 * The GameController
 * 
 * @author Oleksii Khorunzhyi
 */
public class GameController implements LoaderCallbacks<Cursor>,
		OnItemClickListener {


	public static final int STATE_RUNING = 1;
	public static final int STATE_STOPING = 0;
	public static final int PADDING = 1;

	private static int sNumberCellsX = 22;
	private static int sNumberCellsY = 30;
	
	private GameOfLifeOpenHelper mGameOfLifeOpenHelper;
	private GridView mGameView;
	private CellsAdapter mCellAdapter;
	private GameCursorLoader mLoaderCursor;
	private Context mContext;
	private GameThread mGameProcess;

	private int state = STATE_STOPING;

	/**
	 * Instantiates a new game controller.
	 *
	 * @param context the context
	 * @param gameView the game view
	 * @param loaderManager the loader manager
	 */
	public GameController(Context context, GridView gameView,
			LoaderManager loaderManager) {
		mContext = context;
		mGameView = gameView;
		mCellAdapter = new CellsAdapter(mContext, null, 0);
		mGameOfLifeOpenHelper = GameApplication.getDataBase();
		mGameProcess = new GameThread(this);
		loaderManager.initLoader(GameCursorLoader.LOADER_ID, null, this);
		initGame();

	}

	/**
	 * Initialization the game.
	 */
	private void initGame() {
		mGameView.setAdapter(mCellAdapter);
		mGameView.setOnItemClickListener(this);
		adjustGameboard();
		createBoard();
	}

	/**
	 * Adjusting game board.
	 */
	private void adjustGameboard() {
		WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = size.x;
		int numberCells = (width/(Cell.getSIDE() + PADDING));
		setNumberCellsX(numberCells);
		int offset = (width - (Cell.getSIDE() + PADDING) * numberCells)/2;
		mGameView.setNumColumns(sNumberCellsX);
		mGameView.setPadding(offset, offset, 0, 0);
		
	}

	/**
	 * Creates the board.
	 */
	private void createBoard() {
		List<ContentValues> board = new ArrayList<ContentValues>();
		mGameOfLifeOpenHelper.clearTable();
		int y = 0;
		int x = 0;
		for (int indexTable = 0; indexTable < sNumberCellsX * sNumberCellsY; indexTable++, x++) {
			if (x == sNumberCellsX) {
				x = 0;
				y++;
			}
			ContentValues cv = new ContentValues();
			cv.put(GameOfLifeOpenHelper.X, x);
			cv.put(GameOfLifeOpenHelper.Y, y);
			cv.put(GameOfLifeOpenHelper.STATE, Cell.STATE_DEAD);
			board.add(indexTable, cv);
		}
		mGameOfLifeOpenHelper.insertCells(board);
	}

	/**
	 * Updating state one cells in position.
	 *
	 * @param cursor the cursor with all value
	 * @param position the position where update state
	 */
	private void updateState(Cursor cursor, int position) {
		if(state == STATE_RUNING)
		{
			return;
		}
		if (cursor.moveToPosition(position)) {
			int x = cursor.getInt(cursor
					.getColumnIndex(GameOfLifeOpenHelper.X));
			int y = cursor.getInt(cursor
					.getColumnIndex(GameOfLifeOpenHelper.Y));
			int state = cursor.getInt(cursor
					.getColumnIndex(GameOfLifeOpenHelper.STATE));
			if (state == Cell.STATE_LIFE) {
				try {
					mGameOfLifeOpenHelper.updateCell(x, y, Cell.STATE_DEAD);
				} catch (GameOfLifeException e) {
					e.printStackTrace();
				}
			} else
				try {
					mGameOfLifeOpenHelper.updateCell(x, y, Cell.STATE_LIFE);
				} catch (GameOfLifeException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				
			}
			mLoaderCursor.forceLoad();
		}
	}

	/**
	 * @see android.app.LoaderManager.LoaderCallbacks#onCreateLoader(int, android.os.Bundle)
	 */
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		mLoaderCursor = new GameCursorLoader(mContext);
		return mLoaderCursor;
	}
	
	/**
	 * @see android.app.LoaderManager.LoaderCallbacks#onLoadFinished(android.content.Loader, java.lang.Object)
	 */
	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		mCellAdapter.swapCursor(data);
		mLoaderCursor.setMode(GameCursorLoader.MODE_LOAD_TABLE);
	}
	/**
	 * @see android.app.LoaderManager.LoaderCallbacks#onLoaderReset(android.content.Loader)
	 */
	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		mCellAdapter.swapCursor(null);
	}

	/**
	 * @see android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget.AdapterView, android.view.View, int, long)
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Cursor cursor = mCellAdapter.getCursor();
		updateState(cursor, position);
	}

	/**
	 * Next generation.
	 */
	public void nextGeneration() {
		mLoaderCursor.setMode(GameCursorLoader.MODE_NEXT_GENERATION);
		mLoaderCursor.forceLoad();
	}

	/**
	 * Start or stop game process.
	 */
	public void startOrStopGame() {
		if (state == STATE_STOPING) {
			startGame();
		} else
			stopGame();
	}

	/**
	 * Stop game.
	 */
	public void stopGame() {
		state = STATE_STOPING;
		mGameProcess.stop();
	}

	/**
	 * Start game.
	 */
	public void startGame() {
		mGameProcess.start();
		state = STATE_RUNING;
	}

	/**
	 * The method is stopping game process
	 */
	public void close() {
		mGameProcess.stop();
	}

	/**
	 * The method gets the number of cells in length.
	 *
	 * @return the number of cells in length
	 */
	public static int getNumberCellsY() {
		return sNumberCellsY;
	}

	/**
	 * The method sets number of cells in length.
	 *
	 * @param sNumberCellsY the new number of cells in length
	 */
	public static void setNumberCellsY(int sCellsY) {
		sNumberCellsY = sCellsY;
	}

	/**
	 * The method gets the number of cells in height.
	 *
	 * @return the number of cells in height
	 */
	public static int getNumberCellsX() {
		return sNumberCellsX;
	}

	/**
	 * The method sets number of cells in height.
	 *
	 * @param sNumberCellsX the new number of cells in height
	 */
	public static void setNumberCellsX(int sCellsX) {
		sNumberCellsX = sCellsX;
	}
	
	public int getState() {
		return state;
	}

}
