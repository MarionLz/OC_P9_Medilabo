# Étape 1 : image Node officielle
FROM node:20

# Dossier de travail dans le container
WORKDIR /app

# Copie des fichiers de dépendances
COPY package*.json ./

# Installation des dépendances
RUN npm install

# Copie du reste du code
COPY . .

# Port exposé par Vite (dev)
EXPOSE 5173

# Démarre le serveur Vite
CMD ["npm", "run", "dev", "--", "--host"]
