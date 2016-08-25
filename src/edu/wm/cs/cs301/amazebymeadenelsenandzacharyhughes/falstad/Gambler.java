package edu.wm.cs.cs301.amazebymeadenelsenandzacharyhughes.falstad;
import java.util.Random;
import java.util.Random.*;

import edu.wm.cs.cs301.amazebymeadenelsenandzacharyhughes.falstad.Robot.Turn;

public class Gambler extends AutoDriver implements RobotDriver {
	
	public Gambler(){
		super();
	}

	/**
	 * Returns a @return boolean. Hopefully navigates from the begining position
	 * to the end by randomly selecting a direction and going in it. This continues until 
	 * the battery is depleted or the robot has reached its final destination.  
	 */
	@Override 
	public boolean drive2Exit() throws Exception {
		Random random = new Random();
		int direction = random.nextInt(4);
		try{
			while(this.ottoVonRobot.isAtGoal() == false && this.playActivity.shouldQuit == false){
				
					if(this.playActivity.pausedFlag == true){
						continue;
					}
				if(direction == 0){
					this.ottoVonRobot.move(1);	
					
					 
				}
				else if(direction == 1){
					this.ottoVonRobot.rotate(Robot.Turn.LEFT);
					this.ottoVonRobot.move(1);
					
				}
				else if(direction == 2){
					this.ottoVonRobot.rotate(Robot.Turn.RIGHT);
					this.ottoVonRobot.move(1);	
					
					}
				else{
					this.ottoVonRobot.rotate(Robot.Turn.AROUND);
					this.ottoVonRobot.move(1);
					
				}
				direction = random.nextInt(4); 
				
				this.playActivity.progressBar1.setProgress((int) this.playActivity.robot.getBatteryLevel()); 
				
				System.out.println("Still gamblin");
				
//				playActivity.mHandler.post(new Runnable() {
//					public void run(){
//						playActivity.progressBar1.setProgress((int) playActivity.robot.getBatteryLevel());
//					}
//				});
				
			}
			
			while (this.ottoVonRobot.canSeeGoal(Robot.Direction.FORWARD) == false && this.playActivity.shouldQuit == false){
				this.ottoVonRobot.rotate(Turn.LEFT);
			}
			
			this.ottoVonRobot.move(1);
		
			playActivity.mHandler.post(new Runnable() {
				public void run(){
					//intent.putExtra(EXTRA_MESSAGE4, maze); 
					playActivity.uponEnding();
				}
			});

			
			 playActivity.mHandler.postDelayed(new Runnable() {
					public void run(){
						playActivity.maze.notifyViewerRedraw();
					}
				}, 75);
			 
			return true; 
			
			}
	
		catch(Exception exception){
			playActivity.mHandler.post(new Runnable() {
				public void run(){
					//intent.putExtra(EXTRA_MESSAGE4, maze); 
					playActivity.uponEnding();
				}
			});
			if(this.ottoVonRobot.robotMaze.isEndPosition(this.ottoVonRobot.robotMaze.px, this.ottoVonRobot.robotMaze.py)){
				this.ottoVonRobot.robotMaze.state = Constants.STATE_FINISH; 
				return true; 
			}
			this.ottoVonRobot.robotMaze.state = Constants.STATE_UNFINISH;
			exception.printStackTrace(); 
			System.out.println("Issue with the exception in Gambler"); 
			this.ottoVonRobot.robotMaze.notifyViewerRedraw();
			return false;	
			}	
			}
		
	}
	


