package com.mylanguage;

import static com.mylanguage.TokenType.*;

import java.util.HashMap;
import java.util.Map;

public class Keywords {
     Map<String, TokenType> keywords;
 Keywords() {
    keywords = new HashMap<>();
    keywords.put("and",AND);
    keywords.put("class",CLASS);
    keywords.put("else",ELSE);
    keywords.put("false",FALSE);
    keywords.put("for",FOR);
    keywords.put("fun",FUN);
    keywords.put("if",IF);
    keywords.put("nil",NIL);
    keywords.put("or",OR);
    keywords.put("print",PRINT);
    keywords.put("return",RETURN);
    keywords.put("super",SUPER);
    keywords.put("this",THIS);
    keywords.put("true",TRUE);
    keywords.put("var",VAR);
    keywords.put("while",  WHILE);
  }
}