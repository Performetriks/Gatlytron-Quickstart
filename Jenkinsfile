pipeline{
	//=========================================
	// Agent
	//=========================================
	agent {
		label 'loadtestAgent'
	}
	
	//=========================================
	// Parameters
	//=========================================
	parameters {
		//---------------------------------
		// Workload
		choice(
			  name: 'Workload'
			, choices: ['10', '25', '50', '100', '150', '200']
			, description:'''
	
			<!DOCTYPE html>
			<html>
				<head>
					<h1>Following workload can be simulated, please choose one:</h1>
				</head>
				<body>
					<h2>10%  AverageLoad 100 User,  1000 transactions/hour</h2>
					<h2>25%  AverageLoad 250 User,  2500 transactions/hour</h2>
					<h2>50%  AverageLoad 333 User,  5000 transactions/hour</h2>
					<h2>100% AverageLoad 1000 User, 10000 transactions/hour</h2>
					<h2>150% AverageLoad 1500 User, 15000 transactions/hour</h2>
					<h2>200% AverageLoad 2000 User, 20000 transactions/hour</h2>
				</body>
			</html>

		''')

		//---------------------------------
		// Environment
		choice(
			  name: 'Environment'
			, choices: ['DEV', 'TEST']
			, description:''' '''
		)
		
		//---------------------------------
		// Test Duration Minutes
		string(
			  name: 'testDurationInMinutes'
			, defaultValue: '1'
			, description: 'Test duration in minutes'
		)
				
		//---------------------------------
		// Log Level
		choice(
			  name: 'loglevel'
			, choices: ['INFO', 'ERROR','WARN', 'INFO', 'DEBUG', 'TRACE', 'ALL']
			, description:'Log level for the test execution'
		)

	}

	//=========================================
	// Stages
	//=========================================
	stages{
	
		//---------------------------------
		// 
		stage("Build Maven"){
			steps{
				bat "mvn -B clean package -DskipTests"
			}
		}
		
		//---------------------------------
		// 
		stage("Run Gatling"){
			steps{
	
				echo "Environment ${params.Environment}"
				
				echo "Workload ${params.Workload}"
	
				echo "testDurationInMinutes ${params.testDurationInMinutes}"
	
				echo "loglevel ${params.loglevel}"
	
				bat "mvn clean verify -Dgatling.simulationClass=com.performetriks.gatlytron.test.simulation.SimulationJenkins -Dgatling.conf.file=gatling.conf -DWorkload=${params.Workload} -DEnvironment=${params.Environment} -DtestDurationInMinutes=${params.testDurationInMinutes} -Dloglevel=${params.loglevel}"
			}
		}
		
		//---------------------------------
		// 
		stage("Report") {
			steps{
				gatlingArchive()
			}
		}
	}
}
