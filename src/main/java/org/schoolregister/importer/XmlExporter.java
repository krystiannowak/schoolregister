package org.schoolregister.importer;

import java.io.*;
import java.nio.charset.*;
import java.util.*;

import org.apache.commons.lang.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author Krystian Nowak
 * 
 */
public class XmlExporter {

	private static final Log LOG = LogFactory.getLog(XmlExporter.class);

	public static void writeXml(List<Student> students, File dir)
			throws IOException {
		if (!dir.exists()) {
			if (!dir.mkdir()) {
				throw new IOException("cannot create directory "
						+ dir.getAbsolutePath());
			}
		} else {
			if (!dir.isDirectory()) {
				throw new IOException("dir " + dir.getAbsolutePath()
						+ " is not a directory");
			}
		}

		for (Student student : students) {
			writeXmlForStudent(student, dir);
		}
	}

	private static void writeXmlForStudent(Student student, File dir)
			throws IOException {
		String fileName = student.getLastName() + " " + student.getFirstNames()
				+ ".xml";
		LOG.info("generating file " + fileName);
		File file = new File(dir, fileName);
		if (file.exists()) {

			if (!file.isFile()) {
				if (file.isDirectory()) {
					throw new IOException("file " + file.getAbsolutePath()
							+ " should not be a directory");
				} else {
					throw new IOException("file " + file.getAbsolutePath()
							+ " not be a normal file");
				}
			}

			if (!file.delete()) {
				throw new IOException("cannot delete old file "
						+ file.getAbsolutePath());
			}
		}

		OutputStream os = new FileOutputStream(file);
		writeXmlForStudent(student, os);
		os.flush();
		os.close();
	}

