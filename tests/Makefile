
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

dvideo:
	scrunner --codebase ../BUILD/librdanim.jar dvideo.js

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

biketest: classes
	$(JAVAC) BikeTest.java
	$(JAVA) BikeTest
	$(AVCONV) -r 25 -f image2 -i btmp/col-%03d.png -y -r 25 bike.webm

pedtest: classes
	$(JAVAC) PedTest.java
	$(JAVA) PedTest
	$(AVCONV) -r 25 -f image2 -i ptmp/col-%03d.png -y -r 25 ped.webm

sctest: classes
	mkdir -p tmp
	(cd tmp; rm -f *.png)
	scrunner --codebase ../BUILD/librdanim.jar -d:tmpdir:tmp test.js
	$(AVCONV) -r 25 -f image2 -i tmp/col-%03d.png -y -r 25 sctest.webm

sctest1: classes
	mkdir -p tmp
	(cd tmp; rm -f *.png)
	scrunner --codebase ../BUILD/librdanim.jar -d:tmpdir:tmp test1.js

sctest2: classes
	mkdir -p tmp
	(cd tmp; rm -f *.png)
	scrunner --codebase ../BUILD/librdanim.jar -d:tmpdir:tmp test2.js

sctest3: classes
	mkdir -p tmp
	(cd tmp; rm -f *.png)
	scrunner --codebase ../BUILD/librdanim.jar -d:tmpdir:tmp test3.js

prtest: classes
	$(JAVAC) PrintTest.java
	$(JAVA) PrintTest

clean:
	rm -rf classes
	rm -rf btmp ctmp ftmp ptmp rtmp stmp tmp ttmp
	rm [bcps]*.webm
