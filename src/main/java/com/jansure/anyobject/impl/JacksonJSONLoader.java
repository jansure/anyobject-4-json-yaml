package com.jansure.anyobject.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.Reader;
import java.util.Map;

import com.jansure.anyobject.AnyObject;
import com.jansure.anyobject.Loader;
import com.jansure.anyobject.MapBasedAnyObject;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class JacksonJSONLoader implements Loader {

  private static final JacksonJSONLoader instance = new JacksonJSONLoader();

  public static JacksonJSONLoader getInstance() {
    return instance;
  }

  private JacksonJSONLoader() {
  }

  @Override
  public AnyObject load(File file) throws IOException {
    InputStream in = new FileInputStream(file);
    try {
      return load(in);
    } finally {
      in.close();    
    }
  }

  @Override
  public AnyObject load(InputStream in) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    Map<String, Object> map = mapper.readValue(in, Map.class);
    return new MapBasedAnyObject(map);
  }
  
  @Override
  public AnyObject load(Reader in) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    Map<String, Object> map = mapper.readValue(in, Map.class);
    return new MapBasedAnyObject(map);
  }

  @Override
  public AnyObject load(String in) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    Map<String, Object> map = mapper.readValue(in, Map.class);
    return new MapBasedAnyObject(map);
  }

}
