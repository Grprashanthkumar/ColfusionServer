package edu.pitt.sis.exp.colfusion.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import edu.pitt.sis.exp.colfusion.utils.models.IOUtilsStoredFileInfoModel;

/**
 * The class which combines together all utilities functions related to
 * input/output.
 *
 */
public class IOUtils {

	private static final String UTF_8 = "UTF-8";
	final static Logger logger = LogManager.getLogger(IOUtils.class.getName());

	protected IOUtils() {

	}

	/**
	 * Copy content of one file and creates a new file with that content.
	 *
	 * @param originalFileLocation is the location of the file which should be copied.
	 * @param dirLocation the directory location where new file should be put.
	 * @param fileName the name of the new file which will be created.
	 * @return info about newly created file.
	 * @throws IOException
	 */
	public static IOUtilsStoredFileInfoModel copyFileContent(final String originalFileLocation, final String dirLocation, final String fileName) throws IOException{

		try {
			final InputStream io = new FileInputStream(new File(originalFileLocation));

			return IOUtils.writeInputStreamToFile(io, dirLocation, fileName, true);
		} catch (final IOException e) {
			throw e;
		}
	}

	/**
	 * Write string content to a file in current??? directory.
	 * @param content
	 * @param fileName
	 * @return
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static IOUtilsStoredFileInfoModel writeToFile(final String content, final String fileName) throws FileNotFoundException, IOException {
		final InputStream input = StreamUtils.fromString(content);

		final File file = new File(fileName);

		writeToFile(input, file, true);

		return prepareIOUtilsStoredFileInfoModel(file);
	}

	/**
	 * Save uploaded file from input stream to the specified location on the
	 * disk.
	 *
	 * @param uploadedInputStream
	 *            the stream with the file.
	 * @param uploadedFileDirLocation
	 *            the location of the directory where the file should be saved.
	 *            If intermediate directories do not exist, they will be
	 *            created.
	 * @param uploadedFileName
	 *            the file name to be created.
	 * @throws IOException
	 *             if file wasn't written to the disk successfully.
	 */
	public static IOUtilsStoredFileInfoModel writeInputStreamToFile(
			final InputStream uploadedInputStream, final String dirLocation, String fileName, final boolean closeStream)
					throws IOException {

		try {
			fileName = FilenameUtils.getName(fileName);
			fileName = StringUtils.replaceSpaces(fileName);

			final File targetDir = new File(dirLocation);
			targetDir.mkdirs();

			final String fileAbsoluteName = dirLocation + File.separator + fileName;

			final File fileToSave = new File(fileAbsoluteName);

			// If user submits the same file again, rename old file to include
			// the time it was uploaded/written to the disk
			if (fileToSave.exists()) {
				final String newName = fileAbsoluteName + "_version_"
						+ fileToSave.lastModified();
				fileToSave.renameTo(new File(newName));
			}

			writeToFile(uploadedInputStream, fileToSave, closeStream);

			return prepareIOUtilsStoredFileInfoModel(fileToSave);

		} catch (final IOException e) {

			logger.error("writeInputStreamToFile failed!", e);

			throw e;
		}
		finally {
			if (closeStream && uploadedInputStream != null) {
				try {
					uploadedInputStream.close();
				}
				catch (final IOException ignore) {}
			}
		}
	}

	/**
	 * @param uploadedInputStream
	 * @param fileToSave
	 * @param closeStream
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void writeToFile(final InputStream uploadedInputStream,
			final File fileToSave, final boolean closeStream)
					throws FileNotFoundException, IOException {
		final OutputStream out = new FileOutputStream(fileToSave);
		org.apache.commons.io.IOUtils.copy(uploadedInputStream, out);
		out.close();

		if (closeStream) {
			uploadedInputStream.close();
		}
	}

	/**
	 * Creates {@link IOUtilsStoredFileInfoModel} from {@link File} by
	 * extracting name, extension, last modified and absolute file name.
	 *
	 * @param file
	 *            to extract information from.
	 *
	 * @return {@link IOUtilsStoredFileInfoModel} with extracted info.
	 */
	private static IOUtilsStoredFileInfoModel prepareIOUtilsStoredFileInfoModel(
			final File file) {
		final IOUtilsStoredFileInfoModel fileInfo = new IOUtilsStoredFileInfoModel();

		fileInfo.setFileName(file.getName());
		fileInfo.setFileExtension(FilenameUtils.getExtension(file
				.getAbsolutePath()));
		fileInfo.setLastModified(file.lastModified());
		fileInfo.setAbsoluteFileName(file.getAbsolutePath());

		return fileInfo;
	}

	/**
	 * Return extension of a file from provided absolute file name.
	 *
	 * @param absoluteFileName
	 * @return
	 */
	public static String getFileExtension(final String absoluteFileName) {
		return FilenameUtils.getExtension(absoluteFileName);
	}

