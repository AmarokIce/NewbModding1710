package club.snowlyicewolf.modding1710.common.tile;

import club.snowlyicewolf.modding1710.util.UtilItemStack;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Objects;

public class TileSunFurnace extends TileEntity {
    public static final int MAX_SMELTING_TIME = 600;

    @Nullable private ItemStack itemInFurnace;
    private int timer = 0;

    public TileSunFurnace(final World world, final int meta) {
        this.setWorldObj(world);
        this.blockMetadata = meta;
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        if (Objects.isNull(itemInFurnace)) {
            return;
        }
        nbt.setTag("itemInFurnace",
            this.itemInFurnace.writeToNBT(new NBTTagCompound()));
        nbt.setInteger("timer", timer);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        if (!nbt.hasKey("itemInFurnace")) {
            return;
        }
        this.itemInFurnace =
            ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("itemInFurnace"));
        this.timer = nbt.getInteger("timer");
    }

    @Override
    public boolean canUpdate() {
        return this.worldObj.isRemote
            && this.worldObj.getWorldInfo().getVanillaDimension() == 0;
    }

    @Override
    public void updateEntity() {
        final boolean isRaining = this.worldObj.isRaining() || this.worldObj.isThundering();
        final boolean isDaytime = this.worldObj.isDaytime();
        if (isRaining || !isDaytime) {
            return;
        }

        if (Objects.isNull(this.itemInFurnace)) {
            return;
        }

        timer++;
        if (timer < MAX_SMELTING_TIME) {
            return;
        }

        timer = 0;
        final ItemStack itemOutput = getFurnaceRecipes(this.itemInFurnace);
        if (Objects.isNull(itemOutput)) {
            return;
        }

        this.itemInFurnace = UtilItemStack.copy(itemOutput);
    }

    @Nullable
    public ItemStack getItemInFurnace() {
        if (Objects.isNull(this.itemInFurnace)) {
            return null;
        }
        return UtilItemStack.copy(itemInFurnace);
    }

    public void setItemInFurnace(final ItemStack itemInput) {
        if (Objects.nonNull(this.itemInFurnace)) {
            return;
        }

        this.itemInFurnace = UtilItemStack.copy(itemInput);
        this.itemInFurnace.stackSize = 1;
        this.timer = 0;
    }

    @Nullable
    public ItemStack popItemInFurnace() {
        if (Objects.isNull(this.itemInFurnace)) {
            return null;
        }
        final ItemStack item = UtilItemStack.copy(itemInFurnace);
        this.itemInFurnace = null;
        this.timer = 0;
        return item;
    }

    @Nullable
    public static ItemStack getFurnaceRecipes(ItemStack input) {
        final ItemStack item = FurnaceRecipes.smelting().getSmeltingResult(input);
        if (Objects.isNull(item) || !(item.getItem() instanceof ItemFood)) {
            return null;
        }
        return item;
    }
}
