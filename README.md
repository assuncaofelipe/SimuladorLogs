## SIMULANDO LOGS
O dispositivo/emulador precisa está com root.

## PERMISSÕES
Para capturar e gerar logs com maior profundidade, é preciso das permissões: READ_LOGS e PACKAGE_USAGE_STATS com acesso ao root.

## DANDO PERMISSÃO NOS PACOTES

Dado acesso ao root no device, executar no terminal os seguintes comandos:

``` adb shell pm grant home.felipe.topswaptest android.permission.READ_LOGS && adb shell pm grant gertec.sonar.agente android.permission.PACKAGE_USAGE_STATS ```