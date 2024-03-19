package org.cyclops.commoncapabilities.api.capability.fluidhandler;

import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import javax.annotation.Nonnull;
import java.util.Collection;

/**
 * Copy-pasted from Forge 1.12 and adapted.
 * If they re-add it, this can be removed.
 * @author rubensworks
 */
public class FluidHandlerConcatenate implements IFluidHandler {

    protected final IFluidHandler[] subHandlers;

    public FluidHandlerConcatenate(IFluidHandler... subHandlers)
    {
        this.subHandlers = subHandlers;
    }

    public FluidHandlerConcatenate(Collection<IFluidHandler> subHandlers)
    {
        this.subHandlers = subHandlers.toArray(new IFluidHandler[subHandlers.size()]);
    }

    @Override
    public int getTanks() {
        int sum = 0;
        for (IFluidHandler subHandler : this.subHandlers) {
            sum += subHandler.getTanks();
        }
        return sum;
    }

    @Nonnull
    @Override
    public FluidStack getFluidInTank(int tank) {
        for (IFluidHandler subHandler : this.subHandlers) {
            if (tank < subHandler.getTanks()) {
                return subHandler.getFluidInTank(tank);
            }
            tank -= subHandler.getTanks();
        }
        return FluidStack.EMPTY;
    }

    @Override
    public int getTankCapacity(int tank) {
        for (IFluidHandler subHandler : this.subHandlers) {
            if (tank < subHandler.getTanks()) {
                return subHandler.getTankCapacity(tank);
            }
            tank -= subHandler.getTanks();
        }
        return 0;
    }

    @Override
    public boolean isFluidValid(int tank, @Nonnull FluidStack stack) {
        for (IFluidHandler subHandler : this.subHandlers) {
            if (tank < subHandler.getTanks()) {
                return subHandler.isFluidValid(tank, stack);
            }
            tank -= subHandler.getTanks();
        }
        return false;
    }

    @Override
    public int fill(FluidStack resource, FluidAction action)
    {
        if (resource.isEmpty() || resource.getAmount() <= 0)
            return 0;

        resource = resource.copy();

        int totalFillAmount = 0;
        for (IFluidHandler handler : subHandlers)
        {
            int fillAmount = handler.fill(resource, action);
            totalFillAmount += fillAmount;
            resource.setAmount(resource.getAmount() - fillAmount);
            if (resource.getAmount() <= 0)
                break;
        }
        return totalFillAmount;
    }

    @Override
    public FluidStack drain(FluidStack resource, FluidAction action)
    {
        if (resource.isEmpty() || resource.getAmount() <= 0)
            return FluidStack.EMPTY;

        resource = resource.copy();

        FluidStack totalDrained = null;
        for (IFluidHandler handler : subHandlers)
        {
            FluidStack drain = handler.drain(resource, action);
            if (!drain.isEmpty())
            {
                if (totalDrained == null)
                    totalDrained = drain;
                else
                    totalDrained.setAmount(totalDrained.getAmount() + drain.getAmount());;

                resource.setAmount(resource.getAmount() - drain.getAmount());
                if (resource.getAmount() <= 0)
                    break;
            }
        }
        return totalDrained == null ? FluidStack.EMPTY : totalDrained;
    }

    @Override
    public FluidStack drain(int maxDrain, FluidAction action)
    {
        if (maxDrain == 0)
            return FluidStack.EMPTY;
        FluidStack totalDrained = null;
        for (IFluidHandler handler : subHandlers)
        {
            if (totalDrained == null)
            {
                FluidStack drained = handler.drain(maxDrain, action);
                if (!drained.isEmpty())
                {
                    totalDrained = drained;
                    maxDrain -= totalDrained.getAmount();
                }
            }
            else
            {
                FluidStack copy = totalDrained.copy();
                copy.setAmount(maxDrain);
                FluidStack drain = handler.drain(copy, action);
                if (!drain.isEmpty())
                {
                    totalDrained.setAmount(totalDrained.getAmount() + drain.getAmount());
                    maxDrain -= drain.getAmount();
                }
            }

            if (maxDrain <= 0)
                break;
        }
        return totalDrained == null ? FluidStack.EMPTY : totalDrained;
    }

}
