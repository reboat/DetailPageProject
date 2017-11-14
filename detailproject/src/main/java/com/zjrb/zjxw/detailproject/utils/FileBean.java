package com.zjrb.zjxw.detailproject.utils;

import java.io.File;
import java.io.Serializable;

/**
 * 播放器相关bean
 * Created by wanglinjie.
 * create time:2017/11/14  下午2:43
 */

public class FileBean implements Serializable {
    //文件
    private File file;
    //文件时长
    private int fileLength;

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public int getFileLength() {
        return fileLength;
    }

    public void setFileLength(int fileLength) {
        this.fileLength = fileLength;
    }
}
