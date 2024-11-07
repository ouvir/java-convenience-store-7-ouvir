# java-convenience-store-precourse

## 기능 요구 사항
구매자의 할인 혜택과 재고 상황을 고려하여 최종 결제 금액을 계산하고 안내하는 결제 시스템을 구현한다.

- 사용자가 입력한 상품의 가격과 수량을 기반으로 최종 결제 금액을 계산한다.  
  - 총 구매액은 상품별 가격과 수량을 곱하여 계산하며, 프로모션 및 멤버십 할인 정책을 반영하여 최종 결제 금액을 산출한다.
- 구매 내역과 산출한 금액 정보를 영수증으로 출력한다.
- 영수증 출력 후 추가 구매를 진행할지 또는 종료할지를 선택할 수 있다.
- 사용자가 잘못된 값을 입력할 경우 `IllegalArgumentException`를 발생시키고,  
  `[ERROR]`로 시작하는 에러 메시지를 출력 후 그 부분부터 입력을 다시 받는다.
  - `Exception`이 아닌 `IllegalArgumentException`, `IllegalStateException` 등과 같은 명확한 유형을 처리한다.

### 재고 관리
- 각 상품의 재고 수량을 고려하여 결제 가능 여부를 확인한다.
  - (재고에서 수량 확인- 일반 재고와 프로모션 재고 둘다 확인)
- 고객이 상품을 구매할 때마다, 결제된 수량만큼 해당 상품의 재고에서 차감하여 수량을 관리한다.
  - (결제 완료 시, 재고에서 차감)
  - 병렬 진행은 가정하지 않는다! (손님은 1명씩 들어온다고 생각)
- 재고를 차감함으로써 시스템은 최신 재고 상태를 유지하며, 다음 고객이 구매할 때 정확한 재고 정보를 제공한다.
  - 편의점 재고에 대한, write 연산 필요해보임 -> products.md 에

### 프로모션 할인
- 오늘 날짜가 프로모션 기간 내에 포함된 경우에만 할인을 적용한다.
  - 프로모션에 해당하는 상품인지 확인(프로모션 도메인에서 해당 상품이 존재하는지 확인)
- 프로모션은 N개 구매 시 1개 무료 증정(Buy N Get 1 Free)의 형태로 진행된다.
- 1+1 또는 2+1 프로모션이 각각 지정된 상품에 적용되며, 동일 상품에 여러 프로모션이 적용되지 않는다.
  - 프로모션은 단일 값
- 프로모션 혜택은 프로모션 재고 내에서만 적용할 수 있다.
  - 프로모션 재고를 살때만, 혜택 적용
- 프로모션 기간 중이라면 프로모션 재고를 우선적으로 차감하며, 프로모션 재고가 부족할 경우에는 일반 재고를 사용한다.
  - 프로모션 재고를 일반 재고보다 먼저 사용, 프로모션 재고가 떨어지면, 일반 재고 사용 시작
- 프로모션 적용이 가능한 상품에 대해 고객이 해당 수량보다 적게 가져온 경우, 필요한 수량을 추가로 가져오면 혜택을 받을 수 있음을 안내한다.
  - 프로모션 적용이 가능한 상품 -> 프로모션 재고가 1개 이상인 상품
- 프로모션 재고가 부족하여 일부 수량을 프로모션 혜택 없이 결제해야 하는 경우, 일부 수량에 대해 정가로 결제하게 됨을 안내한다.
  - 프로모션 재고가 0개이며, 일반 재고에서만 살 수 있는 경우 안내하기
  
### 멤버십 할인
- 멤버십 회원은 프로모션 미적용 금액의 30%를 할인받는다. 
  - (일반 구매 상품에 대해서만 30% 할인)
- 프로모션 적용 후 남은 금액에 대해 멤버십 할인을 적용한다.
  - (금액에 프로모션 할인을 적용한 뒤 남은 금액에 추가로 멤버십 할인을 적용 - 순서)
    - 금액이 음수가 될 수도 있음(예외처리 필요)
- 멤버십 할인의 최대 한도는 8,000원이다.
  - (한번 결제 마다, 최대 한도라고 가정)

### 영수증 출력
- 영수증은 고객의 구매 내역과 할인을 요약하여 출력한다.
- 영수증 항목은 아래와 같다.
  - 구매 상품 내역: 구매한 상품명, 수량, 가격
  - 증정 상품 내역: 프로모션에 따라 무료로 제공된 증정 상품의 목록
  - 금액 정보
    - 총구매액: 구매한 상품의 총 수량과 총 금액
    - 행사할인: 프로모션에 의해 할인된 금액
      - (증정상품 내역을 통해, 계산 가능한 금액)
    - 멤버십할인: 멤버십에 의해 추가로 할인된 금액
      - (멤버십 할인 정책 필요 - 총 구매액을 통해 계산)
    - 내실돈: 최종 결제 금액
- 영수증의 구성 요소를 보기 좋게 정렬하여 고객이 쉽게 금액과 수량을 확인할 수 있게 한다.



## 입출력 요구 사항
### 입력
- 구현에 필요한 상품 목록과 행사 목록을 파일 입출력을 통해 불러온다.
  - src/main/resources/products.md과 src/main/resources/promotions.md 파일을 이용한다.  
    - 두 파일 모두 내용의 형식을 유지한다면 값은 수정할 수 있다.
