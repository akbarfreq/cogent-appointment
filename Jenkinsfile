node {
   stage('SCM Checkout'){
    // Clone repo
  git 'https://github.com/akbarfreq/cogent-appointment'
   
   }
   stage('Mvn Package'){
     // Build using maven
     def mvn = tool (name: 'maven3', type: 'maven') + '/bin/mvn'
     sh "${mvn}/bin/mvn package"
   }
}
