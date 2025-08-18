# 第一个方块

与物品相同，我们继承原版的Block类，现在让我们创建我们的第一个方块。

```java
public class BlockExample extends Block {
    public BlockExample() {
        super(Material.wood); // (0)

        String name = "example_block";
        this.setCreativeTab(CreativeTabs.tabBlock); // (1)
        this.setBlockName(name); // (2)
        this.setBlockTextureName(ExampleMain.MODID + ":" + name);
    }
}
```

0. Block的构造参数需要参形Material。你可以直接使用原版的某个方块，就像这里使用了木板，也可以直接实现这个类定制自己的材质Material。
1. 方块需要设定Tab？事实上1.7.10中方块的注册会自动注册一个与之匹配的ItemBlock。稍后我们会实现一个自己的ItemBlock。
2. 这些名称的名称键名是tile起头的。请不要再把TileEntity翻译为瓷砖实体，这个叫方块实体。

然后注册这个方块，以你喜欢的形式。注意方块不需要Modid参数。

```java
GameRegistry.registerBlock(this, name);
```

现在资源目录的属性图如此：

```markdown
resources
├── assets
│   └── example_mod_id
│       ├── lang
│       │   ├── en_US.lang
│       │   ├── zh_CN.lang
│       └── textures
│           ├── items
│           │   └── example_item.png
│           └── blocks
│               └── example_block.png
└── mcmod.info
```

## 实现挖掘的工具与硬度

只需要在构造函数中实现 `setHarvestLevel` 方法即可。

```java
this.setHarvestLevel(textureName, hardness, metadata);
```

> texturesName指工具名称，可选工具为"axe", "pickaxe" 与 "shovel", 而后面的hardness则是挖掘等级。最后元数据指代方块元数据。

## 实现ItemBlock并绑定

在1.7.10中，ItemBlock需要在注册中与方块绑定，否则方块会获得自己默认的ItemBlock。**不可以单独注册ItemBlock！**这会直接抛出错误！

我更喜欢让这样的物品作为方块的内部类，这样看起来更干净，并且应该让这个物品不被到处滥用——如果必要（如使用自动构建式注册），可以使方块类和ItemBlock类为final防止this指针被滥用。

注册到方块，需要直接提供这个ItemBlock的class到第二位参数。

!!! note "final 的类无法被继承。"

```java title="BlockExample.java"
public static final BlockExample block = new BlockExample(); // (0)

public BlockExample() {
    super(Material.wood);

    String name = "example_block";
    this.setCreativeTab(CreativeTabs.tabBlock);
    this.setBlockName(name);
    this.setBlockTextureName(ExampleMain.MODID + ":" + name);

    GameRegistry.registerBlock(this, ItemBlockExample.class, name); // (1)
}

public static final class ItemBlockExample extends ItemBlock { // (2)
    public ItemBlockExample() {
        super(block); // (3)
    }
}
```

1. 静态化此方块到实例，使用时也可以从此拿取。
2. 注册在这!是的，这儿传入了整个class！
3. 在一个类文件里创建的默认类都是static的，而类中类则需要声明static，否则需要先实现上层类才能取得此类。
4. 虽然注册中与方块绑定，但是相关数据依然得在实例化时提供。

然而，如果你希望让物品的材质与方块不一样，还需要复写 `registerIcons` 来注册你的Icon，然后通过 `getIconFromDamage` 方法传入Icon。

BlockExample.java的完整代码：

```java title="BlockExample.java"
package club.someoneice.moddingsource.block;

import club.someoneice.moddingsource.ExampleMain;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.IIcon;

public class BlockExample extends Block {
    public static final BlockExample block = new BlockExample();

    public BlockExample() {
        super(Material.wood);

        String name = "example_block";
        this.setCreativeTab(CreativeTabs.tabBlock);
        this.setBlockName(name);
        this.setBlockTextureName(ExampleMain.MODID + ":" + name);

        GameRegistry.registerBlock(this, ItemBlockExample.class, name);
    }

    private static final class ItemBlockExample extends ItemBlock {
        @SideOnly(Side.CLIENT)
        private IIcon icon;

        public ItemBlockExample() {
            super(block);
        }

        @Override
        @SideOnly(Side.CLIENT)
        public void registerIcons(IIconRegister register) {
            this.icon = register.registerIcon(ExampleMain.MODID + ":example_block_item");
        }

        @Override
        @SideOnly(Side.CLIENT)
        public IIcon getIconFromDamage(int meta) {
            return this.icon;
        }
    }
}
```