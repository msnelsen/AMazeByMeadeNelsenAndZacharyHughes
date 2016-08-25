package edu.wm.cs.cs301.amazebymeadenelsenandzacharyhughes.falstad;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import edu.wm.cs.cs301.amazebymeadenelsenandzacharyhughes.ui.FinishActivity;



public class Wizard extends AutoDriver implements RobotDriver {
	
	public boolean exitFlag = false; 
	 
	
	public Wizard(){
		super();
		
	}
	
	/**
	 * Returns a @return boolean. Cheats and utilizes the distance matrix in the system 
	 * to determine the overall optimal route.   
	 */
	@Override
	public boolean drive2Exit() throws Exception {
		
		try{
		   this.ottoVonRobot.setBatteryLevel(2500);
			
			int northDist, southDist, eastDist, westDist;
			northDist = Integer.MAX_VALUE; 
			southDist = Integer.MAX_VALUE;
			eastDist = Integer.MAX_VALUE;  
			westDist = Integer.MAX_VALUE; 
			int currentX = this.ottoVonRobot.getCurrentPosition()[0]; 
			int currentY = this.ottoVonRobot.getCurrentPosition()[1]; 
	
			while (this.ottoVonRobot.isAtGoal() == false && this.playActivity.shouldQuit == false){
				    

				
					if(this.playActivity.pausedFlag == true){
						continue;
					}
				
				currentX = this.ottoVonRobot.getCurrentPosition()[0]; 
				currentY = this.ottoVonRobot.getCurrentPosition()[1]; 
				
				if (this.ottoVonRobot.robotMaze.mazecells.hasWallOnTop(currentX, currentY) == false){
					northDist = this.ottoVonRobot.robotMaze.mazedists.getDistance(currentX,  currentY - 1); 
				}
				if (this.ottoVonRobot.robotMaze.mazecells.hasWallOnBottom(currentX, currentY) == false){
					southDist = this.ottoVonRobot.robotMaze.mazedists.getDistance(currentX,  currentY + 1); 
				}
				if (this.ottoVonRobot.robotMaze.mazecells.hasWallOnLeft(currentX, currentY) == false){
					westDist = this.ottoVonRobot.robotMaze.mazedists.getDistance(currentX - 1,  currentY); 
				}
				if (this.ottoVonRobot.robotMaze.mazecells.hasWallOnRight(currentX, currentY) == false){
					eastDist = this.ottoVonRobot.robotMaze.mazedists.getDistance(currentX + 1,  currentY); 
				}
				
				
				if (northDist == this.ottoVonRobot.robotMaze.mazedists.getDistance(currentX, currentY) - 1){
					this.ottoVonRobot.turnToNorth();
					this.ottoVonRobot.move(1);
					
				}
				else if (southDist == this.ottoVonRobot.robotMaze.mazedists.getDistance(currentX, currentY) - 1){
					this.ottoVonRobot.turnToSouth();
					this.ottoVonRobot.move(1);
				}
				else if (eastDist == this.ottoVonRobot.robotMaze.mazedists.getDistance(currentX, currentY) - 1){
					this.ottoVonRobot.turnToEast(); 
					this.ottoVonRobot.move(1);
					
				}
				else if (westDist == this.ottoVonRobot.robotMaze.mazedists.getDistance(currentX, currentY) - 1){
					this.ottoVonRobot.turnToWest(); 
					this.ottoVonRobot.move(1);
					
				}
				
				northDist = Integer.MAX_VALUE; 
				southDist = Integer.MAX_VALUE;
				eastDist = Integer.MAX_VALUE; 
				westDist = Integer.MAX_VALUE; 
				
				
				
				playActivity.mHandler.postDelayed(new Runnable() {
					public void run(){
						playActivity.maze.notifyViewerRedraw();
					}
				}, 75);
				

				this.playActivity.progressBar1.setProgress((int) this.playActivity.robot.getBatteryLevel()); 
				System.gc(); 

			}
			
//			System.out.println("Reaching final move"); 
//			this.ottoVonRobot.move(1);
//			System.out.print("The value of isAtExit is: "); 
//			exitFlag = !this.ottoVonRobot.hasNotLeftMaze(); 
////			System.out.println(this.ottoVonRobot.robotMaze.mazecells.isExitPosition(this.ottoVonRobot.getCurrentPosition()[0], this.ottoVonRobot.getCurrentPosition()[1]));
////			exitFlag = this.ottoVonRobot.robotMaze.mazecells.isExitPosition(this.ottoVonRobot.getCurrentPosition()[0], this.ottoVonRobot.getCurrentPosition()[1]);
//			
//			
//			if (exitFlag == false){
//				this.ottoVonRobot.rotate(Robot.Turn.LEFT);
//				this.ottoVonRobot.move(1);
//				exitFlag = !this.ottoVonRobot.hasNotLeftMaze(); 
//				if (exitFlag == false){
//				this.ottoVonRobot.rotate(Robot.Turn.RIGHT);
//				}
//				exitFlag = !this.ottoVonRobot.hasNotLeftMaze(); 
////				if (this.ottoVonRobot.robotMaze.mazecells.isExitPosition(this.ottoVonRobot.getCurrentPosition()[0], this.ottoVonRobot.getCurrentPosition()[1])){
////					exitFlag = true; 
////				}
//			}
//			
//			if (exitFlag == false){
//				this.ottoVonRobot.rotate(Robot.Turn.RIGHT);
//				this.ottoVonRobot.move(1);
//				exitFlag = !this.ottoVonRobot.hasNotLeftMaze(); 
//				if (exitFlag == false){
//				this.ottoVonRobot.rotate(Robot.Turn.LEFT);
//				}
//				exitFlag = !this.ottoVonRobot.hasNotLeftMaze(); 
//
////				if (this.ottoVonRobot.robotMaze.mazecells.isExitPosition(this.ottoVonRobot.getCurrentPosition()[0], this.ottoVonRobot.getCurrentPosition()[1])){
////					exitFlag = true; 
////				}
//			}
			
			playActivity.mHandler.post(new Runnable() {
				public void run(){
					//intent.putExtra(EXTRA_MESSAGE4, maze); 
					playActivity.uponEnding();
				}
			});

			



//		while (this.ottoVonRobot.robotMaze.isEndPosition(currentX, currentY) == false){
//			
//			currentX = this.ottoVonRobot.getCurrentPosition()[0]; 
//			currentY = this.ottoVonRobot.getCurrentPosition()[1]; 
//			
//		if (this.ottoVonRobot.robotMaze.mazecells.hasWallOnTop(currentX, currentY) == false){
//
//			this.ottoVonRobot.turnToNorth();
//			if (this.ottoVonRobot.distanceToObstacle(Robot.Direction.FORWARD) == Integer.MAX_VALUE){
//				this.ottoVonRobot.move(1);
//				
//				
//			}
//		}
//		else if (this.ottoVonRobot.robotMaze.mazecells.hasWallOnRight(currentX, currentY) == false){
//
//
//			this.ottoVonRobot.turnToEast();
//			if (this.ottoVonRobot.distanceToObstacle(Robot.Direction.FORWARD) == Integer.MAX_VALUE){
//				this.ottoVonRobot.move(1);
//				//this.counter++;
//				
//
//			}
//		}
//		else if (this.ottoVonRobot.robotMaze.mazecells.hasWallOnBottom(currentX, currentY) == false){
//			this.ottoVonRobot.turnToSouth();
//			if (this.ottoVonRobot.distanceToObstacle(Robot.Direction.FORWARD) == Integer.MAX_VALUE){
//				this.ottoVonRobot.move(1);
//				
//				
//
//			}
//		}
//		else if (this.ottoVonRobot.robotMaze.mazecells.hasWallOnLeft(currentX, currentY) == false){
//
//
//			this.ottoVonRobot.turnToWest();
//			if (this.ottoVonRobot.distanceToObstacle(Robot.Direction.FORWARD) == Integer.MAX_VALUE){
//				this.ottoVonRobot.move(1);
//				
//			}
//		}
//		}
		
		playActivity.mHandler.postDelayed(new Runnable() {
			public void run(){
				playActivity.maze.notifyViewerRedraw();
			}
		}, 75);


//			if(this.ottoVonRobot.robotMaze.isEndPosition(this.ottoVonRobot.robotMaze.px, this.ottoVonRobot.robotMaze.py) || this.ottoVonRobot.isAtGoal()){
//			Log.w("AMazeBy<MeadeNelsenandZacharyHughes", "At the Goal");
			
//			playActivity.mHandler.post(new Runnable() {
//				public void run(){
//					//intent.putExtra(EXTRA_MESSAGE4, maze); 
//					playActivity.startActivity(playActivity.intentSpecial);
//				}
//			});
//			
//			
//			}
//			if(this.ottoVonRobot.getBatteryLevel() - 5 < 0){
//				playActivity.mHandler.post(new Runnable() {
//					public void run(){
//						//intent.putExtra(EXTRA_MESSAGE4, maze); 
//						playActivity.startActivity(playActivity.intentSpecial);
//					}
//				});
//			}
//		if (this.ottoVonRobot.isAtGoal() == true){
//			System.out.println("Hooray"); 
//			playActivity.mHandler.post(new Runnable() {
//				public void run(){
//					//intent.putExtra(EXTRA_MESSAGE4, maze); 
//					playActivity.uponEnding();
//				}
//			});
//			if(this.ottoVonRobot.isAtGoal() == true){
//				this.ottoVonRobot.robotMaze.state = Constants.STATE_FINISH; 
//				return true; 
//			}
//			this.ottoVonRobot.robotMaze.state = Constants.STATE_UNFINISH;
//			this.ottoVonRobot.robotMaze.notifyViewerRedraw();
//			return false;
//		}


		return true; 
	
			
		}

		catch(Exception exception){
			playActivity.mHandler.post(new Runnable() {
				public void run(){
					//intent.putExtra(EXTRA_MESSAGE4, maze); 
					playActivity.uponEnding();
				}
			});
			if(this.ottoVonRobot.isAtGoal() == true){
				this.ottoVonRobot.robotMaze.state = Constants.STATE_FINISH; 
				return true; 
			}
			this.ottoVonRobot.robotMaze.state = Constants.STATE_UNFINISH;
			this.ottoVonRobot.robotMaze.notifyViewerRedraw();
			return false;
			
				
			}	

		}
	}


