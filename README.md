
![image (1)](https://github.com/user-attachments/assets/ddf9ae1f-c33e-4b31-8da7-8919dd9d540d)

<h1 align="center" style="font-size: 2.8rem; font-weight: bold; color: #4A90E2; margin-top: 20px;">
   OhapJijole Ticket & Auction 
</h1>

<p align="center" style="font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; font-size: 1.2rem; color: #333;">
  <strong>실시간 경매와 티켓팅을 통해 사용자들에게 <span style="color: #FF5722;">몰입감 넘치는 경험</span>을 선사합니다!</strong>
</p>

<div align="center" style="margin-top: 20px;">
  <img src="https://img.shields.io/badge/PROJECT-OhapJijole-blue?style=for-the-badge&logo=appveyor" alt="Project Badge">
  <img src="https://img.shields.io/github/languages/top/oohapjijole11/ohapjijole?color=green&style=for-the-badge" alt="Top Language">
  <img src="https://img.shields.io/github/last-commit/oohapjijole11/ohapjijole?color=purple&style=for-the-badge" alt="Last Commit">
</div>

---

## 🏁 **서비스 소개**
<div style="background-color: #f9f9f9; padding: 20px; border-radius: 10px; box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1); margin-top: 20px;">
  <p style="font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; font-size: 1.2rem; color: #333; line-height: 1.8;">
    <strong>OhapJijole Ticket & Auction</strong>은 제한된 수량의 티켓이나 상품을 실시간 경매 형식으로 제공하는 서비스입니다.  
    사용자는 등급 및 티켓 보유 상황에 따라 경매에 참여하며, <span style="color: #FF5722; font-weight: bold;">경쟁 입찰을 통해 원하는 상품</span>을 획득할 수 있습니다.  
    최적화된 경매 경험과 효율적인 입찰 시스템으로 사용자 만족도를 극대화합니다.
  </p>
</div>

---

## 📚 **기술 스택**

<div align="center">
  <img src="https://skillicons.dev/icons?i=java,spring,mysql,aws,docker,githubactions,redis" alt="Tech Stack" />
</div>

---

## 👥 팀 구성원

> **오합지졸 프로젝트 멤버**

<div align="center">
  <table>
    <tr>
      <td align="center">
        <img src="https://via.placeholder.com/100" alt="홍기평" width="100" height="100" style="border-radius: 50%;"/><br>
        <b>🚀 홍기평</b><br>
        <a href="https://github.com/">GitHub</a>
      </td>
      <td align="center">
        <img src="https://via.placeholder.com/100" alt="배진관" width="100" height="100" style="border-radius: 50%;"/><br>
        <b>💻 배진관</b><br>
        <a href="https://github.com/">GitHub</a>
      </td>
      <td align="center">
        <img src="https://via.placeholder.com/100" alt="황우석" width="100" height="100" style="border-radius: 50%;"/><br>
        <b>🔧 황우석</b><br>
        <a href="https://github.com/">GitHub</a>
      </td>
      <td align="center">
        <img src="https://via.placeholder.com/100" alt="김진비" width="100" height="100" style="border-radius: 50%;"/><br>
        <b>📅 김진비</b><br>
        <a href="https://github.com/">GitHub</a>
      </td>
      <td align="center">
        <img src="https://via.placeholder.com/100" alt="노현지" width="100" height="100" style="border-radius: 50%;"/><br>
        <b>📝 노현지</b><br>
        <a href="https://github.com/">GitHub</a>
      </td>
    </tr>
  </table>
</div>

---

## 🚀 **주요 기능**

### 🎟️ **티켓 발급: SQS 및 Lambda를 통한 비동기 처리**

<div align="center" style="background-color:#FDF5E6; border-radius: 8px; padding: 16px; margin: 16px 0;">
  <ul>
    <li>🚀 **대용량 트래픽 수용**: SQS 및 Lambda를 활용한 비동기 티켓 발급 시스템.</li>
    <li>🔒 **동시성 제어**: Lambda와 RDS-Proxy를 통한 안정적인 티켓 발급.</li>
    <li>⚙️ **확장성**: 서버리스 아키텍처 채택으로 자동 확장.</li>
  </ul>
</div>

---

## 🔑 **KEY SUMMARY**

- **문제점**: SQS 메시지 처리 시 RDS 과부하 발생.
- **해결 방안**: Redis 캐싱 도입 및 RDS-Proxy 활용.
- **결과**: 데이터 처리 속도 **3배 이상** 향상.

<div align="center">

| **항목**          | **도입 전** | **도입 후** | **효과**                          |
|-------------------|------------|------------|-----------------------------------|
| **Connection 수** | 10         | 2          | 연결 관리 효율 5배 향상           |
| **처리량**        | 65         | 50         | 시스템 안정성 및 확장성 대폭 개선 |

</div>

---

## 🛠️ **인프라 아키텍처 & 적용 기술**

<div align="center">
  <img src="https://user-images.githubusercontent.com/12345678/architecture.png" alt="Architecture Diagram" width="80%">
</div>

---

### 📚 **데이터베이스 및 캐싱**

<details>
  <summary>💾 <strong>데이터베이스와 캐싱, 근본이 중요해!</strong></summary>

  1. **Redis**  
     - 🚀 **적용 위치**: 캐시 서버  
     - 💡 **사용 이유**: 실시간 상품 조회를 🚤 레이싱카처럼 빠르게 만들기 위해.
  
  2. **RDS-Proxy**  
     - 🛠️ **적용 위치**: Lambda와 RDS 연결 사이  
     - 🌟 **사용 이유**: SQS와 Lambda가 대화를 나눌 때, **RDS의 과부하를 막는 든든한 중재자**.
     - 📈 **효과**: 트래픽이 많아도 안정적으로 처리, 효율적인 연결 수 관리.
</details>

---

### ⚡ **동시성**

<details>
  <summary>⏳ <strong>동시성 처리, 끝까지 기다려줘!</strong></summary>

  1. **Redisson**  
     - 🔗 **적용 위치**: 캐시 서버 및 분산락  
     - 🛡️ **사용 이유**:  
       - 데이터 정합성을 지키기 위해 **"잠금(lock)"**을 걸어주는 믿음직한 수문장.  
       - TTL(유효기간)로 타임세일 종료 후 **자동 데이터 정리**까지 책임!
  
  2. **AWS Lambda**  
     - ⚙️ **적용 위치**: AWS Lambda 트리거  
     - 💪 **사용 이유**: SQS 대기열의 수많은 메시지를 "한 방에" 처리.  
     - 🌐 **효과**: 대규모 트래픽도 여유롭게, 마치 "슈퍼컴퓨터 한 대"가 돕는 느낌!
</details>

---

### 📬 **메시징 시스템**

<details>
  <summary>✉️ <strong>메시지의 전달, 확실히 맡겼어요!</strong></summary>

  1. **AWS SQS**  
     - 📮 **적용 위치**: 서비스 간 비동기 통신  
     - 📜 **사용 이유**: FIFO(선입선출)를 지켜 **순서를 중요시하는 메시지 처리**를 위해.  
     - 🎯 **효과**: 메시지 처리의 신뢰성과 안정성 확보.
  
  2. **SSE (Server-Sent Events)**  
     - 🛎️ **적용 위치**: 경매와 티켓 구매  
     - 👀 **사용 이유**: 사용자가 실시간 입찰 상황을 눈으로 볼 수 있도록, 실시간 알림 띄우기!  
     - 💬 **효과**: "입찰 완료!"가 즉시 화면에 표시되니 사용자는 신나고 경쟁자는 긴장감 UP!
</details>

---

### 🤖 **자동화**

<details>
  <summary>🕒 <strong>자동화는 우리의 시간 친구</strong></summary>

  1. **AWS EventBridge Scheduler**  
     - 🕹️ **적용 위치**: 경매 시작 자동화  
     - ⏰ **사용 이유**: "경매를 시작합니다!"를 정확한 시간에 Lambda가 대신 시작.  
     - 💡 **효과**: 늦지도 빠르지도 않은 정확한 타이밍, 타임라인 완벽 유지.
</details>

---

### 🚀 **인프라 및 배포**

<details>
  <summary>🌐 <strong>배포와 관리, 안정적으로!</strong></summary>

  1. **GitHub Actions**  
     - 🚦 **적용 위치**: CI/CD 파이프라인  
     - 📈 **사용 이유**: "Push" 한 번으로 자동 빌드, 테스트, 배포까지.  
     - 🤖 **효과**: 개발자의 시간은 소중하니까요.
  
  2. **Docker**  
     - 📦 **적용 위치**: 애플리케이션 컨테이너화  
     - 🛳️ **사용 이유**: 코드가 어디서나 잘 실행되도록 컨테이너에 안전히 포장.  
     - 🌍 **효과**: 환경에 구애받지 않는 원활한 실행 보장.
    
   3. **AWS Elastic Container Registry (ECR)**  
     - 🗄️ **적용 위치**: 도커 이미지 저장소  
     - 🛡️ **사용 이유**: **AWS와 찰떡 호흡**으로 안전하고 확장 가능한 이미지 관리.

  4. **AWS ECS Fargate**  
     - ⚙️ **적용 위치**: 컨테이너 관리와 배포  
     - 🤹 **사용 이유**: 서버리스로 모든 것을 관리, 개발자는 기능 개발에만 집중!  
     - 🎯 **효과**: **스케일 업/다운**도 자동으로.

  5. **AWS Application Load Balancer (ALB)**  
     - 🔄 **적용 위치**: 트래픽 분산  
     - 💼 **사용 이유**: **SSL 종료** 및 트래픽 균형 조절로 보안과 성능 강화.

  6. **AWS ElastiCache**  
     - 🚀 **적용 위치**: 캐시 서버  
     - 💾 **사용 이유**: 자주 조회되는 데이터를 캐시에 저장해 응답 속도 극대화.

  7. **AWS RDS (Relational Database Service)**  
     - 🗂️ **적용 위치**: 관계형 데이터베이스  
     - 🔧 **사용 이유**: 관리형 DB 서비스로 데이터 안정성 + 고가용성 보장.
</details>

---

## 🔧 **적용 기술 정리**

| 🎨 **카테고리**       | 🌟 **기술**                          | 📌 **적용 위치**           | 📝 **설명**                                                                                 |
|----------------------|-------------------------------------|--------------------------|-------------------------------------------------------------------------------------------|
| ⚙️ **동시성 관리**   | 🛡️ **Redisson RLock**               | 캐시 서버 및 공유 자원     | 입찰 시 발생하는 데이터 경합 문제를 해결하고, 분산 환경에서 안전한 데이터 처리를 보장합니다.          |
| ⚙️ **동시성 관리**   | ⚡ **AWS Lambda**                   | SQS 트리거                | 대규모 메시지를 병렬로 처리하여 높은 트래픽 환경에서도 안정적이고 효율적으로 운영할 수 있습니다.          |
| 🔗 **RDS-Proxy**     | 🖧 **RDS-Proxy**                   | Lambda와 RDS 연결 사이    | 연결 풀링을 통해 RDS의 부하를 줄이고, 처리량 및 성능을 최적화하여 안정적인 데이터 접근을 지원합니다.      |
| 🗄️ **캐싱 전략**    | 🚀 **Redis**                       | 캐시 서버                  | 실시간 데이터 조회 성능을 극대화하며, 경매와 같은 고빈도 읽기 작업에서 빠른 응답을 보장합니다.            |
| 🗄️ **캐싱 전략**    | 🗄️ **AWS ElastiCache**             | 인메모리 캐시              | 데이터베이스 부하를 줄이고, 자주 조회되는 데이터를 캐싱하여 응답 속도를 대폭 향상시킵니다.                |


---

# ✨ **기술적 고도화**

<details>
<summary>🚀 **경매 시작 및 마감 자동화**</summary>

### 🔧 [구현한 기능]
- 경매의 시작시간과 마감시간을 현재시간과 비교하여 정확한 시간에 경매 상태를 자동으로 변경.

---

### 📋 [주요 로직]
- 생성된 경매들의 시작시간과 마감시간을 비교하여 상태를 정확히 변경.
- Lambda를 활용하여 상태 자동화.

---

### 🗂 [배경]
- 실시간으로 경매 시작과 마감을 처리해야 하는 요구사항이 있었습니다.

---

### 🛠 [선택지]
1. **Spring Boot의 `@Scheduled` 사용**: 1분마다 DB를 수정하는 방식.
2. **AWS EventBridge Scheduler 사용**: Lambda 함수를 일정 시간마다 호출.

---

### ✅ [의사결정 및 사유]
- **AWS EventBridge Scheduler**를 선택:
  - 예약된 작업의 정확한 트리거 시점과 실패 시 재시도 메커니즘 제공.
  - `@Scheduled`는 처리 지연 시 시간 문제가 발생할 가능성이 있어 제외.

---

### 📝 [회고]
- **장점**: 
  - AWS를 활용해 로그 추적과 자동화된 작업이 용이.
- **단점**: 
  - VPC와 IAM 설정에 시간이 소요되며, Lambda 코드의 유지보수가 번거로울 수 있음.
- **다시 시도한다면**: Spring Batch를 활용해 대용량 데이터를 더욱 세밀히 관리.

---

### ⚙️ [성능 개선]
- **변경 전**: 단일 스레드 처리, 평균 15초 소요.
- **변경 후**: 멀티스레드 병렬 실행 도입, 평균 5~10초로 단축.
- **적용 기술**:
  - ThreadPool을 사용해 최대 100개의 스레드로 병렬 실행.

</details>

---

<details>
<summary>🔗 **동시성 제어 및 입찰 처리**</summary>

### 🔧 [구현한 기능]
- 동시 입찰 문제 해결을 위해 Redis 기반의 Redisson 분산 락을 적용.

---

### 📋 [주요 로직]
1. **Hash 구조 활용**:
   - 입찰 데이터를 관리하고, 특정 경매 ID로 시작하는 모든 입찰 정보를 조회.
2. **최신 데이터 비교**:
   - Key 값을 기준으로 최신 데이터를 비교하여 최고 입찰가를 확인.
3. **락 최적화**:
   - 필요한 부분에만 락을 적용하여 효율을 극대화.

---

### 🗂 [배경]
- 비관적 락을 사용하면 DB 부하로 인해 실행 시간이 길어짐.
- Redis를 통해 메모리 기반 처리로 실행 속도 개선.

---

### 🛠 [선택지]
- 함수형 분산 락: 전역 락으로 동작.
- 메서드 분리: 필요한 부분에만 락 적용.

---

### ✅ [의사결정 및 사유]
- 메서드 분리 방식을 선택:
  - 불필요한 락 제거로 성능 개선 (2191ms → 1100ms).

---

### 📝 [회고]
- **장점**: Redis 기반으로 동시성 제어가 효율적.
- **단점**: TTL 관리와 락 설정이 복잡할 수 있음.
- **다시 시도한다면**: AOP 기반으로 락 적용 범위를 더 세밀히 조정.

</details>

---

<details>
<summary>📦 **동시성 및 대규모 트래픽 관리**</summary>

### 🔧 [구현한 기능]
- 대규모 트래픽 처리를 위한 SQS 및 Lambda 기반의 티켓 구매 대기열 시스템 구현.

---

### 📋 [주요 로직]
1. **SQS**:
   - 메시지 대기열을 사용해 대규모 요청을 분산 처리.
2. **Lambda**:
   - 트리거 기반으로 동작하며, 대기열 데이터를 효율적으로 처리.
3. **RDS Proxy**:
   - DB 연결 효율화를 통해 대량 트래픽 처리 최적화.

---

### 🗂 [배경]
- SQS 단일 사용으로 대용량 트래픽 처리 한계를 경험.
- Lambda와 Redis를 결합하여 성능 개선 필요.

---

### 🛠 [선택지]
1. Lambda + SQS 배치 사이즈 조절.
2. Redis 기반 메모리 캐싱.

---

### ✅ [의사결정 및 사유]
- Lambda와 SQS 배치 사이즈 최적화로 대규모 트래픽 처리 해결.

---

### 📝 [회고]
- **장점**:
  - Lambda의 자동 확장성과 AWS 통합으로 처리 효율 극대화.
- **단점**:
  - AWS 의존성이 높아 아키텍처의 유연성이 다소 떨어질 수 있음.
- **다시 시도한다면**:
  - Redis 중심으로 캐싱 구조를 개선하여 대기열 성능을 더욱 높일 계획.

---

### ⚙️ [성능 개선]
- **변경 전**: SQS 단일 호출 방식, 성능 저하 발생.
- **변경 후**: Lambda와 SQS 최적화, 대기열 처리 속도 향상.

</details>

---

<h1>🛠️ 트러블 슈팅: 불필요한 커넥션 점유 해결</h1>

<details>
  <summary><strong>📌 문제 배경 및 정의</strong></summary>
  <h3>📚 배경</h3>
  <p>대규모 데이터 처리 중 <strong>RDS</strong>에서 불필요한 연결 점유가 발생하였습니다.</p>
  <p>연결 제한 초과로 인해 데이터 처리 지연 문제가 발생하였습니다.</p>

  <h3>⚠️ 문제</h3>
  <ul>
    <li>RDS 연결 수를 확장하려 했으나, RDS 버전의 최대 연결 제한으로 한계 발생.</li>
    <li>과도한 연결 점유로 인해 <strong>성능 저하 및 장애</strong>가 발생.</li>
  </ul>
</details>

<details>
  <summary><strong>🚀 해결 방안 및 적용 내용</strong></summary>
  <h3>✅ 해결 방안</h3>
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

  <h3>💡 적용 내용</h3>
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
  <summary><strong>🎯 결과 및 회고</strong></summary>
  <h3>📈 결과</h3>
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

  <h3>🔍 회고</h3>
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

## 📜 **Ground Rules**
- 🚨 **문제 발생 시 팀과 즉시 공유**.
- 💻 **화면 공유를 통한 투명한 협업 유지**.
- ❓ **모든 질문은 즉시 해결**.

---
## 🔮 **미래 계획**
- **Java 21 가상 스레드** 기반 고성능 처리.
- **AI 추천 시스템**으로 개인 맞춤형 입찰 전략 제공.
- 글로벌 사용자 대상 경매 이벤트 개최.
---

<p align="center">
  <strong>"결과적으로, OhapJijole는 안정성과 확장성을 모두 갖춘 시스템으로, 대규모 사용자 트래픽에도 견고하게 운영됩니다."</strong>
</p>
