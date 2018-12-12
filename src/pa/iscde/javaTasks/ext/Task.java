package pa.iscde.javaTasks.ext;

/**
 * Object that represents a Task
 * 
 * @author MrAndrGodinho
 *
 */
public class Task {

	private final String tag;
	private final String description;
	private final String resource;
	private final String path;
	private final int line;
	private final int offset;

	/**
	 * Constructor for Tag
	 * 
	 * @param tag         {@link String}
	 * @param description String
	 * @param resource    String
	 * @param path        String
	 * @param line        Integer
	 * @param offset Integer
	 */
	public Task(String tag, String description, String resource, String path, int line, int offset) {
		this.tag = tag;
		this.description = description;
		this.resource = resource;
		this.path = path;
		this.line = line;
		this.offset = offset;
	}

	/**
	 * Getter for Tag
	 * 
	 * @return {@link String}
	 */
	public String getTag() {
		return tag;
	}

	/**
	 * Getter for Description
	 * 
	 * @return String
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Getter for Resource
	 * 
	 * @return String
	 */
	public String getResource() {
		return resource;
	}

	/**
	 * Getter for Path
	 * 
	 * @return String
	 */
	public String getPath() {
		return path;
	}

	/**
	 * Getter for Line
	 * 
	 * @return Integer
	 */
	public int getLine() {
		return line;
	}

	public int getOffset() {
		return offset;
	}

	@Override
	public String toString() {
		return "Task [tag=" + tag + ", description=" + description + ", resource=" + resource + ", path=" + path
				+ ", line=" + line + ", offset=" + offset + "]";
	}
}
