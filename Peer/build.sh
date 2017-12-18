mvn clean package docker:build

set +x
docker login -u databasserne -p ultrapassword
set -x

docker push databasserne/block-chain
docker logout
