FROM elasticsearch:2.4.6

RUN mkdir -p /home/elasticsearch && \
    chown elasticsearch /home/elasticsearch

USER elasticsearch

ADD elasticsearch.yml /usr/share/elasticsearch/config/elasticsearch.yml
ADD elasticsearch.yml /etc/elasticsearch/elasticsearch.yml

ADD logging.yml /usr/share/elasticsearch/config/logging.yml
ADD logging.yml /etc/elasticsearch/logging.yml

CMD elasticsearch -Des.network.host=0.0.0.0
