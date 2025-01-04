package com.idiot.servlet;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

public class AppContextListener implements ServletContextListener {
	
	@Override
    public void contextInitialized(ServletContextEvent sce) {
        // Initialization logic
        System.out.println("Application context initialized.");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // Cleanup logic
        System.out.println("Application context destroyed.");
    }

}
