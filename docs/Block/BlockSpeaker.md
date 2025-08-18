# 方块交互

这一章节我们要做的很简单，就是让一个方块可以对我们说Hello World！

那么我们先创建一个方块`BlockHello.java`，现在让我们完成这个目标：

```java title="BlockHello.java"
@Override
public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
    
    return true;
}
```

哇！这么一长串参数值！什么是什么阿！

别急！我们一个一个来：
```
World: 方块发生的世界
x, y, z: 方块的坐标，获取方块请使用world.getBlock(x, y, z);
EntityPlayer: 使用方块的玩家
side: 方块被触击的面
hitX, hitY, hitZ: 方块被触击的面位置（16*16*16）
```

我们需要向玩家发送Hello World！,因此我们只需要使用到player参数即可：

```java
@Override
public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
    player.addChatMessage(new ChatComponentText("Hello World!"));
    return true;
}
```

很简单，不是吗？