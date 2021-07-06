package com.jansure.anyobject;

import java.io.IOException;
import java.io.Serializable;

public interface AnyObject extends Serializable {

  AnyObject getAnyObject(String key);

  Double getDouble(String key);

  Float getFloat(String key);

  Long getLong(String key);

  Integer getInteger(String key);

  String getString(String key);

  Boolean getBoolean(String key);

  <T> Iterable<T> getIterable(String key);

  Double getDouble(String key, Double defValue);

  Float getFloat(String key, Float defValue);

  Long getLong(String key, Long defValue);

  Integer getInteger(String key, Integer defValue);

  String getString(String key, String defValue);

  Boolean getBoolean(String key, Boolean defValue);

  byte[] toJsonAsBytes() throws IOException;

  String toJson() throws IOException;

  String toYaml() throws IOException;

  Iterable<AnyTuple> getTuples();

}
