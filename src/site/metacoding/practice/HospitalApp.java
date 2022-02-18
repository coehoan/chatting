package site.metacoding.practice;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.List;

public class HospitalApp {

    public static void main(String[] args) {
        List<Hospital> hospitalList = DownloadHospital.getHospitalList();

        try {
            Connection connect = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "SCOTT", "TIGER");
            System.out.println("DB연결완료");
            String sql = "INSERT INTO hospitaltbl(주소, 운영시작일자, PCR가능여부, 신속항원검사가능여부, 요양종별코드, 호흡기전담클리닉여부, 시군구명, 시도명, 전화번호, X좌표, 세계지구X좌표, Y좌표, 세계지구Y좌표, 병원명, 암호처리된기호) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement pstmt = connect.prepareStatement(sql);
            for (int i = 0; i < hospitalList.size(); i++) {

                pstmt.setString(1, hospitalList.get(i).getAddr());
                pstmt.setString(2, hospitalList.get(i).getMgtStaDd());
                pstmt.setString(3, hospitalList.get(i).getPcrPsblYn());
                pstmt.setString(4, hospitalList.get(i).getRatPsblYn());
                pstmt.setString(5, hospitalList.get(i).getRecuClCd());
                pstmt.setString(6, hospitalList.get(i).getRprtWorpClicFndtTgtYn());
                pstmt.setString(7, hospitalList.get(i).getSgguCdNm());
                pstmt.setString(8, hospitalList.get(i).getSidoCdNm());
                pstmt.setString(9, hospitalList.get(i).getTelno());
                pstmt.setString(10, hospitalList.get(i).getXPos());
                pstmt.setString(11, hospitalList.get(i).getXPosWgs84());
                pstmt.setString(12, hospitalList.get(i).getYPos());
                pstmt.setString(13, hospitalList.get(i).getYPosWgs84());
                pstmt.setString(14, hospitalList.get(i).getYadmNm());
                pstmt.setString(15, hospitalList.get(i).getYkihoEnc());
                pstmt.executeUpdate();
            }
            System.out.println("데이터 전송 완료");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
