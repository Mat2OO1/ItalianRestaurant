
FROM node:14-alpine as build

# set working directory
WORKDIR /app

# copy project files and folders to the container
COPY . .

RUN npm install

RUN npm run build


FROM nginx:alpine

COPY --from=build /app/dist/italian-restaurant /usr/share/nginx/html

# expose port 80
EXPOSE 80

# start nginx server
CMD ["nginx", "-g", "daemon off;"]
