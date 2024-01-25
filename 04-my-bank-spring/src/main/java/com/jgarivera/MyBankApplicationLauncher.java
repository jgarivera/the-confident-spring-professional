package com.jgarivera;

import com.jgarivera.context.MyBankApplicationConfiguration;
import com.jgarivera.web.MyBankServlet;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Wrapper;
import org.apache.catalina.startup.Tomcat;

public class MyBankApplicationLauncher {

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
        Wrapper servlet = Tomcat.addServlet(context, "myBankServlet", new MyBankServlet());
        servlet.setLoadOnStartup(1);
        servlet.addMapping("/*");

        tomcat.start();
    }
}
