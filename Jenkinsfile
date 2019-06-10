#!/usr/bin/groovy

//@Library('jenkins-hm@bug/credentials-sonar') _


mavenNode(['node_qualifier':'SpringBoot']) {
 email='Blaghji.ALAEDDINE@prestataire.sihm.fr'
  mavenBoot{
    gcl_folder="/DEV_EAND/cycledev/tags"
    deployment=true
    deployment_target='sba37int1'
    service_name='int1_acdc-demat-batch_21008.service'
  }
}
