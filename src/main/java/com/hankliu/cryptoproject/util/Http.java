/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hankliu.cryptoproject.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Hank Liu <kccc418@gmail.com>
 */
public class Http {

    /**
     * @param urlParam
     * @param requestType Set the method for the URL request, one of: GET POST
     * HEAD OPTIONS PUT DELETE TRACE are legal, subject to protocol
     * restrictions.
     * @return
     */
    public static String sendRequest(String urlParam, String requestType) {
        try {
            // TODO code application logic here
            URL url = new URL(urlParam);

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod(requestType); // GET POST HEAD OPTIONS PUT DELETE TRACE
            con.setRequestProperty("User-agent", "Mozilla/5.0 (Linux; Android 4.2.1; Nexus 7 Build/JOP40D) AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.166  Safari/535.19");
//            con.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);

            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                System.out.println(response.toString());
                return response.toString();
            }
        } catch (MalformedURLException ex) {
            Logger.getLogger(Http.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Http.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    public static String uploadToServer(String urlParam, String json) {
        String returnStr = "";
        try {
            URL url = new URL(urlParam);
            // 得到連接對象
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            // 設置請求需要返回的數據類型與字符集
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setRequestProperty("User-agent", "Mozilla/5.0 (Linux; Android 4.2.1; Nexus 7 Build/JOP40D) AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.166  Safari/535.19");
            // 允許寫入
            conn.setDoOutput(true);
            // 允許讀出
            conn.setDoInput(true);
            // 設置請求類型
            conn.setRequestMethod("POST");

            // 輸出資料
            OutputStream os = conn.getOutputStream();
            os.write(json.getBytes("UTF-8"));
            os.close();

            // 讀取結果
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                System.out.println("response: " + response.toString());
                returnStr = response.toString();
            }

            conn.disconnect();

        } catch (MalformedURLException ex) {
            Logger.getLogger(Http.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Http.class.getName()).log(Level.SEVERE, null, ex);
        }
        return returnStr;
    }
}
