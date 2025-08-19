package club.snowlyicewolf.modding1710.common.item;

import club.snowlyicewolf.modding1710.ModMain;
import club.snowlyicewolf.modding1710.init.InitItems;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

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

        // 使僵尸死亡。
        pEntity.setDead();
        // 在僵尸所在位置掉落僵尸头颅，僵尸头颅的元数据为 2。
        pEntity.entityDropItem(new ItemStack(Blocks.skull, 1, 2), 0.0f);
        // 发出剪刀的声音。
        pEntity.playSound("mob.sheep.shear", 1.0F, 1.0F);
        // 损耗工具
        pStack.damageItem(1, pEntity);
        return true;
    }
}
