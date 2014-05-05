package com.example.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.SeekBar;
import android.widget.ToggleButton;

import com.example.app.R;
import com.example.app.utils.GameController;


/**
 * 
 * The GameActivity.
 * 
 * @author Oleksii Khorunzhyi
 */
public class GameActivity extends Activity implements OnClickListener {

	/** The constants and variables*/
	private GridView mGameBoard;
	protected GameController mGameController;
	private LayoutInflater mLayoutInflater;
	private Button mNextStepBtn;
	private ToggleButton mStartBtn;

	/**
	 * On create.
	 *
	 * @param savedInstanceState the saved instance state
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().clearFlags(
				WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		setContentView(R.layout.activity_base);
		initActionBar();
		initBoard();
	}

	/**
	 * Initialization the action bar.
	 */
	private void initActionBar() {
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.action_bar_view, null);
		actionBar.setCustomView(view, new ActionBar.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT));
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		mNextStepBtn = (Button) view.findViewById(R.id.btnNextGeneration);
		mStartBtn = (ToggleButton) view.findViewById(R.id.btnStart);
		mNextStepBtn.setOnClickListener(this);
		mStartBtn.setOnClickListener(this);

	}

	/**
	 * Initialization the board.
	 */
	private void initBoard() {
		mGameBoard = (GridView) findViewById(R.id.grid_view_cells);
		mGameController = new GameController(this, mGameBoard,
				getLoaderManager());
	}

	/**
	 * The method create next generation and loading to display new data.
	 *
	 * @param item the item
	 * @return true, if successful
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.next_step:
			mGameController.nextGeneration();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * The method is switching between automatic and stepwise mode.
	 *
	 * @param v the v
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnNextGeneration:
			mGameController.nextGeneration();
			break;
		case R.id.btnStart:
			mGameController.startOrStopGame();
			if (mGameController.getState() == GameController.STATE_RUNING) {
				mNextStepBtn.setVisibility(View.GONE);
			} else
				mNextStepBtn.setVisibility(View.VISIBLE);
			break;
		default:
			break;
		}

	}

	
	/**
	 * @see android.app.Activity#onPause()
	 */
	@Override
	protected void onPause() {
		super.onPause();
		mGameController.close();
	}

}
