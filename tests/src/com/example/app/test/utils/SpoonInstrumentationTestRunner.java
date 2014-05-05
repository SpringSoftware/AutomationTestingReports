package com.example.app.test.utils;

import android.app.Application;
import android.app.KeyguardManager;
import android.os.PowerManager;
import android.test.InstrumentationTestRunner;

import static android.content.Context.KEYGUARD_SERVICE;
import static android.content.Context.POWER_SERVICE;
import static android.os.PowerManager.ACQUIRE_CAUSES_WAKEUP;
import static android.os.PowerManager.FULL_WAKE_LOCK;
import static android.os.PowerManager.ON_AFTER_RELEASE;



/**
 * The Class SpoonInstrumentationTestRunner.
 */
public class SpoonInstrumentationTestRunner extends InstrumentationTestRunner {
	  
  	/**
  	 * @see android.test.InstrumentationTestRunner#onStart()
  	 */
  	@Override public void onStart() {
	    runOnMainSync(new Runnable() {
	      @Override public void run() {
	        Application app = (Application) getTargetContext().getApplicationContext();
	        String simpleName = SpoonInstrumentationTestRunner.class.getSimpleName();
	    
	        // Unlock the device so that the tests can input keystrokes.
	        ((KeyguardManager) app.getSystemService(KEYGUARD_SERVICE)) //
	            .newKeyguardLock(simpleName) //
	            .disableKeyguard();
	        // Wake up the screen.
	        ((PowerManager) app.getSystemService(POWER_SERVICE)) //
	            .newWakeLock(FULL_WAKE_LOCK | ACQUIRE_CAUSES_WAKEUP | ON_AFTER_RELEASE, simpleName) //
	            .acquire();
	      }
	    });

	    super.onStart();
	  }
	}