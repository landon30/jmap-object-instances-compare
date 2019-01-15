package com.landon30.profiler.tools;

/**
 * Histo对象，和jmap -histo pid中的一条数据一一对应
 *
 * @date 2019-01-14
 * @author landon30
 */
public class Histo {
    private long num;
    private long instances;
    private long bytes;
    private String className;

    private Histo(HistoBuilder builder) {
        this.num = builder.num;
        this.instances = builder.instances;
        this.bytes = builder.bytes;
        this.className = builder.className;
    }

    public static class HistoBuilder {
        private long num;
        private long instances;
        private long bytes;
        private String className;

        public HistoBuilder num(long num) {
            this.num = num;
            return this;
        }

        public HistoBuilder instances(long instances) {
            this.instances = instances;
            return this;
        }

        public HistoBuilder bytes(long bytes) {
            this.bytes = bytes;
            return this;
        }

        public HistoBuilder className(String className) {
            this.className = className;
            return this;
        }

        public Histo build() {
            return new Histo(this);
        }
    }

    /**
     * @return the num
     */
    public long getNum() {
        return num;
    }

    /**
     * @return the bytes
     */
    public long getBytes() {
        return bytes;
    }

    /**
     * @return the instances
     */
    public long getInstances() {
        return instances;
    }

    /**
     * @return the className
     */
    public String getClassName() {
        return className;
    }

    @Override
    public String toString() {
        return "Histo [num=" + num + ", instances=" + instances + ", bytes=" + bytes
                + ", className=" + className + "]";
    }
}
