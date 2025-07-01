docker build --build-arg MYSQL_HOST=111.229.130.92:3306 \
             --build-arg MYSQL_USERNAME=root \
             --build-arg MYSQL_PASSWORD=abcd1234 \
             --build-arg REDIS_HOST=111.229.130.92 \
             --build-arg REDIS_PORT=6543 \
             --build-arg REDIS_PASSWORD=abcd1234 \
             -t tinyurl-server .