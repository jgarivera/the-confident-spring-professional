package com.jgarivera;

import com.jgarivera.context.ApplicationConfiguration;
import jakarta.servlet.ServletContext;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Wrapper;
import org.apache.catalina.startup.Tomcat;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class ApplicationLauncher {

    public static void main(String[] args) throws LifecycleException {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8080);

        // Seems like it does nothing, but it bootstraps the HTTP engine
        // I expected it to just get the existing connector (even if there are none)
        // This method name is violation of the Principle of Least Astonishment (!)
        tomcat.getConnector();

        // We only have one app, so no need to set `contextPath` (such as "/webapp1" or "/webapp2")
        // `docBase` is null because we are not going to serve any static files
        Context context = tomcat.addContext("", null);

        // This is a web-aware Spring application context intended for web applications
        WebApplicationContext applicationContext = createApplicationContext(context.getServletContext());

        // This is the only entrypoint for Spring MVC
        DispatcherServlet dispatcherServlet = new DispatcherServlet(applicationContext);

        Wrapper servlet = Tomcat.addServlet(context, "myFirstServlet", dispatcherServlet);

        // Set as 1 to immediately start servlet
        servlet.setLoadOnStartup(1);

        // Respond to any request that starts with "/"
        servlet.addMapping("/*");

        tomcat.start();
    }

    private static WebApplicationContext createApplicationContext(ServletContext servletContext) {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();

        context.register(ApplicationConfiguration.class);
        context.setServletContext(servletContext);

        // Start the application context manually
        context.refresh();
        context.registerShutdownHook();

        return context;
    }
}
