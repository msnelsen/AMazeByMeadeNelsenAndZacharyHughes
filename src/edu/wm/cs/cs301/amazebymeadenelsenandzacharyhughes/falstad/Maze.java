package edu.wm.cs.cs301.amazebymeadenelsenandzacharyhughes.falstad;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import edu.wm.cs.cs301.amazebymeadenelsenandzacharyhughes.ui.GeneratingActivity;
import edu.wm.cs.cs301.amazebymeadenelsenandzacharyhughes.ui.PlayActivity;
import android.app.Application;
import android.os.Handler;
import android.os.Message;



/**
 * Class handles the user interaction for the maze. 
 * It implements a state-dependent behavior that controls the display and reacts to key board input from a user. 
 * After refactoring the original code from an applet into a panel, it is wrapped by a MazeApplication to be a java application 
 * and a MazeApp to be an applet for a web browser. At this point user keyboard input is first dealt with a key listener
 * and then handed over to a Maze object by way of the keyDown method.
 *
 * This code is refactored code from Maze.java by Paul Falstad, www.falstad.com, Copyright (C) 1998, all rights reserved
 * Paul Falstad granted permission to modify and use code for teaching purposes.
 * Refactored by Peter Kemper
 */
// MEMO: original code: public class Maze extends Applet {
//public class Maze extends Panel {
public class Maze extends Application implements Serializable{
	
	GeneratingActivity generating; 
	PlayActivity playing; 
	public String saveMaze;

	// Model View Controller pattern, the model needs to know the viewers
	// however, all viewers share the same graphics to draw on, such that the share graphics
	// are administered by the Maze object
	final private ArrayList<Viewer> views = new ArrayList<Viewer>() ; 
//	MazePanel panel = new MazePanel(this) ; // graphics to draw on, shared by all views
		

    public BasicRobot mazeRobot; //WE WILL NEED TO CHANGE THIS WHEN NEW ROBOTS GET MADE
	public int state;			// keeps track of the current GUI state, one of STATE_TITLE,...,STATE_FINISH, mainly used in redraw()
	// possible values are defined in Constants
	// user can navigate 
	// title -> generating -(escape) -> title
	// title -> generation -> play -(escape)-> title
	// title -> generation -> play -> finish -> title
	// STATE_PLAY is the main state where the user can navigate through the maze in a first person view

	private int percentdone = 0; // describes progress during generation phase
	public boolean showMaze;		 	// toggle switch to show overall maze on screen
	public boolean showSolution;		// toggle switch to show solution in overall maze on screen
	public boolean solving;			// toggle switch 
	public boolean mapMode;    // true: display map of maze, false: do not display map of maze
	// map_mode is toggled by user keyboard input, causes a call to draw_map during play mode

	//static final int viewz = 50;    
	int viewx, viewy, angle;
	int dx, dy;  // current direction
	public int px ; // current position on maze grid (x,y)
	public int py;
	int walkStep;
	int viewdx, viewdy; // current view direction
	
	String theThingThatShallBePassed;
	public int difficulty;


	// debug stuff
	boolean deepdebug = false;
	boolean allVisible = false;
	boolean newGame = false;
    boolean keys = false;
    public Handler handler= new Handler();

	// properties of the current maze
	public int mazew; // width of maze
	public int mazeh; // height of maze
	public Cells mazecells ; // maze as a matrix of cells which keep track of the location of walls
	Distance mazedists ; // a matrix with distance values for each cell towards the exit
	Cells seencells ; // a matrix with cells to memorize which cells are visible from the current point of view
	// the FirstPersonDrawer obtains this information and the MapDrawer uses it for highlighting currently visible walls on the map
	BSPNode rootnode ; // a binary tree type search data structure to quickly locate a subset of segments
	// a segment is a continuous sequence of walls in vertical or horizontal direction
	// a subset of segments need to be quickly identified for drawing
	// the BSP tree partitions the set of all segments and provides a binary search tree for the partitions
	

	// Mazebuilder is used to calculate a new maze together with a solution
	// The maze is computed in a separate thread. It is started in the local Build method.
	// The calculation communicates back by calling the local newMaze() method.
	public MazeBuilder mazebuilder;
	MazePanel panel = new MazePanel(); 

	
	// fixing a value matching the escape key
	final int ESCAPE = 27;

