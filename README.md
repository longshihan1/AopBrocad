# AOPBrocad


适用于API 15+(Android 4.0.3)。
### 简介
这是一款非常简单的实现本地广播等的依赖注入框架。

### 注意
本地广播的方法要求public实现。
内部获取反射对象使用仿Butterknife实现。

### 使用介绍
- gradle 
```java {.class1 .class} 
    allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}


```


- 使用方法

本地广播的依赖注入，直接添加方法获取到数据

```java {.class1 .class} 
  unbinder = BroadcastBindInjector.injectBrocast(this);
  @LocalBind({"123", "345","4586"})
  public void getBroadcastService(Intent intent) {
        Log.d("打印Action", intent.getAction());
        switch (intent.getAction()){
            case "123":
                break;
            case "345":
                break;
            case "4586":
                break;

        }
    }

        @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

```

ID视图的依赖注入
```java {.class1 .class} 

 @Bind(R.id.maintext)
 TextView maintext;

  ViewInjector.injectView(this);

```
