FROM node:10.16.0-alpine AS build

WORKDIR /app

# prevents call to lscpu and stop freezing build
ENV PARCEL_WORKERS=4

# Cache npm dependencies as docker layer
COPY ./package.json /app/
COPY ./package-lock.json /app/
RUN npm ci

COPY ./ /app
RUN npm run build
#--------------------------------------------------------
FROM nginx:stable

COPY --from=build /app/dist /var/www
COPY ./nginx.conf /etc/nginx/nginx.conf
