@echo off
:: gradle build
set fileName=Avalon-1.2.2
echo Handling %fileName%.zip, continue?
pause
xcopy /Y /Q .\build\distributions\%fileName%.zip .
set tempDir=%fileName%\bin
set tmpDir=%fileName%
mkdir %tempDir%
xcopy /E /Y /I /Q .\data %tempDir%\data
xcopy /E /Y /I /Q .\res %tempDir%\res
xcopy /Y /Q .\README.md %tmpDir%
xcopy /Y /Q .\LICENSE %tmpDir%
xcopy /Y /Q .\ShowMsg.json %tempDir%
xcopy /Y /Q .\config_example.json %tempDir%
xcopy /Y /Q .\group_example.json %tempDir%
bc a %fileName%.zip %tmpDir%
rd /S /Q %tmpDir%
echo Finished dist zip!
pause