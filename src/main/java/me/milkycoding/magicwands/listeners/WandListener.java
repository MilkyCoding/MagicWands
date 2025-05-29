package me.milkycoding.magicwands.listeners;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import me.milkycoding.magicwands.wands.WandManager;
import me.milkycoding.magicwands.wands.WandManager.WandType;
import me.milkycoding.magicwands.util.ConfigUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class WandListener implements Listener {
    private final Map<UUID, Long> cooldowns = new HashMap<>();

    @EventHandler
    public void onWandUse(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (!WandManager.isWand(item)) {
            return;
        }

        event.setCancelled(true);
        WandType type = WandManager.getWandType(item);

        if (type == null) {
            return;
        }

        // Проверка кулдауна
        if (isOnCooldown(player, type)) {
            return;
        }

        switch (type) {
            case TELEPORT:
                handleTeleport(player);
                break;
            case EXPLOSION:
                handleExplosion(player);
                break;
            case LIGHTNING:
                handleLightning(player);
                break;
        }

        // Установка кулдауна
        setCooldown(player, type);
    }

    private boolean isOnCooldown(Player player, WandType type) {
        long currentTime = System.currentTimeMillis();
        long lastUse = cooldowns.getOrDefault(player.getUniqueId(), 0L);
        int cooldown = WandManager.getCooldown(type) * 1000; // конвертируем в миллисекунды

        if (currentTime - lastUse < cooldown) {
            int remainingTime = (int) ((cooldown - (currentTime - lastUse)) / 1000);
            player.sendMessage(ConfigUtil.getPrefixedMessage("cooldown", "%time%", String.valueOf(remainingTime)));
            return true;
        }
        return false;
    }

    private void setCooldown(Player player, WandType type) {
        cooldowns.put(player.getUniqueId(), System.currentTimeMillis());
    }

    private void handleTeleport(Player player) {
        Location target = player.getTargetBlock(null, WandManager.getRange(WandType.TELEPORT)).getLocation();
        if (target != null) {
            player.teleport(target.add(0.5, 1, 0.5));
            player.sendMessage(WandManager.getMessage(WandType.TELEPORT));
        }
    }

    private void handleExplosion(Player player) {
        Location target = player.getTargetBlock(null, WandManager.getRange(WandType.EXPLOSION)).getLocation();
        if (target != null) {
            player.getWorld().createExplosion(target, (float) WandManager.getExplosionPower(), false, false);
            player.sendMessage(WandManager.getMessage(WandType.EXPLOSION));
        }
    }

    private void handleLightning(Player player) {
        Location target = player.getTargetBlock(null, WandManager.getRange(WandType.LIGHTNING)).getLocation();
        if (target != null) {
            player.getWorld().strikeLightning(target);
            player.sendMessage(WandManager.getMessage(WandType.LIGHTNING));
        }
    }
} 