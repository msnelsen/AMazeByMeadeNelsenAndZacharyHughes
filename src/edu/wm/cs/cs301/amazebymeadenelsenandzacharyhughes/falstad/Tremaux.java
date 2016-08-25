package edu.wm.cs.cs301.amazebymeadenelsenandzacharyhughes.falstad;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import edu.wm.cs.cs301.amazebymeadenelsenandzacharyhughes.falstad.Robot.Direction;
import edu.wm.cs.cs301.amazebymeadenelsenandzacharyhughes.falstad.Robot.Turn;
import edu.wm.cs.cs301.amazebymeadenelsenandzacharyhughes.ui.PlayActivity;


public class Tremaux extends AutoDriver implements RobotDriver {
   /**
    * So we do a dfs search(ie., we go down the nodes until we reach the end of a trail, then we turn the robot around)
    * If we reach a room, hug the left wall until we reach an exit. Follow WallCrawler around.  explore each of these 
    * routes, marking everything.  if at the end of each of these route, there is no exit, then 
    * continue on until you reach the room.  At the room, hug the wall until the next exit.  
    *  
    *  
    *  while not at exit: 
    *  	
    *  		if robot is in room: 
    *  			hug left wall until reach an exit
    *  			exit
    *  		if at junction
    *  			if junction not yet taken
    *  				pick junction at random
    *  			else
    *  				keep moving forward
    *  		if at dead end
    *  			turn around
    *  			move until reach junction
    *  		
    *  pick a direction at random
    *  while not at exit
    *  		if you reach a junction that has not yet been visited
    *  			pick a random direction at that junction
    *  		if you reach a junction that has been visited
    *  			if the current path is marked only once, turn around
    *  			otherwise, pick the direction with the least number of markings
    *  		
    */
	
	
	HashMap<ArrayList<Integer>, Integer> pathTracker = new HashMap<ArrayList<Integer>, Integer>();

