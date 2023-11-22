package skyclash.skyclash.fileIO;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;

import net.md_5.bungee.api.ChatColor;
import skyclash.skyclash.Scheduler;

public class MOTD {
    public void changeMOTD() {
        String optionsPath = "server.properties";
        String motdBankPath = "plugins"+File.separator+"SDPC"+File.separator+"motdList.txt";
        if (!(new File(motdBankPath).exists())) {
            return;
        }
        Bukkit.getConsoleSender().sendMessage(ChatColor.GRAY+"MOTD Changed");

        ArrayList<String> motds = readFile(motdBankPath);
        String motd = motds.get(new Random().nextInt(motds.size()));
        
        ArrayList<String> options = setMOTD(readFile(optionsPath), motd);
        String output = ArrayToString(options);
        writeToFile(optionsPath, output);
    }

    private void writeToFile(String path, String output) {
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter(path));
            writer.write(output);
            writer.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String ArrayToString(ArrayList<String> array) {
        String outputstring = "";
        for (String line:array) {
            outputstring = outputstring + line + "\n";
        }
        return outputstring;

    }

    private ArrayList<String> setMOTD(ArrayList<String> array, String motd) {
        ArrayList<String> output = new ArrayList<>();
        array.forEach((line)->{
            if (line.contains("=")) {
                String[] splitline = line.split("=");
                if (splitline[0].equals("motd")) {
                    splitline[1] = motd;
                }
                String newString;
                if (splitline.length != 2) {newString = splitline[0]+"=";} else {newString = splitline[0]+"="+splitline[1];}
                output.add(newString);
            } else {
                output.add(line);
            }
        });
        return output;
    }

    private ArrayList<String> readFile(String path) {
        ArrayList<String> lines = new ArrayList<>();
        try {
            BufferedReader bufReader = new BufferedReader(new FileReader(path));
            
            String line = bufReader.readLine();
            while (line != null) {
                lines.add(line);
                line = bufReader.readLine();
            }
            bufReader.close();
        } catch (Exception e) {}
        
        return lines;
    }

    
    public void testlag(int timeSeconds) {
        long initial = System.currentTimeMillis();
        int expected = timeSeconds;
        new Scheduler().scheduleTask(()->{
            long initial1 = initial;
            long change = System.currentTimeMillis() - initial1;
            Bukkit.getLogger().info(expected+" expected seconds is "+change+" milliseconds");
        }, 20*timeSeconds);
    }
    
}
