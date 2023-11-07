package skyclash.skyclash.fileIO;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import skyclash.skyclash.chestgen.LootChest;
import skyclash.skyclash.main;

import java.io.*;
import java.util.logging.Level;

public class LootChestIO {
    private static final File savedChestsDir = new File( "LootChests" + File.separator);

    public static void saveChest(LootChest lootChest) {
        if (!savedChestsDir.exists())
            if (!savedChestsDir.mkdirs()) {
                main.getPlugin(main.class).getLogger().log(Level.SEVERE, "Could not create the folder for saving the LootChests");
                return;
            }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try(Writer writer = new FileWriter(savedChestsDir.getAbsolutePath() + File.separator + lootChest.getName() + ".json")) {
            gson.toJson(lootChest, writer);
            main.getPlugin(main.class).getLogger().log(Level.INFO, "LootChest: '" + lootChest.getName() + "' was successfully saved!");

        } catch (IOException e) {
            main.getPlugin(main.class).getLogger().log(Level.SEVERE, "Couldn't save the LootChest: '" + lootChest.getName() + "'!");
            main.getPlugin(main.class).getLogger().log(Level.SEVERE, e.getMessage());
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
            main.getPlugin(main.class).getLogger().log(Level.SEVERE, "Couldn't load the LootChest: '" + name + "'!");
            main.getPlugin(main.class).getLogger().log(Level.SEVERE, e.getMessage());
        }

        return lootChest;
    }
}

