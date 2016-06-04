How to use:
===========
First of all, many credits to this StackOverflow post: http://stackoverflow.com/a/5963294

Prerequisites:
--------------
- Native Utils by Adam Heinrich* (see https://github.com/adamheinrich/native-utils)  
  - Include it in your project - either by including the source code, 
    or using maven:
```xml
<groupId>cz.adamh.utils</groupId>
<artifactId>native-utils</artifactId>
<version>1.0-SNAPSHOT</version>
```
- `libcomedi` (for controlling elevator hardware, debian package: `libcomedi-dev`)
- `gcc` (and optionally `make`)
- Parts of the C elevator interface driver provided for the course (can be found at https://github.com/TTK4145/Project)  
  - From the 'simulator_2/client' directory, get the following files:
     - `con_load.h`
     - `elev.h`
     - `elev.c`  
  - From the 'driver' directory, get everything except for `Makefile`, `main.c`, and the `elev` files (we got the version with simulator support instead, remember?).

And of course a recent JDK (I built it using OpenJDK 7, any newer versions or Oracle JDK should also work), plus Maven if you decide to use that.


_(\*) Technically you don't **need** to use the Native Utils, but then you would need to install the library into the system - I prefer everything to be included in one JAR so you can just deploy that one file to the target and start it up._

Setting up/compiling the the project:
=====================================
If you use Maven, here's what you should do to get started:

**Step 1:** Compile the driver
- From the `driver` directory, run `make` (or manually run all the commands in the Makefile, in the right order).

**Step 2:** Build the Maven project and verify that it works and that the driver is correctly bundled (and unpacked).
- In the project root (the directory with `pom.xml` in it), run `mvn package`.
- Run the resulting JAR file: `java -jar target/comedielevator-1.0-SNAPSHOT-jar-with-dependencies.jar`


How to do it yourself (how I did it):
=====================================
If you don't want to use the (relatively thin) wrapper class I made, here's roughly how to go about making your own.

**Step 1:** Declare `native` functions in the Java class that you want to implement in C  
    Example: `native int elev_get_floor_sensor_signal();`

**Step 2:** Generate the required C header files to enable integration between the different runtimes.
- While at the root of the Java source tree in your project (typically `src/main/java` or similar), run the following command:  
        `javah [-stubs] full.package.and.ClassName` (`-stubs` is optional, if that wasn't clear - it may yield a bit more verbose header file).

**Step 3:** Implement the C functions defined in the header file(s) you generated (do not edit the header file).

**Step 4:** Compile the C driver to a shared object (.so file)
- First compile all the required source files, remember to include the `-fPIC` flag (Position Independent Code)
  - For the C file containing the JNI implementation, you also need to specify the path of the Java includes (more specifically `jni.h`, in my case [Debian] it was `-I"/usr/lib/jvm/default-java/include/"`)
- Then combine all the object files into a shared object (see the included Makefile in the 'driver' directory):  
  `gcc --shared -Wl,-soname,<desired library name>.so -Wl,-export-dynamic -lcomedi -lm -o <desired library name>.so <list all the object files>`
- For more information, see http://tldp.org/HOWTO/Program-Library-HOWTO/shared-libraries.html

**Step 5:** Include the shared object file you created in step 4
- Either bundle it in the JAR (I recommend you use maven or similar for this, see the `<build>` section of `pom.xml`)  
  - Then you use `NativeUtils.loadLibraryFromJar("/<desired library name>.so");`  
    Note that the "absolute" path here is relative to the resource directory specified in the JAR manifest. E.g. if the `pom.xml` looks something like this: `...<directory>driver</directory>...` and the .so file is called "libelevator.so", the loading call should be like this: `NativeUtils.loadLibraryFromJar("/libelevator.so");`
- OR install the library onto the system where it will be run. This is something I didn't choose to do, and therefore the almighty Internet (with a little bit of Google-fu) can probably give a better explanation than me.

**Step 6..(N-1):** ???

**Step N:** Profit!
