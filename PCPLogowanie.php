<?php
if($_SERVER['REQUEST_METHOD'] == 'POST'){
	
	require_once('DB_Connection_Helper.php');
	$db_Connection_Helper = new DB_Connection_Helper();
	
	$jsonResponse = array("error" => false);
	
	if(isset($_POST['email']) && isset($_POST['password'])){
		
		$email = $_POST['email'];
		$password = $_POST['password'];	
		
		$userData = $db_Connection_Helper->getUserDataByEmailAndPassword($email,$password);
	
		if($userData == false){
			$jsonResponse["error"] = true;
			$jsonResponse["errorMessage"] = " Wrong email or password please try again";
			echo json_encode($jsonResponse);
		}else if($userData == null){
				$jsonResponse["error"] = true;
				$jsonResponse["errorMessage"] = "userData = null";
				echo json_encode($jsonResponse);
				}
		else{
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
	}
	else{
		$jsonResponse["error"] = true;
		$jsonResponse["errorMessage"] = "isset error";
		echo json_encode($jsonResponse);
	}
}
else{
	$jsonResponse["error"] = true;
	$jsonResponse["errorMessage"] = "method is not POST";
	echo json_encode($jsonResponse);
}


?>