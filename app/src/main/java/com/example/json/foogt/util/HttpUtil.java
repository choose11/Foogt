package com.example.json.foogt.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.UUID;

/**
 * Created by Mzz on 2015/10/29.
 */
public class HttpUtil {
    public static void sendHttpRequest(final String address, final String method, final String data, final HttpCallbackListener listener) {
        LogUtil.d("address", address);
        LogUtil.d("PostData", data == null ? "" : data);
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                PrintWriter pw = null;
                BufferedReader reader = null;
                try {
                    URL url = new URL(address);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod(method);
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    if ("POST".equals(method.toUpperCase())) {
                        pw = new PrintWriter(connection.getOutputStream());
                        pw.write(data);
                        pw.flush();
                    }
                    reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    if (listener != null) {
                        listener.onFinish(response.toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    LogUtil.e("HTTPUtil", e.toString());
                    if (listener != null)
                        listener.onError(e);
                } finally {
                    close(connection, pw, reader);
                }
            }
        }).start();
    }

    public static int postFileToURL(final File file, final String mimeType, final URL url, final String fieldName, final HttpCallbackListener listener) {
        final int FILE_NOT_EXIST = -2, SUCCESS = 0, FAILURE = -1;
        if (file == null) // 再判断一次，因为可能在选择图片之后，该图片在上传之前被删除
            return FILE_NOT_EXIST; // -2
        new Thread(new Runnable() {
            @Override
            public void run() {
                BufferedReader reader = null;
                HttpURLConnection conn = null;
                try {
                    String boundary = UUID.randomUUID().toString();
                    conn = (HttpURLConnection) url.openConnection();

                    setHttpURLConnection(conn, boundary);
                    writeData(conn, boundary, file, mimeType, fieldName);
                    int res = conn.getResponseCode();
                    if (res == 200) {
                        reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        StringBuilder response = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }
                        if (listener != null) {
                            listener.onFinish(response.toString());
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    if (listener != null)
                        listener.onError(e);
                } finally {
                    close(conn, null, reader);
                }

            }
        }).start();
        return SUCCESS; // 0
    }

    private static void setHttpURLConnection(HttpURLConnection conn,
                                             String boundary) {
        int TIME_OUT = 30 * 1000;
        String CONTENT_TYPE = "multipart/form-data";
        conn.setConnectTimeout(TIME_OUT); // 30 * 1000 ms
        conn.setReadTimeout(TIME_OUT);
        conn.setDoInput(true); // 允许输入流
        conn.setDoOutput(true); // 允许输出流
        try {
            conn.setRequestMethod("POST");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        conn.setRequestProperty("Connection", "keep-alive");
        conn.setRequestProperty("Content-Type", CONTENT_TYPE + "; boundary="
                + boundary);
    }

    private static void writeData(HttpURLConnection conn, String boundary,
                                  File file, String mimeType, String fieldName) throws IOException {
        DataOutputStream requestData = new DataOutputStream(
                conn.getOutputStream());
        String CRLF = "\r\n";
        requestData.writeBytes("--" + boundary + CRLF); // CRLF = "\r\n"
        requestData.writeBytes("Content-Disposition: form-data; name=\""
                + fieldName + "\"; filename=\"" + file.getName() + "\"" + CRLF);
        requestData.writeBytes("Content-Type: " + mimeType + CRLF + CRLF); // 两个回车换行
        InputStream fileInput = new FileInputStream(file);
        int bytesRead;
        byte[] buffer = new byte[1024];
        while ((bytesRead = fileInput.read(buffer)) != -1) {
            requestData.write(buffer, 0, bytesRead);
        }
        fileInput.close();
        requestData.writeBytes(CRLF);
        requestData.writeBytes("--" + boundary + "--" + CRLF);
        requestData.flush();
        LogUtil.d("Post", "Finish");
    }

    /**
     * close PrintWriter and BufferedReader will also close their socket.
     *
     * @param connection
     * @param pw
     * @param reader
     */
    private static void close(HttpURLConnection connection, PrintWriter pw, BufferedReader reader) {
        try {
            if (pw != null) {
                pw.close();
            }
            if (reader != null) {
                reader.close();
            }
            if (connection != null) {
                connection.disconnect();
            }
        } catch (IOException e) {
            e.printStackTrace();
            LogUtil.e("HttpUtil", e.toString());
        }
    }
}
