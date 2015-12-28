package org.cbcdevotional.prototype;

import com.amazonaws.auth.*;
import com.amazonaws.services.s3.*;
import com.amazonaws.services.s3.model.*;
import java.io.*;
import java.util.*;

/**
 * Test class for prototyping calls to AWS
 **/
public class AWSPrototype
{
  AWSCredentials awsCredentials;
  AmazonS3 s3;

  public AWSPrototype()
  {
    // Credentials for the "prototype1" user
    awsCredentials = new BasicAWSCredentials("AKIAJKE3CJXUFMPT5EVA",
                                             "FKtDwaXMIoBv1YLeyMQ1PvWxgBbxhw7q//kZayyA");
    s3 = new AmazonS3Client(awsCredentials);
  }

  public void s3Test1()
  {
    System.out.println("Listing buckets");
    for(Bucket b : s3.listBuckets()) {
      System.out.println("bucket " + b.getName() + ", created at " + b.getCreationDate());
    }
    uploadFilesToS3(new File("site"));
  }

  public static class FileToUploadToS3
  {
    String s3Name;
    File file;

    public FileToUploadToS3(String s3Name, File file)
    {
      this.s3Name = s3Name;
      this.file = file;
    }
  }

  public void uploadFilesToS3(File dir)
  {
    List<FileToUploadToS3> files = new ArrayList<>();
    addFilesToList(dir, "", files);
    for(FileToUploadToS3 u : files) {
      System.out.println("uploading " + u.s3Name + ", " + u.file);
      PutObjectResult result = s3.putObject("org.cbcdevotional.prototype1", u.s3Name, u.file);
    }
  }

  void addFilesToList(File dir, String prefix, List<FileToUploadToS3> files)
  {
    for(File f : dir.listFiles()) {
      if(f.isDirectory()) {
        String newPrefix = prefix + f.getName() + "/";
        addFilesToList(f, newPrefix, files);
      }
      else if(f.canRead()) {
        String name = prefix + f.getName();
        files.add(new FileToUploadToS3(name, f));
      }
    }
  }

  public void run()
    throws Exception
  {
    s3Test1();
  }
  
  public static void main(String[] args)
    throws Exception
  {
    AWSPrototype ap = new AWSPrototype();
    ap.run();
  }
}
