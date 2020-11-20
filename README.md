## jmap对象实例对比工具

这是一个简单的用于分析‘内存泄露’的小工具

## 实现原理
1. 分析fgc前后的类实例对象分布变化来辅助判断
2. 如果多次fgc后，一个类的对象实例没有发生变化或者越来越多，则需要重点排查

## 操作步骤
1. jmap -histo pid >> preGCHisto.log
    - 打印gc之前的内存对象实例分布
2. jmap -histo:live pid >> postGCHisto.log
    - 打印gc之后的内存对象实例分布
3. 使用HistoAnalysisTool
    - config.properties分别指定fgc前后的histo日志，关注的业务类包名等
4. 工具生成三个文件，按照gc回收的百分比排序，越靠前越需要注意
    - diff_all，所有的类对象对比
    - diff_biz，主要是业务的类对象对比（传入过滤的类名即可）
    - diff_third，主要是三方的类对象对比
5. 可打印多天的histo，用来做对比分析
    - 如果每天某个对象的实例数目都一直在增加，则内存泄露几率较高
    
## 其他注意事项
1. 目前histo的默认读取路径是src/main/resources
2. 默认的config.properties的读取路径也是在src/main/resources
3. 生成的diff文件是在gen目录
4. 目前工具是需要将源代码导入IDE执行
5. 目前工程中的histo可直接使用测试

## 更新
- 2018.1.16 增加阈值过滤，重点关注如对象实例比较大的，如超过10k的对象实例

