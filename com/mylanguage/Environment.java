package com.mylanguage;

import java.util.Map;
import java.util.HashMap;

public  class Environment {
   private Environment enclosing;
   private final Map<String, Object> values ;
   Environment(){
      values=new HashMap<>();
   }
   Environment(Environment enclosing){
      this();
      this.enclosing=enclosing;
   }

   void define(String name, Object value) {
      values.put(name, value);
   }
   void assign(Token name, Object value) {
      if (values.containsKey(name.lexeme)) {
         values.put(name.lexeme, value);
         return;
      }
      if(enclosing!=null){
         enclosing.assign(name,value);
         return;
      }
      throw new RuntimeError(name,"Undefined variable '" + name.lexeme + "'.");
   }
   Object get(Token name) {
      if (values.containsKey(name.lexeme)) {
         return values.get(name.lexeme);
      }
      if(enclosing!=null)return enclosing.get(name);
      throw new RuntimeError(name,"Undefined variable '" + name.lexeme + "'.");
   }
}