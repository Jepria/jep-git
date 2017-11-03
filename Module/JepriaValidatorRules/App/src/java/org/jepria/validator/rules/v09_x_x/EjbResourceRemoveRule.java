package org.jepria.validator.rules.v09_x_x;

import org.jepria.validator.core.base.Message;
import org.jepria.validator.core.base.ValidatorRule;
import org.jepria.validator.core.base.resource.Directory;
import org.jepria.validator.core.base.resource.PlainResource;
import org.jepria.validator.core.base.transform.ResourceRefactorer;
import org.jepria.validator.core.base.transform.Transformation;
import org.jepria.validator.core.base.transform.Transformation.Action;
import org.jepria.validator.rules.MessageLevel;

/**
 * Удаляет ресурсы *Local.java, *Remote.java из ejb
 * <br>
 * Удаляет ресурсы application.xml, orion-application.xml
 */
public class EjbResourceRemoveRule extends ValidatorRule {
  
  @Override
  public Transformation forPlainResource(PlainResource resource) {
    if (resource.getPathName().matches(".+/ejb/.+Local\\.java") 
        || resource.getPathName().matches(".+/ejb/.+Remote\\.java")
        || resource.getName().equals("application.xml")
        || resource.getName().equals("orion-application.xml")) {
      
      // Удалим ресурсы
      handleMessage(new Message(MessageLevel.AUTO_TRANSFORM, "Delete the resource", null));
      
      return Transformation.of().resource(new Action<ResourceRefactorer>() {
        @Override
        public void perform(ResourceRefactorer refactorer) {
          refactorer.deleteThisResource();
        }
      });
    }
    
    return null;
  }
  
  @Override
  public Transformation forDirectory(Directory resource) {
    if (resource.getName().equals("ejb")) {
      
      // Удалим ресурсы
      handleMessage(new Message(MessageLevel.AUTO_TRANSFORM, "Delete the directory", null));
      
      return Transformation.of().resource(new Action<ResourceRefactorer>() {
        @Override
        public void perform(ResourceRefactorer refactorer) {
          refactorer.deleteThisResource();
        }
      });
    }
    
    return null;
  }
  
}
