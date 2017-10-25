FROM ubuntu:16.04

RUN apt-get -y clean all && \
    sed -i 's%us.archive.ubuntu.com%mirrors.gigenet.com/ubuntuarchive/%' /etc/apt/sources.list && \
    apt-get -y update && \
    apt-get -y upgrade && \
    apt-get -y install software-properties-common python-software-properties

RUN add-apt-repository -y ppa:tista/build && \
    apt-get -y update && \
    apt-get -y install default-jdk wget zip gosu

RUN wget -O /usr/bin/lein https://raw.githubusercontent.com/technomancy/leiningen/stable/bin/lein

RUN chmod +x /usr/bin/lein
