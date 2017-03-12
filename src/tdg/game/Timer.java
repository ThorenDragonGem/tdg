package tdg.game;

public class Timer
{
	long startTime;

	public Timer()
	{
		reset();
	}

	public void reset()
	{
		startTime = System.nanoTime();
	}

	public double getElapsed()
	{
		return System.nanoTime() - startTime;
	}
}