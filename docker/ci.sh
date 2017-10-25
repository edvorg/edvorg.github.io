#! /bin/bash

DIR=`dirname ${0}`

cd ${DIR}

if [ ! -f ./rev ] ; then
    touch ./rev
fi

eval "$(ssh-agent -s)"

while true ; do
    echo checking git revision
    git pull
    OLD_REV=`cat ./rev`
    REV=`git rev-parse HEAD`
    echo old revision ${OLD_REV}
    echo new revision ${REV}
    if [ "${OLD_REV}" != "${REV}" ] ; then
        echo changes pending, rebuild
        echo testing
        if ./lein test ; then
            echo building
            ./lein do clean, uberjar
            echo deploying
            ./stop-env.sh
            ./start-env.sh --jar `pwd`/../target/edvorg.jar
        else
            echo TODO send test fail email
        fi
        echo ${REV} > ./rev
    fi
    sleep 30s
done
