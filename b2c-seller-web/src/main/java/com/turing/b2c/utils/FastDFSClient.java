package com.turing.b2c.utils;

import jdk.nashorn.internal.objects.Global;
import org.csource.common.MyException;
import org.csource.fastdfs.*;

import java.io.IOException;

public class FastDFSClient {
    private TrackerClient trackerClient=null;
    private TrackerServer trackerServer=null;
    private StorageClient1 storageClient1=null;
    private StorageServer storageServer=null;
    public FastDFSClient(String conf) throws Exception {
        ClientGlobal.init(conf);
        trackerClient=new TrackerClient();
        trackerServer=trackerClient.getConnection();
        storageClient1=new StorageClient1(trackerServer,storageServer);
    }

    public String upload(byte[] fileContent, String extName) throws Exception{
       return  storageClient1.upload_file1(fileContent,extName,null);
    }

}
