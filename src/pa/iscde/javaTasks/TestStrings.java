package pa.iscde.javaTasks;

import java.io.File;

public class TestStrings {

	public static void main(String[] args) {
		System.out.println("/*");
		System.out.println("*/");
		System.out.println(new File("/TestWorkspace/src/Main.java").getParentFile().getPath());
	}

}
