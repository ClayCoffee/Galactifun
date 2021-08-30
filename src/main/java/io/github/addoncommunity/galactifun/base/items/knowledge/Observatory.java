package io.github.addoncommunity.galactifun.base.items.knowledge;

import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.addoncommunity.galactifun.Galactifun;
import io.github.addoncommunity.galactifun.api.worlds.PlanetaryWorld;
import io.github.addoncommunity.galactifun.core.CoreCategory;
import io.github.addoncommunity.galactifun.core.WorldSelector;
import io.github.thebusybiscuit.slimefun4.core.multiblocks.MultiBlockMachine;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.cscorelib2.data.PersistentDataAPI;

public final class Observatory extends MultiBlockMachine {

    public Observatory(SlimefunItemStack item, ItemStack[] recipe) {
        super(CoreCategory.MACHINES, item, recipe, BlockFace.SELF);
    }

    @Override
    public void onInteract(Player p, Block b) {
        NamespacedKey key = Galactifun.instance().getKey("discovering_" + p.getUniqueId());

        PlanetaryWorld world = Galactifun.worldManager().getWorld(p.getWorld());
        if (world == null) {
            p.sendMessage(ChatColor.RED + "You must be on a planet to use this!");
            return;
        }

        if (PersistentDataAPI.getBoolean(world.worldStorage(), key)) {
            p.sendMessage(ChatColor.RED + "Already discovering!");
            return;
        }

        new WorldSelector((pl, w, l) -> world.distanceTo(w) <= 0.25 && w instanceof PlanetaryWorld pw && KnowledgeLevel.get(pl, pw) == KnowledgeLevel.NONE, (pl, w) -> {
            pl.sendMessage(ChatColor.GREEN + "Discovering planet " + w.name());
            PlanetaryWorld pw = (PlanetaryWorld) w;
            PersistentDataAPI.setBoolean(pw.worldStorage(), key, true);
            Galactifun.instance().runSync(() -> {
                PersistentDataAPI.setBoolean(pw.worldStorage(), key, false);
                KnowledgeLevel.BASIC.set(pl, pw);
            }, 30 * 60 * 20);
        }).open(p);
    }

}
