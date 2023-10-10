FROM node:20-alpine as build
ARG NODE_OPTIONS="--max-old-space-size=8192"
WORKDIR /app
COPY . .
RUN npm install
RUN npm run build --prod

FROM nginx:1.23.4-alpine
COPY --from=build /app/dist/italian-restaurant /usr/share/nginx/html
COPY /nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 443
CMD ["nginx", "-g", "daemon off;"]
