#!/bin/sh

TARGET=target/TrabajoGradoGKR-1.0.one-jar.jar
NEXP=30
MINPAR=0.1074830736
MAXPAR=0.9114391249
HMCR=0.7529512714
HMS=25
PO=0.7994034524
DIST=euclidiana
SEED=10



DATASET=Datasets/iris.data
echo "iris"
java -jar $CTARGET$TARGET -d $DATASET -e $NEXP -m $MINPAR -M $MAXPAR -c $HMCR -s $HMS -o $PO -D $DIST -S $SEED 
sleep 5
