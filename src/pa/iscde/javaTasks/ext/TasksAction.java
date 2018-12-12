package pa.iscde.javaTasks.ext;

import java.io.File;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import pa.iscde.javaTasks.Activator;
import pt.iscte.pidesco.javaeditor.service.JavaEditorServices;

public interface TasksAction {
	
	default String setDescription(Task t) {
		return t.getDescription();
	}
	
	default void doubleClick(Task t) {
		Activator.getJavaServ().selectText(new File(t.getResource()+ "/"+ t.getPath()), t.getOffset(), 0);
	}

}
