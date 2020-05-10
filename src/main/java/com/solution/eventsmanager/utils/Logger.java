package com.solution.eventsmanager.utils;

import java.io.FileWriter;
import java.io.IOException;

public class Logger {
    public void info(String str){
        try(FileWriter writer = new FileWriter("app.log", true))
        {
            writer.write(str);
            writer.write("\n\t");
            writer.flush();
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }
    }
}
