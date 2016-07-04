package com.example.rebelo.gcm_example.backend.spi;

import com.example.rebelo.gcm_example.backend.Constants;
import com.example.rebelo.gcm_example.backend.domain.PlatformDataRecord;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.BadRequestException;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by rebelo on 16/05/2016.
 */


@Api(name = "platformdata", version = "v1", namespace = @ApiNamespace(ownerDomain = "backend.gcm_example.rebelo.example.com", ownerName = "backend.gcm_example.rebelo.example.com", packagePath = ""))
public class PlatformData {

    @ApiMethod(name = "getPlatformData", path = "getplatformdata", httpMethod = ApiMethod.HttpMethod.GET)
    public PlatformDataRecord getPlatformData() throws Exception {
        try {
            System.out.println("*** " + "http://api.iot.ciandt.com/v2/data/" + Constants.MAC + "/last");
            // CREATING HEADER GET
            URL url2 = new URL("http://api.iot.ciandt.com/v2/data/" + Constants.MAC + "/last");
            HttpURLConnection connection = (HttpURLConnection) url2.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("charset", "utf-8");

            // OPEN CONNECTION
            connection.connect();
            int HttpResult = connection.getResponseCode();
            BufferedReader in;

            if (HttpResult != 200 && HttpResult != 201) {
                throw new BadRequestException("ERROR " + HttpResult);
            } else {
                // READING RESULT
                in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();


                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                System.out.println(response.toString());

                // CHECKING IF THE MAC THIS REGISTERED YET UNRECORDED
                if (response.toString() == "") {
                    System.out.println("***CONTENT EMPTY");
                    return null;
                } else {
                    JSONObject obj = new JSONObject(response.toString());

                    /* GET DATA THE PLATFORM AND CALL FUNCTION THAT VERIFY STATUS */
                    return verifyStatusWaiting(obj.getJSONObject("content").getString("statuscoffee"), obj.get("updateTime").toString(), obj.get("currentTime").toString());
                }
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public static PlatformDataRecord verifyStatusWaiting(String currentStatus, String sUpdateTime, String sCurrentTime) {
        long updateTime = Long.parseLong(sUpdateTime);
        long currentTime = Long.parseLong(sCurrentTime);
        String formatDate = "dd/MM/yyyy";
        String formatHour = "HH:mm:ss";

        DateFormat dfDate = new SimpleDateFormat(formatDate);
        DateFormat dfHour = new SimpleDateFormat(formatHour);
        dfDate.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo"));
        dfHour.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo"));

        Date d1 = new Date(updateTime);
        Date d2 = new Date(currentTime);

        //in milliseconds
        long diff = d2.getTime() - d1.getTime();

        long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000) % 24;
        long diffDays = diff / (24 * 60 * 60 * 1000);

        /* VERIFYING THE CURRENT TIME IS OVER IN 1 TIME TIME THE COFFEE WAS READY */
        if (diffDays > 0 || diffHours> 1 || diffMinutes > 30) {
            currentStatus = "1"; /* MACHINE WAITING */
        }

        System.out.println("*** Days: " + diffDays + " Horas: " + diffHours + " Minutos: " + diffMinutes + " Segundos: " + diffSeconds);
        System.out.println("*** " + currentStatus + " Update Time: " + updateTime + " Current Time: " + currentTime);

        return new PlatformDataRecord(currentStatus, dfDate.format(d1), dfHour.format(d1));
    }
}
