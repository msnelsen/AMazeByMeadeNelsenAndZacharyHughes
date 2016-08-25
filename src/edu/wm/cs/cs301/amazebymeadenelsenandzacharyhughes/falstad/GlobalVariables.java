package edu.wm.cs.cs301.amazebymeadenelsenandzacharyhughes.falstad;

public class GlobalVariables {
	
	private static GlobalVariables instance; 
	
	Maze globalMaze; 
	private GlobalVariables(){}
	
	public void setMaze(Maze maze){
		this.globalMaze = maze; 
	}
	
	public Maze getMaze(){
		return this.globalMaze; 
}
	
	public static synchronized GlobalVariables getInstance(){
		if (instance == null){
			instance = new GlobalVariables(); 
		}
		
		return instance; 
	}
}
