# You must this for things to work.
JAVADIR ?= /usr

# Setup java related variables.
JAVAC   ?= $(JAVADIR)/bin/javac
JAVA    ?= $(JAVADIR)/bin/java
JAR     ?= $(JAVADIR)/bin/jar

# Check existence.
ifeq ($(wildcard $(JAVAC)),)
    $(error "ERROR: file not found: $(JAVAC).")
endif

ifeq ($(wildcard $(JAVA)),)
    $(error "ERROR: file not found: $(JAVA).")
endif

ifeq ($(wildcard $(JAR)),)
    $(error "ERROR: file not found: $(JAR).")
endif

# Check the version of java.
JAVAVER := $(shell $(JAVA) -version 2>&1 | head -1 | awk -F'"' '{print $$2}')
JAVACHK := $(shell echo $(JAVAVER) | egrep '^1.[89]|^1.[1-9][0-9]' >/dev/null && echo ok || echo bad)
ifeq ($(JAVACHK),bad)
    $(error "ERROR: java version is $(JAVAVER) must be version 8 or later.")
endif

# ================================================================
# Rules
# ================================================================
.PHONY: all clean help test testp version
all: bin/lsjci bin/lsjci.jar

clean:
	rm -rf bin out tmp *~
	@find . -name '*~' -delete

help: all
	bin/lsjci -h

test: all
	bin/lsjci java.lang.String

testp: all
	bin/lsjci java.lang.String --public

version: all
	bin/lsjci java.lang.String --version

bin/lsjci : src/lsjci.sh
	@[ ! -d $(dir $@) ] && mkdir -p $(dir $@) || true
	@rm -f $@
	@cp -v $< $@
	@chmod 0755 $@

out/production/ListClassInfo/ListClassInfo.class: src/ListClassInfo.java
	@[ ! -d $(dir $@) ] && mkdir -p $(dir $@) || true
	@rm -f $@
	$(JAVAC) -d $(dir $@) $<

out/artifacts/ListClassInfo_jar/ListClassInfo.jar: out/production/ListClassInfo/ListClassInfo.class
	@[ ! -d $(dir $@) ] && mkdir -p $(dir $@) || true
	@rm -f $@
	@[ -d tmp ] && rm -f tmp || true
	rsync -avz out/production/ListClassInfo/ META-INF tmp/
	cd tmp && $(JAR) cvfm ../$@ META-INF/MANIFEST.MF *
	@rm -rf tmp

bin/lsjci.jar: out/artifacts/ListClassInfo_jar/ListClassInfo.jar
	@[ ! -d $(dir $@) ] && mkdir -p $(dir $@) || true
	@rm -f $@
	@cp -v $< $@
	@chmod 0644 $@
