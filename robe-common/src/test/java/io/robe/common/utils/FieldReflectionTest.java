package io.robe.common.utils;

import org.junit.Test;

/**
 * Created by serayuzgur on 22/03/16.
 */
public class FieldReflectionTest {
    static class Sample {
        private boolean bool;
        private byte b;
        private int i;
        private short s;
        private long l;
        private double d;
        private char c;
        private String name;

        public Sample() {
        }

        public Sample(boolean bool, byte b, int i, short s, long l, double d, char c, String name) {
            this.bool = bool;
            this.b = b;
            this.i = i;
            this.s = s;
            this.l = l;
            this.d = d;
            this.c = c;
            this.name = name;
        }

        public boolean isBool() {
            return bool;
        }

        public void setBool(boolean bool) {
            this.bool = bool;
        }

        public byte getB() {
            return b;
        }

        public void setB(byte b) {
            this.b = b;
        }

        public int getI() {
            return i;
        }

        public void setI(int i) {
            this.i = i;
        }

        public short getS() {
            return s;
        }

        public void setS(short s) {
            this.s = s;
        }

        public long getL() {
            return l;
        }

        public void setL(long l) {
            this.l = l;
        }

        public double getD() {
            return d;
        }

        public void setD(double d) {
            this.d = d;
        }

        public char getC() {
            return c;
        }

        public void setC(char c) {
            this.c = c;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Sample)) return false;

            Sample sample = (Sample) o;

            if (bool != sample.bool) return false;
            if (b != sample.b) return false;
            if (i != sample.i) return false;
            if (s != sample.s) return false;
            if (l != sample.l) return false;
            if (Double.compare(sample.d, d) != 0) return false;
            if (c != sample.c) return false;
            return name != null ? name.equals(sample.name) : sample.name == null;

        }

        @Override
        public int hashCode() {
            int result;
            long temp;
            result = (bool ? 1 : 0);
            result = 31 * result + (int) b;
            result = 31 * result + i;
            result = 31 * result + (int) s;
            result = 31 * result + (int) (l ^ (l >>> 32));
            temp = Double.doubleToLongBits(d);
            result = 31 * result + (int) (temp ^ (temp >>> 32));
            result = 31 * result + (int) c;
            result = 31 * result + (name != null ? name.hashCode() : 0);
            return result;
        }
    }

    @Test
    public void testCopy() throws Exception {
        Sample src = new Sample(true, Byte.MIN_VALUE, 2, Short.MAX_VALUE, 4, 5, 'c', "Test");
        Sample dest = new Sample();
        FieldReflection.copy(src, dest);
        assert src.equals(dest);

    }

    @Test
    public void testMergeRight() throws Exception {
        Sample src = new Sample(true, Byte.MIN_VALUE, 2, Short.MAX_VALUE, 4, 5, 'c', null);
        Sample dest = new Sample(false, Byte.MAX_VALUE, 3, Short.MIN_VALUE, 3, 4, 'b', "Test");
        FieldReflection.mergeRight(src, dest);
        src.setName("Test");
        assert src.equals(dest);
    }
}