<?php
include "connect.php";

use PHPMailer\PHPMailer\PHPMailer;
use PHPMailer\PHPMailer\Exception;

require 'PHPMailer/src/Exception.php';
require 'PHPMailer/src/PHPMailer.php';
require 'PHPMailer/src/SMTP.php';

$email = $_POST['email'];
$query = 'SELECT * FROM `user` WHERE `email` = "'.$email.'"';
$data = mysqli_query($conn, $query);
$result = array();
while ($row = mysqli_fetch_assoc($data)) {
  $result[] = ($row);
}

if (empty($result)) {
  $arr = [
    'success' => false,
    'message' => "Email không tồn tại!",
    'result' => $result
  ];
}
else {
    // send email
    $email=$result[0]['email'];
    $username=$result[0]['username'];
    $pass=$result[0]['password'];

    $link="<a href='http://192.168.1.9/appbanthietbididong/reset_pass.php?key=".$email."&reset=".$pass."'>Click To Reset password</a>";
    $mail = new PHPMailer();
    $mail->CharSet =  "utf-8";
    $mail->IsSMTP();
    // enable SMTP authentication
    $mail->SMTPAuth = true;                  
    // GMAIL username
    $mail->Username = "n18dccn154@student.ptithcm.edu.vn";
    // GMAIL password
    $mail->Password = "ksltwgyfhupunhrx";
    $mail->SMTPSecure = "ssl";  
    // sets GMAIL as the SMTP server
    $mail->Host = "smtp.gmail.com";
    // set the SMTP port for the GMAIL server
    $mail->Port = "465";
    $mail->From='n18dccn154@student.ptithcm.edu.vn';
    $mail->FromName='App bán thiết bị di động';
    $mail->AddAddress($email, $username);
    $mail->Subject  =  'Reset Password';
    $mail->IsHTML(true);
    $mail->Body    = $link;
    if($mail->Send())
    {
      $arr = [
        'success' => true,
        'message' => "Mail đã được gửi cho bạn! Vui lòng kiểm tra email của bạn!",
        'result' => $result
      ];
    }
    else
    {
      $arr = [
        'success' => false,
        'message' => "Gửi mail thất bại",
        'result' => $result
      ];
    }
}

print_r(json_encode($arr));
?>