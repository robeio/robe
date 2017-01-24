package io.robe.hibernate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by kamilbukum on 24/01/2017.
 */

@FunctionalInterface
public interface BenchMark {
    Logger LOGGER = LoggerFactory.getLogger(BenchMark.class);
    void apply();
    static void bench(String benchMarkName, int benchCount, BenchMark benchMark){
        long benchMillis = 0 ;
        LOGGER.info("*****  " + benchMarkName + " BENCHMARK *******");
        for(int i = 0 ; i < benchCount ; i ++) {
            long startMilliSecond = System.currentTimeMillis();
            benchMark.apply();
            long endMilliSecond = System.currentTimeMillis();
            long differenceMillis = (endMilliSecond - startMilliSecond);
            benchMillis += differenceMillis;
            LOGGER.info("       -> Single Benchs ! -> : " + differenceMillis);
        }
        LOGGER.info("   -> All BenchMark Total ! -> : " + benchMillis);
        LOGGER.info("_______________________________________________");
    }
}
