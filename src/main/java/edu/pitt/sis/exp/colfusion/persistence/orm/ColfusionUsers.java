package edu.pitt.sis.exp.colfusion.persistence.orm;

// Generated Feb 28, 2014 5:25:41 PM by Hibernate Tools 3.4.0.CR1

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * ColfusionUsers generated by hbm2java
 */
public class ColfusionUsers implements java.io.Serializable {

	private Integer userId;
	private String userLogin;
	private String userLevel;
	private Date userModification;
	private Date userDate;
	private String userPass;
	private String userEmail;
	private String userNames;
	private BigDecimal userKarma;
	private String userUrl;
	private Date userLastlogin;
	private String userAim;
	private String userMsn;
	private String userYahoo;
	private String userGtalk;
	private String userSkype;
	private String userIrc;
	private String publicEmail;
	private String userAvatarSource;
	private String userIp;
	private String userLastip;
	private Date lastResetRequest;
	private Date lastEmailFriend;
	private String lastResetCode;
	private String userLocation;
	private String userOccupation;
	private String userCategories;
	private boolean userEnabled;
	private String userLanguage;
	private Boolean statusSwitch;
	private Boolean statusFriends;
	private Boolean statusStory;
	private Boolean statusComment;
	private Boolean statusEmail;
	private Boolean statusGroup;
	private Boolean statusAllFriends;
	private String statusFriendList;
	private String statusExcludes;
	private Set colfusionSourceinfoUsers = new HashSet(0);
	private Set colfusionDnameinfoMetadataEditHistories = new HashSet(0);
	private Set colfusionCanvaseses = new HashSet(0);
	private Set colfusionShareses = new HashSet(0);
	private Set colfusionNotificationses = new HashSet(0);
	private Set colfusionSourceinfoMetadataEditHistories = new HashSet(0);
	private Set colfusionUserRelationshipVerdicts = new HashSet(0);
	private Set colfusionSourceinfos = new HashSet(0);
	private Set colfusionSourceinfos_1 = new HashSet(0);
	private Set colfusionSynonymsFroms = new HashSet(0);
	private Set colfusionDesAttachmentses = new HashSet(0);
	private Set colfusionRelationshipses = new HashSet(0);
	private Set colfusionSynonymsTos = new HashSet(0);

	public ColfusionUsers() {
	}

	public ColfusionUsers(Date userModification, Date userDate,
			Date userLastlogin, Date lastResetRequest, Date lastEmailFriend,
			boolean userEnabled) {
		this.userModification = userModification;
		this.userDate = userDate;
		this.userLastlogin = userLastlogin;
		this.lastResetRequest = lastResetRequest;
		this.lastEmailFriend = lastEmailFriend;
		this.userEnabled = userEnabled;
	}

