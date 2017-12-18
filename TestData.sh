HOST='localhost 6001'

(
echo open "$HOST"
sleep 2
echo "peer add This is a node 1"
echo "peer add This is a node 2"
echo "peer add This is a node 3"
echo "peer add This is a node 4"
echo "peer add This is a node 5"
echo "peer add This is a node 6"
echo "peer add This is a node 7"
echo "peer add This is a node 8"
echo "peer add This is a node 9"
echo "peer add This is a node 10"
echo "peer add This is a node 11"
echo "peer add This is a node 12"
echo "peer add This is a node 13"
sleep 5
) | telnet
