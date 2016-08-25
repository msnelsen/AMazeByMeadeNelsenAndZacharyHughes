package edu.wm.cs.cs301.amazebymeadenelsenandzacharyhughes.ui;

import edu.wm.cs.cs301.amazebymeadenelsenandzacharyhughes.R; 
import edu.wm.cs.cs301.amazebymeadenelsenandzacharyhughes.R.id;
import edu.wm.cs.cs301.amazebymeadenelsenandzacharyhughes.R.layout;
import edu.wm.cs.cs301.amazebymeadenelsenandzacharyhughes.R.menu;
import edu.wm.cs.cs301.amazebymeadenelsenandzacharyhughes.falstad.BasicRobot;
import edu.wm.cs.cs301.amazebymeadenelsenandzacharyhughes.falstad.Gambler;
import edu.wm.cs.cs301.amazebymeadenelsenandzacharyhughes.falstad.GlobalVariables;
import edu.wm.cs.cs301.amazebymeadenelsenandzacharyhughes.falstad.Maze;
import edu.wm.cs.cs301.amazebymeadenelsenandzacharyhughes.falstad.MazePanel;
import edu.wm.cs.cs301.amazebymeadenelsenandzacharyhughes.falstad.Robot.Turn;
import edu.wm.cs.cs301.amazebymeadenelsenandzacharyhughes.falstad.RobotDriver;
import edu.wm.cs.cs301.amazebymeadenelsenandzacharyhughes.falstad.Tremaux;
import edu.wm.cs.cs301.amazebymeadenelsenandzacharyhughes.falstad.UnsuitableRobotException;
import edu.wm.cs.cs301.amazebymeadenelsenandzacharyhughes.falstad.WallFollower;
import edu.wm.cs.cs301.amazebymeadenelsenandzacharyhughes.falstad.Wizard;
import android.support.v7.app.ActionBarActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.app.ActionBar;


public class PlayActivity extends ActionBarActivity {
	
	
	public final static String EXTRA_MESSAGE4 = "edu.wm.cs.cs301.amazebymeadenelsenandzacharyhughes.MESSAGE4";

	String howlOfTheDamned;
	Thread thread;
	Handler handler;
	
