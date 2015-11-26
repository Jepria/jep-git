/*
  $Id: RegexHostnameVerifierTests.java 19995 2010-02-24 14:55:51Z serac $

  Copyright (C) 2008-2009 Virginia Tech.
  All rights reserved.

  SEE LICENSE FOR MORE INFORMATION

  Author:  Middleware
  Email:   middleware@vt.edu
  Version: $Revision: 19995 $
  Updated: $Date: 2010-02-24 09:55:51 -0500 (Wed, 24 Feb 2010) $
*/
package org.jasig.cas.client.ssl;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * Unit test for {@link RegexHostnameVerifier} class.
 *
 * @author Middleware
 * @version $Revision: 19995 $
 *
 */
public class RegexHostnameVerifierTests extends TestCase {

    /**
     * Test method for {@link RegexHostnameVerifier#verify(String, SSLSession)}.
     */
    public void testVerify() {
        final RegexHostnameVerifier verifier = new RegexHostnameVerifier("\\w+\\.vt\\.edu");
        Assert.assertTrue(verifier.verify("a.vt.edu", null));
        Assert.assertTrue(verifier.verify("host.vt.edu", null));
        Assert.assertFalse(verifier.verify("1-host.vt.edu", null));
        Assert.assertFalse(verifier.verify("mallory.example.com", null));
    }

}
