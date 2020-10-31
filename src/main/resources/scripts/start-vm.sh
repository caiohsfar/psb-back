NAME=$1
DIR=$2
cd "$DIR"

VBoxManage startvm "$NAME" 2>> start.txt