package RcWorldProtection;

import RcWorldProtection.Form.WorldForm;
import RcWorldProtection.Task.MoveTask;
import RcWorldProtection.World.World;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.event.Listener;
import cn.nukkit.level.Level;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;

import java.io.File;
import java.util.LinkedHashMap;

public class Main extends PluginBase implements Listener {

    public static Main instance;

    public Config config;

    public Config message;

    public static LinkedHashMap<String, World> loadWorld = new LinkedHashMap<>();

    @Override
    public void onEnable(){
        instance = this;
        this.getServer().getPluginManager().registerEvents(new Events(),this);
        File file = new File(this.getDataFolder() + "/Worlds");
        if(!file.exists() && !file.mkdirs()){
            this.getLogger().warning("Worlds文件夹加载失败");
        }
        this.saveResource("Config.yml","/Config.yml",false);
        this.config = new Config(Main.instance.getDataFolder()+"/Config.yml");
        this.saveResource("Message.yml","/Message.yml",false);
        this.message = new Config(Main.instance.getDataFolder()+"/Message.yml");
        this.getLogger().info("开始读取世界配置文件");
        for(String name: Handle.getDefaultFiles("Worlds")){
            this.getLogger().info("读取 "+name+".yml");
            World world = null;
            try {
                world = World.loadWorld(name,new Config(this.getDataFolder()+"/Worlds/"+name+".yml",Config.YAML));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            if(world != null){
                loadWorld.put(name,world);
                this.getLogger().info(name+"世界配置读取成功");
            }else{
                this.getLogger().warning(name+"世界配置读取失败");
            }
        }
        this.getServer().getScheduler().scheduleRepeatingTask(new MoveTask(this),20);
        this.getLogger().info("插件加载成功，作者：若尘");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if(!sender.isOp()) {
            sender.sendMessage("权限不足");
            return false;
        }else{
            if(command.getName().equals("wp")){
                WorldForm.showForm1((Player) sender);
            }
            return true;
        }
    }

}
