How to use:

    unzip the procedureTest.zip to desire location, it contains a jar file and a app.properties file.
    modify the app.properties file with suitable inputs like database url, user name, password to connect to Oracle db
    just modify the procedure name in the property oracle.preprocedure.name ={ call GET_IMEI_IMSI_MSIDN(?,?,?,?,?) }  and replace with your procedure name (highlighted in red text),
    This application assumes 1 param is in parameter
    count your out parameter and you need to add or remove question marks as number of out parameter + 1
    update number.out.param to your out parameter count

now your application is configured and ready to run by following command line (change your directory to .

                java -cp procedureTest.jar com.altamides.ConnectionTest <IN PARAM>

                example: java -cp procedureTest.jar com.altamides.ConnectionTest 3515540502583200

You need to have java 11 to run this application.
Test for Oracle db procedure with 1 input parameter