
FROM node:20-alpine as build

# set working directory
WORKDIR /app

# copy project files and folders to the container
COPY . .

RUN npm install

RUN npm run build --prod


FROM nginx:1.23.4-alpine

COPY --from=build /app/dist/italian-restaurant /usr/share/nginx/html
COPY /nginx.conf  /etc/nginx/conf.d/default.conf
# expose port 80
EXPOSE 80

# start nginx server
CMD ["nginx", "-g", "daemon off;"]
