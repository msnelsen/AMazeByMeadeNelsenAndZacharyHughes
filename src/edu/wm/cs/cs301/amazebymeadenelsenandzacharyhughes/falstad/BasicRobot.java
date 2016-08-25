package edu.wm.cs.cs301.amazebymeadenelsenandzacharyhughes.falstad;
import java.awt.*;
import java.util.ArrayList;

/**
 * 
 * @author meadenelsen & zacharydhughes
 * 
 * Basic Robot is an implementation of Robot interface.  It is responsible for implementing moves and maintaining the battery level
 * It is also responsible for various sensors like the juctionSensor, roomSensor, and distanceSensor
 * It is the middleman between the Maze class and the driver ManualDriver class.  
 * It also works with cells.
 * 
 *
 */

public class BasicRobot implements Robot {

	public float batteryLevel = 2500; 
	public int[] position = new int[2];
	public Maze robotMaze = new Maze(); 
	public int[] direction = new int[2];
	public boolean junctionSensor, roomSensor, forwardDistanceSensor, leftDistanceSensor, rightDistanceSensor, backDistanceSensor;
	String thatWhichHasBeenPassed ;
	public ManualDriver driver; 
	public int counter;
	
	public BasicRobot(){
		this.junctionSensor = true;
		this.roomSensor = true;
		this.forwardDistanceSensor = true;
		this.leftDistanceSensor = true;  
		this.rightDistanceSensor = false;  
		this.backDistanceSensor = false;  
	}
	
	public BasicRobot(boolean hasJunction, boolean hasRoom, boolean hasForward, boolean hasLeft, boolean hasRight, boolean hasBack){
		this.junctionSensor = hasJunction; 
		this.roomSensor = hasRoom; 
		this.forwardDistanceSensor = hasForward;
		this.leftDistanceSensor = hasLeft; 
		this.rightDistanceSensor = hasRight; 
		this.backDistanceSensor = hasBack; 
	}
	
	/**
	 * Takes in a direction, @param turn. If the robot energy levels are sufficient, 
	 * the rotation is performed. Otherwise, the operation throws an exception.  
	 */
	@Override
	public void rotate(Turn turn) throws Exception {
		if( turn == Turn.LEFT || turn == Turn.RIGHT){ 
		    if (this.batteryLevel < 3){
				throw new Exception();  
			}
		}
		 else if (turn == Turn.AROUND){
			 if (this.batteryLevel < 6){
				 throw new Exception();
			 }
		    }
		
		if (turn == Turn.LEFT || turn == Turn.RIGHT){
			this.setBatteryLevel(this.getBatteryLevel() - 3);
		}
		if (turn == Turn.AROUND){
			this.setBatteryLevel(this.getBatteryLevel() - 6);
		}
		
		//if statements for the turning
		 
		if (turn == Turn.RIGHT){
			this.robotMaze.rotate(-1);
//			System.out.println("RIGHT");
		}
		else if(turn == Turn.LEFT){
			this.robotMaze.rotate(1);
		}
		else if(turn == Turn.AROUND){
			this.robotMaze.rotate(-1);
			this.robotMaze.rotate(-1);
		}


		}
		

	/**
	 * Takes in a distance, @param distance. If the robot energy levels are sufficient, 
	 * the movement is performed. Otherwise, the operation throws an exception.  
	 */
	@Override
	public void move(int distance) throws Exception {	
		
		for (int movesMade = 0; movesMade < distance; movesMade++){
			
//			if(this.isAtGoal() == true){
//				throw new Exception();
//			}
			
			if (this.getBatteryLevel() - 5 <= 0){
				System.out.println(this.batteryLevel); 
				throw new Exception(); 
				
			}
			
			else{
		        this.robotMaze.walk(1);
		        this.counter ++;
		        System.gc();
		   
				this.setBatteryLevel(this.getBatteryLevel() - this.getEnergyForStepForward());
				


			}
		}
	}
	
	public ArrayList<Integer> getCurrentPositionInteger() throws Exception {

		ArrayList<Integer> returnArray = new ArrayList<Integer>(); 
		if((Integer)this.robotMaze.px < 0 || (Integer)this.robotMaze.px > (Integer)this.robotMaze.mazew){

			throw new Exception();	
		}
		else if(this.robotMaze.py < 0 || this.robotMaze.py > this.robotMaze.mazeh){

			throw new Exception();
		}
		else{
			
			returnArray.add(this.robotMaze.px);  
			returnArray.add(this.robotMaze.py); 
			return returnArray; 
		}
		
	}

	
	/**
	 * Returns the current position, but throws an exception if the position is out of range. 
	 */
	@Override
	public int[] getCurrentPosition() throws Exception {

		int[] returnArray = new int[2]; 
		if(this.robotMaze.px < 0 || this.robotMaze.px > this.robotMaze.mazew){

			throw new Exception();	
		}
		else if(this.robotMaze.py < 0 || this.robotMaze.py > this.robotMaze.mazeh){

			throw new Exception();
		}
		else{
			returnArray[0] = this.robotMaze.px;  
			returnArray[1] = this.robotMaze.py; 
			return returnArray; 
		}
		
	}
	
