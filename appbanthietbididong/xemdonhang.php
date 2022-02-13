<?php
include "connect.php";
$iduser = $_POST['iduser'];

$query = 'SELECT * FROM `donhang` WHERE `iduser` = '.$iduser.' ORDER BY id DESC';
$data = mysqli_query($conn, $query);
$result = array();
while ($row = mysqli_fetch_assoc($data)) {
	$truyvan = 'SELECT * FROM `chitietdonhang` INNER JOIN `sanphammoi` ON `chitietdonhang`.`idsp` = `sanphammoi`.`id` 
	WHERE`chitietdonhang`.`iddonhang` = '.$row['id'];
	$data1 = mysqli_query($conn, $truyvan);
	$item = array();

	while ($row1 = mysqli_fetch_assoc($data1)) {
		$item[] = $row1;
	}

	$row['item'] = $item;
	$result[] = $row;
}

if (!empty($result)) {
	$arr = [
		'success' => true,
		'message' => "Thành công",
		'result' => $result
	];
}
else {
	$arr = [
		'success' => false,
		'message' => "Không thành công",
		'result' => $result
	];
}

print_r(json_encode($arr));
?>