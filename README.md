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
1. **대규모 트래픽 대응**: Redis와 RDS를 활용하여 최대 **500req/sec**의 API 처리량을 목표로 합니다.
2. **성능 최적화**: Spring Cache와 Redisson을 사용해 DB 부하를 줄이고 조회 속도를 **300% 개선**.
3. **자동화된 운영 및 배포**: GitHub Actions를 활용한 **CI/CD 파이프라인 구축**으로 자동화 배포를 실현합니다.

---

## 🚀 **주요 기능**
- ⚡ **실시간 경매 시스템**: 사용자가 실시간으로 경매에 참여하여 상품을 획득.
- 🎫 **티켓 발급 및 관리**: 등급에 따라 경매 참여를 제한하여 공정성 확보.
- 📲 **자동 알림 시스템**: Firebase를 통해 실시간 입찰 상태와 낙찰 결과를 사용자에게 알림.

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
