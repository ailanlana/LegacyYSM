# Yes Steve Model

本分支为模组提供了 Minecraft 1.7.10 支持，内容会定期与 1.12.2 同步。

此自述文档仅补充 1.7.10 分支特有的内容，其余许可证等内容参照 1.12.2 分支自述文档。

## 状况

### 兼容性

模组需要 [UniMixins](https://github.com/LegacyModdingMC/UniMixins) 作为前置。

- 模型
  - 兼容 1.1.6 及以下版本三种格式的模型
  - 对 1.2.0 的非加密模型存在兼容问题
  - 暂不兼容 2.2.1 及以上版本文件夹和 ZIP 格式的模型
  - 永远不会兼容 1.2.0 及以上版本 YSM 加密格式的模型

- 模组
  - 兼容 [Backhand](https://github.com/GTNewHorizons/Backhand) 副手渲染
  - 兼容 [Aqua Acrobatics Legacy](https://github.com/PufferTeam-ModArchive/aquaacrobatics) 的游泳动作
  - 兼容 [Modern KeyBinding](https://github.com/Nova-Committee/ModernKeyBinding) 和 [Controlling GTNH](https://github.com/GTNewHorizons/Controlling) 注册组合键

### 已知问题

除 1.12.2 已知问题以外，还有如下问题。

- 模型预览界面床未渲染
- 副手挥动和主手有差别
- 骑乘 Y 偏移与新版不一致
- 手持物品位置与新版不一致
- 鞘翅兼容没做完
- 原版玩家模型 UV 乱
