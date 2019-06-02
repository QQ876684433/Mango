## 返回码响应模块

### 文件结构

`fileTrans` : 提供文件类型 `content-Type` 对应的文件解析服务，提供的文件类型包含

- text / plain : 普通文本文件
- image / png :  `.png ` 文件
- audio / mp3 :  `.mp3` 文件
- video / mp4: `.mp4` 文件

`statusReceiver` : 提供不同返回码对应的UI响应内容



#### 调用方式

```
StatusHandler.handle(HttpResponse response)
```

将会为UI 客户端返回对应的一个 `map` 对象