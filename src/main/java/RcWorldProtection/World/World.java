package RcWorldProtection.World;

import cn.nukkit.Server;
import cn.nukkit.form.element.ElementToggle;
import cn.nukkit.form.window.FormWindowCustom;
import cn.nukkit.level.GameRule;
import cn.nukkit.level.Level;
import cn.nukkit.utils.Config;
import RcWorldProtection.Main;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.util.ArrayList;

@Getter
@Setter
public class World {

    private String name;

    private boolean canBreak;

    private ArrayList<String> breakBlock = new ArrayList<>();

    private boolean canPlace;

    private ArrayList<String> placeBlock = new ArrayList<>();

    private boolean canInteract;

    private ArrayList<String> interactItem = new ArrayList<>();

    private ArrayList<String> interactBlock = new ArrayList<>();

    private boolean canMove;

    private boolean canTeleport;

    private boolean canSleep;

    private boolean canPick;

    private boolean canDrop;

    private boolean canFly;

    private boolean canBlast;

    private boolean canBreakBlock;

    private boolean canFlow;

    private boolean canCreate;

    private boolean canGrow;

    private boolean canDie;

    private boolean canDamaged;

    private boolean canPvp;

    private boolean canExp;

    private boolean deathItem;

    private boolean deathExp;

    private boolean canPlough;

    private boolean canSign;

    private boolean canChest;

    private boolean canEnderChest;

    private boolean canContainer;

    private boolean canBook;

    private boolean canFish;

    private boolean canClick;

    private boolean canTime;

    private boolean canWeather;

    private boolean canFrame;

    private boolean canBurn;

    private String space;

    private ArrayList<String> barrier = new ArrayList<>();

    private Config config;

    public World(String name,Config config){
        this.name = name;
        this.config = config;
    }

    public static World loadWorld(String name, Config config){
        try{
            World world = new World(name,config);

            world.setCanBreak(config.getBoolean("破坏"));
            world.setCanPlace(config.getBoolean("放置"));
            world.setCanInteract(config.getBoolean("交互"));
            world.setCanMove(config.getBoolean("移动"));
            world.setCanTeleport(config.getBoolean("传送"));
            world.setCanSleep(config.getBoolean("上床"));
            world.setCanPick(config.getBoolean("拾起"));
            world.setCanDrop(config.getBoolean("丢弃"));
            world.setCanFly(config.getBoolean("飞行"));
            world.setCanBlast(config.getBoolean("实体爆炸"));
            world.setCanBreakBlock(config.getBoolean("实体爆炸破坏"));
            world.setCanFlow(config.getBoolean("液体流动"));
            world.setCanCreate(config.getBoolean("方块自然生成"));
            world.setCanDie(config.getBoolean("树叶凋零"));
            world.setCanGrow(config.getBoolean("植物生长"));
            world.setCanDamaged(config.getBoolean("玩家受伤"));
            world.setCanPvp(config.getBoolean("允许PVP"));
            world.setCanExp(config.getBoolean("经验熔炉"));
            world.setDeathExp(config.getBoolean("死亡掉落经验"));
            world.setDeathItem(config.getBoolean("死亡掉落物品"));
            world.setCanPlough(config.getBoolean("耕地保护"));
            world.setCanSign(config.getBoolean("冻结告示牌"));
            world.setCanFrame(config.getBoolean("展示框保护"));
            world.setCanTime(config.getBoolean("时间变化"));
            world.setCanChest(config.getBoolean("箱子"));
            world.setCanEnderChest(config.getBoolean("末影箱"));
            world.setCanContainer(config.getBoolean("容器"));
            world.setCanBook(config.getBoolean("书架"));
            world.setCanFish(config.getBoolean("钓鱼"));
            world.setCanBurn(config.getBoolean("燃烧"));
            world.setCanClick(config.getBoolean("物理触碰"));
            world.setCanWeather(config.getBoolean("天气变化"));
            world.setSpace(config.getString("世界虚空"));

            ArrayList<String> list1 = new ArrayList<>();
            for(String block : config.getStringList("反破坏规则方块")){
                list1.add(block);
            }
            world.setBreakBlock(list1);

            ArrayList<String> list2 = new ArrayList<>();
            for(String block : config.getStringList("反放置规则方块")){
                list2.add(block);
            }
            world.setPlaceBlock(list2);

            ArrayList<String> list3 = new ArrayList<>();
            for(String block : config.getStringList("反交互规则方块")){
                list3.add(block);
            }
            world.setInteractBlock(list3);

            ArrayList<String> list4 = new ArrayList<>();
            for(String item : config.getStringList("反交互规则物品")){
                list4.add(item);
            }
            world.setInteractItem(list4);

            ArrayList<String> list6 = new ArrayList<>();
            for(String barrier : config.getStringList("世界边界")){
                list6.add(barrier);
            }
            world.setBarrier(list6);

            if(!world.isCanTime()){
                Level level = Server.getInstance().getLevelByName(world.getName());
                level.getGameRules().setGameRule(GameRule.DO_DAYLIGHT_CYCLE,false);
            }

            return world;
        }catch(Exception e){
            Main.instance.getLogger().error("加载世界"+name+"配置文件失败");
            return null;
        }

    }

