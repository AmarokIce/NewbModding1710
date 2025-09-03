package club.snowlyicewolf.modding1710.common.item;

import club.snowlyicewolf.modding1710.ModMain;
import club.snowlyicewolf.modding1710.init.InitItems;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.List;

public class ItemExampleWithSubItem extends Item {
    @SideOnly(Side.CLIENT) private IIcon icon1;
    @SideOnly(Side.CLIENT) private IIcon icon2;
    @SideOnly(Side.CLIENT) private IIcon icon3;

    public ItemExampleWithSubItem() {
        final String name = "example_item_with_sub_item";
        this.setCreativeTab(ModMain.TAB);
        this.setTextureName(ModMain.ID + ":" + name);
        this.setUnlocalizedName(name);
        InitItems.registerItem(name, this);

        this.setHasSubtypes(true);
    }

    @Override
    public void getSubItems(Item self, CreativeTabs tab, List list) {
        for (int i = 0; i < 4; i++) {
            list.add(new ItemStack(self, 1, i));
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName(stack) + "_" + stack.getItemDamage();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister register) {
        super.registerIcons(register);

        icon1 = register.registerIcon(this.getIconString() + "_" + 1);
        icon2 = register.registerIcon(this.getIconString() + "_" + 2);
        icon3 = register.registerIcon(this.getIconString() + "_" + 3);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int meta) {
        switch (meta) {
            case 1: return this.icon1;
            case 2: return this.icon2;
            case 3: return this.icon3;
            case 0:
            default: return this.itemIcon;
        }
    }
}
