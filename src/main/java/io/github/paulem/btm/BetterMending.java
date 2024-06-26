package io.github.paulem.btm;

import com.jeff_media.updatechecker.UpdateCheckSource;
import com.jeff_media.updatechecker.UpdateChecker;
import io.github.paulem.btm.commands.CommandBTM;
import io.github.paulem.btm.config.PlayerDataConfig;
import io.github.paulem.btm.libs.bstats.Metrics;
import io.github.paulem.btm.listeners.MendingUseListener;
import io.github.paulem.btm.listeners.PreventDestroyListener;
import io.github.paulem.btm.config.ConfigManager;
import io.github.paulem.btm.damage.DamageManager;
import io.github.paulem.btm.damage.LegacyDamage;
import io.github.paulem.btm.managers.RepairManager;
import io.github.paulem.btm.damage.NewerDamage;
import io.github.paulem.btm.versioning.Versioning;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class BetterMending extends JavaPlugin {
    public PlayerDataConfig playerDataConfig;

    @Override
    public void onEnable() {
        if(!Versioning.isPost9()) {
            getLogger().severe("You need to use a 1.9+ server! Mending isn't present in older versions!");
            setEnabled(false);
            return;
        }

        saveDefaultConfig();
        FileConfiguration config = getConfig();

        new ConfigManager(this).migrate();

        playerDataConfig = new PlayerDataConfig(this);

        final DamageManager damageManager = Versioning.isPost17() ? new NewerDamage() : new LegacyDamage();

        final RepairManager repairManager = new RepairManager(this, config, damageManager);

        final String SPIGOT_RESOURCE_ID = "112248";
        new UpdateChecker(this, UpdateCheckSource.SPIGET, SPIGOT_RESOURCE_ID) // You can also use Spiget instead of Spigot - Spiget's API is usually much faster up to date.
                .checkEveryXHours(24) // Check every 24 hours
                .setChangelogLink(SPIGOT_RESOURCE_ID)
                .setNotifyOpsOnJoin(true)
                .checkNow(); // And check right now

        getServer().getPluginManager().registerEvents(new MendingUseListener(config, damageManager, repairManager, playerDataConfig), this);
        getServer().getPluginManager().registerEvents(new PreventDestroyListener(config, damageManager, repairManager), this);

        final CommandBTM commandBTM = new CommandBTM(config.getInt("version", 0), playerDataConfig, getDescription().getVersion());
        getCommand("btm").setExecutor(commandBTM);
        getCommand("btm").setTabCompleter(commandBTM);

        getLogger().info("Enabled!");

        if(config.getBoolean("auto-repair", false))
            repairManager.initAutoRepair();

        if(config.getBoolean("bstat", true)){
            new Metrics(this, 21472);
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("Disabled! See you later!");
    }
}