	/**
	 * Sets the position to the x and y values passed in
	 */
	public void setPosition(int x, int y){
		
		this.robotMaze.px = x; 
		this.robotMaze.py = y; 
	}

	/**
	 * sets the robot's maze to the maze we passed in (@param maze)
	 */
	@Override
	public void setMaze(Maze maze) {

		this.robotMaze = maze;	
	}

	/**
	 * returns whether or not the position is the exit position using Cells.java's isExitPosistion() method
	 */
	@Override
	public boolean isAtGoal() {

		return (this.robotMaze.mazecells.isExitPosition(this.robotMaze.px, this.robotMaze.py));
	}

	/**
	 * Takes in @param direction, determines whether the goal is visible in a given direction and given the 
	 * robot's current location. If it is, returns true. If not, returns false. 
	 */
	@Override
	public boolean canSeeGoal(Direction direction){ 
	 
	    int tempX, tempY;
	    tempX = this.robotMaze.px;
	    tempY = this.robotMaze.py; 
	    int[] dxdy = new int[2];
	    dxdy = this.relativeToDXDY(direction);
	    boolean hasWall = false;
	    int distanceCounter = 0; 
	    
	     
	    while(hasWall == false && distanceCounter <= Integer.MAX_VALUE) {

		if(dxdy[0] == 0 && dxdy[1] == -1){
			hasWall = this.robotMaze.mazecells.hasWallOnTop(tempX, tempY);
			distanceCounter++; 

		}
		if(dxdy[0] == -1 && dxdy[1] == 0){
			hasWall = this.robotMaze.mazecells.hasWallOnLeft(tempX, tempY);
			distanceCounter++; 

		}
		if(dxdy[0] == 0 && dxdy[1] == 1){
			hasWall = this.robotMaze.mazecells.hasWallOnBottom(tempX, tempY);
			distanceCounter++; 

		}
		if(dxdy[0] == 1 && dxdy[1] == 0){
			hasWall = this.robotMaze.mazecells.hasWallOnRight(tempX, tempY);
			distanceCounter++; 

		}
		if (hasWall == false){
			if(this.robotMaze.mazecells.isExitPosition(tempX, tempY)){
				return true;
			}
			tempX = tempX + this.robotMaze.dx;
			tempY = tempY + this.robotMaze.dy;

		}	
	}
	return false;
	}

	/**
	 * Checks to see whether there are walls to the left and right of the robot. Relies on the orientation, 
	 * so checks north/south, then rotates and checks east/west
	 */
	@Override
	public boolean isAtJunction() throws UnsupportedOperationException {
		
		if(this.hasJunctionSensor() == false){
		    throw new UnsupportedOperationException();
		}

		
		else if((this.robotMaze.dx == 0 && this.robotMaze.dy == -1) || (this.robotMaze.dx == 0 && this.robotMaze.dy == 1)){
			
		    if(this.robotMaze.mazecells.hasWallOnLeft(this.robotMaze.px, this.robotMaze.py)
		    		&& this.robotMaze.mazecells.hasWallOnRight(this.robotMaze.px, this.robotMaze.py)){
		    	
		    	return false;
		    }
		    
		    else{
		    	return true;
		    } 
		}
		else{
			
		    if(this.robotMaze.mazecells.hasWallOnTop(this.robotMaze.px, this.robotMaze.py)
		    		&& this.robotMaze.mazecells.hasWallOnBottom(this.robotMaze.px, this.robotMaze.py)){
		    	
		    	return false;
		    }
		    
		    else{
		    	
		    	return true;
		    }	    
	}
	}

	/**
	 * Returns a boolean indicating whether the robot has a junction sensor
	 */
	@Override
	public boolean hasJunctionSensor() {
		return this.junctionSensor;
	}

	/**
	 * Returns a boolean indicating whether the robot is inside a room
	 */
	@Override
	public boolean isInsideRoom() throws UnsupportedOperationException {
		
		if(this.hasRoomSensor() == false){
			throw new UnsupportedOperationException();
		}
		return(this.robotMaze.mazecells.isInRoom(this.robotMaze.px, this.robotMaze.py));
	}

