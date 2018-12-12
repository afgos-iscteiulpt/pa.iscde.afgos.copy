package pa.iscde.javaTasks.ext;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.SafeRunner;

public class EvaluateContributionsHandler {

	private Set<String> tags = new HashSet<>(Arrays.asList("TODO", "DEBUG", "FIXME"));
	private IExtensionRegistry registry = Platform.getExtensionRegistry();
	private IConfigurationElement[] config = registry.getConfigurationElementsFor(TAGSSERVICE_ID);

	private static final String TAGSSERVICE_ID = "pa.iscde.javaTasks.ext";

	public EvaluateContributionsHandler() {
		for (IConfigurationElement e : config) {
			tags.add(e.getAttribute("TagName"));
		}
	}

	public Set<Task> processTags(Set<Task> set) {
		Set<Task> tasks = new HashSet<>();
		try {
			for (IConfigurationElement e : config) {
				Object o = e.createExecutableExtension("TasksAction");
				for (Task t : set) {
					String newDescription = t.getDescription();
					if (o instanceof TasksAction) {
						newDescription = ((TasksAction) o).setDescription(t);
					}
					tasks.add(new Task(t.getTag(), newDescription, t.getResource(), t.getPath(), t.getLine(), t.getOffset()));
				}
			}
		} catch (CoreException ex) {
			System.out.println(ex.getMessage());
		}
		return tasks;
	}

	public Set<String> getTags() {
		return tags;
	}

	public void doubleClick(Task task) {
		try {
			for (IConfigurationElement e : config) {
				Object o = e.createExecutableExtension("TasksAction");
				if (o instanceof TasksAction) {
					((TasksAction) o).doubleClick(task);
				}
			}
		} catch (CoreException ex) {
			System.out.println(ex.getMessage());
		}
	}
}
