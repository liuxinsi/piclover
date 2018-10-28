package com.lxs.gollum.utils;

import java.io.File;

/**
 * @author liuxinsi
 * @date 2018/9/11 14:55
 */
public class DiskUtils {
    private static final int SPACE_LIMIT = 10;

    public static boolean haveEnoughSpace(File dir) {
        return dir.getFreeSpace() / 1024 / 1024 / 1024 > SPACE_LIMIT;
    }
}
