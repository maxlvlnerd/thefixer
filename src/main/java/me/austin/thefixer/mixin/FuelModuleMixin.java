package me.austin.thefixer.mixin;

import io.github.fabricators_of_create.porting_lib.util.StorageProvider;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import slimeknights.tconstruct.smeltery.block.entity.module.FuelModule;

// Hephaestus for some reason get a NPE when trying to use the fluidHandler and crashes my game when using a smeltery
@Mixin(value = FuelModule.class, remap = false)
public abstract class FuelModuleMixin {
    @Shadow @Nullable private StorageProvider<ItemVariant> itemHandler;
    @Shadow protected abstract void reset();
    @Shadow @Nullable private StorageProvider<FluidVariant> fluidHandler;

    @Overwrite(remap = false)
    public Storage<ItemVariant> getItemStorage() {
        if (this.itemHandler == null) {
            return null;
        }
        Storage<ItemVariant> storage = this.itemHandler.get(null);
        if (storage == null) {
            this.reset();
        }

        return storage;
    }

    @Overwrite(remap = false)
    public Storage<FluidVariant> getFluidStorage() {
        if (this.fluidHandler == null) {
            return null;
        }
        Storage<FluidVariant> storage = this.fluidHandler.get(null);
        if (storage == null) {
            this.reset();
        }

        return storage;
    }
}
