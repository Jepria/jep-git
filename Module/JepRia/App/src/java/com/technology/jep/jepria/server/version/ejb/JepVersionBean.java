package com.technology.jep.jepria.server.version.ejb;

import static com.technology.jep.jepria.server.JepRiaServerConstant.DATA_SOURCE_JNDI_NAME;
import static com.technology.jep.jepria.server.JepRiaServerConstant.JEP_RIA_RESOURCE_BUNDLE_NAME;

import javax.annotation.Resource;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import oracle.j2ee.ejb.StatelessDeployment;

import com.technology.jep.jepria.server.dao.DaoSupport;
import com.technology.jep.jepria.shared.exceptions.ApplicationException;

/**
 * Бин для поддержки версионности приложений.
 */

@Local( { JepVersionLocal.class })
@Remote( { JepVersionRemote.class })
@StatelessDeployment
@Stateless
public class JepVersionBean implements JepVersion {	
	@Resource
	protected SessionContext sessionContext;
	
	public Integer createAppInstallResult(
			String moduleSvnRoot
			, String moduleInitialSvnPath
			, String moduleVersion
			, String deploymentPath
			, String installVersion
			, String installDate
			, Integer operatorId) throws ApplicationException {
//		return DaoSupport.<Integer>execute(
//				"begin " 
//				  + " ? := pkg_moduleinfo.createAppInstallResult(" 
//						+ "moduleSvnRoot => ? " 
//					  + ", moduleInitialSvnPath => ? " 
//						+ ", moduleVersion => ? "
//						+ ", deploymentPath => ? "
//						+ ", installVersion => ? "
//						+ ", installDate => ? "
//						+ ", operatorId => ? "
//				  + ");"
//			    + "end;",
//				sessionContext, DATA_SOURCE_JNDI_NAME, JEP_RIA_RESOURCE_BUNDLE_NAME, Integer.class,
//				moduleSvnRoot,
//				moduleInitialSvnPath, 
//				moduleVersion, 
//				deploymentPath,
//				installVersion,
//				installDate,
//				operatorId);
		return null;
		
	}
}
