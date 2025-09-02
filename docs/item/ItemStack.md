# Item 与 ItemStack

在[重要概念](../core.md)中注册小节中提到过，物品是一个唯一对象。读者可能到现在都还没明白什么是唯一对象（或者都已经忘记它提到过这件事了），不过没关系，这一章节将会解释什么是唯一对象，以及关于 ItemStack 相关的事情。  
`ItemStack`，光看这个名字应该就能知道它与物品少不了关系。确实，它与物品的关系就差在了它是个“Stack（堆）”。

来看一个例子：

![](../assets/item/ItemStack_P0.png)

这里看起来有两种物品，一个是我们刚才注册的物品，另一个是苹果。而在游戏数据中，这里有四个 `ItemStack`。没错，它们或许数量相同，但依然是不同的 `ItemStack`。在玩家的背包中，每一个格子都代表着一个 `ItemStack`，当然，不包括空格子，在 1.7.10 中它们是 `null`。在扁平化之后的版本的这些空格子才从 `null` 变成了 `EMPTY`。不过现在，这些与我们无关。  
`Item` 仅记录一些数据与使用上的约定，它就像是接口，仅负责数据处理，而不负责记录数据。而 `ItemStack` 就是那个记录数据的对象，包括 NBT，耐久，数量... 而 `ItemStack` 通常会持有一个 `Item` 作为标记，这将会让游戏知道这个 `ItemStack` 是什么物品的堆。  
既然我们知道了 `Item` 不负责记录数据，那么为什么说 `Item` 是唯一对象就不难懂了。即便同一个 `Item` 类被反复注册，它们也会被视为不同的物品。

!!! failure  "警惕空指针危机!"
    1.7.10 中,创造模式物品栏里的物品都是 `Item` 而非 `ItemStack`。在特定情况(如新增 `Tooltip`)一定要判断 `ItemStack` 是否为 `null`！

那我们如何在代码中判断 ItemStack 属于什么物品? 只需要使用 ItemStack 包含的方法 `ItemStack#getItem` 就可以取得一个 `Item` 返回值。

## ItemStack 的 NBT 处理

当我们得到一个 ItemStack，我们可以直接通过 `ItemStack#getTagCompound` 获取它的 NBT。不过由于这个 NBT 可能为 `null`，因此获取之前最好进行判空处理。

我们创建一个苹果的 ItemStack 作为演示：

```java
final ItemStack stack = new ItemStack(Items.APPLE);
NBTTagCompound nbt;
if (Objects.isNull(nbt = stack.getTagCompound())) {
    nbt = new  NBTTagCompound();
    stack.setTagCompound(nbt);
}
```

## ItemStack 的拷贝

现在我们知道了物品是唯一对象，但 ItemStack 不是，那么在部分情况下我们需要操作 `ItemStack` 就需要进行拷贝。虽然 ItemStack 原生就有 `ItemStack#copy`，但出于某些原因，这个方法并不可靠。因此我们自己来创建一个拷贝的方法：

```java
public static ItemStack copy(final ItemStack stack) {
    final ItemStack copy = new  ItemStack(stack.getItem(), stack.stackSize, stack.getItemDamage());
    if (stack.hasTagCompound()) {
        // 这里一定也要拷贝，因为 NBTTagComponent 也是数据对象！
        copy.setTagCompound((NBTTagCompound) stack.getTagCompound().copy());
    }
    return copy;
}
```