	public ColfusionUsers(String userLogin, String userLevel,
			Date userModification, Date userDate, String userPass,
			String userEmail, String userNames, BigDecimal userKarma,
			String userUrl, Date userLastlogin, String userAim, String userMsn,
			String userYahoo, String userGtalk, String userSkype,
			String userIrc, String publicEmail, String userAvatarSource,
			String userIp, String userLastip, Date lastResetRequest,
			Date lastEmailFriend, String lastResetCode, String userLocation,
			String userOccupation, String userCategories, boolean userEnabled,
			String userLanguage, Boolean statusSwitch, Boolean statusFriends,
			Boolean statusStory, Boolean statusComment, Boolean statusEmail,
			Boolean statusGroup, Boolean statusAllFriends,
			String statusFriendList, String statusExcludes,
			Set colfusionSourceinfoUsers,
			Set colfusionDnameinfoMetadataEditHistories,
			Set colfusionCanvaseses, Set colfusionShareses,
			Set colfusionNotificationses,
			Set colfusionSourceinfoMetadataEditHistories,
			Set colfusionUserRelationshipVerdicts, Set colfusionSourceinfos,
			Set colfusionSourceinfos_1, Set colfusionSynonymsFroms,
			Set colfusionDesAttachmentses, Set colfusionRelationshipses,
			Set colfusionSynonymsTos) {
		this.userLogin = userLogin;
		this.userLevel = userLevel;
		this.userModification = userModification;
		this.userDate = userDate;
		this.userPass = userPass;
		this.userEmail = userEmail;
		this.userNames = userNames;
		this.userKarma = userKarma;
		this.userUrl = userUrl;
		this.userLastlogin = userLastlogin;
		this.userAim = userAim;
		this.userMsn = userMsn;
		this.userYahoo = userYahoo;
		this.userGtalk = userGtalk;
		this.userSkype = userSkype;
		this.userIrc = userIrc;
		this.publicEmail = publicEmail;
		this.userAvatarSource = userAvatarSource;
		this.userIp = userIp;
		this.userLastip = userLastip;
		this.lastResetRequest = lastResetRequest;
		this.lastEmailFriend = lastEmailFriend;
		this.lastResetCode = lastResetCode;
		this.userLocation = userLocation;
		this.userOccupation = userOccupation;
		this.userCategories = userCategories;
		this.userEnabled = userEnabled;
		this.userLanguage = userLanguage;
		this.statusSwitch = statusSwitch;
		this.statusFriends = statusFriends;
		this.statusStory = statusStory;
		this.statusComment = statusComment;
		this.statusEmail = statusEmail;
		this.statusGroup = statusGroup;
		this.statusAllFriends = statusAllFriends;
		this.statusFriendList = statusFriendList;
		this.statusExcludes = statusExcludes;
		this.colfusionSourceinfoUsers = colfusionSourceinfoUsers;
		this.colfusionDnameinfoMetadataEditHistories = colfusionDnameinfoMetadataEditHistories;
		this.colfusionCanvaseses = colfusionCanvaseses;
		this.colfusionShareses = colfusionShareses;
		this.colfusionNotificationses = colfusionNotificationses;
		this.colfusionSourceinfoMetadataEditHistories = colfusionSourceinfoMetadataEditHistories;
		this.colfusionUserRelationshipVerdicts = colfusionUserRelationshipVerdicts;
		this.colfusionSourceinfos = colfusionSourceinfos;
		this.colfusionSourceinfos_1 = colfusionSourceinfos_1;
		this.colfusionSynonymsFroms = colfusionSynonymsFroms;
		this.colfusionDesAttachmentses = colfusionDesAttachmentses;
		this.colfusionRelationshipses = colfusionRelationshipses;
		this.colfusionSynonymsTos = colfusionSynonymsTos;
	}

