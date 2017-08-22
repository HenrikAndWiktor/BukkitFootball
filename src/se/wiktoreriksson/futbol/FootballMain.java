package se.wiktoreriksson.futbol;

import net.minecraft.server.v1_12_R1.*;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Wiktor Eriksson on 2017-07-28.
 * @author Wiktor Eriksson
 * @see org.bukkit.plugin.java.JavaPlugin
 * @since 1.0.1
 */
public class FootballMain extends JavaPlugin{
    /*Bg goalcheck*/ Thread t = null;
    /**
     * The enable method
     */
    @Override
    public void onEnable() {
        t=new Thread(()->{
                org.bukkit.scoreboard.Scoreboard s = Bukkit.getScoreboardManager().getNewScoreboard();
                s.registerNewObjective("GOALB","dummy");
                s.registerNewObjective("GOALR","dummy");
                while (Bukkit.getServer().getPluginManager().getPlugin("Football").isEnabled()) {
                    List<Entity> ents=new ArrayList<>();
                    List<ArmorStand> futs=new ArrayList<>();
                    for (org.bukkit.World w : Bukkit.getServer().getWorlds()) {
                        ents.addAll(w.getEntities().stream().collect(Collectors.toList()));
                    }
                    futs.addAll(
                            ents.stream().filter(
                                    (Entity e) -> e instanceof ArmorStand && e.getName().equals("ASFUTBAL")
                            ).map(
                                    (Entity e) -> (ArmorStand) e
                            ).collect(
                                    Collectors.toList()
                            )
                    );
                    for (ArmorStand a : futs) {
                        Location loc = a.getLocation();
                        loc.setY(loc.getBlockY() - 2);
                        org.bukkit.Material m = a.getWorld().getBlockAt(loc).getType();
                        boolean goalr = false;
                        boolean goalb = false;
                        switch (m) {
                            case BARRIER:
                                goalb = true;
                            case STRUCTURE_VOID:
                                goalr = true;
                            default:
                        }
                        if (goalr) {
                            List<Player> lp = new ArrayList<>(Bukkit.getOnlinePlayers());
                            lp.stream().filter(p -> s.getTeam("RED").hasPlayer(p)).forEach(p -> s.getObjective("GOALR").getScore(p).setScore(
                                    s.getObjective("GOALR").getScore(p).getScore() + 1
                            ));
                        }
                        if (goalb) {
                            List<Player> lp = new ArrayList<>(Bukkit.getOnlinePlayers());
                            lp.stream().filter(p -> s.getTeam("BLUE").hasPlayer(p)).forEach(p -> s.getObjective("GOALB").getScore(p).setScore(
                                    s.getObjective("GOALB").getScore(p).getScore() + 1
                            ));
                        }
                    }
                }
        });
        getServer().getPluginManager().registerEvents(new MyListener(),this);
        t.start();
    }

    @Override
    public void onDisable() {
        t.interrupt();
    }

    /**
     * @param p Cmd Executor
     * @return Football
     * @see #f(Player, double, double, double)
     * @see #f(Player, double, double, double, String)
     * @see #f(Location, String)
     */
    public Entity f(Player p) {
        return f(p,p.getLocation().getX(),p.getLocation().getY()+10,p.getLocation().getZ());
    }

    /**
     * @param p Cmd executor
     * @param x Spawn location X
     * @param y Spawn location Y
     * @param z Spawn location Z
     * @return Football
     * @see #f(Player, double, double, double, String)
     * @see #f(Location, String)
     */
    protected Entity f(Player p,double x,double y,double z) {
        return f(p, x, y, z,"ASFUTBAL");
    }

    /**
     * @param p Cmd executor
     * @param x Spawn location X
     * @param y Spawn location Y
     * @param z Spawn location Z
     * @param n Football entity name. To use eas a miniature, set n to something other than "ASFUTBAL".
     * @return Football
     * @see #f(Location, String)
     */
    protected Entity f(Player p,double x, double y, double z,String n){
        return f(new Location(p.getWorld(),x,y,z),n);
    }

    /**
     * @param loc Location
     * @param s Football entity name. To use eas a miniature, set n to something other than "ASFUTBAL".
     * @return Football
     */
    private Entity f(Location loc,String s) {
        NBTTagCompound nbtmain = new NBTTagCompound();
        NBTTagCompound nbt1 = new NBTTagCompound();
        NBTTagCompound nbt2 = new NBTTagCompound();
        NBTTagList nbt3 = new NBTTagList();
        NBTTagCompound nbt4 = new NBTTagCompound();
        NBTTagCompound nbt5 = new NBTTagCompound();
        nbt4.set("Value",new NBTTagString("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGU0YTcwYjdiYmNkN2E4YzMyMmQ1MjI1MjA0OTFhMjdlYTZiODNkNjBlY2Y5NjFkMmI0ZWZiYmY5ZjYwNWQifX19"));
        nbt1.set("id", new NBTTagString("skull"));
        nbt1.set("Count", new NBTTagInt(1));
        nbt2.set("id", new NBTTagString("b74e58ec-4460-44b9-b0ca-2a1b9f331523"));
        nbt3.add(nbt4);
        nbt5.set("textures",nbt3);
        nbt2.set("Properties",nbt5);
        nbtmain.set("Item", nbt1);
        nbtmain.set("SkullOwner",nbt2);
        ArmorStand as = loc.getWorld().spawn(loc,ArmorStand.class);
        as.setBoots(
                new ItemStack(
                        CraftItemStack.asBukkitCopy(
                                new net.minecraft.server.v1_12_R1.ItemStack(
                                        nbtmain
                                )
                        )
                )
        );
        as.setCustomName(s);
        as.setVisible(false);
        as.setInvulnerable(true);
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
