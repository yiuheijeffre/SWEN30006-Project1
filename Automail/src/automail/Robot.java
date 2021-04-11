package automail;

import exceptions.ExcessiveDeliveryException;
import exceptions.ItemTooHeavyException;
import simulation.Building;
import simulation.Clock;
import simulation.IMailDelivery;
import simulation.Simulation;

/**
 * The robot delivers mail!
 */
public class Robot {
	
    static public final int INDIVIDUAL_MAX_WEIGHT = 2000;
    static public final int FAIL = 0;
    static public final int SUCCESS = 1;

    IMailDelivery delivery;
    protected final String id;
    /** Possible states the robot can be in */
    public enum RobotState { DELIVERING, WAITING, RETURNING }
    public RobotState current_state;
    private int current_floor;
    private int destination_floor;
    private MailPool mailPool;
    private boolean receivedDispatch;
    
    private MailItem deliveryItem = null;
    private MailItem tube = null;
    
    private int deliveryCounter; //New
    private int totalDeliveryCounter;
    private double totalActivityUnit;
    private double totalServiceCost;
    private int totalLookupCount;
    private int totalFailures;
    private double serviceFee;
    private double activityUnit; //New
    

    /**
     * Initiates the robot's location at the start to be at the mailroom
     * also set it to be waiting for mail.
     * @param behaviour governs selection of mail items for delivery and behaviour on priority arrivals
     * @param delivery governs the final delivery
     * @param mailPool is the source of mail items
     */
    public Robot(IMailDelivery delivery, MailPool mailPool, int number){
    	this.id = "R" + number;
        // current_state = RobotState.WAITING;
    	current_state = RobotState.RETURNING;
        current_floor = Building.MAILROOM_LOCATION;
        this.delivery = delivery;
        this.mailPool = mailPool;
        this.receivedDispatch = false;
        this.deliveryCounter = 0;
        this.serviceFee = 0;
        this.activityUnit = 0; //New
        this.totalDeliveryCounter = 0; //New
        this.totalActivityUnit = 0; //TODO
        this.totalServiceCost = 0; //TODO
        this.totalLookupCount = 0; //TODO
        this.totalFailures = 0; //TODO
    }
    
    /**
     * This is called when a robot is assigned the mail items and ready to dispatch for the delivery 
     */
    public void dispatch() {
    	receivedDispatch = true;
    }

    /**
     * This is called on every time step
     * @throws ExcessiveDeliveryException if robot delivers more than the capacity of the tube without refilling
     */
    public void operate() throws ExcessiveDeliveryException {   
    	switch(current_state) {
    		/** This state is triggered when the robot is returning to the mailroom after a delivery */
    		case RETURNING:
    			/** If its current position is at the mailroom, then the robot should change state */
                if(current_floor == Building.MAILROOM_LOCATION){
                	if (tube != null) {
                		mailPool.addToPool(tube);
                        System.out.printf("T: %3d > old addToPool [%s]%n", Clock.Time(), tube.toString());
                        tube = null;
                	}
        			/** Tell the sorter the robot is ready */
        			mailPool.registerWaiting(this);
                	changeState(RobotState.WAITING);
                } else {
                	/** If the robot is not at the mailroom floor yet, then move towards it! */
                    moveTowards(Building.MAILROOM_LOCATION);
                    
                	break;
                }
    		case WAITING:
                /** If the StorageTube is ready and the Robot is waiting in the mailroom then start the delivery */
                if(!isEmpty() && receivedDispatch){
                	receivedDispatch = false;
                	deliveryCounter = 0; // reset delivery counter
                	setDestination();
                	changeState(RobotState.DELIVERING);
                }
                break;
    		case DELIVERING:
    			if(current_floor == destination_floor){ // If already here drop off either way
    				// serviceFee should be set by remoteLookup()
    				while (remoteLookup() == FAIL) {
    					totalFailures++;
    					activityUnit = activityUnit + 0.1;
    				}
                    /** Delivery complete, report this to the simulator! */
                    delivery.deliver(deliveryItem, activityUnit);
                    activityUnit = 0; //New
                    deliveryItem = null;
                    deliveryCounter++;
                    totalDeliveryCounter++; //New
                    if(deliveryCounter > 2){  // Implies a simulation bug
                    	throw new ExcessiveDeliveryException();
                    }
                    /** Check if want to return, i.e. if there is no item in the tube*/
                    if(tube == null){
                    	changeState(RobotState.RETURNING);
                    }
                    else{
                        /** If there is another item, set the robot's route to the location to deliver the item */
                        deliveryItem = tube;
                        tube = null;
                        setDestination();
                        changeState(RobotState.DELIVERING);
                    }
    			} else {
    				totalActivityUnit += 5; //New
	        		/** The robot is not at the destination yet, move towards it! */
	                moveTowards(destination_floor);
	                activityUnit = activityUnit + 5;
    			}
                break;
    	}
    }

    /**
     * Sets the route for the robot
     */
    private void setDestination() {
        /** Set the destination floor */
        destination_floor = deliveryItem.getDestFloor();
    }

    /**
     * Generic function that moves the robot towards the destination
     * @param destination the floor towards which the robot is moving
     */
    private void moveTowards(int destination) {
        if(current_floor < destination){
            current_floor++;
        } else {
            current_floor--;
        }
    }
    
    private String getIdTube() {
    	return String.format("%s(%1d)", this.id, (tube == null ? 0 : 1));
    }
    
    /**
     * Prints out the change in state
     * @param nextState the state to which the robot is transitioning
     */
    private void changeState(RobotState nextState){
    	assert(!(deliveryItem == null && tube != null));
    	if (current_state != nextState) {
            System.out.printf("T: %3d > %7s changed from %s to %s%n", Clock.Time(), getIdTube(), current_state, nextState);
    	}
    	current_state = nextState;
    	if(nextState == RobotState.DELIVERING){
            System.out.printf("T: %3d > %7s-> [%s]%n", Clock.Time(), getIdTube(), deliveryItem.toString());
    	}
    }

	public MailItem getTube() {
		return tube;
	}

	public boolean isEmpty() {
		return (deliveryItem == null && tube == null);
	}
	
	public boolean isEmptyTube() {
		return tube == null;
	}

	public void addToHand(MailItem mailItem) throws ItemTooHeavyException {
		assert(deliveryItem == null);
		deliveryItem = mailItem;
		if (deliveryItem.weight > INDIVIDUAL_MAX_WEIGHT) throw new ItemTooHeavyException();
	}

	public void addToTube(MailItem mailItem) throws ItemTooHeavyException {
		assert(tube == null);
		tube = mailItem;
		if (tube.weight > INDIVIDUAL_MAX_WEIGHT) throw new ItemTooHeavyException();
	}

	
	//New
	
	// performs a remote lookup to the BMS using the wifi modem. the robot should call until it gets
	// a successful lookup and increment total failures accordingly
	public int remoteLookup() {
		serviceFee = Simulation.performRemoteLookup(current_floor);
		return SUCCESS;
		
	}
	
	public int getTotalDeliveryCounter() {
		return this.totalDeliveryCounter;
	}
	
	public double getTotalActivityUnit() {
		return this.totalActivityUnit;
	}
	
	public double getTotalServiceCost() {
		return this.totalServiceCost;
	}
	
	public int getTotalLookupCount() {
		return this.totalLookupCount;
	}
	
	public int getTotalFailures() {
		return this.totalFailures;
	}
}