	private static void writeXmlForStudent(Student student, OutputStream os)
			throws IOException {
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os,
				Charset.forName("UTF-8")));

		boolean isReady = false;

		String wzor;
		String styleSheet;

		// to config file?
		if (student.isExceptional()) {
			wzor = "ART-II/201-w/3";
			styleSheet = "../../../szablony/ART-II_201-w_3_1.xsl";
		} else {
			wzor = "ART-II/201/3";
			styleSheet = "../../../szablony/ART-II_201_3_1.xsl";
		}

		String wzorArkusza = "ART-II/280/3";

		writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		writer.write("<?xml-stylesheet type=\"text/xsl\" href=\"" + styleSheet
				+ "\" ?>\n");

		final String CERTIFICATE_TAG = "swiadectwo";

		writer.write("<" + CERTIFICATE_TAG + " wzor=\"" + wzor
				+ "\" wzorArkusza=\"" + wzorArkusza + "\" czyGotowe=\""
				+ Boolean.toString(isReady) + "\">");

		writer.write(studentTags(student));
		writer.write(unitTags());
		writer.write(classTags());

		writer.write(closeTag(CERTIFICATE_TAG));

		writer.flush();
	}

	private static String openTag(String name) {
		StringBuffer tags = new StringBuffer();
		tags.append('<');
		tags.append(name);
		tags.append('>');
		return tags.toString();
	}

	private static String closeTag(String name) {
		StringBuffer tags = new StringBuffer();
		tags.append("</");
		tags.append(name);
		tags.append('>');
		return tags.toString();
	}

	private static String tag(String name, String value) {
		StringBuffer tags = new StringBuffer();
		tags.append(openTag(name));
		tags.append(value);
		tags.append(closeTag(name));
		return tags.toString();
	}

	// to ini file?
	private static String unitTags() {
		StringBuffer tags = new StringBuffer();

		final String UNIT_TAG = "jednostka";

		tags.append(openTag(UNIT_TAG));

		tags.append(tag("typ_szkoly", "czwartej szkoły podstawowej"));
		tags.append(tag("nazwa_placowki1",
				"Poznańskiej Ogólnokształcącej Szkoły Muzycznej I stopnia"));
		tags.append(tag("rok_wystawienia", "2015"));
		tags.append(tag("dzien_miesiac_wystawienia", "24 czerwca"));
		tags.append(tag("miejscowosc_wystawienia", "Poznań"));
		tags.append(tag("rok_rady", "2015"));
		tags.append(tag("dzien_miesiac_rady", "23 czerwca"));
		tags.append(tag("wojewodztwo", "wielkopolskie"));
		tags.append(tag("miejscowosc", "Poznaniu"));
		tags.append(tag("imie_szkoly", "Henryka Wieniawskiego"));
		tags.append(tag("rok_szkolny1", "2014"));
		tags.append(tag("rok_szkolny2", "2015"));

		tags.append(closeTag(UNIT_TAG));

		return tags.toString();
	}

	// to ini file?
	private static String classTags() {
		StringBuffer tags = new StringBuffer();

		final String CLASS_TAG = "klasa";

		tags.append(openTag(CLASS_TAG));

		tags.append(tag("promowany_do_klasy", "piątej"));
		tags.append(tag("uczeszczal_do_klasy", "IVa"));

		tags.append(closeTag(CLASS_TAG));

		return tags.toString();
	}

	private static String studentTags(Student student) {
		StringBuffer tags = new StringBuffer();

		final String STUDENT_TAG = "uczen";

		tags.append(openTag(STUDENT_TAG));

		tags.append(studentNumberTags(student));

		tags.append(tag("imie_nazwisko", student.getFirstNames() + " "
				+ student.getLastName()));

		tags.append(tag("aluby", student.isFemale() ? "a" : "y"));
		tags.append(tag("alubkreska", student.isFemale() ? "a" : "-"));
		tags.append(tag("kalubkreska", student.isFemale() ? "ka" : "-"));

		tags.append(tag("kreskalubnie", "-"));
		tags.append(tag("ilube", "ę"));
		tags.append(tag("dzien_miesiac_urodzenia",
				DateUtils.extractDayMonth(student.getBirthDate())));
		tags.append(tag("rok_urodzenia",
				DateUtils.extractYear(student.getBirthDate())));
		tags.append(tag("miejscowosc_urodzenia", student.getBirthCity()));
		tags.append(tag("wojewodztwo_urodzenia", student.getBirthRegion()));
		tags.append(tag("cykl_wydzial", student.getSpeciality()));

		tags.append(markTags(student));

		tags.append(closeTag(STUDENT_TAG));

		return tags.toString();
	}

	private static String studentNumberTags(Student student) {
		StringBuffer tags = new StringBuffer();

		final String STUDENT_NO_TAG = "numer_ucznia";

		tags.append(openTag(STUDENT_NO_TAG));

		if (student.getNumber() != null) {
			tags.append(Integer.toString(student.getNumber()));
		}

		tags.append(closeTag(STUDENT_NO_TAG));

		return tags.toString();
	}

	private static String markTags(Student student) {
		StringBuffer tags = new StringBuffer();

		final String MARKS_TAG = "oceny";

		tags.append(openTag(MARKS_TAG));

		for (String markKey : student.getMarks().keySet()) {
			String tag = markKey.replaceAll(" ", "");
			String mark = student.getMarks().get(markKey);
			if (!StringUtils.isEmpty(mark)) {
				tags.append(tag(tag, mark));
			}
		}

		tags.append(subjectTags());

		tags.append(achievementsTags(student));

		// to some customized config with template
		// e.g. przedmiot16=instrument główny - ${student.instrument}
		tags.append(tag("przedmiot16",
				"instrument główny - " + student.getInstrument()));

		tags.append(closeTag(MARKS_TAG));

		return tags.toString();
	}

	private static String achievementsTags(Student student) {
		final String HTML_LINE_BREAK = "&lt;BR&gt;";

		StringBuffer tags = new StringBuffer();

		final String ACHIEVEMENTS_TAG = "szczegolne_osiagniecia";

		tags.append(openTag(ACHIEVEMENTS_TAG));

		boolean isFirst = true;
		for (String achievement : student.getAchievements()) {

			if (isFirst) {
				isFirst = false;
			} else {
				tags.append(HTML_LINE_BREAK);
			}

			tags.append(achievement);
		}

		tags.append(closeTag(ACHIEVEMENTS_TAG));

		return tags.toString();
	}

	private static final Map<String, String> SUBJECT_MAPPING = new HashMap<String, String>();

	static {
		SUBJECT_MAPPING.put("przedmiot1", "język polski");
		SUBJECT_MAPPING.put("przedmiot2", "historia");
		SUBJECT_MAPPING.put("przedmiot3", "język angielski");
		SUBJECT_MAPPING.put("przedmiot4", "przyroda");
		SUBJECT_MAPPING.put("przedmiot5", "matematyka");
		SUBJECT_MAPPING.put("przedmiot6", "informatyka");
		SUBJECT_MAPPING.put("przedmiot7", "plastyka");
		SUBJECT_MAPPING.put("przedmiot8", "wychowanie fizyczne");

		// SUBJECT_MAPPING.put("przedmiot16", "instrument główny - ...");
		SUBJECT_MAPPING.put("przedmiot17", "fortepian dodatkowy");
		SUBJECT_MAPPING.put("przedmiot18", "instrument dodatkowy");
		SUBJECT_MAPPING.put("przedmiot19", "kształcenie słuchu");
		SUBJECT_MAPPING.put("przedmiot20", "rytmika");
		SUBJECT_MAPPING.put("przedmiot21", "audycje muzyczne");
		SUBJECT_MAPPING.put("przedmiot22", "chór");
		SUBJECT_MAPPING.put("przedmiot23", "orkiestra");
		SUBJECT_MAPPING.put("przedmiot24", "zespół kameralny");

		SUBJECT_MAPPING.put("zajecia_dodatkowe1", "język niemiecki");
		SUBJECT_MAPPING.put("zajecia_dodatkowe2",
				"wychowanie do życia w rodzinie");
		SUBJECT_MAPPING.put("zajecia_dodatkowe3", "rytmika dodatkowa");
	}

	private static String subjectTags() {
		// TODO: from config?

		StringBuffer tags = new StringBuffer();

		for (String tagName : SUBJECT_MAPPING.keySet()) {
			String tagValue = SUBJECT_MAPPING.get(tagName);
			tags.append(tag(tagName, tagValue));
		}

		return tags.toString();
	}

}
