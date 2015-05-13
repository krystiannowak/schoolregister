package org.schoolregister.importer;

import java.io.*;
import java.util.*;

import au.com.bytecode.opencsv.*;

import junit.framework.*;

/**
 * 
 * @author Krystian Nowak
 * 
 */
public class CvsTest extends TestCase {
	public void testParse() throws IOException {
		String input = "4;3;;5;6;;;4;4;;;";

		StringTokenizer t = new StringTokenizer(input, ";");
		for (int i = 0; t.hasMoreTokens(); i++) {
			System.out.println("token #" + i + " = " + t.nextToken());
		}

		CSVReader r = new CSVReader(new InputStreamReader(
				new ByteArrayInputStream(input.getBytes())), ';');
		String[] x = r.readNext();
		int j = 0;
		for (String y : x) {
			System.out.println("token #" + j + " = " + y);
			j++;
		}
	}
}
