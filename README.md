
---

### **Accounts Microservice (README)**

![image](https://github.com/user-attachments/assets/cf3f6019-fcd8-4aa1-a9c5-c7e6e037deb2)



```markdown
# Accounts Microservice

## Description
Ce microservice gère la création et la gestion des comptes des utilisateurs, y compris le numéro de compte et le solde. Il notifie les utilisateurs via le microservice Notification.

## Fonctionnalités
- les operations CRUD sur les comptes utilisateur.
- Envoi d'email lors de la création d'un compte au client.

## Dépendances
- Spring Boot
- Kafka
- Eureka Client

## Configuration
- Configurez Kafka et le service Notification dans `application.yml`.

