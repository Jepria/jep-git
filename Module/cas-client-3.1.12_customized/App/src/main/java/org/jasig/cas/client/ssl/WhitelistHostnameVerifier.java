/*
  $Id: WhitelistHostnameVerifier.java 19995 2010-02-24 14:55:51Z serac $

  Copyright (C) 2008-2009 Virginia Tech.
  All rights reserved.

  SEE LICENSE FOR MORE INFORMATION

  Author:  Middleware
  Email:   middleware@vt.edu
  Version: $Revision: 19995 $
  Updated: $Date: 2010-02-24 09:55:51 -0500 (Wed, 24 Feb 2010) $
*/
package org.jasig.cas.client.ssl;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

/**
 * Verifies a SSL peer host name based on an explicit whitelist of allowed hosts.
 *
 * @author Middleware
 * @version $Revision: 19995 $
 *
 */
public class WhitelistHostnameVerifier implements HostnameVerifier {
    /** Allowed hosts */
    private String[] allowedHosts;


    /**
     * Creates a new instance using the given array of allowed hosts.
     * 
     * @param allowed Array of allowed hosts.
     */
    public WhitelistHostnameVerifier(final String[] allowed) {
        this.allowedHosts = allowed;
    }


    /**
     * Creates a new instance using the given list of allowed hosts.
     * 
     * @param allowedList Comma-separated list of allowed hosts.
     */
    public WhitelistHostnameVerifier(final String allowedList) {
        this.allowedHosts = allowedList.split(",\\s*");
    }


    /** {@inheritDoc} */
    public boolean verify(final String hostname, final SSLSession session) {
        for (int i = 0; i < this.allowedHosts.length; i++) {
            if (hostname.equalsIgnoreCase(this.allowedHosts[i])) {
                return true;
            }
        }
        return false;
    }

}
