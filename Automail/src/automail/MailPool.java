package automail;

import java.util.LinkedList;
import java.util.Comparator;
import java.util.ListIterator;

import exceptions.ItemTooHeavyException;

/**
 * addToPool is called when there are mail items newly arrived at the building to add to the MailPool or
 * if a robot returns with some undelivered items - these are added back to the MailPool.
 * The data structure and algorithms used in the MailPool is your choice.
 * 
 */
public class MailPool {

	private class Item {
		int destination;
		MailItem mailItem;
		// Use stable sort to keep arrival time relative positions
		
		public Item(MailItem mailItem) {
			destination = mailItem.getDestFloor();
			this.mailItem = mailItem;
		}
	}
	
	public class ItemComparator implements Comparator<Item> {
		@Override
		public int compare(Item i1, Item i2) {
			int order = 0;
			if (i1.destination < i2.destination) {
				order = 1;
			} else if (i1.destination > i2.destination) {
				order = -1;
			}
			return order;
		}
	}
	
	private LinkedList<Item> pool;
	private LinkedList<Item> priorityPool;
	private LinkedList<Item> testPool;
	private LinkedList<Item> testPriorityPool;
	private LinkedList<Robot> robots;
	private double chargeThreshold;

	public MailPool(int nrobots, double chargeThreshold){
		// Start empty
		pool = new LinkedList<Item>();
		priorityPool = new LinkedList<Item>();
		testPool = new LinkedList<Item>();
		testPriorityPool = new LinkedList<Item>();
		robots = new LinkedList<Robot>();
		this.chargeThreshold = chargeThreshold;
	}

	/**
     * Adds an item to the mail pool
     * @param mailItem the mail item being added.
     */
	public void addToPool(MailItem mailItem) {
		
		Item item = new Item(mailItem);
		if(mailItem.getEstimatedCharge() > chargeThreshold) { //New
			priorityPool.add(item);
			priorityPool.sort(new ItemComparator());
			testPriorityPool.add(item);
			testPriorityPool.sort(new ItemComparator());
		}
		else {
			pool.add(item);
			pool.sort(new ItemComparator());
			testPool.add(item);
			testPool.sort(new ItemComparator());
		}
//		pool.add(item);
//		pool.sort(new ItemComparator());
	}
	
	
	
	/**
     * load up any waiting robots with mailItems, if any.
     */
	public void loadItemsToRobot() throws ItemTooHeavyException {
		//List available robots
		ListIterator<Robot> i = robots.listIterator();
		while (i.hasNext()) loadItem(i);
	}
	
	//load items to the robot
	private void loadItem(ListIterator<Robot> i) throws ItemTooHeavyException {
		Robot robot = i.next();
		assert(robot.isEmpty());
		// System.out.printf("P: %3d%n", pool.size());
		ListIterator<Item> j = pool.listIterator();
		ListIterator<Item> p = priorityPool.listIterator(); //New
		if(priorityPool.size() > 0) {
			try {
			robot.addToHand(p.next().mailItem);
			p.remove();
			if(priorityPool.size() > 0) {
				robot.addToTube(p.next().mailItem);
				p.remove();
			}
			} catch (Exception e) {
				throw e;
			}
		}
		if (robot.isEmptyTube() && pool.size() > 0) { //Empty tube mean it can still hold an item from normal mail pool
			if(robot.isEmpty()) { //Both hand and tube are empty
				try {
					robot.addToHand(j.next().mailItem); // hand first as we want higher priority delivered first
					j.remove();
					if (pool.size() > 0) {
						robot.addToTube(j.next().mailItem);
						j.remove();
					}
				} catch (Exception e) { 
			            throw e; 
			    } 
			} else if(robot.isEmptyTube()) { //Only tube is empty
				robot.addToTube(j.next().mailItem);
				j.remove();
			}
		}
		if(!robot.isEmpty()) {
			robot.dispatch(); // send the robot off if it has any items to deliver
			i.remove(); // remove from mailPool queue
		}
	}

	/**
     * @param robot refers to a robot which has arrived back ready for more mailItems to deliver
     */	
	public void registerWaiting(Robot robot) { // assumes won't be there already
		robots.add(robot);
	}
	
	public void printMailItem() {
		ListIterator<Item> j = pool.listIterator();
		ListIterator<Item> p = priorityPool.listIterator();
		for(Item item: testPriorityPool) {
			System.out.print("priority: ");
			System.out.println(item.mailItem.toString());
		}
		for(Item item: testPool) {
			System.out.println(item.mailItem.toString());
		}

	}
}
