# BCSDLab-CodeLab-Android
BCSD Lab 19-3 Novice Android Code Lab

## 시작하기에 앞서
- 이번에 구현을 해보는 것은 MP3 플레이어입니다. 처음 접해 보신분들은 구현하시면서 시간이 오래 걸릴수도 있습니다. 이 코드랩을 통해서 안드로이드에 대해서 더 흥미를 가졌으면 좋겠습니다.
- 코드랩 진행중에 모르는거나 막히는게 있으면 주저없이 바로 저한테 연락주세요. 그러면 화이팅입니다!
## Guide

### Android studio 설치

1. <https://developer.android.com/studio> 에 접속해서 다운로드를 받은 후 설치
2. SDK Platforms 다운로드

- ![SDK Platforms 다운로드](./image/1.png)

3. SDK Tools 다운로드

- ![SDK Tools 다운로드](./image/2.png)

### Get Started

1. Create New Project 선택
2. Empty Activity 선택

- ![SDK Empty Activity](./image/3.png)

3. 이름 및 minimum api 설정 (경로에 한글이 들어가면 안돼요!)

- ![이름 및 minimum api 설정](./image/4.png)

4. Finish
5. 실행 버튼을 눌렀을 때 "hello world" 모바일에 출력되면 성공

### 프로젝트 구조 살펴 보기

1. 프로젝트 구조

- ![이름 및 minimum api 설정](./image/5.jpeg)

### Android studio manifest에 외부 저장소 쓰기 읽기 권한 추가하기

1. 외부저장소 (공용 디렉토리 Music folder, Download folder 등) 에 접근하기 위해서는 AndroidManifest.xml에 읽기 쓰기 권한을 쓴다고 명시를 해줘야되요 그리고 MashMallow 버전 이상에서는 RuntimePermission도 추가를 해줘야 됩니다.
`<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />` 을 추가를 해줍니다. 쓰기 권한을 추가해주면 읽기권한도 자동적으로 추가가 됩니다.

- ![AndroidManifest 권한 추가](./image/6.png)

### 레이아웃 꾸미기

1. 여기까지 잘 따라오셨나요? 이제 레이아웃을 꾸며볼꺼에요 지금 꾸미는 layout은 사용자한테 보여주는 화면으로써 여러 view component를 넣어줄수가 있죠 그걸을 포함시킬 부모 layout을 설정해줄건데 저희는 LinearLayout을 사용할겁니다.
LinearLayout은 사용이 간편하고 표시 형태가 직관적이라는 장점을 가졌고 사각형 박스 형태의 디스플레이 화면에 UI 요소들을 일렬로 배치할 수 있어서 안정감있는 화면 구성이 가능하다는 이유 때문에 사용할겁니다.
2. res 패키지에서 layout 패키지에 들어가면 `activity_main.xml` 파일이 있을 겁니다. 선택을 해주세요.
3. 기본적으로 부모 Layout이 `androidx.constraintlayout.widget.ConstraintLayout`로 되어있을꺼에요 이를 `LinearLayout`으로 바꿔주세요 닫히는 태그도 마찬가지입니다.

- ![LinearLayout 으로 변경](./image/7.png)

4. Layout을 짜기전에 앞서 한번 어떻게 구현을 할까 고민을 해볼까요? 전체적으로 구상을 하자면 아래와 같은 형태가 나올꺼에요 이를 쪼개서 생각하면 제목이 나오는 부분, 버튼이 있는 부분, 음악 재생 목록이 나오는 부분으로 나눌 수 있겠죠?

- ![Layout 쪼개기](./image/8.png)

5. 한번 쪼갠것을 코드로 바꿔볼까요? 아래 코드를 참고하면  빨간색 부분이 노래제목을 표시하는곳을 담는 레아웃을 추가 해줬고 노란색 부분이 버튼들을 담아주는 레이아웃이고 주황색부분이 음악재생하는 곳을 출력해주는 ListView를 추가해줬어요. (참고로 부모 Layout에서 `android:orientation = "vertical"` 을 추가해줘야지 수직으로 뷰가 쌓입니다.)

