package com.mylanguage;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Hapi {
    private static boolean hadError = false;
    // run file
    private static void runFile(String path) throws IOException{
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        run(new String(bytes,Charset.defaultCharset()));

        // indicate an error in exit code
        if(hadError){
            System.exit(65);
        }
    }

    private static void run(String source){
        Scanner scanner = new Scanner(source);
        List<Token> tokens = scanner.scanTokens();

        for(Token token : tokens){
            System.out.println(token);
        }
    }

    public static void main(String[] args) throws IOException {
        if(args.length == 1){
            runFile(args[0]);
        }else{
            System.err.println("Use jhapi file_name.hapi");
            System.exit(64);
        }
    }

    // error handling
    protected static void error(int line, String message){
        report(line,"",message);
    }

    private static void report(int line,String where,String message){
        System.err.println("[line "+line+" ] Error "+where+" : "+message);
        hadError=true;
    }

}