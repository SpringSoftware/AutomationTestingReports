package com.example.app.dbhelpers;

import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.app.utils.GameController;
import com.example.app.utils.GameOfLifeException;
import com.example.app.views.Cell;


/**
 * The SQLiteOpenHelper for working game database
 * 
 * @author Oleksii Khorunzhyi
 */
public class GameOfLifeOpenHelper extends SQLiteOpenHelper{

    public static final String TABLE_LIFE_NAME = "LIFE";
    public static final String _id = "_id";
    public static final String X = "X";
    public static final String Y = "Y";
    public static final String STATE = "STATE";
    public static final String NEIGHBOURS = "NEIGHBOURS";
    
    
	private static final String DATABASE_NAME = "LIFE_DATABASE";
    private static final int DATABASE_VERSION = 1;
    
    
    private static final String CREATE_TABLE_LIFE = String.format("CREATE TABLE %s( " +
    		"%s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s INTEGER, %s INTEGER, %s INTEGER, " +
            " UNIQUE ( %s , %s))", 
            TABLE_LIFE_NAME,
            _id, 
            X, 
            Y, 
            STATE, 
            NEIGHBOURS,
            X, Y);
 
    private static final String QUERY_TO_UPDATE_NEIGHBOURS = "UPDATE LIFE SET NEIGHBOURS = " +
    		"( SELECT COUNT(*) FROM LIFE AS L WHERE (L.X <= LIFE.X+1 AND L.X >= LIFE.X-1 ) " +
    		"AND (L.Y <= LIFE.Y + 1 AND L.Y >= LIFE.Y-1 ) AND((L.X != LIFE.X) OR (L.Y != LIFE.Y)) AND STATE = 1)";
    
	private static GameOfLifeOpenHelper mInstance;
	
	
	/**
	 * Instantiates a new game of life open helper.
	 *
	 * @param context the context for create 
	 */
	private GameOfLifeOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	/**
	 * Gets the single instance of GameOfLifeOpenHelper.
	 *
	 * @param context the context
	 * @return single instance of GameOfLifeOpenHelper
	 */
	public static GameOfLifeOpenHelper getInstance(Context context) {
		if (mInstance == null) {
			
			mInstance = new GameOfLifeOpenHelper(context.getApplicationContext());
			
		}
		return mInstance;
	}

	/**
	 * The method is creating database.
	 *
	 * @param db the database to create
	 * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_LIFE);
	}

	/**
	 * The method is upgrading database.
	 *
	 * @param db the database for update
	 * @param oldVersion the old version
	 * @param newVersion the new version
	 * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		 clearTable();
	}
	
	/**
	 * The method is inserting cells to database.
	 *
	 * @param cells the cells to inserting to database
	 */
	public void insertCells(List<ContentValues> cells){
		SQLiteDatabase db = this.getWritableDatabase();
		db.beginTransaction();
		for(int countCells=0; countCells < cells.size(); countCells++ )
		{
			db.insert(TABLE_LIFE_NAME, null, cells.get(countCells));
		}
		db.setTransactionSuccessful();
		db.endTransaction();
		db.close();
	}
	
	
	/**
	 * The method is reading game board.
	 *
	 * @return cursor with all values
	 */
	public Cursor getTable(){
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor mCursor = db.query(TABLE_LIFE_NAME, null, null, null, null, null, null, null);
		return mCursor;
	}

	/**
	 * The method is updating state one cells in position.
	 *
	 * @param x the x coordinate to update
	 * @param y the y coordinate to update
	 * @param state the new state to update
	 * @throws GameOfLifeException the game of life exception
	 */
	public void updateCell(int x, int y, int state) throws GameOfLifeException{
		SQLiteDatabase db = this.getWritableDatabase();
		if(x > GameController.getNumberCellsX() || x < 0 || y > GameController.getNumberCellsY()|| y < 0 || state > Cell.STATE_LIFE || state < 0)
		{
			throw new GameOfLifeException("Input values is invalid");
		}
		String whereClause = String.format("%s = ? AND %s = ?", X , Y);
		String [] arguments = new String[] { String.valueOf(x), String.valueOf(y)};
		ContentValues cv = new ContentValues();
		cv.put(X, x);
		cv.put(Y, y);
		cv.put(STATE, state);
		db.update(TABLE_LIFE_NAME, cv, whereClause, arguments);
		db.close();
	}
	
	/**
	 * Clear game board.
	 */
	public void clearTable(){
		SQLiteDatabase db = this.getWritableDatabase();
		String dropQueries = String.format("DROP TABLE IF EXISTS %s", TABLE_LIFE_NAME);
		db.execSQL(dropQueries);
		onCreate(db);
	}

	/**
	 * The method is generating next generation in database and return all table.
	 *
	 * @return the cursor with new values
	 */
	public Cursor nextGeneration(){
		updateNeighbours();
		createGeneration();
		return getTable();
	}
	
	/**
	 * Update neighbors columns in database
	 */
	public void updateNeighbours(){
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL(QUERY_TO_UPDATE_NEIGHBOURS);
		db.close();
	}
	
	/**
	 * Creates the generation.
	 */
	public void createGeneration(){
		SQLiteDatabase db = this.getWritableDatabase();
		String updateGen = String.format("UPDATE %s SET %s = (NOT(%s > 3 OR %s < 2 )) AND (%s = 3 OR %s)", 
				TABLE_LIFE_NAME, 
				STATE, NEIGHBOURS, NEIGHBOURS, NEIGHBOURS, 
				STATE);
		db.execSQL(updateGen);
		db.close();
	}

	
}


