package com.technology.jep.jepriashowcase.supplier.server.ejb;
 
import static com.technology.jep.jepriashowcase.supplier.server.SupplierServerConstant.DATA_SOURCE_JNDI_NAME;
import static com.technology.jep.jepriashowcase.supplier.server.SupplierServerConstant.RESOURCE_BUNDLE_NAME;
import static com.technology.jep.jepriashowcase.supplier.shared.field.SupplierFieldNames.*;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;

import oracle.j2ee.ejb.StatelessDeployment;

import com.technology.jep.jepria.server.ejb.JepDataStandardBean;
import com.technology.jep.jepria.shared.exceptions.ApplicationException;
import com.technology.jep.jepria.shared.field.option.JepOption;
import com.technology.jep.jepria.shared.record.JepRecord;
import com.technology.jep.jepria.shared.util.JepRiaUtil;
import com.technology.jep.jepria.shared.util.Mutable;
import com.technology.jep.jepriashowcase.supplier.server.ejb.Supplier;
import com.technology.jep.jepriashowcase.supplier.shared.field.BankOptions;
import com.technology.jep.jepria.server.dao.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
 
@Local( { SupplierLocal.class })
@Remote( { SupplierRemote.class })
@StatelessDeployment
@Stateless
public class SupplierBean extends JepDataStandardBean implements Supplier {
 
	public SupplierBean() {
		super(DATA_SOURCE_JNDI_NAME, RESOURCE_BUNDLE_NAME);
	}
 
	public List<JepRecord> find(JepRecord templateRecord, Mutable<Boolean> autoRefreshFlag, Integer maxRowCount, Integer operatorId) throws ApplicationException {
		String sqlQuery = 
			"begin  " 
			  +	"? := pkg_jepriashowcase.findSupplier(" 
				  	+ "supplierId => ? " 
				  	+ ", supplierName => ? " 
				  	+ ", contractFinishDateFrom => ? " 
				  	+ ", contractFinishDateTo => ? " 
				  	+ ", exclusiveSupplierFlag => ? " 
					+ ", maxRowCount => ? " 
					+ ", operatorId => ? " 
			  + ");"
		 + " end;";
		return super.find(sqlQuery,
				new ResultSetMapper<JepRecord>() {
					public void map(ResultSet rs, JepRecord record) throws SQLException {
						record.set(SUPPLIER_ID, getInteger(rs, SUPPLIER_ID));
						record.set(SUPPLIER_NAME, rs.getString(SUPPLIER_NAME));
						record.set(CONTRACT_FINISH_DATE, getDate(rs, CONTRACT_FINISH_DATE));
						
						record.set(EXCLUSIVE_SUPPLIER_FLAG, rs.getBoolean(EXCLUSIVE_SUPPLIER_FLAG));
						record.set(PRIVILEGE_SUPPLIER_FLAG, getBoolean(rs, PRIVILEGE_SUPPLIER_FLAG));
						
						record.set(SUPPLIER_DESCRIPTION, rs.getString(SUPPLIER_DESCRIPTION));
						record.set(PHONE_NUMBER, rs.getString(PHONE_NUMBER));
						record.set(FAX_NUMBER, rs.getString(FAX_NUMBER));
						String bankname = rs.getString(BANKNAME);
						record.set(BANKNAME, bankname);
						String ks = rs.getString(KS);
						record.set(KS, ks);
						String bankBic = rs.getString(BANK_BIC);
						if (!JepRiaUtil.isEmpty(bankBic)) {
							JepOption bankOption = new JepOption(bankname, bankBic);
							bankOption.set(BankOptions.KS, ks);
							record.set(BANK_BIC, bankOption);
						}
						record.set(RECIPIENT_NAME, rs.getString(RECIPIENT_NAME));
						record.set(SETTLEMENT_ACCOUNT, rs.getString(SETTLEMENT_ACCOUNT));
					}
				}
				, templateRecord.get(SUPPLIER_ID)
				, templateRecord.get(SUPPLIER_NAME)
				, templateRecord.get(CONTRACT_FINISH_DATE_FROM)
				, templateRecord.get(CONTRACT_FINISH_DATE_TO)
				, JepOption.<Integer>getValue(templateRecord.get(EXCLUSIVE_SUPPLIER_OPTION))
				, maxRowCount 
				, operatorId);
	}
	public void delete(JepRecord record, Integer operatorId) throws ApplicationException {
		String sqlQuery = 
			"begin " 
			  + "pkg_jepriashowcase.deleteSupplier(" 
				  	+ "supplierId => ? " 
					+ ", operatorId => ? " 
			  + ");"
		  + "end;";
		super.delete(sqlQuery 
				, record.get(SUPPLIER_ID) 
				, operatorId);
	}
 
