package org.biojava.dasobert.dasregistry;

import java.util.Date;



/**
 * class to code timer functionality for the registry warnings for old leaseDates
 * @author jw12
 *
 */
public class RegistryTimer {
	private static int ARCHIVE_TIME=60;//60 days from invalid to deletion 
	int oneday=1000 * 60 * 60 * 24;
	int twodays = 1000 * 60 * 60 * 24 * 2;
	int warningOfDeletionTime= 1000 * 60 * 60 * 24 * RegistryTimer.ARCHIVE_TIME /2;//to show row as red as immenently going to be deleteds
	
	public boolean isActive(DasSource ds){
		boolean isActive=true;
	Date now = new Date();
	if (ds.getLeaseDate().getTime() < (now.getTime() - twodays)){
		isActive=false;
	}
	return isActive;
	}
	
	public int daysBeforeArchiving(DasSource ds){
		int daysLeft = 0;
	Date now = new Date();
	long timeSinceLeaseTime=now.getTime()-ds.getLeaseDate().getTime();
	int days=(int) (timeSinceLeaseTime/oneday);
	daysLeft=RegistryTimer.ARCHIVE_TIME-days;
	System.out.println("days left="+daysLeft);
	return daysLeft;
	}
		
}
