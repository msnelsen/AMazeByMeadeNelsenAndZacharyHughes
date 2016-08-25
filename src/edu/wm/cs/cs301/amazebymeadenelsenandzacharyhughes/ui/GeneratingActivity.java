package edu.wm.cs.cs301.amazebymeadenelsenandzacharyhughes.ui;


import java.io.File;
import java.util.TimerTask;

import edu.wm.cs.cs301.amazebymeadenelsenandzacharyhughes.R;
import edu.wm.cs.cs301.amazebymeadenelsenandzacharyhughes.R.id;
import edu.wm.cs.cs301.amazebymeadenelsenandzacharyhughes.R.layout;
import edu.wm.cs.cs301.amazebymeadenelsenandzacharyhughes.R.menu;
import edu.wm.cs.cs301.amazebymeadenelsenandzacharyhughes.falstad.BasicRobot;
import edu.wm.cs.cs301.amazebymeadenelsenandzacharyhughes.falstad.Distance;
import edu.wm.cs.cs301.amazebymeadenelsenandzacharyhughes.falstad.Gambler;
import edu.wm.cs.cs301.amazebymeadenelsenandzacharyhughes.falstad.GlobalVariables;
import edu.wm.cs.cs301.amazebymeadenelsenandzacharyhughes.falstad.ManualDriver;
import edu.wm.cs.cs301.amazebymeadenelsenandzacharyhughes.falstad.Maze;
import edu.wm.cs.cs301.amazebymeadenelsenandzacharyhughes.falstad.MazeFileReader;
import edu.wm.cs.cs301.amazebymeadenelsenandzacharyhughes.falstad.MazeFileWriter;
import edu.wm.cs.cs301.amazebymeadenelsenandzacharyhughes.falstad.RobotDriver;
import edu.wm.cs.cs301.amazebymeadenelsenandzacharyhughes.falstad.Tremaux;
import edu.wm.cs.cs301.amazebymeadenelsenandzacharyhughes.falstad.UnsuitableRobotException;
import edu.wm.cs.cs301.amazebymeadenelsenandzacharyhughes.falstad.WallFollower;
import edu.wm.cs.cs301.amazebymeadenelsenandzacharyhughes.falstad.Wizard;
//import falstad.MazePanel;
import android.support.v7.app.ActionBarActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

public class GeneratingActivity extends ActionBarActivity {
	
	public final static String EXTRA_MESSAGE4 = "edu.wm.cs.cs301.amazebymeadenelsenandzacharyhughes.MESSAGE4";
	Maze maze;
	//KeyListener kl ;
	BasicRobot robot = new BasicRobot();
	String generationMethod = new String(); 
	String navigationMethod = new String(); 
	String saveMaze = new String();

	
	//Figure out how to implement the drivers.  From here??
	RobotDriver robotDriver;
//	MazePanel appPanel ; 
	
	public ProgressBar progressBar1; 
	Thread thread; 
	//This is a really stupid thing to have to include, but necessary for 
	//properly handling handlers
	GeneratingActivity generating = this; 
	public Handler handler = new Handler();
		
//		@Override 
//		public void handleMessage(Message message){
//			try {
//				generating.startMaze();
//			} catch (Exception e) {
//				e.printStackTrace();
//			} 
//		}
//	}; 
	private int progress = 0; 
	private Button button1;
	int difficulty;
	
