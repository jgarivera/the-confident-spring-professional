package com.jgarivera;

import com.jgarivera.context.Application;
import com.jgarivera.web.MyFancyPdfInvoicesServlet;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Wrapper;
import org.apache.catalina.startup.Tomcat;

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
        Wrapper servlet = Tomcat.addServlet(context, "myFirstServlet",
                new MyFancyPdfInvoicesServlet(Application.invoiceService, Application.objectMapper));

        // Set as 1 to immediately start servlet
        servlet.setLoadOnStartup(1);

        // Respond to any request that starts with "/"
        servlet.addMapping("/*");

        tomcat.start();
    }
}
