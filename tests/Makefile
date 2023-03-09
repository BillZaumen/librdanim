CPATH = classes:../BUILD/librdanim.jar:/usr/share/java/libbzdev.jar

JAVA = java -classpath $(CPATH)
JAVAC = javac -d classes -classpath $(CPATH)
AVCONV = $(shell  which avconv > /dev/null && echo avconv || echo ffmpeg)

all: cartest cartest2 cartest3 cartest4 biketest pedtest ftest prtest


classes: lib
	mkdir -p classes

lib:
	(cd ..; make)

ftest: classes
	$(JAVAC) FactoryTest.java
	$(JAVA) FactoryTest

blindspot: classes
	$(JAVAC) BlindSpotTest.java
	$(JAVA) BlindSpotTest

doortest: classes
	$(JAVAC) DoorTest.java
	$(JAVA) DoorTest

dvideo-js:
	scrunner --codebase ../BUILD/librdanim.jar dvideo.js

dvideo-esp:
	scrunner --codebase ../BUILD/librdanim.jar dvideo.esp


dftest: classes
	$(JAVAC) DoorFactoryTest.java
	$(JAVA) DoorFactoryTest

cartest: classes
	$(JAVAC) CarTest.java
	$(JAVA) CarTest
	$(AVCONV) -r 25 -f image2 -i ctmp/col-%03d.png -y -r 25 car.webm

cartest2: classes
	$(JAVAC) CarTest2.java
	$(JAVA) CarTest2
	$(AVCONV) -r 25 -f image2 -i rtmp/col-%03d.png -y -r 25 car2.webm

cartest3: classes
	$(JAVAC) CarTest3.java
	$(JAVA) CarTest3
	$(AVCONV) -r 25 -f image2 -i ttmp/col-%03d.png -y -r 25 car3.webm

cartest4: classes
	$(JAVAC) CarTest4.java
	$(JAVA) CarTest4
	$(AVCONV) -r 25 -f image2 -i stmp/col-%03d.png -y -r 25 car4.webm

cartest5: classes
	$(JAVAC) CarTest5.java
	$(JAVA) CarTest5
	$(AVCONV) -r 25 -f image2 -i stmp/col-%03d.png -y -r 25 car5.webm

cartest6: classes
	$(JAVAC) CarTest6.java
	$(JAVA) CarTest6
	$(AVCONV) -r 25 -f image2 -i ttmp/col-%03d.png -y -r 25 car6.webm


biketest: classes
	$(JAVAC) BikeTest.java
	$(JAVA) BikeTest
	$(AVCONV) -r 25 -f image2 -i btmp/col-%03d.png -y -r 25 bike.webm

pedtest: classes
	$(JAVAC) PedTest.java
	$(JAVA) PedTest
	$(AVCONV) -r 25 -f image2 -i ptmp/col-%03d.png -y -r 25 ped.webm

#
# This will not work as expected if the librdanim packages are installed
# as scrunner will use those instead
#
sctest: classes
	mkdir -p tmp
	(cd tmp; rm -f *.png)
	scrunner --codebase ../BUILD/librdanim.jar -d:tmpdir:tmp test.js
	$(AVCONV) -r 25 -f image2 -i tmp/col-%03d.png -y -r 25 sctest.mp4

#
# This will not work as expected if the librdanim packages are installed
# as scrunner will use those instead
#
sctest-esp: classes
	mkdir -p tmp
	(cd tmp; rm -f *.png)
	scrunner --codebase ../BUILD/librdanim.jar -d:tmpdir:tmp -o - test.esp
	$(AVCONV) -r 25 -f image2 -i tmp/col-%03d.png -y -r 25 sctest.mp4


#
# This will not work as expected if the librdanim packages are installed
# as yrunner will use those instead
#
sctest-y: classes
	mkdir -p tmp
	(cd tmp; rm -f *.png)
	yrunner --stackTrace  --codebase ../BUILD \
		--add-modules org.bzdev.rdanim \
		 -d:tmpdir:tmp	anim2d,rdanim test.yaml
	$(AVCONV) -r 25 -f image2 -i tmp/col-%03d.png -y -r 25 sctest.mp4

sctest-ya: classes
	mkdir -p tmp
	(cd tmp; rm -f *.png)
	yrunner --stackTrace  --codebase ../BUILD \
		--add-modules org.bzdev.rdanim \
		 -d:tmpdir:tmp	anim2d,rdanim testa.yaml
	$(AVCONV) -r 25 -f image2 -i tmp/col-%03d.png -y -r 25 sctest.mp4

sctest-y-dryrun: classes
	mkdir -p tmp
	(cd tmp; rm -f *.png)
	yrunner --stackTrace  --codebase ../BUILD \
		 -d:tmpdir:tmp --dryrun anim2d,rdanim test.yaml

#
# This will not work as expected if the librdanim packages are installed
# as yrunner will use those instead
#
sctest-yy: classes
	mkdir -p tmp
	(cd tmp; rm -f *.png)
	yrunner --trustLevel=2  -d:tmpdir:tmp anim2d,rdanim test.yaml
	$(AVCONV) -r 25 -f image2 -i tmp/col-%03d.png -y -r 25 sctest.mp4


#
# This will not work as expected if the librdanim packages are installed
# as scrunner will use those instead
#
sctest1-js: classes
	mkdir -p tmp
	(cd tmp; rm -f *.png)
	scrunner --codebase ../BUILD/librdanim.jar -d:tmpdir:tmp test1.js

sctest1-esp: classes
	mkdir -p tmp
	(cd tmp; rm -f *.png)
	scrunner --codebase ../BUILD/librdanim.jar -d:tmpdir:tmp test1.esp

#
# This will not work as expected if the librdanim packages are installed
# as scrunner will use those instead
#
sctest2-js: classes
	mkdir -p tmp
	(cd tmp; rm -f *.png)
	scrunner --codebase ../BUILD/librdanim.jar -d:tmpdir:tmp test2.js
#
# This will not work as expected if the librdanim packages are installed
# as scrunner will use those instead
#
sctest2-esp: classes
	mkdir -p tmp
	(cd tmp; rm -f *.png)
	scrunner --codebase ../BUILD/librdanim.jar -d:tmpdir:tmp test2.esp

#
# This will not work as expected if the librdanim packages are installed
# as scrunner will use those instead
#
sctest3-js: classes
	mkdir -p tmp
	(cd tmp; rm -f *.png)
	scrunner --codebase ../BUILD/librdanim.jar -d:tmpdir:tmp test3.js

#
# This will not work as expected if the librdanim packages are installed
# as scrunner will use those instead
#
sctest3-esp: classes
	mkdir -p tmp
	(cd tmp; rm -f *.png)
	scrunner --codebase ../BUILD/librdanim.jar -d:tmpdir:tmp test3.esp

prtest: classes
	$(JAVAC) PrintTest.java
	$(JAVA) PrintTest

clean:
	rm -rf classes
	rm -rf btmp ctmp ftmp ptmp rtmp stmp tmp ttmp
	rm -f [bcps]*.webm sctest.mp4
