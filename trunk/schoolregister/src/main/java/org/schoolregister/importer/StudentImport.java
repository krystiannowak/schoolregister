package org.schoolregister.importer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author krystian
 * 
 */
public class StudentImport {

	private Log log = LogFactory.getLog(getClass());

	public void doImport(InputStream inputData, File outputDir)
			throws IOException {
		CsvParser p = new CsvParser();

		List<Student> students = p.parseFile(inputData);

		for (Student student : students) {
			log.debug("processing student " + student.getFirstNames() + " "
					+ student.getLastName());
		}

		XmlExporter.writeXml(students, outputDir);

	}

	public static void main(String[] args) throws Exception {
		new StudentImport()
				.doImport(
						new FileInputStream(
								"D:\\devel\\schoolregister\\src\\test\\resources\\org\\schoolregister\\importer\\input.csv"),
						new File("c:\\_s\\ee"));
	}

	// public static void main(String[] args) throws Exception {
	// new StudentImport().doImport(new FileInputStream("c:\\_s\\Dane.csv"),
	// new File("c:\\_s\\e"));
	// }

}
