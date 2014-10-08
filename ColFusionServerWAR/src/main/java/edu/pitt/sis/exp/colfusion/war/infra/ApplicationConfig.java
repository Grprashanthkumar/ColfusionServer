package edu.pitt.sis.exp.colfusion.war.infra;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

import org.glassfish.jersey.media.multipart.MultiPartFeature;

import edu.pitt.sis.exp.colfusion.war.rest.impl.OpenRefineRestServiceImpl;
import edu.pitt.sis.exp.colfusion.war.rest.impl.RelationshipRestServiceImpl;
import edu.pitt.sis.exp.colfusion.war.rest.impl.SimilarityJoinRestServiceImpl;
import edu.pitt.sis.exp.colfusion.war.rest.impl.StoryRestServiceImpl;
import edu.pitt.sis.exp.colfusion.war.rest.impl.UserRestServiceImpl;
import edu.pitt.sis.exp.colfusion.war.rest.impl.WizardRestServiceImpl;



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
       
        // Add additional features such as support for Multipart.
        resources.add(MultiPartFeature.class);

        return resources;
    }
}