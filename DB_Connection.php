<?php
class DB_Connection{
private $connectionToDB;

	public function getConnection(){
		require_once('Config.php');
		
			$this->connectionToDB = new mysqli(HOST,USER,PASSWORD,DB);
			return $this->connectionToDB;
	}
}
?>