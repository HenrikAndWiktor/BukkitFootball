package se.wiktoreriksson.futbol;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by wiktoreriksson on 2017-08-02.
 */
public class BGASG extends Thread {
    public void run() {
        Scoreboard s = Bukkit.getScoreboardManager().getNewScoreboard();
        s.registerNewObjective("GOALB","dummy");
        s.registerNewObjective("GOALR","dummy");
        while (Bukkit.getServer().getPluginManager().getPlugin("Football").isEnabled()) {
            List<Entity> ents=new ArrayList<>();
            List<ArmorStand> futs=new ArrayList<>();
            for (World w:Bukkit.getServer().getWorlds())
                ents.addAll(w.getEntities().stream().collect(Collectors.toList()));
            futs.addAll(ents.stream().filter(
                    e -> e instanceof ArmorStand && e.getName().equals("ASFUTBAL")
            ).map(
                    e -> (ArmorStand) e
            ).collect(Collectors.toList()));
            for (ArmorStand a : futs) {
                Location loc = a.getLocation();
                loc.setY(loc.getBlockY() - 2);
                Material m = a.getWorld().getBlockAt(loc).getType();
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
    }
}
