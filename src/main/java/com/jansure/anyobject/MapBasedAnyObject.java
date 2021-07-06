package com.jansure.anyobject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class MapBasedAnyObject implements AnyObject {

  private Map map;

  public void setMap(Map map) {
    this.map = map;
  }

  public Map getMap() {
    return this.map;
  }

  private final String separator;
  
  public MapBasedAnyObject(Map map) {
    this(map, "\\.");
  }

  public MapBasedAnyObject(Map map, String separator) {
    this.map = new LinkedHashMap(map);
    this.separator = separator;
  }

  // See https://bugs.eclipse.org/bugs/show_bug.cgi?id=98379
  // See http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6302954
  <T> T get(String key) {
    return this.<T>get(map, key.split("\\."));    
  }

  private <T> T get(Map inner, String[] key) {
    Object o = inner.get(key[0]);
    return (T) ((key.length > 1) 
      ? get((Map) o, Arrays.copyOfRange(key, 1, key.length)) : o); 
  } 
  
  @Override
  public AnyObject getAnyObject(String key) {
    Object o = get(key);
    if (o == null) {
      return null;
    }
    if (o instanceof Map) {
      return new MapBasedAnyObject((Map) o);
    }
    return (MapBasedAnyObject) o;
  }
  
  @Override
  public <T> Iterable<T> getIterable(final String key) {
    Iterable<T> iterable = get(key);
    return (iterable == null) ? null : new AnyIterable(iterable);
  }

  @Override
  public Iterable<AnyTuple> getTuples() {
    return new AnyTupleIterable(map);
  }
  
  @Override
  public Long getLong(String key) {
    return (Long) get(key);
  }
  
  @Override
  public Integer getInteger(String key) {
    return (Integer) get(key);
  }

  @Override
  public String getString(String key) {
    return (String) get(key);
  }

  @Override
  public Double getDouble(String key) {
    return (Double) get(key);  
  }

  @Override
  public Float getFloat(String key) {
    return (Float) get(key);    
  }

  @Override
  public Boolean getBoolean(String key) {
    return (Boolean) get(key);    
  }

  private Object get(String key, Object defValue) {
    Object o = get(key);
    return (o != null) ? o : defValue;
  }

  @Override
  public Long getLong(String key, Long defValue) {
    return (Long) get(key, defValue);
  }

  @Override
  public Integer getInteger(String key, Integer defValue) {
    return (Integer) get(key, defValue);
  }

  @Override
  public String getString(String key, String defValue) {
    return (String) get(key, defValue);
  }

  @Override
  public Double getDouble(String key, Double defValue) {
    return (Double) get(key, defValue);  
  }

  @Override
  public Float getFloat(String key, Float defValue) {
    return (Float) get(key, defValue);    
  }

  @Override
  public Boolean getBoolean(String key, Boolean defValue) {
    return (Boolean) get(key, defValue);    
  }

  @Override
  public byte[] toJsonAsBytes() throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    try {
      mapper.writeValue(out, map);
      return out.toByteArray();
    } finally {
      out.close();
    }
  }

  @Override
  public String toJson() throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    try {
      mapper.writeValue(out, map);
      return out.toString();
    } finally {
      out.close();
    }
  }

  @Override
  public String toYaml() throws IOException {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    JavaTimeModule javaTimeModule = new JavaTimeModule();
    mapper.registerModule(javaTimeModule);
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    try {
      mapper.writeValue(out, map);
      return out.toString();
    } finally {
      out.close();
    }
  }

  public Map<String, Object> objectToMap(Object obj) throws Exception {
    if(obj == null) {
      return null;
    }
    //获取关联的所有类，本类以及所有父类
    boolean ret = true;
    Class oo = obj.getClass();
    List<Class> clazzs = new ArrayList<Class>();
    while(ret) {
      clazzs.add(oo);
      oo = oo.getSuperclass();
      if (oo == null || oo == Object.class) {
        break;
      }
    }

    this.map = new HashMap<String, Object>();

    for (int i=0;i<clazzs.size();i++) {
      Field[] declaredFields = clazzs.get(i).getDeclaredFields();
      for (Field field : declaredFields) {
        int mod = field.getModifiers();
        //过滤 static 和 final 类型
        if(Modifier.isStatic(mod) || Modifier.isFinal(mod)){
          continue;
        }
        field.setAccessible(true);
        map.put(field.getName(), field.get(obj));
      }
    }

    return map;
  }

  @Override
  public String toString() {
    return map.toString();
  }

  private final static class AnyIterator implements Iterator {

    private final Iterator iterator;

    AnyIterator(Iterator iterator) {
      this.iterator = iterator;
    }
    
    @Override
    public boolean hasNext() {
      return iterator.hasNext();
    }
    
    @Override
    public Object next() {
      Object o = iterator.next();
      if (o instanceof Iterable) {
        return new AnyIterable((Iterable) o);
      }
      if (o instanceof Map) {
        return new MapBasedAnyObject((Map) o);
      }
      return o;
    }
    
    @Override
    public void remove() {
      throw new UnsupportedOperationException();
    }
  }

  private final static class AnyIterable implements Iterable {
    private final Iterable iterable;

    AnyIterable(Iterable iterable) {
      this.iterable = iterable;
    }
    
    @Override
    public Iterator iterator() {
      return new AnyIterator(iterable.iterator());
    }
  }
 
  private final static class AnyTupleIterable implements Iterable<AnyTuple> {
    private final Map map;

    AnyTupleIterable(Map map) {
      this.map = map;
    }
    
    @Override
    public Iterator<AnyTuple> iterator() {
      return new AnyTupleIterator(map.entrySet().iterator());
    }
  }
 
  private final static class AnyTupleIterator implements Iterator<AnyTuple> {

    private final Iterator iterator;

    AnyTupleIterator(Iterator iterator) {
      this.iterator = iterator;
    }
    
    @Override
    public boolean hasNext() {
      return iterator.hasNext();
    }
    
    @Override
    public AnyTuple next() {
      Map.Entry<String, Object> me = (Map.Entry<String, Object>) iterator.next();
      String key = me.getKey();
      Object o = me.getValue();
      if (o instanceof Iterable) {
        return new AnyTuple(key, new AnyIterable((Iterable) o));
      }
      if (o instanceof Map) {
        return new AnyTuple(key, new MapBasedAnyObject((Map) o));
      }
      return new AnyTuple(key, o);
    }
    
    @Override
    public void remove() {
      throw new UnsupportedOperationException();
    }
  }
}