	/**
	 * Takes in @param savedInstanceState. Initializes the activity and 
	 * sets the content view, defining the UI. 
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		GlobalVariables g = GlobalVariables.getInstance(); 
//		g.setMaze(maze);
		setContentView(R.layout.activity_generating);
		maze = ((Maze)getApplicationContext());
		Intent intentIn = getIntent();
		Bundle extras = intentIn.getExtras();
		navigationMethod = extras.getString(AMazeActivity.EXTRA_MESSAGE);
		generationMethod = extras.getString(AMazeActivity.EXTRA_MESSAGE2);
		difficulty = Integer.parseInt(extras.getString(AMazeActivity.EXTRA_MESSAGE3));
		this.maze.setDifficulty(this.difficulty);
		saveMaze = extras.getString(AMazeActivity.EXTRA_MESSAGE5);
		//maze = (Maze) getIntent().getSerializableExtra(AMazeActivity.EXTRA_MESSAGE4);
		maze.setGenerating(this);
		initializeVariables(); 
		
		
		
		//insert java here
		
		if ("Prim".equalsIgnoreCase(generationMethod)){
			System.out.println("MazeApplication: generating random maze with Prim's algorithm");
			maze.setBuilder(1) ;
			//this.appPanel = maze.panel; 
			//maze.init() ;
		}else if("AldousBroder".equalsIgnoreCase(generationMethod)){
			System.out.println("MazeApplication: generating random maze with AldousBroder's algorithm");
			maze.setBuilder(2) ;
			//this.appPanel = maze.panel; 
			//maze.init() ;
		}else if("DFS".equalsIgnoreCase(generationMethod)){
			System.out.println("MazeApplication: generating a determinate AldousBroder maze");
			maze.setBuilder(0);
			//maze.init();
		}else if("From File".equalsIgnoreCase(generationMethod)){
			Context readingContext = this.maze; 
		//	System.out.println("The mazes difficulty in from file" + this.maze.difficulty);
			File f = new File(readingContext.getFilesDir() + "maze" + String.valueOf(this.difficulty) + ".xml") ;
			//File f2 = new File("/AMazeByMeadeNelsenAndZacharyHughes/src/edu/wm/cs/cs301/amazebymeadenelsenandzacharyhughes/falstad");
			String parameter = readingContext.getFilesDir() + "maze" + String.valueOf(this.difficulty) + ".xml";
			//System.out.println("Here");
//			System.out.print("Testing whether the read file exists: "); 
//			System.out.println(f.exists());
//			System.out.println(f.getAbsoluteFile().exists());
//			System.out.print("Testing whether the file is readable: "); 
//			System.out.println(f.canRead());
			if (f.exists() && f.canRead())
			{
				//System.out.println("HERRREE");
				System.out.println("MazeApplication: loading maze from file: " + parameter);
				// TODO: adjust this to mazeview
				//maze = new Maze() ;
				maze.init();
				MazeFileReader mfr = new MazeFileReader(parameter) ;
				maze.mazeh = mfr.getHeight() ;
				maze.mazew = mfr.getWidth() ;
				Distance d = new Distance(mfr.getDistances()) ;
				maze.newMaze(mfr.getRootNode(),mfr.getCells(),d,mfr.getStartX(), mfr.getStartY()) ;
				//robot = new BasicRobot();
				try {
					this.ender();
				} catch (UnsuitableRobotException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("At the end of all things");
			
		}
		}
		try {
			if(!("From File".equalsIgnoreCase(generationMethod))){
				System.out.println("start maze called");
			this.startMaze();
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		

		button1.setVisibility(View.VISIBLE);

	}
	/**
	 * Used in creating an accurate representation of the loading bar
	 * @throws UnsuitableRobotException
	 */
	public void ender() throws UnsuitableRobotException{
		System.out.println(this.navigationMethod);
		//System.out.println("BRROOOOOOOOOOOO !!!OO OOOO  BRO BRO BRO");
        if(this.navigationMethod.equals("Wizard")){
   
        	this.robotDriver = new Wizard();
        }else if(this.navigationMethod.equals("Gambler")){
        	this.robotDriver = new Gambler();
        	
        }else if(this.navigationMethod.equals( "Tremaux")){
        	this.robotDriver = new Tremaux();
        	//System.out.println("driver CREATED BRO");
        	
        }else if(this.navigationMethod.equals( "Manual")){
        //	System.out.println("We made it here");
        	this.robotDriver = new ManualDriver();
            this.robotDriver.setRobot(robot);
            this.robot.setDriver(this.robotDriver);
        	
        }else if(this.navigationMethod.equals("Wall-Follower")){
        	this.robotDriver = new WallFollower();
        	
        }
       	 
        this.robot.setMaze(maze);
        this.robot.setBatteryLevel(2500);

		this.maze.setRobot(robot);
		this.robotDriver.setRobot(robot);
		//System.out.println("Domo arigato");
        
        this.progressBar1.setProgress(100); 
        //System.gc();

	}

