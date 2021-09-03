package com.intellif.intellif.tool;

import java.io.File;

public class ImageCheck {
    public static boolean isImage(File file){
        if(!file.exists()||file.isDirectory()){
            return false;
        }
        String name = file.getName().toLowerCase();
        String type = name.substring(name.lastIndexOf('.')+1);
        if("jpg".equals(type)||"png".equals(type)||"png".equals(type)){
            return true;
        }
        return false;
    }
}
