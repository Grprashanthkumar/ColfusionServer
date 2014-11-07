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
                "Colfusion Server",                             /* title */
                "This is the REST API of Colfusion Server. Col*Fusion (Collaborative Data Fusion) is an advanced infrastructure for systematic accumulation, integration and utilization of historical data. "
                + "It aims to support large-scale interdisciplinary research, where a comprehensive picture of the subject requires large amounts of historical data from disparate data sources from a variety of disciplines. "
                + "As an example, consider the task of exploring long-term and short-term social changes, which requires consolidation of a comprehensive set of data on social-scientific, health, and environmental dynamics. "
                + "While there are numerous historical data sets available from various groups worldwide, the existing data sources are principally oriented toward regional comparative efforts rather than global applications. "
                + "They vary widely both in content and format, and cannot be easily integrated and maintained by small groups of developers. Devising efficient and scalable methods for integration of the existing and emerging historical data sources is a considerable research challenge.",
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
