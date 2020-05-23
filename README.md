# Instructions

## Add a data file to the *__data__* folder

> the data format should be as followed:
  
  1. First line should be the number of nodes __*n*__

  2. The remaining lines will have two space separated integers
  representing an edge between those two nodes.

  3. The file should be readable.

  <!-- - __**Or you can use an existing file__ -->
  
  ```plain
  e.g. sample.txt
  3
  1 2
  2 3
  3 2
   .
   .
   .
  ```

## To run the project

* Change the file path in *__gHMAC.java__* so that it points to the desired data file.
* Now, the following commands, from the root directory of the project.

```bash
javac -d . dbsec/*.java
javac -d . gVrfy/*.java
javac gHMAC.java
java gHMAC
```
