package com.jansure.anyobject;

import java.io.File;
import java.io.InputStream;
import java.io.Reader;
import java.io.IOException;

public interface Loader {
  AnyObject load(File file) throws IOException;
  AnyObject load(InputStream in) throws IOException;
  AnyObject load(Reader in) throws IOException;
  AnyObject load(String in) throws IOException;
}
