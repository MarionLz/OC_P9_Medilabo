# 🏥 Medilabo

Medilabo est une application web composée de microservices permettant la gestion de patients et de leurs dossiers médicaux. Ce projet est construit avec une architecture orientée microservices, conteneurisée avec Docker.

## 📚 Sommaire

- [Technologies utilisées](#-technologies-utilisées)
- [Description des microservices](#-description-des-microservices)
- [Authentification via Basic Auth](#-authentification-via-basic-auth)
- [Lancement de l'application](#-lancement-de-lapplication-avec-docker-compose)
- [Endpoints des Microservices](#-endpoints-des-microservices)
- [Suggestions d’améliorations (Greencode)](#-suggestions-daméliorations--greencode)

## 🛠 Technologies utilisées

| Catégorie          | Outils / Langages                      |
|--------------------|----------------------------------------|
| 🧠 Backend          | Java 21, Spring Boot, Spring Cloud     |
| 🎨 Frontend         | React, Vite, Axios                     |
| 🗄️ Base de données  | MySQL, MongoDB                         |
| 📋 Logging          | Log4j2                                 |
| 🐳 Orchestration    | Docker, Docker Compose                 |
| 🔐 Authentification | Basic Auth (via Gateway)               |
| ⚙️ Build Tools      | Maven                                  |


## 🧩 Description des microservices

Chaque microservice est responsable d’un domaine métier spécifique, et communique avec les autres via la passerelle (Gateway Service).

| Microservice     | Description                                                                                                                          | Technologies principales                      |
|------------------|--------------------------------------------------------------------------------------------------------------------------------------|----------------------------------------------|
| Gateway Service  | Gère l'authentification, le routage et la sécurité via Basic Auth.                                                                   | Spring Boot, Spring Cloud Gateway      |
| Patient Service  | Gère l'ajout d'un patient et la mise à jour de son dossier avec stockage des données en base MySQL.                                  | Spring Boot, MySQL, JPA, Log4j2               |
| Note Service     | Gère l'ajout de notes de suivi des patients par le praticien (ex : symptômes, antécédents) avec stockage des données en base MongoDB | Spring Boot, MongoDB, Log4j2                  |
| Report Service   | Génère un rapport de risque de diabète pour un patient à partir de son âge, son sexe et les notes du praticien.                      | Spring Boot, Appel REST, Log4j2                |
| Frontend        | Interface utilisateur pour interagir avec les microservices.                                                                         | React, Vite, Axios, React Router               |

## 🔐 Authentification via Basic Auth

L'authentification des requêtes est centralisée dans la **Gateway**, qui agit comme point d'entrée vers l'ensemble des microservices. Cette passerelle est construite avec **Spring Cloud Gateway** et utilise le module de sécurité **Spring Security (WebFlux)** pour filtrer et sécuriser les accès.

### 🛡️ Rôle de la Gateway

La Gateway a plusieurs rôles essentiels :
- Filtrer toutes les requêtes entrantes vers les microservices,
- Appliquer des règles de sécurité (authentification, autorisations),
- Rediriger les requêtes vers le bon microservice selon l’URL.

### 🔐 Authentification Basic

Le système utilise **HTTP Basic Auth**, un mécanisme d’authentification simple où le nom d'utilisateur et le mot de passe sont envoyés dans l’en-tête HTTP de chaque requête. Dans ce projet, un utilisateur unique est défini statiquement au sein de la configuration de sécurité de la Gateway.

### 🌐 Accès via le frontend

Le frontend (développé en React) communique avec les API via cette Gateway. Pour permettre ce dialogue, une configuration **CORS** (Cross-Origin Resource Sharing) a été mise en place. Elle autorise spécifiquement le frontend, hébergé sur `http://localhost:5173`, à interagir avec la Gateway même si celle-ci est sur une autre origine (port différent).


## 🚀 Lancement de l'application avec Docker Compose

### ✅ Pré-requis

- [Docker](https://docs.docker.com/get-docker/) installé
- [Docker Compose](https://docs.docker.com/compose/) installé

### 🛠️ Étapes

1. **Cloner le projet :**

```bash
git clone git@github.com:MarionLz/OC_P9_Medilabo.git
cd OC_P9_Medilabo
```

2. **Lancer tous les services (build + démarrage) :**

```bash
docker-compose up --build
```

3. **Accéder à l'application :**

| Service           | URL                       |
|-------------------|----------------------------|
| 🖥️ Frontend        | [http://localhost:3000](http://localhost:3000) |
| 🌐 Gateway API     | [http://localhost:8080](http://localhost:8080) |
| 🏥 Patient Service | [http://localhost:8081](http://localhost:8081) |
| 📝 Note Service    | [http://localhost:8082](http://localhost:8082) |
| 📄 Report Service  | [http://localhost:8083](http://localhost:8083) |


ℹ️ Pour arrêter les conteneurs, utilisez la commande :

```bash
docker-compose down
```

### 💾 Bases de données

Les bases de données MySQL (pour les patients) et MongoDB (pour les notes) sont automatiquement initialisées avec des scripts au moment du démarrage via Docker. Aucun script manuel n'est requis.

## 📡 Endpoints des Microservices

### 🔹 Patient Service (`http://localhost:8081`)
| Méthode | Endpoint                    | Description                         |
|---------|-----------------------------|-------------------------------------|
| GET     | /patients                   | Liste tous les patients             |
| GET     | /patients/{id}              | Détail d’un patient                 |
| POST    | /patients                   | Créer un nouveau patient            |
| PUT     | /patients/{id}              | Modifier un patient existant        |
| GET  | /patients/{id}/demographics | Obtenir l'âge et le sexe du patient |

### 🔹 Note Service (`http://localhost:8082`)
| Méthode | Endpoint               | Description                   |
|---------|------------------------|-------------------------------|
| GET     | /notes/patient/{id}    | Liste des notes d’un patient  |
| POST    | /notes/patient/{id}    | Ajouter une note              |

### 🔹 Report Service (`http://localhost:8083`)
| Méthode | Endpoint                      | Description                            |
|---------|-------------------------------|----------------------------------------|
| GET     | /diabetes-report/patient/{id} | Rapport de risque pour un patient      |



## 🌱 Suggestions d’améliorations – GreenCode