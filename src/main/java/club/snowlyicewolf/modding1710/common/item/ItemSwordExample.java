package club.snowlyicewolf.modding1710.common.item;

import club.snowlyicewolf.modding1710.ModMain;
import club.snowlyicewolf.modding1710.init.InitItems;
import club.snowlyicewolf.modding1710.init.InitMaterials;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class ItemSwordExample extends ItemSword {
    public ItemSwordExample() {
        super(InitMaterials.TOOL_SCRAP);

        final String name = "example_sword";
        this.setUnlocalizedName(name);
        this.setTextureName(ModMain.ID + ":" + name);
        this.setCreativeTab(ModMain.TAB);
        InitItems.registerItem(name, this);
    }

    @Override
    public boolean hitEntity(ItemStack pStack, EntityLivingBase pUser, EntityLivingBase pTarget) {
        if (pTarget instanceof EntityPlayer && ((EntityPlayer) pTarget).capabilities.isCreativeMode) {
            return false;
        }
        pTarget.addPotionEffect(new PotionEffect(Potion.poison.getId(), 20 * 30, 0));
        return hitEntity(pStack, pUser, pTarget);
    }
}
