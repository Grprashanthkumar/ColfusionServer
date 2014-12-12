package edu.pitt.sis.exp.colfusion.war.infra;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.pitt.sis.exp.colfusion.utils.HibernateUtil;
import edu.pitt.sis.exp.colfusion.utils.StringUtils;

public class LoggingFilter implements javax.servlet.Filter {

	final Logger logger = LogManager.getLogger(LoggingFilter.class.getName());
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub		
	}

	@Override
	public void doFilter(final ServletRequest request, final ServletResponse response,
			final FilterChain chain) throws IOException, ServletException {
		String httpMethod = null;
		String fullRequestUriWithParameters = null;
		
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		
		if (httpRequest != null) {
			httpMethod = httpRequest.getMethod();
			String requestURI = httpRequest.getRequestURI();
			String queryString = httpRequest.getQueryString();
			
			fullRequestUriWithParameters = String.format("%s%s", requestURI, StringUtils.isSpecified(queryString) ? queryString : "");
			
			logger.info(String.format("BEGIN: %s %s", httpMethod, fullRequestUriWithParameters));
		}
		
		chain.doFilter(request, response);
		
		if (HibernateUtil.isTransactionOpen()) {
			String message = String.format("Was about to send the response, "
					+ "but found out that there an open transaction that needs to be handled. %s %s", 
					httpMethod, fullRequestUriWithParameters);
			logger.error(message);
			throw new RuntimeException(message);
		}
		
		logger.info(String.format("END: %s %s%s", httpMethod, fullRequestUriWithParameters, StringUtils.NEWLINE));	
	}

	@Override
	public void init(final FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub		
	}
}
