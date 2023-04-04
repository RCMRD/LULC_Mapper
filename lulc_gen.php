<?php
$graphq = parse_ini_file('/etc/cgi_bin.ini');
$conn = new mysqli($graphq[USQ], $graphq[UPQ], $graphq[PPQ], "LULC_DB");

$error_passwd = "101";
$error_userer = "202";
$error_server = "303";
$error_regiko = "404";




if ($conn->connect_error) {
    die($error_server);
}else{


  if (!empty($_POST["regista_PostJSON"])){

              //resp:success
              $pita = array();
              $pita[] = array("resp" => "success");
              
              $respJSON = json_encode($pita);
              
              $obj = json_decode($_POST["regista_PostJSON"], true);
              
              $simu = $obj[0]["_usertel"];
              $pepe = $obj[0]["_useremail"];
              $pwdo = $obj[0]["_userpass"];
              $pwdo_h = password_hash($pwdo,PASSWORD_DEFAULT);
              
              
              if ($st = $conn->prepare("SELECT * FROM appuser WHERE usafon = ? AND usamail = ? AND usapwd = ?")){
                    $st->bind_param("ssb", $simu, $pepe, $pwdo_h); 
                    $st->execute();
                    $st->store_result();
                    
                    if ($st-> num_rows > 0) {
                    
                      echo $error_regiko;
                      
                    }else{
                      $st->close();
                      $st = $conn->prepare('INSERT INTO appuser (usaname,usafon,usactry,usamail,usapwd,usaorg,usarole,usalat,usalon) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)');
                      $st->bind_param('sssssssss', $nani, $simu, $wapi, $pepe, $siri, $ajir, $ofis, $lato, $lono);
                  
                      
                      foreach ($obj as $key => $value) {
                            
                            $nani = $value["_username"];
                            $simu = $value["_usertel"];
                            $wapi = $value["_usercntry"];
                            $pepe = $value["_useremail"];
                            $siri = $pwdo_h;
                            $ajir = $value["_userorg"];
                            $ofis = $value["_userrole"];
                            $lato = $value["_userlat"];
                            $lono = $value["_userlon"];
                            
                            $st->execute();
                        }
                        
                      
                      echo $respJSON;
                        
                  }      
              
              $st->close();
              $conn->close();
              }else{
                  echo $error_server;
              }

  }else 
  if(!empty($_POST["login_CheckJSON"])){
  //add get all data from fielddata to send to phone too
   $sw = 1;
   
   $obj = json_decode($_POST["login_CheckJSON"], true);
              
   $mail = $obj[0]["mail"];
   
   $pswd = $obj[0]["pswd"];
   
   
   if ($st = $conn->prepare("SELECT * FROM appuser WHERE usaapproved = ? AND usamail = ?")){
                    $st->bind_param("is", $sw, $mail); 
                    $st->execute();
                    $result = $st->get_result();
                    
                    
                    if($result->num_rows == 1) {
                        
                        $row = $result->fetch_assoc();
                        $pwd_s = $row['usapwd'];
                        
                        if(password_verify($pswd, $pwd_s)){
                        
                                $refid = $row['_stem']."_".$row['userid']."_".$row['_year'];
                                $udata = array();
                                $udata[] = array(
                                "_username" => $row['usaname'],
                                "_usertel" => $row['usafon'],
                                "_usercntry" => $row['usactry'],
                                "_useremail" => $row['usamail'],
                                "_userorg" => $row['usaorg'],
                                "_userrole" => $row['usarole'],
                                "_userlat" => $row['usalat'],
                                "_userlon" => $row['usalon'],
                                "_userid" => $refid
                                );
                                
                                /*$udata->_username = $row['usaname'];
                                $udata->_usertel = $row['usafon'];
                                $udata->_usercntry = $row['usactry'];
                                $udata->_useremail = $row['usamail'];
                                $udata->_userorg = $row['usaorg'];
                                $udata->_userrole = $row['usarole'];
                                $udata->_userlat = $row['usalat'];
                                $udata->_userlon = $row['usalon'];
                                $udata->_userid = $refid;*/
                                
                                $respJSON = json_encode($udata);
                                
                                echo $respJSON;
                                
                        
                        }else{
                        
                                echo $error_passwd;
                        
                        }
                          
                    }else{
                    
                       echo $error_userer;
                    
                    }
        
        $st->close();
        $conn->close();
              
              
    }else{
    
        echo $error_server;
        
    }
   
  }else
  if(!empty($_POST["maindata_PostJSON"])){
  
              
              $obj = json_decode($_POST["maindata_PostJSON"], true);
              $sendAr = array();
              
              foreach ($obj as $key => $value) {
              
                    $dref = $value["_datno"];
                    
                    
                    if ($key == "_datno"){
                      $sendAR[$key] = $value;
                    }
                    
              
                    if ($st = $conn->prepare("SELECT _data_ref FROM fielddata WHERE _data_ref = ?")){
                            
                            $st->bind_param('s', $dref);
                            $st->execute();
                            $result = $st->get_result();
                    
                    
                            if($result->num_rows > 0) {
                            
                            
                              $row = $result->fetch_assoc();
                              $dref_s = $row['_data_ref'];
                              
                              if($dref_s == $dref){
                              
                                  $st->close();
                                  $st = $conn->prepare('UPDATE fielddata SET ftname = ?, ftcom = ?, ftlat = ?, ftlon = ?,ftpicnm = ? WHERE _data_ref = ?');
                                  $st->bind_param('ssssss', $fnam, $fcom, $flat, $flon, $fpic, $dref);
                                  
                                  $fnam = $value["_datftrname"];
                                  $fcom = $value["_datcom"];
                                  $flat = $value["_datlat"];
                                  $flon = $value["_datlon"];
                                  $fpic = $value["_datpicnm"];
                                  
                                  $st->execute();
                                  
                                  }
                            
                            } else {
                            
                              $st->close();
                              $st = $conn->prepare('INSERT INTO fielddata (_userid, _data_ref, ftname, ftcom, ftlat, ftlon,ftpicnm) VALUES (?, ?, ?, ?, ?, ?, ?)');
                              $st->bind_param('sssssss', $user, $dref, $fnam, $fcom, $flat, $flon, $fpic);
                              
                              $dref = $value["_datno"];
                              $fnam = $value["_datftrname"];
                              $fcom = $value["_datcom"];
                              $flat = $value["_datlat"];
                              $flon = $value["_datlon"];
                              $fpic = $value["_datpicnm"];
                              $user = $value["_userid"];
                            
                              $st->execute();
                   }
                  }
                }
                
                $res = json_encode($sendAR);
                //$res = $dref;
                echo $res;
              
  
  
  }else
  if(!empty($_POST["maindata_PostImages"])){
  
            $obj = json_decode($_POST["maindata_PostImages"], true);
            
            $zfyl = $obj[0]["zipfile"];
   
            $znem = $obj[0]["zipname"];
            
            $picov = base64_decode($zfyl);
            
            $fp = fopen('./ges/'.$znem,'w');
            
            if($fp){
            
              fwrite($fp,$picov);
              fclose($fp);
            
              $upload_folder = 'ges/';
              $upload_files = 'ges/'.$znem;
            
              system("unzip -d $upload_folder -o $upload_files");
            
              unlink('./ges/'.$znem);

            }

  }
  
  
  
  
  
  
  
  
  
  
  
  
  /*
  if($result->num_rows == 1) {
                          while($row = $result->fetch_assoc()) {
                            $id[] = $row['id'];
                            $name[] = $row['name'];
                            $age[] = $row['age'];
                          }
                    }
  */
  
  
  
}
?>