	/**
	 * Returns a boolean indicating whether the robot is endowed with a room sensor
	 */
	@Override
	public boolean hasRoomSensor() {
		return this.roomSensor;
	}

	/**
	 * Returns an array indicating the current direction in which the robot is facing
	 */
	@Override
	public int[] getCurrentDirection() {
		
		int[] toReturn = new int[2];
		int x = this.robotMaze.dx;
		int y = this.robotMaze.dy;
		toReturn[0] = x;
		toReturn[1] = y;
		return toReturn;
		
	}

	/**
	 * Returns the battery level
	 */
	@Override
	public float getBatteryLevel() {

		return this.batteryLevel;
	}

	/**
	 * Sets the batteryLevel float with the new batteryLevel from @param level
	 */
	@Override
	public void setBatteryLevel(float level) {

		this.batteryLevel = level;

	}

	/**
	 * Returns the energy required for a full rotation, as given as a constant in the project specifications
	 */
	@Override
	public float getEnergyForFullRotation() {

		float x = 12;
		return(x);
	}

	/**
	 * Returns the energy required for a step forward, which given as a constant in the project specifications
	 */
	@Override
	public float getEnergyForStepForward() {

		return 5;
	}
	
	/**
	 * Returns a boolean indicating whether the robot is still capable of motion
	 */
	@Override
	public boolean hasStopped() throws Exception {
		
		if (batteryLevel <= 0 ){
			return true; 
		}

		return false; 
	}

	/**
	 * Given @param direction, determines how many moves must be made to reach the first obstacle
	 * @throws Exception, UnsupportedOperationException 
	 */
	@Override
	public int distanceToObstacle(Direction direction) throws Exception, UnsupportedOperationException  {
		
		if(this.hasDistanceSensor(direction) == false){
			System.out.println("We're trying to use a sensor that doesn't exist"); 
			throw new UnsupportedOperationException();
		}
		
		if (this.batteryLevel - 1 <= 0){
			System.out.println("Trying to check distance to obstacle, but not enough battery"); 
			throw new Exception(); 
		}
		
		int[] dxdy = new int[2];
		dxdy = this.relativeToDXDY(direction);
		int tempX, tempY, travelled;
		this.setBatteryLevel(this.getBatteryLevel() - 1);
		tempX = this.robotMaze.px;
		tempY = this.robotMaze.py;
		travelled = 0; 
		boolean hasWall = false;
		while(hasWall == false && travelled <= Integer.MAX_VALUE && tempX >=0 && tempY >= 0){
			if(dxdy[0] == 0 && dxdy[1] == -1){
				hasWall = this.robotMaze.mazecells.hasWallOnTop(tempX, tempY);
			}
			if(dxdy[0] == -1 && dxdy[1] == 0){
				hasWall = this.robotMaze.mazecells.hasWallOnLeft(tempX, tempY);
			}
			if(dxdy[0] == 0 && dxdy[1] == 1){
				hasWall = this.robotMaze.mazecells.hasWallOnBottom(tempX, tempY);
			}
			if(dxdy[0] == 1 && dxdy[1] == 0){
				hasWall = this.robotMaze.mazecells.hasWallOnRight(tempX, tempY);
			}
			if (hasWall == false){
				travelled = travelled + 1;
				tempX = tempX + dxdy[0];
				tempY = tempY + dxdy[1];
			}	
		}
		
		return travelled;
	}

	/**
	 * Returns a boolean indicating whether the robot is enabled to find distances
	 */
	@Override
	public boolean hasDistanceSensor(Direction direction) {
		if (direction == Robot.Direction.FORWARD){
			if (this.forwardDistanceSensor == true){
				return true; 
			}
			}
		if (direction == Robot.Direction.LEFT){
			if (this.leftDistanceSensor == true){
				return true; 
			}
		}
		if (direction == Robot.Direction.RIGHT){
			if (this.rightDistanceSensor == true){
				return true; 
			}
		}
		if (direction == Robot.Direction.BACKWARD){
			if (this.backDistanceSensor == true){
				return true; 
			}
		}
		return false; 
	}
	
