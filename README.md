# PRÁCTICA 3ª EVALUACIÓN
Desde el ayuntamiento de Zaragoza, nos piden que cambiemos su documento de estaciones de bicicletas.
El documento original es aparcamiento-bicicleta.xml. </br>
El documento final será aparcamiento.xml, que estará ordenado por número de bicicletas, y en caso de empate, por calle. </br>
Aquellas estaciones que tengan 0 bicis no se guardarán en el nuevo documento.</br>
El documento actualizado está en la <a href="https://www.zaragoza.es/sede/servicio/urbanismo-infraestructuras/equipamiento/aparcamiento-bicicleta.xml" target="_blank">siguiente página del ayuntamiento.</a></br>
El formato de salida del nuevo documento será el siguiente:
```
<?xml version="1.0" encoding="UTF-8"?>
<estaciones>
    <estacion id="124">
        <calle>Via Univérsitas - Avda. Duquesa Villahermosa</calle>
        <bicis>2</bicis>
        <anclajes>19</anclajes>
    </estacion>
....
</estaciones>
```
Las estaciones que tienen 0 bicicletas se guardarán en un fichero llamado llevar_bicis.dat, en el que se guardará la</br>
fecha y la hora registrada (LocalDateTime.now()) y el número de la estación (como cadena alfanumérica).</br>
Para guardar  los datos utilizaremos DataOutputStream. </br>
Dicho fichero no se borrará nunca, se mantendrá con los datos de las ejecuciones anteriores.</br>
Todos los ficheros, xml y txt, estarán en la carpeta ficheros.</br>
