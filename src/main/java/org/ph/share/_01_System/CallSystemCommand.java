package org.ph.share._01_System;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


/**
 * 这里注重介绍 java 如何调用系统命令
 */
public class CallSystemCommand {

    public static void main(String[] args) {
        runtimeCall();
        processCall();
    }

    // 1. 直接使用 Runtime 来执行我们的命令
    public static void runtimeCall() {
        try {
            // 如果需要我们这里的脚本在 java 程序终止后还能继续运行，
            // 这就需要我们的程序首先是一个后台程序(启动在后台运行)
            // 例如脚本启动程序的时候使用的 nohup java -jar .. &
            Runtime.getRuntime().exec("bash /opt/app/bin/run.sh");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 2. 使用进程来启动对应的命令
    public static void processCall() {
        ProcessBuilder builder = new ProcessBuilder();
        // 运行的命令
        List<String> commands = new ArrayList<>();
        commands.add("sh");
        commands.add("/opt/app/bin/run.sh");
        // 将命令设置到 process 中
        builder.command(commands);

        builder.redirectErrorStream(true);
        // 启动程序
        try { 
            Process process = builder.start();
            // 获取进程的标准输出流，获知程序运行的结果
            waitFor(process);
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    // 处理调用程序获取的输出(标准输出与错误输出)，并进行处理
    public static int waitFor(Process p) {
        int exitValue = -1;
        try ( 
            // 获取程序的输入
            InputStream in = p.getInputStream();
            InputStream error = p.getErrorStream();
        ){
            int retry = 0;
            int maxRetryTimes = 600;
            boolean finish = false;
            StringBuffer outputString = new StringBuffer();
            while (!finish) {
                if (retry > maxRetryTimes) {
                    return exitValue;
                }
                try {
                    byte[] buf = new byte[2 * 1024];
                    while (in.available() > 0) {
                        int n = in.read(buf);
                        if (n > 0) {
                            String message = new String(buf, StandardCharsets.UTF_8);
                            boolean endline = message.endsWith("\n");
                            String[] msgList = message.split("\n");
                            if (endline) {
                                for (int i = 0; i < msgList.length; i++) {
                                    outputString.append(msgList[i]);
                                    System.out.println(outputString);
                                    outputString = new StringBuffer();
                                }
                            }else {
                                if (message.length() > 1) {
                                    for (int i = 0; i < msgList.length - 1; i++) {
                                        outputString.append(msgList[i]);
                                        System.out.println(outputString);
                                        outputString = new StringBuffer();
                                    }
                                    outputString.append(msgList[msgList.length - 1].trim());
                                }else {
                                    outputString.append(message.trim());
                                }
                            }
                        }
                    }
                    while (error.available() > 0) {
                        int n = in.read(buf);
                        if (n > 0) {
                            String message = new String(buf, StandardCharsets.UTF_8);
                            boolean endline = message.endsWith("\n");
                            String[] msgList = message.split("\n");
                            if (endline) {
                                for (int i = 0; i < msgList.length; i++) {
                                    outputString.append(msgList[i]);
                                    System.out.println(outputString.toString());
                                    outputString = new StringBuffer();
                                }
                            }else {
                                if (message.length() > 1) {
                                    for (int i = 0; i < msgList.length - 1; i++) {
                                        outputString.append(msgList[i]);
                                        System.out.println(outputString.toString());
                                        outputString = new StringBuffer();
                                    }
                                    outputString.append(msgList[msgList.length - 1].trim());
                                }else {
                                    outputString.append(message.trim());
                                }
                            }
                        }
                    }
                    exitValue = p.exitValue();
                    finish  = true;
                }catch(IllegalThreadStateException e) {
                    try {
                        Thread.sleep(1000);
                        retry++;
                    }catch(InterruptedException ex){
                        ex.printStackTrace();
                    }
                }
            }
        }catch(IOException e) {
            e.printStackTrace();
        }
        return exitValue;
    }
}
