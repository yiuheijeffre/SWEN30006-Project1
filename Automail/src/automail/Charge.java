package automail;

public interface Charge {
	public double calculateActivityCost(double activityUnits);
	public double getMarkupPercentage();// jank solution
}