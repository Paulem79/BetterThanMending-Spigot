package ovh.paulem.btm.config;

import ovh.paulem.btm.BetterMending;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class PlayerDataConfig {
    private final BetterMending plugin;
    private final File dataFile;
    private final YamlConfiguration data;

    public PlayerDataConfig(BetterMending plugin) {
        this.plugin = plugin;

        this.dataFile = new File(plugin.getDataFolder(), "data.yml");
        try {
            this.dataFile.createNewFile();
        } catch (IOException e) {
            plugin.getLogger().severe("Error creating data.yml file !");
        }
        this.data = YamlConfiguration.loadConfiguration(dataFile);
    }

    public boolean getPlayer(Player player) {
        return this.data.getBoolean(player.getUniqueId().toString());
    }

    public boolean getPlayerOrDefault(Player player, boolean defaultValue) {
        return this.data.getBoolean(player.getUniqueId().toString(), defaultValue);
    }

    public boolean getPlayerOrCreate(Player player, boolean enabled) throws IOException {
        Object returned = this.data.get(player.getUniqueId().toString());

        if(returned == null) return setPlayer(player, enabled);

        return getPlayer(player);
    }

    public boolean setPlayer(Player player, boolean enabled) throws IOException {
        this.data.set(player.getUniqueId().toString(), enabled);

        this.data.save(this.dataFile);

        return getPlayer(player);
    }

    public void reload() {
        try {
            this.data.load(this.dataFile);
        } catch (Exception e) {
            plugin.getLogger().throwing(PlayerDataConfig.class.getName(), "reload", e);
        }
    }
}
