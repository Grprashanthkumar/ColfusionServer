package edu.pitt.sis.exp.colfusion.war.infra;

import javax.servlet.http.HttpServlet;

import com.wordnik.swagger.config.ConfigFactory;
import com.wordnik.swagger.config.ScannerFactory;
import com.wordnik.swagger.jaxrs.config.DefaultJaxrsScanner;
import com.wordnik.swagger.jaxrs.reader.DefaultJaxrsApiReader;
import com.wordnik.swagger.model.ApiInfo;
import com.wordnik.swagger.reader.ClassReaders;

public class Bootstrap extends HttpServlet {
    static {
        // do any additional initialization here, such as set your base path programmatically as such:
        // ConfigFactory.config().setBasePath("http://www.foo.com/");

        // add a custom filter
       // FilterFactory.setFilter(new CustomFilter());

        ApiInfo info = new ApiInfo(
                "Swagger Sample App",                             /* title */
                "This is a sample server Petstore server.  You can find out more about Swagger " +
                        "at <a href=\"http://swagger.wordnik.com\">http://swagger.wordnik.com</a> or on irc.freenode.net, #swagger.  For this sample, " +
                        "you can use the api key \"special-key\" to test the authorization filters",
                "http://helloreverb.com/terms/",                  /* TOS URL */
                "apiteam@wordnik.com",                            /* Contact */
                "Apache 2.0",                                     /* license */
                "http://www.apache.org/licenses/LICENSE-2.0.html" /* license URL */
        );

        ConfigFactory.config().setApiInfo(info);
        ConfigFactory.config().setApiVersion( "0.0.1" ); 
        ConfigFactory.config().setBasePath( "http://localhost:8080/ColFusionServer/rest" );
        ScannerFactory.setScanner( new DefaultJaxrsScanner( ) );
        ClassReaders.setReader( new DefaultJaxrsApiReader( ) );
    }
}
