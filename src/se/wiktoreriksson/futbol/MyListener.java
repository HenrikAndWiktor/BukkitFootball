package se.wiktoreriksson.futbol;

import net.minecraft.server.v1_12_R1.NBTTagCompound;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftArmorStand;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.List;

/**
 * Listener kick
 */
public class MyListener implements Listener {
    public void onPTAS(PlayerMoveEvent pme){
        List<Entity> ce=pme.getPlayer().getNearbyEntities(1,1,1);
        if((!ce.isEmpty())&&(ce.get(0) instanceof ArmorStand)&&(ce.get(0).getName().equals("ASFUTBAL"))&&pme.getPlayer().isSprinting()){
            CraftArmorStand as=(CraftArmorStand)ce.get(0);
            NBTTagCompound compound = new NBTTagCompound();
            net.minecraft.server.v1_12_R1.Entity e = ((CraftEntity) as).getHandle();
            net.minecraft.server.v1_12_R1.Entity p = ((CraftEntity)pme.getPlayer()).getHandle();
            p.d(compound);
            e.motY=1.0;
            e.motX=p.motX;
            e.motZ=p.motZ;
            Runnable r2 = ()->{
                e.motX=0.0;
                e.motY=0.0;
                e.motZ=0.0;
            };
            Runnable r = ()->{
                e.motY=-1.0;
                new java.util.Timer().schedule(new java.util.TimerTask() {
                    @Override
                    public void run() {
                        r2.run();
                    }
                }, 2000);
            };
            new java.util.Timer().schedule(new java.util.TimerTask() {
                                    @Override
                                    public void run() {
                                        r.run();
                                    }
                                }, 2000);
        }
    }
}
