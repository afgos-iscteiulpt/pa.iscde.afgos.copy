package pa.iscde.javaTasks;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import pa.iscde.javaTasks.ext.EvaluateContributionsHandler;
import pa.iscde.javaTasks.ext.Task;
import pt.iscte.pidesco.extensibility.PidescoView;
import pt.iscte.pidesco.javaeditor.service.JavaEditorListener;
import pt.iscte.pidesco.javaeditor.service.JavaEditorServices;
import pt.iscte.pidesco.projectbrowser.service.ProjectBrowserServices;

/**
 * A View witch shows the user what Tasks he still has to do in his code.
 * 
 * @author MrAndrGodinho
 */
public class JavaTasksView implements PidescoView {

	private EvaluateContributionsHandler extensionsHandler = new EvaluateContributionsHandler();
	private Map<String, Set<Task>> taskList = new HashMap<String, Set<Task>>();
	private Table table;

	/**
	 * Creates a layout with a table, which columns are "Tag", "Description",
	 * "Resource", "Path" and "Location" which values are Strings. It also reads all
	 * files to populate the table
	 */
	@Override
	public void createContents(Composite viewArea, Map<String, Image> imageMap) {
		Activator.getJavaServ().addListener(new JavaEditorListener() {

			@Override
			public void fileOpened(File file) {
				update(file);
			}

			@Override
			public void fileSaved(File file) {
				update(file);
			}

			@Override
			public void fileClosed(File file) {

			}

			@Override
			public void selectionChanged(File file, String text, int offset, int length) {

			}
		});

		viewArea.setLayout(new GridLayout());
		// as written here:
		// http://git.eclipse.org/c/platform/eclipse.platform.swt.git/tree/examples/org.eclipse.swt.snippets/src/org/eclipse/swt/snippets/Snippet38.java
		// documentation:
		// https://help.eclipse.org/kepler/index.jsp?topic=%2Forg.eclipse.platform.doc.isv%2Freference%2Fapi%2Forg%2Feclipse%2Fswt%2Fwidgets%2FTable.html
		table = new Table(viewArea, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		data.heightHint = 200;
		table.setLayoutData(data);
		table.addMouseListener(new MouseListener() {

			@Override
			public void mouseUp(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseDown(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
				TableItem selection = table.getSelection()[0];
				extensionsHandler.doubleClick(new Task(selection.getText(0), selection.getText(1), selection.getText(2), selection.getText(3), Character.getNumericValue(selection.getText(4).charAt(selection.getText(4).length()-1)), Integer.parseInt(selection.getText(5))));
			}
		});
		String[] titles = { "Tag", "Description", "Resource", "Path", "Location" };
		for (int i = 0; i < titles.length; i++) {
			TableColumn column = new TableColumn(table, SWT.NONE);
			column.setText(titles[i]);
		}
		TableColumn column = new TableColumn(table, SWT.NONE);
		column.setText("Offset");
		column.setResizable(false);
		readAllFiles(new File(Activator.getBrowServ().getRootPackage().getFile().getPath() + "/src"));
		for (int i = 0; i < titles.length; i++) {
			table.getColumn(i).pack();
		}
	}

	/**
	 * Reads all files inside {@code file}.
	 * 
	 * @param file {@link File}
	 */
	private void readAllFiles(File file) {
		for (File f : file.listFiles()) {
			if (f.isFile())
				update(f);
			else
				readAllFiles(f);
		}
	}

	/**
	 * Updates the table.
	 * 
	 * @param file
	 */
	public void update(File file) {
		CommentHandler commentHandler = new CommentHandler();
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line;
			int count = 0;
			int offset = 0;
			while ((line = br.readLine()) != null) {
				count++;
				commentHandler.processString(extensionsHandler.getTags(), line, file, count, offset);
				offset += line.length() + 2;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		taskList.put(file.getPath(), extensionsHandler.processTags(commentHandler.getTaskSet()));
		table.removeAll();
		saveDataInTable(taskList);
		table.redraw();
	}

	/**
	 * Populates the table with information present in {@code map}
	 * 
	 * @param map map with a String key and {@link Task} value
	 */
	private void saveDataInTable(Map<String, Set<Task>> map) {
		for (Set<Task> s : map.values())
			for (Task t : s) {
				TableItem item = new TableItem(table, SWT.NONE);
				item.setText(0, t.getTag().toString());
				item.setText(1, t.getDescription());
				item.setText(2, t.getResource());
				item.setText(3, t.getPath());
				item.setText(4, "line " + t.getLine());
				item.setText(5, Integer.toString(t.getOffset()));
			}
		for (int i = 0; i < 5; i++) {
			table.getColumn(i).pack();
		}

	}
}