cloudgate
=========

Cloudgate is the webUI client for Sector distributed file system. For more information about Sector, please go to http://sector.sourceforge.net.

Prerequisites
-------------
* Perl 5.8
* PostgreSQL 8.3
* Ant 1.7
* Maven 2.2
* JDK 1.6
* Eclipse

Steps to setup Cloudgate
------------------------
1. Update Sector JNI's build.properties
  - sector.home : point to the root directory where sector is installed.
  - jdk.home : root dir of your jdk
  - other items can be kept as is
	
2. Add sector JNI jar into maven's local repository:
  - jar is generated at jni's dist directory after ant build
  - run this command to add jar:
    mvn install:install-file -DgroupId=com.opendatagroup -DartifactId=sector.jni -Dpackaging=jar -Dversion=0.3 -Dfile=sectorjni-0.3.jar

3. Download sigar API from http://sourceforge.net/projects/sigar/files/ and copy sigar library to /usr/lib/ directory.

4. In sectorjni_env.sh at cloudgate/trunk/src/main/resources, change the paths based on your sector installation. Do a "source sectorjni_env.sh" to set these environment variables
to the console. 

5. Install m2eclipse plugin from eclipse if not yet, import cloudgate as existing maven project

6. Config a maven build in run configuration dialog. The maven goals are : clean package jetty:run

7. Run the maven build above, then point your browser to  http://myhost:8080/sample, you should see the cloudgate GUI.