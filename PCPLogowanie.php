<?php
if($_SERVER['REQUEST_METHOD'] == 'POST'){
	
	require_once('DB_Connection_Helper.php');
	$db_Connection_Helper = new DB_Connection_Helper();
	
	$jsonResponse = array("error" => false);
	
	if(isset($_POST['phoneNumber']) && isset($_POST['password'])){
		
		$phoneNumber = $_POST['phoneNumber'];
		$password = $_POST['password'];	
		
		$userData = $db_Connection_Helper->getUserDataByEmailAndPassword($phoneNumber,$password);
		if($userData == 0){
			$jsonResponse["error"] = true;
			$jsonResponse["errorMessage"] = "Wrong phone number or password please try again";
			echo json_encode($jsonRespon);
		}
		else{
		while($row = array_shift($userData)){
			$jsonResponse["error"] = false;
			$jsonRespon["userData"]["username"] = $row["username"];
			$jsonRespon["userData"]["phoneNumber"] = $row["phoneNumber"];
			echo json_encode($jsonRespon);
		}
		}
	}
	else{
		
	}
}
else{
	
}


?>