package org.schoolregister.importer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

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

		File outputDir = new File(workDir, "output");
		new StudentImport().doImport(is, outputDir);

	}

}
