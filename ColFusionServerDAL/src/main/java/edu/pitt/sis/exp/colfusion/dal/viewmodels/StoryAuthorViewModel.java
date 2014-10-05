/**
 * 
 */
package edu.pitt.sis.exp.colfusion.dal.viewmodels;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Evgeny
 *
 */
@XmlRootElement
public class StoryAuthorViewModel {
	private int userId;
	private String firstName;
	private String lastName;
	private String login;
	private String avatarSource;
	private BigDecimal karma;
	private int roleId;
	
	public StoryAuthorViewModel() {
		
	}
	
	public StoryAuthorViewModel(final int userId, final String lastName, final String login, final String avatarSource, final BigDecimal karma, final int roleId) {
		setUserId(userId);
		setFirstName(firstName);
		setLastName(lastName);
		setLogin(login);
		setAvatarSource(avatarSource);
		setKarma(karma);
		setRoleId(roleId);
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
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}
	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(final String firstName) {
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
	public void setLastName(final String lastName) {
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
	public void setLogin(final String login) {
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
	public void setAvatarSource(final String avatarSource) {
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
	public void setKarma(final BigDecimal karma) {
		this.karma = karma;
	}
	/**
	 * @return the storyUserRoleId
	 */
	public int getRoleId() {
		return roleId;
	}
	/**
	 * @param storyUserRoleId the storyUserRoleId to set
	 */
	public void setRoleId(final int roleId) {
		this.roleId = roleId;
	}
}
