package edu.pitt.sis.exp.colfusion.tests.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import edu.pitt.sis.exp.colfusion.tests.TestResourcesNames;
import edu.pitt.sis.exp.colfusion.utils.ConfigManager;
import edu.pitt.sis.exp.colfusion.utils.IOUtils;
import edu.pitt.sis.exp.colfusion.utils.PropertyKeys;
import edu.pitt.sis.exp.colfusion.utils.UnitTestBase;
import edu.pitt.sis.exp.colfusion.utils.models.IOUtilsStoredFileInfoModel;

//TODO might need to move to the utils projects
public class IOUtilsTest extends UnitTestBase {
	
	Logger logger = LogManager.getLogger(IOUtilsTest.class.getName());
	ConfigManager configManager = ConfigManager.getInstance();
	
	/**
	 * Test if the Excel file from the test/resources is written to specified in the properties location.
	 */
	@Test
	public void testWriteExcelAsInputStreamToFile() {
			
		String testFileUploadDir = tempFolder.newFolder("testWriteExcelAsInputStreamToFile").toString();
	
		String testExcelFileName = TestResourcesNames.TEST_EXCEL_FILE_XLSX;
		
		assertEquals("testExcelFile.xlsx", testExcelFileName);
		
		InputStream testExcelFileInputStream = getResourceAsStream(testExcelFileName);
		
		assertNotNull("Get resource as stream returned null for resource name " + testExcelFileName, testExcelFileInputStream);
		
		IOUtilsStoredFileInfoModel testFileInfo = new IOUtilsStoredFileInfoModel();
		
		testFileInfo.setFileName("testExcelFile.xlsx");
		testFileInfo.setFileExtension("xlsx");
		
		try {
			IOUtilsStoredFileInfoModel fileInfo = IOUtils.writeInputStreamToFile(testExcelFileInputStream, testFileUploadDir, testExcelFileName, true);
			
			assertEquals(testFileInfo.getFileName(), fileInfo.getFileName());
			assertEquals(testFileInfo.getFileExtension(), fileInfo.getFileExtension());
		} catch (IOException e) {
			
			logger.error("testWriteExcelAsInputStreamToFile failed!", e);
		}
	}	
	
	/**
	 * Test if the TarGz Archive file from the test/resources is written to specified in the properties location.
	 */
	@Test
	public void testWriteTarGzArchiveAsInputStremToFile() {
		String testFileUploadDir = tempFolder.newFolder("testWriteTarGzArchiveAsInputStremToFile").toString();
		
		String testFileName = TestResourcesNames.TEST_TAR_GZ_ARCHIVE_TAR_GZ;
		
		assertEquals("testTarGzArchive.tar.gz", testFileName);
		
		InputStream testInputStream = getResourceAsStream(testFileName);
		
		assertNotNull("Get resource as stream returned null for resource name " + testFileName, testInputStream);
		
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
	@Test
	public void testUnarchive() {
		String testFileUploadDir = tempFolder.newFolder("testUnarchive").toString();
		
		String testFileName = TestResourcesNames.TEST_ZIP_ARCHIVE_ZIP;
		
		assertEquals("testZipArchive.zip", testFileName);
		
		String testFileNameAbsolutePath = getResourceAsAbsoluteURI(testFileName);
		assertNotNull("Get resource as absolute uri returned null for resource name " + testFileName, testFileNameAbsolutePath);
		
		try {
			List<IOUtilsStoredFileInfoModel> fileInfo = IOUtils.unarchive(testFileNameAbsolutePath, testFileUploadDir);
			
			assertEquals(2, fileInfo.size());
			
		} catch (IOException e) {
			
			logger.error("testWriteTarGzArchiveAsInputStremToFile failed!", e);
		} catch (ArchiveException e) {
			
			logger.error("testWriteTarGzArchiveAsInputStremToFile failed!", e);
		}
	}
	
	@Test
	public void testCopyFileContentOnKTRTemplates() {
		String testKTRBaseDirLocation = tempFolder.newFolder("testCopyFileContentOnKTRTemplates").toString();
		
		String fileToCopyName = configManager.getProperty(PropertyKeys.COLFUSION_KTR_TEMPLATES_CSV_TO_DATABASE);
		String fileToCopyLocation = getResourceAsAbsoluteURI(fileToCopyName);
		assertNotNull("Get resource as absolute uri returned null for resource name " + fileToCopyName, fileToCopyLocation);
		
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
	
	/**
	 * Tests {@link IOUtils#isAbsolute(String)}.
	 */
	@Test
	public void testIsAbsolute() {
		assertEquals(false, IOUtils.isAbsolute(""));
		
		if (isWindows()) {
			assertEquals(true, IOUtils.isAbsolute("C:\\drive"));
			assertEquals(true, IOUtils.isAbsolute("C:/drive"));
			assertEquals(false, IOUtils.isAbsolute("drive"));
			
			assertEquals(false, IOUtils.isAbsolute("drive\\drive"));
			assertEquals(false, IOUtils.isAbsolute("drive/drive"));
		}
		else {
			assertEquals(true, IOUtils.isAbsolute("/drive/drive"));
			
			assertEquals(false, IOUtils.isAbsolute("drive/drive"));
		}
		
		assertEquals(false, IOUtils.isAbsolute("drive/.../drive"));		
	}
	
	/**
	 * Tests {@link IOUtils#getAbsolutePathInColfution(String)}.
	 */
	@Test
	public void testGetAbsolutePathInColfution() {
		String testStaticFilesRootLocation = "testLocaiton";
		redefineSystemPropertyForMethod(PropertyKeys.COLFUSION_STATIC_FILES_ROOT_LOCATION.getKey(), testStaticFilesRootLocation);
		
		if (isWindows()) {
			assertEquals("C:\\", IOUtils.getAbsolutePathInColfution("C:\\"));
			assertEquals("C:\\bla", IOUtils.getAbsolutePathInColfution("C:\\bla"));
			assertEquals(testStaticFilesRootLocation + "\\bla", IOUtils.getAbsolutePathInColfution("bla"));
			assertEquals(testStaticFilesRootLocation + "\\bla\\blu", IOUtils.getAbsolutePathInColfution("bla\\blu"));
			assertEquals(testStaticFilesRootLocation + "\\", IOUtils.getAbsolutePathInColfution(""));
		}
		else {
			assertEquals("/", IOUtils.getAbsolutePathInColfution("/"));
			assertEquals("/bla", IOUtils.getAbsolutePathInColfution("/bla"));
			assertEquals(testStaticFilesRootLocation + "/bla", IOUtils.getAbsolutePathInColfution("bla"));
			assertEquals(testStaticFilesRootLocation + "/bla/blu", IOUtils.getAbsolutePathInColfution("bla/blu"));
			assertEquals(testStaticFilesRootLocation + "/", IOUtils.getAbsolutePathInColfution(""));
		}		
	}
}
