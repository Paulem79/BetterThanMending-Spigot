package io.github.paulem.btm.managers;

import com.github.fierioziy.particlenativeapi.api.ParticleNativeAPI;
import com.github.fierioziy.particlenativeapi.api.utils.ParticleException;
import com.github.fierioziy.particlenativeapi.core.ParticleNativeCore;
import io.github.paulem.btm.BTM;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class EffectManager {
    private final FileConfiguration config;
    private ParticleNativeAPI api;

    public EffectManager(BTM plugin){
        this.config = plugin.getConfig();

        try {
            this.api = ParticleNativeCore.loadAPI(plugin);
        } catch (ParticleException e) {// optional runtime exception catch
            this.api = null;
        }
    }

    public void summonCircle(Player player, int size) {
        Location location = player.getEyeLocation()
                .add(
                        config.getDouble("offset.x", 0),
                        config.getDouble("offset.y", 0),
                        config.getDouble("offset.z", 0)
                );

        if(location.getWorld() == null) return;

        for (int d = 0; d <= 90; d += 1) {
            Location particleLoc = new Location(location.getWorld(), location.getX(), location.getY(), location.getZ());
            particleLoc.setX(location.getX() + Math.cos(d) * size);
            particleLoc.setZ(location.getZ() + Math.sin(d) * size);
            api.LIST_1_8.REDSTONE
                    .packetColored(false, particleLoc,
                            checkRGB(config.getInt("color.red", 144), 144),
                            checkRGB(config.getInt("color.green", 238), 238),
                            checkRGB(config.getInt("color.blue", 144), 144))
                    .sendTo(player);
        }
    }

    private int checkRGB(int color, int defaultColor){
        if(color < 0 || color > 255) return defaultColor;
        else return color;
    }
}
