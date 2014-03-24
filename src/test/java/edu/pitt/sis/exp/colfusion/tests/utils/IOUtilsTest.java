package edu.pitt.sis.exp.colfusion.tests.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.compress.archivers.ArchiveException;
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
	
	public IOUtilsTest(String name) {
		super(name);
	}
	
	/**
	 * Test if the Excel file from the test/resources is written to specified in the properties location.
	 */
	public void testWriteExcelAsInputStreamToFile() {
			
		String testFileUploadDir = IOUtils.getInstance().getAbsolutePathInColfutionRoot(configManager.getPropertyByName(PropertyKeysTest.testUploadRawFilesBaseLocation));
	
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
		String testFileUploadDir = IOUtils.getInstance().getAbsolutePathInColfutionRoot(configManager.getPropertyByName(PropertyKeysTest.testUploadRawFilesBaseLocation));
		
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
	
	/**
	 * Test unarchival of a zip archive
	 */
	public void testUnarchive() {
		String testFileUploadDir = IOUtils.getInstance().getAbsolutePathInColfutionRoot(configManager.getPropertyByName(PropertyKeysTest.testUploadRawFilesBaseLocation));
		
		String testFileName = configManager.getPropertyByName(PropertyKeysTest.testZipArchive);
		
		assertEquals("testZipArchive.zip", testFileName);
		
		String testFileNameAbsolutePath = Thread.currentThread().getContextClassLoader().getResource(testFileName).getFile();
		
		
		
		try {
			List<IOUtilsStoredFileInfoModel> fileInfo = IOUtils.getInstance().unarchive(testFileNameAbsolutePath, testFileUploadDir);
			
			assertEquals(2, fileInfo.size());
			
		} catch (IOException e) {
			
			logger.error("testWriteTarGzArchiveAsInputStremToFile failed!", e);
		} catch (ArchiveException e) {
			
			logger.error("testWriteTarGzArchiveAsInputStremToFile failed!", e);
		}
	}
	
	public void testCopyFileContentOnKTRTemplates() {
		String testKTRBaseDirLocation = IOUtils.getInstance().getAbsolutePathInColfutionRoot(configManager.getPropertyByName(PropertyKeysTest.testKtrFielsBaseLocation));
		
		String fileToCopyName = configManager.getPropertyByName(PropertyKeysTest.testCsvToDatabaseKTRTemplate);
		String fileToCopyLocation = Thread.currentThread().getContextClassLoader().getResource(fileToCopyName).getFile();
		
		try {
			IOUtilsStoredFileInfoModel result =  IOUtils.getInstance().copyFileContent(fileToCopyLocation, testKTRBaseDirLocation + File.separator + 
					"testSid",	"testCopiedFile");
			
			File originalFile = new File(fileToCopyLocation);
		
			File copiedFile = new File(result.getAbsoluteFileName());
			
			assertEquals(originalFile.length(), copiedFile.length());
			
		} catch (Exception e) {
			logger.error("testCopyFileContentOnKTRTemplates failed!", e);
		}
	}

}
