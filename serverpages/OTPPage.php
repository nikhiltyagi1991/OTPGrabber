<?php 
if(isset($_POST['phone'])){
	//Code for inserting the phone number in your database.
	//The otp needs to be generated randomly but for this example so that 
	//I know what SMS needs to be sent, I have made it fixed.
	echo '{"status":"ok","result":"success","otp":"987654"}';
	//Make a call to the send SMS api you have.
}
else 
	echo '{"status":"ok","result":"failed","reason":"Incorrect Data Entered"}';
?>