	// generation method used to compute a maze
	int method = 0 ; // 0 : default method, Falstad's original code
	// method == 1: Prim's algorithm
	// method == 2: Aldous-Broder

	int zscale = Constants.VIEW_HEIGHT/2;

	private RangeSet rset;
	private PlayActivity playActivity;
	
	/**
	 * Constructor
	 */
	public Maze() {
		super() ;
		panel = new MazePanel() ;
	}
	/**
	 * Constructor that also selects a particular generation method
	 */
	public Maze(int method)
	{
		super() ;
		// 0 is default, do not accept other settings but 0, 1, 2
		if (1 == method){
			this.method = 1 ;
		}else if(2 == method){
			this.method = 2 ;
		}else if(3 == method){
			this.method = 3;
		}
		panel = new MazePanel() ;
	}
	/**
	 * Method to initialize internal attributes. Called separately from the constructor. 
	 */
	public void init() {
//		state = Constants.STATE_TITLE;
		rset = new RangeSet();
		//panel.initBufferImage() ;
//		addView(new MazeView(this)) ;
		//notifyViewerRedraw() ;
	}
	
	/**
	 * Method obtains a new Mazebuilder and has it compute new maze, 
	 * it is only used in keyDown()
	 * @param skill level determines the width, height and number of rooms for the new maze
	 */
	public void build(int skill, String saveMaze ) {
		// switch screen
		//IMPLEMENT CHOOSEY THINGY
		this.saveMaze = saveMaze;
		
		state = Constants.STATE_GENERATING;
		percentdone = 0;
	//	notifyViewerRedraw() ; 
		// select generation method
		switch(method){ 
		case 3 : mazebuilder = new MazeBuilderAldousBroder(true);
		break;
		case 2 : mazebuilder = new MazeBuilderAldousBroder(); // generate with Aldous-Broder
		break;
		case 1 : mazebuilder = new MazeBuilderPrim(); // generate with Prim's algorithm
		break ;
		case 0: // generate with Falstad's original algorithm (0 and default), note the missing break statement
		default : mazebuilder = new MazeBuilder(); 
		break ;
		}
		// adjust settings and launch generation in a separate thread

		mazew = Constants.SKILL_X[skill];
		mazeh = Constants.SKILL_Y[skill];
		mazebuilder.build(this, mazew, mazeh, Constants.SKILL_ROOMS[skill], Constants.SKILL_PARTCT[skill]);
		// mazebuilder performs in a separate thread and calls back by calling newMaze() to return newly generated maze
		}
	
	 
	/**
	 * Call back method for MazeBuilder to communicate newly generated maze as reaction to a call to build()
	 * @param root node for traversals, used for the first person perspective
	 * @param cells encodes the maze with its walls and border
	 * @param dists encodes the solution by providing distances to the exit for each position in the maze
	 * @param startx current position, x coordinate
	 * @param starty current position, y coordinate
	 */
	public void newMaze(BSPNode root, Cells c, Distance dists, int startx, int starty) {
		if (Cells.deepdebugWall)
		{   // for debugging: dump the sequence of all deleted walls to a log file
			// This reveals how the maze was generated
			c.saveLogFile(Cells.deepedebugWallFileName);
		}
		// adjust internal state of maze model
		showMaze = showSolution = solving = false;
		mazecells = c ;
		mazedists = dists;
		seencells = new Cells(mazew+1,mazeh+1) ;
		rootnode = root ;
		setCurrentDirection(1, 0) ;
		setCurrentPosition(startx,starty) ;
		walkStep = 0;
		viewdx = dx<<16; 
		viewdy = dy<<16;
		angle = 0;
		mapMode = false;
		// set the current state for the state-dependent behavior
		//HHHEEERRRE WOOOOULLD BE A GOOOOOD PLLAACE TOO DOO THE DRIVER STUFF!!  WWOOOOOOO
		state = Constants.STATE_PLAY;
		
		if (generating != null){
			generating.handler.sendMessage(new Message()); 
		}
		cleanViews() ;
		// register views for the new maze
		// mazew and mazeh have been set in build() method before mazebuider was called to generate a new maze.
		// reset map_scale in mapdrawer to a value of 10
		addView(new FirstPersonDrawer(Constants.VIEW_WIDTH,Constants.VIEW_HEIGHT,
				Constants.MAP_UNIT,Constants.STEP_SIZE, mazecells, seencells, 10, mazedists.getDists(), mazew, mazeh, root, this)) ;
		// order of registration matters, code executed in order of appearance!
		addView(new MapDrawer(Constants.VIEW_WIDTH,Constants.VIEW_HEIGHT,Constants.MAP_UNIT,Constants.STEP_SIZE, mazecells, seencells, 10, mazedists.getDists(), mazew, mazeh, this)) ;

		// notify viewers
		//notifyViewerRedraw() ;
	}

