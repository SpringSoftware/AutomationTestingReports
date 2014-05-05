package com.example.app.test;

import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.SmallTest;
import android.view.View;

import com.example.app.adapters.CellsAdapter;
import com.example.app.views.Cell;

/**
 * The Test is testing CellsAdapter
 * 
 * @author Oleksii Khorunzhyi
 */
public class CellsAdapterTest extends AndroidTestCase {

	CellsAdapter mUnderTestAdapter;

	/**
	 * @see android.test.AndroidTestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		mUnderTestAdapter = new CellsAdapter(getContext(), null, 0);
	}

	/**
	 * Test new view
	 */
	@SmallTest
	 public void testNewView() {
	        View result = mUnderTestAdapter.newView(mContext, null, null);
	        assertNotNull(result);
	        assertEquals(com.example.app.R.id.cell, result.getId());
	        assertEquals("Side cell is incorrect", Cell.getSIDE(), result.getLayoutParams().width);
	    }
	/**
	 * @see android.test.AndroidTestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

}
