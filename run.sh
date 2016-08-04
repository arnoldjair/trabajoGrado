#!/bin/sh

TARGET=target/TrabajoGradoGKR-1.0.one-jar.jar
NEXP=30
MINPAR=0.4072946726
MAXPAR=0.4328143221
HMCR=0.7191667829
HMS=33
PO=0.8940636037
DIST=euclidiana
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


TARGET=target/TrabajoGradoGKR-1.0.one-jar.jar
NEXP=30
MINPAR=0.1692978116
MAXPAR=0.909665086
HMCR=0.5636369113
HMS=17
PO=0.3638958987
DIST=euclidiana
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

TARGET=target/TrabajoGradoGKR-1.0.one-jar.jar
NEXP=30
MINPAR=0.9472072115
MAXPAR=0.991607933
HMCR=0.6349389976
HMS=37
PO=0.9672340246
DIST=euclidiana
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

TARGET=target/TrabajoGradoGKR-1.0.one-jar.jar
NEXP=30
MINPAR=0.9918509518
MAXPAR=0.9952376933
HMCR=0.8571976301
HMS=50
PO=0.8449514319
DIST=euclidiana
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

TARGET=target/TrabajoGradoGKR-1.0.one-jar.jar
NEXP=30
MINPAR=0.844916609
MAXPAR=0.9018995071
HMCR=0.3841885059
HMS=27
PO=0.8981053069
DIST=euclidiana
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

TARGET=target/TrabajoGradoGKR-1.0.one-jar.jar
NEXP=30
MINPAR=0.7178804164
MAXPAR=0.8707429698
HMCR=0.0058995968
HMS=33
PO=0.9885303161
DIST=euclidiana
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