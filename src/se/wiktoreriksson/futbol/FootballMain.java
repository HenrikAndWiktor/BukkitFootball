package se.wiktoreriksson.futbol;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by wiktoreriksson on 2017-07-28.
 */
public class FootballMain extends JavaPlugin{
    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new ServiceOnPlayerTouchAS(),this);
    }

    protected Entity f(Player p) {
        return f(p,p.getLocation().getX(),p.getLocation().getY()+10,p.getLocation().getZ());
    }
    protected Entity f(Player p,double x,double y,double z) {
        return f(p, x, y, z,"ASFUTBAL");
    }
    protected Entity f(Player p,double x, double y, double z,String s){
        return f(new Location(p.getWorld(),x,y,z),s);
    }
    private Entity f(Location loc,String s) {
        ArmorStand as = loc.getWorld().spawn(loc, ArmorStand.class);
        as.setCustomName(s);
        as.setVisible(false);
        as.setInvulnerable(true);
        getServer().dispatchCommand(getServer().getConsoleSender(),"/replaceitem entity @e[type=armor_stand,name="+s+"] slot.armor.feet skull 1 3 {SkullOwner:{Id:\"b74e58ec-4460-44b9-b0ca-2a1b9f331523\",Properties:{textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGU0YTcwYjdiYmNkN2E4YzMyMmQ1MjI1MjA0OTFhMjdlYTZiODNkNjBlY2Y5NjFkMmI0ZWZiYmY5ZjYwNWQifX19\"}]}}}");
        return as;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        switch (command.getName()) {
            case "football":
                if (sender instanceof Player) {
                    Player p=(Player)sender;
                    ArmorStand as = (ArmorStand)f(p);
                    Location asl = as.getLocation();
                    p.sendMessage(as.getName()+" is your armor stand football summoned at x: "+asl.getX()+", y: "+asl.getY()+", z: "+asl.getZ());
                }
        }
        return true;
    }
}