    public static Config getWorldConfig(String name){
        Config config = new Config(Main.instance.getDataFolder()+"/Worlds/"+name+".yml");
        return config;
    }

    public static void addWorldConfig(String name){
        Main.instance.saveResource("World.yml","/Worlds/" + name + ".yml",false);
        Config config = new Config(Main.instance.getDataFolder()+"/Worlds/"+name+".yml");
        World world = World.loadWorld(name,config);
        Main.loadWorld.put(name,world);
    }

    public static void delWorldConfig(String name){
        File file = new File(Main.instance.getDataFolder()+"/Worlds","/"+name+".yml");
        file.delete();
        Main.loadWorld.remove(name);
    }

    public FormWindowCustom getForm(){
        FormWindowCustom form = new FormWindowCustom(this.getName() + "世界配置");
        form.addElement(new ElementToggle("破坏",this.isCanBreak()));
        form.addElement(new ElementToggle("放置",this.isCanPlace()));
        form.addElement(new ElementToggle("交互",this.isCanInteract()));
        form.addElement(new ElementToggle("移动",this.isCanMove()));
        form.addElement(new ElementToggle("传送",this.isCanTeleport()));
        form.addElement(new ElementToggle("上床",this.isCanSleep()));
        form.addElement(new ElementToggle("拾起",this.isCanPick()));
        form.addElement(new ElementToggle("丢弃",this.isCanDrop()));
        form.addElement(new ElementToggle("飞行",this.isCanFly()));
        form.addElement(new ElementToggle("实体爆炸",this.isCanBlast()));
        form.addElement(new ElementToggle("实体爆炸破坏",this.isCanBreakBlock()));
        form.addElement(new ElementToggle("液体流动",this.isCanFlow()));
        form.addElement(new ElementToggle("方块自然生成",this.isCanCreate()));
        form.addElement(new ElementToggle("树叶凋零",this.isCanDie()));
        form.addElement(new ElementToggle("植物生长",this.isCanGrow()));
        form.addElement(new ElementToggle("玩家受伤",this.isCanDamaged()));
        form.addElement(new ElementToggle("允许PVP",this.isCanPvp()));
        form.addElement(new ElementToggle("经验熔炉",this.isCanExp()));
        form.addElement(new ElementToggle("死亡掉落经验",this.isDeathExp()));
        form.addElement(new ElementToggle("死亡掉落物品",this.isDeathItem()));
        form.addElement(new ElementToggle("耕地保护",this.isCanPlough()));
        form.addElement(new ElementToggle("冻结告示牌",this.isCanSign()));
        form.addElement(new ElementToggle("展示框保护",this.isCanFrame()));
        form.addElement(new ElementToggle("箱子",this.isCanChest()));
        form.addElement(new ElementToggle("末影箱",this.isCanEnderChest()));
        form.addElement(new ElementToggle("容器",this.isCanContainer()));
        form.addElement(new ElementToggle("书架",this.isCanBook()));
        form.addElement(new ElementToggle("钓鱼",this.isCanFish()));
        form.addElement(new ElementToggle("燃烧",this.isCanBurn()));
        form.addElement(new ElementToggle("物理触碰",this.isCanClick()));
        form.addElement(new ElementToggle("时间变化",this.isCanTime()));
        form.addElement(new ElementToggle("天气变化",this.isCanWeather()));

        return form;
    }

}
