FROM node:19

Expose 3000

WORKDIR /backend

COPY package.json package-lock.json ./

RUN npm install

COPY . .

CMD ["node", "index"]