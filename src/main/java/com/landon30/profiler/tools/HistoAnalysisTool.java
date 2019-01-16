package com.landon30.profiler.tools;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

/**
 * histo工具分析
 *
 * @date 2019-01-14
 * @author landon30
 */
public class HistoAnalysisTool {
    public static void main(String[] args) throws Exception {
        System.out.println("HistoAnalysisTool.start");

        // 读取配置文件
        InputStream in = new BufferedInputStream(
                new FileInputStream("src/main/resources/config.properties"));
        Properties p = new Properties();
        p.load(in);

        // 解析参数
        String preGCHistoLogName = p.getProperty("preGCHistoName");
        String postGCHistoLogName = p.getProperty("postGCHistoName");
        int diffNum = Integer.parseInt(p.getProperty("diffNum"));
        String bizClazzName = p.getProperty("bizClazzName");

        // 生成结果
        Map<Float, HistoComparison> diffMap = compareGCHisto(diffNum, preGCHistoLogName,
                postGCHistoLogName);
        // 生成三个文件，全对比文件，只包括com.playcrab的类实例对比，非com.playcrab的类实例对比

        PrintWriter pw1 = new PrintWriter("gen/diff_all.log");
        diffMap.forEach((k, v) -> pw1.println(v));
        pw1.close();

        PrintWriter pw2 = new PrintWriter("gen/diff_biz.log");
        diffMap.values().stream().filter(v -> v.className.contains(bizClazzName))
                .forEach(v -> pw2.println(v));
        pw2.close();

        PrintWriter pw3 = new PrintWriter("gen/diff_third.log");
        diffMap.values().stream().filter(v -> !v.className.contains(bizClazzName))
                .forEach(v -> pw3.println(v));
        pw3.close();

        PrintWriter pw4 = new PrintWriter("gen/diff_threshold.log");
        diffMap.values().stream().filter(v -> v.getPostGCInstances() >= instanceThreshold)
                .forEach(v -> pw4.println(v));
        pw4.close();

        System.out.println("HistoAnalysisTool.end");
    }

    /**
     * 对比histo，按照回收百分比排序
     * 
     * @param n
     * @param preGCHistoLog
     * @param postGCHistoLog
     * @return
     * @throws Exception
     */
    public static Map<Float, HistoComparison> compareGCHisto(int n, String preGCHistoLog,
            String postGCHistoLog) throws Exception {
        Map<Long, Histo> preGCHistoMap = getHistoMap(preGCHistoLog);
        Map<String, Histo> postGCClassNameHistoMap = convert2NameMap(getHistoMap(postGCHistoLog));

        Map<Float, HistoComparison> diffMap = new TreeMap<>();

        for (long i = 1; i <= n; i++) {
            Histo preGCHisto = preGCHistoMap.get(i);

            Histo postGCHisto = postGCClassNameHistoMap.get(preGCHisto.getClassName());
            if (postGCHisto == null) {
                continue;
            }

            // 比较一下实例数目或者其他算法
            HistoComparison diff = new HistoComparison(preGCHisto, postGCHisto);
            diffMap.put(diff.gcPercent, diff);
        }

        return diffMap;
    }

    /**
     * 按照jmap -histo的格式读取文件
     * 
     * @param histoName
     * @return
     * @throws Exception
     */
    private static Map<Long, Histo> getHistoMap(String histoName) throws Exception {
        Path histoPath = Paths.get("src/main/resources", histoName);
        List<String> histoLines = Files.readAllLines(histoPath, Charset.forName("UTF-8"));

        Map<Long, Histo> histoMap = new TreeMap<>();

        // 跳过最后一行(Total ...)
        for (int i = 3; i < histoLines.size() - 1; i++) {
            Histo histo = parse(histoLines.get(i));
            histoMap.put(histo.getNum(), histo);
        }

        return histoMap;
    }

    /**
     * 解析具体的每一条数据
     * 
     * @param line
     * @return
     */
    private static Histo parse(String line) {
        String strNum = line.substring(0, 4);
        long longNum = Long.parseLong(strNum.trim());

        String instances = line.substring(5, 19);
        long longInstances = Long.parseLong(instances.trim());

        String bytes = line.substring(19, 34);
        long longBytes = Long.parseLong(bytes.trim());

        // 去掉分号(某些有;)
        String className = line.substring(36, line.length());
        if (className.endsWith(";")) {
            className = line.substring(36, line.length() - 1);
        }

        return new Histo.HistoBuilder().num(longNum).instances(longInstances).bytes(longBytes)
                .className(className).build();
    }

    // 转换为key为classname的map
    private static Map<String, Histo> convert2NameMap(Map<Long, Histo> histoMap) {
        Map<String, Histo> nameHistoMap = new HashMap<>();
        histoMap.forEach((k, v) -> nameHistoMap.put(v.getClassName(), v));
        return nameHistoMap;
    }
}
