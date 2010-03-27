package org.schoolregister.importer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class StudentImportTest {

	private static final boolean KEEP_TEMP_DIR = true;

	private Log log = LogFactory.getLog(getClass());

	private File workDir;

	@Before
	public void setUp() throws Exception {

		workDir = new File(getClass().getName() + "_temp_dir");
		workDir.mkdir();

		log.debug("work dir abs path = " + workDir.getAbsolutePath());
	}

	@After
	public void tearDown() throws Exception {
		if (!KEEP_TEMP_DIR) {
			FileUtils.deleteDirectory(workDir);
			Assert.assertFalse(workDir.exists());
		}
	}

	@Test
	public void testDoImport() throws IOException {

		InputStream is = getClass().getResourceAsStream("input.csv");

		File actualOutputDir = new File(workDir, "output");
		new StudentImport().doImport(is, actualOutputDir);

		File expectedOutputDir = new File(workDir, "expected");
		unpackExpectedOutput(getClass().getResourceAsStream("export.zip"),
				expectedOutputDir);

		compareXmlDirs(expectedOutputDir, actualOutputDir);
	}

	private void unpackExpectedOutput(InputStream packedExpectedOutput,
			File outputDirectory) throws IOException {

		ZipInputStream zis = new ZipInputStream(packedExpectedOutput);
		ZipEntry entry = zis.getNextEntry();
		// TODO: unzip file into dir
	}

	private void compareXmlDirs(File expectedDir, File actualDir) {
		// TODO: compare xmls inside
	}

}
