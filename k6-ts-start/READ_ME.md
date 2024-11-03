# k6 테스트

## TS로 진행
- TS로 진행하기 위해 번들러를 통해 TS를 JS로 변환 후 실행한다.
- k6는 로컬에 설치하고 NPM으로 설치하지 않는다.

```shell
brew install k6
```

```shell
npm run bundle
```

```shell

k6 run dist/http-test.js
```
