# 工作中方块

上一章节中我们学习了如何创建一个方块实体并注册它，这一章节我们将要学习如何制作一个会工作的方块实体。

[**章节相关源码: 方块实体**](https://github.com/AmarokIce/NewbModding1710/blob/ModDev/src/main/java/club/snowlyicewolf/modding1710/common/tile/TileSunFurnace.java)  

## 使方块更新

首先我们需要确保更新是被允许的，在方块实体中拥有一个方法控制它：`TileEntity::canUpdate`。这个方法的返回值是一个 `boolean` 并且默认返回 `true`。  
对于任何读者不希望它更新的情况下，都应该使得这个方法返回 `false` 以避免它被登记到世界的更新列表中。通常这个方法应该返回固定值，虽然理论来讲它是可变的，但是不建议这么做——不要相信 1.7.10 的神秘逻辑是 1.7.10 开发者必要的开发素质。  
换句话说，如果读者创建这个方块实体只是为了记录 NBT，那么就不需要方块不停的更新。  
当 `TileEntity::canUpdate` 返回 `true` 时，方法 `TileEntity::updateEntity` 将会在每个 Tick 被更新。因此我们要做的就是覆写 `TileEntity::updateEntity` 方法。

## 实战：创建一个日光炉

最好的学习途径就是实战。在读者学习创建一个会工作的方块实体时会遇到很多概念，没有什么比实战更好的去理解这些概念了！那么我们的实战目标是：创建一个日光炉。

如果我们想要得到一个类似熔炉的方块，就需要使得方块更新。不过由于直至目前，读者还没有开始学习编写 GUI，我们的目标就不会是复刻一个熔炉，因此我们的功能应该就是可以单纯的放入物品和取出物品。  
日光炉将会在白天的时候烹饪内部存储的物品。那么我们分析一下需求，我们需要...

- 确定何时保持更新；
- 确定并缓存食物的熔炼配方；
- 同步数据到客户端以便于更新动画与渲染物品等。

既然我们确定了需求，那么就开始开发吧！首先要做的就是确定何时更新，既然我们叫日光炉，那肯定得在主世界的白天更新。我们需要确定当前方块实体在主世界，并且是白天的。

```java title="TileSunFurnace.java"
@Override
public boolean canUpdate() {
    return this.worldObj.isRemote
        && this.worldObj.getWorldInfo().getVanillaDimension() == 0;
}

@Override
public void updateEntity() {
    final boolean isRaining = this.worldObj.isRaining() || this.worldObj.isThundering();
    final boolean isDaytime = this.worldObj.isDaytime();
    if (isRaining || !isDaytime) {
        return;
    }
}
```

??? question 为什么我们不在客户端更新？
    通常我们的数据需要在逻辑服务端工作，请把这一点记作一种常识并在开发时经常想起：你的大部分数据都应该在逻辑服务端处理与验证，而不是留给客户端。  
    必要时，我们通过封包联络客户端。如果没有必要，务必不要直接在客户端执行它们！

下一步，存储一样物品，显然，我们需要处理一下 `ItemStack`。

```java title="TileSunFurnace.java"
@Nullable private ItemStack itemInFurnace;
public TileSunFurnace(final World world, final int meta) {
    this.setWorldObj(world);
    this.blockMetadata = meta;
}

@Override
public void writeToNBT(NBTTagCompound nbt) {
    super.writeToNBT(nbt);
    if (Objects.isNull(itemInFurnace)) {
        return;
    }
    nbt.setTag("itemInFurnace",
        this.itemInFurnace.writeToNBT(new NBTTagCompound()));
}

@Override
public void readFromNBT(NBTTagCompound nbt) {
    super.readFromNBT(nbt);
    if (!nbt.hasKey("itemInFurnace")) {
        return;
    }
    this.itemInFurnace =
        ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("itemInFurnace"));
}

@Nullable
public ItemStack getItemInFurnace() {
    if (Objects.isNull(this.itemInFurnace)) {
        return null;
    }
    return UtilItemStack.copy(itemInFurnace);
}

public void setItemInFurnace(final ItemStack itemInput) {
    if (Objects.nonNull(this.itemInFurnace)) {
        return;
    }
    this.itemInFurnace = UtilItemStack.copy(itemInput);
    this.itemInFurnace.stackSize = 1;
}

@Nullable
public ItemStack popItemInFurnace() {
    if (Objects.isNull(this.itemInFurnace)) {
        return null;
    }
    final ItemStack item = UtilItemStack.copy(itemInFurnace);
    this.itemInFurnace = null;
    return item;
}
```

??? 为什么 getItemStack 需要拷贝？
    任何需要操作物品的情况都不应该直接干涉容器内的物品，大部分情况下这是不必要的——如果有这样的需要，我们应该创建单独的方法去处理它们。  
    在任何 Java 开发中都应该做到避免让其他对象直接访问当前对象的成员，因为外部对象的操作都是不可控且未知的，尤其是对于 1.7.10 这样已经非常黑暗的版本。

现在，我们完成了物品存取的部分，接下来该确定烹饪食谱了！当然，如果我们需要得到一些熔炉熔炼的合成，就得从 `FurnaceRecipes` 提取。直至目前读者还没有开始学习添加食谱，因此对这个类可能较为陌生。但放轻松，这不会很难处理。  
顺带一提，我们创建的是日光炉，这意味着我们的工作方块的目标应该是烤熟食物而不是取代熔炉，因此我们需要检查产出目标是食物。

```java title="TileSunFurnace.java"
@Nullable
public static ItemStack getFurnaceRecipes(ItemStack input) {
    final ItemStack item = FurnaceRecipes.smelting().getSmeltingResult(input);
    if (Objects.isNull(item) || !(item.getItem() instanceof ItemFood)) {
        return null;
    }
    return item;
}
```

是时候补全 `updateEntity` 了！现在我们设定我们的熔炼时间为 600 Ticks，并且新增一个计数器变量。这个变量需要存储到 NBT，同时会在物品变更的时候清空。

```java title="TileSunFurnace.java"
public class TileSunFurnace extends TileEntity {
    public static final int MAX_SMELTING_TIME = 600;

    @Nullable private ItemStack itemInFurnace;
    private int timer = 0;

    public TileSunFurnace(final World world, final int meta) {
        this.setWorldObj(world);
        this.blockMetadata = meta;
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        if (Objects.isNull(itemInFurnace)) {
            return;
        }
        nbt.setTag("itemInFurnace",
            this.itemInFurnace.writeToNBT(new NBTTagCompound()));
        nbt.setInteger("timer", timer);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        if (!nbt.hasKey("itemInFurnace")) {
            return;
        }
        this.itemInFurnace =
            ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("itemInFurnace"));
        this.timer = nbt.getInteger("timer");
    }

    @Override
    public boolean canUpdate() {
        return this.worldObj.isRemote
            && this.worldObj.getWorldInfo().getVanillaDimension() == 0;
    }

    @Override
    public void updateEntity() {
        final boolean isRaining = this.worldObj.isRaining() || this.worldObj.isThundering();
        final boolean isDaytime = this.worldObj.isDaytime();
        if (isRaining || !isDaytime) {
            return;
        }

        if (Objects.isNull(this.itemInFurnace)) {
            return;
        }

        timer++;
        if (timer < MAX_SMELTING_TIME) {
            return;
        }

        timer = 0;
        final ItemStack itemOutput = getFurnaceRecipes(this.itemInFurnace);
        if (Objects.isNull(itemOutput)) {
            return;
        }

        this.itemInFurnace = UtilItemStack.copy(itemOutput);
    }

    @Nullable
    public ItemStack getItemInFurnace() {
        if (Objects.isNull(this.itemInFurnace)) {
            return null;
        }
        return UtilItemStack.copy(itemInFurnace);
    }

    public void setItemInFurnace(final ItemStack itemInput) {
        if (Objects.nonNull(this.itemInFurnace)) {
            return;
        }

        this.itemInFurnace = UtilItemStack.copy(itemInput);
        this.itemInFurnace.stackSize = 1;
        this.timer = 0;
    }

    @Nullable
    public ItemStack popItemInFurnace() {
        if (Objects.isNull(this.itemInFurnace)) {
            return null;
        }
        final ItemStack item = UtilItemStack.copy(itemInFurnace);
        this.itemInFurnace = null;
        this.timer = 0;
        return item;
    }

    @Nullable
    public static ItemStack getFurnaceRecipes(ItemStack input) {
        final ItemStack item = FurnaceRecipes.smelting().getSmeltingResult(input);
        if (Objects.isNull(item) || !(item.getItem() instanceof ItemFood)) {
            return null;
        }
        return item;
    }
}
```

我们完成了方块实体的部分，现在再来完善方块部分。细节就不在过多赘述，读者可以尝试通过注解来理解具体要做的内容：

// TODO
```

```
