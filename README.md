
# 🍽️ Canteen Reservation System — Levi9 Challenge 2025

Backend aplikacija razvijena za takmičenje **„5 dana u oblacima 2025 – Challenge faza“**.  
Sistem omogućava studentima rezervaciju termina u studentskim menzama uz praćenje kapaciteta i dostupnosti.

---

## 📌 Korišćene tehnologije

| Tehnologija | Verzija |
|------------|---------|
| Java | 21 |
| Spring Boot | 4.0.0 |
| Spring Web MVC | 4.0.0 |
| Spring Data JPA | 4.0.0 |
| H2 In-memory Database | 2.4.240 |
| Maven | 3.x |
| Lombok | 1.18.42 |

---

## ⚙️ Podešavanje okruženja (Environment Setup)

Pre pokretanja potrebno je imati instalirano:

- ☑️ **Java JDK 21**
- ☑️ **Maven 3.x**

Da proveriš instalacije:

```
java -version
mvn -version
```

---

## 🛠️ Kako pokrenuti build

U root direktorijumu projekta pokrenuti:

```
mvn clean install
```

Rezultat ove komande biće:

- obrisani postojeći build artifakti
- kompajliranje aplikacije
- pokretanje validacije dependencija

---

## ▶️ Pokretanje aplikacije

Aplikaciju je moguće pokrenuti na dva načina:

### 🔹 1) Maven Run

```
mvn spring-boot:run
```

### 🔹 2) Izgradnja izvršnog `.jar` fajla

```
mvn clean package
java -jar target/canteen-reservation-system-0.0.1-SNAPSHOT.jar
```

Aplikacija će raditi na:

```
http://localhost:8080
```

---

## 🗄️ Baza Podataka

Aplikacija koristi **H2 in-memory bazu**, što znači da se podaci brišu pri svakom restartu — ovo je zahtev zadatka.

## 📬 API specifikacija

API prati zahteve definisane u zadatku i podržava:

- 👨‍🎓 upravljanje studentima (`/students`)
- 🏛️ upravljanje menzama (`/canteens`)
- 🍽️ rezervacije (`/reservations`)
- 📊 pregled kapaciteta (`/canteens/status`)

Detaljni endpoint-i nalaze se u Postman kolekciji iz zadatka.

⚠️ Pre svakog testiranja restartovati aplikaciju (zbog prazne baze).

---

## 👨‍💻 Autor

**Aleksandar Djokić**
***adjokic24@gmail.com***
---

