package autonomous;

import java.util.ArrayList;

import org.usfirst.frc.team5199.robot.Robot;

import interfaces.AutFunction;

public class Multi implements AutFunction {
	private final ArrayList<AutFunction>[] threads;
	private final int[] indexes;
	private final boolean[] isDone;

	public Multi(int threadCount) {
		threads = new ArrayList[threadCount];
		indexes = new int[threadCount];
		isDone = new boolean[threadCount];

		for (int i = 0; i < threads.length; i++) {
			threads[i] = new ArrayList<AutFunction>();
		}

		Robot.nBroadcaster.println(threads[0]);
	}

	@Override
	public void update(long deltaTime) {
		for (int i = 0; i < threads.length; i++) {
			if (!isDone[i]) {
				AutFunction function = threads[i].get(indexes[i]);

				if (function.isDone()) {
					function.cleanUp();
					Robot.nBroadcaster.println("thread " + i + " " + function.getClass().getName() + " done.");
					indexes[i]++;
					if (indexes[i] >= threads[i].size()) {
						isDone[i] = true;
					} else {
						Robot.nBroadcaster.println("thread " + i + " " + function.getClass().getName() + " start.");
						threads[i].get(indexes[i]).init();
					}
				} else {
					function.update(deltaTime);
				}
			}
		}
	}

	public void add(int i, AutFunction function) {
		threads[i].add(function);
	}

	@Override
	public void init() {
		for (int i = 0; i < threads.length; i++) {
			ArrayList<AutFunction> functions = threads[i];
			if (functions.isEmpty()) {
				isDone[i] = true;
			} else {
				functions.get(0).init();
				Robot.nBroadcaster.println("thread " + i + " " + functions.get(0).getClass().getName() + " start.");
			}
		}
	}

	@Override
	public boolean isDone() {
		for (Boolean b : isDone) {
			if (!b) {
				return false;
			}
		}
		return true;
	}

	@Override
	public void cleanUp() {
		// TODO Auto-generated method stub

	}

}
