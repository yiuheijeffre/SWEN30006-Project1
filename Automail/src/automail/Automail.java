package automail;

import simulation.IMailDelivery;

public class Automail {
	      
    public Robot[] robots;
    public MailPool mailPool;
    public int totalItemsDelivered;
    public double totalBillableActivity;
    public double totalServiceCost;
    public int totalLookupCount;
    public int totalFailures;
    
    public Automail(MailPool mailPool, IMailDelivery delivery, int numRobots) {  	
    	/** Initialize the MailPool */
    	
    	this.mailPool = mailPool;
    	
    	/** Initialize robots */
    	robots = new Robot[numRobots];
    	for (int i = 0; i < numRobots; i++) robots[i] = new Robot(delivery, mailPool, i);
    }
    
    public int getTotalItemsDelivered() {
    	totalItemsDelivered = 0;
    	for(Robot robot : robots) {
    		totalItemsDelivered += robot.getTotalDeliveryCounter();
    	}
    	return totalItemsDelivered;
    }
    
    public double getTotalBillableActivity() {
    	totalBillableActivity = 0;
    	for(Robot robot : robots) {
    		totalBillableActivity += robot.getTotalActivityUnit();
    	}
    	return totalBillableActivity;
    }
    
    public double getTotalServiceCost() {
    	totalServiceCost = 0;
    	for(Robot robot : robots) {
    		totalServiceCost += robot.getTotalServiceCost();
    	}
    	return totalServiceCost;
    }
    
    public int getTotalLookupCount() {
    	totalLookupCount = 0;
    	for(Robot robot : robots) {
    		totalLookupCount += robot.getTotalLookupCount();
    	}
    	return totalLookupCount;
    }
    
    public int getTotalFailures() {
    	totalFailures = 0;
    	for(Robot robot : robots) {
    		totalFailures += robot.getTotalFailures();
    	}
    	return totalFailures;
    }
    
    @Override
    public String toString() {
    	return String.format("Total number of items delivered: %d\nTotal billable activity: %f\n", this.getTotalItemsDelivered(), this.getTotalBillableActivity());
    }
}
