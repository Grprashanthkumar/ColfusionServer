package edu.pitt.sis.exp.colfusion.dal.managers;


import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.NonUniqueResultException;

import edu.pitt.sis.exp.colfusion.dal.dao.AttachmentDAO;
import edu.pitt.sis.exp.colfusion.dal.dao.AttachmentDAOImpl;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionDesAttachments;
import edu.pitt.sis.exp.colfusion.dal.utils.HibernateUtil;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.AttachmentListViewModel;


public class AttachmentManagerImpl extends GeneralManagerImpl<AttachmentDAO, ColfusionDesAttachments, Integer> implements AttachmentManager{
	Logger logger = LogManager.getLogger(AttachmentManagerImpl.class.getName());
	
	public AttachmentManagerImpl() {
		super(new AttachmentDAOImpl(), ColfusionDesAttachments.class);
	}
	
	private final AttachmentDAO attachmentDao = new AttachmentDAOImpl();
	
	@Override
	public List<AttachmentListViewModel> getAttachmentListViewModel(final int sid) {
		try{
			HibernateUtil.beginTransaction();
	        
	        List<ColfusionDesAttachments> attachmentObjs = attachmentDao.findBySid(sid);
	        
	        List<AttachmentListViewModel> result = new ArrayList<>();
	        for(ColfusionDesAttachments attachmentObj : attachmentObjs){
	        	AttachmentListViewModel attachmentListViewModel =  new AttachmentListViewModel();
	        	attachmentListViewModel.setDescription(attachmentObj.getDescription());
	        	attachmentListViewModel.setFileId(attachmentObj.getFileId());
	        	attachmentListViewModel.setFilename(attachmentObj.getFilename());
	        	attachmentListViewModel.setSid(sid);
	        	attachmentListViewModel.setSize(attachmentObj.getSize());
	        	attachmentListViewModel.setTitle(attachmentObj.getTitle());
	        	attachmentListViewModel.setUploadTime(attachmentObj.getUploadTime());
	        	result.add(attachmentListViewModel);
	        }
	                        
	        HibernateUtil.commitTransaction();
	        
	        return result;
	    } catch (NonUniqueResultException ex) {
	
	    	HibernateUtil.rollbackTransaction();
	    	
	    	this.logger.error("getAttachmentListViewModel failed NonUniqueResultException", ex);
	        throw ex;
	    } catch (HibernateException ex) {
	
	    	HibernateUtil.rollbackTransaction();
	    	
	    	this.logger.error("getAttachmentListViewModel failed HibernateException", ex);
	    	throw ex;
	    }	
	}
}
