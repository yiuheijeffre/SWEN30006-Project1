// W05 Team 05 [Wed 05:15PM]

package automail;

import simulation.IMailDelivery;
import statistics.StatisticsProvider;

public class Automail {
	      
    public Robot[] robots;
    public MailPool mailPool;
    public StatisticsProvider statsProvider;
    
    public Automail(MailPool mailPool, IMailDelivery delivery, int numRobots) {  	
    	/** Initialize the MailPool */
    	
    	this.mailPool = mailPool;
    	/** Initialize robots with current default mail charge adapter*/
    	robots = new Robot[numRobots];
    	for (int i = 0; i < numRobots; i++) robots[i] = new Robot(delivery, mailPool, i, new DefaultMailChargeAdapter());
    	this.statsProvider = new StatisticsProvider(robots);
    }
}
