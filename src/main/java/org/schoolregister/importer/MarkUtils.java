package org.schoolregister.importer;

/**
 * 
 * @author Krystian Nowak
 * 
 */
public class MarkUtils {
	public static final String[] NAMES = { "", "niedostateczny",
			"dopuszczający", "dostateczny", "dobry", "bardzo dobry", "celujący" };

	public static String numberToName(int value) {
		return NAMES[value];
	}
}
