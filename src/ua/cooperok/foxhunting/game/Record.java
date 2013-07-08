package ua.cooperok.foxhunting.game;

public class Record {

	/**
	 * Primary key of record
	 */
	public long id;
	
	/**
	 * Date of record's creation
	 */
	public String created;
	
	/**
	 * Count of steps
	 */
	public int steps;
	
	public Record () {
		
		
		
	}
	
	public Record(long id, int steps, String created) {
		
		this.id = id;
		
		this.steps = steps;
		
		this.created = created;
		
	}
	
}
