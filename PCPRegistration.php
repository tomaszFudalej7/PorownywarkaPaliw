<?php
	if($_SERVER['REQUEST_METHOD']=='POST'){
		
		require_once('DB_Connection_Helper.php');
		$db_Connection_Helper = new DB_Connection_Helper();
		
		$jsonResponse = array("error" => false);
		
		if(isset($_POST['name']) && isset($_POST['email']) && isset($_POST['permission']) && isset($_POST['password'])){
		$name = $_POST['name'];
		$surname = $_POST['surname'];
		$email = $_POST['email'];
		$town = $_POST['town'];
		$phoneNumber = $_POST['phoneNumber'];
		$permission = $_POST['permission'];
		$password = $_POST['password'];
		
		
			if(!($db_Connection_Helper->checkIfUserExistInDB($email))){
		
			$userData = $db_Connection_Helper->addNewUser($name,$surname,$email,$town,$phoneNumber,$permission,$password);
		
				while($row = array_shift($userData)){	
				$jsonResponse["error"] = false;
				$jsonResponse["userData"]["name"] = $row["imie"];
				$jsonResponse["userData"]["surname"] = $row["nazwisko"];
				$jsonResponse["userData"]["email"] = $row["email"];
				$jsonResponse["userData"]["town"] = $row["miasto"];
				$jsonResponse["userData"]["phoneNumber"] = $row["telefon"];
				echo json_encode($jsonResponse);
				}
			}
			else{
			$jsonResponse["error"] = true;
			$jsonResponse["errorMessage"] = "User with this email " . $email . " exist";
			echo json_encode($jsonResponse);
			}	
		}
	}
	else{
		$jsonResponse["error"] = true;
		$jsonResponse["errorMessage"] = "isset error";
		echo json_encode($jsonResponse);
	}		
