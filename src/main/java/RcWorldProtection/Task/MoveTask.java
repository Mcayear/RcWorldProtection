package RcWorldProtection.Task;

import RcWorldProtection.Handle;
import RcWorldProtection.Main;
import RcWorldProtection.World.World;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.level.Position;
import cn.nukkit.scheduler.PluginTask;

public class MoveTask extends PluginTask {

    public MoveTask(Main main){
        super(main);
    }

    @Override
    public void onRun(int i){
        if(Server.getInstance().getOnlinePlayers().isEmpty()) return;
        for(Player player : Server.getInstance().getOnlinePlayers().values()){
            if(!Main.loadWorld.containsKey(player.level.getName())) return;
            World world = Main.loadWorld.get(player.level.getName());
            if(!world.getSpace().equals("")){
                int y = Integer.parseInt(world.getSpace().split(":")[0]);
                int type = Integer.parseInt(world.getSpace().split(":")[1]);
                Position pos = null;
                if(type == 3){
                    pos = new Position(Integer.parseInt(world.getSpace().split(":")[2]),Integer.parseInt(world.getSpace().split(":")[3]),Integer.parseInt(world.getSpace().split(":")[4]),Server.getInstance().getLevelByName(world.getName()));
                }
                if(player.y <= y){
                    Handle.sendMessage(player,"title","世界虚空");
                    switch(type){
                        case 1 -> player.teleport(player.getSpawn());
                        case 2 -> {
                            if(player.isSurvival()){
                                player.teleport(Server.getInstance().getLevelByName(world.getName()).getSpawnLocation());
                                player.kill();
                                player.setHealth(0);
                            }
                        }
                        case 3 -> player.teleport(pos);
                    }
                }
            }
            if(!world.getBarrier().isEmpty()){
                int minX = Integer.parseInt(world.getBarrier().get(0).split(":")[0]);
                int maxX = Integer.parseInt(world.getBarrier().get(0).split(":")[1]);
                int minZ = Integer.parseInt(world.getBarrier().get(1).split(":")[0]);
                int maxZ = Integer.parseInt(world.getBarrier().get(1).split(":")[1]);
                if(player.x < minX || player.x >maxX || player.z < minZ || player.z > maxZ){
                    Handle.sendMessage(player,"title","世界边界");
                    player.teleport(Server.getInstance().getLevelByName(world.getName()).getSpawnLocation());
                }
            }
        }
    }

}
