package com.github.wzclouds.utils.converter;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;

import java.lang.reflect.Type;

public class EnumValueDeserializer implements ObjectDeserializer {

  @Override
  public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
    final JSONLexer lexer = parser.lexer;
    final int token = lexer.token();
    Class cls = (Class) type;
    Object[] enumConstants = cls.getEnumConstants();
    if (EnumValue.class.isAssignableFrom(cls)) {
      for (Object enumConstant : enumConstants) {
        if (((EnumValue) enumConstant).code().equals(lexer.stringVal())) {
          return (T) enumConstant;
        }
      }
    } else {
      //没实现EnumValue接口的 默认的按名字或者按ordinal
      if (token == JSONToken.LITERAL_INT) {
        int intValue = lexer.intValue();
        lexer.nextToken(JSONToken.COMMA);

        if (intValue < 0 || intValue > enumConstants.length) {
          throw new JSONException("parse enum " + cls.getName() + " error, value : " + intValue);
        }
        return (T) enumConstants[intValue];
      } else if (token == JSONToken.LITERAL_STRING) {
        return (T) Enum.valueOf(cls, lexer.stringVal());
      }
    }
    return null;
  }

  @Override
  public int getFastMatchToken() {
    return JSONToken.LITERAL_INT;
  }
}
