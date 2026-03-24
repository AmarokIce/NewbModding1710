# 文本组件

在物品和方块中，我们已经了解且实战过使用本地化文本了。没有印象了吗？好吧，因为我们使用的内容被称之为 `Unlocalized 文本（未本地化文本）`。显然，这些文本不会自己神奇的变成 `lang` 文件中的文本。

我们有三种方式处理文本：`ChatComponentTranslation`，`ChatComponentText`，`I18N`。

## ChatComponentText

`ChatComponentText` 负责提供原样的文本，也就是我们常说的 “硬编码”。`ChatComponentText` 无论使用何种方式或格式化输出，都只有原文。

`ChatComponentText` 无法使用格式化文本占位符，但是因为实现了 `IChatComponent`，`ChatComponentText` 可以使用 `append` 类型的方法，追加额外的文本。

`appendText` 将会追加一个硬编码文本，而 `appendSibling` 可以提供另一个实现了 `IChatComponent` 的对象。任何 `IChatComponent` 都可以设置文本格式。

```java

new ChatComponentText("Hello world! I'm ChatComponentText!") => "Hello world! I'm ChatComponentText!"

```

## ChatComponentTranslation

`ChatComponentTranslation` 将会提供 `lang` 翻译后的文本，并且可以使用文本占位符。与 `ChatComponentText` 一样，实现了 `IChatComponent`。

```lang
message.oneofname.mymodid=Hello world! I'm %s!
```

```java

new ChatComponentTranslation("message.oneofname.mymodid", "ChatComponentTranslation") => "Hello world! I'm ChatComponentTranslation!"

```

## I18N

当然，I18N 是一个缩写，全称是 `Internationalization (国际化)`。`I18n` 类内的方法 `format` 将会尝试格式化后提供翻译后的文本，这与 `ChatComponentTranslation` 中使用 `getFormattedText` 得到的结果是相同的。

I18N 不是很常用，通常推荐使用 `ChatComponentTranslation` 避免不必要的麻烦。但对于那些小型辅助性的模组，创建一个额外对象是不必要的，那么使用 I18N 获取文本结果是很好的方式。
