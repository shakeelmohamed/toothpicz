<?php
	//Here is the basic DB connection
	$dbconn = pg_connect("host=ec2-54-243-62-232.compute-1.amazonaws.com port=5432 dbname=d482hpig0og3o7 user=eprbfwldrxxxsq password=tcZtq8ZRT6KTs32vSD2ctHObso sslmode=require options='--client_encoding=UTF8'") or die('Could not connect: ' . pg_last_error());


	echo 'Welcome to ToothPicz <br />';
	if($dbconn)
		echo 'DB connection worked!';
	else
		echo 'DB connection failed!';
?>