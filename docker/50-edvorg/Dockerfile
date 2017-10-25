FROM ubuntu:16.04

RUN apt-get -y clean all && \
    sed -i 's%us.archive.ubuntu.com%mirrors.gigenet.com/ubuntuarchive/%' /etc/apt/sources.list && \
    apt-get -y update && \
    apt-get -y install default-jdk telnet locales

RUN locale-gen en_US.UTF-8

ENV LANG en_US.UTF-8
ENV LANGUAGE en_US:en
ENV LC_ALL en_US.UTF-8

RUN useradd -m -g users edvorg

USER edvorg

ADD edvorg.jar /home/edvorg/edvorg.jar

ADD .lein-env /home/edvorg/.lein-env

EXPOSE 12309

CMD cd /home/edvorg && java -jar edvorg.jar
