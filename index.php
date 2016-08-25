<html>

<head>
</head>

<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
<body>
<div class="container">
    <div class="jumbotron">
        <h1 align="center" style="font-family:Comic Sans MS">Alexa++</h1>
        <p align="center" style="font-family:Comic Sans MS">Cuz we don't need no API :/</p>
    </div>

    <form method="get" action="index.php">
        <div class="form-group">
            <label for="link">Enter number of links</label>
            <input type="text" class="form-control" id="link" name="userinput">
        </div>
<!--          <input type="button" id="submit" class="btn btn-primary" value="submit" onclick="GetURLs()">-->
        <input type="submit" name="submit" id="submit" class="btn btn-primary" value="submit" onclick="GetURLs()">
    </form>
</div>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/2.2.0/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</body>
</html>

<?php
/**
 * Created by PhpStorm.
 * User: aftab
 * Date: 8/23/2016
 * Time: 4:09 PM
 */

//helper function to get text between two pointers in a string
function GetBetween($var1="",$var2="",$pool){
    $temp1 = strpos($pool,$var1)+strlen($var1);
    $result = substr($pool,$temp1,strlen($pool));
    $dd=strpos($result,$var2);
    if($dd == 0){
        $dd = strlen($result);
    }
    return substr($result,0,$dd);
}

/* getting n urls from http://www.alexa.com/topsites/global */
function GetURLs($n)
{
    $allURLs = array();
    $linkPattern = "<a href=\"/siteinfo/";
    $count = 0;
    $pageNumber = 0;

    while(1)
    {
        $pageURL = "http://www.alexa.com/topsites/global;".$pageNumber;
        $file = fopen($pageURL,"r");
        $matchCount = 0;
        while ( ($line = fgets($file)) !== false)
        {
            /* <li class="site-listing"><div class="count">2</div><div class="desc-container"><p class="desc-paragraph"><a href="/siteinfo/youtube.com">Youtube.com</a> */
            if ((strpos($line, $linkPattern) !== false))
            {
                $url = substr(GetBetween("<a href=\"/siteinfo/","</a>",$line), strpos(GetBetween("<a href=\"/siteinfo/","</a>",$line), ">") + 1);
//                print($url);
                array_push($allURLs,$url);
                $count++;

                if($count == $n)
                    return $allURLs;
            }
        }

        $pageNumber++;
        fclose($file);
    }
}

//if(isset($_GET['userinput']))
//{
//    $data = GetURLs($_GET['userinput']);
////    echo json_encode($data);
//
//    /* putting it in a table */
//    echo "<table name=\"myTable\" id=\"myTable\" width=70% border=1 cellspacing=0 cellpadding=0>";
//    echo "<tr><th>Serial</th><th>Site</th></tr>";
//    for($i = 0;$i < count($data);$i++)
//    {
//        $serial = $i + 1;
//        echo "<tr>"."<td>".$serial."</td>"."<td>".$data[$i]."</td>"."</tr>";
//    }
//    echo "</table>";
//}

if(isset($_GET['userinput']))
{
    $input = $_GET['userinput'];
    if((int) $input <= 0)
        echo "input needs to be a positive number!";
    else
    {
        $data = GetURLs($_GET['userinput']);
        // echo json_encode($data);

        /* putting it in a table */
        echo "<div class=\"row\">";
        echo "<div class=\"col-md-6 col-md-offset-2\">";
        //    echo "<table name=\"myTable\" id=\"myTable\" width=70% border=1 cellspacing=0 cellpadding=0>";
        echo "<table name=\"myTable\" id=\"myTable\" width=70% border=1 cellspacing=0 cellpadding=0 class=\"table\">";
        echo "<tr><th>Serial</th><th>Site</th></tr>";
        for($i = 0;$i < count($data);$i++)
        {
            $serial = $i + 1;
            echo "<tr>"."<td>".$serial."</td>"."<td>".$data[$i]."</td>"."</tr>";
        }
        echo "</table>";
        echo "</div>";
        echo "</div>";
    }


}
?>
