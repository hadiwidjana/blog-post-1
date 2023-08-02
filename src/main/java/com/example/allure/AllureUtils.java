package com.example.allure;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

import static org.zeroturnaround.zip.ZipUtil.pack;

public class AllureUtils {

    public static void generateReport(){
        //generate allure report
        try {
            Runtime rt = Runtime.getRuntime();
            Process pr;
            if(System.getProperty("os.name").toLowerCase().contains("windows")) pr = rt.exec("cmd.exe allure generate");
            else pr = rt.exec("allure generate");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void delAllureReport(){
        try {
            FileUtils.deleteDirectory(new File(System.getProperty("user.dir")+"/allure-report"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void delAllureResult(){
        try {
            FileUtils.deleteDirectory(new File(System.getProperty("user.dir")+"/allure-results"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void zipReport(){
        File allureReport = new File(System.getProperty("user.dir")+"/allure-report");
        //check folder and zip it
        if(allureReport.exists())
        pack(allureReport,new File(System.getProperty("user.dir")+"/allure-report.zip"));
    }

    public static void delZipReport(){
        File file = new File(System.getProperty("user.dir")+"/allure-report.zip");
        file.delete();
    }
}
