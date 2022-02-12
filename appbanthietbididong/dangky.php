<?php
include "connect.php";
$email = $_POST['email'];
$password = $_POST['password'];
$username = $_POST['username'];
$mobile = $_POST['mobile'];

$query = 'SELECT * FROM `user` WHERE `email` = "'.$email.'"';
$data = mysqli_query($conn, $query);
$numrow = mysqli_num_rows($data);
if ($numrow > 0) {
	$arr = [
		'success' => false,
		'message' => "Email đã tồn tại"
	];
}
else {
	$query = 'INSERT INTO `user`(`email`, `password`, `username`, `mobile`) VALUES ("'.$email.'", "'.$password.'", "'.$username.'", "'.$mobile.'")';
	$data = mysqli_query($conn, $query);
	if ($data == true) {
		$arr = [
			'success' => true,
			'message' => "thanh cong"
		];
	}
	else {
		$arr = [
			'success' => false,
			'message' => "khong thanh cong"
		];
	}
}

print_r(json_encode($arr));
?>