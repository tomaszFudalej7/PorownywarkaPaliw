<?php
date_default_timezone_set('Europe/Warsaw');
define('logDestination','/home/a7083098//public_html/php_error_logger.log');
class DB_Connection{
private $connectionToDB;

	public function getConnection(){
		require_once('Config.php');
		
			$this->connectionToDB = new mysqli(HOST,USER,PASSWORD,DB);
			if($this->connectionToDB->connect_error){
                error_log("\n " .date("F j, Y, g:i a") . "  DB_Connection->getConnection() connection error : " . $this->connectionToDB->connect_error ,3,logDestination);
            }
			return $this->connectionToDB;
	}
}
?>