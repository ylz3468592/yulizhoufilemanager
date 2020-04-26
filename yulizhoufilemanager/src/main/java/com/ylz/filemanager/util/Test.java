package com.ylz.filemanager.util;

import android.os.Handler;
import android.os.Message;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author yulizhou
 * @description:
 * @date :2020/4/17 13:40
 */
public class Test {

    public void test(String sourcefile,String targetfile) throws IOException {

        DataOutputStream outputStream  = null;
        InputStream inputStream = null;

        try {
            inputStream = new FileInputStream(sourcefile);
            outputStream = new DataOutputStream(new FileOutputStream(targetfile));
            int len = inputStream.available();
            //判断长度是否大于1M
            if (len <= 1024 * 1024) {
                byte[] bytes = new byte[len];
                inputStream.read(bytes);
                outputStream.write(bytes);
            } else {
                int byteCount = 0;
                //1M逐个读取
                byte[] bytes = new byte[1024*1024];
                while ((byteCount = inputStream.read(bytes)) != -1){
                    outputStream.write(bytes, 0, byteCount);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            outputStream.flush();
            inputStream.close();
            outputStream.close();
        }

    }
}