	/////////////////////////////// Methods for the Model-View-Controller Pattern /////////////////////////////
	/**
	 * Register a view
	 */
	public void addView(Viewer view) {
		views.add(view) ;
	}
	/**
	 * Unregister a view
	 */
	public void removeView(Viewer view) {
		views.remove(view) ;
	}
	/**
	 * Remove obsolete FirstPersonDrawer and MapDrawer
	 */
	private void cleanViews() {
		// go through views and notify each one
		Iterator<Viewer> it = views.iterator() ;
		while (it.hasNext())
		{
			Viewer v = it.next() ;
			if ((v instanceof FirstPersonDrawer)||(v instanceof MapDrawer))
			{
				//System.out.println("Removing " + v);
				it.remove() ;
			}
		}

	}
	/**
	 * Notify all registered viewers to redraw their graphics IMPORTANT
	 */
	public void notifyViewerRedraw() {
		// go through views and notify each one
		Iterator<Viewer> it = views.iterator() ;
		while (it.hasNext())
		{
			Viewer v = it.next() ;
		   // viewers dr'aw on the buffer graphics
//			System.out.println(String.valueOf(state));
			if (state == Constants.STATE_PLAY){
				v.redraw(panel, state, px, py, viewdx, viewdy, walkStep, Constants.VIEW_OFFSET, rset, angle) ;
			}
		}
		
		if (state == Constants.STATE_PLAY){
			this.playActivity.mHandler.post(new Runnable() {
				public void run(){
					playActivity.update();
				}
			});
		}		
	}
	
	/** 
	 * Notify all registered viewers to increment the map scale
	 */
	public void notifyViewerIncrementMapScale() {
		// go through views and notify each one
		Iterator<Viewer> it = views.iterator() ;
		while (it.hasNext())
		{
			Viewer v = it.next() ;
			v.incrementMapScale() ;
		}
		// update the screen with the buffer graphics
		//panel.update() ;
	}
	/** 
	 * Notify all registered viewers to decrement the map scale
	 */
	public void notifyViewerDecrementMapScale() {
		// go through views and notify each one
		Iterator<Viewer> it = views.iterator() ;
		while (it.hasNext())
		{
			Viewer v = it.next() ;
			v.decrementMapScale() ;
		}
		// update the screen with the buffer graphics
		//panel.update() ;
	}
	////////////////////////////// get methods ///////////////////////////////////////////////////////////////
	boolean isInMapMode() { 
		return mapMode ; 
	} 
	boolean isInShowMazeMode() { 
		return showMaze ; 
	} 
	boolean isInShowSolutionMode() { 
		return showSolution ; 
	} 
	public String getPercentDone(){
		return String.valueOf(percentdone) ;
	}
	public MazePanel getPanel() {
		return this.panel ;
	}
	////////////////////////////// set methods ///////////////////////////////////////////////////////////////
	////////////////////////////// Actions that can be performed on the maze model ///////////////////////////
	private void setCurrentPosition(int x, int y)
	{
		px = x ;
		py = y ;
	}
	private void setCurrentDirection(int x, int y)
	{
		dx = x ;
		dy = y ;
	}
	
	
	void buildInterrupted() {
//		state = Constants.STATE_TITLE;
		notifyViewerRedraw() ;
		mazebuilder = null;
	}

	final double radify(int x) {
		return x*Math.PI/180;
	}


