package com.jansure.anyobject.impl;

import java.io.InputStream;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import com.jansure.anyobject.AnyObject;

public class JacksonJSONLoaderTest {

  AnyObject json;
  
  @Before
  public void init() throws Exception { 
    InputStream in = getClass().getResourceAsStream("/test.json");
    this.json = JacksonJSONLoader.getInstance().load(in);
  }

  @Test
  public void testJSONData() throws Exception {   
    assertEquals("John", json.getString("firstName"));
    assertEquals(25, json.getInteger("age").intValue());
    assertEquals("New York", json.getString("address.city"));
  }

  @Test
  public void testJSONIterator() {
    Iterable items = json.getIterable("phoneNumber");
    for (Object o : items) {
      assertTrue((o instanceof AnyObject));
      AnyObject item = (AnyObject) o;
      assertNotNull(item.getString("number"));
    }
  } 

  @Test
  public void testNumbers() {
    Iterable items = json.getIterable("numbers");
    int num = 0;
    for (Object o : items) {
      assertTrue((o instanceof Integer));
      Integer item = (Integer) o;
      assertEquals(num++, item.intValue());
    }
  } 
}
