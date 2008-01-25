#!/bin/sh

JAVA=/opt/ibm/java2-ppc-50/bin/java
BUILD_ROOT=/shared/technology/epp/udc_build

BUILD_DATE=`date +%Y%m%d`
BUILD_TIME=`date +%H%M`
TIMESTAMP=${BUILD_DATE}${BUILD_TIME}

rm -r ${BUILD_ROOT}/workspace

${JAVA} -jar ${BUILD_ROOT}/eclipse/plugins/org.eclipse.equinox.launcher_1.0.100.v20071211.jar \
        -application org.eclipse.ant.core.antRunner \
        -buildfile ${BUILD_ROOT}/eclipse/plugins/org.eclipse.pde.build_3.4.0.v20071212/scripts/build.xml \
        -Dbuilder=${BUILD_ROOT}/org.eclipse.epp/releng/org.eclipse.epp.usagedata.releng/ \
        -DbuildDirectory=${BUILD_ROOT}/workspace/ \
        -Dbase=${BUILD_ROOT} \
        -DbuildId=${TIMESTAMP}