## AR 여행지 방명록 서비스 - YOU_AR_HERE
2021.09 ~ 2022.04

<br>   

### 실행 예시
1. 메인 화면
    - 방명록을 남길 수 있는 기능(MEMO 버튼)과 주변 관광지를 보여주는 기능(MAP) 두 가지로 나뉜다.   
        <div>
        <img src="https://user-images.githubusercontent.com/26539591/169676663-270ae46b-8731-4fa2-b989-ee841080fec5.jpg" width="30%"/>
        </div>


2. 방명록 기능 (MEMO 버튼 클릭 시)
    - 주변 평면을 인식하고 평면이 인식되면 흰색 점들로 표시된다.  
        <div text-align="center">
        <img src="https://user-images.githubusercontent.com/26539591/169676666-36af1a5a-d50c-455c-9778-16a198bc7a95.jpg" width="30%"/> 
          <img src="https://user-images.githubusercontent.com/26539591/169676667-fa1d9285-91f2-4365-95e4-01d4104451b0.jpg" width="30%"/> 
        </div>
    
    - `ADD` 버튼을 누르면 메세지를 입력할 수 있는 창이 뜨고, 원하는 내용을 입력한 후 평면을 터치하면 해당 자리에 3D 방명록이 렌더링된다.
        <div text-align="center">
          <img src="https://user-images.githubusercontent.com/26539591/169676672-8ce7bde8-7b45-4667-b672-3cf7394b314a.jpg" width="30%"/>
          <img src="https://user-images.githubusercontent.com/26539591/169676674-f24cb6cd-cbf6-4914-ab4e-3580ac1a5a8b.jpg" width="30%"/>
        </div>
    
3. 주변 관광지 AR 표시 기능 (MAP 버튼 클릭 시)
    - 현재 위치를 기준으로 특정 반경 이내의 특정 장소들을 검색하고 지도와 AR view에 나타낸다.
    - 설정된 반경: `2km`, 장소 카테고리: `park`  
     
        <div text-align="center">
          <img src="https://user-images.githubusercontent.com/26539591/169676680-7c6d4362-f4bc-469f-96ef-5a9b3f263f22.jpg" width="30%"/>
          <img src="https://user-images.githubusercontent.com/26539591/169676681-fe5ffb09-7c53-4f7a-90f5-761890e80d84.jpg" width="30%"/>
        </div>

                                                                                                                     
                                                                                                                     
### [기본 정보 및 개발 환경]  
|  타입 |  내용  | 
| ------ | ----- |
| 프로그램 종류 | 안드로이드 애플리케이션 |
| OS | Windows 10 |
| 개발 도구 | Android Studio 3.1 |
| 개발 언어 | Kotlin 1.5.30, Java 17.0.1 |
| SDK API 버전 | Android 10.0 (Q) API 29 |
| 오픈소스 라이브러리 | ARCore, Sceneform  |
| 테스트 기기 | Galaxy S8 Android 9.0 (Pie) |


<br>

### [차별화 기능]
- depth 기반 방명록 배치
- 각도에 따라 뭐를 달리할지
- 애플리케이션의 배포를 위한 인프라 구축 (AWS 활용)
- GPS 사용 ??


### [개요]
1. 서비스 설명    
  여러 여행지에서 '~왔다감' 등의 낙서가 많다고 한다. 방문했다는 사실을 어딘가에 기록하므로써 그 사람들에게는 추억이 될 수 있지만, 
  그런 낙서들은 문화적으로 가치가 있는 유적지를 오래 보존하기 어렵게 만들고 있다.   
  ["이러라고 만든게 아닌데..." 국제적 망신, 세계 속의 우리말 낙서 (아주경제 뉴스)](https://www.ajunews.com/view/20201008161015697)   <br></br>
  디지털 방명록은 이미 여러 곳에서 시행되고 있긴 하지만, 단순한 2D 화면으로 나타나는 디지털 방명록이 아니라, 실제 그 랜드마크를 AR 화면으로 비춰보았을 때, 공간적으로 보이는 방명록은 색다른 느낌을 줄 수 있을 것이라고 생각한다.  
  또한 여행자들이 본인의 기록을 공유하는 문화가 될 수 있을 것이다.
    - [대형 디지털 스크린 방명록](http://m.joongdo.co.kr/view.php?key=20170115000016308)
    - [이탈리아 디지털 낙서장](https://m.khan.co.kr/world/world-general/article/201603172210245/amp)  


2. 관련 기술  
    - AR 어플리케이션 개발 
    - 위치 기반 서비스(GPS)
    - 모바일 DB, 서버 구축


3. 계획   

    |  일정  |  내용  |  제출  | 
    | ------ | ----- | ------ |
    | 9월 | 서비스 요구사항 명세  | 제안서  |
    | 10월 ~ 11월 | AR 개발 공부, 자료조사  |  |
    | 12월 (종강 이후) | AR 어플리케이션 개발 시작  |  |
    | 1월 | AR 어플리케이션 기본 기능 구현  | |
    | 2월 | AR 어플리케이션 + DB 연동 | |
    | 3월 | 구현 상황 정리, 배포용 서버 구축  | 중간 보고서 |
    | 4월 | 개발 마무리| 최종 보고서 |
    | 5월 | 발표 준비 | 발표 자료|
  

### [제안서]
1. 과제의 필요성
2. 선행 연구 및 기술현황
3. 작품/논문 전체 진행 계획 및 구성
