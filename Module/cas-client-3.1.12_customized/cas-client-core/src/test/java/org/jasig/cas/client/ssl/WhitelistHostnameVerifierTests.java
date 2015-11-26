/*
  $Id: WhitelistHostnameVerifierTests.java 19995 2010-02-24 14:55:51Z serac $

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
 * Unit test for {@link WhitelistHostnameVerifier} class.
 *
 * @author Middleware
 * @version $Revision: 19995 $
 *
 */
public class WhitelistHostnameVerifierTests extends TestCase {
    /**
     * Test method for {@link WhitelistHostnameVerifier#verify(String, SSLSession)}.
     */
    public void testVerify() {
        final WhitelistHostnameVerifier verifier = new WhitelistHostnameVerifier(
                "red.vt.edu, green.vt.edu,blue.vt.edu");
        Assert.assertTrue(verifier.verify("red.vt.edu", null));
        Assert.assertTrue(verifier.verify("green.vt.edu", null));
        Assert.assertTrue(verifier.verify("blue.vt.edu", null));
        Assert.assertFalse(verifier.verify("purple.vt.edu", null));
    }

}
