package com.jansure.anyobject.impl;

import java.io.InputStream;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import com.jansure.anyobject.AnyObject;

public class SnakeYamlLoaderTest {

  AnyObject yaml;
  
  @Before
  public void init() throws Exception {
    InputStream in = getClass().getResourceAsStream("/test.yaml");
    this.yaml = SnakeYAMLLoader.getInstance().load(in);
  }

  @Test
  public void testYAMLData() throws Exception {    
    assertEquals("jansure.zhang", yaml.getString("receipt"));
    assertEquals("lenovo", yaml.getString("customer.given"));
    assertEquals("china", yaml.getString("ship-to.state"));
  }

  @Test
  public void testYAMLIterator() {
    Iterable items = yaml.getIterable("items");
    for (Object o : items) {
      assertTrue((o instanceof AnyObject));
      AnyObject item = (AnyObject) o;
      assertNotNull(item.getString("part_no"));
    }
  } 

  @Test
  public void testNumbers() {
    Iterable items = yaml.getIterable("numbers");
    int num = 0;
    for (Object o : items) {
      Integer item = (Integer) o;
      assertEquals(num++, item.intValue());
    }
  } 
}
