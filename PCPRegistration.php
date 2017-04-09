<?php
	if($_SERVER['REQUEST_METHOD']=='POST'){
		
		require_once('DB_Connection_Helper.php');
		$db_Connection_Helper = new DB_Connection_Helper();
		
		$jsonRespon = array("error" => false);
		
		if(isset($_POST['name']) && isset($_POST['email']) && isset($_POST['permission']) && isset($_POST['password'])){
		
		$username = $_POST['name'];
		$username = $_POST['surname'];
		$username = $_POST['email'];
		$username = $_POST['town'];
		$phoneNumber = $_POST['phoneNumber'];
		$username = $_POST['permission'];
		$password = $_POST['password'];
		
		
			if(!($db_Connection_Helper->checkIfUserExistInDB($phoneNumber))){
		
			$userData = $db_Connection_Helper->addNewUser($name,$surname,$email,$town,$phoneNumber,$permission,$password);
		
				while($row = array_shift($userData)){	
				$jsonRespon["error"] = false;
				$jsonRespon["userData"]["name"] = $row["imie"];
				$jsonRespon["userData"]["surname"] = $row["nazwisko"];
				$jsonRespon["userData"]["email"] = $row["email"];
				$jsonRespon["userData"]["town"] = $row["miasto"];
				$jsonRespon["userData"]["phoneNumber"] = $row["telefon"];
				echo json_encode($jsonRespon);
				}
			}
			else{
			$jsonRespon["error"] = true;
			$jsonRespon["errorMessage"] = "User with this phone number " . $phoneNumber . " exist";
			echo json_encode($jsonRespon);
			}	
		}
	}	
?>