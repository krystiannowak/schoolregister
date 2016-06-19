package org.schoolregister.importer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.codepoetics.protonpack.Indexed;
import com.codepoetics.protonpack.StreamUtils;

/**
 * 
 * @author Krystian Nowak
 * 
 */
public class XmlExporter {

	private static final Log LOG = LogFactory.getLog(XmlExporter.class);

	public static void writeXml(List<Student> students, File dir) throws IOException {
		if (!dir.exists()) {
			if (!dir.mkdir()) {
				throw new IOException("cannot create directory " + dir.getAbsolutePath());
			}
		} else {
			if (!dir.isDirectory()) {
				throw new IOException("dir " + dir.getAbsolutePath() + " is not a directory");
			}
		}

		for (Student student : students) {
			writeXmlForStudent(student, dir);
		}
	}

	private static void writeXmlForStudent(Student student, File dir) throws IOException {
		String fileName = student.getLastName() + " " + student.getFirstNames() + ".xml";
		LOG.info("generating file " + fileName);
		File file = new File(dir, fileName);
		if (file.exists()) {

			if (!file.isFile()) {
				if (file.isDirectory()) {
					throw new IOException("file " + file.getAbsolutePath() + " should not be a directory");
				} else {
					throw new IOException("file " + file.getAbsolutePath() + " not be a normal file");
				}
			}

			if (!file.delete()) {
				throw new IOException("cannot delete old file " + file.getAbsolutePath());
			}
		}

		OutputStream os = new FileOutputStream(file);
		writeXmlForStudent(student, os);
		os.flush();
		os.close();
	}

