# 🚀 GUIDE DE DÉMARRAGE DES SERVICES

## Prérequis
- Java 17+ installé
- Maven 3.8+ installé
- Port 8761, 8080, 9091, 8888 disponibles

---

## Option 1: Compiler et Lancer (Depuis zéro)

### Étape 1: Compiler le projet
```bash
cd mini-projet-agent-ia
mvn clean package -DskipTests -q
```

### Étape 2: Démarrer les services un par un

#### Terminal 1 - Discovery Service (Eureka)
```bash
cd mini-projet-agent-ia
java -jar discovery-service/target/discovery-service-1.0.0.jar
```
✅ Attend que vous voyiez: `Tomcat started on port(s): 8761`

#### Terminal 2 - Auth Service
```bash
cd mini-projet-agent-ia
java -jar auth-service/target/auth-service-1.0.0.jar
```

#### Terminal 3 - Product Service
```bash
cd mini-projet-agent-ia
java -jar product-service/target/product-service-0.0.1-SNAPSHOT.jar
```

#### Terminal 4 - Gateway Service
```bash
cd mini-projet-agent-ia
java -jar gateway-service/target/gateway-service-1.0.0.jar
```

---

## Option 2: Lancer en arrière-plan (PowerShell - Windows)

```powershell
# Arrêter tous les services Java existants
Get-Process java -ErrorAction SilentlyContinue | Stop-Process -Force

# Attendre un peu
Start-Sleep -Seconds 3

# Démarrer Discovery Service
cd "C:\Users\arijb\Downloads\mini-projet-agent-ia\mini-projet-agent-ia"
java -jar discovery-service/target/discovery-service-1.0.0.jar | Out-Null &

# Attendre que Eureka démarre
Start-Sleep -Seconds 5

# Démarrer les autres services
java -jar auth-service/target/auth-service-1.0.0.jar | Out-Null &
java -jar product-service/target/product-service-0.0.1-SNAPSHOT.jar | Out-Null &
java -jar gateway-service/target/gateway-service-1.0.0.jar | Out-Null &

# Attendre la stabilisation
Start-Sleep -Seconds 10

# Vérifier l'état
& .\test-services.ps1
```

---

## Option 3: Utiliser Maven spring-boot:run

```bash
# Dans le répertoire du service
mvn -pl discovery-service spring-boot:run

# Ou depuis la racine pour tous les services
mvn -pl discovery-service spring-boot:run
mvn -pl auth-service spring-boot:run
mvn -pl product-service spring-boot:run
mvn -pl gateway-service spring-boot:run
```

---

## 🧪 Vérifier que tout fonctionne

### PowerShell
```powershell
# Exécuter le script de test
cd "C:\Users\arijb\Downloads\mini-projet-agent-ia\mini-projet-agent-ia"
& .\test-services.ps1
```

### Bash/Terminal Unix
```bash
cd mini-projet-agent-ia
./test-services.sh
```

### Curl manuel
```bash
# Vérifier Eureka
curl -s http://localhost:8761/eureka/apps | head -50

# Vérifier les services enregistrés
curl -s http://localhost:8761/eureka/apps/AUTH-SERVICE
curl -s http://localhost:8761/eureka/apps/PRODUCT-SERVICE
curl -s http://localhost:8761/eureka/apps/GATEWAY-SERVICE
```

---

## 🛑 Arrêter les services

### PowerShell
```powershell
# Arrêter tous les processus Java
Get-Process java -ErrorAction SilentlyContinue | Stop-Process -Force
```

### Bash/Terminal
```bash
# Arrêter tous les processus Java
pkill -f java
```

### Avec Ctrl+C
- Appuyez sur `Ctrl+C` dans chaque terminal où s'exécute un service

---

## 🐛 Dépannage

### Les services ne se lancent pas
1. Vérifier que les ports ne sont pas occupés:
   ```powershell
   netstat -ano | findstr "8761\|8080\|9091\|8888"
   ```

2. Vérifier que Java est installé:
   ```bash
   java -version
   ```

### Discovery Service démarre mais les autres services ne s'enregistrent pas
1. Vérifier la config d'Eureka dans chaque service
2. S'assurer que `eureka.client.serviceUrl.defaultZone` pointe sur `http://localhost:8761/eureka`

### Port déjà utilisé
```powershell
# Trouver quel processus utilise le port 8080 par exemple
Get-Process | Where-Object {$_.ProcessName -like "*java*"}

# Ou avec netstat
netstat -ano | findstr ":8080"
```

---

## 📊 Monitorer les services

### Vérifier les logs en temps réel
```bash
# Afficher les dernières lignes de logs pour chaque service
tail -f ~/logs/discovery-service.log
tail -f ~/logs/auth-service.log
tail -f ~/logs/product-service.log
tail -f ~/logs/gateway-service.log
```

### Utiliser jps (Java Process Status)
```bash
jps -l  # Affiche les processus Java avec les classes main
```

---

## 🔗 URLs Importantes

| Service | URL | Purpose |
|---------|-----|---------|
| Eureka | http://localhost:8761/eureka | Voir tous les services enregistrés |
| Auth Service | http://localhost:8080 | Service d'authentification |
| Product Service | http://localhost:9091 | Service de gestion des produits |
| Gateway | http://localhost:8888 | Point d'entrée API Gateway |

---

## 💡 Tips

1. **Lancer Discovery Service en premier** - Les autres services ont besoin de s'y enregistrer
2. **Attendre 5-10 secondes** entre chaque démarrage pour éviter les conditions de course
3. **Utiliser des terminaux séparés** - Plus facile de monitorer les logs
4. **Redémarrer proprement** - Arrêter tous les services avant de relancer

---

## 📝 Variables d'environnement (optionnel)

```bash
# Pour changer le port Eureka
export SERVER_PORT=8761

# Pour forcer une zone d'availability
export EUREKA_CLIENT_AVAILABILITY_ZONES_ZONE=default

# Pour logs plus verbeux
export LOGGING_LEVEL_ROOT=DEBUG
```

---

**Version:** 1.0  
**Date:** 6 janvier 2026  
**Statut:** ✅ Opérationnel
