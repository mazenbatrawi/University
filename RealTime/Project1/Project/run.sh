#!/bin/sh

gcc parent.c -o parent
gcc child.c -o child
gcc animation.c -o animation -lglut -lGLU -lGL

if [ $# -gt 0 ]; then
    ./parent $1
else
    ./parent 5
fi
