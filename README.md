# PGDummyApp (안드로이드 PG 결제 테스트 앱)

이 프로젝트는 안드로이드 WebView를 이용해 다양한 카드사 결제 연동을 테스트할 수 있는 샘플 앱입니다.

## 주요 기능
- 결제 테스트 URL 선택 및 직접 입력
- WebView를 통한 결제 페이지 호출
- 카드사 앱(국민, 현대, 토스, 하나 등) 연동 및 미설치 시 마켓 이동 처리

## 프로젝트 구조
- `app/` : 실제 앱 소스코드 및 리소스
- `build.gradle.kts`, `settings.gradle.kts`, `gradle.properties` : Gradle 빌드 설정
- `gradle/`, `gradlew`, `gradlew.bat` : Gradle Wrapper (별도 설치 없이 빌드 가능)

## 빌드 및 실행 방법

### 1. Android Studio에서 열기
1. Android Studio에서 `Open` 또는 `Import Project`로 이 폴더를 엽니다.
2. Gradle Sync가 자동으로 진행됩니다.
3. 에뮬레이터 또는 실제 디바이스를 연결한 후, 실행(▶️) 버튼을 누르면 앱이 빌드 및 실행됩니다.

### 2. 명령어로 APK 빌드
```sh
./gradlew assembleDebug
```
- 빌드 결과물: `app/build/outputs/apk/debug/app-debug.apk`
- APK 파일을 디바이스에 복사하여 설치할 수 있습니다.

### 3. APK 설치 방법
- APK 파일을 휴대폰에 복사 후, 파일 앱에서 터치하여 설치
- "알 수 없는 앱 설치 허용" 필요

## 테스트 방법
1. 앱 실행 후, 원하는 결제 테스트 URL을 선택하거나 직접 입력
2. "WebView 실행" 버튼 클릭
3. 결제 페이지가 WebView에 표시되고, 카드사 앱 호출이 정상 동작하는지 확인

## 참고 사항
- 인터넷 연결이 필요합니다.
- 카드사 앱이 미 설치된 경우, Play스토어(마켓)로 이동합니다.
- 필요시 colors.xml, strings.xml 등 리소스 파일을 확인/수정할 수 있습니다.

## 문의
- 추가 문의 사항이나 개선 요청은 담당 개발자에게 연락해 주세요. 