package edu.wm.cs.cs301.amazebymeadenelsenandzacharyhughes.ui;

import edu.wm.cs.cs301.amazebymeadenelsenandzacharyhughes.R;
import edu.wm.cs.cs301.amazebymeadenelsenandzacharyhughes.R.id;
import edu.wm.cs.cs301.amazebymeadenelsenandzacharyhughes.R.layout;
import edu.wm.cs.cs301.amazebymeadenelsenandzacharyhughes.R.menu;
import edu.wm.cs.cs301.amazebymeadenelsenandzacharyhughes.falstad.Maze;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import android.widget.AdapterView.*;
public class AMazeActivity extends ActionBarActivity{
	
	public final static String EXTRA_MESSAGE = "edu.wm.cs.cs301.amazebymeadenelsenandzacharyhughes.MESSAGE";
	public final static String EXTRA_MESSAGE2 = "edu.wm.cs.cs301.amazebymeadenelsenandzacharyhughes.MESSAGE2";
	public final static String EXTRA_MESSAGE3 = "edu.wm.cs.cs301.amazebymeadenelsenandzacharyhughes.MESSAGE3";
	public final static String EXTRA_MESSAGE4 = "edu.wm.cs.cs301.amazebymeadenelsenandzacharyhughes.MESSAGE4";
	public final static String EXTRA_MESSAGE5 = "edu.wm.cs.cs301.amazebymeadenelsenandzacharyhughes.MESSAGE5";
 	private SeekBar seekBar; 
	private TextView textView; 
	private Spinner generationSpinner; 
	private Spinner navigationSpinner; 
	private CheckBox saveBox;
	Maze maze; 

	
	/**
	 * Takes in @param savedInstanceState. Initializes the activity and 
	 * sets the content view, defining the UI. 
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		System.gc();
		setContentView(R.layout.activity_main);
		initializeVariables(); 
		
		generationSpinner.setOnItemSelectedListener(new OnItemSelectedListener(){
			
			/**
			 * Defines what to do when an item is selected. Takes in @param parent, @param view, 
			 * and @param id. Produces a toast notification and outputs to LogCat upon selection. 
			 */
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
	            Toast.makeText(getApplicationContext(), (CharSequence) generationSpinner.getSelectedItem(), Toast.LENGTH_SHORT).show();
				Log.w("AMazeBy<MeadeNelsenandZacharyHughes", String.valueOf(generationSpinner.getSelectedItem()));
				
				
			}
			
			/**
			 * Defines what to do when no item is selected. Takes in @param parent. Produces a 
			 * toast notification and outputs to LogCat upon selection. 
			 */
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				Log.w("AMazeBy<MeadeNelsenandZacharyHughes", "Nothing selected");
				generationSpinner.setSelection(1);
				
			}
			 
			
		});
		
		navigationSpinner.setOnItemSelectedListener(new OnItemSelectedListener(){
			
			
			/**
			 * Defines what to do when an item is selected. Takes in @param parent, @param view, 
			 * and @param id. Produces a toast notification and outputs to LogCat upon selection. 
			 */
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				Log.w("AMazeBy<MeadeNelsenandZacharyHughes", String.valueOf(navigationSpinner.getSelectedItem()));
	            Toast.makeText(getApplicationContext(), (CharSequence) navigationSpinner.getSelectedItem(), Toast.LENGTH_SHORT).show();				
			}
			
			/**
			 * Defines what to do when no item is selected. Takes in @param parent. Produces a 
			 * toast notification and outputs to LogCat upon selection. 
			 */
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				Log.w("AMazeBy<MeadeNelsenandZacharyHughes", "Nothing selected");
				generationSpinner.setSelection(0);
				
			}
			
		});

	    seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
	           int progress = 0;
//	          
	          /** 
	           * Takes in @param seekBar, @param progresValue, @param fromUser. Defines how to handle 
	           * changes in the progress of the seek bar. 
	           */
	          @Override
	          public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
	              progress = progresValue;
	              
	              //Toast.makeText(getApplicationContext(), String.valueOf(progress), Toast.LENGTH_SHORT).show();
	              
	             // Log.w("AMazeBy<MeadeNelsenandZacharyHughes>", "Changing difficulty" );
	          }

	          /**
	           * Takes in @param seekBar. Defines what happens when the user begins touching the 
	           * seek bar.
	           */
	          @Override
	          public void onStartTrackingTouch(SeekBar seekBar) {
	              //Toast.makeText(getApplicationContext(), "Started tracking seekbar", Toast.LENGTH_SHORT).show();
	          }

	          /**
	           * Takes in @param seekBar. Defines what happens when the user stops touching the 
	           * seek bar.
	           */
	          @Override
	          public void onStopTrackingTouch(SeekBar seekBar) {
	        	  Toast.makeText(getApplicationContext(), String.valueOf(progress), Toast.LENGTH_SHORT).show();
				  Log.w("AMazeBy<MeadeNelsenandZacharyHughes", String.valueOf(seekBar.getProgress()));

	          }
	       });
	     }

	
	/**
	 * Takes in @param menu. Defines the utility of the action bar. 
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/**
	 * Takes in @param item. Defines how item clicks to the action bar are handled.
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * 
	 * Takes in @param view. Handles the behavior of the button when it is clicked and 
	 * begins the maze. Also creates a message indicating whether the navigation is manual 
	 * or automatic. 
	 */
	public void startMaze(View view){
		Log.w("AMazeBy<MeadeNelsenandZacharyHughes", "Starting the maze");
		Intent intent = new Intent(this, GeneratingActivity.class);
		String message = String.valueOf(this.navigationSpinner.getSelectedItem());
		String message2 = String.valueOf(this.generationSpinner.getSelectedItem());
		String message3 = String.valueOf(this.seekBar.getProgress());
		String message5 = String.valueOf(this.saveBox.isChecked());
		Bundle extras = new Bundle();
		extras.putString(EXTRA_MESSAGE, message);
		extras.putString(EXTRA_MESSAGE2, message2);
		extras.putString(EXTRA_MESSAGE3, message3);
		extras.putString(EXTRA_MESSAGE5, message5);
		intent.putExtra(EXTRA_MESSAGE4, maze); 
		intent.putExtras(extras);
		startActivity(intent);
		System.gc();
		
	}
	
	/**
	 * Initializes the various bars, buttons, and other menu options that 
	 * need to be initialized for this activity to work. 
	 */
    private void initializeVariables() {
        seekBar = (SeekBar) findViewById(R.id.seekBar1);
        generationSpinner = (Spinner) findViewById(R.id.generationSpinner); 
        navigationSpinner = (Spinner) findViewById(R.id.navigationSpinner); 
        textView = (TextView) findViewById(R.id.textView1);
        saveBox = (CheckBox) findViewById(R.id.checkBox1);
    }
    
    /**
     * Handles what to do if back is pressed
     */
    @Override
   	public void onBackPressed(){
   	    Intent intent = new Intent(this, AMazeActivity.class);
   	    startActivity(intent);
   	    super.onBackPressed();
   	}



     

}