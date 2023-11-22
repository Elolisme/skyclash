package skyclash.skyclash.fileIO;

import java.util.ArrayList;

public class MapData {
    public String icon;
    public Boolean ignore;
    public ArrayList<ArrayList<Integer>> chests = new ArrayList<>();
    public ArrayList<ArrayList<Integer>> spawns = new ArrayList<>();
    public Boolean isdefault;

    public MapData() {}
    
    public MapData(String icon, Boolean ignore, ArrayList<ArrayList<Integer>> chests,
            ArrayList<ArrayList<Integer>> spawns, Boolean isdefault) {
        this.icon = icon;
        this.ignore = ignore;
        this.chests = chests;
        this.spawns = spawns;
        this.isdefault = isdefault;
    }

    public String getIcon() {
        return icon;
    }

    public Boolean getIgnore() {
        return ignore;
    }

    public ArrayList<ArrayList<Integer>> getChests() {
        return chests;
    }

    public ArrayList<ArrayList<Integer>> getSpawns() {
        return spawns;
    }

    public Boolean getIsdefault() {
        return isdefault;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setIgnore(Boolean ignore) {
        this.ignore = ignore;
    }

    public void setChests(ArrayList<ArrayList<Integer>> chests) {
        this.chests = chests;
    }

    public void setSpawns(ArrayList<ArrayList<Integer>> spawns) {
        this.spawns = spawns;
    }

    public void setIsdefault(Boolean isdefault) {
        this.isdefault = isdefault;
    }
}
