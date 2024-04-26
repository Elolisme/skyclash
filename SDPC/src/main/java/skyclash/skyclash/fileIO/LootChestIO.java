package skyclash.skyclash.fileIO;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.md_5.bungee.api.ChatColor;
import skyclash.skyclash.chestgen.LootChest;
import skyclash.skyclash.main;

import java.io.*;
import java.net.URL;
import java.util.logging.Level;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;

public class LootChestIO {
    private static final File savedChestsDir = new File( "plugins"+File.separator+"SDPC"+File.separator+"LootChests" + File.separator);

    public static void saveChest(LootChest lootChest) {
        if (!savedChestsDir.exists()) {
            if (!savedChestsDir.mkdirs()) {
                main.plugin.getLogger().log(Level.SEVERE, "Could not create the folder for saving the LootChests");
                return;
            }
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try(Writer writer = new FileWriter(savedChestsDir.getAbsolutePath() + File.separator + lootChest.getName() + ".json")) {
            gson.toJson(lootChest, writer);
            main.plugin.getLogger().log(Level.INFO, "LootChest: '" + lootChest.getName() + "' was successfully saved!");

        } catch (IOException e) {
            main.plugin.getLogger().log(Level.SEVERE, "Couldn't save the LootChest: '" + lootChest.getName() + "'!");
            main.plugin.getLogger().log(Level.SEVERE, e.getMessage());
        }
    }

    public static LootChest loadChest(String name) {
        Gson gson = new Gson();
        LootChest lootChest = null;

        try {
            Reader reader = new BufferedReader(new FileReader(savedChestsDir.getAbsolutePath() + File.separator + name + ".json"));
            lootChest = gson.fromJson(reader,LootChest.class);
            reader.close();
        } catch (IOException e) {
            main.plugin.getLogger().log(Level.SEVERE, "Couldn't load the LootChest: '" + name + "'!");
            main.plugin.getLogger().log(Level.SEVERE, e.getMessage());
        }

        return lootChest;
    }

    public static void downloadLootChestFiles() {
        if (savedChestsDir.exists()) {
            return;
        }
        if (!savedChestsDir.mkdirs()) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED+"Could not create the folder for saving the LootChests");
            return;
        }
        downloadFilesNoChecks();
        
    }

    @SuppressWarnings("deprecation")
    public static void downloadFilesNoChecks() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN+"Downloading the lootchest files...");
        try {
            URL url = new URL("https", "raw.githubusercontent.com", "/Elolisme/skyclash/main/LootChests/mid.json");
            File file = new File( "plugins"+File.separator+"SDPC"+File.separator+"LootChests" + File.separator + "mid.json");
            FileUtils.copyURLToFile(url, file, 10*1000, 10*1000);
            
            URL url2 = new URL("https", "raw.githubusercontent.com", "/Elolisme/skyclash/main/LootChests/spawn.json");
            File file2 = new File( "plugins"+File.separator+"SDPC"+File.separator+"LootChests" + File.separator + "spawn.json");
            FileUtils.copyURLToFile(url2, file2, 10*1000, 10*1000);
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage(e.getMessage());
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED+"The download failed");
            return;
        }
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN+"The files have been downloaded");
    }
}

