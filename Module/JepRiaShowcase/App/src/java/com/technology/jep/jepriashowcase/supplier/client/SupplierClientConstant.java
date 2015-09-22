package com.technology.jep.jepriashowcase.supplier.client;
 
import static com.technology.jep.jepriashowcase.main.client.JepRiaShowcaseClientConstant.*;
import com.google.gwt.core.client.GWT;
import com.technology.jep.jepriashowcase.supplier.shared.SupplierConstant;
import com.technology.jep.jepriashowcase.supplier.shared.text.SupplierText;
 
public class SupplierClientConstant extends SupplierConstant {
 
	public static String[] scopeModuleIds = {SUPPLIER_MODULE_ID, GOODS_MODULE_ID}; 
 
	public static SupplierText supplierText = (SupplierText) GWT.create(SupplierText.class);
}
