import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class Project01_E {

    public static void map_services(String point_x,String point_y,String address){
        String URL_STATICMAP = "https://naveropenapi.apigw.ntruss.com/map-static/v2/raster?";
        try {
            String pos = URLEncoder.encode(point_x+" "+point_y, "UTF-8"); //공백 발생 때문에 encode 필요
            //"https://naveropenapi.apigw.ntruss.com/map-static/v2/raster?w=300&h=300&center=127.1054221,37.3591614&level=16"
            String url = URL_STATICMAP;
            url += "center=" +point_x + "," + point_y;
            url += "&level=16&w=700&h=500";
            url += "&markers=type:t|size:mid|pos:"+pos+"|label:"+URLEncoder.encode(address,"UTF-8");

            URL u = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) u.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("X-NCP-APIGW-API-KEY-ID","7czrj2fpc0");
            conn.setRequestProperty("X-NCP-APIGW-API-KEY", "qhww6utZ7cs0bUcLkfjIb9zyYKICJpSA2fSZyV5h");
            int responseCode = conn.getResponseCode();
            BufferedReader br;
            if (responseCode == 200) {
                InputStream is = conn.getInputStream();
                int read = 0;
                byte[] bytes = new byte[1024];
                //랜덤한 이름으로 파일 생성
                String tempName = Long.valueOf(new Date().getTime()).toString();
                File f = new File(tempName + ".jpg");
                f.createNewFile();
                OutputStream outputStream = new FileOutputStream(f);
                while ((read = is.read(bytes)) != -1){
                    outputStream.write(bytes,0,read);
                }
                is.close();
            }else {
                br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();
                while ((inputLine = br.readLine())!= null){
                    response.append(inputLine);
                }
                br.close();
                System.out.println(response.toString());
            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {


        String clientId = " 7czrj2fpc0"; //네이버 지도 API Key
        String clientSecret ="qhww6utZ7cs0bUcLkfjIb9zyYKICJpSA2fSZyV5h"; //네이버 지도 API 비밀번호
        String apiUrl = "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode?query=";

        BufferedReader io = new BufferedReader(new InputStreamReader(System.in));
    /*
    System.in은 Byte 이고
    BufferedReader가 문자 Stream이기 때문에,
    Byte와 문자 stream은 바로 연결 할 수 없기 때문에, 중간에 InputStreamReader 라는 브릿지 Stream을 놓는다.
     */

        try {
            System.out.println("주소를 입력하세요");
            String address = io.readLine();
            String addr = URLEncoder.encode(address, "utf-8");
            String reqUrl = apiUrl + addr; //네이버 api + 입력 addr -> 요청 Url

            URL url = new URL(reqUrl);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("X-NCP-APIGW-API-KEY-ID",clientId);
            conn.setRequestProperty("X-NCP-APIGW-API-KEY",clientSecret);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Accept", "application/json");

            int responseCode = conn.getResponseCode();
            BufferedReader br;
            if (responseCode == 200) {
                br = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
            }else {
                br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }

            String inputLine;
            StringBuffer response = new StringBuffer(); //문자열 추가 변경 시 사용 -> JSON 사용 필요

            String x=""; String y=""; String z="";
            //위도, 경도, 주소
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();

            JSONTokener tokener = new JSONTokener(response.toString());
            JSONObject object = new JSONObject(tokener);

            System.out.println(object);


            JSONArray arr = object.getJSONArray("addresses");
            for (int i = 0; i < arr.length(); i++) {
                JSONObject temp = (JSONObject) arr.get(i);
                System.out.println("address : " + temp.get("roadAddress"));
                System.out.println("jibunAddress : " + temp.get("jibunAddress"));
                System.out.println("경도 : " + temp.get("x"));
                System.out.println("위도 : " + temp.get("y"));

                x = (String) temp.get("x");
                y = (String) temp.get("x");
                z = (String) temp.get("roadAddress");

            }
            map_services(x,y,z);
        }catch (Exception e){
            e.printStackTrace();
        }


    }
}
