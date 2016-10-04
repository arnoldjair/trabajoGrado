#!/bin/sh

TARGET=target/TrabajoGradoGKR-1.0.jar
NEXP=30
MINPAR=0.3
MAXPAR=0.5
HMCR=0.9
HMS=25
PO=0.7
DIST=manhattan
SEED=10

DATASET=Datasets/glass.data
echo "glass"
java -jar $CTARGET$TARGET -d $DATASET -e $NEXP -m $MINPAR -M $MAXPAR -c $HMCR -s $HMS -o $PO -D $DIST -S $SEED 
sleep 5

DATASET=Datasets/iris.data
echo "iris"
java -jar $CTARGET$TARGET -d $DATASET -e $NEXP -m $MINPAR -M $MAXPAR -c $HMCR -s $HMS -o $PO -D $DIST -S $SEED 
sleep 5

DATASET=Datasets/sonar.data
echo "sonar"
java -jar $CTARGET$TARGET -d $DATASET -e $NEXP -m $MINPAR -M $MAXPAR -c $HMCR -s $HMS -o $PO -D $DIST -S $SEED 
sleep 5

DATASET=Datasets/wdbc.data
echo "wdbc"
java -jar $CTARGET$TARGET -d $DATASET -e $NEXP -m $MINPAR -M $MAXPAR -c $HMCR -s $HMS -o $PO -D $DIST -S $SEED 

sleep 5
DATASET=Datasets/wine.data
echo "wine"
java -jar $CTARGET$TARGET -d $DATASET -e $NEXP -m $MINPAR -M $MAXPAR -c $HMCR -s $HMS -o $PO -D $DIST -S $SEED 


