#!/bin/bash

#This script is used to build the etherwake command itself.

#PATH to NDK folder:
NDK=<enter path to ndk folder>

FILE=ether-wake.c
TARGET_DIR=../app/src/main/res/raw

$NDK/build/tools/make-standalone-toolchain.sh --platform=android-19 --install-dir=/tmp/ndk-arm --arch=arm
$NDK/build/tools/make-standalone-toolchain.sh --platform=android-19 --install-dir=/tmp/ndk-x86 --arch=x86
$NDK/build/tools/make-standalone-toolchain.sh --platform=android-19 --install-dir=/tmp/ndk-mips --arch=mips

CC_ARM=/tmp/ndk-arm/bin/arm-linux-androideabi-gcc
CC_X86=/tmp/ndk-x86/bin/i686-linux-android-gcc
CC_MIPS=/tmp/ndk-mips/bin/mipsel-linux-android-gcc

#mkdir $TARGET_DIR/arm
#mkdir $TARGET_DIR/x86
#mkdir $TARGET_DIR/mips

$CC_ARM $FILE -o $TARGET_DIR/etherwake_arm
$CC_X86 $FILE -o $TARGET_DIR/etherwake_i686
$CC_MIPS $FILE -o $TARGET_DIR/etherwake_mips