	public Integer getUserId() {
		return this.userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUserLogin() {
		return this.userLogin;
	}

	public void setUserLogin(String userLogin) {
		this.userLogin = userLogin;
	}

	public String getUserLevel() {
		return this.userLevel;
	}

	public void setUserLevel(String userLevel) {
		this.userLevel = userLevel;
	}

	public Date getUserModification() {
		return this.userModification;
	}

	public void setUserModification(Date userModification) {
		this.userModification = userModification;
	}

	public Date getUserDate() {
		return this.userDate;
	}

	public void setUserDate(Date userDate) {
		this.userDate = userDate;
	}

	public String getUserPass() {
		return this.userPass;
	}

	public void setUserPass(String userPass) {
		this.userPass = userPass;
	}

	public String getUserEmail() {
		return this.userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUserNames() {
		return this.userNames;
	}

	public void setUserNames(String userNames) {
		this.userNames = userNames;
	}

	public BigDecimal getUserKarma() {
		return this.userKarma;
	}

	public void setUserKarma(BigDecimal userKarma) {
		this.userKarma = userKarma;
	}

	public String getUserUrl() {
		return this.userUrl;
	}

	public void setUserUrl(String userUrl) {
		this.userUrl = userUrl;
	}

	public Date getUserLastlogin() {
		return this.userLastlogin;
	}

	public void setUserLastlogin(Date userLastlogin) {
		this.userLastlogin = userLastlogin;
	}

	public String getUserAim() {
		return this.userAim;
	}

	public void setUserAim(String userAim) {
		this.userAim = userAim;
	}

	public String getUserMsn() {
		return this.userMsn;
	}

	public void setUserMsn(String userMsn) {
		this.userMsn = userMsn;
	}

	public String getUserYahoo() {
		return this.userYahoo;
	}

	public void setUserYahoo(String userYahoo) {
		this.userYahoo = userYahoo;
	}

	public String getUserGtalk() {
		return this.userGtalk;
	}

	public void setUserGtalk(String userGtalk) {
		this.userGtalk = userGtalk;
	}

	public String getUserSkype() {
		return this.userSkype;
	}

	public void setUserSkype(String userSkype) {
		this.userSkype = userSkype;
	}

	public String getUserIrc() {
		return this.userIrc;
	}

	public void setUserIrc(String userIrc) {
		this.userIrc = userIrc;
	}

	public String getPublicEmail() {
		return this.publicEmail;
	}

	public void setPublicEmail(String publicEmail) {
		this.publicEmail = publicEmail;
	}

	public String getUserAvatarSource() {
		return this.userAvatarSource;
	}

	public void setUserAvatarSource(String userAvatarSource) {
		this.userAvatarSource = userAvatarSource;
	}

	public String getUserIp() {
		return this.userIp;
	}

	public void setUserIp(String userIp) {
		this.userIp = userIp;
	}

	public String getUserLastip() {
		return this.userLastip;
	}

	public void setUserLastip(String userLastip) {
		this.userLastip = userLastip;
	}

	public Date getLastResetRequest() {
		return this.lastResetRequest;
	}

	public void setLastResetRequest(Date lastResetRequest) {
		this.lastResetRequest = lastResetRequest;
	}

	public Date getLastEmailFriend() {
		return this.lastEmailFriend;
	}

	public void setLastEmailFriend(Date lastEmailFriend) {
		this.lastEmailFriend = lastEmailFriend;
	}

	public String getLastResetCode() {
		return this.lastResetCode;
	}

	public void setLastResetCode(String lastResetCode) {
		this.lastResetCode = lastResetCode;
	}

	public String getUserLocation() {
		return this.userLocation;
	}

	public void setUserLocation(String userLocation) {
		this.userLocation = userLocation;
	}

	public String getUserOccupation() {
		return this.userOccupation;
	}

	public void setUserOccupation(String userOccupation) {
		this.userOccupation = userOccupation;
	}

	public String getUserCategories() {
		return this.userCategories;
	}

	public void setUserCategories(String userCategories) {
		this.userCategories = userCategories;
	}

	public boolean isUserEnabled() {
		return this.userEnabled;
	}

	public void setUserEnabled(boolean userEnabled) {
		this.userEnabled = userEnabled;
	}

	public String getUserLanguage() {
		return this.userLanguage;
	}

	public void setUserLanguage(String userLanguage) {
		this.userLanguage = userLanguage;
	}

	public Boolean getStatusSwitch() {
		return this.statusSwitch;
	}

	public void setStatusSwitch(Boolean statusSwitch) {
		this.statusSwitch = statusSwitch;
	}

	public Boolean getStatusFriends() {
		return this.statusFriends;
	}

	public void setStatusFriends(Boolean statusFriends) {
		this.statusFriends = statusFriends;
	}

	public Boolean getStatusStory() {
		return this.statusStory;
	}

	public void setStatusStory(Boolean statusStory) {
		this.statusStory = statusStory;
	}

	public Boolean getStatusComment() {
		return this.statusComment;
	}

	public void setStatusComment(Boolean statusComment) {
		this.statusComment = statusComment;
	}

	public Boolean getStatusEmail() {
		return this.statusEmail;
	}

	public void setStatusEmail(Boolean statusEmail) {
		this.statusEmail = statusEmail;
	}

	public Boolean getStatusGroup() {
		return this.statusGroup;
	}

	public void setStatusGroup(Boolean statusGroup) {
		this.statusGroup = statusGroup;
	}

	public Boolean getStatusAllFriends() {
		return this.statusAllFriends;
	}

	public void setStatusAllFriends(Boolean statusAllFriends) {
		this.statusAllFriends = statusAllFriends;
	}

	public String getStatusFriendList() {
		return this.statusFriendList;
	}

	public void setStatusFriendList(String statusFriendList) {
		this.statusFriendList = statusFriendList;
	}

	public String getStatusExcludes() {
		return this.statusExcludes;
	}

	public void setStatusExcludes(String statusExcludes) {
		this.statusExcludes = statusExcludes;
	}

	public Set getColfusionSourceinfoUsers() {
		return this.colfusionSourceinfoUsers;
	}

	public void setColfusionSourceinfoUsers(Set colfusionSourceinfoUsers) {
		this.colfusionSourceinfoUsers = colfusionSourceinfoUsers;
	}

	public Set getColfusionDnameinfoMetadataEditHistories() {
		return this.colfusionDnameinfoMetadataEditHistories;
	}

	public void setColfusionDnameinfoMetadataEditHistories(
			Set colfusionDnameinfoMetadataEditHistories) {
		this.colfusionDnameinfoMetadataEditHistories = colfusionDnameinfoMetadataEditHistories;
	}

	public Set getColfusionCanvaseses() {
		return this.colfusionCanvaseses;
	}

	public void setColfusionCanvaseses(Set colfusionCanvaseses) {
		this.colfusionCanvaseses = colfusionCanvaseses;
	}

	public Set getColfusionShareses() {
		return this.colfusionShareses;
	}

	public void setColfusionShareses(Set colfusionShareses) {
		this.colfusionShareses = colfusionShareses;
	}

	public Set getColfusionNotificationses() {
		return this.colfusionNotificationses;
	}

	public void setColfusionNotificationses(Set colfusionNotificationses) {
		this.colfusionNotificationses = colfusionNotificationses;
	}

	public Set getColfusionSourceinfoMetadataEditHistories() {
		return this.colfusionSourceinfoMetadataEditHistories;
	}

	public void setColfusionSourceinfoMetadataEditHistories(
			Set colfusionSourceinfoMetadataEditHistories) {
		this.colfusionSourceinfoMetadataEditHistories = colfusionSourceinfoMetadataEditHistories;
	}

	public Set getColfusionUserRelationshipVerdicts() {
		return this.colfusionUserRelationshipVerdicts;
	}

	public void setColfusionUserRelationshipVerdicts(
			Set colfusionUserRelationshipVerdicts) {
		this.colfusionUserRelationshipVerdicts = colfusionUserRelationshipVerdicts;
	}

	public Set getColfusionSourceinfos() {
		return this.colfusionSourceinfos;
	}

	public void setColfusionSourceinfos(Set colfusionSourceinfos) {
		this.colfusionSourceinfos = colfusionSourceinfos;
	}

	public Set getColfusionSourceinfos_1() {
		return this.colfusionSourceinfos_1;
	}

	public void setColfusionSourceinfos_1(Set colfusionSourceinfos_1) {
		this.colfusionSourceinfos_1 = colfusionSourceinfos_1;
	}

	public Set getColfusionSynonymsFroms() {
		return this.colfusionSynonymsFroms;
	}

	public void setColfusionSynonymsFroms(Set colfusionSynonymsFroms) {
		this.colfusionSynonymsFroms = colfusionSynonymsFroms;
	}

	public Set getColfusionDesAttachmentses() {
		return this.colfusionDesAttachmentses;
	}

	public void setColfusionDesAttachmentses(Set colfusionDesAttachmentses) {
		this.colfusionDesAttachmentses = colfusionDesAttachmentses;
	}

	public Set getColfusionRelationshipses() {
		return this.colfusionRelationshipses;
	}

	public void setColfusionRelationshipses(Set colfusionRelationshipses) {
		this.colfusionRelationshipses = colfusionRelationshipses;
	}

	public Set getColfusionSynonymsTos() {
		return this.colfusionSynonymsTos;
	}

	public void setColfusionSynonymsTos(Set colfusionSynonymsTos) {
		this.colfusionSynonymsTos = colfusionSynonymsTos;
	}

}
