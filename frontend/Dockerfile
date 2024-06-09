FROM node:alpine

Expose 5173

WORKDIR /frontend

COPY package.json .

RUN npm install

COPY . .

CMD ["npm", "run", "dev", "--", "--host"]