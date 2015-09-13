package edu.pitt.sis.exp.colfusion.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Ignore;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import edu.pitt.sis.exp.colfusion.utils.infra.TestResourcesNames;
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

		final String testFileUploadDir = this.tempFolder.newFolder("testWriteExcelAsInputStreamToFile").toString();

		final String testExcelFileName = TestResourcesNames.TEST_EXCEL_FILE_XLSX;

		assertEquals("testExcelFile.xlsx", testExcelFileName);

		final InputStream testExcelFileInputStream = getResourceAsStream(testExcelFileName);

		assertNotNull("Get resource as stream returned null for resource name " + testExcelFileName, testExcelFileInputStream);

		final IOUtilsStoredFileInfoModel testFileInfo = new IOUtilsStoredFileInfoModel();

		testFileInfo.setFileName("testExcelFile.xlsx");
		testFileInfo.setFileExtension("xlsx");

		try {
			final IOUtilsStoredFileInfoModel fileInfo = IOUtils.writeInputStreamToFile(testExcelFileInputStream, testFileUploadDir, testExcelFileName, true);

			assertEquals(testFileInfo.getFileName(), fileInfo.getFileName());
			assertEquals(testFileInfo.getFileExtension(), fileInfo.getFileExtension());
		} catch (final IOException e) {

			this.logger.error("testWriteExcelAsInputStreamToFile failed!", e);
		}
	}

	/**
	 * Test if the TarGz Archive file from the test/resources is written to specified in the properties location.
	 */
	@Test
	public void testWriteTarGzArchiveAsInputStremToFile() {
		final String testFileUploadDir = this.tempFolder.newFolder("testWriteTarGzArchiveAsInputStremToFile").toString();

		final String testFileName = TestResourcesNames.TEST_TAR_GZ_ARCHIVE_TAR_GZ;

		assertEquals("testTarGzArchive.tar.gz", testFileName);

		final InputStream testInputStream = getResourceAsStream(testFileName);

		assertNotNull("Get resource as stream returned null for resource name " + testFileName, testInputStream);

		final IOUtilsStoredFileInfoModel testFileInfo = new IOUtilsStoredFileInfoModel();

		testFileInfo.setFileName("testTarGzArchive.tar.gz");
		testFileInfo.setFileExtension("gz");

		try {
			final IOUtilsStoredFileInfoModel fileInfo = IOUtils.writeInputStreamToFile(testInputStream, testFileUploadDir, testFileName, true);

			assertEquals(testFileInfo.getFileName(), fileInfo.getFileName());
			assertEquals(testFileInfo.getFileExtension(), fileInfo.getFileExtension());
		} catch (final IOException e) {

			this.logger.error("testWriteTarGzArchiveAsInputStremToFile failed!", e);
		}
	}

	/**
	 * Test unarchival of a zip archive
	 */
	@Test
	public void testUnarchive() {
		final String testFileUploadDir = this.tempFolder.newFolder("testUnarchive").toString();

		final String testFileName = TestResourcesNames.TEST_ZIP_ARCHIVE_ZIP;

		assertEquals("testZipArchive.zip", testFileName);

		final String testFileNameAbsolutePath = getResourceAsAbsoluteURI(testFileName);
		assertNotNull("Get resource as absolute uri returned null for resource name " + testFileName, testFileNameAbsolutePath);

		try {
			final List<IOUtilsStoredFileInfoModel> fileInfo = IOUtils.unarchive(testFileNameAbsolutePath, testFileUploadDir);

			assertEquals(2, fileInfo.size());

		} catch (final IOException e) {

			this.logger.error("testWriteTarGzArchiveAsInputStremToFile failed!", e);
		} catch (final ArchiveException e) {

			this.logger.error("testWriteTarGzArchiveAsInputStremToFile failed!", e);
		}
	}

	//TODO:Fix the test. It should not care about ktr file since the functionality that is being tested is the copyFileContent
	// currently test fails because it cannot find the ktr file which is in the colfusionserverbll project.
	@Ignore
	@Test
	public void testCopyFileContentOnKTRTemplates() {
		final String testKTRBaseDirLocation = this.tempFolder.newFolder("testCopyFileContentOnKTRTemplates").toString();

		final String fileToCopyName = this.configManager.getProperty(PropertyKeys.COLFUSION_KTR_TEMPLATES_CSV_TO_DATABASE);
		final String fileToCopyLocation = getResourceAsAbsoluteURI(fileToCopyName);
		assertNotNull("Get resource as absolute uri returned null for resource name " + fileToCopyName, fileToCopyLocation);

		try {
			final IOUtilsStoredFileInfoModel result =  IOUtils.copyFileContent(fileToCopyLocation, testKTRBaseDirLocation + File.separator +
					"testSid",	"testCopiedFile");

			final File originalFile = new File(fileToCopyLocation);

			final File copiedFile = new File(result.getAbsoluteFileName());

			assertEquals(originalFile.length(), copiedFile.length());

		} catch (final Exception e) {
			this.logger.error("testCopyFileContentOnKTRTemplates failed!", e);
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
		final String testStaticFilesRootLocation = "testLocaiton";
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

	@Test
	public void testReadXMLDocument() throws ParserConfigurationException, SAXException, IOException {

		final Document doc = IOUtils.readXMLDocument(getResourceAsAbsoluteURI(TestResourcesNames.TEST_COMPANY_XML_FILE));

		assertEquals("company", doc.getDocumentElement().getNodeName());

		final NodeList nList = doc.getElementsByTagName("staff");

		assertEquals(2, nList.getLength());
	}

	@Test
	public void testWriteXMLDocument() throws ParserConfigurationException, SAXException, IOException, TransformerException {
		final String originalTestXMLFile = getResourceAsAbsoluteURI(TestResourcesNames.TEST_COMPANY_XML_FILE);
		final Document doc = IOUtils.readXMLDocument(originalTestXMLFile);

		//TODO make sure this the funcitnality still works. for some reason after using java 8, setXmlStandalone is undefined.
		//doc.setXmlStandalone(false);

		final String writtenFileName = this.tempFolder.newFile("testWriteXMLDocument").toString();

		IOUtils.writeXMLDocument(doc, writtenFileName);

		final String originalTestXMLFileContent = org.apache.commons.io.IOUtils.toString(new FileInputStream(originalTestXMLFile));
		final String writtenTestXMLFileContent = org.apache.commons.io.IOUtils.toString(new FileInputStream(writtenFileName));

		assertEqualsIgnoreWhiteSpaces("The content doesn't match", originalTestXMLFileContent, writtenTestXMLFileContent);
	}
}
