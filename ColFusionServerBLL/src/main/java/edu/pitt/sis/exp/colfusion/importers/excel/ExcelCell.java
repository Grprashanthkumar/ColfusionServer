package edu.pitt.sis.exp.colfusion.importers.excel;

import java.io.Serializable;

public interface ExcelCell {
	public Serializable getValue();

	public String getDataType();

	public boolean getIsInError();
}
