# 服务端上的逻辑
有时我们可能需要调试某些东西，或者处于某些目的我们需要新增一些方便的方式完成，那么指令就是我们好的选择。<br />
当然，也可以处于单纯想加一个指令。因此让我们开始吧。<br />
首先需要知道一点：指令是纯服务端的，因此内容无需判断是否在服务端执行，也不应该直接调用客户端的代码！如果有必要，请使用发包！

# 创建CommandTree
// TODO

# 注册服务端指令
在`FMLServerStartingEvent`或`FMLServerStartedEvent`中注册。注意：这两个Event都属于主线Event，需要使用注解`@Mod.EventHandle`