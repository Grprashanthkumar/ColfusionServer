/**
 *
 */
package edu.pitt.sis.exp.colfusion.importers;

public enum ImporterType {
	ExcelImporter, CSVImporter;

	/**
	 * Gets {@link ImporterType} based on file extension.
	 * @param fileExtension extension of the file without dot.
	 * @return the {@link ImporterType}
	 */
	public static ImporterType getImporterType(final String fileExtension) {
		ImporterType importerType = null;

		if (fileExtension.equals("csv")) {
			importerType = ImporterType.CSVImporter;
		}
		else if (fileExtension.equals("xls") || fileExtension.equals("xlsx")) {
			importerType = ImporterType.ExcelImporter;
		}

		return importerType;
	}
}
