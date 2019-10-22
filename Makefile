# GNU Make file.
#
#
# Set this if  'make install' should install its files into a
# user directory - useful for package systems that will grab
# all the files they see.  Setting this will allow a package
# to be built without requiring root permissions.
#
DESTDIR :=

JROOT := $(shell while [ ! -d src -a `pwd` != / ] ; do cd .. ; done ; pwd)

include VersionVars.mk

ANIM2D_DIR = /usr/share/doc/libbzdev-doc/api/org/bzdev/anim2d
ANIM2D_DIR_SED = $(shell echo $(ANIM2D_DIR) | sed  s/\\//\\\\\\\\\\//g)
ALT_ANIM2D_DIR = ../../../bzdev/doc/api/org/bzdev/anim2d
ALT_ANIM2D_DIR_SED = $(shell echo $(ALT_ANIM2D_DIR) | sed  s/\\//\\\\\\\\\\//g)

TMPSRC = $(JROOT)/tmpsrc/
#
# System directories (that contains JAR files, etc.)
#
SYS_LIBJARDIR = /usr/share/bzdev
SYS_API_DOCDIR = /usr/share/doc/librdanim-doc
SYS_JAVADOCS = $(SYS_API_DOCDIR)/api
SYS_EXAMPLES = $(SYS_API_DOCDIR)/examples

EXTDIR = $(SYS_LIBJARDIR)
EXTLIBS = $(EXTDIR)

ALL = jarfile javadocs

all: $(ALL)

# Target for the standard Java extension directory
LIBJARDIR = $(DESTDIR)$(SYS_LIBJARDIR)

LIBJARDIR_SED=$(shell echo $(SYS_LIBJARDIR) | sed  s/\\//\\\\\\\\\\//g)

# Other target directories

API_DOCDIR = $(DESTDIR)$(SYS_API_DOCDIR)
JAVADOCS = $(DESTDIR)$(SYS_JAVADOCS)
EXAMPLES = $(DESTDIR)$(SYS_EXAMPLES)

JROOT_JARDIR = $(JROOT)/BUILD
JROOT_LIBJARDIR = $(JROOT_JARDIR)

JROOT_JAVADOCS = $(JROOT)/BUILD/api
JROOT_ALT_JAVADOCS = $(JROOT)/BUILD/alt-api
JROOT_EXAMPLES = $(JROOT)/examples

JDOCS = *.html stylesheet.css package-list
RDOCS = *.gif

BZDEV = org/bzdev

$(JROOT_JARDIR)/libbzdev.jar: $(EXTDIR)/libbzdev.jar
	mkdir -p $(JROOT_JARDIR)
	ln -s $(EXTDIR)/libbzdev.jar $(JROOT_JARDIR)/libbzdev.jar

JFILES = $(wildcard $(BZDEV)/roadanim/*.java)

RESOURCES = $(wildcard $(BZDEV)/roadanim/lpack/*.properties)


include MajorMinor.mk



$(TMPSRC):
	mkdir -p $(TMPSRC)


JARFILE = $(JROOT_LIBJARDIR)/librdanim.jar

jarfile: $(JARFILE)

NOF_SERVICE = org.bzdev.obnaming.NamedObjectFactory


RDANIM_DIR = ./src/org.bzdev.rdanim
RDANIM_MODINFO = $(RDANIM_DIR)/module-info.java
RDANIM_JFILES = $(wildcard $(RDANIM_DIR)/$(BZDEV)/roadanim/*.java)
RDANIM_RESOURCES1 = \
	$(wildcard $(RDANIM_DIR)/$(BZDEV)/roadanim/lpack/*.properties)
RDANIM_RESOURCES = $(subst ./src/,,$(RDANIM_RESOURCES1))

JDOC_MODULES = org.bzdev.rdanim
JDOC_EXCLUDE = org.bzdev.roadanim.lpack

FILES = $(RDANIM_JFILES) $(RESOURCES) $(RDANIM_MODINFO)

$(JARFILE): $(FILES) $(TMPSRC) $(JROOT_JARDIR)/libbzdev.jar \
	    META-INF/services/$(NOF_SERVICE)
	mkdir -p mods/org.bzdev.rdanim
	mkdir -p BUILD
	javac -d mods/org.bzdev.rdanim -p $(EXTLIBS) \
		--processor-module-path $(EXTLIBS) -s tmpsrc/org.bzdev.rdanim \
		$(RDANIM_MODINFO) $(RDANIM_JFILES) \
		$(RDANIM_DIR)/$(BZDEV)/roadanim/lpack/DefaultClass.java
	for i in $(RDANIM_RESOURCES) ; do mkdir -p mods/`dirname $$i` ; \
		cp src/$$i mods/$$i ; done
	mkdir -p mods/org.bzdev.rdanim/META-INF/services
	cp META-INF/services/* mods/org.bzdev.rdanim/META-INF/services
	jar --create --file $(JARFILE) --manifest=$(RDANIM_DIR)/manifest.mf \
		copyright -C mods/org.bzdev.rdanim .

clean:
	@[ -d mods ] && rm -rf mods || echo -n
	@[ -d tmpsrc ] && rm -rf tmpsrc || echo -n
	rm -r BUILD

jdclean:
	@ [ -d BUILD/api ] && rm -rf BUILD/api  || echo -n
	@ [ -d BUILD/alt-api ] && rm -rf BUILD/alt-api || echo -n

javadocs: $(JROOT_JAVADOCS)/index.html

altjavadocs: $(JROOT_ALT_JAVADOCS)/index.html

JAVA_VERSION=11
JAVADOC_LIBS = BUILD/librdanim.jar:$(EXTLIBS)

$(JROOT_JAVADOCS)/index.html: $(RDANIM_JFILES) overview.html $(JARFILE)
	mkdir -p $(JROOT_JAVADOCS)
	rm -rf $(JROOT_JAVADOCS)/*
	javadoc -d $(JROOT_JAVADOCS) --module-path $(JAVADOC_LIBS) \
		--module-source-path src:tmpsrc \
		--add-modules org.bzdev.rdanim \
		-link file:///usr/share/doc/openjdk-$(JAVA_VERSION)-doc/api \
		-link file:///usr/share/doc/libbzdev-doc/api/ \
		-overview overview.html \
		--module $(JDOC_MODULES) -exclude $(JDOC_EXCLUDE)
	lsnof -d $(JROOT_JAVADOCS) -p $(JARFILE) \
	      --link file:///usr/share/doc/openjdk-$(JAVA_VERSION)-doc/api/ \
	      --link file:///usr/share/doc/libbzdev-doc/api/ \
	      --overview src/FactoryOverview.html 'org.bzdev.roadanim.*'

$(JROOT_ALT_JAVADOCS)/index.html: $(RDANIM_JFILES) overview.html $(JARFILE) \
		$(JROOT_JAVADOCS)/index.html
	mkdir -p $(JROOT_ALT_JAVADOCS)
	rm -rf $(JROOT_ALT_JAVADOCS)/*
	javadoc -d $(JROOT_ALT_JAVADOCS) --module-path $(JAVADOC_LIBS) \
		--module-source-path src:tmpsrc \
		--add-modules org.bzdev.rdanim \
		-linkoffline ../../../bzdev/doc/api \
			file:///usr/share/doc/libbzdev-doc/api/ \
		-linkoffline \
	   https://docs.oracle.com/en/java/javase/$(JAVA_VERSION)/docs/api/ \
		    file:///usr/share/doc/openjdk-$(JAVA_VERSION)-doc/api \
		-overview overview.html \
		--module $(JDOC_MODULES) -exclude $(JDOC_EXCLUDE)
	lsnof -d $(JROOT_ALT_JAVADOCS) -p $(JARFILE) \
	      --link-offline \
	   https://docs.oracle.com/en/java/javase/$(JAVA_VERSION)/docs/api/ \
			file:///usr/share/doc/openjdk-$(JAVA_VERSION)-doc/api/ \
	      --link-offline ../../../bzdev/doc/api \
			file:///usr/share/doc/libbzdev-doc/api/ \
	      --overview src/FactoryOverview.html 'org.bzdev.roadanim.*'

install: install-lib install-links install-docs

install-lib: $(JARFILE)
	install -d $(LIBJARDIR)
	install -m 0644 $(JARFILE) $(LIBJARDIR)/librdanim-$(VERSION).jar

install-links:
	rm -f $(LIBJARDIR)/librdanim.jar
	ln -s $(LIBJARDIR)/librdanim-$(VERSION).jar \
		$(LIBJARDIR)/librdanim.jar

install-docs: javadocs
	install -d $(API_DOCDIR)
	install -d $(JAVADOCS)
	for i in `cd $(JROOT_JAVADOCS); find . -type d -print ` ; \
		do install -d $(JAVADOCS)/$$i ; done
	for i in `cd $(JROOT_JAVADOCS); find . -type f -print ` ; \
		do j=`dirname $$i`; install -m 0644 $(JROOT_JAVADOCS)/$$i \
			$(JAVADOCS)/$$j ; \
		done
	install -d $(EXAMPLES)
	install -m 0644 $(JROOT_EXAMPLES)/example.js $(EXAMPLES)/example.js
