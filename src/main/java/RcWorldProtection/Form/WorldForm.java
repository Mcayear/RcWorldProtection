package RcWorldProtection.Form;

import RcWorldProtection.Events;
import RcWorldProtection.Handle;
import RcWorldProtection.World.World;
import cn.nukkit.Player;
import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.element.ElementInput;
import cn.nukkit.form.element.ElementLabel;
import cn.nukkit.form.window.FormWindowCustom;
import cn.nukkit.form.window.FormWindowSimple;

import java.util.ArrayList;

public class WorldForm {

    public static void showForm1(Player player){
        FormWindowSimple form = new FormWindowSimple("世界管理系统","");
        form.addButton(new ElementButton("管理世界"));
        form.addButton(new ElementButton("管理权限"));
        form.addButton(new ElementButton("重载配置"));
        player.showFormWindow(form,99950);
    }

    public static void showForm2(Player player){
        FormWindowSimple form = new FormWindowSimple("管理世界","");
        form.addButton(new ElementButton("添加世界"));
        form.addButton(new ElementButton("删除世界"));
        form.addButton(new ElementButton("配置世界"));
        player.showFormWindow(form,99951);
    }

    public static void showForm3(Player player){
        FormWindowSimple form = new FormWindowSimple("添加世界","");
        ArrayList<String> list = Handle.getAllUnLoadWorlds();
        if(list.isEmpty()){
            form.setContent("当前无世界可以创建配置文件");
        }else{
            for(String name : list){
                form.addButton(new ElementButton(name));
            }
        }
        player.showFormWindow(form,99952);
    }

    public static void showForm4(Player player){
        FormWindowSimple form = new FormWindowSimple("删除世界","");
        ArrayList<String> list = Handle.getAllLoadWorlds();
        if(list.isEmpty()){
            form.setContent("当前无世界可以删除");
        }else{
            for(String name : list){
                form.addButton(new ElementButton(name));
            }
        }
        player.showFormWindow(form,99953);
    }

    public static void showForm5(Player player){
        FormWindowSimple form = new FormWindowSimple("配置世界","");
        ArrayList<String> list = Handle.getAllLoadWorlds();
        if(list.isEmpty()){
            form.setContent("当前无世界可以配置");
        }else{
            for(String name : list){
                form.addButton(new ElementButton(name));
            }
        }
        player.showFormWindow(form,99954);
    }

    public static void showForm6(Player player, World world){
        FormWindowCustom form = world.getForm();
        Events.editWorld.put(player,world.getName());
        player.showFormWindow(form,99955);
    }

    public static void showForm7(Player player){
        FormWindowSimple form = new FormWindowSimple("管理权限","");
        form.addButton(new ElementButton("添加权限"));
        form.addButton(new ElementButton("删除权限"));
        player.showFormWindow(form,99956);
    }

    public static void showForm8(Player player){
        FormWindowCustom form = new FormWindowCustom("添加权限");
        form.addElement(new ElementInput("请输入要添加权限的玩家名称"));
        player.showFormWindow(form,99957);
    }

    public static void showForm9(Player player){
        FormWindowSimple form = new FormWindowSimple("删除权限","");
        ArrayList<String> list = Handle.getWhiteList();
        if(list.isEmpty()){
            form.setContent("当前无管理员可以删除");
        }else{
            for(String name : list){
                form.addButton(new ElementButton(name));
            }
        }
        player.showFormWindow(form,99958);
    }


}