	private ImageButton up; 
	private ImageButton down; 
	private ImageButton left; 
	private ImageButton right; 
	private Button pausePlay; 
	private Button skip; 
	public BasicRobot robot;
	public Maze maze;
	RobotDriver driver;
	Bitmap bitmap;
	Canvas canvas;
	MazePanel panel;
	RelativeLayout layout;
	public ProgressBar progressBar1; 
	public final Handler mHandler = new Handler();
	public Intent intentSpecial;
	public Thread t;
	public boolean pausedFlag = false; 
	public boolean shouldQuit = false; 
	MediaPlayer mp;
	public Drawable floor , ceilingImage1, ceilingImage2, ceilingImage3, ceilingImage4;
	public Bitmap fillBMP;
	//New runnable for posting to the UI thread
//	final Runnable mUpdateResults = new Runnable(){
//		public void run() {
//			this.maze.notifyViewerRedraw();
//		}
//	};
	/**
	 * Takes in @param savedInstanceState. Initializes the activity and 
	 * sets the content view, defining the UI. 
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mp = MediaPlayer.create(getApplicationContext(), R.raw.accordyin);
		mp.start();
		this.intentSpecial =  new Intent(this, FinishActivity.class);
		
//		GlobalVariables g = GlobalVariables.getInstance(); 
//		this.maze = g.getMaze(); 
		this.maze = ((Maze)getApplicationContext());
		this.maze.setPlayActivity(this);
		this.panel = maze.getPanel();
		this.panel.setPlayActivity(this);
		this.panel.setMaze(this.maze);
		
		
		setContentView(R.layout.activity_play);
		Intent intentIn = getIntent();
		Bundle extras = intentIn.getExtras();
		//maze = (Maze) getIntent().getSerializableExtra(GeneratingActivity.EXTRA_MESSAGE4);
		
		String message = extras.getString(AMazeActivity.EXTRA_MESSAGE);
		System.out.println(message);
		this.floor =  maze.getResources().getDrawable(R.drawable.canvas);
		this.ceilingImage1 =  maze.getResources().getDrawable(R.drawable.starry1c);
		this.ceilingImage2 =  maze.getResources().getDrawable(R.drawable.starry2c);
		this.ceilingImage3 =  maze.getResources().getDrawable(R.drawable.starry3c);
		this.ceilingImage4 =  maze.getResources().getDrawable(R.drawable.starry4c);
		fillBMP = BitmapFactory.decodeResource(maze.getResources(), R.drawable.brushstrokes);  
		if (this.floor == null){
			System.out.println("The floor is lava!");
		}
		
		initializeVariables(); 
		
		if (message.equals("Manual")){
			pausePlay.setVisibility(View.GONE);
		
		
		}else{
			up.setVisibility(View.GONE);
			down.setVisibility(View.GONE);
			left.setVisibility(View.GONE);
			right.setVisibility(View.GONE);
		}

		robot = this.maze.mazeRobot;
		if (this.robot == null){
			System.out.println("The robot is null"); 
		}
		if (this.robot.driver == null){
			System.out.println("The driver is null"); 
		}
		driver = this.robot.driver;
		//robot.setBatteryLevel(30);
		makeGraphics();
	
		this.maze.notifyViewerRedraw();
		if(message.equals("Tremaux")){
			this.startTremaux();
	} if(message.equals("Wizard")){
		this.startWizard();
	} if(message.equals("Gambler")){
		this.startGambler();
	} if(message.equals("Wall-Follower")){
		this.startWallFollower();
	}
	}
	public void startWallFollower(){
		driver = new WallFollower();
		driver.setPlayActivity(this);
		
		try {
			driver.setRobot(this.robot);
		} catch (UnsuitableRobotException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

			t = new Thread(){
				public void run(){
			try {
				driver.drive2Exit();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				}
				};
				t.start();
				

			maze.notifyViewerRedraw();
			progressBar1.setProgress((int)this.maze.mazeRobot.getBatteryLevel());

	}
	public void startGambler(){
		driver = new Gambler();
		driver.setPlayActivity(this);
		
		try {
			driver.setRobot(this.robot);
		} catch (UnsuitableRobotException e1) {
			e1.printStackTrace();
		}

			t = new Thread(){
				public void run(){
			try{
				driver.drive2Exit();
			} 
			catch (Exception e){
				e.printStackTrace();
			}
				}
				};
				t.start();
			maze.notifyViewerRedraw();

	}
	
	public void startWizard(){
		driver = new Wizard();
		driver.setPlayActivity(this);
		
		try {
			driver.setRobot(this.robot);
		} catch (UnsuitableRobotException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

			t = new Thread(){
				public void run(){
			try {
				driver.drive2Exit();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				}
				};
				t.start();


			maze.notifyViewerRedraw();

	}
	
	public void startTremaux(){
		driver = new Tremaux();
		driver.setPlayActivity(this);
		
		try {
			driver.setRobot(this.robot);
		} catch (UnsuitableRobotException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

			t = new Thread(){
				public void run(){
			try {
				driver.drive2Exit();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				}
				};
				t.start();
				
			maze.notifyViewerRedraw();
			try {
				if(this.maze.isEndPosition(this.maze.px, this.maze.py) || this.robot.isAtGoal()){
				Log.w("AMazeBy<MeadeNelsenandZacharyHughes", "At the Goal");
				Intent intent = new Intent(this, FinishActivity.class);
				//intent.putExtra(EXTRA_MESSAGE4, maze); 
				startActivity(intent);
				System.out.println(robot.batteryLevel);
				
				}
				if(robot.getBatteryLevel() - 5 < 0){
					System.out.println("battery level issue");
					Intent intent = new Intent(this, FinishActivity.class);
					startActivity(intent);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("Exception");
				Intent intent = new Intent(this, FinishActivity.class);
				startActivity(intent);
			}

	}
		

	
	
	/**
	 * Handles the creation of the graphics, linking the play activity to the 
	 * appropriate maze panel. 
	 */
	public void makeGraphics(){
		bitmap = Bitmap.createBitmap(400, 400, Bitmap.Config.ARGB_8888);
		canvas = new Canvas(bitmap); 
		this.panel.setCanvas(canvas);
		this.panel.setPlayActivity(this); 
		layout = (RelativeLayout) findViewById(R.id.playout);
		layout.setBackground(new BitmapDrawable(getResources(), bitmap));
	}
	
	public void update(){
		
		layout.setBackground(new BitmapDrawable(getResources(), bitmap));
	}
	


