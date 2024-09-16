# suncloud

<img src="https://github.com/user-attachments/assets/3f174dbf-f89e-476d-910f-586615c6ac1b" align="left"
alt="suncloud_app_image" width="200" style="box-shadow: 10px 10px 15px rgba(0, 0, 0, 0.5);">
 
현재 위치한 지역의 기상 정보와 원하는 도시의 기상 정보를 제공합니다.

제공하는 기상 정보로 기온, 습도, 미세먼지, 24시간 날씨 정보, 1주일간 날씨정보 등을 제공합니다. 

<p align="left">
<a href="https://play.google.com/store/apps/details?id=com.knichu.suncloud">
    <img alt="Get it on Google Play"
        height="80"
        src="https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png" />
</a>

<img src="https://github.com/user-attachments/assets/db72dbc0-712c-477e-85b3-338dce7ba310" width="140px" title="1" alt="1"></img>
<img src="https://github.com/user-attachments/assets/58be8278-4043-437c-811e-804440a0e881" width="140px" title="2" alt="2"></img>
<img src="https://github.com/user-attachments/assets/511fd549-956b-4515-9b7f-54d59eb53526" width="140px" title="3" alt="3"></img>
<img src="https://github.com/user-attachments/assets/ddb25f17-02a5-4d1f-8797-215cea0b0255" width="140px" title="4" alt="4"></img>
<img src="https://github.com/user-attachments/assets/ab471f65-0fd6-451a-baa6-c3c17884d8c8" width="140px" title="5" alt="5"></img>

## 개발환경 / Application Version
- Android Studio @Giraffe 2022.3.1
- minSdkVersion : 28
- targetSdkVersion : 34

## android tech
- language : Kotlin
- architecture : ACC MVVM
- di : Dagger-Hlit
- async library : RxJava
- network library : Retrofit2, Gson, OkHttp
- jetpack : Navigation Component, DataBinding, LiveData, DataStore, Paging3…

## 특징
- RxJava를 사용하여 비동기 프로그래밍을 구현
- ACC ViewModel을 사용해 ACC MVVM 디자인 패턴을 구현
- android jetpack을 최대한 활용

## APIs
- openweathermap api
  - https://openweathermap.org/current
  - https://openweathermap.org/api/air-pollution
<br/>

- 공공데이터포털 api
  - https://www.data.go.kr/data/15084084/openapi.do
  - https://www.data.go.kr/data/15059468/openapi.do

## 모듈 구조

```mermaid
flowchart TD
%% Nodes
    A("app")
    B[("gateway")]
    C[("forecast")]
    D[("nationwide")]
    E[("setting")]
    F[("common-ui")]
    G[("common")]
    H[("data")]
    I[("domain")]
    
%% Edge connections between nodes
    A --> B & H & I
    B --> C & D & E
    C --> G & F
    D --> G & F
    E --> G & F
    F --> I & G
    G --> I
    H --> I
    
%% Individual node styling. Try the visual editor toolbar for easier styling!
    style A fill:#FFCDD2
    style B fill:#FFE0B2
    style C fill:#FFF9C4
    style D fill:#FFF9C4
    style E fill:#FFF9C4
    style F fill:#C8E6C9
    style G fill:#C8E6C9
    style H fill:#BBDEFB
    style I fill:#E1BEE7
```
- **Suncloud (앱 모듈)**
  - 앱의 진입점
- **Gateway (게이트웨이 모듈)**
  - 메인 액티비티가 시작되는 곳 - GatewayActivity 
  - NetworkModule, RepositoryModule등 각종 Hilt 모듈로 의존성 설정
- **Forecast (기상 예보 모듈)**
  - ForecastFragment, ForecastViewModel
  - 현재 날씨, 24시간 날씨, 일주일간 날씨, 미세먼지 정보 등등
- **Nationwide (전국 기상 예보 모듈)**
  - NationWideFragment, NationWideViewModel
  - 전국 지도에 주요 도시 일기예보
- **Setting (설정 모듈)** 
  - SettingFragment, SettingViewModel
  - 유저들의 개인 설정 저장
- **Common** 
  - 피쳐의 공통기능 (ActivityLauncher, 각종 Base 등)
- **CommonUI**
  - 피쳐에서 공통적으로 사용할 UI에 관련된 모든 것 (Custom View, enum 등)
- **Data (데이터 모듈)** 
  - DTO, RepositoryImpl, DataSource 등
  - RepositoryImpl 에서 DTO 를 VO 로 변환
- **Domain (도메인 모듈)** 
  - VO, UseCase, RepositoryInterface 등
