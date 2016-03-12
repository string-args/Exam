
 <%@page contentType="text/html" pageEncoding="UTF-8"%>
<html>
		

    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Upload File</title>
    </head>
	<body>

        <div>
            <h3> Choose File to Upload: </h3>
            <form action="FileUploadHandler" method="post" enctype="multipart/form-data">
                <input type="file" name="uploadfile" />
                <input type="submit" value="upload" />
            </form>          
        </div>

	<body>

</html>