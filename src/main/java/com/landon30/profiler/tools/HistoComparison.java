
package com.landon30.profiler.tools;

import java.text.DecimalFormat;

/**
 * histo对比结果
 *
 * @date 2019-01-14
 * @author landon30
 */
public class HistoComparison {
    public String className;

    // gc的数目
    public long gcCount;
    // gc的百分比
    public float gcPercent;

    public String instanceDiff;
    public String numDiff;
    public String bytesDiff;

    public HistoComparison(Histo preHisto, Histo postHisto) {
        this.className = preHisto.getClassName();

        this.numDiff = postHisto.getNum() + "/" + preHisto.getNum();
        this.bytesDiff = postHisto.getBytes() + "/" + preHisto.getBytes();
        this.instanceDiff = postHisto.getInstances() + "/" + preHisto.getInstances();

        this.gcCount = preHisto.getInstances() - postHisto.getInstances();
        this.gcPercent = gcCount * 1.0f / preHisto.getInstances();
    }

    @Override
    public String toString() {
        DecimalFormat format = new DecimalFormat("0.00");
        return "HistoComparison [className=" + className + ", gcCount=" + gcCount + ", gcPercent="
                + format.format(gcPercent) + ", instanceDiff=" + instanceDiff + ", numDiff="
                + numDiff + ", bytesDiff=" + bytesDiff + "]";
    }
}
