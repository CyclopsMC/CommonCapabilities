package org.cyclops.commoncapabilities.capability;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.cyclopscore.persist.nbt.NBTClassType;

import java.util.Collection;
import java.util.Set;

/**
 * Registry for capabilities created by this mod.
 * @author rubensworks
 */
public class CapabilityConstructorRegistry {
    private static final CapabilityConstructorRegistry INSTANCE = new CapabilityConstructorRegistry();
    private static final Multimap<Class<? extends TileEntity>, ICapabilityConstructor<? extends TileEntity>>
            CAPABILITY_CONSTRUCTORS_TILE = HashMultimap.create();
    private static final Multimap<Class<? extends Entity>, ICapabilityConstructor<? extends Entity>>
            CAPABILITY_CONSTRUCTORS_ENTITY = HashMultimap.create();
    private static final Multimap<Class<? extends Item>, ICapabilityConstructor<? extends ItemStack>>
            CAPABILITY_CONSTRUCTORS_ITEM = HashMultimap.create();
    private static final Set<Pair<Class<?>, ICapabilityConstructor<? extends TileEntity>>>
            CAPABILITY_CONSTRUCTORS_TILE_SUPER = Sets.newHashSet();
    private static final Set<Pair<Class<?>, ICapabilityConstructor<? extends Entity>>>
            CAPABILITY_CONSTRUCTORS_ENTITY_SUPER = Sets.newHashSet();
    private static final Set<Pair<Class<?>, ICapabilityConstructor<? extends ItemStack>>>
            CAPABILITY_CONSTRUCTORS_ITEM_SUPER = Sets.newHashSet();

    /**
     * Register a tile capability constructor.
     * @param clazz The tile class.
     * @param constructor The capability constructor.
     * @param <T> The tile type.
     */
    public static <T extends TileEntity> void registerTile(Class<T> clazz, ICapabilityConstructor<T> constructor) {
        CAPABILITY_CONSTRUCTORS_TILE.put(clazz, constructor);
    }

    /**
     * Register an entity capability constructor.
     * @param clazz The entity class.
     * @param constructor The capability constructor.
     * @param <T> The entity type.
     */
    public static <T extends Entity> void registerEntity(Class<T> clazz, ICapabilityConstructor<T> constructor) {
        CAPABILITY_CONSTRUCTORS_ENTITY.put(clazz, constructor);
    }

    /**
     * Register an item capability constructor.
     * @param clazz The item class.
     * @param constructor The capability constructor.
     * @param <T> The item type.
     */
    public static <T extends Item> void registerItem(Class<T> clazz, ICapabilityConstructor<ItemStack> constructor) {
        CAPABILITY_CONSTRUCTORS_ITEM.put(clazz, constructor);
    }

    /**
     * Register a tile capability constructor with subtype checking.
     * Only call this when absolutely required, this will is less efficient than its non-inheritable counterpart.
     * @param clazz The tile class, all subtypes will be checked.
     * @param constructor The capability constructor.
     * @param <K> The tile type.
     * @param <V> The tile type.
     */
    public static <K, V extends TileEntity> void registerInheritableTile(Class<K> clazz, ICapabilityConstructor<V> constructor) {
        CAPABILITY_CONSTRUCTORS_TILE_SUPER.add(
                Pair.<Class<?>, ICapabilityConstructor<? extends TileEntity>>of(clazz, constructor));
    }

    /**
     * Register an entity capability constructor with subtype checking.
     * Only call this when absolutely required, this will is less efficient than its non-inheritable counterpart.
     * @param clazz The tile class, all subtypes will be checked.
     * @param constructor The capability constructor.
     * @param <K> The tile type.
     * @param <V> The tile type.
     */
    public static <K, V extends Entity> void registerInheritableEntity(Class<K> clazz, ICapabilityConstructor<V> constructor) {
        CAPABILITY_CONSTRUCTORS_ENTITY_SUPER.add(
                Pair.<Class<?>, ICapabilityConstructor<? extends Entity>>of(clazz, constructor));
    }

    /**
     * Register an item capability constructor with subtype checking.
     * Only call this when absolutely required, this will is less efficient than its non-inheritable counterpart.
     * @param clazz The tile class, all subtypes will be checked.
     * @param constructor The capability constructor.
     * @param <T> The tile type.
     */
    public static <T> void registerInheritableItem(Class<T> clazz, ICapabilityConstructor<? extends ItemStack> constructor) {
        CAPABILITY_CONSTRUCTORS_ITEM_SUPER.add(
                Pair.<Class<?>, ICapabilityConstructor<? extends ItemStack>>of(clazz, constructor));
    }

    @SuppressWarnings("unchecked")
    protected static <H, HE> ICapabilityProvider createProvider(HE host, ICapabilityConstructor<H> capabilityConstructor) {
        return capabilityConstructor.createProvider((H) host);
    }

    private CapabilityConstructorRegistry() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    protected <T> void onLoad(Multimap<Class<? extends T>, ICapabilityConstructor<? extends T>> allConstructors,
                              Set<Pair<Class<?>, ICapabilityConstructor<? extends T>>> allInheritableConstructors,
                              T object, AttachCapabilitiesEvent event) {
        onLoad(allConstructors, allInheritableConstructors, object, object, event);
    }

    protected <K, V> void onLoad(Multimap<Class<? extends K>, ICapabilityConstructor<? extends V>> allConstructors,
                                 Set<Pair<Class<?>, ICapabilityConstructor<? extends V>>> allInheritableConstructors,
                                 K keyObject, V valueObject, AttachCapabilitiesEvent event) {
        Collection<ICapabilityConstructor<? extends V>> constructors = allConstructors.get((Class<? extends K>) keyObject.getClass());
        for(ICapabilityConstructor<? extends V> constructor : constructors) {
            event.addCapability(constructor.getKey(), createProvider(valueObject, constructor));
        }

        for (Pair<Class<?>, ICapabilityConstructor<? extends V>> constructorEntry : allInheritableConstructors) {
            if(constructorEntry.getLeft().isInstance(keyObject)) {
                ICapabilityConstructor<? extends V> constructor = constructorEntry.getRight();
                ICapabilityProvider provider = createProvider(valueObject, constructor);
                if(provider != null) {
                    event.addCapability(constructor.getKey(), provider);
                }
            }
        }

    }

    @SubscribeEvent
    public void onTileLoad(AttachCapabilitiesEvent.TileEntity event) {
        onLoad(CAPABILITY_CONSTRUCTORS_TILE, CAPABILITY_CONSTRUCTORS_TILE_SUPER, event.getTileEntity(), event);
    }

    @SubscribeEvent
    public void onEntityLoad(AttachCapabilitiesEvent.Entity event) {
        onLoad(CAPABILITY_CONSTRUCTORS_ENTITY, CAPABILITY_CONSTRUCTORS_ENTITY_SUPER, event.getEntity(), event);
    }

    @SubscribeEvent
    public void onItemStackLoad(AttachCapabilitiesEvent.Item event) {
        onLoad(CAPABILITY_CONSTRUCTORS_ITEM, CAPABILITY_CONSTRUCTORS_ITEM_SUPER, event.getItem(), event.getItemStack(), event);
    }
}
