package com.technology.jep.jepriashowcase.supplier.client.ui.form.detail;

import static com.technology.jep.jepriashowcase.supplier.shared.field.SupplierFieldNames.ADD_DOWN_BUTTON_ID;
import static com.technology.jep.jepriashowcase.supplier.shared.field.SupplierFieldNames.ADD_RIGHT_BUTTON_ID;
import static com.technology.jep.jepriashowcase.supplier.shared.field.SupplierFieldNames.NEXT_BUTTON_ID;
import static com.technology.jep.jepriashowcase.supplier.shared.field.SupplierFieldNames.PREVIOUS_BUTTON_ID;
import static com.technology.jep.jepriashowcase.supplier.client.SupplierClientConstant.supplierText;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.technology.jep.jepria.client.ui.form.detail.DetailFormViewImpl;
import com.technology.jep.jepria.client.ui.wizard.BlockClientFactory;
import com.technology.jep.jepria.client.ui.wizard.BlockContainer;
import com.technology.jep.jepria.client.ui.wizard.BlockContainerImpl;
import com.technology.jep.jepria.client.ui.wizard.BlockNavigator;
import com.technology.jep.jepria.client.ui.wizard.BlockManager;
import com.technology.jep.jepria.client.ui.wizard.BlockManagerImpl;
import com.technology.jep.jepria.client.widget.field.FieldManager;
import com.technology.jep.jepria.shared.record.JepRecord;
import com.technology.jep.jepriashowcase.supplier.client.ui.wizard.SupplierFifthBlockClientFactory;
import com.technology.jep.jepriashowcase.supplier.client.ui.wizard.SupplierFirstBlockClientFactory;
import com.technology.jep.jepriashowcase.supplier.client.ui.wizard.SupplierFourthBlockClientFactory;
import com.technology.jep.jepriashowcase.supplier.client.ui.wizard.SupplierSecondBlockClientFactory;
import com.technology.jep.jepriashowcase.supplier.client.ui.wizard.SupplierThirdBlockClientFactory;

public class SupplierDetailFormViewImpl
  extends DetailFormViewImpl 
    implements SupplierDetailFormView {  

  private static BlockContainer wizardBlock;
  private Button previuosButton, nextButton, addRightButton, addDownButton;
  
  static {
    wizardBlock = new BlockContainerImpl(new BlockManagerImpl(SupplierFirstBlockClientFactory.getInstance(), SupplierSecondBlockClientFactory.getInstance(), SupplierThirdBlockClientFactory.getInstance(), SupplierFourthBlockClientFactory.getInstance(), SupplierFifthBlockClientFactory.getInstance()));
  }
  
  public SupplierDetailFormViewImpl() {
    super(new FieldManager(){
      
      BlockManager manager = wizardBlock.getBlockManager();
      
      @Override
      public void setValues(JepRecord record) {
        for (BlockClientFactory<?> factory : manager.getBlockClientFactories()){
          factory.getView().getFieldManager().setValues(record);
        }
      }
      
      @Override
      public JepRecord getValues() {
        JepRecord record = new JepRecord();
        for (BlockClientFactory<?> factory : manager.getBlockClientFactories()){
          record.update(factory.getView().getFieldManager().getValues());
        }
        return record;
      }
      
      @Override
      public boolean isValid(){
        boolean valid = true;
        for (BlockClientFactory<?> factory : manager.getBlockClientFactories()){
          if (!factory.getView().getFieldManager().isValid()) {
            valid = false;
          }
        }
        
        return valid;
      }
      
      @Override
      public void clear() {
        for (BlockClientFactory<?> factory : manager.getBlockClientFactories()){
          factory.getView().getFieldManager().clear();
        }
      }
    });
    
    // Подключение прокрутки.
    ScrollPanel scrollPanel = new ScrollPanel();
    scrollPanel.setSize("100%", "100%");
    
    VerticalPanel panel = new VerticalPanel();
    panel.getElement().getStyle().setMarginTop(5, Unit.PX);
    scrollPanel.add(panel);
    
    panel.add(wizardBlock);
    
    HorizontalPanel buttonPanel = new HorizontalPanel();
    
    previuosButton = new Button(supplierText.supplier_button_previuos_alt());
    nextButton = new Button(supplierText.supplier_button_next_alt());
    addRightButton = new Button(supplierText.supplier_button_addRight_alt());
    addDownButton = new Button(supplierText.supplier_button_addBottom_alt());
    
    buttonPanel.add(addDownButton);
    buttonPanel.add(previuosButton);
    buttonPanel.add(nextButton);
    buttonPanel.add(addRightButton);
    initButtonProperties();
    
    panel.add(buttonPanel);
    panel.setCellHorizontalAlignment(buttonPanel, HasHorizontalAlignment.ALIGN_CENTER);
    
    setWidget(scrollPanel);
  }

  public void initButtonProperties() {
    BlockNavigator controller = wizardBlock.getBlockManager().getController();
    previuosButton.setEnabled(!controller.isFirstBlock());
    nextButton.setEnabled(!controller.isLastBlock());
  }
  
  public void addButtonClickHandler(String buttonId, ClickHandler handler){
    if (ADD_DOWN_BUTTON_ID.equalsIgnoreCase(buttonId)){
      addDownButton.addClickHandler(handler);
    }
    else if (ADD_RIGHT_BUTTON_ID.equalsIgnoreCase(buttonId)){
      addRightButton.addClickHandler(handler);
    }
    else if (PREVIOUS_BUTTON_ID.equalsIgnoreCase(buttonId)){
      previuosButton.addClickHandler(handler);
    }
    else if (NEXT_BUTTON_ID.equalsIgnoreCase(buttonId)){
      nextButton.addClickHandler(handler);
    }
  }
  
  public BlockContainer getBlockContainer(){
    return wizardBlock;
  }
}
