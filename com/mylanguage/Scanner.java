package com.mylanguage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import static com.mylanguage.TokenType.*;

class Scanner {
    private final String source;
    private final List<Token> tokens;
    private int start=0;
    private int current=0;
    private int line=1;
    private static final Map<String, TokenType> keywords;
    static {
        keywords = new HashMap<>();
        keywords.put("and", AND);
        keywords.put("class",  CLASS);
        keywords.put("else", ELSE);
        keywords.put("false",  FALSE);
        keywords.put("for", FOR);
        keywords.put("fun",FUN);
        keywords.put("if", IF);
        keywords.put("nil", NIL);
        keywords.put("or", OR);
        keywords.put("print",  PRINT);
        keywords.put("return", RETURN);
        keywords.put("super",  SUPER);
        keywords.put("this",THIS);
        keywords.put("true", TRUE);
        keywords.put("var", VAR);
        keywords.put("while",  WHILE);
    }

    Scanner(String source){
        this.source=source;
        this.tokens=new ArrayList<>();
    }

    public List<Token> scanTokens(){
        while(!isAtEnd()){
            // We are at the beginning of the next lexeme.
            start=current;
            scanToken();
        }
        tokens.add(new Token(EOF,"",null,this.line));
        return tokens;
    }

    private boolean isAtEnd(){
        return this.current >= this.source.length();
    }

    private char advance(){
        this.current++;
        return this.source.charAt(this.current-1);
    }

    private void scanToken(){
        char c=advance();
        switch (c){
            case '(': addToken(LEFT_PAREN); break;
            case ')': addToken(RIGHT_PAREN); break;
            case '{': addToken(LEFT_BRACE); break;
            case '}': addToken(RIGHT_BRACE); break;
            case ',': addToken(COMMA); break;
            case '.': addToken(DOT); break;
            case '-': addToken(MINUS); break;
            case '+': addToken(PLUS); break;
            case ';': addToken(SEMICOLON); break;
            case '*': addToken(STAR); break;
            case '!':
                addToken(match('=') ? BANG_EQUAL : BANG);
                break;
            case '=':
                addToken(match('=') ? EQUAL_EQUAL : EQUAL);
                break;
            case '<':
                addToken(match('=') ? LESS_EQUAL : LESS);
                break;
            case '>':
                addToken(match('=') ? GREATER_EQUAL : GREATER);
                break;
            case '/':
                if (match('/')) {
                    // A comment goes until the end of the line.
                    while (peek() != '\n' && !isAtEnd()) advance();
                } else {
                    addToken(SLASH);
                }
                break;
            // Ignore whitespace.
            case ' ': break;
            case '\r': break;
            case '\t': break;
            case '\n':
                this.line++;
                break;
            case '"': string(); break;

            default:
                if(isDigit(c)){
                    number();
                } else if(isAlpha(c)){
                    identifier();
                } else {
                    Hapi.error(this.line,"Unexpected character.");
                }
        }
    }

    private void addToken(TokenType type){
        addToken(type,null);
    }

    private void addToken(TokenType type,Object literal){
        String lexeme = this.source.substring(start,current);
        this.tokens.add(new Token(type,lexeme,literal,this.line));
    }

    private boolean match(char expected){
        if(isAtEnd()) return false;
        if(this.source.charAt(this.current) != expected) return false;

        this.current++;
        return true;
    }

    private char peek(){
        if(isAtEnd()) return '\0'; // null character
        return this.source.charAt(this.current);
    }

    private char peekNext() {
        if (current + 1 >= source.length()) return '\0';
        return source.charAt(current + 1);
    }

    private void string(){
        while(peek() != '"' && !isAtEnd()){
            if (peek() == '\n') this.line++;
            advance();
        }
        if(isAtEnd()){
            Hapi.error(line, "Unterminated string.");
            return;
        }
        // closing ".
        advance();
        // Trim the surrounding quotes.
        String value = source.substring(start + 1, current - 1);
        addToken(STRING, value);
    }

    private boolean isDigit(char ch){
        return ch >= '0' && ch <= '9';
    }

    private void number() {
        while (isDigit(peek())) advance();
        // Look for a fractional part.
        if (peek() == '.' && isDigit(peekNext())) {
            // Consume the "."
            advance();
            while (isDigit(peek())) advance();
        }
        addToken(NUMBER, Double.parseDouble(source.substring(start, current)));
    }

    private boolean isAlpha(char ch){
        return (ch>='A' && ch<='Z') || (ch>='a' && ch<='z') || ch == '_';
    }

    private boolean isAlphaNumeric(char ch){
        return isAlpha(ch) || isDigit(ch);
    }

    private void identifier(){
        while (isAlphaNumeric(peek())) advance();
        String text = source.substring(start, current);
        TokenType type = keywords.get(text);
        if (type == null) type = IDENTIFIER;

        addToken(type);
    }

}