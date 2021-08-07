# genesis
Population structure and PCA viewer

August 2021 Updates (bug fixes -- system specific)
*  http://www.bioinf.wits.ac.za/software/genesis/downloads

Program takes as input output of PCA or admixture programs and allows user to interactively produce publication quality pictures. 

* *the manual can be found in the _doc_ directory*
* You need only download the Genesis.jar file which requires Java 1.7 or 1.8.

Run the program like this on an Apple

  java -XstartOnFirstThread -jar Genesis.jar

And like this on Windows/Linux

  java -jar Genesis.jar

You can download documentation and examples. While you can do all of this individually, you can also get a copy of everything by cloning

git clone https://github.com/shaze/genesis


#------ Making your own version

Download the following platform-independent jars to the `src` directory

org.eclipse.core.commands-3.10.0.jar
org.eclipse.core.runtime-3.7.0.jar
batik-all-1.8pre-r1084380.jar         org.eclipse.equinox.common-3.15.0.jar
itextpdf-5.5.9.jar                    org.eclipse.jface-3.6.0.jar
jfreechart-1.5.3.jar                  org.w3c.dom.svg-1.1.0.jar
jfreesvg-3.4.1.jar

Reasonably similar versions of the above should be fine.

Move to the `src` directory and do

```
for j in org.eclipse.core.commands-3.10.0.jar org.eclipse.core.runtime-3.7.0.jar batik-all-1.8pre-r1084380.jar         org.eclipse.equinox.common-3.15.0.jar itextpdf-5.5.9.jar org.eclipse.jface-3.6.0.jar jfreechart-1.5.3.jar org.w3c.dom.svg-1.1.0.jar jfreesvg-3.4.1.jar; do
    jar -xf $x; 
  done
```

Download the swt.jar specific for your platform to the `src` directory

run
    make

I've tested this with Java 8 and Java 11













