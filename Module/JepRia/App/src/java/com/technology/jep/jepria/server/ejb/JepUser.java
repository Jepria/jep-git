package com.technology.jep.jepria.server.ejb;

import java.math.BigInteger;
import java.rmi.server.UID;
import java.security.Permission;
import java.security.cert.X509Certificate;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Set;

import com.evermind.security.Group;
import com.technology.jep.jepria.shared.exceptions.ApplicationException;
import com.technology.jep.jepria.shared.exceptions.NotImplementedYetException;

/**
 * Класс используется в реализации custom-аутентификации.
 */
@SuppressWarnings("deprecation")
public class JepUser implements com.evermind.security.User {
	private String login;
	private String password;
	private Locale userLocale;
	private UID userUniqueID;

	public JepUser(String name) {
		this.login = name;
	}

	public boolean authenticate(String password) {
		System.out.println("authenticate(" + password + ")");
		boolean result = true;
		// TODO Реализовать метод
		initSession(); // TODO Попробовать перенести в аутентификацию (чтобы делать один раз на сессию)
		return result;
	}

	public String getPassword() {
		return password;
	}

	public void setLocale(Locale locale) {
		this.userLocale = locale;
	}

	public Locale getLocale() {
		return this.userLocale;
	}

	// -----------------------------------------------------------------------------
	public String getName() {
		return login;
	}

	public boolean hasPermission(Permission perm) {
		boolean result = true;
		// TODO Реализовать метод
		return result;
	}

	void init() throws ApplicationException, SQLException {
	}

	public UID getUniqueID() {
		return userUniqueID;
	}

	public String toString() {
		return login;
	}

	public String getDescription() {
		return null;
	}

	public void setDescription(String parm1) {
		throw new UnsupportedOperationException("setDescription()");
	}

	public void setPassword(String parm1) {
		throw new UnsupportedOperationException("setPassword()");
	}

	public String getCertificateIssuerDN() {
		throw new UnsupportedOperationException("getCertificateIssuerDN()");
	}

	public void setCertificate(String parm1, BigInteger parm2) {
		throw new UnsupportedOperationException("setCertificate()");
	}

	public void setCertificate(X509Certificate parm1) {
		throw new UnsupportedOperationException("setCertificate()");
	}

	public BigInteger getCertificateSerial() {
		throw new UnsupportedOperationException("getCertificateSerial()");
	}

	public void addToGroup(Group group) {
		throw new UnsupportedOperationException("addToGroup()");
	}

	public void removeFromGroup(Group group) {
		throw new UnsupportedOperationException("removeFromGroup()");
	}

	@SuppressWarnings("rawtypes")
	public Set getGroups() {
		throw new UnsupportedOperationException("getGroups()");
	}

	public boolean isMemberOf(Group group) {
		boolean result = true;
		// TODO Реализовать метод
		return result;
	}

	private void initSession() {
		// TODO Установить currentUserId и Locale. Нужно определить как именно...
		// this.serverUser.setLocale(???); установить Locale
		throw new NotImplementedYetException(this.getClass().getName() + ".initSession() не реализован");
	}

	public String getLocalizedText(String textKey) {
		throw new NotImplementedYetException("getString()");
	}

	/**
	 * Обновление сессии.
	 * Метод вызывается для каждого вызова EJB-метода, но реальные действия выполняются
	 * только при первом вызове, когда имя ресурсного файла модуля передаётся первый раз,
	 * и тогда выполняется загрузка.
	 *  
	 * @param resourceBundleName имя ресурсного файла модуля
	 */
	public void updateSession(String resourceBundleName) {
		throw new NotImplementedYetException("updateSession()");
	}
}