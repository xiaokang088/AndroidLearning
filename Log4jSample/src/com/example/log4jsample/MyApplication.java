package com.example.log4jsample;

import java.io.File;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import de.mindpipe.android.logging.log4j.LogConfigurator;
import android.app.Application;
import android.os.Environment;
import android.util.Log;

public class MyApplication extends Application{

	@Override
	public void onCreate(){
		super.onCreate();
		initLog();
	}
	
	void initLog(){
	    LogConfigurator logConfigurator = new LogConfigurator();
        logConfigurator.setFileName(Environment.getExternalStorageDirectory()
                        + File.separator + "Log4jSample" + File.separator + "logs"
                        + File.separator + "log4j.txt");
        
        logConfigurator.setRootLevel(Level.DEBUG);
        logConfigurator.setLevel("org.apache", Level.ERROR);
        logConfigurator.setFilePattern("%d %-5p [%c{2}]-[%L] %m%n");
        logConfigurator.setMaxFileSize(1024 * 1024 * 5);
        logConfigurator.setImmediateFlush(true);
        logConfigurator.configure();
       
          
	}
}
