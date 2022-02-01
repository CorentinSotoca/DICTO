#!/bin/bash
export SOURCES=src
export CLASSES=classes

javac -cp lib/ap.jar:. -sourcepath ${SOURCES} -d ${CLASSES} $@ `find src -name "*.java"`
