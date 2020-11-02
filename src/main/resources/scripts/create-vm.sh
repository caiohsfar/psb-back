#!/bin/bash

MACHINENAME=$1
RAM=$2
DISK=$3
OSTYPE=$4
VMSPATH=$5

cd $VMSPATH

#Create VM
VBoxManage createvm --name $MACHINENAME --ostype $OSTYPE --register --basefolder `pwd`
#Set memory and network
VBoxManage modifyvm  $MACHINENAME --ioapic on
VBoxManage modifyvm  $MACHINENAME --memory $RAM --vram 128
VBoxManage modifyvm  $MACHINENAME --nic1 nat
#Create Disk and connect Debian Iso
VBoxManage createhd --filename `pwd`/$MACHINENAME/$MACHINENAME_DISK.vdi --size $DISK --format VDI
VBoxManage storagectl $MACHINENAME --name "SATA Controller" --add sata --controller IntelAhci
VBoxManage storageattach $MACHINENAME --storagectl "SATA Controller" --port 0 --device 0 --type hdd --medium  `pwd`/$MACHINENAME/$MACHINENAME_DISK.vdi
VBoxManage storagectl  $MACHINENAME --name "IDE Controller" --add ide --controller PIIX4
VBoxManage storageattach $MACHINENAME --storagectl "IDE Controller" --port 1 --device 0 --type dvddrive --medium `pwd`/$OSTYPE.iso
VBoxManage modifyvm $MACHINENAME --boot1 dvd --boot2 disk --boot3 none --boot4 none

#Enable RDP
VBoxManage modifyvm $MACHINENAME --vrde on
VBoxManage modifyvm $MACHINENAME --vrdemulticon on --vrdeport 10001

#Start the VM
#VBoxManage startvm $MACHINENAME