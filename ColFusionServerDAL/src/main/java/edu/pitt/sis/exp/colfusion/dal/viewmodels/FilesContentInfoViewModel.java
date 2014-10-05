/**
 * 
 */
package edu.pitt.sis.exp.colfusion.dal.viewmodels;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Wraps into list {@link FileContentInfoViewModel} to be passed e.g. to/from wizard last steps where we ask user to provide metadata on a column level.
 * 
 * @author Evgeny
 *
 */
@XmlRootElement
public class FilesContentInfoViewModel {
	
	/**
	 * To which store the files belong.
	 */
	private int sid;
	
	/**
	 * Who provides the data (e.g. the user who requests the data or who provided metadata. 
	 */
	private int userId;
	
	/**
	 * The files for which meatadata was provided/requested
	 */
	private ArrayList<FileContentInfoViewModel> files;
	
	public FilesContentInfoViewModel() {
		setFiles(new ArrayList<FileContentInfoViewModel>());
	}
	
	public FilesContentInfoViewModel(final int sid, final int userId, final ArrayList<FileContentInfoViewModel> files) {
		setSid(sid);
		setUserId(userId);
		setFiles(files);
	}

	/**
	 * @return the sid
	 */
	public int getSid() {
		return sid;
	}

	/**
	 * @param sid the sid to set
	 */
	public void setSid(final int sid) {
		this.sid = sid;
	}

	/**
	 * @return the userId
	 */
	public int getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(final int userId) {
		this.userId = userId;
	}

	/**
	 * @return the files
	 */
	public ArrayList<FileContentInfoViewModel> getFiles() {
		return files;
	}

	/**
	 * @param files the files to set
	 */
	public void setFiles(final ArrayList<FileContentInfoViewModel> files) {
		this.files = files;
	}
}
