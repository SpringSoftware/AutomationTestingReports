package com.example.app.test.utils;

import android.app.Activity;
import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

/**
 * The TestCase for testing GUI
 * 
 * @author Oleksii Khorunzhyi
 */
public class GameTestCase<T extends Activity> extends ActivityInstrumentationTestCase2<T> {


	protected Solo mSolo;
	protected Context mContext;
	
	/**
	 * Instantiates a new test case.
	 *
	 * @param activityClass the activity class
	 */
	public GameTestCase(Class<T> activityClass) {
		super(activityClass);
	}

	
	/**
	 * @see android.test.ActivityInstrumentationTestCase2#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		mSolo = new Solo(getInstrumentation(), getActivity());
		mContext = getActivity().getApplicationContext();
	}
	
	/**
	 * @see android.test.ActivityInstrumentationTestCase2#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		mSolo.finishOpenedActivities();
		try {
			mSolo.finalize();
		} catch (Throwable e) {
			GameTestUtils.writeLogCat(e.getMessage());
		}
	}
}
