package edu.pitt.sis.exp.colfusion.dal.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Query;

import edu.pitt.sis.exp.colfusion.dal.orm.Statonverdicts;
import edu.pitt.sis.exp.colfusion.dal.orm.StatonverdictsId;

public interface StatonverdictsDAO {
	public Map<Integer,BigDecimal> getAvgConfidence();
}