	/**
	 * Returns an array of the dx/dy values given the current position and the Direction @param relative
	 */
	public int[] relativeToDXDY(Direction relative){
	    int[] toReturn = new int[2];
	    toReturn[0] = this.robotMaze.dx;
	    toReturn[1] = this.robotMaze.dy;
		
		if (relative == Direction.FORWARD){
			toReturn = toReturn;
			}

		else if (relative == Direction.BACKWARD){
			toReturn[0] = -toReturn[0];
			toReturn[1] = -toReturn[1];
			}
		
		else if (relative == Direction.LEFT){
			if (this.robotMaze.dx == 0 && this.robotMaze.dy == -1){
				toReturn[0] = 1;
				toReturn[1] = 0;
			}
			else if (this.robotMaze.dx == 1 && this.robotMaze.dy == 0){
				toReturn[0] = 0;
				toReturn[1] = 1; 
			}
			else if (this.robotMaze.dx == 0 && this.robotMaze.dy == 1){
				toReturn[0] = -1;
				toReturn[1] = 0; 
			} 
			else{
				toReturn[0] = 0;
				toReturn[1] = -1;
			}
			}
		
		else{
			if (this.robotMaze.dx == 0 && this.robotMaze.dy == -1){
				toReturn[0] = -1;
				toReturn[1] = 0;
			}
			else if (this.robotMaze.dx == 1 && this.robotMaze.dy == 0){
				toReturn[0] = 0;
				toReturn[1] = -1;
			}
			else if (this.robotMaze.dx == 0 && this.robotMaze.dy == 1){
				toReturn[0] = 1;
				toReturn[1] = 0; 
			}
			else{
				toReturn[0] = 0;
				toReturn[1] = 1; 
			}
			}
		return toReturn;
	}
	
	/**
	 * Links the BasicRobot to a driver given @param d
	 */
	public void setDriver(RobotDriver d){
		this.driver =  (ManualDriver) d;
	}
    
	/**
	 * Passes information to Maze and ManualDriver
	 */
	public void letSlipTheDogsOfWar() throws Exception{
		this.driver.cryOfTheDamned = this.thatWhichHasBeenPassed;
		this.driver.drive2Exit();
	}
	
	/**
	 * Turns the robot to face north
	 */
	public void turnToNorth() throws Exception{
		int currentDX = this.getCurrentDirection()[0]; 
		int currentDY = this.getCurrentDirection()[1]; 
		
		if (currentDX == 0 && currentDY == -1){
			return; 
		}
		
		else if (currentDX == 0 && currentDY == 1){
			this.rotate(Turn.LEFT);
			this.rotate(Turn.LEFT);
		}
		
		else if (currentDX == 1 && currentDY == 0){
			this.rotate(Turn.RIGHT);
		}
		
		else if (currentDX == -1 && currentDY == 0){
			this.rotate(Turn.LEFT);
		}
	}

	/**
	 * Turns the robot to face east
	 */
	public void turnToEast() throws Exception{
		int currentDX = this.getCurrentDirection()[0]; 
		int currentDY = this.getCurrentDirection()[1]; 
		
		if (currentDX == 0 && currentDY == -1){
			this.rotate(Turn.LEFT);
		}
		
		else if (currentDX == 0 && currentDY == 1){
			this.rotate(Turn.RIGHT);
		}
		
		else if (currentDX == 1 && currentDY == 0){
			return;
		}
		
		else if (currentDX == -1 && currentDY == 0){
			this.rotate(Turn.LEFT);
			this.rotate(Turn.LEFT); 
		}
	}

	/**
	 * Turns the robot to face south
	 */
	public void turnToSouth() throws Exception{
		int currentDX = this.getCurrentDirection()[0]; 
		int currentDY = this.getCurrentDirection()[1]; 
		
		if (currentDX == 0 && currentDY == -1){
			this.rotate(Turn.LEFT);
			this.rotate(Turn.LEFT); 
		}
		
		else if (currentDX == 0 && currentDY == 1){
			return;
		}
		
		else if (currentDX == 1 && currentDY == 0){
			this.rotate(Turn.LEFT);
		}
		
		else if (currentDX == -1 && currentDY == 0){
			this.rotate(Turn.RIGHT);  
		}
	}
	
	/**
	 * Turns the robot to face west
	 */
	public void turnToWest() throws Exception{
		int currentDX = this.getCurrentDirection()[0]; 
		int currentDY = this.getCurrentDirection()[1]; 
		
		if (currentDX == 0 && currentDY == -1){
			this.rotate(Turn.RIGHT); 
		}
		
		else if (currentDX == 0 && currentDY == 1){
			this.rotate(Turn.LEFT);
		}
		
		else if (currentDX == 1 && currentDY == 0){
			this.rotate(Turn.LEFT);
			this.rotate(Turn.LEFT); 
		}
		
		else if (currentDX == -1 && currentDY == 0){
			return;  
		}
	}
	
	public boolean hasNotLeftMaze(){
		int mazeHeight = this.robotMaze.mazeh; 
		int mazeWidth = this.robotMaze.mazew; 
		
		if (this.robotMaze.px < 0 || this.robotMaze.px >= mazeWidth){
			return false; 
		}
		
		if (this.robotMaze.py < 0 || this.robotMaze.py >= mazeHeight){
			return false; 
		}
		
		return true; 
	}
}
