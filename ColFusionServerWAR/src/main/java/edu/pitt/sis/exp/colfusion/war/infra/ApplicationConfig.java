package edu.pitt.sis.exp.colfusion.war.infra;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

import edu.pitt.sis.exp.colfusion.war.rest.OpenRefineRestService;
import edu.pitt.sis.exp.colfusion.war.rest.RelationshipRestService;
import edu.pitt.sis.exp.colfusion.war.rest.SimilarityJoinRestService;
import edu.pitt.sis.exp.colfusion.war.rest.StoryRestService;
import edu.pitt.sis.exp.colfusion.war.rest.UserRestService;
import edu.pitt.sis.exp.colfusion.war.rest.VisualizationRestService;
import edu.pitt.sis.exp.colfusion.war.rest.WizardRestService;



public class ApplicationConfig extends Application {
	
	@Override
	public Set<Class<?>> getClasses() {
        final Set<Class<?>> resources = new HashSet<Class<?>>();

        // Add your resources.
        resources.add(WizardRestService.class);
        resources.add(StoryRestService.class);
        resources.add(UserRestService.class);
        resources.add(RelationshipRestService.class);
        resources.add(OpenRefineRestService.class);
        resources.add(SimilarityJoinRestService.class);
        resources.add(VisualizationRestService.class);
       
        // Add additional features such as support for Multipart.
//        resources.add(MultiPartFeature.class);
        
        return resources;
    }
	
}