package club.snowlyicewolf.modding1710.common.item;

import club.snowlyicewolf.modding1710.ModMain;
import club.snowlyicewolf.modding1710.init.InitItems;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentTranslation;

import javax.annotation.CheckForNull;
import java.util.List;

public class ItemToolExample extends Item {
    public ItemToolExample() {
        this.setMaxStackSize(1);
        this.setMaxDamage(100);

        final String name = "example_tool";
        this.setUnlocalizedName(name);
        this.setTextureName(ModMain.ID + ":" + name);
        this.setCreativeTab(ModMain.TAB);
        InitItems.registerItem(name, this);
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack pStack, EntityPlayer pPlayer, EntityLivingBase pEntity) {
        // 如果不是僵尸就直接取消方法。
        if (!(pEntity instanceof EntityZombie)) {
            return false;
        }

        // 在僵尸所在位置生成头颅掉落物。僵尸头颅的元数据为 2。
        final ItemStack head = new ItemStack(Blocks.skull, 1, 2);
        final EntityItem itemEntity = new EntityItem(pEntity.worldObj, pEntity.posX, pEntity.posY, pEntity.posZ, head);
        pEntity.worldObj.spawnEntityInWorld(itemEntity);

        // 使僵尸死亡。
        pEntity.setDead();
        // 通知实体已经死亡，同步状态。
        pEntity.worldObj.setEntityState(pEntity, (byte) 3);

        // 发出剪刀的声音。
        pEntity.playSound("mob.sheep.shear", 1.0F, 1.0F);
        // 损耗工具
        pStack.damageItem(1, pEntity);
        return true;
    }

    // 参数位分别代表当前物品的 ItemStack（可能是空的）,检查物品的玩家，介绍列表，是否显示高级数据（F3 + H）
    @Override
    public void addInformation(@CheckForNull ItemStack pStack, EntityPlayer pPlayer,
                               List pTooltipInfos, boolean pFlag) {
        pTooltipInfos.add(new ChatComponentTranslation("item.exampletool.info")
            .getFormattedText());
    }
}
