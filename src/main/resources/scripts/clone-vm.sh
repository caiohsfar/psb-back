DIR=$1
NAME=$2
NEW_NAME=$3
cd "$DIR"
VBoxManage export "$NAME" --output "$NEW_NAME".ova 2>> clone.txt
VBoxManage import "$NEW_NAME".ova 2>> clone.txt
rm "$NEW_NAME".ova