	/**
	 * Takes in @param menu. Defines the utility of the action bar. 
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.play, menu);
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
		else if(id == R.id.visible){
			Log.w("AMazeBy<MeadeNelsenandZacharyHughes", "Visible Walls Selected");
            Toast.makeText(getApplicationContext(), "Toggle Visible Walls", Toast.LENGTH_SHORT).show();
            this.maze.showMaze = !this.maze.showMaze;
            this.maze.notifyViewerRedraw();

		}
		else if(id == R.id.solution){
			Log.w("AMazeBy<MeadeNelsenandZacharyHughes", "Show Solution Selected");
            Toast.makeText(getApplicationContext(), "Toggle Solution", Toast.LENGTH_SHORT).show();
            
            this.maze.showSolution = !this.maze.showSolution;
            this.maze.notifyViewerRedraw();
            

		}
		else if(id == R.id.topdown){
			Log.w("AMazeBy<MeadeNelsenandZacharyHughes", "Top-Down View Selected");
            Toast.makeText(getApplicationContext(), "Toggle Top-Down View", Toast.LENGTH_SHORT).show();
            this.maze.mapMode = !this.maze.mapMode;
            this.maze.notifyViewerRedraw();

		}
		else if(id == R.id.zoomin){
			Log.w("AMazeBy<MeadeNelsenandZacharyHughes>", "Zoomed in on the Map");
			Toast.makeText(getApplicationContext(), "Zoomed In", Toast.LENGTH_SHORT).show();
			this.maze.notifyViewerIncrementMapScale();
			this.maze.notifyViewerRedraw();
		}
		else if(id == R.id.zoomout){
			Log.w("AMazeBy<MeadeNelsenandZacharyHughes>", "Zoomed out of the Map");
			Toast.makeText(getApplicationContext(), "Zoomed Out", Toast.LENGTH_SHORT).show();
			this.maze.notifyViewerDecrementMapScale();
			this.maze.notifyViewerRedraw();
		}
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * Takes in @param view. Notifies LogCat that the up button has been pushed. 
	 */
	public void up(View view){
		this.howlOfTheDamned = "UP";
		try {
			this.maze.cryHavoc(this.howlOfTheDamned);
			progressBar1.setProgress((int)this.maze.mazeRobot.getBatteryLevel());
		} catch (Exception e) {
			System.out.println("Up error");
			e.printStackTrace();
		}
		try {
			if(this.maze.isEndPosition(this.maze.px, this.maze.py)){
			Log.w("AMazeBy<MeadeNelsenandZacharyHughes", "At the Goal");
			Intent intent = new Intent(this, FinishActivity.class);
			//intent.putExtra(EXTRA_MESSAGE4, maze); 
			startActivity(intent);
			System.out.println(robot.batteryLevel);
			
			}
			if(robot.getBatteryLevel() - 5 < 0){
				System.out.println("battery level issue");
				Intent intent = new Intent(this, FinishActivity.class);
				startActivity(intent);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Exception");
			Intent intent = new Intent(this, FinishActivity.class);
			startActivity(intent);
		}
		//this.maze.notifyViewerRedraw();
		Log.w("AMazeBy<MeadeNelsenandZacharyHughes", "Up");
	}
	
	/**
	 * Takes in @param view. Notifies LogCat that the down button has been pushed. 
	 */
	public void down(View view){
		this.howlOfTheDamned = "DOWN";
		try {
			this.maze.cryHavoc(this.howlOfTheDamned);
			progressBar1.setProgress((int)this.maze.mazeRobot.getBatteryLevel());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			if(this.maze.isEndPosition(this.maze.px, this.maze.py)){
			Log.w("AMazeBy<MeadeNelsenandZacharyHughes", "At the Goal");
			Intent intent = new Intent(this, FinishActivity.class);
			//intent.putExtra(EXTRA_MESSAGE4, maze); 
			startActivity(intent);
			System.out.println(robot.batteryLevel);
			
			}
			if(robot.getBatteryLevel() - 6 < 0){
				System.out.println("battery level issue");
				Intent intent = new Intent(this, FinishActivity.class);
				startActivity(intent);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Exception");
			Intent intent = new Intent(this, FinishActivity.class);
			startActivity(intent);
		}
		//this.maze.notifyViewerRedraw();
		Log.w("AMazeBy<MeadeNelsenandZacharyHughes", "Down");
	}
	
	/**
	 * Takes in @param view. Notifies LogCat that the left button has been pushed. 
	 */
	public void left(View view){
		this.howlOfTheDamned = "LEFT";
		try {
			this.maze.cryHavoc(this.howlOfTheDamned);
			progressBar1.setProgress((int)this.maze.mazeRobot.getBatteryLevel());

		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			if(this.maze.isEndPosition(this.maze.px, this.maze.py)){
			Log.w("AMazeBy<MeadeNelsenandZacharyHughes", "At the Goal");
			Intent intent = new Intent(this, FinishActivity.class);
			//intent.putExtra(EXTRA_MESSAGE4, maze); 
			startActivity(intent);
			System.out.println(robot.batteryLevel);
			
			}
			if(robot.getBatteryLevel() - 3 < 0){
				System.out.println("battery level issue");
				Intent intent = new Intent(this, FinishActivity.class);
				startActivity(intent);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Exception");
			Intent intent = new Intent(this, FinishActivity.class);
			startActivity(intent);
		}
		//this.maze.notifyViewerRedraw();
		Log.w("AMazeBy<MeadeNelsenandZacharyHughes", "Left");
	}
	
	/**
	 * Takes in @param view. Notifies LogCat that the right button has been pushed. 
	 */
	public void right(View view){
		this.howlOfTheDamned = "RIGHT";
		try {
			this.maze.cryHavoc(this.howlOfTheDamned);
			progressBar1.setProgress((int)this.maze.mazeRobot.getBatteryLevel());

		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			if(this.maze.isEndPosition(this.maze.px, this.maze.py)){
			Log.w("AMazeBy<MeadeNelsenandZacharyHughes", "At the Goal");
			Intent intent = new Intent(this, FinishActivity.class);
			//intent.putExtra(EXTRA_MESSAGE4, maze); 
			startActivity(intent);
			System.out.println(robot.batteryLevel);
			
			}
			if(robot.getBatteryLevel() - 3 < 0){
				System.out.println("battery level issue");
				Intent intent = new Intent(this, FinishActivity.class);
				startActivity(intent);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Exception");
			Intent intent = new Intent(this, FinishActivity.class);
			startActivity(intent);
		}
		//this.maze.notifyViewerRedraw();
		Log.w("AMazeBy<MeadeNelsenandZacharyHughes", "Right");

	}
	
	/**
	 * Takes in @param view. Notifies LogCat that the skip button has been pushed, 
	 * then establishes intent and moves to another activity.
	 */
	public void finishMaze(View view){
		this.shouldQuit = true; 
		Log.w("AMazeBy<MeadeNelsenandZacharyHughes", "Ending Early");
		Intent intent = new Intent(this, FinishActivity.class);
		startActivity(intent);
	}
	
	/** 
	 * Takes in @param view. Notifies LogCat that the pause button has been pushed. 
	 */
	public void pausePlay(View view){
		mp.pause();
		Log.w("AMazeBy<MeadeNelsenandZacharyHughes", "Paused/Started");
		this.pausedFlag = !this.pausedFlag; 

	}
	
	/**
	 * Initializes the various buttons used in the play state. 
	 */
	public void initializeVariables(){
	    up = (ImageButton) findViewById(R.id.UP);
	    down = (ImageButton) findViewById(R.id.DOWN); 
	    left = (ImageButton) findViewById(R.id.LEFT); 
	    right = (ImageButton) findViewById(R.id.RIGHT);
	    pausePlay = (Button) findViewById(R.id.pauseplay); 
	    skip = (Button) findViewById(R.id.skip); 
	    pausePlay = (Button) findViewById(R.id.pauseplay); 
	    progressBar1 = (ProgressBar) findViewById(R.id.progressBar1); 
		this.progressBar1.setMax(2500);
		this.progressBar1.setProgress(2500);

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
	
	@Override
	public void onStop(){
		super.onStop();
		//t.interrupt();
		
		mp.stop();
		fillBMP.recycle();
		bitmap.recycle();
		System.gc();
		
	}
	
	public void uponEnding(){
		
		mp.stop();
		
		if(this.maze.mazeRobot.isAtGoal() /*|| this.maze.mazecells.isExitPosition(this.maze.px, this.maze.py)*/){
		Log.w("AMazeBy<MeadeNelsenandZacharyHughes", "At the Goal");
		Intent intent = new Intent(this, FinishActivity.class);
		//intent.putExtra(EXTRA_MESSAGE4, maze); 
		startActivity(intent);
		System.out.println(robot.batteryLevel);
		
		}
		if(robot.getBatteryLevel() - 5 < 0){
			System.out.println("battery level issue");
			Intent intent = new Intent(this, FinishActivity.class);
			startActivity(intent);
		}
		//mp.pause();
		}
	}
