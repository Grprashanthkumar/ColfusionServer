/**
 * 
 */
package edu.pitt.sis.exp.colfusion.importers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.POIXMLException;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import edu.pitt.sis.exp.colfusion.dal.viewmodels.DatasetVariableViewModel;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.FileContentInfoViewModel;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.PreviewFileViewModel;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.WorksheetDataRowViewModel;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.WorksheetDataViewModel;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.WorksheetViewModel;
import edu.pitt.sis.exp.colfusion.utils.models.IOUtilsStoredFileInfoModel;

/**
 * @author Evgeny
 *
 */
//TODO: maybe make it as Singleton
public class ExcelImporter implements Importer {
	final Logger logger = LogManager.getLogger(ExcelImporter.class.getName());
	
	@Override
	public Collection<WorksheetViewModel> getTables(final IOUtilsStoredFileInfoModel fileModel) throws IOException, IllegalArgumentException, POIXMLException {
		try {
	    	ArrayList<WorksheetViewModel> result = new ArrayList<>();
			
	        File file = new File(fileModel.getAbsoluteFileName());
	        InputStream is = new FileInputStream(file);
	        boolean xmlBased = "xlsx".equals(fileModel.getFileExtension());
	        try {
	            Workbook wb = xmlBased ?
	                    new XSSFWorkbook(is) :
	                        new HSSFWorkbook(new POIFSFileSystem(is));
	
	            int sheetCount = wb.getNumberOfSheets();
	            
	            for (int i = 0; i < sheetCount; i++) {
	                Sheet sheet = wb.getSheetAt(i);
	                int rows = sheet.getLastRowNum() - sheet.getFirstRowNum() + 1;
	
	                WorksheetViewModel worksheet = new WorksheetViewModel();
	                worksheet.setSheetName(sheet.getSheetName());
	                worksheet.setNumberOfRows(rows);
	                worksheet.setHeaderRow(sheet.getFirstRowNum() +1);
	                worksheet.setStartColumn("A");
	                worksheet.setIndexInTheFile(i);
	                result.add(worksheet); 
	            }
	        } finally {
	            is.close();
	        }
	        
	        return result;
                           
        } catch (IOException e) {
            logger.error("Error getting sheet data for Excel file", e);
            throw e;
        } catch (IllegalArgumentException e) {
            logger.error("Error getting sheet data for Excel file (only Excel 97 & later supported)", e);
            throw e;
        } catch (POIXMLException e) {
            logger.error("Error getting sheet data for Excel file - invalid XML", e);
            throw e;
        }
		
	}
	
	@Override
	public HashMap<String, ArrayList<DatasetVariableViewModel>> readVariables(final FileContentInfoViewModel fileAndSheetsInfo) throws Exception {
		
		try {
			HashMap<String, ArrayList<DatasetVariableViewModel>> result = new HashMap<>();
			
	        File file = new File(fileAndSheetsInfo.getFileAbsoluteName());
	        InputStream is = new FileInputStream(file);
	        boolean xmlBased = "xlsx".equals(fileAndSheetsInfo.getExtension());
	        try {
	            Workbook wb = xmlBased ?
	                    new XSSFWorkbook(is) :
	                        new HSSFWorkbook(new POIFSFileSystem(is));
	
	            int sheetCount = wb.getNumberOfSheets();
	                    
	            //For each user selected sheet, get variables from the header row
	            for (WorksheetViewModel worksheet : fileAndSheetsInfo.getWorksheets()) {
	            	
	            	ArrayList<DatasetVariableViewModel> oneSheetResult = new ArrayList<>();
	            	
	            	if (worksheet.getIndexInTheFile() > sheetCount) {
	            		logger.error("Requested sheet is out of number of sheets in the file");
	            		
	            		throw new Exception("Requested sheet is out of number of sheets in the file");
	            	}
	            	
	            	Sheet sheet = wb.getSheetAt(worksheet.getIndexInTheFile());
	            	
	            	if (worksheet.getHeaderRow() > sheet.getLastRowNum() + 1) {
        				logger.error("Requested header row is out of number of rows in the sheet");
	            		
	            		throw new Exception("Requested header row is out of number of rows in the sheet");
	            	}
	            	
	            	org.apache.poi.ss.usermodel.Row row = sheet.getRow(worksheet.getHeaderRow() - 1);
	            	
	            	if (row != null) {
                        short lastCell = row.getLastCellNum();
                        //TODO: right now reads all columns, need read starting from user specified.
                        for (short cellIndex = 0; cellIndex < lastCell; cellIndex++) {
                        	DatasetVariableViewModel datasetVariable = null;
                            
                            org.apache.poi.ss.usermodel.Cell sourceCell = row.getCell(cellIndex);
                            if (sourceCell != null) {
                            	datasetVariable = extractVariableCell(sourceCell);
                            	
                            	if (datasetVariable != null) {
                            		oneSheetResult.add(datasetVariable);
                            	}
                            }
                            
                        }
                        
                        result.put(worksheet.getSheetName(), oneSheetResult);
                    }
	            	else {
	            		//TODO: send message back to the client that the row number might be wrong.
	            	}
	            	
	            }
	            
	        } finally {
	            is.close();
	        }
	        
	        return result;
                           
        } catch (IOException e) {
            logger.error("Error getting sheet data for Excel file", e);
            throw e;
        } catch (IllegalArgumentException e) {
            logger.error("Error getting sheet data for Excel file (only Excel 97 & later supported)", e);
            throw e;
        } catch (POIXMLException e) {
            logger.error("Error getting sheet data for Excel file - invalid XML", e);
            throw e;
        }
	}

