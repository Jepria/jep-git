package org.jepria;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.jepria.TypedValueParser.TypedValueParseException;

/**
 * Simple {@link CastMap} implementation that uses a {@link TypedValueParser} to parse values
 */
public class CastMapBase extends HashMap<String, Object> implements CastMap<String, Object> {

  protected final TypedValueParser parser;

  public CastMapBase(TypedValueParser parser, Map<? extends String, ? extends Object> source) {
    super(source);
    Objects.requireNonNull(parser);
    this.parser = parser;
  }
  
  public CastMapBase(TypedValueParser parser) {
    Objects.requireNonNull(parser);
    this.parser = parser;
  }

  private static final long serialVersionUID = -4621908477271689859L;

  @Override
  public Integer getInteger(Object key) {
    try {
      return parser.parse(get(key), Integer.class);
    } catch (TypedValueParseException e) {
      throw new CastOnGetException(key, e.getValue(), Integer.class);
    }
  }

  @Override
  public String getString(Object key) {
    try {
      return parser.parse(get(key), String.class);
    } catch (TypedValueParseException e) {
      throw new CastOnGetException(key, e.getValue(), String.class);
    }
  }

  @Override
  public BigDecimal getBigDecimal(Object key) {
    try {
      return parser.parse(get(key), BigDecimal.class);
    } catch (TypedValueParseException e) {
      throw new CastOnGetException(key, e.getValue(), BigDecimal.class);
    }
  }

}