- ![Layout 쪼개기](./image/9.png)

6. 이제 안에다가 view component를 넣어 볼까요 노래제목을 표시해줄 Textview를 추가해주고 버튼들을 추가해줍니다! 코드를 작성해보면 이와 같이 구현할 수 있습니다.

- ![Layout 구현하기](./image/10.png)

7. `android:id` 는 나중에 자바 코드상으로 연결해서 view를 control 해주기위해 고유한 id값을 넣어줬습니다.
8. 여기까지 하시면 layout 구성은 완료입니다.
9. 실행을 해보면 이와 같이 보일겁니다.

- ![실행화면](./image/11.png)

### 기능 구현하기

1. 다시 MainActivty.java 파일로 돌아가볼까요? 코드를 잘 살펴보면 onCreate라는 메소드가 있을겁니다. 그 안에 setContentView라를 메소드를 사용한것도 보이고요. 여기서 onCraete는 액티비티의 생명주기 중 하나로 액티비티가 처음 시작하고 불려지는 메소드입니다. 그리고 view를 초기화 작업을 해줍니다. 보통 여러분께서는 main 함수에서 프로그램이 시작이 되었지만 안드로이드에서는 생명주기로 관리가 되어서 생성 부터 화면이 destroy될때까지 메소드가 하나씩 불려집니다. 생명주기를 나타낸 그림입니다. 더 자세한 정보를 얻고 싶으시면 <https://developer.android.com/guide/components/activities/activity-lifecycle> 에서 확인해주세요.

- ![생명주기](./image/12.jpg)

2. 이제 TextView, Button, ListView와 같은 view를 control 해주기위해서 객체에다가 id를 통해서 연결을 해주는 작업을 해줄겁니다. 아까 layout에서 id를 설정했던걸 기억하시죠? 한번 init()함수를 만들어서 해볼까요? 초기화를 하면 이와 같이 나옵니다.

- ![View 초기화](./image/13.png)

3. 이제 music 폴더에서 음악 리스트를 가져올까요? 가져오기 앞서 위에서 언급했듯이 MashMallow 버전 이상 부터는 RuntimePermission 이라고 해서 사용중에 사용자로 부터 권한을 받아야지 파일 접근이 가능하답니다. 이를 한번 구현해볼까요?
`private boolean isPermitted;` 와 `public final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;` 를 상단에 추가하고 권한을 받아오는 코드를 추가해줍니다. 그리고 추가해준 `requestRuntimePermission` 메소드를 init메소드 끝에서 호출합니다.

- ![권한받아오기](./image/14.png)

- ![RuntimePermission](./image/15.png)

4. 이제 권한도 받아왔으니 Music folder에서 음악 리스트를 가져오는 것을 구현을 해볼까요? 음악 리스트를 가져오기전에 음악 정보들을 저장하기 위해 음악 파일에 대한 class를 만들어봅시다. 음악파일에는 어떤 정보가 필요할까요? 제목, 아티스트, 파일경로 정도를 알면 좋겠죠? 그러면 바로 만들어봅시다. Music 이라는 새로운 클래스를 만들어봅시다.

- ![Music class](./image/16.png)

- ![Music class](./image/17.png)

5. 다시 MainActivty class로 돌아갈까요? Music 정보들을 담은 배열을 선언해줍니다. `private ArrayList<Music> musicList = new ArrayList<>()`
6. 음악리스트를 가져오는 메소드를 구현해줍니다. getExternalStoragePublicDirectory메소드를 사용할건데 이 메소드는 공용 디렉토리에 있는 파일 정보를 가져올수 있습니다. 하지만 안드로이드 Q이상 부터는 deprecated 되서 다른 방법으로 구현을 해줘야되지만 이를 고려하지 않고 구현을 하겠습니다. 이 메소드를 통해서 musicList에는 음악 정보들이 저장이 될겁니다. 이 메소드를 사용하는 곳은 2군데가 있는데 아래 그림과 같이 선언을 해줍니다.

