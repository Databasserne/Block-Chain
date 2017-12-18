HOST='localhost 6003'

(
echo open "$HOST"
sleep 2
echo "peer mine 1 \"Hallo\""
sleep 5
echo "peer mine 2 \"Hallo\" -r"
sleep 10
) | telnet
