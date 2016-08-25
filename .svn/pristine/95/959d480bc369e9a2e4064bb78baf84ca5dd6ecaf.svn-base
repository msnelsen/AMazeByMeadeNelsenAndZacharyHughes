package edu.wm.cs.cs301.amazebymeadenelsenandzacharyhughes.falstad;


/**
 * This class has the responsibility to create a maze of given dimensions (width, height) using the 
 * Aldous Broder algorithm.  Maze calls the build() method and provides width and height. Maze has a 
 * call back method newMaze that this class calls to communicate a new maze and a BSP root node and a solution. 
 * 
 * This code is based on code written by Falstad and refactored by Peter Kemper. The final product is written by Zachary Douglas Hughes and 
 * Meade Sumner Nelsen. 
 */
public class MazeBuilderAldousBroder extends MazeBuilder  {
	
	public MazeBuilderAldousBroder() {
		
		super();
		System.out.println("MazeBuilderAldousBroder uses Aldous-Broder's algorithm to generate maze.");
	}
	
	public MazeBuilderAldousBroder(boolean det) {
		super(det);
		System.out.println("MazeBuilderAldousBroder uses Aldous-Broder's algorithm to generate maze.");
	}
	
	/**
 * This generates pathways using the Aldous Broder algorithm. This code pseudo-randomly selects a starting position
 * in the maze, sets the cell as visited, and begins its path carving. While there are still cells that have not been
 * visited, it selects a direction and goes to that cell as long as it can be legally reached. If it's the first visit 
 * to that cell, it carves out a wall and sets it as visited. Otherwise, it simply visits the cell and then selects 
 * another direction in which to travel. 
 * 
 * This continues until every cell has been visited and an appropriate maze has been carved out. 
 */
	@Override
	protected void generatePathways() {
		// pick initial position (x,y) at some random position on the maze
		int x = random.nextIntWithinInterval(0, width-1);
		int y = random.nextIntWithinInterval(0, height - 1);
		cells.setCellAsVisited(x, y); 
		// create an initial list of all walls that could be removed
		// those walls lead to adjacent cells that are not part of the spanning tree yet.
		int counter = 1;
		int direction = random.nextIntWithinInterval(0, 3); 
		//Checks to make sure that we haven't visited all rooms
		while(counter < width * height){ 

			if (direction == 0){
				//Checks legality, proceeds if able
				if (y - 1 >= 0){
					if(isFirstVisit(x, (y - 1))){ 
					cells.deleteWall(x, y, 0, -1); 
					cells.setCellAsVisited(x, y - 1);
					counter = counter + 1; 
					}
				y = y - 1; 
				}
			}
			
			else if (direction == 1){
				if (x + 1 < width){
					if(isFirstVisit((x + 1), y)){
					cells.deleteWall(x, y, 1, 0); 
					cells.setCellAsVisited(x + 1, y);
					counter = counter + 1; 
					}
				x = x + 1; 
				}
			}
			
			else if (direction == 2){ 
				if (y + 1 < height ){
					if(isFirstVisit(x, (y + 1))){
					cells.deleteWall(x, y, 0, 1); 
					cells.setCellAsVisited(x, y + 1);
					counter = counter + 1; 
					}
				y = y + 1; 
				}
			}
			
			else if (direction == 3){
				if (x - 1 >= 0){
					if(isFirstVisit((x - 1), y)){ 
					cells.deleteWall(x, y, -1, 0); 
					cells.setCellAsVisited(x - 1, y);
					counter = counter + 1; 
					}
				x = x - 1; 
				}
			}
			
			direction = random.nextIntWithinInterval(0, 3);  
			
			

			}
		
	//	System.out.print("Is built");
		
		}
		
/**
 * This method was imported from another class in order to avoid strange compilation issues in 
 * WebCAT. For further documentation, please see documentation in the Cells class. 
 */
	private boolean isFirstVisit(int x, int y) {
		return cells.hasMaskedBitsTrue(x, y, Constants.CW_VISITED);
	}		



}
