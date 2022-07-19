package com.datasolution.msa.gateway.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.util.List;

@Slf4j
public class PrintUtil {
    public static void printLog(List<String> logList, int maxLength) {
        logList.stream().findFirst().ifPresent(e -> log.info("{}", "┌─ " + StringUtils.rightPad(e + " ", maxLength, "─") + "┐"));
        logList.stream().skip(1).forEach(e -> log.info("{}", String.format("│ %-" + maxLength + "s │", e)));
        log.info("{}", "└" + StringUtils.rightPad("", maxLength + 2, "─") + "┘");
    }
}
