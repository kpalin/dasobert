package org.biojava.dasobert.dasregistry;
import java.io.PrintStream;

final class Timer
{
	private long	startTime;
	private long	endTime;

	public Timer()
	{
		reset();
	}

	public void start()
	{
		
		startTime =
			System.currentTimeMillis();
	}

	public void end()
	{
		
		endTime =
			System.currentTimeMillis();
	}

	public long duration()
	{
		return (endTime - startTime);
	}

	public void printDuration( PrintStream out )
	{
		long elapsedTimeInSecond =
			duration() / 1000;
		long remainderInMillis =
			duration() % 1000;

		out.println("\nTotal execution time:"
			+ elapsedTimeInSecond
			+ "."
			+ remainderInMillis
			+ " seconds");
	}

	public void reset()
	{
		startTime = 0;
		endTime = 0;
	}

	public static void main( String[] args )
	{
		Timer timer = new Timer();
		timer.start();

		for (int i = 0; i < 500; i++)
		{
			System.out.print("#");
		}

		timer.end();
		timer.printDuration(System.out);
	}
}
