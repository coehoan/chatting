package site.metacoding.practice;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ResponseDto {

    private Response response;

    @AllArgsConstructor
    @Data
    class Response {
        private Header header;
        private Body body;

    }

    @AllArgsConstructor
    @Data
    class Header {
        private String resultCode;
        private String resultMsg;

    }

    @AllArgsConstructor
    @Data
    class Body {
        private Items items;
        private int numOfRows;
        private int pageNo;
        private int totalCount;

        @AllArgsConstructor
        @Data
        class Items {
            private List<Item> item;

            @AllArgsConstructor
            @Data
            class Item {
                private String addr; // 주소
                private String mgtStaDd; // 운영시작일자
                private String pcrPsblYn; // PCR 가능여부
                private String ratPsblYn; // RAT(신속항원검사) 가능여부
                private String recuClCd; // 요양종별코드 11: 종합병원 21: 병원 31: 의원
                private String rprtWorpClicFndtTgtYn; // 호흡기전담클리닉여부
                private String sgguCdNm; // 시군구명
                private String sidoCdNm; // 시도명
                private String telno; // 요양기관전화번호
                private String XPos; // x 좌표
                private String XPosWgs84; // 세계지구 x 좌표
                private String YPos; // y 좌표
                private String YPosWgs84; // 세계지구 y 좌표
                private String yadmNm; // 요양기관명
                private String ykihoEnc; // 암호화된 요양기호

            }
        }
    }

}
