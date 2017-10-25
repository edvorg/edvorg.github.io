FROM redis:3.2

RUN apt-get clean && \
    apt-get -y update && \
    apt-get -y install locales

RUN locale-gen en_US.UTF-8

ENV LANG en_US.UTF-8
ENV LANGUAGE en_US:en
ENV LC_ALL en_US.UTF-8

CMD [ "redis-server" ]
