

## Builds

### Debug build 
• Jar및, kDoc함께 생성 (projectDir/debug/)

### Release
• Proguard 적용

### Gradle Task, exportJar (Optional)
• Jar format 의 library 를 build 한다. 해당 모듈 프로젝트의 release 폴더에 생성되며, kDoc 도 함께 생성된다.

## Usage Guide
• 기본사용은 SimpleDataManager 를 통해 가져온 requester 를 통해 동작을 기본으로 하고있으며
  해당 requester 에 timeout, header, body, query 를 전달하도록 한다.

### Requester
• connect() : 받고자 하는 형태의 데이터를 받기위해 parser 를 전달 하게 되면 해당 포맷으로 변환하여 return 한다.
• setBody() : body 정보를 전달 할때, requestBodyParser 를 함께 전달하게 되면, object 를 body 에 담을 수 있다.

### JsonParser
• JsonParer : toJson : String 과 parsing 하고 자 하는 type을 전달 받아 Object성 생
              fromJson : Object 를 String 으로 변환
