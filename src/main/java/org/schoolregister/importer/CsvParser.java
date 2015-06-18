package org.schoolregister.importer;

import java.io.*;
import java.nio.charset.*;
import java.util.*;

import org.apache.commons.collections.*;
import org.apache.commons.lang.*;

import au.com.bytecode.opencsv.*;

/**
 * 
 * @author Krystian Nowak
 * 
 */
public class CsvParser {
	private static final String KEY_PESEL = "pesel";
	private static final String KEY_LAST_NAME = "nazwisko";
	private static final String KEY_FIRST_NAME = "imie";
	private static final String KEY_BIRTH_DATE = "data_urodzenia";
	private static final String KEY_BIRTH_CITY = "miejscowosc_urodzenia_m";
	private static final String KEY_EXCEPTIONAL = "wyroznienie";
	private static final String KEY_STUDENT_NUMBER = "numer_ucznia";
	private static final String KEY_SPECIALITY = "cykl_wydzial";
	private static final String KEY_INSTRUMENT = "instrument";
	private static final String KEY_ACHIEVEMENTS = "szczegolne_osiagniecia";

	private Map<Integer, String> positionToKeyMap = new HashMap<Integer, String>();

	private void parseHeader(List<String> line) {
		int currentPosition = 0;
		for (String token : line) {
			positionToKeyMap.put(currentPosition++, token);
		}
	}

	private void fillStudent(Student student, String key, String value) {
		if (KEY_PESEL.equalsIgnoreCase(key)) {
			student.setPesel(value);
		} else if (KEY_LAST_NAME.equalsIgnoreCase(key)) {
			student.setLastName(value);
		} else if (KEY_FIRST_NAME.equalsIgnoreCase(key)) {
			student.setFirstNames(value);
		} else if (KEY_BIRTH_DATE.equalsIgnoreCase(key)) {
			student.setBirthDate(DateUtils.stringToDate(value));
		} else if (KEY_BIRTH_CITY.equalsIgnoreCase(key)) {
			student.setBirthCity(value);
		} else if (KEY_EXCEPTIONAL.equalsIgnoreCase(key)) {
			student.setExceptional("TAK".equalsIgnoreCase(value));
		} else if (KEY_STUDENT_NUMBER.equalsIgnoreCase(key)) {
			putNumber(student, value);
		} else if (KEY_SPECIALITY.equalsIgnoreCase(key)) {
			student.setSpeciality(value);
		} else if (KEY_INSTRUMENT.equalsIgnoreCase(key)) {
			student.setInstrument(value);
		} else if (KEY_ACHIEVEMENTS.equalsIgnoreCase(key)) {
			putAchievements(student, value);
		} else {
			putMark(student, key, value);
		}
	}

	private static void putAchievements(Student student, String value) {
		if (!StringUtils.isEmpty(value)) {
			final String ACHIEVEMENTS_SEPARATOR = "|";
			StringTokenizer tokenizer = new StringTokenizer(value,
					ACHIEVEMENTS_SEPARATOR);

			while (tokenizer.hasMoreTokens()) {
				String token = tokenizer.nextToken();
				if (!StringUtils.isEmpty(token)) {
					student.getAchievements().add(token);
				}
			}
		}
	}

	private static void putNumber(Student student, String value) {
		if (!StringUtils.isEmpty(value)) {
			student.setNumber(Integer.parseInt(value));
		}
	}

	private static void putMark(Student student, String key, String value) {
		if (!StringUtils.isEmpty(value)) {
			if (StringUtils.isNumeric(value)) {
				int mark = Integer.parseInt(value);
				String markName = MarkUtils.numberToName(mark);
				student.getMarks().put(key, markName);
			} else {
				student.getMarks().put(key, value);
			}
		}
	}

	private Student parseLine(List<String> line) {
		if (!CollectionUtils.isEmpty(line)) {
			Student student = new Student();
			int currentPosition = 0;
			for (String token : line) {
				String key = positionToKeyMap.get(currentPosition++);
				fillStudent(student, key, token);
			}

			return student;
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Student> parseFile(InputStream is) throws IOException {
		List<Student> students = new ArrayList<Student>();

		InputStreamReader isr = new InputStreamReader(is,
				Charset.forName("windows-1250"));

		CSVReader r = new CSVReader(isr, ';');
		List<String[]> lines = r.readAll();

		boolean isHeader = true;
		for (String[] line : lines) {
			if (isHeader) {
				isHeader = false;
				parseHeader(Arrays.asList(line));
			} else {
				Student student = parseLine(Arrays.asList(line));
				if (student != null) {
					students.add(student);
				}
			}
		}

		return students;
	}
}
