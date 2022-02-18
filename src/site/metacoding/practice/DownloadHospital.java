package site.metacoding.practice;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import site.metacoding.practice.ResponseDto.Body.Items.Item;

public class DownloadHospital {

    public static List<Hospital> getHospitalList() {
        List<Hospital> hospitalList = new ArrayList<>();

        try {
            // totalcount 받아오기
            URL url = new URL(
                    "http://apis.data.go.kr/B551182/rprtHospService/getRprtHospService?serviceKey=wJmmW29e3AEUjwLioQR22CpmqS645ep4S8TSlqtSbEsxvnkZFoNe7YG1weEWQHYZ229eNLidnI2Yt5EZ3Stv7g%3D%3D&pageNo=1&numOfRows=10&_type=json");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            String responseJson = br.readLine();
            Gson gson = new Gson();
            ResponseDto dto = gson.fromJson(responseJson, ResponseDto.class);
            // totalcount = 데이터 전체 개수
            int totalcount = dto.getResponse().getBody().getTotalCount();

            // 전체 데이터 받아오기
            url = new URL(
                    "http://apis.data.go.kr/B551182/rprtHospService/getRprtHospService?serviceKey=wJmmW29e3AEUjwLioQR22CpmqS645ep4S8TSlqtSbEsxvnkZFoNe7YG1weEWQHYZ229eNLidnI2Yt5EZ3Stv7g%3D%3D&pageNo=1&numOfRows="
                            + totalcount + "&_type=json");
            conn = (HttpURLConnection) url.openConnection();
            br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            responseJson = br.readLine();
            gson = new Gson();
            dto = gson.fromJson(responseJson, ResponseDto.class);
            List<Item> result = dto.getResponse().getBody().getItems().getItem();
            for (int i = 0; i < result.size(); i++) {
                Hospital hospital = new Hospital(result.get(i).getAddr(), result.get(i).getMgtStaDd(),
                        result.get(i).getPcrPsblYn(), result.get(i).getRatPsblYn(), result.get(i).getRecuClCd(),
                        result.get(i).getRprtWorpClicFndtTgtYn(), result.get(i).getSgguCdNm(),
                        result.get(i).getSidoCdNm(), result.get(i).getTelno(), result.get(i).getXPos(),
                        result.get(i).getXPosWgs84(), result.get(i).getYPos(), result.get(i).getYPosWgs84(),
                        result.get(i).getYadmNm(), result.get(i).getYkihoEnc());

                hospitalList.add(hospital);
            }
            return hospitalList;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

}
