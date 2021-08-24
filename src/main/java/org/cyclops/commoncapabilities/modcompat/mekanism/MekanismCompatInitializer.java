package org.cyclops.commoncapabilities.modcompat.mekanism;

import com.google.common.collect.Lists;
import mekanism.api.MekanismAPI;
import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.chemical.gas.IGasHandler;
import mekanism.api.chemical.infuse.IInfusionHandler;
import mekanism.api.chemical.infuse.InfuseType;
import mekanism.api.chemical.infuse.InfusionStack;
import mekanism.api.chemical.pigment.IPigmentHandler;
import mekanism.api.chemical.pigment.Pigment;
import mekanism.api.chemical.pigment.PigmentStack;
import mekanism.api.chemical.slurry.ISlurryHandler;
import mekanism.api.chemical.slurry.Slurry;
import mekanism.api.chemical.slurry.SlurryStack;
import net.minecraft.block.Block;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.IForgeRegistry;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponentCategoryType;
import org.cyclops.commoncapabilities.api.ingredient.storage.IIngredientComponentStorage;
import org.cyclops.commoncapabilities.api.ingredient.storage.IIngredientComponentStorageSlotted;
import org.cyclops.cyclopscore.modcompat.ICompatInitializer;

/**
 * @author rubensworks
 */
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class MekanismCompatInitializer implements ICompatInitializer {

    public static IngredientComponent<GasStack, Integer> INGREDIENT_GASSTACK;
    public static IngredientComponent<InfusionStack, Integer> INGREDIENT_INFUSIONSTACK;
    public static IngredientComponent<PigmentStack, Integer> INGREDIENT_PIGMENTSTACK;
    public static IngredientComponent<SlurryStack, Integer> INGREDIENT_SLURRYSTACK;

    @CapabilityInject(IGasHandler.class)
    public static Capability<IGasHandler> GAS_HANDLER_CAPABILITY = null;
    @CapabilityInject(IInfusionHandler.class)
    public static Capability<IInfusionHandler> INFUSION_HANDLER_CAPABILITY = null;
    @CapabilityInject(IPigmentHandler.class)
    public static Capability<IPigmentHandler> PIGMENT_HANDLER_CAPABILITY = null;
    @CapabilityInject(ISlurryHandler.class)
    public static Capability<ISlurryHandler> SLURRY_HANDLER_CAPABILITY = null;

    @Override
    public void initialize() {
        FMLJavaModLoadingContext.get().getModEventBus()
                .addGenericListener(Block.class, EventPriority.LOW, this::onRegistriesLoad);
        FMLJavaModLoadingContext.get().getModEventBus()
                .addListener(EventPriority.LOW, this::afterCapabilitiesLoaded);
    }

    public void onRegistriesLoad(RegistryEvent.Register<Block> event) {
        // Create ingredient types
        INGREDIENT_GASSTACK = new IngredientComponent<>(
                "mekanism:gasstack",
                new IngredientMatcherChemicalStack<GasStack, Gas>() {
                    @Override
                    protected IForgeRegistry<Gas> getRegistry() {
                        return MekanismAPI.gasRegistry();
                    }

                    @Override
                    protected GasStack createChemicalStack(Gas type, long quantity) {
                        return new GasStack(type, quantity);
                    }

                    @Override
                    public GasStack getEmptyInstance() {
                        return GasStack.EMPTY;
                    }
                },
                new IngredientSerializerChemicalStack<GasStack, Gas>() {
                    @Override
                    protected GasStack readFromNbt(CompoundNBT tag) {
                        return GasStack.readFromNBT(tag);
                    }
                },
                Lists.newArrayList(
                        new IngredientComponentCategoryType<>(new ResourceLocation("mekanism:gasstack/gas"),
                                Gas.class, true, GasStack::getType, ChemicalMatch.TYPE, false),
                        new IngredientComponentCategoryType<>(new ResourceLocation("mekanism:gasstack/amount"),
                                Long.class, false, GasStack::getAmount, ChemicalMatch.AMOUNT, true)
                )
        ).setTranslationKey("recipecomponent.mekanism.gasstack");

        INGREDIENT_INFUSIONSTACK = new IngredientComponent<>(
                "mekanism:infusionstack",
                new IngredientMatcherChemicalStack<InfusionStack, InfuseType>() {
                    @Override
                    protected IForgeRegistry<InfuseType> getRegistry() {
                        return MekanismAPI.infuseTypeRegistry();
                    }

                    @Override
                    protected InfusionStack createChemicalStack(InfuseType type, long quantity) {
                        return new InfusionStack(type, quantity);
                    }

                    @Override
                    public InfusionStack getEmptyInstance() {
                        return InfusionStack.EMPTY;
                    }
                },
                new IngredientSerializerChemicalStack<InfusionStack, InfuseType>() {
                    @Override
                    protected InfusionStack readFromNbt(CompoundNBT tag) {
                        return InfusionStack.readFromNBT(tag);
                    }
                },
                Lists.newArrayList(
                        new IngredientComponentCategoryType<>(new ResourceLocation("mekanism:infusionstack/infusion"),
                                InfuseType.class, true, InfusionStack::getType, ChemicalMatch.TYPE, false),
                        new IngredientComponentCategoryType<>(new ResourceLocation("mekanism:infusionstack/amount"),
                                Long.class, false, InfusionStack::getAmount, ChemicalMatch.AMOUNT, true)
                )
        ).setTranslationKey("recipecomponent.mekanism.infusionstack");

        INGREDIENT_PIGMENTSTACK = new IngredientComponent<>(
                "mekanism:pigmentstack",
                new IngredientMatcherChemicalStack<PigmentStack, Pigment>() {
                    @Override
                    protected IForgeRegistry<Pigment> getRegistry() {
                        return MekanismAPI.pigmentRegistry();
                    }

                    @Override
                    protected PigmentStack createChemicalStack(Pigment type, long quantity) {
                        return new PigmentStack(type, quantity);
                    }

                    @Override
                    public PigmentStack getEmptyInstance() {
                        return PigmentStack.EMPTY;
                    }
                },
                new IngredientSerializerChemicalStack<PigmentStack, Pigment>() {
                    @Override
                    protected PigmentStack readFromNbt(CompoundNBT tag) {
                        return PigmentStack.readFromNBT(tag);
                    }
                },
                Lists.newArrayList(
                        new IngredientComponentCategoryType<>(new ResourceLocation("mekanism:pigmentstack/pigment"),
                                Pigment.class, true, PigmentStack::getType, ChemicalMatch.TYPE, false),
                        new IngredientComponentCategoryType<>(new ResourceLocation("mekanism:pigmentstack/amount"),
                                Long.class, false, PigmentStack::getAmount, ChemicalMatch.AMOUNT, true)
                )
        ).setTranslationKey("recipecomponent.mekanism.pigmentstack");

        INGREDIENT_SLURRYSTACK = new IngredientComponent<>(
                "mekanism:slurrystack",
                new IngredientMatcherChemicalStack<SlurryStack, Slurry>() {
                    @Override
                    protected IForgeRegistry<Slurry> getRegistry() {
                        return MekanismAPI.slurryRegistry();
                    }

                    @Override
                    protected SlurryStack createChemicalStack(Slurry type, long quantity) {
                        return new SlurryStack(type, quantity);
                    }

                    @Override
                    public SlurryStack getEmptyInstance() {
                        return SlurryStack.EMPTY;
                    }
                },
                new IngredientSerializerChemicalStack<SlurryStack, Slurry>() {
                    @Override
                    protected SlurryStack readFromNbt(CompoundNBT tag) {
                        return SlurryStack.readFromNBT(tag);
                    }
                },
                Lists.newArrayList(
                        new IngredientComponentCategoryType<>(new ResourceLocation("mekanism:slurrystack/slurry"),
                                Slurry.class, true, SlurryStack::getType, ChemicalMatch.TYPE, false),
                        new IngredientComponentCategoryType<>(new ResourceLocation("mekanism:slurrystack/amount"),
                                Long.class, false, SlurryStack::getAmount, ChemicalMatch.AMOUNT, true)
                )
        ).setTranslationKey("recipecomponent.mekanism.slurrystack");

        // Register ingredient types
        IngredientComponent.REGISTRY.registerAll(
                MekanismCompatInitializer.INGREDIENT_GASSTACK,
                MekanismCompatInitializer.INGREDIENT_INFUSIONSTACK,
                MekanismCompatInitializer.INGREDIENT_PIGMENTSTACK,
                MekanismCompatInitializer.INGREDIENT_SLURRYSTACK
        );
    }

    public void afterCapabilitiesLoaded(InterModEnqueueEvent event) {
        // Add ingredient type capabilities
        IngredientComponentStorageWrapperHandlerChemicalStack<Gas, GasStack, IGasHandler> gasWrapper = new IngredientComponentStorageWrapperHandlerChemicalStack<Gas, GasStack, IGasHandler>(INGREDIENT_GASSTACK, GAS_HANDLER_CAPABILITY) {
            @Override
            public IGasHandler wrapStorage(IIngredientComponentStorage<GasStack, Integer> componentStorage) {
                if (componentStorage instanceof IIngredientComponentStorageSlotted) {
                    return new StorageWrapperGasSlotted((IIngredientComponentStorageSlotted<GasStack, Integer>) componentStorage);
                }
                return new StorageWrapperGas(componentStorage);
            }
        };
        INGREDIENT_GASSTACK.setStorageWrapperHandler(GAS_HANDLER_CAPABILITY, gasWrapper);
        IngredientComponentStorageWrapperHandlerChemicalStack<InfuseType, InfusionStack, IInfusionHandler> infusionWrapper = new IngredientComponentStorageWrapperHandlerChemicalStack<InfuseType, InfusionStack, IInfusionHandler>(INGREDIENT_INFUSIONSTACK, INFUSION_HANDLER_CAPABILITY){
            @Override
            public IInfusionHandler wrapStorage(IIngredientComponentStorage<InfusionStack, Integer> componentStorage) {
                if (componentStorage instanceof IIngredientComponentStorageSlotted) {
                    return new StorageWrapperInfusionSlotted((IIngredientComponentStorageSlotted<InfusionStack, Integer>) componentStorage);
                }
                return new StorageWrapperInfusion(componentStorage);
            }
        };
        INGREDIENT_INFUSIONSTACK.setStorageWrapperHandler(INFUSION_HANDLER_CAPABILITY, infusionWrapper);
        IngredientComponentStorageWrapperHandlerChemicalStack<Pigment, PigmentStack, IPigmentHandler> pigmentWrapper = new IngredientComponentStorageWrapperHandlerChemicalStack<Pigment, PigmentStack, IPigmentHandler>(INGREDIENT_PIGMENTSTACK, PIGMENT_HANDLER_CAPABILITY) {
            @Override
            public IPigmentHandler wrapStorage(IIngredientComponentStorage<PigmentStack, Integer> componentStorage) {
                if (componentStorage instanceof IIngredientComponentStorageSlotted) {
                    return new StorageWrapperPigmentSlotted((IIngredientComponentStorageSlotted<PigmentStack, Integer>) componentStorage);
                }
                return new StorageWrapperPigment(componentStorage);
            }
        };
        INGREDIENT_PIGMENTSTACK.setStorageWrapperHandler(PIGMENT_HANDLER_CAPABILITY, pigmentWrapper);
        IngredientComponentStorageWrapperHandlerChemicalStack<Slurry, SlurryStack, ISlurryHandler> slurryWrapper = new IngredientComponentStorageWrapperHandlerChemicalStack<Slurry, SlurryStack, ISlurryHandler>(INGREDIENT_SLURRYSTACK, SLURRY_HANDLER_CAPABILITY) {
            @Override
            public ISlurryHandler wrapStorage(IIngredientComponentStorage<SlurryStack, Integer> componentStorage) {
                if (componentStorage instanceof IIngredientComponentStorageSlotted) {
                    return new StorageWrapperSlurrySlotted((IIngredientComponentStorageSlotted<SlurryStack, Integer>) componentStorage);
                }
                return new StorageWrapperSlurry(componentStorage);
            }
        };
        INGREDIENT_SLURRYSTACK.setStorageWrapperHandler(SLURRY_HANDLER_CAPABILITY, slurryWrapper);
    }

    // --- Dummy storage wrapper extensions to make sure that the proper handler interfaces are implemented, because we can't do this via generics. ---

    public static final class StorageWrapperGasSlotted extends IngredientComponentStorageWrapperHandlerChemicalStack.ChemicalStorageWrapperSlotted<Gas, GasStack> implements IGasHandler {
        public StorageWrapperGasSlotted(IIngredientComponentStorageSlotted<GasStack, Integer> storage) {
            super(storage);
        }
    }
    public static final class StorageWrapperGas extends IngredientComponentStorageWrapperHandlerChemicalStack.ChemicalStorageWrapper<Gas, GasStack> implements IGasHandler {
        public StorageWrapperGas(IIngredientComponentStorage<GasStack, Integer> storage) {
            super(storage);
        }
    }
    public static final class StorageWrapperInfusionSlotted extends IngredientComponentStorageWrapperHandlerChemicalStack.ChemicalStorageWrapperSlotted<InfuseType, InfusionStack> implements IInfusionHandler {
        public StorageWrapperInfusionSlotted(IIngredientComponentStorageSlotted<InfusionStack, Integer> storage) {
            super(storage);
        }
    }
    public static final class StorageWrapperInfusion extends IngredientComponentStorageWrapperHandlerChemicalStack.ChemicalStorageWrapper<InfuseType, InfusionStack> implements IInfusionHandler {
        public StorageWrapperInfusion(IIngredientComponentStorage<InfusionStack, Integer> storage) {
            super(storage);
        }
    }
    public static final class StorageWrapperPigmentSlotted extends IngredientComponentStorageWrapperHandlerChemicalStack.ChemicalStorageWrapperSlotted<Pigment, PigmentStack> implements IPigmentHandler {
        public StorageWrapperPigmentSlotted(IIngredientComponentStorageSlotted<PigmentStack, Integer> storage) {
            super(storage);
        }
    }
    public static final class StorageWrapperPigment extends IngredientComponentStorageWrapperHandlerChemicalStack.ChemicalStorageWrapper<Pigment, PigmentStack> implements IPigmentHandler {
        public StorageWrapperPigment(IIngredientComponentStorage<PigmentStack, Integer> storage) {
            super(storage);
        }
    }
    public static final class StorageWrapperSlurrySlotted extends IngredientComponentStorageWrapperHandlerChemicalStack.ChemicalStorageWrapperSlotted<Slurry, SlurryStack> implements ISlurryHandler {
        public StorageWrapperSlurrySlotted(IIngredientComponentStorageSlotted<SlurryStack, Integer> storage) {
            super(storage);
        }
    }
    public static final class StorageWrapperSlurry extends IngredientComponentStorageWrapperHandlerChemicalStack.ChemicalStorageWrapper<Slurry, SlurryStack> implements ISlurryHandler {
        public StorageWrapperSlurry(IIngredientComponentStorage<SlurryStack, Integer> storage) {
            super(storage);
        }
    }
}
