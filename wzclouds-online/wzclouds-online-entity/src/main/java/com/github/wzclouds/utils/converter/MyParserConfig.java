package com.github.wzclouds.utils.converter;

import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;

import java.lang.reflect.Type;

public class MyParserConfig extends ParserConfig {
  @Override
  public ObjectDeserializer getDeserializer(Class<?> clazz, Type type) {
    ObjectDeserializer derializer;
    if (clazz.isEnum()) {
      Class<?> deserClass = null;
      JSONType jsonType = clazz.getAnnotation(JSONType.class);
      if (jsonType != null) {
        deserClass = jsonType.deserializer();
        try {
          derializer = (ObjectDeserializer) deserClass.newInstance();
          this.putDeserializer(type, derializer);
          return derializer;
        } catch (Throwable error) {
          // skip
        }
      }
      //这里替换了原来的反序列化器。
      derializer = new EnumValueDeserializer();
      return derializer;
    }
    return super.getDeserializer(clazz, type);
  }
}
