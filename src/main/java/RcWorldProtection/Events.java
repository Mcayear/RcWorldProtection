package RcWorldProtection;

import RcWorldProtection.Form.WorldForm;
import RcWorldProtection.World.World;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.*;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.*;
import cn.nukkit.event.entity.*;
import cn.nukkit.event.inventory.FurnaceSmeltEvent;
import cn.nukkit.event.inventory.InventoryPickupItemEvent;
import cn.nukkit.event.level.WeatherChangeEvent;
import cn.nukkit.event.player.*;
import cn.nukkit.form.response.FormResponseCustom;
import cn.nukkit.form.response.FormResponseSimple;
import cn.nukkit.item.*;
import cn.nukkit.level.Level;
import cn.nukkit.math.Vector3;
import cn.nukkit.utils.Config;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Events implements Listener {

    public static LinkedHashMap<Player,String> editWorld = new LinkedHashMap<>();

    public LinkedList<Integer> containers = new LinkedList<>(){{
        add(457);
        add(61);
        add(451);
        add(453);
        add(145);
        add(450);
        add(116);
        add(458);
        add(138);
        add(154);
        add(452);
        add(23);
        add(125);
    }};

    public Events(){}

    @EventHandler
    public void onPlace(BlockPlaceEvent event){
        Player player = event.getPlayer();
        Block block = event.getBlock();
        if(!Main.loadWorld.containsKey(player.level.getName())) return;
        World world = Main.loadWorld.get(player.level.getName());
        if(!world.isCanPlace() && !Handle.isWhiteList(player.getName()) && !Handle.isRule(block,world,"放置")){
            event.setCancelled();
            Handle.sendMessage(player,"title","放置");
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event){
        Player player = event.getPlayer();
        Block block = event.getBlock();
        if(!Main.loadWorld.containsKey(player.level.getName())) return;
        World world = Main.loadWorld.get(player.level.getName());
        if(!world.isCanBreak() && !Handle.isWhiteList(player.getName()) && !Handle.isRule(block,world,"破坏")){
            event.setCancelled();
            Handle.sendMessage(player,"title","破坏");
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event){
        Player player = event.getPlayer();
        if(!Main.loadWorld.containsKey(player.level.getName())) return;
        World world = Main.loadWorld.get(player.level.getName());
        if(!world.isCanMove() && !Handle.isWhiteList(player.getName())){
            event.setCancelled();
            Handle.sendMessage(player,"popup","移动");
        }
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event){
        Player player = event.getPlayer();
        if(!Main.loadWorld.containsKey(player.level.getName())) return;
        World world = Main.loadWorld.get(player.level.getName());
        if(!world.isCanTeleport() && !Handle.isWhiteList(player.getName())){
            event.setCancelled();
            Handle.sendMessage(player,"title","传送");
        }
    }

    @EventHandler
    public void onBed(PlayerBedEnterEvent event){
        Player player = event.getPlayer();
        if(!Main.loadWorld.containsKey(player.level.getName())) return;
        World world = Main.loadWorld.get(player.level.getName());
        if(!world.isCanSleep() && !Handle.isWhiteList(player.getName())){
            event.setCancelled();
            Handle.sendMessage(player,"title","上床");
        }
    }

    @EventHandler
    public void onPick(InventoryPickupItemEvent event){
        for(Player player : event.getViewers()){
            if(!Main.loadWorld.containsKey(player.level.getName())) return;
            World world = Main.loadWorld.get(player.level.getName());
            if(!world.isCanPick() && !Handle.isWhiteList(player.getName())){
                event.setCancelled();
                Handle.sendMessage(player,"popup","拾起");
            }
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event){
        Player player = event.getPlayer();
        if(!Main.loadWorld.containsKey(player.level.getName())) return;
        World world = Main.loadWorld.get(player.level.getName());
        if(!world.isCanDrop() && !Handle.isWhiteList(player.getName())){
            event.setCancelled();
            Handle.sendMessage(player,"title","丢弃");
        }
    }

    @EventHandler
    public void onFly(PlayerToggleFlightEvent event){
        Player player = event.getPlayer();
        if(!Main.loadWorld.containsKey(player.level.getName())) return;
        World world = Main.loadWorld.get(player.level.getName());
        if(event.isFlying() && (!world.isCanFly() && !Handle.isWhiteList(player.getName()))){
            event.setCancelled();
            player.setPosition(player.getPosition());
            player.setMotion(new Vector3(0,-1,0));
            player.fall(player.getMotion().getFloorY()+1);
            Handle.sendMessage(player,"title","飞行");
        }
    }

    @EventHandler
    public void onBlast(EntityExplodeEvent event){
        Entity entity = event.getEntity();
        if(!Main.loadWorld.containsKey(entity.level.getName())) return;
        World world = Main.loadWorld.get(entity.level.getName());
        if(world.isCanBlast() && !world.isCanBreakBlock()){
            event.setBlockList(new ArrayList<>());
        }else if(!world.isCanBlast()){
            event.setCancelled();
        }
    }

    @EventHandler
    public void onFlow(LiquidFlowEvent event){
        Block block = event.getBlock();
        if(!Main.loadWorld.containsKey(block.level.getName())) return;
        World world = Main.loadWorld.get(block.level.getName());
        if(!world.isCanFlow()){
            event.setCancelled();
        }
    }

    @EventHandler
    public void onForm(BlockFormEvent event){
        Block block = event.getBlock();
        if(!Main.loadWorld.containsKey(block.level.getName())) return;
        World world = Main.loadWorld.get(block.level.getName());
        if(!world.isCanCreate()){
            event.setCancelled();
        }
    }

    @EventHandler
    public void onDie(LeavesDecayEvent event){
        Block block = event.getBlock();
        if(!Main.loadWorld.containsKey(block.level.getName())) return;
        World world = Main.loadWorld.get(block.level.getName());
        if(!world.isCanDie()){
            event.setCancelled();
        }
    }

    @EventHandler
    public void onGrow(BlockGrowEvent event){
        Block block = event.getBlock();
        if(!Main.loadWorld.containsKey(block.level.getName())) return;
        World world = Main.loadWorld.get(block.level.getName());
        if(!world.isCanGrow()){
            event.setCancelled();
        }
    }

    @EventHandler
    public void onDamaged(EntityDamageEvent event){
        Entity entity = event.getEntity();
        if(!Main.loadWorld.containsKey(entity.level.getName())) return;
        World world = Main.loadWorld.get(entity.level.getName());
        if(!world.isCanDamaged() && entity instanceof Player){
            event.setCancelled();
        }
    }

    @EventHandler
    public void onDamaged(EntityDamageByEntityEvent event){
        Entity entity = event.getEntity();
        Entity damager = event.getDamager();
        if(!Main.loadWorld.containsKey(entity.level.getName())) return;
        World world = Main.loadWorld.get(entity.level.getName());
        if(!world.isCanPvp() && entity instanceof Player && damager instanceof Player){
            event.setCancelled();
            Handle.sendMessage((Player) damager,"title","允许PVP");
        }
    }

    @EventHandler
    public void onExp(FurnaceSmeltEvent event){
        Block block = event.getBlock();
        if(!Main.loadWorld.containsKey(block.level.getName())) return;
        World world = Main.loadWorld.get(block.level.getName());
        if(!world.isCanExp()){
            event.setXp(0);
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event){
        Player player = event.getEntity();
        if(!Main.loadWorld.containsKey(player.level.getName())) return;
        World world = Main.loadWorld.get(player.level.getName());
        if(!world.isDeathExp()){
            event.setKeepExperience(true);
        }
        if(!world.isDeathItem()){
            event.setKeepInventory(true);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event){
        Player player = event.getPlayer();
        Block block = event.getBlock();
        Item item = event.getItem();
        if(block != null && player.isSneaking() && player.isOp()){
            player.sendMessage(block.x + ":" + block.y + ":" + block.z);
        }
        if(!Main.loadWorld.containsKey(player.level.getName())) return;
        World world = Main.loadWorld.get(player.level.getName());
        if(!world.isCanInteract()){
            if((block != null && !Handle.isRule(block,world,"交互")) || (item != null && !Handle.isRule(item,world))){
                event.setCancelled();
                Handle.sendMessage(player,"title","交互");
            }
            return;
        }
        if(!world.isCanClick() && !Handle.isWhiteList(player.getName()) && event.getAction() == PlayerInteractEvent.Action.PHYSICAL){
            event.setCancelled();
            Handle.sendMessage(player,"title","物理触碰");
        }
        if(block != null){
            if (world.isCanPlough() && block.getId() == Block.FARMLAND && event.getAction() == PlayerInteractEvent.Action.PHYSICAL) {
                event.setCancelled();
                Handle.sendMessage(player,"title","耕地保护");
            }
            if(!world.isCanSign() && block instanceof BlockSignBase && !Handle.isWhiteList(player.getName())){
                event.setCancelled();
                Handle.sendMessage(player,"title","冻结告示牌");
            }
            if(!world.isCanChest() && block instanceof BlockChest && !Handle.isWhiteList(player.getName())){
                event.setCancelled();
                Handle.sendMessage(player,"title","箱子");
            }
            if(!world.isCanEnderChest() && block instanceof BlockEnderChest && !Handle.isWhiteList(player.getName())){
                event.setCancelled();
                Handle.sendMessage(player,"title","末影箱");
            }
            if(!world.isCanContainer() && containers.contains(block.getId()) && !Handle.isWhiteList(player.getName())){
                event.setCancelled();
                Handle.sendMessage(player,"title","容器");
            }
            if(world.isCanFrame() && !Handle.isWhiteList(player.getName()) && (block.getId() == 389 || block.getId() == -339)){
                event.setCancelled();
                Handle.sendMessage(player,"title","展示框保护");
            }
        }
        if(item != null){
            if(item instanceof ItemAxeWood || item instanceof ItemAxeDiamond || item instanceof ItemAxeGold || item instanceof ItemAxeIron || item instanceof ItemAxeNetherite || item instanceof ItemAxeStone){
                if (!world.isCanBreak() && !Handle.isWhiteList(player.getName())) {
                    event.setCancelled();
                }
            }
            if(item instanceof ItemFishingRod){
                if (!world.isCanFish() && !Handle.isWhiteList(player.getName())) {
                    event.setCancelled();
                    Handle.sendMessage(player,"title","钓鱼");
                }
            }
            if(item.getId() == 259 || item.getId() == 385){
                if (!world.isCanBurn() && !Handle.isWhiteList(player.getName())) {
                    event.setCancelled();
                    Handle.sendMessage(player,"title","燃烧");
                }
            }
        }
    }

    @EventHandler
    public void onEntityInteract(EntityInteractEvent event){
        Entity entity = event.getEntity();
        Block block = event.getBlock();
        if(!Main.loadWorld.containsKey(entity.level.getName())) return;
        World world = Main.loadWorld.get(entity.level.getName());
        if(world.isCanPlough() && block.getId() == Block.FARMLAND){
            event.setCancelled();
        }
    }

    @EventHandler
    public void onSign(SignChangeEvent event){
        Player player = event.getPlayer();
        if(!Main.loadWorld.containsKey(player.level.getName())) return;
        World world = Main.loadWorld.get(player.level.getName());
        if(world.isCanSign() && !Handle.isWhiteList(player.getName())){
            event.setCancelled();
            Handle.sendMessage(player,"title","冻结告示牌");
        }
    }

    @EventHandler
    public void ItemFrameDropItemEvent(ItemFrameUseEvent event) {
        Player player = event.getPlayer();
        if(player != null){
            if (!Main.loadWorld.containsKey(player.level.getName())) return;
            World world = Main.loadWorld.get(player.level.getName());
            if(world.isCanFrame() && !Handle.isWhiteList(player.getName())){
                event.setCancelled();
                Handle.sendMessage(player,"title","展示框保护");
            }
        }
    }

    @EventHandler
    public void onBurn(BlockBurnEvent event) {
        Block block = event.getBlock();
        if (!Main.loadWorld.containsKey(block.level.getName())) return;
        World world = Main.loadWorld.get(block.level.getName());
        if (!world.isCanBurn()) {
            event.setCancelled();
        }
    }

    @EventHandler
    public void onWeather(WeatherChangeEvent event) {
        Level level = event.getLevel();
        if (!Main.loadWorld.containsKey(level.getName())) return;
        World world = Main.loadWorld.get(level.getName());
        if (!world.isCanWeather()) {
            event.setCancelled();
        }
    }

    @EventHandler
    public void onRespond(PlayerFormRespondedEvent event){
        int id = event.getFormID();
        if(id == 99950 && event.wasClosed()){
            return;
        }
        Player player = event.getPlayer();
        switch (id) {
            case 99950 -> {
                FormResponseSimple response1 = (FormResponseSimple) event.getResponse();
                if (response1 == null) return;
                switch (response1.getClickedButtonId()) {
                    case 0 -> WorldForm.showForm2(player);
                    case 1 -> WorldForm.showForm7(player);
                    case 2 -> {
                        for (String name : Handle.getDefaultFiles("Worlds")) {
                            World world = null;
                            try {
                                world = World.loadWorld(name, new Config(Main.instance.getDataFolder() + "/Worlds/" + name + ".yml", Config.YAML));
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                            if (world != null) {
                                Main.loadWorld.put(name, world);
                            }
                        }
                        WorldForm.showForm1(player);
                    }
                }
            }
            case 99951 -> {
                FormResponseSimple response2 = (FormResponseSimple) event.getResponse();
                if (response2 == null) {
                    WorldForm.showForm1(player);
                    return;
                }
                switch (response2.getClickedButtonId()) {
                    case 0 -> WorldForm.showForm3(player);
                    case 1 -> WorldForm.showForm4(player);
                    case 2 -> WorldForm.showForm5(player);
                }
            }
            case 99952 -> {
                FormResponseSimple response3 = (FormResponseSimple) event.getResponse();
                if (response3 == null) {
                    WorldForm.showForm2(player);
                    return;
                }
                World.addWorldConfig(response3.getClickedButton().getText());
                WorldForm.showForm2(player);
            }
            case 99953 -> {
                FormResponseSimple response4 = (FormResponseSimple) event.getResponse();
                if (response4 == null) {
                    WorldForm.showForm2(player);
                    return;
                }
                World.delWorldConfig(response4.getClickedButton().getText());
                WorldForm.showForm2(player);
            }
            case 99954 -> {
                FormResponseSimple response5 = (FormResponseSimple) event.getResponse();
                if (response5 == null) {
                    WorldForm.showForm2(player);
                    return;
                }
                WorldForm.showForm6(player, Main.loadWorld.get(response5.getClickedButton().getText()));
            }
            case 99955 -> {
                FormResponseCustom response6 = (FormResponseCustom) event.getResponse();
                if (response6 == null) {
                    WorldForm.showForm5(player);
                    return;
                }
                Config config = World.getWorldConfig(Events.editWorld.get(player));
                config.set("破坏", response6.getToggleResponse(0));
                config.set("放置", response6.getToggleResponse(1));
                config.set("交互", response6.getToggleResponse(2));
                config.set("移动", response6.getToggleResponse(3));
                config.set("传送", response6.getToggleResponse(4));
                config.set("上床", response6.getToggleResponse(5));
                config.set("拾起", response6.getToggleResponse(6));
                config.set("丢弃", response6.getToggleResponse(7));
                config.set("飞行", response6.getToggleResponse(8));
                config.set("实体爆炸", response6.getToggleResponse(9));
                config.set("实体爆炸破坏", response6.getToggleResponse(10));
                config.set("液体流动", response6.getToggleResponse(11));
                config.set("方块自然生成", response6.getToggleResponse(12));
                config.set("树叶凋零", response6.getToggleResponse(13));
                config.set("植物生长", response6.getToggleResponse(14));
                config.set("玩家受伤", response6.getToggleResponse(15));
                config.set("允许PVP", response6.getToggleResponse(16));
                config.set("经验熔炉", response6.getToggleResponse(17));
                config.set("死亡掉落经验", response6.getToggleResponse(18));
                config.set("死亡掉落物品", response6.getToggleResponse(19));
                config.set("耕地保护", response6.getToggleResponse(20));
                config.set("冻结告示牌", response6.getToggleResponse(21));
                config.set("展示框保护", response6.getToggleResponse(22));
                config.set("箱子", response6.getToggleResponse(23));
                config.set("末影箱", response6.getToggleResponse(24));
                config.set("容器", response6.getToggleResponse(25));
                config.set("钓鱼", response6.getToggleResponse(26));
                config.set("燃烧", response6.getToggleResponse(27));
                config.set("物理触碰", response6.getToggleResponse(28));
                config.set("时间变化", response6.getToggleResponse(29));
                config.set("天气变化", response6.getToggleResponse(30));
                config.save();
                WorldForm.showForm5(player);
            }
            case 99956 -> {
                FormResponseSimple response7 = (FormResponseSimple) event.getResponse();
                if (response7 == null) {
                    WorldForm.showForm1(player);
                    return;
                }
                switch (response7.getClickedButtonId()) {
                    case 0 -> WorldForm.showForm8(player);
                    case 1 -> WorldForm.showForm9(player);
                }
            }
            case 99957 -> {
                FormResponseCustom response8 = (FormResponseCustom) event.getResponse();
                if (response8 == null) {
                    WorldForm.showForm7(player);
                    return;
                }
                Handle.addWhiteList(response8.getInputResponse(0));
                WorldForm.showForm7(player);
            }
            case 99958 -> {
                FormResponseSimple response9 = (FormResponseSimple) event.getResponse();
                if (response9 == null) {
                    WorldForm.showForm7(player);
                    return;
                }
                Handle.removeWhiteList(response9.getClickedButton().getText());
                WorldForm.showForm7(player);
            }
        }
    }

}
