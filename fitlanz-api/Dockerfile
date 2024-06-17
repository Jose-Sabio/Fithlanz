# Usa una imagen base oficial de Node.js
FROM node:16

# Establece el directorio de trabajo en el contenedor
WORKDIR /usr/src/app

# Copia el package.json y el package-lock.json al contenedor
COPY package*.json ./

# Instala las dependencias
RUN npm install

# Si estás construyendo para producción, utiliza:
# RUN npm ci --only=production

# Copia el resto del código de la aplicación al contenedor
COPY . .

# Expone el puerto en el que la aplicación estará corriendo
EXPOSE 3000

# Comando para ejecutar la aplicación
CMD ["node", "index.js"]