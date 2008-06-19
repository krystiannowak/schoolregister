package org.schoolregister.importer;

import java.io.*;
import java.nio.charset.*;
import java.util.*;

import org.apache.commons.lang.*;

/**
 * 
 * @author krystian
 * 
 */
public class XmlExporter {
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
		System.out.println("generating file " + fileName);
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

		// String wzor = "MENiS-I/9/2";
		// String wzorArkusza = "MENiS-I/110/2";

		String wzor;

		// to config file?
		if (student.isExceptional()) {
			wzor = "ART-II/220-w/3";
		} else {
			wzor = "ART-II/220/3";
		}

		String wzorArkusza = "ART-II/280/3";

		writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		writer.write("<?xml-stylesheet type=\"text/xsl\" href=\"\" ?>\n");

		writer.write("<swiadectwo wzor=\"" + wzor + "\" wzorArkusza=\""
				+ wzorArkusza + "\" czyGotowe=\"" + Boolean.toString(isReady)
				+ "\">" + studentTags(student) + unitTags() + "</swiadectwo>");

		writer.flush();
	}

	private static String tag(String name, String value) {
		StringBuffer tags = new StringBuffer();

		tags.append('<');
		tags.append(name);
		tags.append('>');

		tags.append(value);

		tags.append("</");
		tags.append(name);
		tags.append('>');

		return tags.toString();

	}

	// to ini file?
	private static String unitTags() {
		StringBuffer tags = new StringBuffer();

		tags.append("<jednostka>");

		tags.append(tag("nazwa_placowki1", ""));
		tags.append(tag("nazwa_placowki2", "Poznańską"));
		tags.append(tag("rok_wystawienia", "2008"));
		tags.append(tag("dzien_miesiac_wystawienia", "19 czerwca"));
		tags.append(tag("miejscowosc_wystawienia", "Poznań"));
		tags.append(tag("rok_rady", "2008"));
		tags.append(tag("dzien_miesiac_rady", "14 czerwca"));
		tags.append(tag("wojewodztwo", "wielkopolskie"));
		tags.append(tag("miejscowosc", "Poznaniu"));
		tags.append(tag("imie_szkoly", "Henryka Wieniawskiego"));
		tags.append(tag("rok_szkolny1", "2007"));
		tags.append(tag("rok_szkolny2", "2008"));

		tags.append("</jednostka>");

		return tags.toString();
	}

	private static String studentTags(Student student) {
		StringBuffer tags = new StringBuffer();

		tags.append("<uczen>");

		tags.append(studentNumberTags(student));

		tags.append("<imie_nazwisko>");
		tags.append(student.getFirstNames());
		tags.append(' ');
		tags.append(student.getLastName());
		tags.append("</imie_nazwisko>");

		tags.append("<aluby>");
		if (student.isFemale()) {
			tags.append("a");
		} else {
			tags.append("y");
		}
		tags.append("</aluby>");

		tags.append("<alubkreska>");
		if (student.isFemale()) {
			tags.append("a");
		} else {
			tags.append("-");
		}
		tags.append("</alubkreska>");

		tags.append("<kalubkreska>");
		if (student.isFemale()) {
			tags.append("ka");
		} else {
			tags.append("-");
		}
		tags.append("</kalubkreska>");

		tags.append("<kreskalubnie>");
		tags.append("-");
		tags.append("</kreskalubnie>");

		tags.append("<ilube>");
		tags.append("ę");
		tags.append("</ilube>");

		tags.append("<dzien_miesiac_urodzenia>");
		tags.append(DateUtils.extractDayMonth(student.getBirthDate()));
		tags.append("</dzien_miesiac_urodzenia>");

		tags.append("<rok_urodzenia>");
		tags.append(DateUtils.extractYear(student.getBirthDate()));
		tags.append("</rok_urodzenia>");

		tags.append("<miejscowosc_urodzenia>");
		tags.append(student.getBirthCity());
		tags.append("</miejscowosc_urodzenia>");

		tags.append(tag("wojewodztwo_urodzenia", student.getBirthRegion()));

		tags.append(tag("w_zakresie_gry_na", student.getSpeciality()));

		tags.append(markTags(student));

		tags.append("</uczen>");

		return tags.toString();
	}

	private static String studentNumberTags(Student student) {
		StringBuffer tags = new StringBuffer();

		tags.append("<numer_ucznia>");

		if (student.getNumber() != null) {
			tags.append(Integer.toString(student.getNumber()));
		}

		tags.append("</numer_ucznia>");

		return tags.toString();
	}

	private static String markTags(Student student) {
		StringBuffer tags = new StringBuffer();
		tags.append("<oceny>");

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
		tags.append(tag("przedmiot16", "instrument główny - "
				+ student.getInstrument()));

		tags.append("</oceny>");

		return tags.toString();
	}

	private static String achievementsTags(Student student) {
		final String HTML_LINE_BREAK = "&lt;BR&gt;";

		StringBuffer tags = new StringBuffer();

		tags.append("<szczegolne_osiagniecia>");

		boolean isFirst = true;
		for (String achievement : student.getAchievements()) {

			if (isFirst) {
				isFirst = false;
			} else {
				tags.append(HTML_LINE_BREAK);
			}

			tags.append(achievement);
		}

		tags.append("</szczegolne_osiagniecia>");

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
