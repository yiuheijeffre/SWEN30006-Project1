// W05 Team 05 [Wed 05:15PM]
package automail;

public class DefaultMailChargeAdapter implements Charge {
	private double activityUnitPrice = 0.224;
	private double markupPercentage = 0.059;//Could be in a better place

	@Override
	public double calculateActivityCost(double activityUnits) {
		
		return activityUnits * this.activityUnitPrice;
	}
	
	public double getMarkupPercentage() {
		return this.markupPercentage;
	}

}