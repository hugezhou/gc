package com.wosai.generate.util;

import java.io.File;

/**
 * Created by LW on 2018/3/2
 */
public class FileUtils {

    public static void deleteFile(File[] files) {
        for (File file : files) {
            if (file.exists()) {
                if (file.isDirectory()) {
                    deleteFile(file.listFiles());
                }
                file.delete();
            }

        }
    }

    public static void deleteFile(File file) {
        if (file.exists()) {
            if (file.isFile())
                file.delete();
            else
                deleteFile(file.listFiles());
        }

    }

}
