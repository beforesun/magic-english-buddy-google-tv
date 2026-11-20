# Google TV 侧载版

本仓库基于 [xckevin/magic-english-buddy](https://github.com/xckevin/magic-english-buddy) 改造。原项目和本改造遵循仓库中的 CC BY-NC-SA 4.0 许可，仅供非商业用途。

## 下载与安装

打开本仓库的 **Actions → Build Google TV APK → 最近一次成功运行 → Artifacts**，下载 `magic-english-buddy-google-tv-debug-apk`，解压得到 `app-debug.apk`。把 APK 传到 Google TV 盒子，在盒子上允许文件管理器安装未知来源应用，然后打开 APK 安装。安装后可从电视应用列表启动。

APK 内置网页应用和课程资源，不依赖 GitHub Pages 地址。首次启动后，学习记录保存在盒子的应用数据中。课程音频仍按原项目的设置流程下载，下载需要联网。

## 操作与限制

- 方向键移动焦点，确认键选择，返回键返回上一页。
- 电视版为首个适配版本，遥控器焦点通过 WebView 辅助脚本处理；部分题型、长按查词、文件备份与打印尚未在真实盒子上验证。
- 跟读录音需要盒子识别外接麦克风，并授予录音权限。没有麦克风时仍可阅读、听音和答题。
- 已在 Chromecast with Google TV（Sabrina）上验证：普通录音只得到静音，遥控器语音键会启动 Google 助手，无法作为本应用的录音键。此盒子要使用跟读录音与回放，需要系统可供第三方应用使用的麦克风输入。
- APK 是 debug 签名，适合个人侧载；卸载应用会清除盒子上的本地学习记录，请先在应用内导出备份。

## 本地构建

需要 Node.js 22、pnpm、JDK 17、Android SDK 35 和 Gradle 8.11.1：

```sh
pnpm install --frozen-lockfile
pnpm build
mkdir -p android-tv/app/src/main/assets
cp -R dist/. android-tv/app/src/main/assets/
cd android-tv
gradle assembleDebug
```

输出：`android-tv/app/build/outputs/apk/debug/app-debug.apk`。
