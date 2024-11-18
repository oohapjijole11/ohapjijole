# 🎉 **OhapJijole Ticket & Auction Service** 🎉

> **"OhapJijole Ticket & Auction"**  
> 실시간 경매와 티켓팅 서비스를 통해 사용자들에게 몰입감 넘치는 구매 경험을 제공합니다!
> ![image (1)](https://github.com/user-attachments/assets/fec142e2-6876-42c4-8e48-ff6440be1a24)


---

<div align="center">
  <img src="https://img.shields.io/badge/PROJECT-OhapJijole-blue?style=for-the-badge&logo=appveyor"/>
</div>

## 🏁 **서비스 소개**
“**OhapJijole Ticket & Auction**”은 한정된 수량의 티켓이나 상품을 실시간 경매 형태로 제공하는 티켓팅 옥션 서비스입니다.  
사용자는 등급과 보유한 티켓에 따라 경매에 참여하고, **경쟁 입찰을 통해 높은 금액을 제시한 사용자가 원하는 상품을 획득**할 수 있습니다.?????



---

## 📚 **Tech Stacks**
<p align="center">
  <img src="https://img.shields.io/badge/Java-007396?style=flat-square&logo=java&logoColor=white">
  <img src="https://img.shields.io/badge/SpringBoot-6DB33F?style=flat-square&logo=springboot&logoColor=white">
  <img src="https://img.shields.io/badge/AWS-Lambda-FF9900?style=flat-square&logo=awslambda&logoColor=white">
  <img src="https://img.shields.io/badge/MySQL-4479A1?style=flat-square&logo=mysql&logoColor=white">
  <img src="https://img.shields.io/badge/Redis-C925D1?style=flat-square&logo=redis&logoColor=white">
  <img src="https://img.shields.io/badge/GitHubActions-2088FF?style=flat-square&logo=githubactions&logoColor=white">
  <img src="https://img.shields.io/badge/Docker-2496ED?style=flat-square&logo=docker&logoColor=white">
</p>

---

## 🎯 **프로젝트 목표**

1. **대규모 트래픽 대응**
    <details>
    <summary>🔍 자세히 보기</summary>
    
    - **SQS, RDS-Proxy 활용**: 비동기 처리를 통해 API 요청을 **49req/sec 이하**로 안정성을 확보합니다.
    - **동시성 처리**: SQS의 배치 크기와 Lambda의 처리량을 활용하여 **100단위의 동시성**을 처리합니다.
    
    </details>

2. **성능 최적화**
    <details>
    <summary>🔍 자세히 보기</summary>
    
    - **Redis 캐싱**: 경매 상품 정보와 실시간 입찰 현황을 Redis에 캐싱하여 데이터베이스 부하를 줄이고 **속도를 28% 향상**시킵니다.
    - **RDS Proxy 사용**: CPU를 효율적으로 관리하여 데이터베이스 성능을 최적화합니다.
    
    </details>

3. **운영 및 배포 효율화**
    <details>
    <summary>🔍 자세히 보기</summary>
    
    - **CI/CD 파이프라인 구축**: Docker와 GitHub Actions를 이용하여 **배포 자동화**를 구현합니다.
    - **ECS Fargate 활용**: 컨테이너 기반의 확장 가능한 서비스를 **배포**합니다.
    - **ALB Health Check**: 비정상적인 인스턴스를 자동으로 제외시켜 **안정적인 운영**을 보장합니다.
    
    </details>

4. **데이터 일관성 및 트랜잭션 관리**
    <details>
    <summary>🔍 자세히 보기</summary>
    
    - **FIFO 패턴 관리**: SQS를 이용하여 티켓 구매 인원의 메모리 순서를 **FIFO(선입선출)** 패턴으로 관리합니다.
    - **중복 메시지 제거**: SQS FIFO의 중복 ID 그룹과 메시지 그룹 ID를 통해 **중복 메시지**를 제거합니다.
    - **Redisson 활용**: 입찰 데이터의 **일관성을 보장**하기 위해 Redisson을 사용합니다.
    
    </details>
---
## 🔑 **KEY SUMMARY**

### **성능 개선: 최저가 상품 조회 성능, Redis 도입으로 3배 이상 향상**
<details>
  <summary>🔍 도입 배경</summary>
  
  - **문제점**:  
    SQS의 메시지를 Lambda 함수에서 처리할 때 RDS에 과도한 부하 발생
  
  - **필요성**:  
    RDS 부하 감소, 안정성 향상, 연결 개수 관리 기능 필요
</details>

<details>
  <summary>📊 한줄 요약</summary>
  
  | <span style="color:#4CAF50">**항목**</span> | <span style="color:#FF5722">**도입 전**</span> | <span style="color:#2196F3">**도입 후**</span> | <span style="color:#9C27B0">**효과**</span> |
  | --- | --- | --- | --- |
  | **Connection 수** | 10 | 2 | 5배 효율 증가 |
  | **Throughput** | 65 | 50 | 시스템 안정성 및 장기적 확장성 향상 |
</details>

<details>
  <summary>🛠️ 기술 선택지</summary>
  
  | **기술** | **장점** |
  | --- | --- |
  | **RDS-Proxy** | - Connection 및 처리량 관리<br>- RDS 부하 감소 |
  | **Redis 캐싱** | - 읽기 작업 성능 극대화<br>- RDS 부하 추가 감소 |
  
  **결론**:  
  RDS-Proxy 도입을 선택하여 연결 관리 및 응답 처리 속도를 크게 개선하고, 처리량 조절을 통해 안정성을 확보하였습니다.
</details>

---

## 🛠️ **인프라 아키텍처 & 적용 기술**

### **데이터베이스 및 캐싱**

<details>
  <summary>📚 데이터베이스 및 캐싱 상세</summary>
  
  1. **Redis**  
     - **적용 위치**: 캐시 서버  
     - **사용 이유**: 실시간 상품 조회 성능 향상.
  
  2. **RDS-Proxy**  
     - **적용 위치**: Lambda와 RDS 연결 사이  
     - **사용 이유**: SQS에 메시지를 Lambda 함수에서 처리 시 RDS의 기능 최적화를 위해 필요  
     - **구체적 역할**: 대규모 트래픽 처리 시 연결 개수와 CPU, 처리량 효율 및 안정성 향상
</details>

### **동시성**

<details>
  <summary>⚡ 동시성 상세</summary>
  
  1. **Redisson**  
     - **적용 위치**: 캐시 서버 및 분산락  
     - **사용 이유**:  
       - **분산락(Redisson RLock)**: 입찰 시 데이터 정합성 보장, 공유 자원 보호  
       - **TTL 설정**: 타임세일 종료 시 데이터 자동 삭제
  
  2. **AWS Lambda**  
     - **적용 위치**: AWS Lambda 트리거  
     - **사용 이유**: SQS 대용량 대기열 메시지 동시성 처리  
     - **구체적 역할**: 배치 크기를 통해 SQS에 저장된 대규모 트래픽을 백단위 이상으로 동시성을 처리 가능
</details>

### **메시징 시스템**

<details>
  <summary>📬 메시징 시스템 상세</summary>
  
  1. **AWS SQS**  
     - **적용 위치**: 서비스 간 비동기 통신  
     - **사용 이유**: 대규모 메시지 처리를 위한 안정적 메시징 큐 구현.  
     - **구체적 역할**: 주문 생성 시 티켓 대기열 생성, FIFO(선입선출)을 통한 메시지 순서 보장
  
  2. **SSE (Server-Sent Events)**  
     - **적용 위치**: 경매, 티켓 구매  
     - **사용 이유**: 사용자가 실시간으로 입찰 상황이나 티켓 구매 상황을 알기 위한 기능  
     - **구체적 역할**: 경매나 사용자의 아이디로 해당 알림창을 불러와 실시간 알림을 띄움
</details>

### **자동화**

<details>
  <summary>🤖 자동화 상세</summary>
  
  1. **AWS EventBridge Scheduler**  
     - **적용 위치**: AWS  
     - **사용 이유**: 일정 시간에 경매 시작 자동화.  
     - **구체적 역할**: 경매의 시작 시간에 맞춰 자동으로 경매를 시작시키는 Lambda 함수 호출
</details>

### **인프라 및 배포**

<details>
  <summary>🚀 인프라 및 배포 상세</summary>
  
  1. **GitHub Actions**  
     - **적용 위치**: CI/CD 파이프라인  
     - **사용 이유**: 코드 푸쉬 시 자동으로 빌드, 테스트, 배포 과정을 실행하여 개발 프로세스의 효율성과 일관성 향상
  
  2. **Docker**  
     - **적용 위치**: 애플리케이션 컨테이너화  
     - **사용 이유**: 애플리케이션을 컨테이너화하여 환경 일관성을 유지하고, 배포 속도를 개선함으로써 다양한 환경에서의 원활한 실행 보장.
  
  3. **AWS Elastic Container Registry (ECR)**  
     - **적용 위치**: 도커 이미지 저장소  
     - **사용 이유**: AWS와의 긴밀한 통합을 통해 안전하고 확장 가능한 도커 이미지 관리를 가능하게 하여, 배포 프로세스를 간소화함.
  
  4. **AWS ECS Fargate**  
     - **적용 위치**: 컨테이너 오케스트레이션 및 배포  
     - **사용 이유**: 서버리스 방식으로 컨테이너를 관리하여 인프라 관리 부담을 줄이고, 확장성과 유연성을 제공함.
  
  5. **AWS Application Load Balancer (ALB)**  
     - **적용 위치**: 트래픽 분산  
     - **사용 이유**: 외부 트래픽을 내부 서비스로 안정적으로 라우팅하고, SSL 종료 및 로드 밸런싱을 통해 보안과 성능을 강화함.
  
  6. **AWS ElastiCache**  
     - **적용 위치**: 인메모리 캐시  
     - **사용 이유**: 데이터베이스 부하를 줄이고 응답 속도를 향상시키기 위해 자주 조회되는 데이터를 캐시에 저장함으로써 애플리케이션 성능을 최적화함.
  
  7. **AWS RDS (Relational Database Service)**  
     - **적용 위치**: 관계형 데이터베이스  
     - **사용 이유**: 관리형 데이터베이스 서비스를 통해 데이터의 안정적인 저장과 고가용성을 보장하며, 다양한 데이터베이스 엔진을 지원하여 유연성을 제공함.
</details>

---








## 🛠️ **시스템 아키텍처**
<div align="center">
  <img src="https://user-images.githubusercontent.com/12345678/architecture.png" alt="아키텍처 다이어그램" width="70%">
</div>

### **설명**
- **ECS 인스턴스**에서 ????????????????
- **RDS(MySQL)**와 **ElastiCache(Redis)**를 통한 데이터 저장 및 캐싱.
- **SQS**를 이용한 비동기 메시지 처리.
- **GitHub Actions**를 통한 자동화된 **CI/CD 파이프라인 구축**.
- ??????
- ???????
- ?????

---

## ⚙️ **기술적 고도화**
<details>
<summary><b>🍁 Redisson을 활용한 동시성 문제 해결</b></summary>

- Redisson 기반의 **분산 락 시스템**을 사용하여 동시 입찰 시 데이터 충돌 방지.
- **CPU 점유율 25% 감소**, 데이터 일관성 보장.
</details>

<details>
<summary><b>🍁 SQS와 Lambda를 통한 비동기 처리</b></summary>

- SQS와 Lambda를 조합하여 경매 종료 시 자동 낙찰 처리.
- 처리 속도 **2배 향상**.
</details>

---

## 👥 **팀원 역할**
| 이름   | 역할                           | GitHub 링크      |
|--------|-------------------------------|-----------------|
| 홍기평 | ????????              | [GitHub](https://github.com/) |
| 배진관 | ????????       | [GitHub](https://github.com/) |
| 황우석 |????????           | [GitHub](https://github.com/) |
| 김진비 | ????????          | [GitHub](https://github.com/) |
| 노현지 | ????????         | [GitHub](https://github.com/) |

### **Ground Rule**
- **1 Day, 1 Issue, 1 PR** 원칙 준수.
- 코드 리뷰 **최소 2회** 이상 필수.
- 매일 **10분 스크럼 미팅**을 통한 일정 공유 및 문제 해결.

---

## 📈 **성과 및 회고**
### **성과**
- API 처리량 **500req/sec** 달성.
- 실시간 경매 응답 속도 **3배 개선**.

### **회고**
- **잘된 점**: 
- **아쉬운 점**: 

---

## 🔮 **미래 계획**
> **Java 21 가상 스레드** 및 **AI 기반 추천 시스템** 도입으로 사용자 맞춤형 서비스 제공 목표.

**"OhapJijole"와 함께하는 새로운 경매 경험을 기대해주세요! 감사합니다.** 😊
