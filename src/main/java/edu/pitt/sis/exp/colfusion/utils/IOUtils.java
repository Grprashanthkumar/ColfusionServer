package edu.pitt.sis.exp.colfusion.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

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
 * @author Evgeny
 * 
 */
public class IOUtils {

	final Logger logger = LogManager.getLogger(IOUtils.class.getName());

	private static IOUtils instance = null;

	protected IOUtils() {

	}

	public static IOUtils getInstance() {
		if (instance == null) {
			instance = new IOUtils();
		}

		return instance;
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
	public IOUtilsStoredFileInfoModel copyFileContent(String originalFileLocation, String dirLocation, String fileName) throws IOException{
		
		InputStream io = null;
		
		try {
			io = new FileInputStream(new File(originalFileLocation));
			
			return IOUtils.getInstance().writeInputStreamToFile(io, dirLocation, fileName);
		} catch (IOException e) {
			throw e;
		}
		finally {
			if (io != null) {
				io.close();
			}
		}
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
	public IOUtilsStoredFileInfoModel writeInputStreamToFile(
			InputStream uploadedInputStream, String dirLocation, String fileName)
			throws IOException {

		try {
			fileName = FilenameUtils.getName(fileName);

			File targetDir = new File(dirLocation);
			targetDir.mkdirs();

			String fileAbsoluteName = dirLocation + File.separator + fileName;

			File fileToSave = new File(fileAbsoluteName);

			// If user submits the same file again, rename old file to include
			// the time it was uploaded/written to the disk
			if (fileToSave.exists()) {
				String newName = fileAbsoluteName + "_version_"
						+ fileToSave.lastModified();
				fileToSave.renameTo(new File(newName));
			}

			OutputStream out = new FileOutputStream(fileToSave);
			org.apache.commons.io.IOUtils.copy(uploadedInputStream, out);
			out.close();

			return prepareIOUtilsStoredFileInfoModel(fileToSave);

		} catch (IOException e) {

			logger.error("writeInputStreamToFile failed!", e);

			throw e;
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
	private IOUtilsStoredFileInfoModel prepareIOUtilsStoredFileInfoModel(
			File file) {
		IOUtilsStoredFileInfoModel fileInfo = new IOUtilsStoredFileInfoModel();

		fileInfo.setFileName(file.getName());
		fileInfo.setFileExtension(FilenameUtils.getExtension(file
				.getAbsolutePath()));
		fileInfo.setLastModified(file.lastModified());
		fileInfo.setAbsoluteFileName(file.getAbsolutePath());

		return fileInfo;
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
	public ArrayList<IOUtilsStoredFileInfoModel> unarchive(
			String absoluteFileName, String directory) throws IOException,
			ArchiveException {

		logger.info(String.format("Unarchiving %s to dir %s", absoluteFileName,
				directory));

		try {
			final InputStream is = new FileInputStream(absoluteFileName);

			ArchiveInputStream in = new ArchiveStreamFactory()
					.createArchiveInputStream("zip", is);

			ArrayList<IOUtilsStoredFileInfoModel> result = new ArrayList<IOUtilsStoredFileInfoModel>();
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

				IOUtilsStoredFileInfoModel savedFileInfo = writeInputStreamToFile(
						in, directory, entry.getName());

				result.add(savedFileInfo);
			}

			in.close();

			return result;

		} catch (IOException e) {

			logger.error("unarchive failed!", e);

			throw e;
		} catch (ArchiveException e) {

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
	public ArrayList<IOUtilsStoredFileInfoModel> unarchive(
			String absoluteFileName) throws IOException, ArchiveException {

		File archiveFile = new File(absoluteFileName);

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
	public Document readXMLDocument(String ablsoluteXMLDocumentPath) throws ParserConfigurationException, SAXException, IOException {
		
		logger.info(String.format("readXMLDocument: Starting to read XML document %s", ablsoluteXMLDocumentPath));
		
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.parse(ablsoluteXMLDocumentPath);
		
		logger.info(String.format("readXMLDocument: Done to read XML document %s", ablsoluteXMLDocumentPath));
		
		return doc;
	}
}
