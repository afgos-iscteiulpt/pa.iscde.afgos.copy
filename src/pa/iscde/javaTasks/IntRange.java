package pa.iscde.javaTasks;

/**
 * Resource to help process Ranges since i found out something as simple
 * as this is not a standart library in Java.
 * @author MrAndrGodinho
 *
 */
public class IntRange {
	
	private Integer min;
	private Integer max;
	
	/**
	 * Constructor for IntRange
	 * @param min Integer
	 * @param max Integer
	 */
	public IntRange(Integer min, Integer max) {
		this.min = min;
		this.max = max;
	}
	
	/**
	 * Check if an Int is inside this Range
	 * @param number
	 * @return
	 */
	public boolean contains(Integer number) {
		return (min < number && number < max);
	}

}
