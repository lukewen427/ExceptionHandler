/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.connexience.server.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;

public class CommandRunner {

    private StreamConsumer sysErr, sysOut;
    private int exitCode;
    private volatile boolean commandStarted = false;
    
    public int run(String command) throws InterruptedException, IOException {
        commandStarted = false;
        Process process = Runtime.getRuntime().exec(command);
        commandStarted = true;
        sysErr = new StreamConsumer(process.getErrorStream());
        sysOut = new StreamConsumer(process.getInputStream());
        exitCode = process.waitFor();
        return exitCode;
    }

    public int getExitCode() {
        return exitCode;
    }

    public boolean isCommandStarted() {
        return commandStarted;
    }
   

    public String sysErr() {
        return sysErr.toString();
    }

    public String sysOut() {
        return sysOut.toString();
    }

    class StreamConsumer implements Runnable {

        private InputStream inputStream;
        private StringWriter stringWriter = new StringWriter();
        private PrintWriter output = new PrintWriter(stringWriter, true);

        public StreamConsumer(InputStream inputStream) {
            this.inputStream = inputStream;
            new Thread(this).start();
        }

        public void run() {
            try {
                InputStreamReader isr = new InputStreamReader(inputStream);
                BufferedReader br = new BufferedReader(isr);
                while (true) {
                    String s = br.readLine();
                    if (s == null) {
                        break;
                    }
                    output.println(s);
                }
                inputStream.close();
                output.close();
            } catch (Exception e) {
                throw new RuntimeException("problems reading input stream", e);
            }
        }

        @Override
        public String toString() {
            return stringWriter.toString();
        }
    }
}
