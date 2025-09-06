package club.snowlyicewolf.modding1710.common.block;

import club.snowlyicewolf.modding1710.ModMain;
import club.snowlyicewolf.modding1710.common.tile.TileExample;
import club.snowlyicewolf.modding1710.init.InitBlocks;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

public class BlockExampleWithTile extends BlockContainer {
    public BlockExampleWithTile() {
        super(Material.rock);

        final String name = "example_tile_block";

        this.setBlockName(name);
        this.setBlockTextureName(ModMain.ID + ":" + name);
        this.setCreativeTab(ModMain.TAB);

        this.setHardness(2.0F);

        InitBlocks.registerBlock(name, this);
    }

    @Override
    public TileEntity createNewTileEntity(World pWorld, int pMeta) {
        return new TileExample(pWorld, pMeta);
    }

    // 这里用于处理玩家右键方块时将要做的。
    @Override
    public boolean onBlockActivated(World pWorld, int pPosX, int pPosY, int pPosZ,
                                    EntityPlayer pUser, int pFace, float pHitX,
                                    float pHitY, float pHitZ) {
        // 检查是否为服务端。当 isRemote 为 True 时为客户端，因此提前返回。
        if (pWorld.isRemote) {
            return true;
        }

        // 检查 TileEntity 是否为我们需要的，为了避免任何意外。每次采取 TileEntity 时都应该注意检查。
        final TileEntity tileEntity = pWorld.getTileEntity(pPosX, pPosY, pPosZ);
        if (!(tileEntity instanceof TileExample)) {
            return false;
        }

        // 投射过去。
        final TileExample tile = (TileExample) tileEntity;
        tile.addCount();

        // 输出我们的消息
        pUser.addChatMessage(new ChatComponentText(tile.getCount() + ""));

        return true;
    }

    // 这里用于处理玩家左键方块时将要做的。
    @Override
    public void onBlockClicked(World pWorld, int pPosX, int pPosY, int pPosZ, EntityPlayer pUser) {
        if (pWorld.isRemote) {
            return;
        }

        final TileEntity tileEntity = pWorld.getTileEntity(pPosX, pPosY, pPosZ);
        if (!(tileEntity instanceof TileExample)) {
            return;
        }

        final TileExample tile = (TileExample) tileEntity;
        tile.modifyCount();
        pUser.addChatMessage(new ChatComponentText(tile.getCount() + ""));
    }
}
