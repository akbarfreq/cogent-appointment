node {
   stage('SCM Checkout'){
    // Clone repo
  git 'https://github.com/akbarfreq/cogent-appointment'
   
   }
   stage ('build & push') {
            steps {
                container ('maven') {
                    sh 'mvn -o -Dmaven.test.skip=true -gs `pwd`/configuration/settings.xml clean package'
                   
                    }
                }
            }
