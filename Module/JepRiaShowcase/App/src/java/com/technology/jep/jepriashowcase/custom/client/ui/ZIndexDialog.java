package com.technology.jep.jepriashowcase.custom.client.ui;

import static com.technology.jep.jepriashowcase.custom.client.CustomClientConstant.customText;

import java.util.ArrayList;
import java.util.Arrays;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.technology.jep.jepria.client.message.MessageBox;
import com.technology.jep.jepria.client.message.PredefinedButton;
import com.technology.jep.jepria.client.util.JepClientUtil;
import com.technology.jep.jepria.client.widget.field.multistate.JepComboBoxField;
import com.technology.jep.jepria.client.widget.field.multistate.JepDateField;
import com.technology.jep.jepria.client.widget.field.multistate.JepRichTextEditorField;
import com.technology.jep.jepria.shared.field.option.JepOption;

public class ZIndexDialog extends MessageBox {
  
  private Button ok = new Button("Close");
  private JepRichTextEditorField jepRichTextEditorField = new JepRichTextEditorField();
  private JepComboBoxField jepComboBoxField = new JepComboBoxField();
  private JepDateField jepDateField = new JepDateField();
  private Button maskButton = new Button(customText.custom_zindexDialog_loadingPanel_button());
  
  public ZIndexDialog() {
    super(customText.custom_zindexDialog_button(), "");
    
    jepRichTextEditorField.setValue(customText.custom_zindexDialog_info());
    jepRichTextEditorField.setFieldHeight(200);
    
    jepComboBoxField.setOptions(new ArrayList<JepOption>(Arrays.asList(new JepOption[]{
        new JepOption("element#1", "element#1"),
        new JepOption("element#2", "element#2"),
        new JepOption("element#3", "element#3")
        })));
    
    VerticalPanel panel = new VerticalPanel();
    panel.add(jepRichTextEditorField);
    panel.add(jepComboBoxField);
    panel.add(jepDateField);
    panel.add(maskButton);
    mainPanel.setWidget(0, 0, panel);
    
    ok.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        hide();
      }
    });
    
    maskButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        JepClientUtil.showLoadingPanel(customText.custom_zindexDialog_loadingPanel_header(),
            customText.custom_zindexDialog_loadingPanel_text());
        new Timer() {
          @Override
          public void run() {
            JepClientUtil.hideLoadingPanel();
          }
        }.schedule(2000);
      }
    });
    
    addButton(PredefinedButton.OK, ok);
  }
}
