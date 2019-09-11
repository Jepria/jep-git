package org.jepria.server.service.rest.gson;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbException;

import com.google.gson.Gson;

/**
 * {@link com.google.gson.Gson} to {@link javax.json.bind.Jsonb} adapter
 */
public class GsonJsonb implements Jsonb {

  protected final Gson gson;
  
  public GsonJsonb(Gson gson) {
    this.gson = gson;
  }
  
  @Override
  public void close() throws Exception {
    // NO-OP
  }

  @Override
  public <T> T fromJson(String arg0, Class<T> arg1) throws JsonbException {
    try {
      return gson.fromJson(arg0, arg1);
    } catch (Throwable e) {
      throw new JsonbException("Failed to deserialize an object from JSON", e);
    }
  }

  @Override
  public <T> T fromJson(String arg0, Type arg1) throws JsonbException {
    try {
      return gson.fromJson(arg0, arg1);
    } catch (Throwable e) {
      throw new JsonbException("Failed to deserialize an object from JSON", e);
    }
  }

  @Override
  public <T> T fromJson(Reader arg0, Class<T> arg1) throws JsonbException {
    try {
      return gson.fromJson(arg0, arg1);
    } catch (Throwable e) {
      throw new JsonbException("Failed to deserialize an object from JSON", e);
    }
  }

  @Override
  public <T> T fromJson(Reader arg0, Type arg1) throws JsonbException {
    try {
      return gson.fromJson(arg0, arg1);
    } catch (Throwable e) {
      throw new JsonbException("Failed to deserialize an object from JSON", e);
    }
  }

  @Override
  public <T> T fromJson(InputStream arg0, Class<T> arg1) throws JsonbException {
    try {
      return gson.fromJson(new InputStreamReader(arg0), arg1);
    } catch (Throwable e) {
      throw new JsonbException("Failed to deserialize an object from JSON", e);
    }
  }

  @Override
  public <T> T fromJson(InputStream arg0, Type arg1) throws JsonbException {
    try {
      return gson.fromJson(new InputStreamReader(arg0), arg1);
    } catch (Throwable e) {
      throw new JsonbException("Failed to deserialize an object from JSON", e);
    }
  }

  @Override
  public String toJson(Object arg0) throws JsonbException {
    try {
      return gson.toJson(arg0);
    } catch (Throwable e) {
      throw new JsonbException("Failed to serialize an object into JSON", e);
    }
  }

  @Override
  public String toJson(Object arg0, Type arg1) throws JsonbException {
    try {
      return gson.toJson(arg0, arg1);
    } catch (Throwable e) {
      throw new JsonbException("Failed to serialize an object into JSON", e);
    }
  }

  @Override
  public void toJson(Object arg0, Writer arg1) throws JsonbException {
    try {
      gson.toJson(arg0, arg1);
    } catch (Throwable e) {
      throw new JsonbException("Failed to serialize an object into JSON", e);
    }
  }

  @Override
  public void toJson(Object arg0, OutputStream arg1) throws JsonbException {
    try {
      gson.toJson(arg0, new OutputStreamWriter(arg1));
    } catch (Throwable e) {
      throw new JsonbException("Failed to serialize an object into JSON", e);
    }
  }

  @Override
  public void toJson(Object arg0, Type arg1, Writer arg2) throws JsonbException {
    try {
      gson.toJson(arg0, arg1, arg2);
    } catch (Throwable e) {
      throw new JsonbException("Failed to serialize an object into JSON", e);
    }
  }

  @Override
  public void toJson(Object arg0, Type arg1, OutputStream arg2) throws JsonbException {
    try {
      gson.toJson(arg0, arg1, new OutputStreamWriter(arg2));
    } catch (Throwable e) {
      throw new JsonbException("Failed to serialize an object into JSON", e);
    }
  }

}
