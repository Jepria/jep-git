/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2017 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://oss.oracle.com/licenses/CDDL+GPL-1.1
 * or LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package org.jepria.server.service.rest.gson;

import com.google.gson.Gson;
import org.glassfish.jersey.message.internal.AbstractMessageReaderWriterProvider;

import javax.json.bind.Jsonb;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * Entity provider (reader and writer) for JSONB.
 *
 * @author Adam Lindenthal (adam.lindenthal at oracle.com)
 */
// This class is almost a copy of org.glassfish.jersey.jsonb.internal.JsonBindingProvider
// from https://raw.githubusercontent.com/jersey/jersey/master/media/json-binding/src/main/java/org/glassfish/jersey/jsonb/internal/JsonBindingProvider.java
@Produces({"application/json", "text/json", "*/*"})
@Consumes({"application/json", "text/json", "*/*"})
public class JsonBindingProvider extends AbstractMessageReaderWriterProvider<Object> {

    /*
     *  Note: amazingly this injection requires javassist.jar dependency (from the Jersey jaxrs-ri bundle).
     *  Dependent code: 
     *  org.jvnet.hk2.internal.Utilities.createService(ActiveDescriptor, Injectee, ServiceLocatorImpl, ServiceHandle, Class)
     *  which calls
     *  org.jvnet.hk2.internal.Utilities.proxiesAvailable()
     *  which calls
     *  [ClassLoader] loader.loadClass("javassist.util.JsonBindingProviderproxy.MethodHandler");
     *  TODO add fail-fast dependency checking here?
     */
    @Context
    private ResourceInfo resourceInfo;
  
    private static final String JSON = "json";
    private static final String PLUS_JSON = "+json";

    public JsonBindingProvider() {
    }

    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return supportsMediaType(mediaType);
    }

    @Override
    public Object readFrom(Class<Object> type, Type genericType,
                           Annotation[] annotations,
                           MediaType mediaType,
                           MultivaluedMap<String, String> httpHeaders,
                           InputStream entityStream) throws IOException, WebApplicationException {
        Jsonb jsonb = getJsonb(type, annotations);
//        try {
            return jsonb.fromJson(entityStream, genericType);
//        } catch (JsonbException e) {
//            throw new ProcessingException("Error deserializing object from entity stream", e);
//        }
    }

    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return supportsMediaType(mediaType);
    }

    @Override
    public void writeTo(Object o, Class<?> type, Type genericType,
                        Annotation[] annotations,
                        MediaType mediaType,
                        MultivaluedMap<String, Object> httpHeaders,
                        OutputStream entityStream) throws IOException, WebApplicationException {
        Jsonb jsonb = getJsonb(type, annotations);
//        try {
            entityStream.write(jsonb.toJson(o).getBytes(AbstractMessageReaderWriterProvider.getCharset(mediaType)));
            entityStream.flush();
//        } catch (IOException e) {
//            throw new ProcessingException("Error writing JSON-B serialized object", e);
//        }
    }

    private Jsonb getJsonb(Class<?> type, Annotation[] annotations) {
      final Gson gson = new AnnotatedGsonBuilder(resourceInfo).build();
      return new GsonJsonb(gson);
    }

    /**
     * @return true for all media types of the pattern *&#47;json and
     * *&#47;*+json.
     */
    private static boolean supportsMediaType(final MediaType mediaType) {
        return mediaType.getSubtype().equals(JSON) || mediaType.getSubtype().endsWith(PLUS_JSON);
    }

    /**
     * Method for obtaining resource-contextual Jsonb for various utility purposes
     * @return
     */
    public static Jsonb getJsonb() {
        return new JsonBindingProvider().getJsonb(null, null);
    }
}
