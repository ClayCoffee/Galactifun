package io.github.addoncommunity.galactifun.base.aliens;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.Nonnull;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Silverfish;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.persistence.PersistentDataContainer;

import io.github.addoncommunity.galactifun.Galactifun;
import io.github.addoncommunity.galactifun.api.aliens.Alien;
import io.github.mooy1.infinitylib.common.PersistentType;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;

/**
 * Class for the leech, an alien of Titan. They can steal your items when attacking
 *
 * @author Seggan
 * @author Mooy1
 */
public final class Leech extends Alien<Silverfish> {

    private final NamespacedKey eatenKey = Galactifun.createKey("eaten");

    public Leech(String id, String name, double maxHealth, int spawnChance) {
        super(Silverfish.class, id, name, maxHealth, spawnChance);
    }

    @Override
    public void onAttack(@Nonnull EntityDamageByEntityEvent e) {

        if (ThreadLocalRandom.current().nextDouble(100) <
                Galactifun.instance().getConfig().getDouble("aliens.leech-eat-chance", 0, 100)) return;

        // random item
        PlayerInventory inv = ((Player) e.getEntity()).getInventory();
        IntList slots = new IntArrayList();

        ItemStack[] contents = inv.getContents();
        for (int i = 0; i < contents.length; i++) {
            if (contents[i] != null) {
                slots.add(i);
            }
        }

        if (slots.isEmpty()) {
            return;
        }

        int slot = slots.getInt(ThreadLocalRandom.current().nextInt(slots.size()));

        ItemStack item = contents[slot];

        // eat it
        PersistentDataContainer container = e.getEntity().getPersistentDataContainer();
        List<ItemStack> eatenItems = container.get(this.eatenKey, PersistentType.ITEM_STACK_LIST);
        if (eatenItems != null) {
            eatenItems.add(item);
            container.set(this.eatenKey, PersistentType.ITEM_STACK_LIST, eatenItems);
        } else {
            container.set(this.eatenKey, PersistentType.ITEM_STACK_LIST, Collections.singletonList(item));
        }

        inv.setItem(slot, null);

        // heal
        LivingEntity attacker = (LivingEntity) e.getDamager();
        attacker.setHealth(Math.min(maxHealth(), attacker.getHealth() + 2));
    }

    @Override
    public void onDeath(@Nonnull EntityDeathEvent e) {
        e.getDrops().clear();
        List<ItemStack> eatenItems = e.getEntity().getPersistentDataContainer().get(this.eatenKey, PersistentType.ITEM_STACK_LIST);
        if (eatenItems != null) {
            e.getDrops().addAll(eatenItems);
        }
    }

}