	/**
	 * Unarchive files into given directory. Currently works only with ZIP
	 * archives.
	 *
	 * @param absoluteFileName
	 *            of the archive file.
	 * @return The {@link List} of {@link IOUtilsStoredFileInfoModel}s which
	 *         were unarchived.
	 * @throws IOException
	 * @throws ArchiveException
	 */
	public static ArrayList<IOUtilsStoredFileInfoModel> unarchive(
			final String absoluteFileName, final String directory) throws IOException,
	ArchiveException {

		logger.info(String.format("Unarchiving %s to dir %s", absoluteFileName,
				directory));

		try {
			final InputStream is = new FileInputStream(absoluteFileName);

			final ArchiveInputStream in = new ArchiveStreamFactory()
					.createArchiveInputStream("zip", is);

			final ArrayList<IOUtilsStoredFileInfoModel> result = new ArrayList<IOUtilsStoredFileInfoModel>();
			ZipArchiveEntry entry;
			while ((entry = (ZipArchiveEntry) in.getNextEntry()) != null) {

				// Don't need to write directories or files which are not
				// directly under archive file.
				if (entry.isDirectory()
						|| FilenameUtils.getName(entry.getName()) != entry
						.getName()) {
					continue;
				}

				logger.info(String.format(
						"Attempting to write file %s into %s", entry.getName(),
						directory));

				final IOUtilsStoredFileInfoModel savedFileInfo = writeInputStreamToFile(
						in, directory, entry.getName(), false);

				result.add(savedFileInfo);
			}

			in.close();

			return result;

		} catch (final IOException e) {

			logger.error("unarchive failed!", e);

			throw e;
		} catch (final ArchiveException e) {

			logger.error("unarchive failed!", e);

			throw e;
		}

	}

	/**
	 * Unarchive files into archive parent directory. Currently works only with
	 * ZIP archives.
	 *
	 * @param absoluteFileName
	 *            of the archive file.
	 * @return The {@link List} of {@link IOUtilsStoredFileInfoModel}s which
	 *         were unarchived.
	 * @throws IOException
	 * @throws ArchiveException
	 */
	public static ArrayList<IOUtilsStoredFileInfoModel> unarchive(
			final String absoluteFileName) throws IOException, ArchiveException {

		final File archiveFile = new File(absoluteFileName);

		return unarchive(absoluteFileName, archiveFile.getParent());
	}

	/**
	 * Parses given XML document.
	 *
	 * @param ablsoluteXMLDocumentPath the absolute path of the XML document thatneed to be parsed.
	 * @return the {@link Document} parsed document.
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public static Document readXMLDocument(final String ablsoluteXMLDocumentPath) throws ParserConfigurationException, SAXException, IOException {

		logger.info(String.format("readXMLDocument: Starting to read XML document %s", ablsoluteXMLDocumentPath));

		final File xmlFile = new File(ablsoluteXMLDocumentPath);

		if (!xmlFile.exists()) {
			final String message = String.format("%s file doesn't exist", ablsoluteXMLDocumentPath);
			logger.error(message);
			throw new IOException(message);
		}

		return readXMLDocument(new FileInputStream(xmlFile));
	}

	public static Document readXMLDocument(final InputStream xmlDocumentStream) throws SAXException, IOException, ParserConfigurationException {
		final DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		final DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		final Document doc = docBuilder.parse(xmlDocumentStream);

		doc.getDocumentElement().normalize();

		return doc;
	}

	public static void writeXMLDocument(final Document document, final String absoluteFileName) throws TransformerException {
		final TransformerFactory transformerFactory = TransformerFactory.newInstance();
		final Transformer transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.ENCODING, UTF_8);
		final DOMSource source = new DOMSource(document);
		final StreamResult result = new StreamResult(new File(absoluteFileName));
		transformer.transform(source, result);
	}

	/**
	 * Make file URL from file absolute name.
	 * @param fileName absolute file name.
	 * @return URL of the file.
	 */
	public static String getFileURLFromName(String fileAbsoluteName) {
		//TODO FIXME if COLFUSION_STATIC_FILES_ROOT_LOCATION provided in "windows" format using \\, then  fileRelativeName
		// includes absolute path to the file and ktr execution fails.
		final String colfusionRoot = ConfigManager.getInstance().getProperty(PropertyKeys.COLFUSION_STATIC_FILES_ROOT_LOCATION);

		fileAbsoluteName = fileAbsoluteName.replace('\\', '/');

		final String fileRelativeName = fileAbsoluteName.replace(colfusionRoot, "");

		final String colfusionURL = ConfigManager.getInstance().getProperty(PropertyKeys.COLFUSION_URL);

		return colfusionURL + "/" + fileRelativeName;
	}

	/**
	 * Return absolute value of the provided path by doing following:
	 * if the provided path is absolute, then it will be returned without changes
	 * else the provided path will be concatenated to the value of the {@link PropertyKeys#COLFUSION_STATIC_FILES_ROOT_LOCATION} property.
	 *
	 * @param path
	 * @return
	 */
	public static String getAbsolutePathInColfution(final String path) {
		String ktrBaseDirLocation = "";

		if (IOUtils.isAbsolute(path)) {
			ktrBaseDirLocation = path;
		}
		else {
			final String colfusionRoot = ConfigManager.getInstance().getProperty(PropertyKeys.COLFUSION_STATIC_FILES_ROOT_LOCATION);
			ktrBaseDirLocation = colfusionRoot + File.separator + path;
		}

		return ktrBaseDirLocation;
	}

	/**
	 * Check whether provided path is absolute or relative.
	 *
	 * @param path
	 * 		the path to check
	 * @return true if provided path is absolute, otherwise false
	 */
	public static boolean isAbsolute(final String path) {
		//TODO: check if this might throw an exception or not if the value is not correct.
		return new File(path).isAbsolute();
	}
}
