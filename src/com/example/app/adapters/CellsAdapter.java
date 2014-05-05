package com.example.app.adapters;

import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CursorAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;


import com.example.app.R;
import com.example.app.dbhelpers.GameOfLifeOpenHelper;
import com.example.app.views.Cell;


/**
 * The Class CellsAdapter is creating adapter for game board
 * 
 * @author Oleksii Khorunzhyi
 */
public class CellsAdapter  extends CursorAdapter { 

    private LayoutInflater mLayoutInflater;

    /**
     * Instantiates a new cells adapter.
     *
     * @param context the context for create adapter
     * @param cursor the cursor to c
     * @param flags the flags
     */
    public CellsAdapter(Context context, Cursor cursor,int flags) {
        super(context, cursor, flags);
        mLayoutInflater = LayoutInflater.from(context);
    }

	/**
	 * @see android.widget.CursorAdapter#newView(android.content.Context, android.database.Cursor, android.view.ViewGroup)
	 */
	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		View mView = mLayoutInflater.inflate(R.layout.cell, parent, false);
		mView.setLayoutParams(new GridView.LayoutParams(Cell.getSIDE(), Cell.getSIDE()));
		return mView;
	}

	/**
	 * @see android.widget.CursorAdapter#bindView(android.view.View, android.content.Context, android.database.Cursor)
	 */
	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		Cell cell = (Cell)view.findViewById(R.id.cell);
		cell.setCoordX(cursor.getInt(cursor.getColumnIndex(GameOfLifeOpenHelper.X)));
		cell.setCoordY(cursor.getInt(cursor.getColumnIndex(GameOfLifeOpenHelper.Y)));
        int state = cursor.getInt(cursor.getColumnIndex(GameOfLifeOpenHelper.STATE));
		if(state==1){
        	cell.setState(Cell.STATE_LIFE);
        	}else cell.setState(Cell.STATE_DEAD);
    }
}
    




