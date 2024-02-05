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

        Integer port = Integer.getInteger("server.port");

        if (port == null) {
            port = 8080;
            System.out.println("Using default port");
        }

        tomcat.setPort(port);
        tomcat.getConnector();

        Context context = tomcat.addContext("", null);

        WebApplicationContext applicationContext = createApplicationContext(context.getServletContext());
        DispatcherServlet dispatcherServlet = new DispatcherServlet(applicationContext);

        Wrapper servlet = Tomcat.addServlet(context, "myBankServlet", dispatcherServlet);
        servlet.setLoadOnStartup(1);
        servlet.addMapping("/*");

        tomcat.start();
    }

    private static WebApplicationContext createApplicationContext(ServletContext servletContext) {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();

        context.register(ApplicationConfiguration.class);
        context.setServletContext(servletContext);
        context.refresh();
        context.registerShutdownHook();

        return context;
    }
}
