// W05 Team 05 [Wed 05:15PM]
package statistics;

public class RobotStatistics {
	
	private double totalActivityUnit;
    private double totalServiceCost;
    private int totalLookupCount;
    private int totalFailures;
    private int totalSuccesses;
    private int totalDeliveries;
    
    public RobotStatistics() {
        this.totalActivityUnit = 0;
        this.totalServiceCost = 0;
        this.totalLookupCount = 0;
        this.totalFailures = 0;
        this.totalSuccesses = 0;
        this.totalDeliveries = 0;
    }
    
    
    public int getTotalDeliveries() {
		return totalDeliveries;
	}
	public void setTotalDeliveries(int totalDeliveries) {
		this.totalDeliveries = totalDeliveries;
	}
	public double getTotalActivityUnit() {
		return totalActivityUnit;
	}
	public void setTotalActivityUnit(double totalActivityUnit) {
		this.totalActivityUnit = totalActivityUnit;
	}
	public double getTotalServiceCost() {
		return totalServiceCost;
	}
	public void setTotalServiceCost(double totalServiceCost) {
		this.totalServiceCost = totalServiceCost;
	}
	public int getTotalLookupCount() {
		return totalLookupCount;
	}
	public void setTotalLookupCount(int totalLookupCount) {
		this.totalLookupCount = totalLookupCount;
	}
	public int getTotalFailures() {
		return totalFailures;
	}
	public void setTotalFailures(int totalFailures) {
		this.totalFailures = totalFailures;
	}
	public int getTotalSuccesses() {
		return totalSuccesses;
	}

	public void incrementTotalServiceCost(double amount) {
		totalServiceCost+=amount;
	}
	public void incrementTotalActivityUnit(double amount) {
		totalActivityUnit+=amount;
	}
	
	public void incrementFailures() {
		totalFailures++;
	}
	
	public void incrementSuccesses() {
		totalSuccesses++;
	}
	
	public void incrementLookupCount() {
		totalLookupCount++;
	}
	
	public void incrementTotalDeliveries() {
		totalDeliveries++;
	}
	
	
}




