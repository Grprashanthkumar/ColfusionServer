package edu.pitt.sis.exp.colfusion.dal.managers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.pitt.sis.exp.colfusion.dal.dao.LinksDAO;
import edu.pitt.sis.exp.colfusion.dal.dao.LinksDAOImpl;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionLinks;

public class LinksManagerImpl extends GeneralManagerImpl<LinksDAO, ColfusionLinks, Integer> implements LinksManager {

	Logger logger = LogManager.getLogger(LinksManagerImpl.class.getName());
	
	public LinksManagerImpl() {
		super(new LinksDAOImpl(), ColfusionLinks.class);
	}

}