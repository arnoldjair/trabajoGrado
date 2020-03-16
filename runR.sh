#!/bin/sh

TARGET=target/TrabajoGradoGKR-1.0.jar

echo "glass"
java -jar $CTARGET$TARGET -r Ar/input_glass_norm.json
java -jar $CTARGET$TARGET -r Ar/input_glass_original.json 

echo "iris"
java -jar $CTARGET$TARGET -r Ar/input_iris_norm.json
java -jar $CTARGET$TARGET -r Ar/input_iris_original.json 

echo "sonar"
java -jar $CTARGET$TARGET -r Ar/input_sonar_norm.json
java -jar $CTARGET$TARGET -r Ar/input_sonar_original.json 

echo "wdbc"
java -jar $CTARGET$TARGET -r Ar/input_wdbc_norm.json
java -jar $CTARGET$TARGET -r Ar/input_wdbc_original.json 

echo "wine"
java -jar $CTARGET$TARGET -r Ar/input_wine_norm.json
java -jar $CTARGET$TARGET -r Ar/input_wine_original.json 