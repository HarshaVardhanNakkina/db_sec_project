rm dbsec/*.class
rm gVrfy/*.class
javac -d . dbsec/*.java
javac -d . gVrfy/*.java
javac gHMAC.java
java gHMAC
