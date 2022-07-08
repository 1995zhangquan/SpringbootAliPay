package com.example.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public final class IdUtil {

    public static String createUUid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static String createTimeId() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHssSSSS");
        return sdf.format(new Date());
    }

    public static Long createSnowId() {
        SnowflakeIdWorker sfiw = new SnowflakeIdWorker(0, 0);
        return sfiw.nextId();
    }

    public static String createOrderId(Integer trade_state) {
        String businessCode = "CZ";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHssSSSS");
        if (trade_state == null || trade_state == StaticUtil.TRANSACTION_STATUS_PAY) {
            businessCode = "CZ";
        } else if (trade_state == StaticUtil.TRANSACTION_STATUS_REFUND) {
            businessCode = "TK";
        } else if (trade_state == StaticUtil.TRANSACTION_STATUS_PART_REFUND) {
            businessCode = "BTK";
        } else {
            businessCode = "CZ";
        }
        return "OD" + sdf.format(new Date()) + ((int)Math.random() * 10 ) + businessCode;
    }

    public static void main(String[] args) {
        while (true) {
            System.out.println(Math.random());
        }
    }
}
