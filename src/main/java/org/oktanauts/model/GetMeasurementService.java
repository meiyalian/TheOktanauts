package org.oktanauts.model;

import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class GetMeasurementService {

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }


    public void updateMonitoredPatientMeasurement(Patient p, String code,  GetMeasurementCallback callback){


            String url = "https://fhir.monash.edu/hapi-fhir-jpaserver/fhir/Observation?subject=" + p.getId()
                    + "&code=" + code + "&_format=json";

            System.out.println(url);
            try (InputStream is = new URL(url).openStream()) {
                BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                String jsonText = readAll(rd);
                JSONObject json = new JSONObject(jsonText);

                if (json.has("entry")){
                    JSONObject valueQuantity = json.getJSONArray("entry").getJSONObject(0).getJSONObject("resource")
                            .getJSONObject("valueQuantity");

                    Timestamp dateTime = new Timestamp(new SimpleDateFormat("yyyy-MM-dd'T'H:m:s.SX")
                            .parse(json.getJSONArray("entry").getJSONObject(0).getJSONObject("resource")
                                    .getString("issued")).getTime());
                    Float value = valueQuantity.getFloat("value");
                    String unit = valueQuantity.getString("unit");
                    String name = json.getJSONArray("entry").getJSONObject(0).getJSONObject("resource")
                            .getJSONObject("code").getString("text");

                    Measurement result = new Measurement(code, name, value, unit, dateTime, p);
                    p.addMeasurement(code,result);
                }
                else{
                    p.addMeasurement(code, null);
                    System.out.println("patient" + p.getName() + " doesnt have this measurement");
                }

                if (callback != null) {
                    callback.updateView();
                }
            }
            catch (IOException | ParseException e) {
                e.printStackTrace();
            }
        }





}
