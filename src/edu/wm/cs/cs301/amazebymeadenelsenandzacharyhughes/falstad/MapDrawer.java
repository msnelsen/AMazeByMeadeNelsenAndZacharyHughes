/**
 * 
 */
package edu.wm.cs.cs301.amazebymeadenelsenandzacharyhughes.falstad;

import android.graphics.Canvas;



/**
 * This class encapsulates all functionality to draw a map of the overall maze, the set of visible walls, the solution.
 * The map is drawn on the screen in such a way that the current position remains at the center of the screen.
 * The current position is visualized as a red dot with an attached arc for its current direction.
 * The solution is visualized as a yellow line from the current position towards the exit of the map.
 * Walls set are currently visible in the first person view are drawn white, all other walls are drawn in grey.
 * 
 * This code is refactored code from Maze.java by Paul Falstad, www.falstad.com, Copyright (C) 1998, all rights reserved
 * Paul Falstad granted permission to modify and use code for teaching purposes.
 * Refactored by Peter Kemper
 *
 */
public class MapDrawer extends DefaultViewer {

	// keep local copies of values determined in Maze.java
	int view_width = 400;
	int view_height = 400;
	int map_unit = 128;
	int map_scale = 10 ;
	int step_size = map_unit/4;
	Cells mazecells ;
	Cells seencells ; 
	int[][] mazedists ;
	// width and height of map are chosen according to a user given skill level
	int mazew ; // width 
	int mazeh ; // height

	Maze maze;  
	MazePanel mazePanel;
	/**
	 * Constructor
	 * @param mazecells TODO
	 * @param seencells TODO
	 * @param map_scale TODO
	 * @param mazedists TODO
	 * @param mazew TODO
	 * @param mazeh TODO
	 */
	public MapDrawer(int width, int height, int map_unit, int step_size, Cells mazecells, Cells seencells, int map_scale, int[][] mazedists, int mazew, int mazeh, Maze maze){
		view_width = width ;
		view_height = height ;
		this.map_unit = map_unit ;
		this.step_size = step_size ;
		this.mazecells = mazecells ;
		this.seencells = seencells ;
		this.map_scale = map_scale ;
		this.mazedists = mazedists ;
		this.mazew = mazew ;
		this.mazeh = mazeh ;
		this.maze = maze ;
		mazePanel = maze.getPanel(); 
		
	}
	@Override
	public void incrementMapScale() {
		if (maze.isInMapMode())
			map_scale += 1 ;
	}
	@Override
	public void decrementMapScale() {
		if (maze.isInMapMode())
		{
			map_scale -= 1 ;
			if (1 > map_scale)
				map_scale = 1 ;
		}
	}
	//////////
	final int viewd_unscale(int x) {
		return x >> 16;
	}
	
	void dbg(String str) {
		System.out.println("MapDrawer:"+ str);
	}
	/**
	 * @deprecated Use {@link falstad.MazePanel#redrawMapDrawer(edu.wm.cs.cs301.amazebymeadenelsenandzacharyhughes.falstad.MapDrawer,Object,int,int,int,int,int,int,int,RangeSet,int)} instead
	 */
	@Override
	public void redraw(Canvas gc, int state, int px, int py,
			int view_dx, int view_dy, int walk_step, int view_offset, RangeSet rset, int ang) {

		
				mazePanel.redrawMapDrawer(this, gc, state, px, py, view_dx,
						view_dy, walk_step, view_offset, rset, ang);
			}
	/**
	 * Helper method for redraw_play, called if map_mode is true, i.e. the users wants to see the overall map.
	 * The map is drawn only on a small rectangle inside the maze area such that only a part of the map is actually shown.
	 * Of course a part covering the current location needs to be displayed.
	 * @param gc graphics handler to manipulate screen
	 * @deprecated Use {@link falstad.MazePanel#draw_map(edu.wm.cs.cs301.amazebymeadenelsenandzacharyhughes.falstad.MapDrawer,Graphics,int,int,int,int,int,boolean,boolean)} instead
	 */
	public void draw_map(Canvas gc, int px, int py, int walk_step, int view_dx, int view_dy, boolean showMaze, boolean showSolution) {
		mazePanel.draw_map(this, gc, px, py, walk_step, view_dx, view_dy,
				showMaze, showSolution);
	}
	/**
	 * Draws an oval red shape with and arrow for the current position and direction on the maze.
	 * It always reside on the center of the screen. The map drawing moves if the user changes location.
	 * @param gc
	 * @deprecated Use {@link falstad.MazePanel#draw_currentlocation(edu.wm.cs.cs301.amazebymeadenelsenandzacharyhughes.falstad.MapDrawer,Graphics,int,int)} instead
	 */
	public void draw_currentlocation(Object gc, int view_dx, int view_dy) {
		mazePanel.draw_currentlocation(this, gc, view_dx, view_dy);
	}
	
	/**
	 * Draws a yellow line to show the solution on the overall map. 
	 * Method is only called if in STATE_PLAY and map_mode and showSolution are true.
	 * Since the current position is fixed at the center of the screen, all lines on the map are drawn with some offset.
	 * @param gc to draw lines on
	 * @param offx
	 * @param offy
	 * @deprecated Use {@link falstad.MazePanel#draw_solution(edu.wm.cs.cs301.amazebymeadenelsenandzacharyhughes.falstad.MapDrawer,Graphics,int,int,int,int)} instead
	 */
	public void draw_solution(Canvas gc, int offx, int offy, int px, int py) {
		mazePanel.draw_solution(this, gc, offx, offy, px, py);
	}
	
	// same code as in Maze.java
	public int getDirectionIndexTowardsSolution(int x, int y, int d) {
		for (int n = 0; n < 4; n++) {
			if (mazecells.hasMaskedBitsTrue(x,y,Constants.MASKS[n]))
				continue;
				int dx = Constants.DIRS_X[n];
				int dy = Constants.DIRS_Y[n];
				int dn = mazedists[x+dx][y+dy];
				if (dn < d)
					return n ;
		}
		return 4 ;
	}

}
