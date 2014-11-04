package edu.pitt.sis.exp.colfusion.war.infra;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

import edu.pitt.sis.exp.colfusion.war.rest.OpenRefineRestServiceImpl;
import edu.pitt.sis.exp.colfusion.war.rest.RelationshipRestServiceImpl;
import edu.pitt.sis.exp.colfusion.war.rest.SimilarityJoinRestServiceImpl;
import edu.pitt.sis.exp.colfusion.war.rest.StoryRestServiceImpl;
import edu.pitt.sis.exp.colfusion.war.rest.TestResourceImpl;
import edu.pitt.sis.exp.colfusion.war.rest.UserRestServiceImpl;
import edu.pitt.sis.exp.colfusion.war.rest.WizardRestServiceImpl;



public class ApplicationConfig extends Application {
	
	@Override
	public Set<Class<?>> getClasses() {
        final Set<Class<?>> resources = new HashSet<Class<?>>();

        // Add your resources.
        resources.add(WizardRestServiceImpl.class);
        resources.add(StoryRestServiceImpl.class);
        resources.add(UserRestServiceImpl.class);
        resources.add(RelationshipRestServiceImpl.class);
        resources.add(OpenRefineRestServiceImpl.class);
        resources.add(SimilarityJoinRestServiceImpl.class);
        
        resources.add(TestResourceImpl.class);
       
        // Add additional features such as support for Multipart.
//        resources.add(MultiPartFeature.class);
        
        return resources;
    }
	
}