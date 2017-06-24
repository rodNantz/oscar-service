package com.go.oscar.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertySource {
	public static Properties props = new Properties();
	//public static String filePath = "main" + File.separator + "resources" + File.separator + "resysclagem.properties";
	public static String filePath 		= "src" + File.separator +
										  "main" + File.separator + 
										  "resources" + File.separator + 
										  "go-oscars.properties";
	public static String filePathBuild 	= "go-oscars.properties";
	public static boolean retry = true;
	static {
		File propertiesFile = new File(filePath);
		try {
			props.load(new FileInputStream(propertiesFile));
			System.out.println("* Properties file: " + filePath);
		} catch (IOException io1) {
			if(retry){
				retry = false;
				try {
					props.load(new FileInputStream(filePathBuild));
					System.out.println("* Properties file: " + filePathBuild);
				}catch (Exception io2) {
					System.err.println( new File("").getAbsolutePath() );
					io1.printStackTrace();
					io2.printStackTrace();
				}
			}
		}
	}
}
