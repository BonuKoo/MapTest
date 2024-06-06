import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class Project01_D {

    public static void main(String[] args) {

    String apiUrl = "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode?query=";
    String clientId = " 7czrj2fpc0"; //네이버 지도 API Key
    String clientSecret ="qhww6utZ7cs0bUcLkfjIb9zyYKICJpSA2fSZyV5h"; //네이버 지도 API 비밀번호

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

            }

        }catch (Exception e){
            e.printStackTrace();
        }


    }

}
