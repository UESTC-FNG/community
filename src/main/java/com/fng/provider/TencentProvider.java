package com.fng.provider;


import com.fng.exception.CustomizeErrorCode;
import com.fng.exception.CustomizeException;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.region.Region;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URL;
import java.sql.Time;
import java.util.Date;
import java.util.UUID;

@Service
public class TencentProvider {

    @Value("${tencent.file.public-key}")
    private String publicKey="AKIDyjV1SfNHiFXYI1mooXOzKahZAUZfo6PT";


    @Value("${tencent.file.private-key}")
    private String privateKey="ziAN6aolTxA6mX3LOUL0QtTqyqUu3xaH";

    @Value("${tencent.file.bucketName}")
    private String bucketName;
    @Value("${tencent.file.regionName}")
    private String regionName;
    @Value("${tencent.file.expires}")
    private Long expires;




    public String
    upload(InputStream fileStream, String fileName, String mineType,
    HttpServletResponse response) {
        //授权器
        COSCredentials cred = new BasicCOSCredentials(publicKey, privateKey);

        //设置区域
        Region region = new Region(regionName);
        ClientConfig clientConfig = new ClientConfig(region);
        //生成COS客户端
        COSClient cosClient = new COSClient(cred, clientConfig);
        String[] split = fileName.split("\\.");
        if (split.length>1){
            String generateName = UUID.randomUUID().toString() + "." + split[split.length - 1];

// 指定要上传到 COS 上对象键
            String key = generateName;
            ObjectMetadata objectMetadata=new ObjectMetadata();
            objectMetadata.setContentType(mineType);
//            生成COS客户端
            cosClient = new COSClient(cred, clientConfig);
//            上传文件
            cosClient.putObject(bucketName, key,fileStream,objectMetadata);


            //返回url地址
            if (response !=null){

                Date date=new Date(System.currentTimeMillis()+expires );
                String url =cosClient.generatePresignedUrl(bucketName, generateName, date).toString();
                return url;
            }else{
                throw new CustomizeException(CustomizeErrorCode.FILE_UPLOAD_ERROR);
            }
        }else{
            return null;
        }
    }
}
