package com.bjpowernode;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class QRCodeTest {
    @Test
    public void generatQRCode() throws WriterException, IOException {
        //{"country":"China","Province":"河北省","city":"雄安区","address":{"社区"：”“”“”“”“”“”“”"幸福社区"，
        // “”"街道":"解放路","门号":"888号"“”“”“”“”“}}
        //创建一个json对象
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("country","China");
        jsonObject.put("Province","河北省");
        jsonObject.put("city","雄安区");
        JSONObject addressJson = new JSONObject();
        addressJson.put("社区","幸福社区");
        addressJson.put("街道","解放路");

        jsonObject.put("address",addressJson);
         //将json对象转为json格式的字符串
        String jsonString = jsonObject.toJSONString();

        Map<EncodeHintType, Object> hintMap = new HashMap<>();
        hintMap.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        String contents = "http://www.bjpowernode.com";
        //创建一个矩阵对象
        BitMatrix bitMatrix = new MultiFormatWriter().
                encode(jsonString, BarcodeFormat.QR_CODE, 200, 200, hintMap);
        String filePath = "E://";
        String fileName = "qrCode.jpg";
        Path path = FileSystems.getDefault().getPath(filePath, fileName);
        //将矩阵对象转换为图像
        MatrixToImageWriter.writeToPath(bitMatrix, "jpg", path);
    }
}