	/**
	 * Takes in @param menu. Defines the utility of the action bar. 
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.generating, menu);
		return true;
	}

	/**
	 * Takes in @param item. Defines how item clicks to the action bar are handled.
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * Initializes the progress bar.  
	 */
    private void initializeVariables() {
        progressBar1 = (ProgressBar)findViewById(R.id.progressBar1); 
        progressBar1.setMax(100);
        button1 = (Button)findViewById(R.id.button1);
        button1.setVisibility(View.INVISIBLE);
        }
    
    /**
     * Takes in @param view. Establishes intent, outputs to LogCat, and 
     * transfers to the next activity.
     */
    public void hitTheButton(View view) {
//    	if (maze.mazebuilder.buildThread.isAlive()){
//        	Log.v("Generating Activity", "Builder Not Finished Yet");
//        	return;
//        }
    	
		Log.w("AMazeBy<MeadeNelsenandZacharyHughes", "Begin playing maze");
    	Intent intentIn = getIntent();
    	Bundle extras = intentIn.getExtras();
    	String message1 = extras.getString(AMazeActivity.EXTRA_MESSAGE);
    	String message2 = extras.getString(AMazeActivity.EXTRA_MESSAGE2);
    	String message3 = extras.getString(AMazeActivity.EXTRA_MESSAGE3);
    	
    	Bundle extras2 = new Bundle();
    	
    	extras2.putString(AMazeActivity.EXTRA_MESSAGE, message1);
		extras2.putString(AMazeActivity.EXTRA_MESSAGE2, message2);
		extras2.putString(AMazeActivity.EXTRA_MESSAGE3, message3);
		
		
    	
		Intent intentOut = new Intent(this, PlayActivity.class);
		intentOut.putExtras(extras2);
		//intentOut.putExtra(EXTRA_MESSAGE4, maze); 

		startActivity(intentOut);
		
    }
    
    
    /**
     * Overrides the back button so that it returns the user to the beginning 
     * activity, as per the project specifications. 
     */
    @Override
	public void onBackPressed(){
	    Intent intent = new Intent(this, AMazeActivity.class);
	    startActivity(intent);
	    super.onBackPressed();
	}
    
    /**
     * Sets this Generating Activity's maze to @param m
     * @param m
     */
    
    public void setMaze(Maze m){
		this.maze = m;
	}
    
    /**
     * Starts the creation of the maze based off of passed in parameters
     * @throws Exception
     */
	public void startMaze() throws Exception{
		
		if( this.generationMethod == "DFS" ){
			   this.maze.setMethod(0);
		}else if (this.generationMethod == "Prim's"){
				this.maze.setMethod(1);
		}else if(this.generationMethod == "Aldous-Broder"){
				this.maze.setMethod(2);
		}
		this.maze.init();

        this.maze.build(this.difficulty, this.saveMaze);
        	
       
//        System.out.println(this.navigationMethod);
//        if(this.navigationMethod == "Wizard"){
//        	this.robotDriver = new Wizard();
//        }else if(this.navigationMethod == "Gambler"){
//        	this.robotDriver = new Gambler();
//        	
//        }else if(this.navigationMethod == "Tremaux"){
//        	this.robotDriver = new Tremaux();
//        	
//        }else if(this.navigationMethod == "Manual Driver"){
//        	System.out.println("We made it here");
//        	this.robotDriver = new ManualDriver();
//            this.robotDriver.setRobot(robot);
//            this.robot.setDriver(this.robotDriver);
//        	
//        }else if(this.navigationMethod == "Wall Follower"){
//        	this.robotDriver = new WallFollower();
//        	
//        }
//       	 
//        this.robot.setMaze(maze);
////		try {
////			this.robotDriver.setRobot(robot);
////		} catch (UnsuitableRobotException e) {
////			e.printStackTrace();
////		}
//		this.maze.setRobot(robot);
//		this.robotDriver.setRobot(robot);
////		this.robot.setDriver(robotDriver);
//		if(this.navigationMethod != "Manual Driver"){
//			this.robotDriver.drive2Exit();
//		}
		
	}
}


	
