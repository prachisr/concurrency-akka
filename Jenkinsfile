pipeline {
    agent { docker { image 'hseeberger/scala-sbt' } }
    stages {
			stage('build') {
				steps {
				 sh 'sbt clean compile'
				 }
			}
		}
	}


