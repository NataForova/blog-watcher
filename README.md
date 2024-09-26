# Blog - watcher

Blog - watcher allows to see all changes in blog's articles

# Api documentation:

https://github.com/NataForova/blog/blob/dev/api.yaml

it will be also available after the application is launched

http://localhost:8081/swagger-ui/index.html

# How to run in Docker

1. Run

````
 mvn clean install
````

2. Build docker image

````
 docker build -t your-blog-wathcer .
````

3. Run the image

````
docker run -d -p 8080:8080 --env-file .env  your-blog-watcher
````