	public Tremaux(){
		super();
	}
	
	
	/**
	 * Returns a @return boolean. Navigates from the beginning position to the end using a system
	 * that keeps track of which cells have been visted and recursively backtracks until the solution
	 * has been reached.   
	 */
	@Override 
	public boolean drive2Exit() throws Exception {
		try{
			// Method to print out the hash table			
					while(this.ottoVonRobot.isAtGoal() == false && this.playActivity.shouldQuit == false){
						
						
						if(this.ottoVonRobot.isAtGoal()){
					    	throw new Exception();
					    }

							if(this.playActivity.pausedFlag == true){
								continue;
							}
						this.ottoVonRobot.robotMaze.notifyViewerRedraw();
//							for (ArrayList<Integer> name: pathTracker.keySet()){
			//
//					            ArrayList<Integer> key = name;
//					            String value = pathTracker.get(name).toString();
//					            
//					            System.out.format("%d, %d : %s \n", key.get(0), key.get(1), value);  
//					} 
						//	System.out.println("------------");
						    if(this.ottoVonRobot.isInsideRoom() == true){
						    	if (this.ottoVonRobot.distanceToObstacle(Robot.Direction.LEFT) == 0 && this.ottoVonRobot.distanceToObstacle(Robot.Direction.FORWARD) == 0){
									this.ottoVonRobot.rotate(Turn.RIGHT);
									this.ottoVonRobot.move(1);
									
								}
								else if (this.ottoVonRobot.distanceToObstacle(Robot.Direction.LEFT) == 0 && this.ottoVonRobot.distanceToObstacle(Robot.Direction.FORWARD) != 0){
									this.ottoVonRobot.move(1);
								
								}
								else if (this.ottoVonRobot.distanceToObstacle(Robot.Direction.LEFT) != 0 && this.ottoVonRobot.distanceToObstacle(Robot.Direction.FORWARD) == 0){
									this.ottoVonRobot.rotate(Turn.LEFT);
									this.ottoVonRobot.move(1);
								}
								else{
									this.ottoVonRobot.rotate(Turn.LEFT);
									this.ottoVonRobot.move(1);
								}
						    }
							else if(this.ottoVonRobot.isAtJunction() == false && this.ottoVonRobot.distanceToObstacle(Robot.Direction.FORWARD) == 0){
							this.ottoVonRobot.rotate(Robot.Turn.AROUND);
						}
				
						    else if(this.ottoVonRobot.isAtJunction() == false){
						    	
						    	this.ottoVonRobot.move(1);
						    	if(pathTracker.get(this.ottoVonRobot.getCurrentPositionInteger()) == null){
						    		pathTracker.put(this.ottoVonRobot.getCurrentPositionInteger(), 1);	
						    	}
						    	else{
						    		pathTracker.put(this.ottoVonRobot.getCurrentPositionInteger(), 
						    				pathTracker.get(this.ottoVonRobot.getCurrentPositionInteger()) + 1);
						    	}
						    	
						    }
							else if(this.ottoVonRobot.isAtJunction() == true && this.ottoVonRobot.isInsideRoom() == false){
								
								
							int[] dxdyl = this.ottoVonRobot.relativeToDXDY(Robot.Direction.LEFT);
							int[] dxdyr = this.ottoVonRobot.relativeToDXDY(Robot.Direction.RIGHT);
							int[] dxdyf = this.ottoVonRobot.relativeToDXDY(Robot.Direction.FORWARD);
							int[] dxdyb = this.ottoVonRobot.relativeToDXDY(Robot.Direction.BACKWARD);
							int[] pxpy =  this.ottoVonRobot.getCurrentPosition();
							ArrayList<Integer> npxpyl = new ArrayList<Integer>();
							npxpyl.add(pxpy[0] + dxdyl[0]);
							npxpyl.add(pxpy[1] + dxdyl[1]);
							ArrayList<Integer> npxpyr = new ArrayList<Integer>();
							npxpyr.add(pxpy[0] + dxdyr[0]);
							npxpyr.add(pxpy[1] + dxdyr[1]);
						    ArrayList<Integer> npxpyf = new ArrayList<Integer>();
							npxpyf.add(pxpy[0] + dxdyf[0]);
							npxpyf.add(pxpy[1] + dxdyf[1]);
							ArrayList<Integer> npxpyb = new ArrayList<Integer>();
							npxpyb.add(pxpy[0] + dxdyb[0]);
							npxpyb.add(pxpy[1] + dxdyb[1]);
							Random random = new Random();
//							System.out.format("npxpyl %d, %d :", npxpyl[0], npxpyl[1]);
//							System.out.println((Integer) pathTracker.get(npxpyl));
//							System.out.format("npxpyr %d, %d :", npxpyr[0], npxpyr[1]);
//							System.out.println((Integer) pathTracker.get(npxpyr));
//							System.out.format("npxpyb %d, %d :", npxpyb[0], npxpyb[1]);
//							System.out.println((Integer) pathTracker.get(npxpyb));
//							System.out.format("npxpyf %d, %d :", npxpyf[0], npxpyf[1]);
//							System.out.println((Integer) pathTracker.get(npxpyf));
//							
							ArrayList<ArrayList<Integer>> firstClass = new ArrayList<>();
							ArrayList<ArrayList<Integer>> secondClass = new ArrayList<>();
							ArrayList<ArrayList<Integer>> thirdClass = new ArrayList<>();
							Boolean senseRight = senseRight();
							Boolean senseBack = senseBack();
							//Assign directions to their preferred direction.  
							//System.out.println(pathTracker.get(npxpyl));
							if ((Integer) pathTracker.get(npxpyl) == null && this.ottoVonRobot.distanceToObstacle(Direction.LEFT) != 0){
								firstClass.add(npxpyl);
							}else if ((Integer) pathTracker.get(npxpyl) != null && this.ottoVonRobot.distanceToObstacle(Direction.LEFT) != 0){
								//System.out.println("Got in the second conditional left");
								if((Integer) pathTracker.get(npxpyl) == 1){
								//System.out.println("Got in the secondSquared condition left");
								
								secondClass.add(npxpyl);
								}
							}else if(this.ottoVonRobot.distanceToObstacle(Direction.LEFT) != 0 ){
								//System.out.println("in here left");
								thirdClass.add(npxpyl);
							}
							
							if ((Integer) pathTracker.get(npxpyr) == null && senseRight == false){
								firstClass.add(npxpyr);
							}else if ((Integer) pathTracker.get(npxpyr) != null && senseRight == false){
								//System.out.println("Got in the second conditional right");
								if((Integer) pathTracker.get(npxpyr) == 1){
								secondClass.add(npxpyr);
								}
							}else if (senseRight == false){
								//System.out.println("in here right");
								thirdClass.add(npxpyr);
							}
							
							if ((Integer) pathTracker.get(npxpyb) == null  && senseBack == false){
								firstClass.add(npxpyb);
							}else if ((Integer) pathTracker.get(npxpyb) != null && senseBack == false){
								//System.out.println("Got in the second conditional back");
								if((Integer) pathTracker.get(npxpyb) == 1){
								secondClass.add(npxpyb);
								}
							}else if(senseRight == false){
								//System.out.println("in here back");
								thirdClass.add(npxpyb);
							}
							
							if ((Integer) pathTracker.get(npxpyf) == null && this.ottoVonRobot.distanceToObstacle(Direction.FORWARD) != 0){
								firstClass.add(npxpyf);
							}else if ((Integer) pathTracker.get(npxpyf) != null && this.ottoVonRobot.distanceToObstacle(Direction.FORWARD) != 0){
								//System.out.println("Got in the second conditional forward");
								if((Integer) pathTracker.get(npxpyf) == 1){
							    	secondClass.add(npxpyf);
								}
							}else if(this.ottoVonRobot.distanceToObstacle(Direction.FORWARD) != 0){
								//System.out.println("in here forward");
								thirdClass.add(npxpyf);
							}
							
							//Select the direction putting preferrence on the firstClass list
							int firstCount = firstClass.size();
							int secondCount = secondClass.size();
							int thirdCount = thirdClass.size();
							
//							System.out.format("first class: %d\n", firstCount);
//							System.out.println(firstClass);
//							System.out.format("Second class: %d\n", secondCount);
//							System.out.println(secondClass);
//							System.out.format("third class: %d\n", thirdCount);
//							System.out.println(thirdClass);
							
							if(firstCount != 0){
								//System.out.println("In firstCount list");
								int direction = random.nextInt(firstCount  );
								ArrayList<Integer> path = firstClass.get(direction );
								if(/*path.get(0) == npxpyl.get(1) && path.get(1) == npxpyl.get(1)*/ path==npxpyl){
									this.ottoVonRobot.rotate(Robot.Turn.LEFT);
			         				this.ottoVonRobot.move(1);
			         				this.pathTracker.put(npxpyl, 1);
			         				//System.out.format("npxpyl inserted %d, %d :", npxpyl[0], npxpyl[1]);
			         			    //System.out.println("left");
			         			 
								}
								else if(path == npxpyr){
									this.ottoVonRobot.rotate(Robot.Turn.RIGHT);
			         				this.ottoVonRobot.move(1);
			         				this.pathTracker.put(npxpyr,  1);
			         				//System.out.println("right");
								}
								else if(path == npxpyf){
			         				this.ottoVonRobot.move(1);
			         				this.pathTracker.put(npxpyf,  1);
			         				//System.out.println("forard");
								}
								else if(path == npxpyb){
									this.ottoVonRobot.rotate(Robot.Turn.AROUND);
			         				this.ottoVonRobot.move(1);
			         				this.pathTracker.put(npxpyb,  1);
			         				//System.out.println("back");
								}
									
							}
							else if(secondCount != 0 && firstCount == 0){
								//System.out.println("In secondCount list");
								int direction = random.nextInt(secondCount );
								ArrayList<Integer> path = secondClass.get(direction );
								if(path == npxpyl){
									this.ottoVonRobot.rotate(Robot.Turn.LEFT);
			         				this.ottoVonRobot.move(1);
			         				this.pathTracker.put(npxpyl, (Integer) pathTracker.get(npxpyl) + 1);
			         				//System.out.println("left");
			         				
								}
								else if(path == npxpyr){
									this.ottoVonRobot.rotate(Robot.Turn.RIGHT);
			         				this.ottoVonRobot.move(1);
			         				this.pathTracker.put(npxpyr, (Integer) pathTracker.get(npxpyr) + 1);
			         				//System.out.println("right");
								}
								else if(path == npxpyf){
			         				this.ottoVonRobot.move(1);
			         				this.pathTracker.put(npxpyf, (Integer) pathTracker.get(npxpyf) + 1);
			         				//System.out.println("forward");
								}
								else if(path == npxpyb){
									this.ottoVonRobot.rotate(Robot.Turn.AROUND);
			         				this.ottoVonRobot.move(1);
			         				this.pathTracker.put(npxpyb, (Integer) pathTracker.get(npxpyb) + 1);
			         				//System.out.println("back");
								}
							}else if(secondCount == 0 && firstCount == 0&& thirdCount != 0){
								//System.out.println("In thirdCount list");
								int direction = random.nextInt(thirdCount );
								ArrayList<Integer> path = thirdClass.get(direction );
								if(path == npxpyl){
									this.ottoVonRobot.rotate(Robot.Turn.LEFT);
			         				this.ottoVonRobot.move(1);
			         				this.pathTracker.put(npxpyl, (Integer) pathTracker.get(npxpyl) + 1);
								}
								else if(path == npxpyr){
									this.ottoVonRobot.rotate(Robot.Turn.RIGHT);
			         				this.ottoVonRobot.move(1);
			         				this.pathTracker.put(npxpyr, (Integer) pathTracker.get(npxpyr) + 1);
								}
								else if(path == npxpyf){
			         				this.ottoVonRobot.move(1);
			         				this.pathTracker.put(npxpyf, (Integer) pathTracker.get(npxpyf) + 1);
								}
								else if(path == npxpyb){
									this.ottoVonRobot.rotate(Robot.Turn.AROUND);
			         				this.ottoVonRobot.move(1);
			         				this.pathTracker.put(npxpyb, (Integer) pathTracker.get(npxpyb) + 1);	
							}
							}
							
							

				}
							this.playActivity.progressBar1.setProgress((int) this.playActivity.robot.getBatteryLevel()); 
							
							

							
							

							
						    playActivity.mHandler.postDelayed(new Runnable() {
								public void run(){
									playActivity.maze.notifyViewerRedraw();
								}
							}, 75);
						   // System.out.println("----------------------");
						}
					
					playActivity.mHandler.post(new Runnable() {
						public void run(){
							//intent.putExtra(EXTRA_MESSAGE4, maze); 
							playActivity.uponEnding();
						}
					});
				

						
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
					exception.printStackTrace();
					this.ottoVonRobot.robotMaze.state = Constants.STATE_UNFINISH;
					this.ottoVonRobot.robotMaze.notifyViewerRedraw();
					return false;
					}
					
					
				}
			public boolean senseRight() throws Exception{
				this.ottoVonRobot.rotate(Robot.Turn.RIGHT);
				if(this.ottoVonRobot.distanceToObstacle(Robot.Direction.FORWARD) == 0){
					this.ottoVonRobot.rotate(Robot.Turn.LEFT);
					return true;
						
				}else{
					this.ottoVonRobot.rotate(Robot.Turn.LEFT);
					return false;
				}
			}

			public boolean senseBack() throws Exception{
				this.ottoVonRobot.rotate(Turn.LEFT);
				if(this.ottoVonRobot.distanceToObstacle(Robot.Direction.LEFT) == 0){
					this.ottoVonRobot.rotate(Robot.Turn.RIGHT);
					return true;
						
				}else{
					this.ottoVonRobot.rotate(Robot.Turn.RIGHT);
					return false;
				}
				
			}
			

			}