	/**
	 * Allows external increase to percentage in generating mode with subsequence graphics update
	 * @param pc gives the new percentage on a range [0,100]
	 * @return true if percentage was updated, false otherwise
	 */
	public boolean increasePercentage(int pc) {
		//System.out.println("inint");
		//put handler here!!
		if (percentdone < pc && pc < 100) {
			//System.out.println("conditional uno");
			percentdone = pc;
			if (state == Constants.STATE_GENERATING)
			{
				//System.out.println("conditional deus");
				generating.handler.post(new Runnable() {
					public void run(){
						generating.progressBar1.setProgress(percentdone);
					}
				});
			}
			else
				dbg("Warning: Receiving update request for increasePercentage while not in generating state, skip redraw.") ;
			return true ;
		}
		return false ;
	}

	



	/////////////////////// Methods for debugging ////////////////////////////////
	private void dbg(String str) {
		//System.out.println(str);
	}

	private void logPosition() {
		if (!deepdebug)
			return;
		dbg("x="+viewx/Constants.MAP_UNIT+" ("+
				viewx+") y="+viewy/Constants.MAP_UNIT+" ("+viewy+") ang="+
				angle+" dx="+dx+" dy="+dy+" "+viewdx+" "+viewdy);
	}
	///////////////////////////////////////////////////////////////////////////////

	/**
	 * Helper method for walk()
	 * @param dir
	 * @return true if there is no wall in this direction
	 */
	private boolean checkMove(int dir) {
		// obtain appropriate index for direction (CW_BOT, CW_TOP ...) 
		// for given direction parameter
		int a = angle/90;
		if (dir == -1)
			a = (a+2) & 3; // TODO: check why this works
		// check if cell has walls in this direction
		// returns true if there are no walls in this direction
		return mazecells.hasMaskedBitsFalse(px, py, Constants.MASKS[a]) ;
	}



	private void rotateStep() {
		angle = (angle+1800) % 360;
		viewdx = (int) (Math.cos(radify(angle))*(1<<16));
		viewdy = (int) (Math.sin(radify(angle))*(1<<16));
		moveStep();
	}

	private void moveStep() {
		notifyViewerRedraw() ;
		try {
			Thread.currentThread().sleep(25);
		} catch (Exception e) { }
	}

	private void rotateFinish() {
		setCurrentDirection((int) Math.cos(radify(angle)), (int) Math.sin(radify(angle))) ;
		logPosition();
		notifyViewerRedraw();
	}
	

	private void walkFinish(int dir) {
		setCurrentPosition(px + dir*dx, py + dir*dy) ;
		
		if (isEndPosition(px,py)) {
			state = Constants.STATE_FINISH;
			notifyViewerRedraw() ;
		}
		walkStep = 0;
		logPosition();
	}

	/**
	 * checks if the given position is outside the maze
	 * @param x
	 * @param y
	 * @return true if position is outside, false otherwise
	 */
	public boolean isEndPosition(int x, int y) {
		return x < 0 || y < 0 || x >= mazew || y >= mazeh;
	}



	synchronized public void walk(int dir) {
		if (!checkMove(dir)){
			notifyViewerRedraw();
			return;
		}
		for (int step = 0; step != 4; step++) {
			walkStep += dir;
			moveStep();
		}
		walkFinish(dir);
	}

	synchronized public void rotate(int dir) {
		final int originalAngle = angle;
		final int steps = 4;

		for (int i = 0; i != steps; i++) {
			angle = originalAngle + dir*(90*(i+1))/steps;
			rotateStep();
		}
		rotateFinish();
	}

 

