package listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

//@WebListener
public final class MyServletListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        sce.getServletContext().addFilter("SetCharacterEncodingFilter", "org.apache.catalina.filters.SetCharacterEncodingFilter");

        //dao.MessageDao.startDatabase();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        //dao.MessageDao.stopDatabase();
    }
}
