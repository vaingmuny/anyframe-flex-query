package org.anyframe.plugin.interceptor;

import java.io.File;
import java.io.FileInputStream;

import org.apache.commons.collections.ExtendedProperties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Target;

import flex.ant.MxmlcTask;

public class FlexPluginInterceptor {

	protected final Log log = LogFactory.getLog(getClass());
	
	public void postInstall(String baseDir, File pluginJarFile)
			throws Exception {
		String contextRoot = "";
		
		try {
			File metadataFile = new File(new File(baseDir)
					+System.getProperty("file.separator") + "META-INF",
					"project.mf");
			
			ExtendedProperties anyframeProperty = new ExtendedProperties();
			anyframeProperty.load(new FileInputStream(metadataFile));
			
			contextRoot  = (String)anyframeProperty.getProperty("project.name");
			
			String appRoot = baseDir + "/src/main/webapp";
			String fdkHome = appRoot + "/WEB-INF/flex/fdk";
			String serviceName = appRoot + "/WEB-INF/flex/services-config.xml";
			
			String[] arrFlexSrc = { "chat" , "collaboration", "companymgr", "fileupload", "httpservice", "insync01", 
					"insync02", "insync03", "insync04", "insync05", "insync06", "jmspush", "moviefinder", "uisample" };
			
			String[] arrMainFileName = {"chat.mxml" , "collaboration.mxml", "companymgr.mxml", "fileupload.mxml", "httpservice.mxml", "insync01.mxml", 
					"insync02.mxml", "insync03.mxml", "insync04.mxml", "insync05.mxml", "insync06.mxml", "jmspush.mxml", "moviefinder.mxml", "Main.mxml"};
			
			String[] arrSwfFileName = {"chat.swf" , "collaboration.swf", "companymgr.swf", "fileupload.swf", "httpservice.swf", "insync01.swf", 
					"insync02.swf", "insync03.swf", "insync04.swf", "insync05.swf", "insync06.swf", "jmspush.swf", "moviefinder.swf", "Main.swf"};
			
			String flexSrc = "";
			String mainFileName = "";
			String swfFileName = "";
			
			for( int i = 0 ; i < arrFlexSrc.length ; i ++ ){
				MxmlcTask mxmlcTask = new MxmlcTask();
				
				flexSrc = appRoot + "/WEB-INF/flex/pjt/" + arrFlexSrc[i] + "/src";
				mainFileName = flexSrc + "/" + arrMainFileName[i];
				swfFileName = appRoot + "/flex/"+ arrFlexSrc[i] + "/" + arrSwfFileName[i];
				
				Project project = new Project();
				project.setBasedir(".");
				project.setName("anyframe flex ui sample");
				project.setProperty("FLEX_HOME", fdkHome );
				
				Target target = new Target();
				target.setName("compile");
				target.setProject(project);
				
				mxmlcTask.setProject(project);
				mxmlcTask.init();
				mxmlcTask.setFile(mainFileName);
				mxmlcTask.setOutput(swfFileName);
				mxmlcTask.setDynamicAttribute("actionscript-file-encoding", "UTF-8");
				mxmlcTask.setDynamicAttribute("services", serviceName);
				mxmlcTask.setDynamicAttribute("context-root", contextRoot );	
				mxmlcTask.execute();
				
				mxmlcTask.clearArgs();
			}
		}catch(Exception e){
			e.printStackTrace();
			log
			.warn("Error occurred in postInstall FlexPluginInterceptor. The reason is a '"
					+ e.getMessage() + "'.");
		}
	}
}
