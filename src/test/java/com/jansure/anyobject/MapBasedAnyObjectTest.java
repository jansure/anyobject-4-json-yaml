package com.jansure.anyobject;

import java.io.*;
import java.util.Map;

import static org.junit.Assert.*;

import com.jansure.anyobject.model.Address;
import com.jansure.anyobject.model.Person;
import org.junit.Before;
import org.junit.Test;

import com.jansure.anyobject.impl.SnakeYAMLLoader;
import org.kamranzafar.commons.cloner.ObjectCloner;

public class MapBasedAnyObjectTest {

  MapBasedAnyObject yaml;
  
  @Before
  public void init() throws Exception {
    InputStream in = getClass().getResourceAsStream("/test.yaml");
    this.yaml = (MapBasedAnyObject) SnakeYAMLLoader.getInstance().load(in);
  }

  @Test
  public void testGet() throws Exception {    
    assertNotNull(yaml.get("receipt"));
    assertNull(yaml.get("nothing")); 
    assertNotNull(yaml.get("customer.given"));

    System.out.println(yaml.getMap());

    Map m = new ObjectCloner<Map>().deepClone(yaml.getMap());
    m.put("receipt", "jansure");
    yaml.setMap(m);
    //System.out.println(yaml.getMap());
   // System.out.println(yaml.toYaml());

    //FileOutputStream outputStream = new FileOutputStream(
    //        new File(System.getProperty("user.dir") + "/dest/test_dest.yaml"));

    Writer writer = new FileWriter(new File(System.getProperty("user.dir") + "/dest/test_dest.yaml"));
    writer.write(yaml.toYaml());
    //outputStream.write(yaml.toYaml().getBytes());
    writer.flush();
    writer.close();

    /**
     * testing for any object to map ,meanwhile any object to yaml or json
     */
    Address address = Address.builder().countryCode(86).doorCode(1.2).detail("xibeiwang Street bejing").build();
    Person person = Person.builder().address(address).age(18).name("jansure").build();
    yaml.objectToMap(person);
    System.out.println(yaml.toYaml());
    System.out.println(yaml.toJson());
  }

  @Test
  public void testGetAnyObject() throws Exception {    
    assertTrue((yaml.getAnyObject("customer") instanceof AnyObject));
    assertNull(yaml.getAnyObject("nothing")); 
  }  

  @Test
  public void testGetIterable() throws Exception {    
    assertTrue((yaml.getIterable("items") instanceof Iterable));
    assertNull(yaml.getIterable("nothing")); 
  }  
  
  @Test(expected = NullPointerException.class)
  public void testWrongPath() throws Exception { 
    yaml.get("nothing.given");   
  }
}