	/**
	 * Some code have been borrows from ExcelImporter.java from OpenRefine.
	 * 
	 * @param sourceCell
	 * @return
	 * 
	 */
	//TODO: change method to return cell value and type, not the dataset view model.
	private DatasetVariableViewModel extractVariableCell(final org.apache.poi.ss.usermodel.Cell cell) {
		DatasetVariableViewModel result = new DatasetVariableViewModel();
		
		//TODO: try to also extract other metadata about the value/column
		
		int cellType = cell.getCellType();
        if (cellType == org.apache.poi.ss.usermodel.Cell.CELL_TYPE_FORMULA) {
            cellType = cell.getCachedFormulaResultType();
        }
        if (cellType == org.apache.poi.ss.usermodel.Cell.CELL_TYPE_ERROR ||
            cellType == org.apache.poi.ss.usermodel.Cell.CELL_TYPE_BLANK) {
            return null;
        }
        
        Serializable value = null;
        if (cellType == org.apache.poi.ss.usermodel.Cell.CELL_TYPE_BOOLEAN) {
            value = cell.getBooleanCellValue();
            //TODO: the types should be enums, not hardwired strings.
            result.setVariableValueType("boolean");
        } else if (cellType == org.apache.poi.ss.usermodel.Cell.CELL_TYPE_NUMERIC) {
            double d = cell.getNumericCellValue();
            
            if (HSSFDateUtil.isCellDateFormatted(cell)) {
                value = HSSFDateUtil.getJavaDate(d);
                result.setVariableValueType("date");
            } else {
                value = d;
                result.setVariableValueType("number");
            }
        } else {
            String text = cell.getStringCellValue();
            if (text.length() > 0) {
                value = text;
            }
            
            result.setVariableValueType("text");
        }
        
        if (value == null) {
			return null;
		}
        
        result.setOriginalName(value.toString());
        result.setChosenName(value.toString());
        
		return result;
	}

	@Override
	public ArrayList<WorksheetDataViewModel> readWorksheetData(final PreviewFileViewModel previewFileViewModel) throws IOException {
		
		ArrayList<WorksheetDataViewModel> result = new ArrayList<WorksheetDataViewModel>();
		
		int startRow = previewFileViewModel.getPreviewRowsPerPage() * (previewFileViewModel.getPreviewPage() - 1);
		int endRow = startRow + previewFileViewModel.getPreviewRowsPerPage();
		
		File file = new File(previewFileViewModel.getFileAbsoluteName());
        InputStream is = new FileInputStream(file);
        boolean xmlBased = "xlsx".equals(FilenameUtils.getExtension(previewFileViewModel.getFileAbsoluteName()));
        try { //TODO:add catch
            Workbook wb = xmlBased ?
                    new XSSFWorkbook(is) :
                        new HSSFWorkbook(new POIFSFileSystem(is));

            int sheetCount = wb.getNumberOfSheets();
            
            for (int i = 0; i < sheetCount; i++) {
            	Sheet sheet = wb.getSheetAt(i);
                int rows = sheet.getLastRowNum();
                
                WorksheetDataViewModel worksheetDataViewModel = new WorksheetDataViewModel();
                worksheetDataViewModel.setWorksheetName(sheet.getSheetName());
                
                ArrayList<WorksheetDataRowViewModel> worksheetData = new ArrayList<>(); 
                
                for (int j = startRow; j <= endRow && j <= rows ; j++) {
                	
                	org.apache.poi.ss.usermodel.Row row = sheet.getRow(j);
                	
                	short lastCell = row.getLastCellNum();
                	
                	WorksheetDataRowViewModel rowCells = new WorksheetDataRowViewModel();
                	
                	for (int k = 0; k < lastCell; k++) {
                		
                		org.apache.poi.ss.usermodel.Cell sourceCell = row.getCell(k);
                		
                		DatasetVariableViewModel datasetVariable = extractVariableCell(sourceCell);
                		
                		rowCells.getWorksheetDataRow().add(datasetVariable.getOriginalName());
					}
          	
                	worksheetData.add(rowCells);
				}
               
                worksheetDataViewModel.setWorksheetData(worksheetData);
                
                result.add(worksheetDataViewModel);
			}
        }
        finally {
        	is.close();
        }
		
        return result;
	}
}