	public void update(JepRecord record, Integer operatorId) throws ApplicationException {
		String sqlQuery = 
			"begin " 
			+	"pkg_jepriashowcase.updateSupplier(" 
				  	+ "supplierId => ? " 
				  	+ ", supplierName => ? " 
				  	+ ", contractFinishDate => ? " 
				  	+ ", exclusiveSupplierFlag => ? "
				  	+ ", phoneNumber => ? "
				  	+ ", faxNumber => ? "
				  	+ ", bankBic => ? "
				  	+ ", recipientName => ? "
				  	+ ", settlementAccount => ? " 
					+ ", operatorId => ? " 
			+ ");"
		 + "end;";
		super.update(sqlQuery 
				, record.get(SUPPLIER_ID)
				, record.get(SUPPLIER_NAME)
				, record.get(CONTRACT_FINISH_DATE)
				, record.get(EXCLUSIVE_SUPPLIER_FLAG)
				, record.get(PHONE_NUMBER)
				, record.get(FAX_NUMBER)
				, JepOption.getValue(record.get(BANK_BIC))
				, record.get(RECIPIENT_NAME)
				, record.get(SETTLEMENT_ACCOUNT)
				, operatorId);
	}
 
	public Integer create(JepRecord record, Integer operatorId) throws ApplicationException {
		String sqlQuery = 
			"begin " 
			  + "? := pkg_jepriashowcase.createSupplier(" 
				  	+ "supplierName => ? " 
				  	+ ", contractFinishDate => ? " 
				  	+ ", exclusiveSupplierFlag => ? "
				  	+ ", phoneNumber => ? "
				  	+ ", faxNumber => ? "
				  	+ ", bankBic => ? "
				  	+ ", recipientName => ? "
				  	+ ", settlementAccount => ? "  
					+ ", operatorId => ? " 
			  + ");"
			+ "end;";
		return super.create(sqlQuery, 
				Integer.class 
				, record.get(SUPPLIER_NAME)
				, record.get(CONTRACT_FINISH_DATE)
				, record.get(EXCLUSIVE_SUPPLIER_FLAG)
				, record.get(PHONE_NUMBER)
				, record.get(FAX_NUMBER)
				, JepOption.getValue(record.get(BANK_BIC))
				, record.get(RECIPIENT_NAME)
				, record.get(SETTLEMENT_ACCOUNT)
				, operatorId);
	}
	
	@Override
	public List<JepOption> getBank(String bankBic, Integer maxRowCount) throws ApplicationException {
		String sqlQuery = 
				"begin  " 
				  +	"? := pkg_jepriashowcase.getBank(" 
					  	+ "bankBic => ? " 
					  	+ ", maxRowCount => ? "
				  + ");"
			 + " end;";
		return super.getOptions(sqlQuery, 
			new ResultSetMapper<JepOption>(){
				@Override
				public void map(ResultSet rs, JepOption dto) throws SQLException {
					dto.setValue(rs.getString(BankOptions.BIC));
					dto.setName(rs.getString(BankOptions.BANKNAME));
					dto.set(BankOptions.KS, rs.getString(BankOptions.KS));				
				}}, 
			bankBic,
			maxRowCount);
	}
 
}