- ![음악정보 가져오기](./image/18.png)

- ![음악정보 가져오기](./image/19.png)

- ![음악정보 가져오기](./image/20.png)

7. 파일 정보도 가져왔으니 가져온 정보를 리스트로 출력을 해야겠죠? 저희가 선언한 `musicListView`를 통해서 리스트로 출력을 해줄겁니다. 리스트 뷰를 초기화를 해보겠습니다. init메소드 밑에 리스트를 출력해줄수 있게 만들어줍니다. 리스트뷰를 통해서 출력하기 위해서는 SimpleAdapter와 HashMap을 사용해서 제목과 아티스트를 출력하게 해줄수 있습니다.  해당 코드는 아래와 같습니다.

- ![음악정보 출력하기](./image/21.png)

- ![음악정보 출력하기](./image/22.png)

- ![음악정보 출력하기](./image/23.png)

8. 리스트 출력도 잘되는걸 확인할 수 있습니다. 다음에는 클릭했을때 몇번째 아이템을 눌렀는지 확인을 하고 싶을 껍니다. 해당 position을 받아오기 위해서 `setOnItemClickListener`라는 callback 메소드를 쓸겁니다. 받아온 position을 통해 현재 음악재생할 위치 정보를 저장해줍니다. 다음 코드와 같이 사용하면 됩니다.

- ![리스트 클릭 위치 가져오기](./image/24.png)

9. 이제 음악 재생 기능을 구현할 겁니다. 음악을 재생하기 위해서 MediaPlayer를 사용할 겁니다. 그리고 리스트 위치를 통해서 음악플레이를 하기위한 메소드를 선언합니다. 음악을 재생할때 제목과 버튼 이미지를 변경 해줍니다. 코드는 다음과 같습니다.

- ![음악 재생](./image/25.png)

- ![음악 재생](./image/26.png)

10. 거의 다 끝나갑니다 ! 음악도 재생되니 버튼을 눌렀을 때 처리를 해야겠죠! 버튼을 눌렀을때 이벤트를 처리를 하기 위해서 onClickListener를 사용할겁니다. 모든 View들은 클릭 이벤트를 받을 수 있습니다. 해당 코드는 아래와 같습니다. 버튼을 클릭하면 onClick 메소드가 불려지게 됩니다.

- ![음악 재생](./image/27.png)

11. 음악 재생 버튼을 누를때는 일시 정지였을때는 음악을 재생을 해야되고 플레이중에는 일시정지를 해야겠죠? 그리고 리스트를 클릭하지 않고 바로 재생버튼을 누르게하면 첫번째 곡을 재생하게 합니다. 처음에 `currentPlayingPosition`변수를 -1로 초기화를 해주어서 플레이했던 여부를 판단해줍니다.

- ![음악 재생](./image/28.png)

12. 다음 버튼을 누르거나 이전곡으로 이동하는 버튼을 눌렀을때 증가 하거나 감소할때 0보다 커야되고 리스트 사이즈보다 작아야되는것을 감안하고 구현을 해줍니다.

 - ![이전 버튼 다음 버튼 클릭 구현](./image/29.png)

13. 마지막 입니다. 음악 앱을 종료를 했을 때 MediaPlayer 리소스를 반환을 해주어야 되기 때문에 생명주기인 메소드 onDestroy에서 MediaPlayer 리소스를 반환합니다.

 - ![onDestroy](./image/30.png)

## 마무리

- 여기까지 따라오시느라 수고 많으셨습니다. 좋은 경험이 되셨기를 바랍니다. 하다가 막히는게 있으면 issue에 등록을 하거나 저한테 DM을 날려주시면 감사하겠습니다. 다시 한번 수고 많으셨습니다.
