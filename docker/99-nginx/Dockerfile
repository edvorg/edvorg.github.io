FROM nginx:1.10

RUN apt-get clean && \
    apt-get -y update && \
    apt-get -y install locales

RUN locale-gen en_US.UTF-8

ENV LANG en_US.UTF-8
ENV LANGUAGE en_US:en
ENV LC_ALL en_US.UTF-8

ADD nginx.conf /etc/nginx/conf.d/default.conf
