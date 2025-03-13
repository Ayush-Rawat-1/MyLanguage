package com.mylanguage;

import java.util.ArrayList;
import java.util.List;
import static com.mylanguage.TokenType.*;

class Scanner {
    private final String source;
    private final List<Token> tokens;
    private int start=0;
    private int current=0;
    private int line=1;

    Scanner(String source){
        this.source=source;
        this.tokens=new ArrayList<>();
    }

    public List<Token> scanTokens(){
        while(!isAtEnd()){
            // We are at the beginning of the next lexeme.
            start=current;
            advance();
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
                Hapi.error(this.line,"Unexpected character.");
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

}