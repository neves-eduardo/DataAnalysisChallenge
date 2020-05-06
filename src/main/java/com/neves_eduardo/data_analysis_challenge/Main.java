package com.neves_eduardo.data_analysis_challenge;

import com.neves_eduardo.data_analysis_challenge.config.AppConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext appContext = new AnnotationConfigApplicationContext(AppConfig.class);
        DirectoryObserver directoryObserver = (DirectoryObserver) appContext.getBean("directoryObserver");
        directoryObserver.appBoot();
        appContext.close();
        while(true){directoryObserver.appCheckDirectory();}


    }

}



