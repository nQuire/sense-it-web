package org.greengin.senseitweb.rs;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/sense-it-web")
public class SenseItWebApp extends Application {

    public Set<Class<?>> getClasses() {
    	HashSet<Class<?>> classes = new HashSet<Class<?>>();
    	classes.add(JacksonObjectMapper.class);
    	return classes;
    }
}

