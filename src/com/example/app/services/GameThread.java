package com.example.app.services;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.example.app.utils.GameController;


/**
 * The tread for automatic generation
 * 
 * @author Oleksii Khorunzhyi
 */
public class GameThread implements Runnable{

	private static final int SPEED_GAME = 1000; // speed game
	private GameController mGameController;
	private ScheduledExecutorService mService;
	private ScheduledFuture<?> mFuture;
	
	
	/**
	 * Instantiates a new game thread.
	 *
	 * @param gameController the game controller
	 */
	public GameThread(GameController gameController){
		mGameController = gameController;
	}
	
	
	/**
	 * Start thread
	 */
	public void start(){
		mService = Executors.newSingleThreadScheduledExecutor();
		mFuture = mService.scheduleWithFixedDelay(this, 0, SPEED_GAME, TimeUnit.MILLISECONDS);
	}
	
	/**
	 * Stop thread
	 */
	public void stop(){
		if(mFuture != null){
			mFuture = null;
			mService.shutdownNow();}

	}
	
	/**
	 * The method creates a new generation with a predetermined score
	 *
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		if(mGameController != null)
		mGameController.nextGeneration();
	}
}
