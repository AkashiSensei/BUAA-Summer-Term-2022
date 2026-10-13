# 圆圈骑士（Circle Knight）版本与恢复记录

## 名称依据

原始开始菜单 `img/Start_Menu.jpg` 显示「CIRCLE KNIGHT / 圆圈骑士」，窗口标题也使用 Circle Knight。对外名称统一为「圆圈骑士（Circle Knight）」，构建产物为 `CircleKnight.jar`；历史目录中的 Little Knight 及源码角色类 `LittleKnight` 保留原名。

## 版本依据

归档基准为 SummerTerm 的「项目源码文件夹 / Little Knight 08161430」。13 个源码文件与「SummerTerm提交版本.zip」及「Little Knight08222150.zip」逐字节一致；目录名中的 08161430 不是可靠的版本日期。

顶层「Little Knight」为早期原型；顶层「Little Knight 08161430」与提交版有 5 个源码文件不同，涉及初始金币 1000、15 个怪物、灵魂速度、攻击状态及日志调整。该版本的源码和差异文件保留在本地，没有合并进归档基准。

`SOURCE_SHA256.json` 记录本仓库 13 个原始源码文件的 SHA-256，恢复及发布整理均未修改这些文件。

## 2026-10-13 恢复验证

- 原 JAR 使用系统 Java 8 启动时出现 UnsupportedClassVersionError；其字节码版本为 58，而 Java 8 运行时仅支持到 52。
- 源码使用 switch 箭头规则。使用 Java 21 的 `javac --release 14 -encoding UTF-8` 原样编译全部源码成功。
- 重新打包，保留入口 `Windows.StartMenu`，输出 Java 14 字节码。
- macOS 图形会话日志确认菜单初始化、CrossRoad 地图加载、10 个怪物生成、组件添加及刷新线程启动。
- 69 张源码引用图片均存在，且通过 Swing ImageIcon 解码，尺寸均为正。
- 作者实际游玩，并确认与印象中的原版本完全一致；README 中四张截图由作者在此次运行时提供。

以上属于恢复验证和作者体验确认，不等同于所有难度、地图切换、死亡复活及通关流程的系统回归测试。

## 发布整理

- 保留 13 个源码文件和 69 张运行必需图片；原材料没有改动。
- 增加跨平台 Python 构建/运行工具、macOS 启动入口、README、四张截图和文字开发文档。
- PSD、未使用素材、原始 Word 文档、构思记录、原 JAR 与另一版源码继续保留在本地，不进入 Git 历史。
- 未更换系统默认 Java，也未安装新的 JDK。

## 保留的早期实现

原文档说明怪物主动攻击尚未完成；商店、武器、经验系统仍属于构思，不能作为成品功能介绍。程序保留多个线程直接更新 Swing 组件和持续刷新循环；固定尺寸菜单、字体回退及线程模型可作为后续维护方向，本次没有大范围重构。
