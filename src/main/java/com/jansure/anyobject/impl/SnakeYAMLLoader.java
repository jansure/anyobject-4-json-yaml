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
import org.yaml.snakeyaml.Yaml;

public final class SnakeYAMLLoader implements Loader {

  private static final SnakeYAMLLoader instance = new SnakeYAMLLoader();

  public static SnakeYAMLLoader getInstance() {
      return instance;
  }

  private SnakeYAMLLoader() {
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
    Yaml yaml = new Yaml();
    Map<String, Object> map = (Map) yaml.load(in);
    return new MapBasedAnyObject(map);
  }

  @Override
  public AnyObject load(Reader in) throws IOException {
    Yaml yaml = new Yaml();
    Map<String, Object> map = (Map) yaml.load(in);
    return new MapBasedAnyObject(map);
  }

  @Override
  public AnyObject load(String in) throws IOException {
    Yaml yaml = new Yaml();
    Map<String, Object> map = (Map) yaml.load(in);
    return new MapBasedAnyObject(map);
  }

}
