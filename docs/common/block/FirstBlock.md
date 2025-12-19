# 第一个方块

这一章节我们将会创建我们在 1.7.10 模组开发中的第一个方块。

[**章节相关源码: 创建**](https://github.com/AmarokIce/NewbModding1710/blob/ModDev/src/main/java/club/snowlyicewolf/modding1710/common/block/BlockExample.java)  
[**章节相关源码: 注册**](https://github.com/AmarokIce/NewbModding1710/blob/ModDev/src/main/java/club/snowlyicewolf/modding1710/init/InitBlocks.java)

## 创建一个方块

现在读者应该已经完成了整个 `Item` 章节的学习，对于创建一个 `Block` 也应该能够猜到我们将要做的基本部分。让我们先来完成一些基本的内容，再来细细讨论 `Block` 的特性：

```java title="BlockExample.java"
public class BlockExample extends Block {
    protected BlockExample(Material material) {
        super(material);

        final String name = "example_block";

        this.setBlockName(name);
        this.setBlockTextureName(ModMain.ID + ":" + name);
        this.setCreativeTab(ModMain.TAB);

        InitBlocks.registerBlock(name, this);
    }
}
```

这一部分看起来和 `Item` 一样，除了 `Block` 的父类有一个 `Material`。等等，一个 `Material`？我们又要对 `enum` 打注入了吗？  
等等，先别逃跑！先来看实现！当我们打开 `Material` 类，可以看到这不仅不是一个 `enum`，而且它和 `Blocks` 或者 `Items` 一样拥有很多的实现。  
这个材质主要决定了当前方块在地图上将会渲染的颜色，方块是否能够被活塞推动，是否透明渲染等。虽然这些内容也可以透过覆写 `Block` 中的方法来处理，但是使用 `Material` 会更方便查阅。好吧，读者可以选择喜欢的方式解决它们。而且值得一提的是，Forge 也对 `Block` 注入了大量的方法，在开发时请多多阅读文档！

最后，别忘记在主类中加入注册！


## 设置方块的数据

默认情况下方块是没有硬度的，这太糟糕了。因此我们需要再设定一下：

```java title="BlockExample.java"
public BlockExample() {
    super(new Material(MapColor.yellowColor));

    final String name = "example_block";

    this.setBlockName(name);
    this.setBlockTextureName(ModMain.ID + ":" + name);
    this.setCreativeTab(ModMain.TAB);

    // 设定方块的硬度。
    this.setHardness(1.0F);
    // 设定方块的防爆抗性。
    this.setResistance(10.0F);
    // 设置光照登记，当前方块将会发出 15 级光照。
    this.setLightLevel(15.0F);

    InitBlocks.registerBlock(name, this);
}

@Override
public String getHarvestTool(int metadata) {
    // 使用镐子采集。
    return "pickaxe";
}
```

## 使方块拥有掉落物

```java title="BlockEntity.java"
@Override
public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
    return Lists.newArrayList(new ItemStack(this));
}
```
