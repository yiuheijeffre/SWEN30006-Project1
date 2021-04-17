// W05 Team 05 [Wed 05:15PM]
package statistics;

import automail.Robot;

public class StatisticsProvider {
	private Robot[] robots;
	
	public StatisticsProvider(Robot[] robots) {
		this.robots = robots;
	}
	
	public int getTotalDeliveries() {
		int totalDeliveries = 0;
		for (Robot robot : robots) {
			totalDeliveries += robot.getRobotStats().getTotalDeliveries();
		}
		return totalDeliveries;
	}
	
	public double getTotalBillableActivity() {
		double totalBillableActivity = 0.0;
		for (Robot robot : robots) {
			totalBillableActivity += robot.getRobotStats().getTotalActivityUnit();
		}
//		we shouldn't charge the tenant for failed lookups as this is out of their control
		totalBillableActivity = totalBillableActivity - getTotalFailures()*0.1;
		return totalBillableActivity;
	}
	
	public double getTotalServiceCost() {
		double totalServiceCost = 0.0;
		for (Robot robot : robots) {
			totalServiceCost += robot.getRobotStats().getTotalServiceCost();
		}
		return totalServiceCost;
	}
	
	public int getTotalLookupCount() {
		int totalLookupCount = 0;
		for (Robot robot : robots) {
			totalLookupCount += robot.getRobotStats().getTotalLookupCount();
		}
		return totalLookupCount;
	}
	
	public int getTotalFailures() {
		int totalFailures = 0;
		for (Robot robot : robots) {
			totalFailures += robot.getRobotStats().getTotalFailures();
		}
		return totalFailures;
	}
	
	public int getTotalSuccesses() {
		int totalSuccesses = 0;
		for (Robot robot : robots) {
			totalSuccesses += robot.getRobotStats().getTotalSuccesses();
		}
		return totalSuccesses;
	}
	
	public double getTotalActivityCost() {
		double totalActivityCost = 0.0;
		for (Robot robot : robots) {
			totalActivityCost += robot.getMailChargeAdapter().calculateActivityCost(robot.getRobotStats().getTotalActivityUnit());
		}
		return totalActivityCost;
	}
}