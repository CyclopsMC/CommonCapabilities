package org.cyclops.commoncapabilities.ingredient;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.item.ItemResource;
import org.cyclops.commoncapabilities.api.ingredient.IResourceConverter;

/**
 * @author rubensworks
 */
public class ResourceConverterItem implements IResourceConverter<ItemResource, ItemStack> {
    @Override
    public ItemStack fromResource(ItemResource resource, int amount) {
        return resource.toStack(amount);
    }

    @Override
    public ItemResource toResource(ItemStack ingredient) {
        return ItemResource.of(ingredient);
    }
}
