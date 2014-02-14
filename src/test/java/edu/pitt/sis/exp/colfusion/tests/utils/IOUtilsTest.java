package edu.pitt.sis.exp.colfusion.tests.utils;

import java.io.IOException;
import java.io.InputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.pitt.sis.exp.colfusion.ConfigManager;
import edu.pitt.sis.exp.colfusion.tests.PropertyKeysTest;
import edu.pitt.sis.exp.colfusion.utils.IOUtils;
import edu.pitt.sis.exp.colfusion.utils.models.IOUtilsStoredFileInfoModel;
import junit.framework.TestCase;

public class IOUtilsTest extends TestCase {
	
	Logger logger = LogManager.getLogger(IOUtilsTest.class.getName());
	ConfigManager configManager = ConfigManager.getInstance();
	
	/**
	 * Test if the Excel file from the test/resources is written to specified in the properties location.
	 */
	public void testWriteExcelAsInputStreamToFile() {
			
		String testFileUploadDir = configManager.getPropertyByName(PropertyKeysTest.testUploadRawFilesBaseLocation);
	
		String testEcelFileName = configManager.getPropertyByName(PropertyKeysTest.testExcelFileNameInResourceFolder);
		
		assertEquals("testExcelFile.xlsx", testEcelFileName);
		
		InputStream testExcelFileInputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(testEcelFileName);
		
		IOUtilsStoredFileInfoModel testFileInfo = new IOUtilsStoredFileInfoModel();
		
		testFileInfo.setFileName("testExcelFile.xlsx");
		testFileInfo.setFileExtension("xlsx");
		
		try {
			IOUtilsStoredFileInfoModel fileInfo = IOUtils.getInstance().writeInputStreamToFile(testExcelFileInputStream, testFileUploadDir, testEcelFileName);
			
			assertEquals(testFileInfo.getFileName(), fileInfo.getFileName());
			assertEquals(testFileInfo.getFileExtension(), fileInfo.getFileExtension());
		} catch (IOException e) {
			
			logger.error("testWriteExcelAsInputStreamToFile failed!", e);
		}
	}	
	
	/**
	 * Test if the TarGz Archive file from the test/resources is written to specified in the properties location.
	 */
	public void testWriteTarGzArchiveAsInputStremToFile() {
		String testFileUploadDir = configManager.getPropertyByName(PropertyKeysTest.testUploadRawFilesBaseLocation);
		
		String testFileName = configManager.getPropertyByName(PropertyKeysTest.testTarGzArchiveFileNameInResourceFolder);
		
		assertEquals("testTarGzArchive.tar.gz", testFileName);
		
		InputStream testInputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(testFileName);
		
		IOUtilsStoredFileInfoModel testFileInfo = new IOUtilsStoredFileInfoModel();
		
		testFileInfo.setFileName("testTarGzArchive.tar.gz");
		testFileInfo.setFileExtension("gz");
		
		try {
			IOUtilsStoredFileInfoModel fileInfo = IOUtils.getInstance().writeInputStreamToFile(testInputStream, testFileUploadDir, testFileName);
			
			assertEquals(testFileInfo.getFileName(), fileInfo.getFileName());
			assertEquals(testFileInfo.getFileExtension(), fileInfo.getFileExtension());
		} catch (IOException e) {
			
			logger.error("testWriteTarGzArchiveAsInputStremToFile failed!", e);
		}
	}

}
