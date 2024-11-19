
![image (1)](https://github.com/user-attachments/assets/ddf9ae1f-c33e-4b31-8da7-8919dd9d540d)

<h1 align="center" style="font-size: 2.8rem; font-weight: bold; color: #4A90E2; margin-top: 20px;">
  🎟️ OhapJijole Ticket & Auction 🎟️
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

## 👥 **팀원 역할**

<div align="center">

| **이름**       | **역할**             | **GitHub**                        |
|----------------|---------------------|----------------------------------|
| 🚀 홍기평      | ?????????????????  | [GitHub](https://github.com/)    |
| 💻 배진관      | ?????????????????? | [GitHub](https://github.com/)    |
| 🔧 황우석      | ?????????????????? | [GitHub](https://github.com/)    |
| 📅 김진비      | ?????????????????? | [GitHub](https://github.com/)    |
| 📝 노현지      | ?????????????????? | [GitHub](https://github.com/)    |

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

## 🔧 **기술적 고도화**

<p align="center">
  <strong>"시스템 안정성과 성능 최적화를 위한 설계를 실현합니다."</strong>
</p>

| 🎨 **카테고리**       | 🌟 **기술**                          | 📌 **적용 위치**           | 📝 **설명**                                                                                 |
|----------------------|-------------------------------------|--------------------------|-------------------------------------------------------------------------------------------|
| ⚙️ **동시성 관리**   | 🛡️ **Redisson RLock**               | 캐시 서버 및 공유 자원     | 입찰 시 발생하는 데이터 경합 문제를 해결하고, 분산 환경에서 안전한 데이터 처리를 보장합니다.          |
| ⚙️ **동시성 관리**   | ⚡ **AWS Lambda**                   | SQS 트리거                | 대규모 메시지를 병렬로 처리하여 높은 트래픽 환경에서도 안정적이고 효율적으로 운영할 수 있습니다.          |
| 🔗 **RDS-Proxy**     | 🖧 **RDS-Proxy**                   | Lambda와 RDS 연결 사이    | 연결 풀링을 통해 RDS의 부하를 줄이고, 처리량 및 성능을 최적화하여 안정적인 데이터 접근을 지원합니다.      |
| 🗄️ **캐싱 전략**    | 🚀 **Redis**                       | 캐시 서버                  | 실시간 데이터 조회 성능을 극대화하며, 경매와 같은 고빈도 읽기 작업에서 빠른 응답을 보장합니다.            |
| 🗄️ **캐싱 전략**    | 🗄️ **AWS ElastiCache**             | 인메모리 캐시              | 데이터베이스 부하를 줄이고, 자주 조회되는 데이터를 캐싱하여 응답 속도를 대폭 향상시킵니다.                |

---

### 🎯 **기술적 구현 방식 및 효과**

1. **동시성 관리**
   - **Redisson RLock**: 분산 락 구현을 통해 경매 입찰과 같은 중요한 이벤트에서 데이터 정합성을 유지합니다.
   - **AWS Lambda**: SQS 대기열에서 병렬로 메시지를 처리하여 대규모 트래픽 환경에서도 높은 안정성을 제공합니다.  
     특히, Lambda의 동시성 제어와 배치 크기 설정으로 효율적인 처리 성능을 달성했습니다.

2. **RDS-Proxy**
   - Lambda와 RDS 사이의 연결을 효율적으로 관리하여 트래픽 급증 시에도 데이터베이스의 안정성을 유지합니다.
   - 연결 수를 줄이고, CPU와 메모리 사용량을 최적화하여 처리량(Throughput)을 대폭 개선했습니다.

3. **캐싱 전략**
   - **Redis**는 상품 조회와 같은 고빈도 읽기 작업을 최적화하여 응답 속도를 크게 향상시켰습니다.
   - **AWS ElastiCache**는 자주 요청되는 데이터를 캐싱하여 데이터베이스 부하를 줄이고, 사용자 경험을 강화합니다.

---

### 🛠️ **기술적 고도화의 종합 효과**
- **성능 개선**: Redis와 RDS-Proxy를 통해 데이터 처리 속도가 대폭 향상.
- **안정성 강화**: 대규모 트래픽 환경에서도 안정적인 서비스 운영 가능.
- **확장성 보장**: Lambda와 ElastiCache를 활용하여 동적으로 확장 가능한 구조 구현.

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
