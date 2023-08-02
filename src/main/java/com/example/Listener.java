package com.example;

import org.testng.IExecutionListener;

import static com.example.netlify.NetlifyAPI.DeployAllure;

public class Listener implements IExecutionListener {
    @Override
    public void onExecutionFinish() {

        //deploy allure report
        try {
            DeployAllure(true, true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}

