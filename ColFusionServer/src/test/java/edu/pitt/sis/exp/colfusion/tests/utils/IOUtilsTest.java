package edu.pitt.sis.exp.colfusion.tests.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import junit.framework.TestCase;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.pitt.sis.exp.colfusion.utils.ConfigManager;
import edu.pitt.sis.exp.colfusion.utils.IOUtils;
import edu.pitt.sis.exp.colfusion.utils.PropertyKeysTest;
import edu.pitt.sis.exp.colfusion.utils.models.IOUtilsStoredFileInfoModel;

public class IOUtilsTest extends TestCase {
	
	Logger logger = LogManager.getLogger(IOUtilsTest.class.getName());
	ConfigManager configManager = ConfigManager.getInstance();
	
	public IOUtilsTest(final String name) {
		super(name);
	}
	
	/**
	 * Test if the Excel file from the test/resources is written to specified in the properties location.
	 */
	public void testWriteExcelAsInputStreamToFile() {
			
		String testFileUploadDir = IOUtils.getAbsolutePathInColfutionRoot(configManager.getProperty(PropertyKeysTest.testUploadRawFilesBaseLocation));
	
		String testEcelFileName = configManager.getProperty(PropertyKeysTest.testExcelFileNameInResourceFolder);
		
		assertEquals("testExcelFile.xlsx", testEcelFileName);
		
		InputStream testExcelFileInputStream = this.getClass().getResourceAsStream(testEcelFileName);
		
		IOUtilsStoredFileInfoModel testFileInfo = new IOUtilsStoredFileInfoModel();
		
		testFileInfo.setFileName("testExcelFile.xlsx");
		testFileInfo.setFileExtension("xlsx");
		
		try {
			IOUtilsStoredFileInfoModel fileInfo = IOUtils.writeInputStreamToFile(testExcelFileInputStream, testFileUploadDir, testEcelFileName, true);
			
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
		String testFileUploadDir = IOUtils.getAbsolutePathInColfutionRoot(configManager.getProperty(PropertyKeysTest.testUploadRawFilesBaseLocation));
		
		String testFileName = configManager.getProperty(PropertyKeysTest.testTarGzArchiveFileNameInResourceFolder);
		
		assertEquals("testTarGzArchive.tar.gz", testFileName);
		
		InputStream testInputStream = this.getClass().getResourceAsStream(testFileName);
		
		IOUtilsStoredFileInfoModel testFileInfo = new IOUtilsStoredFileInfoModel();
		
		testFileInfo.setFileName("testTarGzArchive.tar.gz");
		testFileInfo.setFileExtension("gz");
		
		try {
			IOUtilsStoredFileInfoModel fileInfo = IOUtils.writeInputStreamToFile(testInputStream, testFileUploadDir, testFileName, true);
			
			assertEquals(testFileInfo.getFileName(), fileInfo.getFileName());
			assertEquals(testFileInfo.getFileExtension(), fileInfo.getFileExtension());
		} catch (IOException e) {
			
			logger.error("testWriteTarGzArchiveAsInputStremToFile failed!", e);
		}
	}
	
	/**
	 * Test unarchival of a zip archive
	 */
	public void testUnarchive() {
		String testFileUploadDir = IOUtils.getAbsolutePathInColfutionRoot(configManager.getProperty(PropertyKeysTest.testUploadRawFilesBaseLocation));
		
		String testFileName = configManager.getProperty(PropertyKeysTest.testZipArchive);
		
		assertEquals("testZipArchive.zip", testFileName);
		
		String testFileNameAbsolutePath = this.getClass().getResource(testFileName).getFile();
		
		
		
		try {
			List<IOUtilsStoredFileInfoModel> fileInfo = IOUtils.unarchive(testFileNameAbsolutePath, testFileUploadDir);
			
			assertEquals(2, fileInfo.size());
			
		} catch (IOException e) {
			
			logger.error("testWriteTarGzArchiveAsInputStremToFile failed!", e);
		} catch (ArchiveException e) {
			
			logger.error("testWriteTarGzArchiveAsInputStremToFile failed!", e);
		}
	}
	
	public void testCopyFileContentOnKTRTemplates() {
		String testKTRBaseDirLocation = IOUtils.getAbsolutePathInColfutionRoot(configManager.getProperty(PropertyKeysTest.testKtrFielsBaseLocation));
		
		String fileToCopyName = configManager.getProperty(PropertyKeysTest.testCsvToDatabaseKTRTemplate);
		String fileToCopyLocation = this.getClass().getResource(fileToCopyName).getFile();
		
		try {
			IOUtilsStoredFileInfoModel result =  IOUtils.copyFileContent(fileToCopyLocation, testKTRBaseDirLocation + File.separator + 
					"testSid",	"testCopiedFile");
			
			File originalFile = new File(fileToCopyLocation);
		
			File copiedFile = new File(result.getAbsoluteFileName());
			
			assertEquals(originalFile.length(), copiedFile.length());
			
		} catch (Exception e) {
			logger.error("testCopyFileContentOnKTRTemplates failed!", e);
		}
	}

}
