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
 * @author Krystian Nowak
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
		if (args.length != 2) {
			System.out
					.println("usage: java -jar jarfile.jar input.csv outputDir");
		} else {
			new StudentImport().doImport(new FileInputStream(args[0]),
					new File(args[1]));
		}
	}

}
