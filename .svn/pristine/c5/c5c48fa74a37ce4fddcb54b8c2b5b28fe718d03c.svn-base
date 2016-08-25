package edu.wm.cs.cs301.amazebymeadenelsenandzacharyhughes.falstad;

import edu.wm.cs.cs301.amazebymeadenelsenandzacharyhughes.ui.PlayActivity;

/** Serves as an intermediary between RobotDriver and the newly implemented classes such as Gambler, 
 * WallFollower, etc. 
 */

public class AutoDriver implements RobotDriver {

	BasicRobot ottoVonRobot;
	int width, height;
	Distance distance;
	PlayActivity playActivity;
	int counter = 0;
	

	

	@Override
	public void setRobot(Robot r) throws UnsuitableRobotException {
		// TODO Auto-generated method stub
       this.ottoVonRobot = (BasicRobot) r;
	}

	@Override
	public void setDimensions(int width, int height) {
		// TODO Auto-generated method stub

		this.width = width;
		this.height = height;

	}

	@Override
	public void setDistance(Distance distance) {
		// TODO Auto-generated method stub
		

        this.distance = distance;

	}

	@Override
	public boolean drive2Exit() throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public float getEnergyConsumption() {
		// TODO Auto-generated method stub
		return 2500 - this.ottoVonRobot.getBatteryLevel();
		
	}

	@Override
	public int getPathLength() {
		// TODO Auto-generated method stub
		return this.ottoVonRobot.counter;
	}

	@Override
	public void setPlayActivity(PlayActivity PaPa) {
		// TODO Auto-generated method stub
		this.playActivity = PaPa;
		
	}


}
