package org.schoolregister.importer;

import java.io.*;
import java.util.*;

/**
 * 
 * @author krystian
 * 
 */
public class StudentImport {
	public static void main(String[] args) throws Exception {
		CsvParser p = new CsvParser();

		List<Student> students = p.parseFile(new FileInputStream(
				"c:\\_s\\Dane.csv"));

		for (Student student : students) {
			System.out.println(student.getFirstNames() + " "
					+ student.getLastName());
		}

		XmlExporter.writeXml(students, new File("c:\\_s\\e"));
	}
}
