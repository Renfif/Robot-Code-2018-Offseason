package pathTools;

import gfx.Display;
import networking.RobotNetworkInterface;
import path.Path;
import path.RecordedPaths;
import util.ClockRegulator;

public class PathTools {
	public static void main(String[] args) {
		ClockRegulator clockRegulator = new ClockRegulator(60);
		RobotNetworkInterface robotInterface = new RobotNetworkInterface(5801);
		Path path = RecordedPaths.scaleRtoR();

		robotInterface.start();

		Display display = new Display(robotInterface, path);

		while (true) {
			if (robotInterface.isNewPath()) {
				display.setPath(robotInterface.getPath());
			}

			display.update();
			clockRegulator.sync();
		}
	}
}
