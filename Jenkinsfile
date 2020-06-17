node {
   stage('SCM Checkout'){
    // Clone repo
  git branch: 'master', 
  credentialsId: 'github', 
  url: 'https://github.com/akbarfreq/cogent-appointment'
   
   }
   
   stage('Mvn Package'){
     // Build using maven
     
     sh "mvn clean package"
   }
}
