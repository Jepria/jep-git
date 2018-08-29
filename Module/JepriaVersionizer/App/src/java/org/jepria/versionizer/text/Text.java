package org.jepria.versionizer.text;

import java.util.Locale;
import java.util.ResourceBundle;

public final class Text {
  
  private static ResourceBundle bundle;
  
  public static String get(String key) {
    if (bundle == null) {
      bundle = ResourceBundle.getBundle("org.jepria.versionizer.text.Text", new Locale("en", "US") /*new Locale("ru", "RU")*/); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }
    
    return bundle.getString(key);
  }
}
