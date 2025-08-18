package club.snowlyicewolf.modding1710.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public final class UtilItemStack {
    private UtilItemStack() {}

    public static ItemStack copy(final ItemStack stack) {
        final ItemStack copy = new  ItemStack(stack.getItem(), stack.stackSize, stack.getItemDamage());
        if (stack.hasTagCompound()) {
            copy.setTagCompound((NBTTagCompound) stack.getTagCompound().copy());
        }
        return copy;
    }
}
