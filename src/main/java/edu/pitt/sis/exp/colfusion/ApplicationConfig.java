package edu.pitt.sis.exp.colfusion;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

import org.glassfish.jersey.media.multipart.MultiPartFeature;

import edu.pitt.sis.exp.colfusion.controllers.StoryController;
import edu.pitt.sis.exp.colfusion.controllers.WizardController;

public class ApplicationConfig extends Application {

    public Set<Class<?>> getClasses() {
        final Set<Class<?>> resources = new HashSet<Class<?>>();

        // Add your resources.
        resources.add(WizardController.class);
        resources.add(StoryController.class);

        // Add additional features such as support for Multipart.
        resources.add(MultiPartFeature.class);

        return resources;
    }
}