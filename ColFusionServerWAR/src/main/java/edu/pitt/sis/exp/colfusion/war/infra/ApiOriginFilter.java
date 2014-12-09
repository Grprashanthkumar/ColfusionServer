package edu.pitt.sis.exp.colfusion.war.infra;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

public class ApiOriginFilter implements javax.servlet.Filter {
  @Override
  public void doFilter(final ServletRequest request, final ServletResponse response,
      final FilterChain chain) throws IOException, ServletException {
    HttpServletResponse res = (HttpServletResponse) response;
    
    if (res != null) {
    	setCORSHeaders(res);
    }
    
    chain.doFilter(request, response);
  }
  
  public static void setCORSHeaders(final HttpServletResponse res) {
	  res.addHeader("Access-Control-Allow-Origin", "*");
      res.addHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT");
      res.addHeader("Access-Control-Allow-Headers", "Content-Type");
  }

  @Override
  public void destroy() {
  }

  @Override
  public void init(final FilterConfig filterConfig) throws ServletException {
  }
}