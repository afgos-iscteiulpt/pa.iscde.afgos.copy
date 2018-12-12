package pa.iscde.javaTasks;

import java.util.Set;

import pa.iscde.javaTasks.ext.EvaluateContributionsHandler;
import pa.iscde.javaTasks.ext.Task;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * Handler for processing a line and check for a comment and a Tag inside it
 * @author MrAndrGodinho
 *
 */
public class CommentHandler {

	private boolean multiLine = false;
	private Set<Task> taskSet = new HashSet<>();

	/**
	 * Process a {@code line} from a {@code file} to check for Tags
	 * @param line String
	 * @param file {@link file}
	 * @param count Integer line number
	 * @param offset 
	 */
	public void processString(Set<String> tags, String line, File file, int count, int offset) {
		Collection<Integer> quotesList = stringZones(line);
		int multiLineStart = notQuotedIndexOf(line, quotesList, "/*");
		int multiLineEnd = notQuotedIndexOf(line, quotesList, "*/");
		int comment = notQuotedIndexOf(line, quotesList, "//");

		if (multiLine) {
			if (multiLineEnd == -1)
				lookForTag(tags, line, file, count, offset);
			else {
				multiLine = false;
				processString(tags, line.substring(multiLineEnd + 2), file, count, offset);
				lookForTag(tags, line.substring(0, multiLineEnd), file, count, offset);
			}
		} else if (comment != -1) {
			lookForTag(tags, line.substring(comment + 2), file, count, offset);
		} else if (multiLineStart != -1) {
			multiLine = true;
			if (multiLineEnd != -1) {
				processString(tags, line.substring(multiLineEnd + 2), file, count, offset);
				lookForTag(tags, line.substring(multiLineStart + 2, multiLineEnd), file, count, offset);
			} else
				lookForTag(tags, line.substring(multiLineStart + 2), file, count, offset);
		}
	}
	
	/**
	 * Given a list of quotes positions looks for a not quoted index of {@code findString}
	 * @param line String where to search
	 * @param quotesList list of quotes
	 * @param findString String to be found
	 * @return first index of {@code findString} not inside quotes
	 */
	private int notQuotedIndexOf(String line, Collection<Integer> quotesList, String findString) {
		int lastIndex = 0;
		while (lastIndex != -1) {
			lastIndex = line.indexOf(findString, lastIndex);
			if (lastIndex != -1) {
				if (isInsideQuotes(quotesList, lastIndex))
					lastIndex++;
				else
					return lastIndex;
			}
		}
		return -1;
	}

	/**
	 * Check if a index is inside quotes.
	 * @param quotesList List of quotes
	 * @param index Integer
	 * @return boolean that is true if that index is between quotes
	 */
	private boolean isInsideQuotes(Collection<Integer> quotesList, int index) {
		List<Integer> list = (List<Integer>) quotesList;
		for (int i = 0; i < quotesList.size(); i += 2) {
			IntRange range = new IntRange(list.get(i), list.get(i + 1));
			if (range.contains(index))
				return true;
		}
		return false;
	}

	/**
	 * Search for a Tags and add a new Task with that Tag to a set
	 * @param line String
	 * @param file File being processed
	 * @param count Integer line number
	 */
	private void lookForTag(Set<String> tags, String line, File file, int count, int offset) {
		for (String s : tags) {
			int index = line.indexOf(s.toString());
			if (index > -1)
				this.taskSet.add(new Task(s, line.substring(index + s.toString().length()), file.getParentFile().getPath(), file.getName(), count, offset));
		}
	}

	/**
	 * Creates collection with all '"' indexes. If there's none, returns an emptyList.
	 * @param line String
	 * @return Collection of Integers
	 */
	private Collection<Integer> stringZones(String line) {
		int lastIndex = 0;
		Collection<Integer> list = new ArrayList<Integer>();
		while (lastIndex != -1) {
			lastIndex = line.indexOf("\"", lastIndex);
			if (lastIndex != -1) {
				if (!line.substring(lastIndex - 1, lastIndex).equals("\""))
					list.add(lastIndex);
				lastIndex++;
			}
		}
		if (list.isEmpty())
			return Collections.emptyList();
		return list;

	}

	/**
	 * Getter for MultiLine
	 * @return boolean
	 */
	public boolean isMultiLine() {
		return multiLine;
	}

	/**
	 * Setter for MultiLine
	 * @param multiLine
	 */
	public void setMultiLine(boolean multiLine) {
		this.multiLine = multiLine;
	}

	/**
	 * Getter for TaskSet
	 * @return Set of tasks
	 */
	public Set<Task> getTaskSet() {
		return taskSet;
	}
}
