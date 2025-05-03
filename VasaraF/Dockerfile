FROM node:18-alpine AS builder
WORKDIR /app
COPY . .
RUN npm install -g @quasar/cli
RUN npm install && quasar build

FROM nginx:stable-alpine

COPY --from=builder /app/dist/spa /usr/share/nginx/html

COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]