	private static void writeXmlForStudent(Student student, OutputStream os) throws IOException {
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, Charset.forName("UTF-8")));

		boolean isReady = false;

		String wzor;
		String styleSheet;

		// to config file?
		if (student.isExceptional()) {
			wzor = "ART-II/201-w/3-15.03.2012";
			styleSheet = "../../../szablony/ART-II_201-w_3-15.03.2012_1.xsl";
		} else {
			wzor = "ART-II/201/3-15.03.2012";
			styleSheet = "../../../szablony/ART-II_201_3-15.03.2012_1.xsl";
		}

		String wzorArkusza = "ART-II/280/3-15.03.2012";

		writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		writer.write("<?xml-stylesheet type=\"text/xsl\" href=\"" + styleSheet + "\" ?>\n");

		final String CERTIFICATE_TAG = "swiadectwo";

		writer.write("<" + CERTIFICATE_TAG + " wzor=\"" + wzor + "\" wzorArkusza=\"" + wzorArkusza + "\" czyGotowe=\""
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

		tags.append(tag("typ_szkoly", "piątej szkoły podstawowej"));
		tags.append(tag("nazwa_placowki1", "Poznańskiej Ogólnokształcącej Szkoły Muzycznej I stopnia nr 1"));
		tags.append(tag("rok_wystawienia", "2016"));
		tags.append(tag("dzien_miesiac_wystawienia", "24 czerwca"));
		tags.append(tag("miejscowosc_wystawienia", "Poznań"));
		tags.append(tag("rok_rady", "2016"));
		tags.append(tag("dzien_miesiac_rady", "20 czerwca"));
		tags.append(tag("wojewodztwo", "wielkopolskie"));
		tags.append(tag("miejscowosc", "Poznaniu"));
		tags.append(tag("imie_szkoly", "Henryka Wieniawskiego"));
		tags.append(tag("rok_szkolny1", "2015"));
		tags.append(tag("rok_szkolny2", "2016"));

		tags.append(closeTag(UNIT_TAG));

		return tags.toString();
	}

	// to ini file?
	private static String classTags() {
		StringBuffer tags = new StringBuffer();

		final String CLASS_TAG = "klasa";

		tags.append(openTag(CLASS_TAG));

		tags.append(tag("promowany_do_klasy", "szóstej"));
		tags.append(tag("uczeszczal_do_klasy", "piątej"));

		tags.append(closeTag(CLASS_TAG));

		return tags.toString();
	}

	private static String studentTags(Student student) {
		StringBuffer tags = new StringBuffer();

		final String STUDENT_TAG = "uczen";

		tags.append(openTag(STUDENT_TAG));

		tags.append(studentNumberTags(student));

		tags.append(tag("pesel", student.getPesel()));
		tags.append(tag("imie_nazwisko", student.getFirstNames() + " " + student.getLastName()));

		tags.append(tag("aluby", student.isFemale() ? "a" : "y"));
		tags.append(tag("alubkreska", student.isFemale() ? "a" : "-"));
		tags.append(tag("kalubkreska", student.isFemale() ? "ka" : "-"));

		tags.append(tag("kreskalubnie", "-"));
		tags.append(tag("ilube", "ę"));
		tags.append(tag("dzien_miesiac_urodzenia", DateUtils.extractDayMonth(student.getBirthDate())));
		tags.append(tag("rok_urodzenia", DateUtils.extractYear(student.getBirthDate())));
		tags.append(tag("miejscowosc_urodzenia_m", student.getBirthCity()));
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

	private static final String OPTIONAL_SUBJECT_PREFIX = "OPT:";

	private static final String OPTIONAL_GRADE_TAG_PREFIX = "zajecia_dodatkowe_ocena";
	private static final String OPTIONAL_SUBJECT_TAG_PREFIX = "zajecia_dodatkowe";

	private static final Map<String, String> ADDITIONAL_SUBJECT_MAPPING = new LinkedHashMap<>();

	static {
		ADDITIONAL_SUBJECT_MAPPING.put("zajecia_dodatkowe_ocena1", "język niemiecki");
		ADDITIONAL_SUBJECT_MAPPING.put("zajecia_dodatkowe_ocena2", "zespół rytmiki");
		// ADDITIONAL_SUBJECT_MAPPING.put("zajecia_dodatkowe_ocena3",
		// "instrument dodatkowy - ");
	}

	private static String markTags(Student student) {

		Map<Long, Map.Entry<String, String>> indexed = StreamUtils
				.zipWithIndex(student.getMarks().entrySet().stream()
						.filter(e -> e.getKey().startsWith(OPTIONAL_SUBJECT_PREFIX)
								&& !StringUtils.isEmpty(e.getValue()))
						.sorted(Comparator.comparing(Map.Entry::getKey)))
				.collect(Collectors.toMap(Indexed::getIndex, Indexed::getValue));

		Map<String, String> gradeTags = indexed.entrySet().stream().collect(
				Collectors.toMap(e -> OPTIONAL_GRADE_TAG_PREFIX + (e.getKey() + 1), e -> e.getValue().getValue()));

		Map<String, String> subjectTags = indexed.entrySet().stream()
				.collect(Collectors.toMap(e -> OPTIONAL_SUBJECT_TAG_PREFIX + (e.getKey() + 1), e -> {

					String mappingLookup = e.getValue().getKey().substring(OPTIONAL_SUBJECT_PREFIX.length());
					if ("zajecia_dodatkowe_ocena3".equals(mappingLookup)) {
						return "instrument dodatkowy - " + student.getAdditionalInstrument();
					} else {
						return ADDITIONAL_SUBJECT_MAPPING.get(mappingLookup);
					}

				}));

		StringBuffer tags = new StringBuffer();

		final String MARKS_TAG = "oceny";

		tags.append(openTag(MARKS_TAG));

		student.getMarks().entrySet().stream()
				.filter(e -> !e.getKey().startsWith(OPTIONAL_SUBJECT_PREFIX) && !StringUtils.isEmpty(e.getValue()))
				.forEach(e -> tags.append(tag(e.getKey().replaceAll(" ", ""), e.getValue())));

		gradeTags.forEach((k, v) -> tags.append(tag(k.replaceAll(" ", ""), v)));

		tags.append(subjectTags(student.getInstrument()));

		subjectTags.forEach((k, v) -> tags.append(tag(k, v)));

		tags.append(achievementsTags(student));

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

	private static final Map<String, String> SUBJECT_MAPPING = new LinkedHashMap<>();

	static {
		SUBJECT_MAPPING.put("przedmiot1", "język polski");
		SUBJECT_MAPPING.put("przedmiot2", "język angielski");
		SUBJECT_MAPPING.put("przedmiot3", "historia i społeczeństwo");
		SUBJECT_MAPPING.put("przedmiot4", "przyroda");
		SUBJECT_MAPPING.put("przedmiot5", "matematyka");
		SUBJECT_MAPPING.put("przedmiot6", "zajęcia komputerowe");
		SUBJECT_MAPPING.put("przedmiot7", "zajęcia techniczne");
		SUBJECT_MAPPING.put("przedmiot8", "plastyka");
		SUBJECT_MAPPING.put("przedmiot9", "wychowanie fizyczne");
		SUBJECT_MAPPING.put("przedmiot10", "wychowanie do życia w rodzinie");

		// SUBJECT_MAPPING.put("przedmiot16", "instrument główny - ...");
		SUBJECT_MAPPING.put("przedmiot17", "fortepian obowiązkowy");

		SUBJECT_MAPPING.put("przedmiot18", "kształcenie słuchu");
		SUBJECT_MAPPING.put("przedmiot19", "audycje muzyczne");

		SUBJECT_MAPPING.put("przedmiot20", "chór");
		SUBJECT_MAPPING.put("przedmiot21", "orkiestra");
		SUBJECT_MAPPING.put("przedmiot22", "zespół kameralny");
	}

	private static String subjectTags(String mainInstrument) {
		// TODO: from config?

		StringBuffer tags = new StringBuffer();

		for (String tagName : SUBJECT_MAPPING.keySet()) {
			String tagValue = SUBJECT_MAPPING.get(tagName);
			tags.append(tag(tagName, tagValue));
		}

		// to some customized config with template
		// e.g. przedmiot16=instrument główny - ${student.instrument}
		tags.append(tag("przedmiot16", "instrument główny - " + mainInstrument));

		return tags.toString();
	}

}
