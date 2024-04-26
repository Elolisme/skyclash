package skyclash.skyclash.commands;

import java.io.File;
import java.net.URL;
import java.util.List;
import static skyclash.skyclash.main.plugin;
import static skyclash.skyclash.main.pluginFileName;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import skyclash.skyclash.Scheduler;
import skyclash.skyclash.fileIO.LootChestIO;

@SuppressWarnings("deprecation")
public class update implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Please add more arguments\nUse /update [true/false]");
            return true;
        }

        Boolean update;
        if ("true".equals(args[0])) {
            update = true;
        } else if ("false".equals(args[0])) {
            update = false;
        } else {
            sender.sendMessage(ChatColor.RED + "Decision must be boolean");
            return true;
        }

        checkVersion(sender, update);
        return true;
    }

    private static void checkVersion(CommandSender sender, Boolean updateFile) {
        String latestVersion = getCurrentVersion(sender);
        if (latestVersion == null) {
            return;
        }

        // find files that are the plugin
        File folder = new File("plugins/");
        File[] listOfFiles = folder.listFiles();
        Boolean containsNew = false;

        for (File file : listOfFiles) {
            if (file.isFile() && file.getName().contains("SDPC-")) {
                String version = file.getName().replace("SDPC-", "").replace(".jar", "");
                if (version.equals(latestVersion)) {
                    containsNew = true;
                }
            }
        }

        // update file
        if (!containsNew) {
            sender.sendMessage(ChatColor.YELLOW + "Not up to date, should be on version " + latestVersion);
            if (!updateFile) { return; }
            updateFile(sender, latestVersion);
            return;
        }

        sender.sendMessage(ChatColor.GREEN + "SDPC is up to date: version " + latestVersion);
        if (!pluginFileName.replace("SDPC-", "").replace(".jar", "").equals(latestVersion)) {
            Bukkit.getPluginManager().disablePlugin(plugin);
        } else {
            new Scheduler().scheduleTask(() -> {
                for (File file : listOfFiles) {
                    if (file.isFile() && file.getName().contains("SDPC-")) {
                        String version = file.getName().replace("SDPC-", "").replace(".jar", "");
                        if (!version.equals(latestVersion)) {
                            file.delete();
                        }
                    }
                }
            }, 20);
        }
    }

    private static String getCurrentVersion(CommandSender sender) {
        URL url;
        String latestVersion = null;
        try {
            url = new URL("https://raw.githubusercontent.com/Elolisme/skyclash/main/CHANGELOG.md");
            List<String> lines = Resources.readLines(url, Charsets.UTF_8);
            latestVersion = lines.get(1).replace("## v", "");
        } catch (Exception e) {
            e.printStackTrace();
            sender.sendMessage(ChatColor.RED + "Could not get file with version from Github");
        }
        return latestVersion;
    }

    private static void updateFile(CommandSender sender, String latestVersion) {
        sender.sendMessage(ChatColor.GREEN + "Downloading the latest version of SDPC...");

        try {
            URL url2 = new URL("https", "github.com",
                    "/Elolisme/skyclash/raw/main/SDPC/target/SDPC-" + latestVersion + ".jar");
            File file2 = new File("plugins" + File.separator + "SDPC-" + latestVersion + ".jar");
            FileUtils.copyURLToFile(url2, file2, 10 * 1000, 10 * 1000);
        } catch (Exception e) {
            sender.sendMessage(e.getMessage());
            sender.sendMessage(ChatColor.RED + "The download failed");
            return;
        }

        if (new File("plugins" + File.separator + "SDPC" + File.separator + "LootChests" + File.separator).exists()) {
            LootChestIO.downloadFilesNoChecks();
        } else {
            LootChestIO.downloadLootChestFiles();
        }

        sender.sendMessage(ChatColor.GREEN + "The files have been downloaded");
        sender.sendMessage(ChatColor.RED + "Restart the server immediately");
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Restart the server immediately");
        Bukkit.shutdown();
    }
}
