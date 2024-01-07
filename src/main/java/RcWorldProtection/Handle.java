package RcWorldProtection;

import RcWorldProtection.World.World;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.item.Item;
import cn.nukkit.level.Level;
import cn.nukkit.utils.Config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Handle {

    public static ArrayList<String> getAllWorlds(){
        ArrayList<String> list = new ArrayList<>();
        for (Level level : Server.getInstance().getLevels().values()){
            list.add(level.getName());
        }
        return list;
    }

    public static ArrayList<String> getAllLoadWorlds(){
        ArrayList<String> list = new ArrayList<>();
        for (String name : Main.loadWorld.keySet()){
            list.add(name);
        }
        return list;
    }

    public static ArrayList<String> getAllUnLoadWorlds(){
        ArrayList<String> list = Handle.getAllWorlds();
        for(String name : Handle.getAllLoadWorlds()){
            if(list.contains(name)) list.remove(name);
        }
        return list;
    }

    public static String[] getDefaultFiles(String fileName) {
        List<String> names = new ArrayList<>();
        File files = new File(Main.instance.getDataFolder()+ "/"+fileName);
        if(files.isDirectory()){
            File[] filesArray = files.listFiles();
            if(filesArray != null){
                for (File file : filesArray) {
                    names.add(file.getName().substring(0, file.getName().lastIndexOf(".")));
                }
            }
        }
        return names.toArray(new String[0]);
    }

    public static ArrayList<String> getWhiteList(){
        ArrayList<String> list = (ArrayList<String>) Main.instance.config.getStringList("管理员");
        return list;
    }

    public static void addWhiteList(String name){
        Config config = Main.instance.config;
        ArrayList<String> list = (ArrayList<String>) config.getStringList("管理员");
        if(!list.contains(name)) list.add(name);
        config.set("管理员",list);
        config.save();
    }

    public static void removeWhiteList(String name){
        Config config = Main.instance.config;
        ArrayList<String> list = (ArrayList<String>) config.getStringList("管理员");
        if(list.contains(name)) list.remove(name);
        config.set("管理员",list);
        config.save();
    }

    public static boolean isWhiteList(String name){
        Config config = Main.instance.config;
        ArrayList<String> list = (ArrayList<String>) config.getStringList("管理员");
        return list.contains(name);
    }

    public static void sendMessage(Player player,String type,String text){
        Config config = new Config(Main.instance.getDataFolder()+"/Message.yml");
        if(!config.getBoolean("消息开关")) return;
        if(config.getString(text).equals("")) return;
        switch (type) {
            case "message" -> player.sendMessage(config.getString(text));
            case "title" -> player.sendTitle(config.getString(text));
            case "popup" -> player.sendPopup(config.getString(text));
            case "tip" -> player.sendTip(config.getString(text));
        }
    }

    public static boolean isRule(Block block, World world, String type){
        return switch(type){
            case "破坏" -> world.getBreakBlock().contains(Item.get(block.getItemId()).getNamespaceId());
            case "放置" -> world.getPlaceBlock().contains(Item.get(block.getItemId()).getNamespaceId());
            case "交互" -> world.getInteractBlock().contains(Item.get(block.getItemId()).getNamespaceId());
            default -> false;
        };
    }

    public static boolean isRule(Item item, World world){
        return world.getInteractItem().contains(item.getNamespaceId());
    }

}