	/**
	 * Method incorporates all reactions to keyboard input in original code, 
	 * after refactoring, Java Applet and Java Application wrapper call this method to communicate input.
	 * @throws Exception 
	 */
	public boolean keyDown(int key) throws Exception  {
		switch (state) {
		// if screen shows title page, keys describe level of expertise
		// create a maze according to the user's selected level
		case Constants.STATE_TITLE:

//		}
//			if (key >= '0' && key <= '9') {
//				build(key - '0');
//				break;
//			}
//			if (key >= 'a' && key <= 'f') {
//				build(key - 'a' + 10);
//				break;
//			} 
//			break;
			
	    // Make a new state STATE_CHOOSEY where we choose which robot driver to use, using key input.
		
		// if we are currently generating a maze, recognize interrupt signal (ESCAPE key)
		// to stop generation of current maze
		case Constants.STATE_GENERATING:
			if (key == ESCAPE) {
				mazebuilder.interrupt();
				buildInterrupted();
			}
			break;
		//case Constants.STATE_SOLVER
			
		// if user explores maze, 
		// react to input for directions and interrupt signal (ESCAPE key)	
		// react to input for displaying a map of the current path or of the overall maze (on/off toggle switch)
		// react to input to display solution (on/off toggle switch)
		// react to input to increase/reduce map scale
		case Constants.STATE_PLAY:
			
			switch (key) {
			case ESCAPE: case 65385:
				if (solving) 
					solving = false;
				else
					state = Constants.STATE_TITLE;
					notifyViewerRedraw() ;

				break;
			case 'k': case '8':
				//walk(1);
				if(keys == true){
				theThingThatShallBePassed = "UP";
				this.cryHavoc(theThingThatShallBePassed);
				}
				break;
			case 'h': case '4':
				//rotate(1);
				if(keys == true){
				theThingThatShallBePassed = "LEFT";
				this.cryHavoc(theThingThatShallBePassed);
				}
				break;
			case 'l': case '6':
				//rotate(-1);
				if(keys == true){
				theThingThatShallBePassed = "RIGHT";
				this.cryHavoc(theThingThatShallBePassed);
				}
				break;
			 case 'j': case '2':
				//walk(-1);
				//theThingThatShallBePassed = Event.DOWN;
				//this.cryHavoc(theThingThatShallBePassed);
				break;
			
			case ('w' & 0x1f): 
			{ 
				setCurrentPosition(px + dx, py + dy) ;
				notifyViewerRedraw() ;
				break;
			}
			case '\t': case 'm':
				mapMode = !mapMode; 		
				notifyViewerRedraw() ; 
				break;
			case 'z':
				showMaze = !showMaze; 		
				notifyViewerRedraw() ; 
				break;
			case 's':
				showSolution = !showSolution; 		
				notifyViewerRedraw() ;
				break;
			case ('s' & 0x1f):
				if (solving)
					solving = false;
				else {
					solving = true;
				}
			break;
			case '+': case '=':
			{
				notifyViewerIncrementMapScale() ;
				notifyViewerRedraw() ; // seems useless but it is necessary to make the screen update
				break ;
			}
			case '-':
				notifyViewerDecrementMapScale() ;
				notifyViewerRedraw() ; // seems useless but it is necessary to make the screen update
				break ;
			}
			break;
			
		// if we are finished, return to initial state with title screen	
		case Constants.STATE_FINISH:
			state = Constants.STATE_TITLE;
			notifyViewerRedraw() ;
			this.mazeRobot.setBatteryLevel(2500);
			break;
		case Constants.STATE_UNFINISH:
//			state = Constants.STATE_TITLE;
			notifyViewerRedraw() ;
			this.mazeRobot.setBatteryLevel(2500);
			this.mazeRobot.counter = 0;
			break;
		} 
		return true;
	}


    public void setRobot(Robot r){
    	this.mazeRobot = (BasicRobot) r;
    }
    public BasicRobot getRobot(){
    	return this.mazeRobot;
    }
     
    public void cryHavoc(String theThingThatShallBePassed2) throws Exception{
    	this.mazeRobot.thatWhichHasBeenPassed = theThingThatShallBePassed2;
    	this.mazeRobot.letSlipTheDogsOfWar(); 
    }
    public void setDifficulty(int i){
    	this.difficulty = i;
    }
    public void setMethod(int method){

    	if (1 == method){
			this.method = 1 ;
		}else if(2 == method){
			this.method = 2 ;
		}else if(0 == method){
			this.method = 0;
		}
    }
    public void setPlayActivity(PlayActivity playActivity){
    	this.playActivity = playActivity;
    }
    public void setBuilder(int builder){
    	this.method = builder;
    }
    public void setGenerating(GeneratingActivity genny){
    	this.generating = genny;
    }
    
}
