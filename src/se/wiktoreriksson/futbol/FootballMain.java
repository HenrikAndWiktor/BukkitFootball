package se.wiktoreriksson.futbol;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by Wiktor Eriksson on 2017-07-28.
 * @author Wiktor Eriksson
 * @see org.bukkit.plugin.java.JavaPlugin
 * @since 1.0.1
 */
public class FootballMain extends JavaPlugin{
    /**
     * The enable method
     */
    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new ServiceOnPlayerTouchAS(),this);
    }

    /**
     * @param p Cmd Executor
     * @return Football
     * @see #f(Player, double, double, double)
     */
    protected Entity f(Player p) {
        return f(p,p.getLocation().getX(),p.getLocation().getY()+10,p.getLocation().getZ());
    }

    /**
     * @param p Cmd executor
     * @param x Spawn location X
     * @param y Spawn location Y
     * @param z Spawn location Z
     * @return Football
     * @see #f(Player, double, double, double, String)
     */
    protected Entity f(Player p,double x,double y,double z) {
        return f(p, x, y, z,"ASFUTBAL");
    }

    /**
     * @param p Cmd executor
     * @param x Spawn location X
     * @param y Spawn location Y
     * @param z Spawn location Z
     * @param n Football entity name. To use as a miniature, set n to something other than "ASFUTBAL".
     * @return Football
     * @see #f(Location, String)
     */
    protected Entity f(Player p,double x, double y, double z,String n){
        return f(new Location(p.getWorld(),x,y,z),n);
    }

    /**
     * @param loc Location
     * @param s Football entity name. To use as a miniature, set n to something other than "ASFUTBAL".
     * @return Football
     */
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
