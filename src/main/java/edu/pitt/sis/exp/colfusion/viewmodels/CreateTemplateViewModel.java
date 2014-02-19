/**
 * 
 */
package edu.pitt.sis.exp.colfusion.viewmodels;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Evgeny
 *
 */
@XmlRootElement
public class CreateTemplateViewModel {
	private String sid;
	
	private String fileMode;
	
	private List<OneUploadedItemViewModel> fileName;

	public CreateTemplateViewModel() {
		
	}
	
public CreateTemplateViewModel(String sid, String fileMode, List<OneUploadedItemViewModel> fileName) {
		setSid(sid);
		setFileMode(fileMode);
		setFileName(fileName);
	}
	
	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getFileMode() {
		return fileMode;
	}

	public void setFileMode(String fileMode) {
		this.fileMode = fileMode;
	}

	public List<OneUploadedItemViewModel> getFileName() {
		return fileName;
	}

	public void setFileName(List<OneUploadedItemViewModel> fileName) {
		this.fileName = fileName;
	}
	
}
