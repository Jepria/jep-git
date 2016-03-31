package com.technology.jep.jepriatoolkit.creator;

import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.UTF_8;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Locale;
import java.util.Map;

import com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil;

import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.TemplateNotFoundException;

public class ApplicationStructureCreatorUtil {
	
	private static Configuration cfg = null;
	
	public static Configuration getTemplateConfiguration(){
		if (cfg == null) {
			// Create your Configuration instance, and specify if up to what FreeMarker
			// version (here 2.3.22) do you want to apply the fixes that are not 100%
			// backward-compatible. See the Configuration JavaDoc for details.
			cfg = new Configuration(Configuration.VERSION_2_3_23);
	
			// Specify the source where the template files come from. Here I set a
			// plain directory for it, but non-file-system sources are possible too:
			cfg.setClassForTemplateLoading(JepRiaToolkitUtil.class, "/com/technology/jep/jepriatoolkit/creator/template");
			// Set the preferred charset template files are stored in. UTF-8 is
			// a good choice in most applications:
			cfg.setDefaultEncoding(UTF_8);
			//charset of the output
			cfg.setOutputEncoding(UTF_8);
			//default locale
		    cfg.setLocale(Locale.US);
			// Sets how errors will appear.
			// During web page *development* TemplateExceptionHandler.HTML_DEBUG_HANDLER is better.
			cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
		}
		return cfg;
	}
	
	public static void convertTemplateToFile(String templateFile, Map<String, Object> dataToMap, String resultFile){
		try {
			Template webXmlTemplate = getTemplateConfiguration().getTemplate(templateFile);
			Writer bufferedWriter = new BufferedWriter(
				new OutputStreamWriter(
					new FileOutputStream(
						resultFile
					), 
					UTF_8
				)
			);
			try {
				webXmlTemplate.process(dataToMap, bufferedWriter);
			}
			finally {
				bufferedWriter.close();
			}
			
		} catch (TemplateNotFoundException e) {
			e.printStackTrace();
		} catch (MalformedTemplateNameException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TemplateException e) {
			e.printStackTrace();
		}
	}

}
