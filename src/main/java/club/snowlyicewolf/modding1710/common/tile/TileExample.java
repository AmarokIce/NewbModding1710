package club.snowlyicewolf.modding1710.common.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TileExample extends TileEntity {
    private int count = 0;

    public TileExample(final World world, final int meta) {
        this.setWorldObj(world);
        this.blockMetadata = meta;
    }

    // 这里用于存储数据
    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setInteger("count", count);
    }

    // 这里用于读取数据
    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        count = nbt.getInteger("count");
    }

    public int getCount() {
        return count;
    }

    public void addCount() {
        count++;
    }

    public void modifyCount() {
        // 为了不让 count 小于 0，我们需要限制底线。
        count = Math.max(count - 1, 0);
    }
}
