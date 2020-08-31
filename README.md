# How to compile
This will compile to native and ship a zip file ready for upload
```
 mvn clean package -Pnative
```
# Requirements
* GraalVM = 20.1.0.r11/20.1.0.r8
* GraalVM native-image = ```gu install native-image```
* Maven >= 3.5
