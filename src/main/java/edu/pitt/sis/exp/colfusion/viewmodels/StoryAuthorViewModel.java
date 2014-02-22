/**
 * 
 */
package edu.pitt.sis.exp.colfusion.viewmodels;

import java.math.BigDecimal;

/**
 * @author Evgeny
 *
 */
public class StoryAuthorViewModel {
	private int userId;
	private String firstName;
	private String lastName;
	private String login;
	private String avatarSource;
	private BigDecimal karma;
	private int storyUserRoleId;
	private String storyUserRoleName;
	private String storyUserRoleDescription;
	/**
	 * @return the userId
	 */
	public int getUserId() {
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(int userId) {
		this.userId = userId;
	}
	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}
	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}
	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	/**
	 * @return the login
	 */
	public String getLogin() {
		return login;
	}
	/**
	 * @param login the login to set
	 */
	public void setLogin(String login) {
		this.login = login;
	}
	/**
	 * @return the avatarSource
	 */
	public String getAvatarSource() {
		return avatarSource;
	}
	/**
	 * @param avatarSource the avatarSource to set
	 */
	public void setAvatarSource(String avatarSource) {
		this.avatarSource = avatarSource;
	}
	/**
	 * @return the karma
	 */
	public BigDecimal getKarma() {
		return karma;
	}
	/**
	 * @param karma the karma to set
	 */
	public void setKarma(BigDecimal karma) {
		this.karma = karma;
	}
	/**
	 * @return the storyUserRoleId
	 */
	public int getStoryUserRoleId() {
		return storyUserRoleId;
	}
	/**
	 * @param storyUserRoleId the storyUserRoleId to set
	 */
	public void setStoryUserRoleId(int storyUserRoleId) {
		this.storyUserRoleId = storyUserRoleId;
	}
	/**
	 * @return the storyUserRoleName
	 */
	public String getStoryUserRoleName() {
		return storyUserRoleName;
	}
	/**
	 * @param storyUserRoleName the storyUserRoleName to set
	 */
	public void setStoryUserRoleName(String storyUserRoleName) {
		this.storyUserRoleName = storyUserRoleName;
	}
	/**
	 * @return the storyUserRoleDescription
	 */
	public String getStoryUserRoleDescription() {
		return storyUserRoleDescription;
	}
	/**
	 * @param storyUserRoleDescription the storyUserRoleDescription to set
	 */
	public void setStoryUserRoleDescription(String storyUserRoleDescription) {
		this.storyUserRoleDescription = storyUserRoleDescription;
	}
}
