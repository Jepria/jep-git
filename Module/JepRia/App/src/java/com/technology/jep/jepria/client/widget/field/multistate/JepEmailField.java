package com.technology.jep.jepria.client.widget.field.multistate;

import static com.technology.jep.jepria.client.JepRiaClientConstant.JepTexts;

import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.uibinder.client.UiConstructor;
import com.technology.jep.jepria.shared.util.JepRiaUtil;

/**
 * Поле для ввода электронной почты.
 */
public class JepEmailField extends JepTextField {

  /**
   * Конструтор. Если входящие параметры не определены для данного поля, то передавать null.
   * @param fieldLabel Label
   */
  @UiConstructor
  public JepEmailField(String fieldLabel) {
    this(null, fieldLabel);
  }

  /**
   * Конструтор. Если входящие параметры не определены для данного поля, то передавать null.
   * @param fieldIdAsWebEl web-ID.
   * @param fieldLabel Label
   */
  public JepEmailField(String fieldIdAsWebEl, String fieldLabel) {
    super(fieldIdAsWebEl, fieldLabel);
  }
  
  /**
   * https://www.mkyong.com/regular-expressions/how-to-validate-email-address-with-regular-expression/
   * <code>
   ^      #start of the line
      [_A-Za-z0-9-\\+]+ #  must start with string in the bracket [ ], must contains one or more (+)
      (     #   start of group #1
        \\.[_A-Za-z0-9-]+ #     follow by a dot "." and string in the bracket [ ], must contains one or more (+)
      )*      #   end of group #1, this group is optional (*)
        @     #     must contains a "@" symbol
         [A-Za-z0-9-]+      #       follow by string in the bracket [ ], must contains one or more (+)
          (     #         start of group #2 - first level TLD checking
           \\.[A-Za-z0-9]+  #           follow by a dot "." and string in the bracket [ ], must contains one or more (+)
          )*    #         end of group #2, this group is optional (*)
          (     #         start of group #3 - second level TLD checking
           \\.[A-Za-z]{2,}  #           follow by a dot "." and string in the bracket [ ], with minimum length of 2
          )     #         end of group #3
    $     #end of the line
   * </code>
   */
  private static final RegExp ALLOWED_REG_EXP = RegExp.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isValid() {
    
    boolean isValid = super.isValid(); 
   
    if(!isValid) return false;
    
    String email = getRawValue(); // Если поле пустое, то регуляркой проверять не нужно, проверка на обязательность в super. 
    if (!JepRiaUtil.isEmpty(email) && !ALLOWED_REG_EXP.test(email)) {
      markInvalid(JepTexts.checkForm_wrongFormat());
      return false;
    }
    
    return true;
  }

}
