package edu.pitt.sis.exp.colfusion.servicemonitor;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.pitt.sis.exp.colfusion.utils.ConfigManager;
import edu.pitt.sis.exp.colfusion.utils.PropertyKeys;

/**
 * @author Hao Bai
 * 
 * This class is used for sending email notification to users
 * who have certain level when the status of some service is changed.
 */
public class EmailNotifier {

    private static String senderAddress = ConfigManager.getInstance().getPropertyByName(PropertyKeys.emailSenderAddress);
    private static String senderPassword = ConfigManager.getInstance().getPropertyByName(PropertyKeys.emailSenderPassword);
    private static String smtpHost = ConfigManager.getInstance().getPropertyByName(PropertyKeys.smtpHost);
    
    private Logger logger = LogManager.getLogger(ServiceMonitor.class.getName());
    
	public EmailNotifier(){
	}
	
	public void sendMail(String toAddress, String mailSubject, String mailText){
		Properties properties = new Properties();
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.host", smtpHost);
		properties.put("mail.smtp.port", "587");

        Session session = Session.getInstance(properties,
        	new javax.mail.Authenticator() {
            	protected PasswordAuthentication getPasswordAuthentication() {
           			return new PasswordAuthentication(senderAddress, senderPassword);
            	}
          	});

        try{
        	Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderAddress));
            message.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse(toAddress));
            message.setSubject(mailSubject);
            message.setText(mailText);

            Transport.send(message);
        }
		catch(MessagingException exception){
			logger.error("In EmailNotifier.sendMail()\n"
					+exception.toString()+" "+exception.getMessage()+" "+exception.getCause());
		}
	}	
}
