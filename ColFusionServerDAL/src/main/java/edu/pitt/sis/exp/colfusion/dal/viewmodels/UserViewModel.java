package edu.pitt.sis.exp.colfusion.dal.viewmodels;

import javax.xml.bind.annotation.XmlRootElement;

import com.google.gson.annotations.Expose;
/**
 * @author Weichuan Hong
 *
 */
@XmlRootElement
public class UserViewModel {
	@Expose private int userId;
	@Expose private String userLogin;
    private String userNames;
	
	public UserViewModel(){
		
	}
	
	 public UserViewModel(Integer userId, String  userNames){
	    	this.userId = userId;
	    	this.userNames = userNames;
	    }
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getUserLogin() {
		return userLogin;
	}
	public void setUserLogin(String userLogin) {
		this.userLogin = userLogin;
	}

	public String getUserNames() {
		return userNames;
	}

	public void setUserNames(String userNames) {
		this.userNames = userNames;
	}
	

}
