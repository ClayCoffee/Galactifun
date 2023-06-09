package io.github.addoncommunity.galactifun.api.universe.attributes.atmosphere;

import io.github.addoncommunity.galactifun.Galactifun;
import io.github.addoncommunity.galactifun.api.universe.PlanetaryObject;
import io.github.addoncommunity.galactifun.base.BaseItems;
import io.github.addoncommunity.galactifun.base.BaseMats;
import io.github.addoncommunity.galactifun.base.BaseUniverse;
import io.github.addoncommunity.galactifun.base.items.DiamondAnvil;
import io.github.addoncommunity.galactifun.core.CoreItemGroup;
import io.github.addoncommunity.galactifun.core.CoreRecipeType;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;

import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.generators.CombustionGenerator;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.machines.Freezer;
import io.github.thebusybiscuit.slimefun4.utils.ChatUtils;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;

import lombok.Getter;

import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineFuel;

import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static io.github.addoncommunity.galactifun.api.universe.attributes.atmosphere.BasicGas.*;


public class Gas {

    static {
        if (SlimefunItems.FREEZER_2.getItem() instanceof Freezer freezer) {
            freezer.registerRecipe(10, NITROGEN.item(), SlimefunItems.REACTOR_COOLANT_CELL.asQuantity(4));
        }

        if (SlimefunItems.COMBUSTION_REACTOR.getItem() instanceof CombustionGenerator generator) {
            generator.registerFuel(new MachineFuel(15, HYDROGEN.item()));
            generator.registerFuel(new MachineFuel(30, HYDROCARBONS.item()));
            generator.registerFuel(new MachineFuel(70, AMMONIA.item()));
            generator.registerFuel(new MachineFuel(200, METHANE.item()));
        }

        if (BaseItems.DIAMOND_ANVIL.getItem() instanceof DiamondAnvil anvil) {
            anvil.registerRecipe(10, HYDROGEN.item().asQuantity(4), HELIUM.item());
            anvil.registerRecipe(10, HELIUM.item().asQuantity(4), BaseMats.FUSION_PELLET);
        }
    }

    @Getter
    protected SlimefunItemStack item;

    protected String id;
    protected String name;

    @Getter
    private final SlimefunItem slimefunItem;


    public Gas(String id, String name, String texture, RecipeType recipeType, ItemStack[] recipe) {
        this.id = id;
        this.name = name;
        this.item = new SlimefunItemStack(
                "ATMOSPHERIC_GAS_" + name(),
                SlimefunUtils.getCustomHead(texture),
                "&f" + ChatUtils.humanize(name) + " 气罐"
        );

        slimefunItem = new SlimefunItem(CoreItemGroup.ITEMS, this.item, recipeType, recipe);
        slimefunItem.register(Galactifun.instance());
    }


    public Gas(String id, String name, @Nonnull String texture) {
        this(id, name, texture, CoreRecipeType.ATMOSPHERIC_HARVESTER);
    }


    @ParametersAreNonnullByDefault
    public Gas(String id, String name, String texture, RecipeType recipeType) {
        this(id, name, texture, recipeType, new ItemStack[9]);
    }


    public Gas(String id, String name) {
        this.id = id;
        this.name = name;
        this.item = null;
        this.slimefunItem = null;
    }


    @Nonnull
    @Override
    public String toString() {
        return ChatUtils.humanize(this.name());
    }

    public String name() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static void setRecipes() {
        setRecipe(OXYGEN, BaseUniverse.EARTH);
        setRecipe(NITROGEN, BaseUniverse.EARTH);
        setRecipe(CARBON_DIOXIDE, BaseUniverse.EARTH);
        setRecipe(WATER, BaseUniverse.EARTH);
        setRecipe(HELIUM, BaseUniverse.JUPITER);
        setRecipe(ARGON, BaseUniverse.EARTH);
        setRecipe(METHANE, BaseUniverse.SATURN);
        setRecipe(HYDROCARBONS, BaseUniverse.TITAN);
        setRecipe(HYDROGEN, BaseUniverse.JUPITER);
    }

    @ParametersAreNonnullByDefault
    private static void setRecipe(Gas gas, PlanetaryObject planetaryObject) {
        gas.slimefunItem().setRecipe(new ItemStack[] {
                null, null, null,
                null, planetaryObject.item(), null,
                null, null, null
        });
    }
}
