#! /bin/bash

DIR=`dirname ${0}`
LOCAL="${DIR}/local"
LOCAL_EXAMPLE="${DIR}/local.example"

if [ ! -f "${LOCAL}" ] ; then
    cp ${LOCAL_EXAMPLE} ${LOCAL}
fi

if [ -f "${LOCAL}" ] ; then
    source "${LOCAL}"
fi

echo stopping edvorg environment

DIRS=`ls`

for entry in ${DIRS} ; do
    if [ -d ${entry} ] ; then
        echo "------------------------------------------------------------------------------------------------"
        echo entering ${entry}
        pushd ${entry}
        if [ ! -f ./stop ] ; then
            echo nothing to stop
        else
            ./stop
        fi
        popd
    fi
done
