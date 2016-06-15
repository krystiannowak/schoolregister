package org.schoolregister.importer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author Krystian Nowak
 * 
 */
public class Student {

	private String pesel;

	// for future
	private boolean isNameDeclinable;

	private String lastName;

	private String firstNames;

	private Calendar birthDate;

	private String birthCity;

	private String speciality;

	private String instrument;

	private boolean exceptional;

	private Integer number;

	private Map<String, String> marks = new LinkedHashMap<>();

	private List<String> achievements = new ArrayList<>();

	public String getPesel() {
		return pesel;
	}

	public void setPesel(String pesel) {
		this.pesel = pesel;
	}

	public boolean isFemale() {
		return getFirstNames().endsWith("a");
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstNames() {
		return firstNames;
	}

	public void setFirstNames(String firstNames) {
		this.firstNames = firstNames;
	}

	public Calendar getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Calendar birthDate) {
		this.birthDate = birthDate;
	}

	public String getBirthCity() {
		return birthCity;
	}

	public void setBirthCity(String birthCity) {
		this.birthCity = birthCity;
	}

	public Map<String, String> getMarks() {
		return marks;
	}

	public boolean isExceptional() {
		return exceptional;
	}

	public void setExceptional(boolean exceptional) {
		this.exceptional = exceptional;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public String getSpeciality() {
		return speciality;
	}

	public void setSpeciality(String speciality) {
		this.speciality = speciality;
	}

	public String getInstrument() {
		return instrument;
	}

	public void setInstrument(String instrument) {
		this.instrument = instrument;
	}

	public List<String> getAchievements() {
		return achievements;
	}

}