- 구매할 상품과 수량을 입력 받는다. 
  - 상품명, 수량은 하이픈(-)으로, 개별 상품은 대괄호([])로 묶어 쉼표(,)로 구분한다.
- 프로모션 적용이 가능한 상품에 대해 고객이 해당 수량보다 적게 가져온 경우, 그 수량만큼 추가 여부를 입력받는다.
- 프로모션 재고가 부족하여 일부 수량을 프로모션 혜택 없이 결제해야 하는 경우, 일부 수량에 대해 정가로 결제할지 여부를 입력받는다.
- 멤버십 할인 적용 여부를 입력 받는다.
- 추가 구매 여부를 입력 받는다.

### 출력
- 환영 인사와 함께 상품명, 가격, 프로모션 이름, 재고를 안내한다. 만약 재고가 0개라면 재고 없음을 출력한다
- 프로모션 적용이 가능한 상품에 대해 고객이 해당 수량만큼 가져오지 않았을 경우, 혜택에 대한 안내 메시지를 출력한다.
- 프로모션 재고가 부족하여 일부 수량을 프로모션 혜택 없이 결제해야 하는 경우, 일부 수량에 대해 정가로 결제할지 여부에 대한 안내 메시지를 출력한다.
- 멤버십 할인 적용 여부를 확인하기 위해 안내 문구를 출력한다.
- 구매 상품 내역, 증정 상품 내역, 금액 정보를 출력한다. 
- 추가 구매 여부를 확인하기 위해 안내 문구를 출력한다.
- 사용자가 잘못된 값을 입력했을 때, "[ERROR]"로 시작하는 오류 메시지와 함께 상황에 맞는 안내를 출력한다.
  - 구매할 상품과 수량 형식이 올바르지 않은 경우: [ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.
  - 존재하지 않는 상품을 입력한 경우: [ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요.
  - 구매 수량이 재고 수량을 초과한 경우: [ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.
  - 기타 잘못된 입력의 경우: [ERROR] 잘못된 입력입니다. 다시 입력해 주세요.


## 라이브러리
camp.nextstep.edu.missionutils에서 제공하는 DateTimes 및 Console API를 사용하여 구현해야 한다.
- 현재 날짜와 시간을 가져오려면 camp.nextstep.edu.missionutils.DateTimes의 `now()`를 활용한다.
- 사용자가 입력하는 값은 camp.nextstep.edu.missionutils.Console의 `readLine()`을 활용한다.


# 기능 분리(흐름 기준 분리)
- 0.필요기능
  - 잘못 입력시, 메시지 출력과 단계 다시 진행
  - app 실행시 재고 데이터 READ 로 재고 객체 업데이트
  - app 실행시 프로모션 데이터 READ

- 환영인사 출력
- 1.재고 안내 메시지 출력
- 2.구매할 상품과 수량 입력 받기
  - 입력 예외 처리(split)
  - 구매 상품 재고 확인
    - 프로모션 재고 확인 -> 일반 재고 확인(순서)
    - 재고 부족 시, 예외 처리

- 3.구매 상품에 대해 프로모션 적용
  - 프로모션 개수 부족 시, 일부 수량에 대해 정가로 결제할지 여부 메시지(Y,N)
    - 프로모션 개수 부족 조건(프로모션 제품 재고 개수 < 총 구매 수량)
    - (담은 프로모션 상품 개수 % (buy + get)) + 일반 상품 개수 => 프로모션 적용 불가 상품 개수
    - Y: 그대로 담기
      - 프로모션 적용 불가 상품 개수 중 프로모션 상품이였던 재고는 일반 상품 카트로 이동
    - N: 프로모션 적용 불가 상품 개수만큼 제거(일반 상품에서 먼저 제거 -> 프로모션 상품에서 제거)

  - 프로모션 상품 추가 안내 메시지(Y, N) 
    - (담은 프로모션 상품 개수 % (buy + get)) 이 buy 인 경우 => 혜택을 받지 않은 경우
    - Y: 프로모션 혜택 상품 추가로 담기
    - N: 그대로 진행

- 5.멤버십 할인 적용 여부 입력 받기(Y, N)
  - Y: 멤버십 할인 금액 계산(일반 구매 상품의 금액에서 30프로 할인)
    - 멤버십 할인의 최대 한도는 8,000원
    - 30프로 나눌때, 소수점은 제거 필요.(첫째자리에서 올림 처리)
  - N: 적용 X

- 6.영수증 출력
  - 상품 명, 수량, 금액 출력 (금액: 상품 가격 * 수량)
  - 증정품 출력
  - 총 결제 금액 출력
    - 총 결제 금액 계산
  - 프로모션 할인 금액 출력
    - 프로모션 할인 금액 계산
  - 멤버십 할인 금액 출력
    - 멤버십 할인 금액 계산
  - 낼 돈 출력
    - 낼 돈 계산

- 7.재고 업데이트

- 8.추가 구매 여부 입력 받기(Y, N)
  - Y: 다시 처음으로
  - N: 시스템 종료하며, 재고 데이터 WRITE
---