package pa.iscde.javaTasks;

import java.io.File;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import pt.iscte.pidesco.javaeditor.service.JavaEditorListener;
import pt.iscte.pidesco.javaeditor.service.JavaEditorServices;
import pt.iscte.pidesco.projectbrowser.service.ProjectBrowserServices;

public class Activator implements BundleActivator {

	// Comment
	private static BundleContext context;
	private static JavaEditorServices javaServ;
	private static ProjectBrowserServices browServ;

	public static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		ServiceReference<JavaEditorServices> editorServiceReferance = context
				.getServiceReference(JavaEditorServices.class);
		ServiceReference<ProjectBrowserServices> browserServiceReference = context
				.getServiceReference(ProjectBrowserServices.class);
		javaServ = context.getService(editorServiceReferance);
		browServ = context.getService(browserServiceReference);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

	public static JavaEditorServices getJavaServ() {
		return javaServ;
	}

	public static ProjectBrowserServices getBrowServ() {
		return browServ;
	}

}
