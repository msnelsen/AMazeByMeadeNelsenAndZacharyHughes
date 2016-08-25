package edu.wm.cs.cs301.amazebymeadenelsenandzacharyhughes.falstad;

import edu.wm.cs.cs301.amazebymeadenelsenandzacharyhughes.falstad.Robot.Turn;

public class WallFollower extends AutoDriver implements RobotDriver{
	
	public WallFollower(){
		super();
	}
	
	/**
	 * Returns a @return boolean. Navigates by clinging to the left wall until it either reaches the 
	 * exit or runs out of battery. 
	 */
	@Override
	public boolean drive2Exit() throws Exception {
		try{
			
			while (this.ottoVonRobot.isAtGoal() == false && this.playActivity.shouldQuit == false){
				if(this.playActivity.pausedFlag == true){
					continue;
				}
				if (this.ottoVonRobot.distanceToObstacle(Robot.Direction.LEFT) == 0 && this.ottoVonRobot.distanceToObstacle(Robot.Direction.FORWARD) == 0){
					this.ottoVonRobot.rotate(Turn.RIGHT);
					this.ottoVonRobot.move(1);
					this.counter++;
				}
				else if (this.ottoVonRobot.distanceToObstacle(Robot.Direction.LEFT) == 0 && this.ottoVonRobot.distanceToObstacle(Robot.Direction.FORWARD) != 0){
					this.ottoVonRobot.move(1);
					this.counter++;
				}
				else if (this.ottoVonRobot.distanceToObstacle(Robot.Direction.LEFT) != 0 && this.ottoVonRobot.distanceToObstacle(Robot.Direction.FORWARD) == 0){
					this.ottoVonRobot.rotate(Turn.LEFT);
					this.ottoVonRobot.move(1);
					this.counter++;
				}
				else{
					this.ottoVonRobot.rotate(Turn.LEFT);
					this.ottoVonRobot.move(1);
					this.counter++;
				}

				this.playActivity.progressBar1.setProgress((int) this.playActivity.robot.getBatteryLevel()); 

				} 

//			while (this.ottoVonRobot.distanceToObstacle(Robot.Direction.FORWARD) != Integer.MAX_VALUE){
//				System.out.println(this.ottoVonRobot.distanceToObstacle(Robot.Direction.FORWARD));
//				this.ottoVonRobot.rotate(Turn.LEFT);
//			}
			this.ottoVonRobot.move(1);
			this.counter++;

			
			playActivity.mHandler.postDelayed(new Runnable() {
					public void run(){
						playActivity.maze.notifyViewerRedraw();
					}
				}, 10);
			
			playActivity.mHandler.post(new Runnable() {
				public void run(){
					//intent.putExtra(EXTRA_MESSAGE4, maze); 
					playActivity.uponEnding();
				}
			});

			
			playActivity.mHandler.post(new Runnable() {
				public void run(){
					//intent.putExtra(EXTRA_MESSAGE4, maze); 
					playActivity.uponEnding();
				}
			});
			return true;
			
		}
			
//			while(this.ottoVonRobot.isAtGoal() == false){
//				if(this.ottoVonRobot.isAtJunction() == true && this.ottoVonRobot.distanceToObstacle(Robot.Direction.LEFT) != 0){
//					this.ottoVonRobot.rotate(Robot.Turn.LEFT);
//					this.ottoVonRobot.move(1);
//					System.out.println("First block"); 
//				}
//				
//				else if(this.ottoVonRobot.distanceToObstacle(Robot.Direction.FORWARD) == 0  && 
//						this.ottoVonRobot.distanceToObstacle(Robot.Direction.LEFT) == 0){
//					this.ottoVonRobot.rotate(Robot.Turn.RIGHT);
//					//this.ottoVonRobot.move(1);
//					System.out.println("Second block"); 
//				}
//				else{
//					this.ottoVonRobot.move(1);
//					System.out.println("Third block"); 
//				}
//			}
//			return true;
		
		catch(Exception exception){
			playActivity.mHandler.post(new Runnable() {
				public void run(){
					System.out.println("Upon ending is now being called!"); 
					//intent.putExtra(EXTRA_MESSAGE4, maze); 
					playActivity.uponEnding();
				}
			});
			if(this.ottoVonRobot.robotMaze.isEndPosition(this.ottoVonRobot.robotMaze.px, this.ottoVonRobot.robotMaze.py)){
				this.ottoVonRobot.robotMaze.state = Constants.STATE_FINISH; 
				return true; 
			}
			this.ottoVonRobot.robotMaze.state = Constants.STATE_UNFINISH;
			this.ottoVonRobot.robotMaze.notifyViewerRedraw();
			return false;	
	}

}
}