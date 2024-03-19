package org.cyclops.commoncapabilities.modcompat.thermalexpansion.slotlessitemhandler;

import cofh.thermalexpansion.block.storage.TileCache;
import net.minecraft.item.ItemStack;
import net.neoforged.fml.relauncher.Side;
import net.neoforged.neoforge.items.IItemHandler;
import org.cyclops.commoncapabilities.api.capability.itemhandler.DefaultSlotlessItemHandlerWrapper;
import org.cyclops.commoncapabilities.api.ingredient.IIngredientMatcher;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;

import javax.annotation.Nonnull;

import TileCache;

/**
 * A slotless item handler for TE's cache.
 * This allows item extraction for 64+ items at a time.
 * @author rubensworks
 */
public class TileCacheSlotlessItemHandler extends DefaultSlotlessItemHandlerWrapper {

    private final TileCache tile;

    public TileCacheSlotlessItemHandler(TileCache tile) {
        super(null); // Not needed since we override getItemHandler
        this.tile = tile;
    }

    @Override
    public IItemHandler getItemHandler() {
        return tile.getHandler();
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int amount, boolean simulate) {
        TileCache.CacheItemHandler handler = tile.getHandler();
        ItemStack storedStack = handler.getStackInSlot(0);
        if (storedStack.getCount() < amount) {
            amount = storedStack.getCount();
        }
        ItemStack ret = IngredientComponent.ITEMSTACK.getMatcher().withQuantity(storedStack, amount);
        simulate |= tile.isCreative;
        if (!simulate) {
            storedStack.shrink(amount);
            handler.setItem(storedStack);
            if (storedStack.isEmpty()) {
                if (!handler.isLocked()) {
                    tile.sendTilePacket(Side.CLIENT);
                }
            }
        }
        return ret;
    }

    @Nonnull
    @Override
    public ItemStack extractItem(@Nonnull ItemStack matchStack, int matchFlags, boolean simulate) {
        TileCache.CacheItemHandler handler = tile.getHandler();
        ItemStack storedStack = handler.getStackInSlot(0);
        IIngredientMatcher<ItemStack, Integer> matcher = IngredientComponent.ITEMSTACK.getMatcher();
        if (matcher.matches(matchStack, storedStack, matchFlags)) {
            return this.extractItem(matchStack.getCount(), simulate);
        }
        return ItemStack.EMPTY;
    }
}
