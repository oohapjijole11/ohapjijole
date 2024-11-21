
![image (1)](https://github.com/user-attachments/assets/ddf9ae1f-c33e-4b31-8da7-8919dd9d540d)

<h1 align="center" style="font-size: 2.8rem; font-weight: bold; color: #4A90E2; margin-top: 20px;">
   OhapJijole Ticket & Auction 
</h1>

<p align="center" style="font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; font-size: 1.2rem; color: #333;">
  <strong>실시간 경매와 티켓팅을 통해 사용자들에게 <span style="color: #FF5722;">몰입감 넘치는 경험</span>을 선사합니다!</strong>
</p>

<div align="center" style="margin-top: 20px;">
  <img src="https://img.shields.io/badge/PROJECT-OhapJijole-blue?style=for-the-badge&logo=appveyor" alt="Project Badge">
</div>

---

## 🏁 **서비스 소개**
<div>
    <strong>OhapJijole Ticket & Auction</strong>은 제한된 수량의 티켓이나 상품을 실시간 경매 형식으로 제공하는 서비스입니다.  
    사용자는 등급 및 티켓 보유 상황에 따라 경매에 참여하며, <span style="color: #FF5722; font-weight: bold;">경쟁 입찰을 통해 원하는 상품</span>을 획득할 수 있습니다.  
    최적화된 경매 경험과 효율적인 입찰 시스템으로 사용자 만족도를 극대화합니다.

</div>

## **기술 스택**

<div align="center">
  <img src="https://skillicons.dev/icons?i=java,spring,mysql,aws,docker,githubactions,redis" alt="Tech Stack" />
</div>

##  **주요 기능**

### **티켓 발급: SQS 및 Lambda를 통한 비동기 처리**

<div>
  <ul>
    <li> 대용량 트래픽 수용: SQS 및 Lambda를 활용한 비동기 티켓 발급 시스템.</li>
    <li> 동시성 제어: Lambda와 RDS-Proxy를 통한 안정적인 티켓 발급.</li>
    <li> 확장성: 서버리스 아키텍처 채택으로 자동 확장.</li>
  </ul>
</div>

---

# 🔑 **KEY SUMMARY**
## **1️⃣ SQS 메시지 처리 성능 개선**
### **문제점**
- SQS 메시지 처리 중 RDS 연결 수 과부하 발생.
- 대규모 트래픽에서 시스템 처리량 감소와 사용자 응답 속도 저하.

### **해결 방안**
1. **Redis 캐싱 도입**  
   - 자주 조회되는 데이터를 캐시로 저장해 RDS 접근 최소화.  
   - 데이터베이스 부하 감소, 읽기 요청 속도 향상.

2. **RDS-Proxy 활용**  
   - 연결 풀링으로 RDS 연결 효율성 증대.  
   - Lambda와 연결 관리 최적화로 안정성 확보.


### **결과**
| **항목**          | **도입 전** | **도입 후** | **효과**                          |
|-------------------|------------|------------|-----------------------------------|
| **Connection 수** | 10         | 2          | 연결 관리 효율 **5배** 향상       |
| **처리량**        | 65 TPS     | 50 TPS     | 시스템 안정성 및 확장성 대폭 개선 |

---

## **2️⃣ 입찰 처리 성능 최적화** 

### **문제점**
- 입찰 100회 평균 응답 시간 **3473ms**로 비효율적.  
- 사용자 경험 저하와 실시간 처리 요구사항 미충족.



### **해결 방안**
1. **캐시 기반 최고 입찰가 읽기**  
   - 기존 DB 직접 조회를 캐시 데이터 조회로 변경.  
   - 읽기 요청 속도 대폭 개선.

2. **분산락 범위 최적화**  
   - 락 적용 범위를 데이터 읽기 및 저장 관련 메서드로 축소.  
   - 병렬 처리 성능 개선.

3. **Redis 캐시 활용**  
   - 입찰 권한 체크를 캐시에 저장해 중복 조회 최소화.  
   - 빠른 응답 제공 및 데이터베이스 요청 감소.



### **결과**
| **항목**               | **개선 전**  | **개선 후**   | **효과**                                  |
|------------------------|--------------|---------------|-------------------------------------------|
| **입찰 평균 응답 시간**     | 3473ms       | 476ms         | 처리 속도 약 **7배** 개선, 효율 극대화    |
| **최고 입찰가 읽기**   | DB 직접 조회  | 캐시 데이터    | 데이터 읽기 속도 대폭 향상                |
| **락 처리 범위**       | 메서드 전체   | 특정 메서드    | 불필요한 락 제거로 동시 처리 성능 향상     |
| **입찰 권한 체크**     | DB 중복 조회  | Redis 캐시     | 중복 조회 감소, 빠른 응답 제공             |

---
### **트러블 슈팅: 불필요한 커넥션 점유 해결**

<details>
  <summary><strong> 문제 배경 및 정의</strong></summary>
  <h3> 배경</h3>
  <p>대규모 데이터 처리 중 <strong>RDS</strong>에서 불필요한 연결 점유가 발생하였습니다.</p>
  <p>연결 제한 초과로 인해 데이터 처리 지연 문제가 발생하였습니다.</p>

  <h3>문제</h3>
  <ul>
    <li>RDS 연결 수를 확장하려 했으나, RDS 버전의 최대 연결 제한으로 한계 발생.</li>
    <li>과도한 연결 점유로 인해 <strong>성능 저하 및 장애</strong>가 발생.</li>
  </ul>
</details>

<details>
  <summary><strong> 해결 방안 및 적용 내용</strong></summary>
  <h3> 해결 방안</h3>
  <ol>
    <li>
      <strong>RDS Proxy 활용:</strong>
      <ul>
        <li>RDS Proxy를 통해 <strong>연결 재활용 및 최적화</strong>.</li>
        <li>테스트 시 Gradual Ramp-Up 방식을 적용해 <strong>RAM 사용량</strong>을 조절.</li>
      </ul>
    </li>
    <li>
      <strong>HikariCP 설정:</strong>
      <ul>
        <li>Spring Boot에서 HikariCP를 사용해 <strong>최대/최소 연결 값</strong> 조정.</li>
        <li>
          주요 설정:
          <ul>
            <li><code>maximumPoolSize</code>: 최대 연결 수 제한 설정.</li>
            <li><code>minimumIdle</code>: 최소 연결 수를 설정해 불필요한 연결 감소.</li>
          </ul>
        </li>
      </ul>
    </li>
  </ol>

  <h3> 적용 내용</h3>
  <ul>
    <li>
      <strong>RDS Proxy:</strong>
      <ul>
        <li>기존 연결을 재활용하여 <strong>연결 풀 고갈 방지</strong>.</li>
        <li>연결 관리 효율성을 대폭 개선.</li>
      </ul>
    </li>
    <li>
      <strong>HikariCP:</strong>
      <ul>
        <li><strong>최적 연결 수 관리</strong>로 성능 향상.</li>
        <li>연결 점유 최소화로 리소스 낭비 감소.</li>
      </ul>
    </li>
  </ul>
</details>

<details>
  <summary><strong> 결과 및 회고</strong></summary>
  <h3>결과</h3>
  <ul>
    <li>
      <strong>RDS Proxy:</strong>
      <ul>
        <li>연결 풀 효율성 증가.</li>
        <li>대규모 트래픽 처리 시 <strong>안정성 확보</strong>.</li>
      </ul>
    </li>
    <li>
      <strong>HikariCP:</strong>
      <ul>
        <li>연결 점유 문제 완화.</li>
        <li>처리 속도 약 <strong>30%</strong> 향상 및 오류 발생 빈도 <strong>0%</strong> 달성.</li>
      </ul>
    </li>
  </ul>

  <h3> 회고</h3>
  <ul>
    <li>
      <strong>장점:</strong>
      <ul>
        <li>RDS Proxy는 연결 재활용과 안정성 측면에서 매우 유용.</li>
        <li>HikariCP는 설정 변경만으로 빠른 성능 최적화 가능.</li>
      </ul>
    </li>
    <li>
      <strong>단점:</strong>
      <ul>
        <li>RDS Proxy 설정 시 AWS Console 및 권한 관리에 추가 시간이 소요.</li>
        <li>테스트 환경에서는 RAM 사용량 증가로 리소스 관리 필요.</li>
      </ul>
    </li>
  </ul>
</details>

---

## **인프라 아키텍처 & 적용 기술**

 **적용 기술 정리**

| **카테고리**       |  **기술**                          | **적용 위치**           |  **설명**                                                                                 |
|----------------------|-------------------------------------|--------------------------|-------------------------------------------------------------------------------------------|
|  **동시성 관리**   |  **Redisson RLock**               | 캐시 서버 및 공유 자원     | 입찰 시 발생하는 데이터 경합 문제를 해결하고, 분산 환경에서 안전한 데이터 처리를 보장합니다.          |
|  **동시성 관리**   |  **AWS Lambda**                   | SQS 트리거                | 대규모 메시지를 병렬로 처리하여 높은 트래픽 환경에서도 안정적이고 효율적으로 운영할 수 있습니다.          |
|  **RDS-Proxy**     |  **RDS-Proxy**                   | Lambda와 RDS 연결 사이    | 연결 풀링을 통해 RDS의 부하를 줄이고, 처리량 및 성능을 최적화하여 안정적인 데이터 접근을 지원합니다.      |
|  **캐싱 전략**    |  **Redis**                       | 캐시 서버                  | 실시간 데이터 조회 성능을 극대화하며, 경매와 같은 고빈도 읽기 작업에서 빠른 응답을 보장합니다.            |
|  **캐싱 전략**    |  **AWS ElastiCache**             | 인메모리 캐시              | 데이터베이스 부하를 줄이고, 자주 조회되는 데이터를 캐싱하여 응답 속도를 대폭 향상시킵니다.                |

**인프라 아키텍처**

![최종이요 drawio](https://github.com/user-attachments/assets/1eff2a5d-0659-4d1b-aa9c-ef1305d91753)


![_cicd drawio](https://github.com/user-attachments/assets/cd915739-8e56-42d3-b270-556f4da20e32)

---

# 기술적 고도화

<details>
<summary>1. 경매 시작과 마감 자동화</summary>

### 기능 개요
- 경매의 시작시간과 마감시간을 현재시간과 비교하여 정확한 시간에 상태를 자동으로 변경.

### 주요 로직
- 생성된 경매들의 시작시간과 마감시간을 비교하여 상태를 정확히 변경.
- Lambda를 활용하여 상태 자동화.

### 배경
- 경매의 시작과 마감을 실시간으로 진행할 필요가 있었고, 정확한 시간에 상태 변경이 요구됨.

### 요구사항
- 정확한 시간에 경매의 시작과 마감이 이루어져야 하며, 일관성 있게 작동해야 함.

### 선택지
1. **Spring Boot의 `@Scheduled` 사용**:
   - 로컬 프로젝트에서 1분마다 DB를 수정.
2. **AWS EventBridge Scheduler 사용**:
   - Lambda 함수를 일정 시간마다 호출.

### 의사결정
- **EventBridge Scheduler 선택 이유**:
  - 정확한 트리거 시점과 실패 시 재시도 가능.
  - 여러 인스턴스에서 중복 실행 방지.
  - 예약 작업 실행 수에 따른 비용 절감.

### 회고
- **기술의 장단점**:
  - AWS는 VPC와 IAM 설정 등 초기 설정이 복잡.
  - Lambda는 Java 코드 수정 시 jar 파일 업로드로 번거로움.
  - AWS에 익숙한 개발자에게는 효율적이고 다양한 로그 확인 가능.
- **다시 시도한다면**:
  - 대용량 데이터 처리와 세밀한 일정 관리를 위해 Spring Batch를 사용할 계획.

</details>

<details>
<summary>2. 성능 개선: 멀티스레드와 병렬 실행</summary>

### 성능 개선 및 코드 개선 요약
- Lambda 코드의 경매 상태 변화를 멀티스레드 병렬 실행으로 변경.

### 문제 정의
- 기존 단일 스레드 작업으로 인해 처리 속도가 느림.

### 가설
- 주기적인 데이터베이스 호출과 비효율적인 쿼리 호출로 성능 저하 발생 가능.

### 해결 방안
1. **문제 해결 의사결정**:
   - 시작과 마감 쿼리를 하나로 합쳐 DB 호출을 줄임.
   - 단일 스레드에서 멀티스레드로 병렬 실행 변경.
2. **해결 과정**:
   - 데이터베이스 호출을 두 번에서 한 번으로 줄여 리소스 낭비 제거.
   - ThreadPool을 사용해 최대 100개의 스레드로 병렬 실행 가능.

### 해결 완료
- **결과**:
  - 병렬 실행으로 평균 처리 속도가 15초에서 5~10초로 단축.
- **전후 데이터 비교**:
  - 성능 개선 전: 평균 15초 소요.
  - 성능 개선 후: 평균 5~10초 소요.

### 회고
- **기술의 장단점**:
  - 병렬 실행으로 대량 데이터 처리 속도가 개선되었으나, 제한된 스레드 초과 시 성능 저하 가능.
- **다시 시도한다면**:
  - AWS ElasticCache를 적용하여 속도를 더 개선하고 DB 접근 빈도를 줄일 계획.

</details>

<details>
<summary>3. 동시성 및 대규모 트래픽 관리</summary>

### 기능 개요
- SQS 및 Lambda 기반의 티켓 구매 대기열 시스템 구현.

### 주요 로직
1. SQS로 대규모 요청 분산 처리.
2. Lambda를 활용한 효율적 대기열 데이터 처리.
3. RDS Proxy를 통한 DB 연결 효율화.

### 선택지
1. Lambda + SQS 배치 사이즈 최적화.
2. Redis 기반 메모리 캐싱.

### 의사결정
- Lambda와 SQS 최적화를 통해 트래픽 처리 안정성 확보.

### 성능 개선
- 변경 전: SQS 단일 호출로 성능 저하.
- 변경 후: SQS와 Lambda 최적화로 속도 개선.
</details>

## **팀 구성원**

> **오합지졸 프로젝트 멤버**

<div align="center">
  <table>
    <tr>
      <td align="center">
        <b>홍기평</b><br>
      </td>
      <td align="center">
        <b>배진관</b><br>
      </td>
      <td align="center">
        <b>황우석</b><br>
      </td>
      <td align="center">
        <b>김진비</b><br>
      </td>
      <td align="center">
        <b>노현지</b><br>
      </td>
    </tr>
  </table>
</div>
