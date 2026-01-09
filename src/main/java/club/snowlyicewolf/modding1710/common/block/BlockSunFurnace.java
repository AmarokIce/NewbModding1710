package club.snowlyicewolf.modding1710.common.block;

import club.snowlyicewolf.modding1710.ModMain;
import club.snowlyicewolf.modding1710.common.tile.TileSunFurnace;
import club.snowlyicewolf.modding1710.init.InitBlocks;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.Objects;

public class BlockSunFurnace extends BlockContainer {
    public BlockSunFurnace() {
        super(Material.iron);

        final String name = "sun_furnace";

        this.setBlockName(name);
        this.setBlockTextureName(ModMain.ID + ":" + name);
        this.setCreativeTab(ModMain.TAB);

        this.setHardness(1.0F);

        InitBlocks.registerBlock(name, this);
    }

    @Override
    public boolean onBlockActivated(World pWorld, int pPosX, int pPosY, int pPosZ,
                                    EntityPlayer pUser, int pFace, float pHitX,
                                    float pHitY, float pHitZ) {
        super.onBlockActivated(pWorld, pPosX, pPosY, pPosZ, pUser, pFace, pHitX, pHitY, pHitZ);

        final TileEntity te = pWorld.getTileEntity(pPosX, pPosY, pPosZ);
        if (!(te instanceof TileSunFurnace)) {
            return false;
        }

        final TileSunFurnace tile = (TileSunFurnace) te;

        // 如果玩家手持物品且不是蹲下状态。
        if (Objects.nonNull(pUser.getHeldItem()) && !pUser.isSneaking()) {
            if (Objects.nonNull(tile.getItemInFurnace())) {
                return false;
            }

            // 因为方法内已经拷贝过了，因此我们不需要拷贝。否则此处应该拷贝 ItemStack。
            tile.setItemInFurnace(pUser.getHeldItem());
            pUser.getHeldItem().stackSize--;
            return true;
        }

        final ItemStack item = tile.popItemInFurnace();
        if (Objects.isNull(item)) {
            return true;
        }

        // 生成一个物品，重置可捡起的时间 CD，然后生成到世界。
        final EntityItem entityItem = new EntityItem(pWorld, pPosX, pPosY + 0.6, pPosZ, item);
        entityItem.delayBeforeCanPickup = 0;
        pWorld.spawnEntityInWorld(entityItem);
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World pWorld, int pMeta) {
        return new TileSunFurnace(pWorld, pMeta);
    }
}
