/*
  $Id: AnyHostnameVerifier.java 19995 2010-02-24 14:55:51Z serac $

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
 * Hostname verifier that performs no host name verification for an SSL peer
 * such that all hosts are allowed.
 *
 * @author Middleware
 * @version $Revision: 19995 $
 *
 */
public class AnyHostnameVerifier implements HostnameVerifier {

    /** {@inheritDoc} */
    public boolean verify(final String hostname, final SSLSession session) {
        return true;
    }

}
